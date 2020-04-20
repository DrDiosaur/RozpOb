import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PunktB {
    static int gardenSize = 5;
    static boolean[][] garden = new boolean[gardenSize][gardenSize];

    public static void main(String[] argv){
        ReadWriteLok rwLock = new ReadWriteLok();
        for (int i = 0; i < gardenSize; i++) {
            for (int j = 0; j < gardenSize; j++) {
                garden[i][j] = true;
            }
        }
       new Nature(rwLock).start();
       new Gardener(rwLock).start();
       new Monitor1(rwLock).start();
       new Monitor2(rwLock).start();
    }

    static class ReadWriteLok {
        private AtomicBoolean isWriteLocked;
        private AtomicBoolean isReadLocked;

        ReadWriteLok(){
            isWriteLocked = new AtomicBoolean(false);
            isReadLocked = new AtomicBoolean(false);
        }

        public synchronized void writeLock(){
            while (isWriteLocked.get() && isReadLocked.get()){

            }
            isWriteLocked.set(true);
        }

        public void writeUnlock()
        {
            isWriteLocked.set(false);
        }

        public synchronized void readLock(){
            while (isWriteLocked.get()){

            }
            isReadLocked.set(true);
        }

        public void readUnlock(){
            isReadLocked.set(false);
        }
    }

    static class Gardener extends Thread{
        ReadWriteLok rwLock;

        Gardener(ReadWriteLok rwLock){
            this.rwLock = rwLock;
        }

        @Override
        public void run() {
            while (!interrupted()){
                for (int i = 0; i < gardenSize; i++) {
                    for (int j = 0; j < gardenSize; j++) {
                        //System.out.println("Gardener checked plant[" + i + ", " + j + "]");
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        rwLock.writeLock();
                        try{
                            if (!garden[i][j]){
                                garden[i][j] = true;
                                System.out.println("Gardener fixed plant[" + i + ", " + j + "]");
                            }
                        } finally {
                            rwLock.writeUnlock();
                        }

                    }
                }
            }
        }
    }

    static class Nature extends Thread{
        ReadWriteLok rwLock;

        Nature(ReadWriteLok rwLock){
            this.rwLock = rwLock;
        }

        @Override
        public void run() {
            Random random = new Random();
            int w, h;
            while (!interrupted()){
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                rwLock.writeLock();
                try {
                    w = random.nextInt(gardenSize);
                    h = random.nextInt(gardenSize);
                    garden[w][h] = !garden[w][h];
                    System.out.println("Nature changed state of plant[" + w + ", " + h + "]");
                } finally {
                    rwLock.writeUnlock();
                }
            }
        }
    }

    static class Monitor1 extends Thread{
        ReadWriteLok rwLock;

        Monitor1(ReadWriteLok rwLock){
            this.rwLock = rwLock;
        }

        @Override
        public void run() {
            try {
                File file = new File("Monitor.txt");
                if(file.exists()){
                    file.delete();
                }
                BufferedWriter writer = new BufferedWriter(new FileWriter("Monitor.txt", true));
                String string;
                while (!interrupted()){
                    sleep(2500);
                    rwLock.readLock();
                    try{
                        for (int i = 0; i < gardenSize; i++) {
                            for (int j = 0; j < gardenSize; j++) {
                                if(garden[i][j]){
                                    string = " <Plant[" + i + ", " + j + "] blooms> ";
                                }
                                else {
                                    string = " <Plant[" + i + ", " + j + "] is wilted> ";
                                }
                                writer.append(string);
                            }
                            writer.append("\n");
                        }
                        writer.append("\n\n\n");
                    } finally {
                        rwLock.readUnlock();
                    }
                }
                writer.close();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    static class Monitor2 extends Thread{
        ReadWriteLok rwLock;

        Monitor2(ReadWriteLok rwLock){
            this.rwLock = rwLock;
        }

        @Override
        public void run() {
            String string;
            while (!interrupted()){
                try {
                    sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                rwLock.readLock();
                try {
                    for (int i = 0; i < gardenSize; i++) {
                        for (int j = 0; j < gardenSize; j++) {
                            if(garden[i][j]){
                                string = " <Plant[" + i + ", " + j + "] blooms> ";
                            }
                            else {
                                string = " <Plant[" + i + ", " + j + "] is wilted> ";
                            }
                            System.out.print(string);
                        }
                        System.out.println();
                    }
                    System.out.println("\n");
                } finally {
                    rwLock.readUnlock();
                }
            }
        }
    }

}
