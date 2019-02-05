import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        int row, col, size, plies;
        String direction;
        boolean isXEdge;

        System.out.print("Enter board size: ");
        Scanner in = new Scanner(System.in);
        size = in.nextInt();
        System.out.print("Enter num of plies: ");
        plies = in.nextInt();
        in.nextLine();


        Board board = new Board(size);
        Player currentPlayer = Player.BLACK;

        board.drawBoard();

        System.out.println("Directions\n----------");
        System.out.println("Enter the ROW and COLUMN index of the STARTING POINT of the line");
        System.out.println("followed by the DIRECTION you want to draw from this point - RIGHT(R) or DOWN(D)");
        System.out.println("\nPress ENTER to begin\n");

        in.nextLine();


        while(true) {

            if (currentPlayer == Player.BLACK) {
                System.out.println("I am thinking about my next move...");
                Action computerAction = Search.miniMax(board, plies);
                row = computerAction.getRow();
                col = computerAction.getCol();
                isXEdge = computerAction.isxEdge();
            } else {
                System.out.print("\nROW [space] COLUMN [space] DIRECTION [enter]: ");
                row = in.nextInt();
                col = in.nextInt();
                direction = in.nextLine().trim().toLowerCase();

                //TODO: Fix this

                if (direction.equals("r")) {
                    isXEdge = true;
                } else {
                    isXEdge = false;
                }
            }


            if (board.addEdge(row, col, isXEdge)) {
                board.checkBox(row, col, isXEdge, currentPlayer);
                currentPlayer = currentPlayer == Player.BLACK ? Player.WHITE : Player.BLACK;
            }
            System.out.println("\nCURRENT PLAYER: " + currentPlayer + "\n");
            board.drawBoard();

        }


    }

}
