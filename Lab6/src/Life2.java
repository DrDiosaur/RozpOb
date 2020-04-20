import javax.swing.*;
import java.awt.*;
import java.util.concurrent.*;

import static java.lang.Thread.sleep;

public class Life2 extends Container{
    private JPanel panel1;
    private Cell[][] cells;
    private JPanel[][] cellsView;
    private static int SIZE = 30;
    private static JFrame jf;

    public Life2(){
        panel1 = new JPanel();
        panel1.setLayout(new GridLayout(SIZE, SIZE, 2, 2));
        panel1.setBackground(Color.GRAY);
        createCells();
        CyclicBarrier barrier = new CyclicBarrier(4, new CellPrinter());
        Semaphore semaphore = new Semaphore(1);
        new LifeChanger(semaphore, barrier, Color.GREEN).start();
        new LifeChanger(semaphore, barrier, Color.RED).start();
        new LifeChanger(semaphore, barrier, Color.BLUE).start();
        new LifeChanger(semaphore, barrier, Color.YELLOW).start();
    }

    static void createGUI(){
        jf = new JFrame("Life");
        jf.setContentPane(new Life2().panel1);
        jf.setLocation(400, 100);
        jf.setMinimumSize(new Dimension(SIZE*23, SIZE*23));
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.pack();
        jf.setVisible(true);
    }

