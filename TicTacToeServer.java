import java.lang.*;
import java.io.*;
import java.util.Scanner;

public class TicTacToeServer {
    public static void main(String[] args)
    {
        try
        {
            String board[][] = new String[3][3];
            String userLetter, AILetter, turn;
            boolean over = false;
            boolean win = false;
            boolean lose = false;

            Scanner serverInput = new Scanner(System.in);

            Process ai = Runtime.getRuntime().exec("java AIClient");
            InputStream aiInput = ai.getInputStream();
            OutputStream aiOutput = ai.getOutputStream();
            Scanner fromAI = new Scanner(aiInput);
            PrintStream toAI = new PrintStream(aiOutput);

            Process user = Runtime.getRuntime().exec("java UserClient");
            InputStream userInput = user.getInputStream();
            OutputStream userOutput = user.getOutputStream();
            Scanner fromUser = new Scanner(userInput);
            PrintStream toUser = new PrintStream(userOutput);

            // set board up as blanks
            for (int r = 0; r < 3; r++)
            {
                for (int c = 0; c < 3; c++)
                {
                    board[r][c] = " ";
                }
            }

            // prompt and set letter
            System.out.println("What do you want to play? X or O?");
            userLetter = serverInput.nextLine();
            if (userLetter.equals("X"))
                AILetter = "O";
            else
                AILetter = "X";

            // send letter to clients
            toUser.println(userLetter);
            toAI.println(AILetter);
            toUser.flush();
            toAI.flush();

            // user start
            if (userLetter.equals("X"))
            {
                printBoard(board);
                turn = "user";
                while(!over)
                {
                    // user turn
                    if(turn.equals("user"))
                    {
                        // win and loss messages
                        if (win == true)
                        {
                            System.out.println("You are the winner!");
                        }
                        if (lose == true)
                        {
                            System.out.println("You have lost this game!");
                        }
                        if (win == false && lose == false && fullBoard(board))
                        {
                            System.out.println("It's a tie!");
                        }

                        System.out.print("Enter your move (you are " + userLetter + "): ");
                        String move = serverInput.nextLine();
                        if (move.equals("quit"))
                        {
                            over = true;
                            toUser.println(move);
                            toUser.flush();
                            toAI.println(move);
                            toAI.flush();
                        }
                        else if(move.equals("skip"))
                        {
                            printBoard(board);
                            if(win == true || lose == true)
                            {
                                turn = "ai";
                            }
                            else
                            {
                                System.out.println("Error: Invalid move");
                            }
                        }
                        else
                        {
                            toUser.println(userLetter);
                            toUser.flush();
                            for (int r = 0; r < 3; r++)
                            {
                                for (int c = 0; c < 3; c++)
                                {
                                    toUser.println(board[r][c]);
                                    toUser.flush();
                                }
                            }

                            // if someone has won, cannot make a move
                            if(win == true || lose == true  || fullBoard(board))
                            {
                                printBoard(board);
                                System.out.println("Error: Invalid move");
                            }
                            else // move valid
                            {
                                toUser.println(move);
                                toUser.flush();

                                // check if valid move on board
                                String hold = fromUser.nextLine();
                                if (hold.equals(" ") || hold.equals("X") || hold.equals("O"))
                                {
                                    for (int r = 0; r < 3; r++)
                                    {
                                        for (int c = 0; c < 3; c++)
                                        {
                                            board[r][c] = hold;
                                            hold = fromUser.nextLine();
                                        }
                                    }
                                }
                                else // print invalid move message
                                {
                                    printBoard(board);
                                    System.out.println(hold);
                                    hold = fromUser.nextLine();
                                }
                                toUser.flush();

                                // check board for win
                                if (checkWin(board, userLetter) == 1)
                                {
                                    win = true;
                                    printBoard(board);
                                }
                                else if (checkWin(board, userLetter) == 2)
                                {
                                    lose = true;
                                    printBoard(board);
                                }
                                else if (checkWin(board, userLetter) == 0 && fullBoard(board))
                                {
                                    printBoard(board);
                                }
                                else
                                {
                                    turn = "ai";
                                }
                            }
                        }
                    }
                    else // ai turn
                    {
                        if (win == true || lose == true || fullBoard(board))
                        {
                            turn = "user";
                        }
                        else
                        {
                            toAI.println(AILetter);
                            toAI.flush();
                            for (int r = 0; r < 3; r++)
                            {
                                for (int c = 0; c < 3; c++)
                                {
                                    toAI.println(board[r][c]);
                                    toAI.flush();
                                }
                            }

                            for (int r = 0; r < 3; r++)
                            {
                                for (int c = 0; c < 3; c++)
                                {
                                    board[r][c] = fromAI.nextLine();
                                }
                            }

                            // check board for win
                            if (checkWin(board, userLetter) == 1)
                            {
                                win = true;
                            }
                            else if (checkWin(board, userLetter) == 2)
                            {
                                lose = true;
                            }
                            else
                            {
                                turn = "user";
                            }
                            printBoard(board);
                        }
                    }
                }
            }
            else // ai start
            {
                turn = "ai";
                while(!over)
                {
                    // user turn
                    if(turn.equals("user"))
                    {
                        // win and loss messages
                        if (win == true)
                        {
                            System.out.println("You are the winner!");
                        }
                        if (lose == true)
                        {
                            System.out.println("You have lost this game!");
                        }
                        if (win == false && lose == false && fullBoard(board))
                        {
                            System.out.println("It's a tie!");
                        }

                        System.out.print("Enter your move (you are " + userLetter + "): ");
                        String move = serverInput.nextLine();
                        if (move.equals("quit"))
                        {
                            over = true;
                            toUser.println(move);
                            toUser.flush();
                            toAI.println(move);
                            toAI.flush();
                        }
                        else if(move.equals("skip"))
                        {
                            printBoard(board);
                            if(win == true || lose == true)
                            {
                                turn = "ai";
                            }
                            else
                            {
                                System.out.println("Error: Invalid move");
                            }
                        }
                        else
                        {
                            toUser.println(userLetter);
                            toUser.flush();
                            for (int r = 0; r < 3; r++)
                            {
                                for (int c = 0; c < 3; c++)
                                {
                                    toUser.println(board[r][c]);
                                    toUser.flush();
                                }
                            }

                            // if someone has won, cannot make a move
                            if(win == true || lose == true  || fullBoard(board))
                            {
                                printBoard(board);
                                System.out.println("Error: Invalid move");
                            }
                            else // move valid
                            {
                                toUser.println(move);
                                toUser.flush();

                                // check if valid move on board
                                String hold = fromUser.nextLine();
                                if (hold.equals(" ") || hold.equals("X") || hold.equals("O"))
                                {
                                    for (int r = 0; r < 3; r++)
                                    {
                                        for (int c = 0; c < 3; c++)
                                        {
                                            board[r][c] = hold;
                                            hold = fromUser.nextLine();
                                        }
                                    }
                                }
                                else // print invalid move message
                                {
                                    printBoard(board);
                                    System.out.println(hold);
                                    hold = fromUser.nextLine();
                                }
                                toUser.flush();

                                // check board for win
                                if (checkWin(board, userLetter) == 1)
                                {
                                    win = true;
                                    printBoard(board);
                                }
                                else if (checkWin(board, userLetter) == 2)
                                {
                                    lose = true;
                                    printBoard(board);
                                }
                                else if (checkWin(board, userLetter) == 0 && fullBoard(board))
                                {
                                    printBoard(board);
                                }
                                else
                                {
                                    turn = "ai";
                                }
                            }
                        }
                    }
                    else // ai turn
                    {
                        if (win == true || lose == true || fullBoard(board))
                        {
                            turn = "user";
                        }
                        else
                        {
                            toAI.println(AILetter);
                            toAI.flush();
                            for (int r = 0; r < 3; r++)
                            {
                                for (int c = 0; c < 3; c++)
                                {
                                    toAI.println(board[r][c]);
                                    toAI.flush();
                                }
                            }

                            for (int r = 0; r < 3; r++)
                            {
                                for (int c = 0; c < 3; c++)
                                {
                                    board[r][c] = fromAI.nextLine();
                                }
                            }

                            // check board for win
                            if (checkWin(board, userLetter) == 1)
                            {
                                win = true;
                            }
                            else if (checkWin(board, userLetter) == 2)
                            {
                                lose = true;
                            }
                            else
                            {
                                turn = "user";
                            }
                            printBoard(board);
                        }
                    }
                }
            }

            //System.out.println(fromUser.nextLine() + " user"); //debug
            //System.out.println(fromAI.nextLine() + " ai"); //debug

            ai.waitFor();
            user.waitFor();
        }
        catch (IOException e)
        {
            System.out.println("Unable to run AI Client and/or UserClient");
        }
        catch (InterruptedException e)
        {
            System.out.println("Unexpected Termination");
        }
    }

