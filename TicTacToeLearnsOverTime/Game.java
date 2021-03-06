/**
 * Write a description of class Game here.
 *
 * @author Eli Exner and Troy Czapar
 * @version 4/30/20
 */
 
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
public class Game
{ 
    
    //Graphics
   private JFrame f = new JFrame();
   private ImageIcon imgO = new ImageIcon("O.png");
   private ImageIcon imgX = new ImageIcon("X.png");
   private JButton[] buttons = new JButton[9];      
   private boolean waitingForButton = false;
   private int buttonReturned = -1;
        
    public String emptyBoard()
    {
        return "123456789";
    }
    
    public String xPlays(String b, int x)
    {
        try 
        {
            b = b.substring(0, x - 1) + "x" + b.substring(x);
        }
        catch(Exception e)
        {
            System.err.println("illegal move, passed in" + x);
        }
        return b;
    }
    public String oPlays(String b, int x)
    {
        try 
        {
            b = b.substring(0, x - 1) + "o" + b.substring(x);
        }
        catch(Exception e)
        {
            System.err.println("illegal move, passed in" + x);
        }
        return b;
    }
    
    /* printGame
     * This is the most important method. It shows the actual game board and waits for a response.
     */
    public void printGame(String bd)
    {
        String b = bd;
        if (buttons[0] == null)
        {
            for (int i = 0; i < buttons.length; i++)
            {
                buttons[i] = new JButton();
            }
        }
        for (JButton butt: buttons)
        {
            f.add(butt);
        }
        for (int i = 0; i < buttons.length; i++)
        {
            if(b.substring(i, i+1).equalsIgnoreCase("x"))
            {
                buttons[i].setIcon(imgX);
            }
            else if(b.substring(i, i+1).equalsIgnoreCase("o"))
            {
                buttons[i].setIcon(imgO);
            }
            else
            {
                buttons[i].setIcon(null);
            }
        }
        
        buttons[0].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    if(waitingForButton)
                    {
                    buttonReturned = 1;
                    waitingForButton = false;
                    }
                }
            });
        buttons[1].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    if(waitingForButton)
                    {
                    buttonReturned = 2;
                    waitingForButton = false;
                    }
                }
            });
        buttons[2].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    if(waitingForButton)
                    {
                    buttonReturned = 3;
                    waitingForButton = false;
                    }
                }
            });
        buttons[3].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    if(waitingForButton)
                    {
                    buttonReturned = 4;
                    waitingForButton = false;
                    }
                }
            });
        buttons[4].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    if(waitingForButton)
                    {
                    buttonReturned = 5;
                    waitingForButton = false;
                    }
                }
            });
        buttons[5].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    if(waitingForButton)
                    {
                    buttonReturned = 6;
                    waitingForButton = false;
                    }
                }
            });
        buttons[6].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    if(waitingForButton)
                    {
                    buttonReturned = 7;
                    waitingForButton = false;
                    }
                }
            });
        buttons[7].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    if(waitingForButton)
                    {
                    buttonReturned = 8;
                    waitingForButton = false;
                    }
                }
            });
        buttons[8].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    if(waitingForButton)
                    {
                    buttonReturned = 9;
                    waitingForButton = false;
                    }
                }
            });
        f.setLayout(new GridLayout(3,3));
        f.setBounds(0, 0, 500, 500);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
    /* printChances
     *  This is never called, but if you uncomment some code in Brutus.java you can see it work.
     */
    public void printChances(String bd)
    {
        String b = bd;
        b = removeNegative1(b);
        System.out.println("Chance of moving to each spot:");
        System.out.println("[" + b.substring(0,1) + "][" + b.substring(1,2) + "][" + b.substring(2,3) + "]");
        System.out.println("[" + b.substring(3,4) + "][" + b.substring(4,5) + "][" + b.substring(5,6) + "]");
        System.out.println("[" + b.substring(6,7) + "][" + b.substring(7,8) + "][" + b.substring(8,9) + "]");
    }
    public int waitForButton()
    {
        waitingForButton = true;
        while (true){
            if (!waitingForButton)
            {
                return buttonReturned;
            } else {
                System.out.print("");//This line is necessary
            }
        } //waits until waitingForButton = false
    }
    
    /*
     * remove all the 1s after - that way, a value of -1 turns into a single-character: "-"
     */
    public static String removeNegative1 (String str)
    {
        for(int i = 0; i < str.length() - 1; i++)
        {
            if (str.substring(i, i + 2).equals("-1"))
            {
                str = str.substring(0, i + 1) + str.substring (i + 2);
            }
        }
        return str;
    }  
    public void gameOver()
    {
    	f.dispose();
    }
}
