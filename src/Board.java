import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Board {

    // Array of all completed horizontal edges on the board
    // [row number] [start dot (drawing left-to-right)]
    boolean[][] xEdges;

    // Array of all completed vertical edges on the board
    // [start dot (drawing top-to-bottom)] [col number]
    boolean[][] yEdges;

    Box[][] boxes;

    int size;

    int edgesRemaining;

    public Board(int size) {
        this.size = size;

        xEdges = new boolean[size + 1][size];
        for(boolean[] row : xEdges) {
            Arrays.fill(row, false);
        }

        yEdges = new boolean[size][size + 1];
        for(boolean[] row : yEdges) {
            Arrays.fill(row, false);
        }

        Random generator = new Random();

        boxes = new Box[size][size];
        for(Box[] row : boxes) {
            for(int j = 0; j < row.length; j++) {
                row[j] = new Box(generator.nextInt(5) + 1);
            }
        }

        edgesRemaining = 2 * ( (size + 1) * (size) );
    }

    boolean addEdge(int row, int col, boolean isXEdge) {

        if (coordinatesAreNotValid(row, col, isXEdge)) {
            System.out.println("\nCoordinates are not valid\n");
            return false;
        }

        if (isXEdge && !xEdges[row][col]) {
            xEdges[row][col] = true;
            edgesRemaining--;
            return true;
        } else if (!isXEdge && !yEdges[row][col]){
            yEdges[row][col] = true;
            edgesRemaining--;
            return true;
        }

        System.out.println("\nFailed to add edge\n");
        return false;
    }

    boolean coordinatesAreNotValid(int row, int col, boolean isXEdge) {
        if(isXEdge) {
            return (row < 0 || row > size       || col < 0 || col > (size - 1));
        } else {
            return (row < 0 || row > (size - 1) || col < 0 || col > size);
        }
    }


    ArrayList<Board> getActions() {
        return new ArrayList<>();
    }

    boolean isTerminal() {
        return (edgesRemaining == 0);
    }

    void drawBoard() {
        drawHeader();
        for(int row = 0; row < size + 1; row++) {
            drawXRow(row);
            if (row < size) {
                drawYRow(row);
            }
        }

        System.out.println("\nEDGES REMAINING: " + edgesRemaining + "\n");
    }

    void drawHeader() {
        StringBuilder rowString = new StringBuilder();
        rowString.append("  ");

        for(int col = 0; col < size + 1; col ++) {
            rowString.append(col);
            rowString.append("     ");
        }

        System.out.println(rowString.toString());

    }
    void drawXRow(int row) {
        StringBuilder rowString = new StringBuilder();
        rowString.append(row);
        rowString.append(" ");

        for (int col = 0; col < size; col++) {
            if (xEdges[row][col]) {
                rowString.append("◉ --- ");
            } else {
                rowString.append("◉     ");
            }
        }

        rowString.append("◉");
        System.out.println(rowString.toString());
    }

    void drawYRow(int row) {
        StringBuilder rowString = new StringBuilder();
        rowString.append("  ");

        String centerString;

        for(int col = 0; col < size + 1; col++) {

            // Generate string for center of box
            // Display owner initial if box is complete, value of box otherwise
            if (col < size) {

                if (boxes[row][col].isComplete()) {
                    centerString = boxes[row][col].getOwnerInitial();
                } else {
                    centerString = Integer.toString(boxes[row][col].getValue());
                }

            } else {
                centerString = " ";
            }

            // Generate vertical line
            if(yEdges[row][col]) {
                rowString.append("|  ")
                         .append(centerString)
                         .append("  ");
            } else {
                rowString.append("   ")
                         .append(centerString)
                         .append("  ");
            }
        }

        System.out.println(rowString.toString());

    }

}