    // function to print board
    private static void printBoard(String board[][])
    {
        for(int r = 0; r < 3; r++)
        {
            for(int c = 0; c < 3; c++)
            {
                System.out.print(board[r][c]);

                if (c != 2)
                    System.out.print(":");
            }
            if (r != 2)
                System.out.println("\n-----");
        }
        System.out.println("\n\n\n");
    }

    // function to check if anyone has won and who
    private static int checkWin(String board[][], String userL)
    {
        // return 0 if no one wins
        // return 1 if user wins
        // return 2 if user loses
        String letterX = "X";
        String letterO = "O";

        // check row win
        for(int r = 0; r < 3; r++)
        {
            // x row win
            if(board[r][0].equals(letterX) && board[r][1].equals(letterX) && board[r][2].equals(letterX))
            {
                if(userL.equals(letterX))
                {
                    return 1;
                }
                else
                {
                    return 2;
                }
            }

            // o row win
            if(board[r][0].equals(letterO) && board[r][1].equals(letterO) && board[r][2].equals(letterO))
            {
                if(userL.equals(letterO))
                {
                    return 1;
                }
                else
                {
                    return 2;
                }
            }
        }

        // check column win
        for(int c = 0; c < 3; c++)
        {
            // x column win
            if(board[0][c].equals(letterX) && board[1][c].equals(letterX) && board[2][c].equals(letterX))
            {
                if(userL.equals(letterX))
                {
                    return 1;
                }
                else
                {
                    return 2;
                }
            }

            // o column win
            if(board[0][c].equals(letterO) && board[1][c].equals(letterO) && board[2][c].equals(letterO))
            {
                if(userL.equals(letterO))
                {
                    return 1;
                }
                else
                {
                    return 2;
                }
            }
        }

        // check diagonal win x
        if(board[0][0].equals(letterX) && board[1][1].equals(letterX) && board[2][2].equals(letterX))
        {
            if(userL.equals(letterX))
            {
                return 1;
            }
            else
            {
                return 2;
            }
        }

        // check other diagonal win x
        if(board[0][2].equals(letterX) && board[1][1].equals(letterX) && board[2][0].equals(letterX))
        {
            if(userL.equals(letterX))
            {
                return 1;
            }
            else
            {
                return 2;
            }
        }

        // check diagonal win o
        if(board[0][0].equals(letterO) && board[1][1].equals(letterO) && board[2][2].equals(letterO))
        {
            if(userL.equals(letterO))
            {
                return 1;
            }
            else
            {
                return 2;
            }
        }

        // check other diagonal win o
        if(board[0][2].equals(letterO) && board[1][1].equals(letterO) && board[2][0].equals(letterO))
        {
            if(userL.equals(letterO))
            {
                return 1;
            }
            else
            {
                return 2;
            }
        }
        return 0;
    }

    private static boolean fullBoard(String[][] board)
    {
        for(int r = 0; r < 3; r++)
        {
            for(int c = 0; c < 3; c++)
            {
                if(board[r][c].equals(" "))
                {
                    return false;
                }
            }
        }
        return true;
    }
}
