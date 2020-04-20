import java.io.*;
import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PunktA {
    public static void main(String[] argv) throws IOException {
        String filename = "Database.txt";
        ReadWriteLock rwLock = new ReentrantReadWriteLock();
        new UserDeleteAdd(filename, rwLock).start();
        new SearchingByName(filename, rwLock).start();
        new SearchingByPhone(filename, rwLock).start();
    }
}

class UserDeleteAdd extends Thread{
    String fileName;
    ReadWriteLock rwLock;

    UserDeleteAdd(String fileName, ReadWriteLock rwLock){
        this.fileName = fileName;
        this.rwLock = rwLock;
    }

    @Override
    public void run() {
        Random random = new Random();
        String str = "";
        try {
            while (!interrupted()){
                sleep(5000);
                int w_or_r = random.nextInt(2);
                int countLines = 0;

                rwLock.readLock().lock();
                BufferedReader reader = new BufferedReader(new FileReader(fileName));
                while (reader.readLine() != null){
                    countLines++;
                }
                reader.close();
                rwLock.readLock().unlock();

                if (w_or_r == 0){
                    rwLock.writeLock().lock();
                    System.out.println("Attempting to add");
                    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
                    str = "Walker Paul Fatherovich " + countLines + " --- 38093214" + countLines + " \n";
                    writer.append(str);
                    //sleep(1000);
                    System.out.println("Added " + str);
                    writer.close();
                    rwLock.writeLock().unlock();
                    System.out.println("Added successfully");
                }
                else {
                    if (countLines == 0) continue;
                    int lineToDelete = random.nextInt(countLines);
                    BufferedWriter writer = new BufferedWriter(new FileWriter("temp.txt", true));
                    reader = new BufferedReader(new FileReader(fileName));

                    rwLock.writeLock().lock();
                    System.out.println("Attempting to delete");
                    for (int i = 0; i < countLines; i++) {
                        String line = reader.readLine() + "\n";
                        if (i == lineToDelete){
                            str = line;
                            line = "";
                        }
                        writer.append(line);
                    }
                    reader.close();
                    writer.close();
                    //sleep(1000);
                    System.out.println("Deleted " + str);

                    File file = new File(fileName);
                    if (!file.delete()) {
                        System.out.println("Could not delete file");
                        return;
                    }
                    File tempFile = new File("temp.txt");
                    if (!tempFile.renameTo(file)){
                        System.out.println("Could not rename file");
                    }
                    rwLock.writeLock().unlock();

                    System.out.println("Deleted successfully");
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class SearchingByName extends Thread{
    private String fileName;
    private ReadWriteLock rwLock;

    SearchingByName(String fileName, ReadWriteLock rwLock){
        this.fileName = fileName;
        this.rwLock = rwLock;
    }

    @Override
    public void run() {
        try {
            Random random = new Random();
            String name, phone;
            int countLines;
            while (!interrupted()){
                sleep(2000);
                countLines = 0;
                phone = "";

                rwLock.readLock().lock();
                BufferedReader reader = new BufferedReader(new FileReader(fileName));
                while (reader.readLine() != null){
                    countLines++;
                }
                reader.close();

                name = "Walker Paul Fatherovich " + random.nextInt(countLines) + " ";
                reader = new BufferedReader(new FileReader(fileName));
                for (int i = 0; i < countLines; i++) {
                    String line = reader.readLine();
                    if (line.contains(name)){
                        int index = line.indexOf("- 3") + 2;
                        phone = line.substring(index);
                    }
                }
                reader.close();
                rwLock.readLock().unlock();

                if (phone.equals("")){
                    System.out.println("User with name " + name + "is not found");
                }else{
                    System.out.println("User with name " + name + "has phone number: " + phone);
                }
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}

class SearchingByPhone extends Thread{
    private String fileName;
    private ReadWriteLock rwLock;

    SearchingByPhone(String fileName, ReadWriteLock rwLock){
        this.fileName = fileName;
        this.rwLock = rwLock;
    }

    @Override
    public void run() {
        try {
            Random random = new Random();
            String name, phone;
            int countLines;
            while (!interrupted()){
                sleep(1000);
                countLines = 0;
                name = "";

                rwLock.readLock().lock();
                BufferedReader reader = new BufferedReader(new FileReader(fileName));
                while (reader.readLine() != null){
                    countLines++;
                }
                reader.close();

                phone = "38093214" + random.nextInt(countLines) + " ";
                reader = new BufferedReader(new FileReader(fileName));
                for (int i = 0; i < countLines; i++) {
                    String line = reader.readLine();
                    if (line.contains(phone)){
                        int index = line.indexOf(" -");
                        name = line.substring(0, index);
                    }
                }
                reader.close();
                rwLock.readLock().unlock();
                if (name.equals("")){
                    System.out.println("User with phone " + phone + "is not found");
                }else{
                    System.out.println("User with phone " + phone + "has name: " + name);
                }
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