    private void createCells(){
        cells = new Cell[SIZE][SIZE];


        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                cells[i][j] = new Cell(false, Color.BLACK);
            }
        }

        initPopulation();

        cellsView = new JPanel[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                cellsView[i][j] = new JPanel();
                cellsView[i][j].setSize(500, 500);
                if (cells[i][j].isAlive()){
                    cellsView[i][j].setBackground(cells[i][j].getColor());
                    //System.out.print(" [#] ");
                } else {
                    cellsView[i][j].setBackground(cells[i][j].getColor());
                    //System.out.print(" [] ");
                }
                panel1.add(cellsView[i][j]);
            }
            //System.out.println();
        }

    }

    void initPopulation(){
        //        cells[7][5].makeAlive();
//        cells[7][6].makeAlive();
//        cells[7][7].makeAlive();
//        cells[7][8].makeAlive();

        cells[2][2].makeAlive();
        cells[2][2].setColor(Color.GREEN);
        cells[2][3].makeAlive();
        cells[2][3].setColor(Color.GREEN);
        cells[2][4].makeAlive();
        cells[2][4].setColor(Color.GREEN);
        cells[2][5].makeAlive();
        cells[2][5].setColor(Color.GREEN);
        cells[2][SIZE-3].makeAlive();
        cells[2][SIZE-3].setColor(Color.RED);
        cells[2][SIZE-4].makeAlive();
        cells[2][SIZE-4].setColor(Color.RED);
        cells[2][SIZE-5].makeAlive();
        cells[2][SIZE-5].setColor(Color.RED);
        cells[2][SIZE-6].makeAlive();
        cells[2][SIZE-6].setColor(Color.RED);
        cells[SIZE-3][2].makeAlive();
        cells[SIZE-3][2].setColor(Color.BLUE);
        cells[SIZE-3][3].makeAlive();
        cells[SIZE-3][3].setColor(Color.BLUE);
        cells[SIZE-3][4].makeAlive();
        cells[SIZE-3][4].setColor(Color.BLUE);
        cells[SIZE-3][5].makeAlive();
        cells[SIZE-3][5].setColor(Color.BLUE);
        cells[SIZE-3][SIZE-3].makeAlive();
        cells[SIZE-3][SIZE-3].setColor(Color.YELLOW);
        cells[SIZE-3][SIZE-4].makeAlive();
        cells[SIZE-3][SIZE-4].setColor(Color.YELLOW);
        cells[SIZE-3][SIZE-5].makeAlive();
        cells[SIZE-3][SIZE-5].setColor(Color.YELLOW);
        cells[SIZE-3][SIZE-6].makeAlive();
        cells[SIZE-3][SIZE-6].setColor(Color.YELLOW);

        cells[9][9].makeAlive();
        cells[9][9].setColor(Color.BLUE);
        cells[9][10].makeAlive();
        cells[9][10].setColor(Color.BLUE);
        cells[9][11].makeAlive();
        cells[9][11].setColor(Color.BLUE);
        cells[9][12].makeAlive();
        cells[9][12].setColor(Color.BLUE);
        cells[10][9].makeAlive();
        cells[10][9].setColor(Color.BLUE);
        cells[11][9].makeAlive();
        cells[11][9].setColor(Color.BLUE);
        cells[12][9].makeAlive();
        cells[12][9].setColor(Color.BLUE);

        cells[10][10].makeAlive();
        cells[10][10].setColor(Color.GREEN);
        cells[10][11].makeAlive();
        cells[10][11].setColor(Color.GREEN);
        cells[10][12].makeAlive();
        cells[10][12].setColor(Color.GREEN);
        cells[11][10].makeAlive();
        cells[11][10].setColor(Color.GREEN);
        cells[11][11].makeAlive();
        cells[11][11].setColor(Color.GREEN);
        cells[11][12].makeAlive();
        cells[11][12].setColor(Color.GREEN);
        cells[12][10].makeAlive();
        cells[12][10].setColor(Color.GREEN);
        cells[12][11].makeAlive();
        cells[12][11].setColor(Color.GREEN);
        cells[12][12].makeAlive();
        cells[12][12].setColor(Color.GREEN);
        cells[13][13].makeAlive();
        cells[13][13].setColor(Color.GREEN);
        cells[13][14].makeAlive();
        cells[13][14].setColor(Color.GREEN);
        cells[13][15].makeAlive();
        cells[13][15].setColor(Color.GREEN);
        cells[14][13].makeAlive();
        cells[14][13].setColor(Color.GREEN);
        cells[14][14].makeAlive();
        cells[14][14].setColor(Color.GREEN);
        cells[14][15].makeAlive();
        cells[14][15].setColor(Color.GREEN);
        cells[15][13].makeAlive();
        cells[15][13].setColor(Color.GREEN);
        cells[15][14].makeAlive();
        cells[15][14].setColor(Color.GREEN);
        cells[15][15].makeAlive();
        cells[15][15].setColor(Color.GREEN);

        cells[16][13].makeAlive();
        cells[16][13].setColor(Color.RED);
        cells[16][15].makeAlive();
        cells[16][15].setColor(Color.RED);
        cells[16][14].makeAlive();
        cells[16][14].setColor(Color.RED);
        cells[16][16].makeAlive();
        cells[16][16].setColor(Color.RED);
        cells[15][16].makeAlive();
        cells[15][16].setColor(Color.RED);
        cells[14][16].makeAlive();
        cells[14][16].setColor(Color.RED);
        cells[13][16].makeAlive();
        cells[13][16].setColor(Color.RED);

//        cells[15][15].makeAlive();
//        cells[15][15].setColor(Color.RED);
//        cells[15][16].makeAlive();
//        cells[15][16].setColor(Color.RED);
//        cells[15][17].makeAlive();
//        cells[15][17].setColor(Color.RED);
//        cells[16][15].makeAlive();
//        cells[16][15].setColor(Color.RED);
//        cells[16][16].makeAlive();
//        cells[16][16].setColor(Color.RED);
    }

    void printCells(){
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (cells[i][j].isAlive()){
                    cellsView[i][j].setBackground(cells[i][j].getColor());
                    //System.out.print(" [#] ");
                } else {
                    cellsView[i][j].setBackground(cells[i][j].getColor());
                    //System.out.print(" [] ");
                }
                cellsView[i][j].repaint();
            }
            //System.out.println();
        }
    }

    class CellPrinter implements Runnable{
        @Override
        public void run() {
            try {
                //System.out.println("0------------------");
                sleep(1000);
                for (int i = 0; i < SIZE; i++) {
                    for (int j = 0; j < SIZE; j++) {
                        cells[i][j] = cells[i][j].getNextState();
                    }
                }
                printCells();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Cell{
        private boolean alive;
        private Cell nextState;
        private Color color;
        private boolean changed;

        Cell(boolean alive, Color color){
            this.color = color;
            this.alive = alive;
            changed = false;
        }

        public boolean isChanged() {
            return changed;
        }

        public void setChanged(boolean changed) {
            this.changed = changed;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }

        public void makeAlive(){
            alive = true;
        }

        public void makeDead(){
            alive = false;
        }

        public boolean isAlive() {
            return alive;
        }

        public void setNextState(Cell nextState) {
            this.nextState = nextState;
        }

        public Cell getNextState() {
            return nextState;
        }
    }

    class LifeChanger extends Thread{
        CyclicBarrier barrier;
        Color color;
        Semaphore semaphore;

        LifeChanger(Semaphore semaphore, CyclicBarrier barrier, Color color){
            this.barrier = barrier;
            this.color = color;
            this.semaphore = semaphore;
        }

        void checkCells() throws InterruptedException {
            for (int j = 0; j < SIZE; j++) {
                for (int i = 0; i < SIZE; i++){
                    int numOfAlive = 0;
                    if(j == 0){
                        if (i == 0){
                            numOfAlive = checkTopLeftCell(j, i, color);
                        } else if (i == SIZE-1){
                            numOfAlive = checkTopRightCell(j, i, color);
                        } else {
                            numOfAlive = checkTopCell(j, i, color);
                        }
                    } else if (j == SIZE-1){
                        if (i == 0){
                            numOfAlive = checkBotLeftCell(j, i, color);
                        } else if(i == SIZE-1){
                            numOfAlive = checkBotRightCell(j, i, color);
                        } else {
                            numOfAlive = checkBotCell(j, i, color);
                        }
                    } else {
                        if (i == 0){
                            numOfAlive = checkLeftCell(j, i, color);
                        } else if (i == SIZE-1){
                            numOfAlive = checkRightCell(j, i, color);
                        } else {
                            numOfAlive = checkMidCell(j, i, color);
                        }
                    }

                    //System.out.println("Cell["+line+"]["+i+"] has "+numOfAlive+" alive cells around");

                    semaphore.acquire();
                    if (!cells[j][i].isChanged()){
                        cells[j][i].setNextState(new Cell(false, Color.BLACK));
                        if (cells[j][i].isAlive() && cells[j][i].getColor() == color){
                            if (numOfAlive >= 2 && numOfAlive <= 3){
                                cells[j][i].setNextState(new Cell(true, color));
                                cells[j][i].setChanged(true);
                            } else {
                                cells[j][i].setNextState(new Cell(false, Color.BLACK));
                                cells[j][i].setChanged(true);
                            }
                        } else {
                            if (numOfAlive == 3){
                                cells[j][i].setNextState(new Cell(true, color));
                                cells[j][i].setChanged(true);
                            }
                        }
                    }
                    semaphore.release();
                }
            }

        }

        int checkTopLeftCell(int j, int i, Color color){
            int numOfAlive = 0;

            if(cells[j][i + 1].isAlive() && cells[j][i + 1].getColor() == color){
                numOfAlive++;
            }

            if (cells[j+1][i].isAlive() && cells[j+1][i].getColor() == color){
                numOfAlive++;
            }

            if (cells[j+1][i+1].isAlive() && cells[j+1][i + 1].getColor() == color){
                numOfAlive++;
            }

            return numOfAlive;
        }

        int checkTopRightCell(int j, int i, Color color){
            int numOfAlive = 0;

            if(cells[j][i - 1].isAlive() && cells[j][i - 1].getColor() == color){
                numOfAlive++;
            }

            if (cells[j+1][i].isAlive() && cells[j+1][i].getColor() == color){
                numOfAlive++;
            }

            if (cells[j+1][i-1].isAlive() && cells[j+1][i - 1].getColor() == color){
                numOfAlive++;
            }

            return numOfAlive;
        }

        int checkTopCell(int j, int i, Color color){
            int numOfAlive = 0;

            if(cells[j][i - 1].isAlive() && cells[j][i - 1].getColor() == color){
                numOfAlive++;
            }

            if(cells[j][i + 1].isAlive() && cells[j][i + 1].getColor() == color){
                numOfAlive++;
            }

            if (cells[j+1][i].isAlive() && cells[j+1][i].getColor() == color){
                numOfAlive++;
            }

            if (cells[j+1][i-1].isAlive() && cells[j+1][i - 1].getColor() == color){
                numOfAlive++;
            }

            if(cells[j+1][i + 1].isAlive() && cells[j+1][i + 1].getColor() == color){
                numOfAlive++;
            }

            return numOfAlive;
        }

        int checkBotLeftCell(int j, int i, Color color){
            int numOfAlive = 0;

            if(cells[j][i + 1].isAlive() && cells[j][i + 1].getColor() == color){
                numOfAlive++;
            }

            if (cells[j-1][i].isAlive() && cells[j-1][i].getColor() == color){
                numOfAlive++;
            }

            if (cells[j-1][i+1].isAlive() && cells[j-1][i + 1].getColor() == color){
                numOfAlive++;
            }

            return numOfAlive;
        }

        int checkBotRightCell(int j, int i, Color color){
            int numOfAlive = 0;

            if(cells[j][i - 1].isAlive() && cells[j][i - 1].getColor() == color){
                numOfAlive++;
            }

            if (cells[j-1][i].isAlive() && cells[j-1][i].getColor() == color){
                numOfAlive++;
            }

            if (cells[j-1][i-1].isAlive() && cells[j-1][i - 1].getColor() == color){
                numOfAlive++;
            }

            return numOfAlive;
        }

        int checkBotCell(int j, int i, Color color){
            int numOfAlive = 0;

            if(cells[j][i - 1].isAlive() && cells[j][i - 1].getColor() == color){
                numOfAlive++;
            }

            if(cells[j][i + 1].isAlive() && cells[j][i + 1].getColor() == color){
                numOfAlive++;
            }

            if (cells[j-1][i].isAlive() && cells[j-1][i].getColor() == color){
                numOfAlive++;
            }

            if (cells[j-1][i-1].isAlive() && cells[j-1][i - 1].getColor() == color){
                numOfAlive++;
            }

            if(cells[j-1][i + 1].isAlive() && cells[j-1][i + 1].getColor() == color){
                numOfAlive++;
            }

            return numOfAlive;
        }

        int checkLeftCell(int j, int i, Color color){
            int numOfAlive = 0;

            if(cells[j-1][i].isAlive() && cells[j-1][i].getColor() == color){
                numOfAlive++;
            }

            if (cells[j-1][i+1].isAlive() && cells[j-1][i + 1].getColor() == color){
                numOfAlive++;
            }

            if (cells[j][i+1].isAlive() && cells[j][i + 1].getColor() == color){
                numOfAlive++;
            }

            if (cells[j+1][i+1].isAlive() && cells[j+1][i + 1].getColor() == color){
                numOfAlive++;
            }

            if (cells[j+1][i].isAlive() && cells[j+1][i].getColor() == color){
                numOfAlive++;
            }

            return numOfAlive;
        }

        int checkRightCell(int j, int i, Color color){
            int numOfAlive = 0;

            if(cells[j-1][i].isAlive() && cells[j-1][i].getColor() == color){
                numOfAlive++;
            }

            if (cells[j-1][i-1].isAlive() && cells[j-1][i - 1].getColor() == color){
                numOfAlive++;
            }

            if (cells[j][i-1].isAlive() && cells[j][i - 1].getColor() == color){
                numOfAlive++;
            }

            if (cells[j+1][i-1].isAlive() && cells[j+1][i - 1].getColor() == color){
                numOfAlive++;
            }

            if (cells[j+1][i].isAlive() && cells[j+1][i].getColor() == color){
                numOfAlive++;
            }

            return numOfAlive;
        }

        int checkMidCell(int j, int i, Color color){
            int numOfAlive = 0;

            if(cells[j-1][i].isAlive() && cells[j-1][i].getColor() == color){
                numOfAlive++;
            }

            if(cells[j-1][i + 1].isAlive() && cells[j-1][i + 1].getColor() == color){
                numOfAlive++;
            }

            if (cells[j][i+1].isAlive() && cells[j][i + 1].getColor() == color){
                numOfAlive++;
            }

            if (cells[j+1][i+1].isAlive() && cells[j+1][i + 1].getColor() == color){
                numOfAlive++;
            }

            if(cells[j+1][i].isAlive() && cells[j+1][i].getColor() == color){
                numOfAlive++;
            }

            if (cells[j+1][i-1].isAlive() && cells[j+1][i - 1].getColor() == color){
                numOfAlive++;
            }

            if (cells[j][i-1].isAlive() && cells[j][i - 1].getColor() == color){
                numOfAlive++;
            }

            if(cells[j-1][i-1].isAlive() && cells[j-1][i - 1].getColor() == color){
                numOfAlive++;
            }

            return numOfAlive;
        }


        @Override
        public void run() {
            while (!interrupted()){
                try {
                    checkCells();
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static void main(String[] args) throws InterruptedException {
        new Thread(new Runnable() {
            public void run() {
                //JFrame.setDefaultLookAndFeelDecorated(true);
                createGUI();
            }
        }).start();
    }
}
