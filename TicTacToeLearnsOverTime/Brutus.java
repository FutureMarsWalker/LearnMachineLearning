package com;

import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
public class Brutus
{
    @SuppressWarnings("unused")
	private Game board = new Game();
	
    private ArrayList<String> thisGameInput;
    private ArrayList<Integer> thisGameOutput;
    
    private ArrayList<String> input;
    private ArrayList<Integer> output;
    private ArrayList<Integer> outputSuccess;
    private static File fileInput = new File("brutusInput.txt");
    private static File fileOutput = new File("brutusOutput.txt");
    private static File fileOutputSuccess = new File("brutusOutputSuccess.txt");
    
    private boolean randomEnabled;
    private boolean isObserver;
    
    public Brutus(boolean isObserver)
    {	
        thisGameInput = new ArrayList<String>();
        thisGameOutput = new ArrayList<Integer>();
        
        input = new ArrayList<String>();
        output = new ArrayList<Integer>();
        outputSuccess = new ArrayList<Integer>();
        
        this.isObserver = isObserver;
        
        loadMemoryFromFiles();
        if (input.size() != output.size() || input.size() != outputSuccess.size())
        {
            System.err.println("The memory files are messed up.");
            System.err.println(input.size());
            System.err.println(output.size());
            System.err.println(outputSuccess.size());
        }
        randomEnabled = true;
    }
    public int move(String bd, boolean isX)//bd is for board
    {
        if (isObserver) {
        //chance of choosing: 0=no chance, 1=not prefered, 2=neutral, 3=good
        int[] chanceOfChoosing = new int[9];
        
        //if the space is full, set the chance of choosing it to -1
        for (int i = 0; i < bd.length(); i++)
        {
            if (bd.substring(i, i + 1).equalsIgnoreCase("x") || bd.substring(i, i + 1).equalsIgnoreCase("o"))
            {
                chanceOfChoosing[i] = -1;
            } 
            else 
            {
                chanceOfChoosing[i] = 4;//if not, set it to 4 temporarily
            }
        }
        //loop through the memory
        //if the space is in memory, set chanceOfChoosing to how good of a move it is
        //WORK HERE
        int startI = 0;
        for (int i = startI; i < input.size(); i++)
        {
            if(bd.equals(input.get(i)))
            {
                //WORK HERE
                chanceOfChoosing[output.get(i)] = outputSuccess.get(i);
            }
        }
        for (int i = 0; i < startI; i++)
        {
            if(bd.equals(input.get(i)))
            {
                chanceOfChoosing[output.get(i)] = outputSuccess.get(i);
            }
        }
//        //uncomment this code to "see inside" the AI's brain
//        String stringChanceOfChoosing = "";
//        for (int value: chanceOfChoosing)
//        {
//            stringChanceOfChoosing += value;
//        }
//        System.out.println();
//        board.printChances(stringChanceOfChoosing);//
//        System.out.println();

        
        
        //find the best space(prioritizes first in list) and play there, returns -1 for error
        //pick a random order for checking all of the variables
        int[] order = {0,1,2,3,4,5,6,7,8};//all the possible moves
        if (randomEnabled)
        {
            order = getRandomOrder();
        }
        //NEW nested for loop
        for (int j = 7; j >= 0; j--)
        {
            for (int i = 0; i < chanceOfChoosing.length; i++)
            {
                if (chanceOfChoosing[order[i]] == j)
                {
                    thisGameInput.add(bd);
                    thisGameOutput.add(order[i]);
                    return order[i] + 1;
                }
            }
        }

        System.out.println("ERROR");
        return -1;
    } else {
        System.out.println("ERROR: Brutus cannot move because it is an observer");
        return -1;
    }
    }

    public void observe(String board, int move)
    {
        thisGameInput.add(board);
        thisGameOutput.add(move);
    }

    public void learn(int results)
    {
        for (int i = 0; i < thisGameInput.size(); i++)
        {
          outputSuccess.add(results);
          input.add(thisGameInput.get(i));
          output.add(thisGameOutput.get(i));
        }
        cleanMemory();
       
        thisGameInput = new ArrayList<String>();
        thisGameOutput = new ArrayList<Integer>();
        writeToFiles();
    }

