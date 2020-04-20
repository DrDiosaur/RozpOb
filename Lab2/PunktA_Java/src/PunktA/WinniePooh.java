package PunktA;

public class WinniePooh {
    private volatile static boolean[][] hives;
    private volatile static boolean found = false;
    private volatile static int coordX = -999, coordY = -999;
    private volatile static Integer lastCoord = 4;

    private static final int SIZE = 100;

    static class FlockThread extends Thread{
        int col, num;

        FlockThread(int col, int num)
        {
            this.col = col;
            this.num = num;
        }

        public void run(){
            while (col < SIZE)
            {
                System.out.println("Flock number " + num + " started seeking");
                for(int i = 0; i < 10; i++)
                {
                    if(hives[col][i]){
                        found = true;
                        coordX = col;
                        coordY = i;
                    }
                    System.out.println("Flock number " + num + " checked hive [" + col + ", " + i + "]");
                }

                synchronized (lastCoord)
                {
                    if(found)
                    {
                        break;
                    }
                    col = ++lastCoord;
                }
            }
        }
    }

    public static void main(String[] argv)
    {
        hives = new boolean[SIZE][10];
        hives[90][2] = true;

        for(int i = 0; i < 5; i++)
        {
            new FlockThread(i, i+ 1).start();
        }

        new Thread(() -> {
            while (true)
            {
                if(coordX != -999 && coordY != -999) {
                    System.out.println("Winnie Pooh was found in hive with coordinates: [" + coordX + ", " + coordY + "]");
                    break;
                }
            }
        }).start();
    }
}
