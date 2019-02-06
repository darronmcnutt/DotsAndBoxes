import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        int row, col, size, plies, search;
        String direction;
        boolean isXEdge;

        System.out.print("Enter board size: ");
        Scanner in = new Scanner(System.in);
        size = in.nextInt();
        System.out.print("Enter num of plies: ");
        plies = in.nextInt();
        System.out.print("Select search algorithm\n(1) Minimax (2) Alpha-beta: ");
        search = in.nextInt();
        in.nextLine();

        Board board = new Board(size);
        Player currentPlayer = Player.BLACK;

        board.drawBoard();

        System.out.println("Directions\n----------");
        System.out.println("Enter the ROW and COLUMN index of the STARTING POINT of the line you wish to draw");
        System.out.println("followed by the DIRECTION you want to draw from this point - RIGHT(R) or DOWN(D)");
        System.out.println("Example: 0 2 d");
        System.out.println("\nPress ENTER to begin\n");

        in.nextLine();


        while(!board.isTerminal()) {

            if (currentPlayer == Player.BLACK) {

                System.out.println("I am thinking about my next move...");
                Action computerAction;

                Instant start = Instant.now();

                if (search == 2) {
                    System.out.println("Using Alpha-beta search");
                    computerAction = Search.alphaBeta(board, plies);
                } else {
                    System.out.println("Using Minimax search");
                    computerAction = Search.miniMax(board, plies);
                }

                Instant end = Instant.now();
                System.out.println("Search time: " + Duration.between(start, end).toMillis() + "ms\n");

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

        System.out.println("GAME OVER!");

    }

}
