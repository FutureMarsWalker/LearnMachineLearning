package com;

import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
public class GameRunner
{
	//declaring the images so it can have graphics
        static private ImageIcon xWin = new ImageIcon("XWins.png");
        static private ImageIcon oWin = new ImageIcon("OWins.png");
        static private ImageIcon tie = new ImageIcon("Tie.png");
        static private ImageIcon title = new ImageIcon("Title.png");      
   //declaring the win/loss screen
        static private JFrame f = new JFrame();
        static private JButton b = new JButton();     
  //declaring the AI for x and o
        static private Brutus xAI;
        static private Brutus oAI;    
  //declaring the booleans for whether x or o is actually played by the AI
        static private boolean osAI;
        static private boolean xsAI;
 //declaring the number of games to play
        static private int gamesToPlay;
        
	@SuppressWarnings("deprecation")
	public static void main(String args[])
    {
        gamesToPlay = 10;//initialized to default starting number of games
    	Game board = new Game();
        b.setIcon(title);
        f.setBounds(0, 0, 500, 500);
        f.add(b);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    synchronized(f) {
                         f.notify();
                    }
                }
            });
        f.setVisible(true);
        
        //initialize 
        osAI = true;
        xsAI = false;
        
        //call the popup window to start the game
        getTypeOfGame();

        xAI = new Brutus(xsAI);
        oAI = new Brutus(osAI);
        
        //gamesToPlay was already set by the getTypeOfGame method
        int runs = gamesToPlay;
        //this loop plays r games
        for (int r = runs; r > 0; r--)
        {
            String bd = board.theBoard();
            int turn = 0;
            //this next loop plays a single game
            while (true)//infinite loop, uses break to escape
            {
                //check if anybody won or tied
                if(bd.substring(0, 3).equals("xxx") ||
                   bd.substring(3, 6).equals("xxx") ||
                   bd.substring(6, 9).equals("xxx") ||
                   (bd.substring(0, 1) + bd.substring(3, 4) + bd.substring(6, 7)).equals("xxx") ||
                   (bd.substring(1, 2) + bd.substring(4, 5) + bd.substring(7, 8)).equals("xxx") ||
                   (bd.substring(2, 3) + bd.substring(5, 6) + bd.substring(8, 9)).equals("xxx") ||
                   (bd.substring(0, 1) + bd.substring(4, 5) + bd.substring(8, 9)).equals("xxx") ||
                   (bd.substring(2, 3) + bd.substring(4, 5) + bd.substring(6, 7)).equals("xxx"))
                {
                    b.setIcon(xWin);
                    f.setVisible(true);
                    try {
                        synchronized(f)
                        {
                            f.wait();
                        }
                    } catch (java.lang.InterruptedException e)
                    {}
                    if (turn == 5 || turn == 6)
                    {
                        oAI.learn(1);
                        xAI.learn(7);
                    } else if (turn == 7 || turn == 8)
                    {
                        oAI.learn(2);
                        xAI.learn(6);
                    } else if (turn == 9)
                    {
                        oAI.learn(3);
                        xAI.learn(5);
                    }
                    
                    //board.gameOver(xWin);
                    break;
                }
                else if (bd.substring(0, 3).equals("ooo") ||
                   bd.substring(3, 6).equals("ooo") ||
                   bd.substring(6, 9).equals("ooo") ||
                  (bd.substring(0, 1) + bd.substring(3, 4) + bd.substring(6, 7)).equals("ooo") ||
                  (bd.substring(1, 2) + bd.substring(4, 5) + bd.substring(7, 8)).equals("ooo") ||
                  (bd.substring(2, 3) + bd.substring(5, 6) + bd.substring(8, 9)).equals("ooo") ||
                  (bd.substring(0, 1) + bd.substring(4, 5) + bd.substring(8, 9)).equals("ooo") ||
                  (bd.substring(2, 3) + bd.substring(4, 5) + bd.substring(6, 7)).equals("ooo"))
                {
                    b.setIcon(oWin);
                    f.show();
                    try {
                        synchronized(f)
                        {
                            f.wait();
                        }
                    } catch (java.lang.InterruptedException e)
                    {}
                    if (turn == 5 || turn == 6)
                    {
                        oAI.learn(7);
                        xAI.learn(1);
                    } else if (turn == 7 || turn == 8)
                    {
                        oAI.learn(6);
                        xAI.learn(2);
                    } 
                    else if (turn == 9) // Possibly this is unreachable, o can't win in 9 turns
                    {
                        oAI.learn(5);
                        xAI.learn(3);
                    }
                    break;
                }
                else if (!(bd.substring(0, 1).equals("1") || bd.substring(1, 2).equals("2") ||
                           bd.substring(2, 3).equals("3") || bd.substring(3, 4).equals("4") ||
                           bd.substring(4, 5).equals("5") || bd.substring(5, 6).equals("6") ||
                           bd.substring(6, 7).equals("7") || bd.substring(7, 8).equals("8") ||
                           bd.substring(8, 9).equals("9")))
                {
                    b.setIcon(tie);
                    f.show();
                    try {
                        synchronized(f)
                        {
                            f.wait();
                        }
                    } catch (java.lang.InterruptedException e)
                    {}
                    oAI.learn(4);
                    xAI.learn(4);
                    break;
                }
                else//if nobody won yet
                {
                    if (turn % 2 == 0)//if it's my turn
                    {
                        if (xsAI)//If AI x is on, use AI
                        {
                            int x = xAI.move(bd, true);
                            bd = board.xPlays(bd, x);
                            turn++;
                        }
                        else//Otherwise use human input
                        {
                            board.printGame(bd);
                            int x = 0;
                             //get a number and be sure not to "write on top of" another
                             while (x == 0 || bd.substring(x - 1, x).equals("x") || bd.substring(x - 1, x).equals("o"))
                             {
                                x = board.waitForButton();
                             }
                            xAI.observe(bd, x);//add this in later too
                            bd = board.xPlays(bd, x);//make the move
                            turn++;
                        }
                    }
                    else
                    {
                        if (osAI)//If o AI is on, use it
                        {
                            int x = oAI.move(bd, false);
                            bd = board.oPlays(bd, x);
                            turn++;
                        }
                        else//Otherwise use human input
                        {
                            board.printGame(bd);
                            int x = 0;
                            //get a number and be sure not to "write on top of" another
                            while (x == 0 || bd.substring(x - 1, x).equals("x") || bd.substring(x - 1, x).equals("o"))
                            {
                                x = board.waitForButton();
                            }
                            oAI.observe(bd, x);
                            bd = board.oPlays(bd, x);
                            turn++;
                        }
                    }       
                }
            }
            board.printGame(bd);
            System.out.println();//This line is necessary
        }       
    }
    
	
	/* getTypeOfGame()
	 * This is that little startup window that asks you how many games to play and such
	 */
    public static void getTypeOfGame()
    {
    	JFrame j = new JFrame();
    	j.setSize(500, 500);
    	LayoutManager l = new FlowLayout();
    	j.setLayout(l);
    	JRadioButton humanX = new JRadioButton("Play as X against Brutus");
    	humanX.setSelected(true);
    	humanX.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e)
    		{
    			osAI = true;
    			xsAI = false;
    		}
    	}
    			);
    	JRadioButton humanO = new JRadioButton("Play as O against Brutus");
    	humanO.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e)
    		{
    			osAI = false;
    			xsAI = true;
    		}
    	});
    	JRadioButton computerOnly = new JRadioButton("Brutus plays against itself");
    	computerOnly.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e)
    		{
    			osAI = true;
    			xsAI = true;
    		}
    	});
    	JRadioButton peopleOnly = new JRadioButton("Play against a friend");
    	peopleOnly.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e)
    		{
    			osAI = false;
    			xsAI = false;
    		}
    	});
    	ButtonGroup bg = new ButtonGroup();
    	bg.add(humanX);
    	bg.add(humanO);
    	bg.add(computerOnly);
    	bg.add(peopleOnly);
    	j.add(humanX);
    	j.add(humanO);
    	j.add(computerOnly);
    	j.add(peopleOnly);
    	
    	JTextField f = new JTextField("Number of games to play");
    	j.add(f);
    	
    	
    	JSpinner s = new JSpinner();//to improve graphics, maybe experiment with getModel
    	s.setValue(10);
    	s.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
			    gamesToPlay = (int) s.getValue();
			    			}
    	});
    	j.add(s);
    	
    	JButton b = new JButton("Start Game");
    	b.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e)
    		{
    			j.setVisible(false);
    			synchronized(j) {
    			j.notify();
    			}
    			
    		}
    	});
    	j.add(b);
    	j.setVisible(true);
    	try {
    		synchronized(j) {
    			j.wait();
    		}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
    }
    
}

