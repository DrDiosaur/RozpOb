import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Form {
    private JSlider slider1;
    private JPanel panel1;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton startButton;
    Thread t1, t2;


   public static void main(String[] argw){
       JFrame jf = new JFrame("Form1");
       jf.setContentPane(new Form().panel1);
       jf.setLocation(666, 166);
       jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
       jf.pack();
       jf.setVisible(true);
    }

    public void createGui(){
       t1 = new Thread(()->{
          while(true){
              if(slider1.getValue()>0)
              {
                  slider1.setValue(slider1.getValue()-1);
                  try {
                      Thread.sleep(100);
                  } catch (InterruptedException ex) {
                      break;
                  }
              }

          }
       });



        t2 = new Thread(()->{
            while(true){
                if(slider1.getValue()<100)
                {
                    slider1.setValue(slider1.getValue()+1);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        break;
                    }
                }

            }
        });



        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                t1.start();
                t2.start();
            }
        });

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(t1.getPriority()<10) {
                    t1.setPriority(t1.getPriority() + 1);
                    System.out.println(t1.getPriority());
                }
            }

        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(t2.getPriority()<10) {
                    t2.setPriority(t2.getPriority() + 1);
                    System.out.println(t2.getPriority());
                }
            }

        });

        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(t2.getPriority()>1) {
                    t2.setPriority(t2.getPriority() - 1);
                    System.out.println(t2.getPriority());
                }
            }

        });

        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(t1.getPriority()>1) {
                    t1.setPriority(t1.getPriority() - 1);
                    System.out.println(t1.getPriority());
                }
            }

        });
    }

    Form(){
       createGui();
    }
}

