import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormForm {
    private JPanel panel1;
    private JSlider slider1;
    private JButton start1Button;
    private JButton start2Button;
    private JButton stop2Button;
    private JButton stop1Button;
    private JLabel mes;

    int semaphor;
    Thread t1, t2;


    class T1 extends Thread {
        public void run() {
            while (!isInterrupted()) {
                if (slider1.getValue() > 0) {
                    slider1.setValue(slider1.getValue() - 1);
                }
            }
        }
    }

    class T2 extends Thread {
        public void run() {
            while (!isInterrupted()) {
                if (slider1.getValue() < 100) {
                    slider1.setValue(slider1.getValue() + 1);
                }
            }
        }
    }

    public void createGui(){
        start1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (semaphor != 1) {
                    semaphor = 1;
                    mes.setText("");
                    t1 = new T1();
                    t1.setPriority(1);
                    t1.start();
                } else {
                    mes.setText("Занято потоком");
                }
            }
        });

        start2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (semaphor != 1) {
                    semaphor = 1;
                    mes.setText("");
                    t2 = new T2();
                    t2.setPriority(1);
                    t2.start();
                } else {
                    mes.setText("Занято потоком");
                }
            }
        });

        stop1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (t1.isAlive()) {
                    mes.setText("");
                    try {
                        t1.interrupt();
                        t1.join();
                        semaphor = 0;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        stop2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (t2.isAlive()) {
                    mes.setText("");
                    try {
                        t2.interrupt();
                        t2.join();
                        semaphor = 0;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    FormForm(){
        createGui();
    }

    public static void main(String[] args) {
        JFrame jf = new JFrame("Form1");
        jf.setContentPane(new FormForm().panel1);
        jf.setLocation(666, 166);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.pack();
        jf.setVisible(true);
    }
}