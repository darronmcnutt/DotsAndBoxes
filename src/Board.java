import java.util.ArrayList;
import java.util.Arrays;

public class Board {

    // Array of all completed horizontal edges on the board
    // [row number] [start dot (drawing left-to-right)]
    boolean[][] xEdges;

    // Array of all completed vertical edges on the board
    // [start dot (drawing top-to-bottom)] [col number]
    boolean[][] yEdges;

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

        edgesRemaining = 2 * ( (size + 1) * (size) );
    }

    boolean addEdge(int row, int col, boolean isXEdge) {
        if (!coordinatesExist(row, col)) {
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

        return false;
    }

    boolean coordinatesExist(int row, int col) {
        if(row < 0 || row > size || col < 0 || col > size) {
            return false;
        } else {
            return true;
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
                rowString.append("· --- ");
            } else {
                rowString.append("·     ");
            }
        }

        rowString.append("·");
        System.out.println(rowString.toString());
    }

    void drawYRow(int row) {
        StringBuilder rowString = new StringBuilder();
        rowString.append("  ");

        for(int col = 0; col < size + 1; col++) {
            if(yEdges[row][col]) {
                rowString.append("|     ");
            } else {
                rowString.append("      ");
            }
        }

        System.out.println(rowString.toString());

    }

}
