package helicopterbattle;

import java.awt.Container;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Rank extends JFrame {

   
   JButton b1 = new JButton("등록");
   JButton b2 = new JButton("랭킹 보기");
   JButton b3 = new JButton("게임 종료");

   TextField tf = new TextField();

   public Rank() {

      Container c = getContentPane();

      c.add(b1);
      c.add(tf);
      c.add(b3);
      c.add(b2);

      c.setLayout(null);

      tf.setBounds(30, 30, 100, 30);
      b1.setBounds(180, 30,100, 30);
      b2.setBounds(30, 80, 100, 30);
      b3.setBounds(180, 80,100, 30);

      b1.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            try {
               BufferedWriter out = new BufferedWriter(new FileWriter("out.txt", true));
               out.write(Framework.gameTime / Framework.secInNanosec + "  " + tf.getText() + "\n");
               out.newLine();
               out.close();
            } catch (IOException e1) {
               System.err.println(e1); 
               System.exit(1);
            }
         }
      });
      b2.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            new SeeRank();
         }
      });
      b3.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            System.exit(0);
         }
      });

      setTitle("Record");
      setBounds(Framework.frameWidth/2, Framework.frameHeight/2, 300, 200);
      setResizable(false);
      setVisible(true);

   }

}