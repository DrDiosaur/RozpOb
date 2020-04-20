import javax.swing.*;
import java.awt.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import static java.lang.Thread.sleep;

public class Life extends Container{
    private JPanel panel1;
    private Cell[][] cells;
    private JPanel[][] cellsView;
    private static int SIZE = 12;
    private static JFrame jf;

    public Life(){
        panel1 = new JPanel();
        panel1.setLayout(new GridLayout(SIZE, SIZE, 2, 2));
        panel1.setBackground(Color.GRAY);
        createCells();
        CyclicBarrier barrier = new CyclicBarrier(SIZE, new CellPrinter());
        for (int i = 0; i < SIZE; i++) {
            new LifeChanger(i, barrier).start();
        }
    }

    private static void createGUI(){
        jf = new JFrame("Life");
        jf.setContentPane(new Life().panel1);
        jf.setLocation(570, 150);
        jf.setMinimumSize(new Dimension(SIZE*33, SIZE*33));
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.pack();
        jf.setVisible(true);
    }

    private void createCells(){
        cells = new Cell[SIZE][SIZE];


        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                cells[i][j] = new Cell(false);
            }
        }

//        cells[7][5].makeAlive();
//        cells[7][6].makeAlive();
//        cells[7][7].makeAlive();
//        cells[7][8].makeAlive();

        cells[3][3].makeAlive();
        cells[3][4].makeAlive();
        cells[3][5].makeAlive();
        cells[4][3].makeAlive();
        cells[4][4].makeAlive();
        cells[4][5].makeAlive();
        cells[5][3].makeAlive();
        cells[5][4].makeAlive();
        cells[5][5].makeAlive();
        cells[6][6].makeAlive();
        cells[6][7].makeAlive();
        cells[6][8].makeAlive();
        cells[7][6].makeAlive();
        cells[7][7].makeAlive();
        cells[7][8].makeAlive();
        cells[8][6].makeAlive();
        cells[8][7].makeAlive();
        cells[8][8].makeAlive();

        cellsView = new JPanel[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                cellsView[i][j] = new JPanel();
                cellsView[i][j].setSize(500, 500);
                if (cells[i][j].isAlive()){
                    cellsView[i][j].setBackground(Color.GREEN);
                   //System.out.print(" [#] ");
                } else {
                    cellsView[i][j].setBackground(Color.BLACK);
                    //System.out.print(" [] ");
                }
                panel1.add(cellsView[i][j]);
            }
            //System.out.println();
        }

    }

    private void printCells(){
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (cells[i][j].isAlive()){
                    cellsView[i][j].setBackground(Color.GREEN);
                    //System.out.print(" [#] ");
                } else {
                    cellsView[i][j].setBackground(Color.BLACK);
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

        Cell(boolean alive){
            this.alive = alive;
        }

        public void makeAlive(){
            alive = true;
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
        int line;
        CyclicBarrier barrier;

        LifeChanger(int line, CyclicBarrier barrier){
            this.line = line;
            this.barrier = barrier;
        }

        void checkCells(){
            for (int i = 0; i < SIZE; i++){
                int numOfAlive = 0;
                if(line == 0){
                    if (i == 0){
                        numOfAlive = checkTopLeftCell(i);
                    } else if (i == SIZE-1){
                        numOfAlive = checkTopRightCell(i);
                    } else {
                        numOfAlive = checkTopCell(i);
                    }
                } else if (line == SIZE-1){
                    if (i == 0){
                        numOfAlive = checkBotLeftCell(i);
                    } else if(i == SIZE-1){
                        numOfAlive = checkBotRightCell(i);
                    } else {
                        numOfAlive = checkBotCell(i);
                    }
                } else {
                    if (i == 0){
                        numOfAlive = checkLeftCell(i);
                    } else if (i == SIZE-1){
                        numOfAlive = checkRightCell(i);
                    } else {
                        numOfAlive = checkMidCell(i);
                    }
                }

                //System.out.println("Cell["+line+"]["+i+"] has "+numOfAlive+" alive cells around");

                cells[line][i].setNextState(new Cell(false));
                if (cells[line][i].isAlive()){
                    if (numOfAlive >= 2 && numOfAlive <= 3){
                        cells[line][i].setNextState(new Cell(true));
                    } else {
                        cells[line][i].setNextState(new Cell(false));
                    }
                } else {
                    if (numOfAlive == 3){
                        cells[line][i].setNextState(new Cell(true));
                    }
                }
            }
        }

        int checkTopLeftCell(int i){
            int numOfAlive = 0;

            if(cells[line][i + 1].isAlive()){
                numOfAlive++;
            }

            if (cells[line+1][i].isAlive()){
                numOfAlive++;
            }

            if (cells[line+1][i+1].isAlive()){
                numOfAlive++;
            }

            return numOfAlive;
        }

        int checkTopRightCell(int i){
            int numOfAlive = 0;

            if(cells[line][i - 1].isAlive()){
                numOfAlive++;
            }

            if (cells[line+1][i].isAlive()){
                numOfAlive++;
            }

            if (cells[line+1][i-1].isAlive()){
                numOfAlive++;
            }

            return numOfAlive;
        }

        int checkTopCell(int i){
            int numOfAlive = 0;

            if(cells[line][i - 1].isAlive()){
                numOfAlive++;
            }

            if(cells[line][i + 1].isAlive()){
                numOfAlive++;
            }

            if (cells[line+1][i].isAlive()){
                numOfAlive++;
            }

            if (cells[line+1][i-1].isAlive()){
                numOfAlive++;
            }

            if(cells[line+1][i + 1].isAlive()){
                numOfAlive++;
            }

            return numOfAlive;
        }

        int checkBotLeftCell(int i){
            int numOfAlive = 0;

            if(cells[line][i + 1].isAlive()){
                numOfAlive++;
            }

            if (cells[line-1][i].isAlive()){
                numOfAlive++;
            }

            if (cells[line-1][i+1].isAlive()){
                numOfAlive++;
            }

            return numOfAlive;
        }

        int checkBotRightCell(int i){
            int numOfAlive = 0;

            if(cells[line][i - 1].isAlive()){
                numOfAlive++;
            }

            if (cells[line-1][i].isAlive()){
                numOfAlive++;
            }

            if (cells[line-1][i-1].isAlive()){
                numOfAlive++;
            }

            return numOfAlive;
        }

        int checkBotCell(int i){
            int numOfAlive = 0;

            if(cells[line][i - 1].isAlive()){
                numOfAlive++;
            }

            if(cells[line][i + 1].isAlive()){
                numOfAlive++;
            }

            if (cells[line-1][i].isAlive()){
                numOfAlive++;
            }

            if (cells[line-1][i-1].isAlive()){
                numOfAlive++;
            }

            if(cells[line-1][i + 1].isAlive()){
                numOfAlive++;
            }

            return numOfAlive;
        }

        int checkLeftCell(int i){
            int numOfAlive = 0;

            if(cells[line-1][i].isAlive()){
                numOfAlive++;
            }

            if (cells[line-1][i+1].isAlive()){
                numOfAlive++;
            }

            if (cells[line][i+1].isAlive()){
                numOfAlive++;
            }

            if (cells[line+1][i+1].isAlive()){
                numOfAlive++;
            }

            if (cells[line+1][i].isAlive()){
                numOfAlive++;
            }

            return numOfAlive;
        }

        int checkRightCell(int i){
            int numOfAlive = 0;

            if(cells[line-1][i].isAlive()){
                numOfAlive++;
            }

            if (cells[line-1][i-1].isAlive()){
                numOfAlive++;
            }

            if (cells[line][i-1].isAlive()){
                numOfAlive++;
            }

            if (cells[line+1][i-1].isAlive()){
                numOfAlive++;
            }

            if (cells[line+1][i].isAlive()){
                numOfAlive++;
            }

            return numOfAlive;
        }

        int checkMidCell(int i){
            int numOfAlive = 0;

            if(cells[line-1][i].isAlive()){
                numOfAlive++;
            }

            if(cells[line-1][i + 1].isAlive()){
                numOfAlive++;
            }

            if (cells[line][i+1].isAlive()){
                numOfAlive++;
            }

            if (cells[line+1][i+1].isAlive()){
                numOfAlive++;
            }

            if(cells[line+1][i].isAlive()){
                numOfAlive++;
            }

            if (cells[line+1][i-1].isAlive()){
                numOfAlive++;
            }

            if (cells[line][i-1].isAlive()){
                numOfAlive++;
            }

            if(cells[line-1][i-1].isAlive()){
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
        //JFrame.setDefaultLookAndFeelDecorated(true);
        new Thread(Life::createGUI).start();
    }
}
