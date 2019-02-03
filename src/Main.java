import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        int row, col;
        String direction;
        boolean isXEdge;
        Board board = new Board(2);
        board.drawBoard();
        while(true) {
            System.out.println("Enter the ROW and COLUMN of the point you want to draw FROM");
            System.out.println("followed by the direction you want to draw - RIGHT(R) or DOWN(D)");
            System.out.print("row [space] column [space] direction: ");
            Scanner in = new Scanner(System.in);
            row = in.nextInt();
            col = in.nextInt();
            direction = in.nextLine().trim().toLowerCase();
            if (direction.equals("r")) {
                isXEdge = true;
            } else {
                isXEdge = false;
            }

            board.addEdge(row, col, isXEdge);
            board.drawBoard();
        }


    }
}
