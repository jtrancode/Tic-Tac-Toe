import java.util.Scanner;

public class AIClient {
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

            // ai make a move
            aimove:
            for(int r = 0; r < 3; r++)
            {
                for(int c = 0; c < 3; c++)
                {
                    if (board[r][c].equals(" "))
                    {
                        board[r][c] = letter;
                        break aimove;
                    }
                }
            }

            // send board back
            for(int r = 0; r < 3; r++)
            {
                for(int c = 0; c < 3; c++)
                {
                    System.out.println(board[r][c]);
                }
            }

        }
        //System.out.println(line); //debug
    }
}
