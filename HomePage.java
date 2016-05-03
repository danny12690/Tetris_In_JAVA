/*
This class is the main class where the user can change settings as per his/her choice.

The speed of falling objects, the scoring factor, number of lines to be completed to complete a level and the
size of playable area as well as shapes can be adjusted using a slider.

The following are the comments of the previous iteration:
The interface has the following parts :

1> The motion/fall of objects stop when the mouse pointer hovers over the game
frame and a pause notification appears.
2> Left click moves the object left by one unit. 
3> Right click moves the object right by one unit.
4> The mouse wheel controls the up rotations in clockwise and counter clockwise directions/
5> The scores and level will be displayed on the right as well as the next shape that will fall.

The following are facilitated in this version of the triangular tetris :

ChangeLog :
1>The classes, HomePage.java,RestartScreen.java are settings and score dialogs respectively.

2>The methods namely lineFilled(), clearRegistered() are added so as to map the registered 
shapes as well as to check if a line is completed.

3>The method showGameOver() int ITetris.java is used to display the game over screen as well as the user's score

4>Member variables lines,score and level in ITetris.java are introduced to keep track of the user's progress.

5>Classes Tools2d.java,Point2D from java are incorporated from the text book. These classes provide
methods for POINT INSIDE POLYGON test.

6>The speed, scoring factor and size of the objects can be adjusted from the home screen.
 */


/**
 Author : Dhananjay Singh
 Net Id : dxs145530
 Utd Id : 2021250625
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
public class HomePage extends JFrame implements ActionListener
{
    Font fo=new Font("Comic Sans MS",Font.ITALIC,16);
    JFrame f=new JFrame("DANNY'S TETRIS");
    JOptionPane op=new JOptionPane();
    public JLabel l1=new JLabel("SPEED :");
    public JLabel l3=new JLabel("SCORING FACTOR: ");
    public JLabel l2=new JLabel("SIZE  :");
    public JLabel l4=new JLabel("LINES :");
    public JButton b1=new JButton("START");
    JSlider speed = new JSlider(JSlider.HORIZONTAL, 1,10,1);
    JSlider size = new JSlider(JSlider.HORIZONTAL, 1,3,1);
    JSlider scoreFactor = new JSlider(JSlider.HORIZONTAL, 1, 10,1);
    JSlider lines = new JSlider(JSlider.HORIZONTAL, 20,50,20);
    public HomePage()
    {
        f.setLayout(null);
        f.setFont(fo);
        b1.addActionListener(this);
        b1.setBounds(150,280,110,30);
        b1.setVisible(true);
        l1.setBounds(30,30,110,30);
        l1.setFont(fo);
        l1.setForeground(Color.BLACK);
        l1.setVisible(true);
        l2.setBounds(30,80,110,30);
        l2.setFont(fo);
        l2.setForeground(Color.BLACK);
        l2.setVisible(true);
        l3.setBounds(30,130,160,30);
        l3.setFont(fo);
        l3.setForeground(Color.BLACK);
        l3.setVisible(true);
        l4.setBounds(30,180,150,30);
        l4.setFont(fo);
        l4.setForeground(Color.BLACK);
        l4.setVisible(true);
        speed.setMinorTickSpacing(1);
        speed.setMajorTickSpacing(10);
        speed.setPaintTicks(true);
        speed.setPaintLabels(true);
        speed.setLabelTable(speed.createStandardLabels(9));
        speed.setBounds(200,30,110,50);
        size.setMinorTickSpacing(1);
        size.setMajorTickSpacing(1);
        size.setPaintTicks(true);
        size.setPaintLabels(true);
        size.setLabelTable(size.createStandardLabels(1));
        size.setBounds(200,80,110,50);
        scoreFactor.setBounds(200,130,110,50);
        scoreFactor.setMinorTickSpacing(1);
        scoreFactor.setMajorTickSpacing(10);
        scoreFactor.setPaintTicks(true);
        scoreFactor.setPaintLabels(true);
        scoreFactor.setLabelTable(scoreFactor.createStandardLabels(1));
        scoreFactor.setBounds(200,130,110,50);
        scoreFactor.setBounds(200,130,110,50);
        lines.setMinorTickSpacing(1);
        lines.setMajorTickSpacing(10);
        lines.setPaintTicks(true);
        lines.setPaintLabels(true);
        lines.setLabelTable(lines.createStandardLabels(10));
        lines.setBounds(200,180,110,50);
        f.add(l1);
        f.add(l2);
        f.add(l3);
        f.add(l4);
        f.add(speed);
        f.add(size);
        f.add(scoreFactor);
        f.add(lines);
        f.setVisible(true);
        f.setSize(500,400);
        f.add(b1);
        f.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==b1)
        {
            new ITetris(speed.getValue()/10.0f,lines.getValue(),size.getValue(),scoreFactor.getValue());
            f.dispose();
        }
    }
    public static void main(String args[])
    {
        HomePage hp=new HomePage();
    }
}
