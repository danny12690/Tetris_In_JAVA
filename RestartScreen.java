/*
This class is the interface to the game over screen that shows the user's progress in terms of Score and
level reached.
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
public class RestartScreen extends JFrame implements ActionListener 
{
    int gameOver=0;
    int score=0;
    int level=0;
    Font fo=new Font("Comic Sans MS",Font.ITALIC,16);
    JFrame f=new JFrame("Your Score!!!");
    JOptionPane op=new JOptionPane();
    public JLabel l1=new JLabel("Score :");
    public JLabel l2=new JLabel("Level :");
    public JLabel scoreLabel;
    public JLabel levelLabel;
    public JButton b1=new JButton("RESTART");
    public RestartScreen(int score,int level, int gameOver)
    {
        this.gameOver=gameOver;
        this.score=score;
        this.level=level;
        f.setLayout(null);
        l1.setBounds(30,30,110,30);
        l1.setFont(fo);
        l1.setForeground(Color.BLACK);
        l1.setVisible(true);
        l2.setBounds(30,70,110,30);
        l2.setFont(fo);
        l2.setForeground(Color.BLACK);
        l2.setVisible(true);
        scoreLabel=new JLabel(""+score);
        scoreLabel.setBounds(150,30,110,30);
        scoreLabel.setFont(fo);
        scoreLabel.setForeground(Color.BLACK);
        scoreLabel.setVisible(true);
        levelLabel=new JLabel(""+level);
        levelLabel.setBounds(150,70,110,30);
        levelLabel.setFont(fo);
        levelLabel.setForeground(Color.BLACK);
        levelLabel.setVisible(true);
        b1.setBounds(100,110,110,30);
        b1.addActionListener(this);
        b1.setVisible(true);
        f.add(l1);
        f.add(l2);
        f.add(scoreLabel);
        f.add(levelLabel);
        f.add(b1);
        f.add(scoreLabel);
        f.setSize(450,400);
        f.setVisible(true);
    }
    public int returnGameStatus()
    {
        return gameOver;
    }
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==b1)
        {
            gameOver=0;
            new HomePage();
            f.dispose();
        }
    }
}