    public void writeToFiles()
    {
        clearMemoryFiles();
        try 
        {

            FileWriter inWriter = new FileWriter(fileInput);
            FileWriter outWriter = new FileWriter(fileOutput);
            FileWriter outSucWriter = new FileWriter(fileOutputSuccess);
            

            for (int i = 0; i < input.size(); i++)
            {
                inWriter.write(input.get(i)); 
                inWriter.write(System.lineSeparator());
                outWriter.write(output.get(i)+ ""); 
                outWriter.write(System.lineSeparator());
                outSucWriter.write(outputSuccess.get(i)+"");
                outSucWriter.write(System.lineSeparator());
            }
            inWriter.close();
            outWriter.close();
            outSucWriter.close();
        } 
        catch (java.io.IOException e)
        {
            System.err.println("Something went wrong with the save files");
        }
    }
    //This method just loads variables from files
    private void loadMemoryFromFiles()
    {
        try
        {
             // a new scanner for each of the 4 save files
            Scanner inScan = new Scanner(fileInput);
            Scanner outScan = new Scanner(fileOutput);
            Scanner outSucScan = new Scanner(fileOutputSuccess);
            
            while(inScan.hasNextLine())
            {
                input.add(inScan.nextLine());
                try {
                    output.add(Integer.parseInt(outScan.nextLine()));
                    outputSuccess.add(Integer.parseInt(outSucScan.nextLine()));
                } catch (java.lang.NumberFormatException c)
                {
                    System.err.println("Problem: non-numerical charachter in brutusOutput.txt or brutusOutputSuccess.txt");
                }
            }
            
            inScan.close();
            outScan.close();
            outSucScan.close();
            cleanMemory();
        } catch (java.io.FileNotFoundException e)
        {
            System.out.println("Someone deleted the save files. They should be named:");
            System.out.println("brutusFailInput.txt, brutusFailOutput.txt, brutusWinOutput.txt, brutusWinInput.txt");
        }
    }
    
    //This method learning text documents
    public static void clearMemoryFiles()
    {
        try 
        {
            FileWriter inWriter = new FileWriter(fileInput, false);
            FileWriter outWriter = new FileWriter(fileOutput, false);
            FileWriter outSucWriter = new FileWriter(fileOutputSuccess, false);
            
            inWriter.close();
            outWriter.close();
            outSucWriter.close();
        } 
        catch (java.io.IOException e)
        {
            System.out.println("Something went wrong with the save files");
        }
    }
    
    /* cleanMemory()
     * This method removes duplicate memories by combining them into 1.
     * The "success value" of both memories are averaged and then rounded down.
     */
    private void cleanMemory()
    {
        int firstItemSuccess = -1;
        int lastItemSuccess = -1;
        Integer averageSuccess = -1;
        for (int i = 0; i < input.size();)	//purposefully doesn't use i++
        {
            if ((i != input.lastIndexOf(input.get(i))) && (output.get(i) == output.get(input.lastIndexOf(input.get(i)))))
            {
                firstItemSuccess = outputSuccess.get(i); 
                lastItemSuccess = outputSuccess.get(input.lastIndexOf(input.get(i)));
                averageSuccess = (int)Math.floor(((double)(firstItemSuccess) + lastItemSuccess) / 2);
                
                output.remove(input.lastIndexOf(input.get(i)));
                outputSuccess.remove(input.lastIndexOf(input.get(i)));
                input.remove(input.lastIndexOf(input.get(i)));
                
                outputSuccess.set(i, averageSuccess);
            } else {
                i++;
            }
        }
        
    }
    /* getRandomOrder()
     * returns an array of numbers 1 through 9 in random order
     */
    private int[] getRandomOrder()
    {
        ArrayList<Integer> numbers = new ArrayList<Integer>();
        numbers.add(0);numbers.add(1);numbers.add(2);numbers.add(3);numbers.add(4);numbers.add(5);numbers.add(6);numbers.add(7);numbers.add(8);
        int[] randomOrder = new int[9];
        int randomIndex = 0;
        for (int i = 0; i < 9; i++)
        {
            randomIndex = (int)(Math.random() * numbers.size());
            randomOrder[i] = numbers.get(randomIndex);
            numbers.remove(randomIndex);
        }
        return randomOrder;
    }
    /* addOpponentMoveToMemory
     * 
     */
    public void addOpponentMoveToMemory(String board, int reaction)
    {
        thisGameInput.add(board);
        thisGameOutput.add(reaction);
    }
    
}

