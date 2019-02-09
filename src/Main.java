import java.time.Duration;
import java.time.Instant;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        int row, col, size = 0, plies = 0, search = 0;
        String direction;
        boolean isXEdge;
        boolean inputComplete = false;
        Scanner userInput = new Scanner(System.in);

        while(!inputComplete) {
            try {
                System.out.print("Enter board size: ");
                size = Integer.parseInt(userInput.nextLine());
                if (size < 2) { throw new InputMismatchException(); }

                System.out.print("Enter num of plies: ");
                plies = Integer.parseInt(userInput.nextLine());
                if (plies < 1) { throw new InputMismatchException(); }

                System.out.print("Select search algorithm\n(1) Minimax (2) Alpha-beta: ");
                search = Integer.parseInt(userInput.nextLine());
                if (search != 1 && search != 2) { throw new InputMismatchException(); }

                inputComplete = true;
            } catch (NumberFormatException e) {
                System.out.println("\nINVALID INPUT: please enter an integer\n");
            } catch (InputMismatchException e) {
                System.out.println("\nINVALID INPUT\n");
            }
        }

        Board board = new Board(size);
        Player currentPlayer = Player.BLACK;

        System.out.println("\nDirections\n----------");
        System.out.println("Enter the ROW (Y) and COLUMN (X) coordinates of the STARTING POINT of the line you wish to draw");
        System.out.println("followed by the DIRECTION in which you want to draw from this point - RIGHT(R) or DOWN(D)");
        System.out.println("\nPress ENTER to begin\n");

        userInput.nextLine();

        while (!board.isTerminal()) {

            System.out.println("\nCURRENT PLAYER: " + currentPlayer + "\n");
            board.drawBoard();

            if (currentPlayer == Player.BLACK) {

                System.out.println("I am thinking about my next move... ");
                System.out.print("SEARCH STRATEGY: ");

                Action computerAction;

                Instant start = Instant.now();

                switch(search) {
                    case 1:
                        System.out.println("Minimax");
                        computerAction = Search.miniMax(board, plies);
                        break;
                    case 2:
                        System.out.println("Alpha-beta");
                        computerAction = Search.alphaBeta(board, plies);
                        break;
                    default:
                        System.out.println("Alpha-beta");
                        computerAction = Search.alphaBeta(board, plies);
                }

                Instant end = Instant.now();
                System.out.println("SEARCH TIME: " + Duration.between(start, end).toMillis() + "ms\n");

                row = computerAction.getRow();
                col = computerAction.getCol();
                isXEdge = computerAction.isXEdge();

                if (board.addEdge(row, col, isXEdge)) {
                    board.checkBox(row, col, isXEdge, currentPlayer);
                    currentPlayer = Player.WHITE;
                }

            } else {
                try {
                    String inputString;
                    System.out.print("ROW [space] COLUMN [space] DIRECTION(R or D): ");
                    userInput = new Scanner(System.in);
                    inputString = userInput.nextLine();

                    Scanner inputParser = new Scanner(inputString);

                    row = inputParser.nextInt();
                    col = inputParser.nextInt();
                    direction = inputParser.next().trim().toLowerCase();

                    if (direction.equals("r")) {
                        isXEdge = true;
                    } else if(direction.equals("d")) {
                        isXEdge = false;
                    } else {
                        throw new java.util.InputMismatchException();
                    }
                    if (board.addEdge(row, col, isXEdge)) {
                        board.checkBox(row, col, isXEdge, currentPlayer);
                        currentPlayer = Player.BLACK;
                    }
                } catch (Exception e) {
                    System.out.println("\nINVALID INPUT: please use the correct format");
                }
            }

        }
        board.drawBoard();
        System.out.println("GAME OVER!");
    }
}
