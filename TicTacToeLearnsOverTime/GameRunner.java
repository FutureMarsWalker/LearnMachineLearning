/**
 * Write a description of class GameRunner here.
 *
 * @author Troy Czapar and Eli Exner
 * @version 4/30/20
 */
 

import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JButton;
import javax.swing.ButtonGroup;

import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
public class GameRunner
{
  //declaring the AI for x and o
        static private Brutus xAI;
        static private Brutus oAI;    
  //declaring the booleans for whether x or o is actually played by the AI
        static private boolean oIsAI;
        static private boolean xIsAI;
 //declaring the number of games to play
        static private int gamesToPlay;
 //declaring an object used to pause the main script in a static context
        static private Object sync = new Object();
        
        
	public static void main(String args[])
    {
        gamesToPlay = 10;//initialized to default starting number of games
    	Game board = new Game();

        oIsAI = true;
        xIsAI = false;
        
        //call the popup window to start the game
        getTypeOfGame();

        xAI = new Brutus(xIsAI);
        oAI = new Brutus(oIsAI);
        
        //gamesToPlay was already set by the getTypeOfGame method
        int runs = gamesToPlay;
        //this loop plays r games
        for (int r = runs; r > 0; r--)
        {
            String bd = board.emptyBoard();
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

                    oAI.learn(4);
                    xAI.learn(4);
                    break;
                }
                else//if nobody won yet
                {
                    if (turn % 2 == 0)//if it's x's turn
                    {
                        if (xIsAI)//If AI x is on, use AI
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
                        if (oIsAI)//If o AI is on, use it
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
            
            //if a human is playing, wait a second so they can see who won before
            //moving on to the next game
            if (!(oIsAI && xIsAI))
            {
            	try {
            		synchronized(sync)
            		{
            			sync.wait(1000);
            		}
            	} catch (InterruptedException e) {
            		e.printStackTrace();
            	}
            }
            
            System.out.println();//This line is necessary
        }
        board.gameOver();
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
    			oIsAI = true;
    			xIsAI = false;
    		}
    	}
    			);
    	JRadioButton humanO = new JRadioButton("Play as O against Brutus");
    	humanO.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e)
    		{
    			oIsAI = false;
    			xIsAI = true;
    		}
    	});
    	JRadioButton computerOnly = new JRadioButton("Brutus plays against itself");
    	computerOnly.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e)
    		{
    			oIsAI = true;
    			xIsAI = true;
    		}
    	});
    	JRadioButton peopleOnly = new JRadioButton("Play against a friend");
    	peopleOnly.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e)
    		{
    			oIsAI = false;
    			xIsAI = false;
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
    	
    	//wait until "Start Game" is clicked to run any scripts after this one
    	try {
    		synchronized(j) {
    			j.wait();
    		}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
    	j.dispose();
    }
    
}

