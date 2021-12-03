import java.lang.*;
import java.util.Scanner;

public class UserClient {
    public static void main(String[] args)
    {
        String board[][] = new String[3][3];
        String letter, line;

        Scanner input = new Scanner(System.in);
        line = input.next();

        quit:
        while(!line.equals("quit"))
        {
            line = input.next();
            if (line.equals("quit"))
            {
                break quit;
            }
            letter = line;
            input.nextLine();

            // set up board
            for(int r = 0; r < 3; r++)
            {
                for(int c = 0; c < 3; c++)
                {
                    line = input.nextLine();
                    board[r][c] = line;
                }
            }

            // receive move and do move
            line = input.nextLine();
            if (line.contains(";"))
            {
                String moveRC[] = line.split(";");
                int row = Integer.parseInt(moveRC[0]) - 1;
                int column = Integer.parseInt(moveRC[1]) - 1;

                if(row < 3 && row >= 0 && column < 3 && column >= 0)
                {
                    if (board[row][column].equals(" "))
                    {
                        board[row][column] = letter;
                    }
                }
                else
                {
                    System.out.println("Error: Invalid move");
                }

                for(int r = 0; r < 3; r++)
                {
                    for(int c = 0; c < 3; c++)
                    {
                        System.out.println(board[r][c]);
                    }
                }
                System.out.println("buffer");
            }

        }
        //System.out.println(line); //debug
    }
}
