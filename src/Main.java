import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        int row, col;
        String direction;
        boolean isXEdge;
        Action computerAction;

        Board board = new Board(3);
        Player currentPlayer = Player.BLACK;
        board.drawBoard();
        System.out.println("Directions\n----------");
        System.out.println("Enter the ROW and COLUMN index of the STARTING POINT of the line");
        System.out.println("followed by the DIRECTION you want to draw from this point - RIGHT(R) or DOWN(D)");
        System.out.print("\nROW [space] COLUMN [space] DIRECTION [enter]: ");

        while(true) {
            computerAction = Search.miniMax(board, 5);
            row = computerAction.getRow();
            col = computerAction.getCol();
            isXEdge = computerAction.isxEdge();

            System.out.println(row + " " + col + " " + isXEdge);

            if (board.addEdge(row, col, isXEdge)) {
                board.checkBox(row, col, isXEdge, currentPlayer);
                currentPlayer = currentPlayer == Player.BLACK ? Player.WHITE : Player.BLACK;
            }
            System.out.println("\nCURRENT PLAYER: " + currentPlayer + "\n");
            board.drawBoard();

            Scanner in = new Scanner(System.in);
            row = in.nextInt();
            col = in.nextInt();
            direction = in.nextLine().trim().toLowerCase();
            //TODO: Fix this
            if (direction.equals("r")) {
                isXEdge = true;
            } else {
                isXEdge = false;
            }

            if (board.addEdge(row, col, isXEdge)) {
                board.checkBox(row, col, isXEdge, currentPlayer);
                currentPlayer = switchPlayer(currentPlayer);
            }
            System.out.println("\nCURRENT PLAYER: " + currentPlayer + "\n");
            board.drawBoard();
        }


    }

    private static Player switchPlayer(Player currentPlayer) {
        return currentPlayer == Player.BLACK ? Player.WHITE : Player.BLACK;
    }
}
