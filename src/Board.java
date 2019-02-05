import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Board {

    // Array of all completed horizontal edges on the board
    // [row number] [column number == start dot (drawing left-to-right)]
    boolean[][] xEdges;

    // Array of all completed vertical edges on the board
    // [row number == start dot (drawing top-to-bottom)] [col number]
    boolean[][] yEdges;

    Box[][] boxes;

    int size;
    int edgesRemaining;
    int blackScore;
    int whiteScore;

    // Bookkeeping for Minimax
    Action lastAction;
    int utilityValue;


    public Board(int size) {

        this.blackScore = 0;
        this.whiteScore = 0;
        this.utilityValue = 0;

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

    private Board(boolean[][] xEdges, boolean[][] yEdges, Box[][] boxes, int size, int edgesRemaining, int blackScore, int whiteScore, Action lastAction) {
        this.xEdges = xEdges;
        this.yEdges = yEdges;
        this.boxes = boxes;
        this.size = size;
        this.edgesRemaining = edgesRemaining;
        this.blackScore = blackScore;
        this.whiteScore = whiteScore;
        this.lastAction = lastAction;
        this.utilityValue = blackScore - whiteScore;
    }

    boolean addEdge(int row, int col, boolean isXEdge) {

        if (coordinatesAreNotValid(row, col, isXEdge)) {
            System.out.println("\nCoordinates are not valid\n");
            return false;
        }

        if (isXEdge && !xEdges[row][col]) {
            xEdges[row][col] = true;
            lastAction = new Action(row, col, true);
            edgesRemaining--;
            return true;
        } else if (!isXEdge && !yEdges[row][col]){
            yEdges[row][col] = true;
            lastAction = new Action(row, col, false);
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

    //TODO: Think about where to call this function
    void checkBox(int row, int col, boolean isXEdge, Player player) {

        if (isXEdge) {

            // Check if box above horizontal edge is complete
            if (row > 0)    { checkBoxHelper(row - 1, col, player); }

            // Check if box below horizontal edge is complete
            if (row < size) { checkBoxHelper(row, col, player); }

        } else {

            // Check if box left of vertical edge is complete
            if (col > 0)    { checkBoxHelper(row, col - 1, player); }

            // Check if box right of vertical edge is complete
            if (col < size) { checkBoxHelper(row, col, player); }
        }
    }
    void checkBoxHelper(int row, int col, Player player) {

        if (xEdges[row][col] && xEdges[row+1][col] && yEdges[row][col] && yEdges[row][col+1]) {

            boxes[row][col].setComplete(true);
            boxes[row][col].setOwner(player);

            if (player == Player.BLACK) {
                blackScore += boxes[row][col].getValue();
            } else {
                whiteScore += boxes[row][col].getValue();
            }
        }

    }

    ArrayList<Board> getActions(Player player) {
        ArrayList<Board> actions = new ArrayList<>();

        // Get xEdge actions
        for(int i = 0; i < size + 1; i++) {
            for(int j = 0; j < size; j++) {
                if (!xEdges[i][j]) {
                    Board boardCopy = this.copy();
                    boardCopy.addEdge(i, j, true);
                    boardCopy.checkBox(i, j, true, player);
                    actions.add(boardCopy);
                }
            }
        }

        // Get yEdge actions
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size + 1; j++) {
                if(!yEdges[i][j]) {
                    Board boardCopy = this.copy();
                    boardCopy.addEdge(i, j, false);
                    boardCopy.checkBox(i, j, false, player);
                    actions.add(boardCopy);
                }
            }
        }

        return actions;
    }

    public Action getLastAction() {
        return lastAction;
    }

    int getScoreDiff() {
        return blackScore - whiteScore;
    }

    boolean isTerminal() {
        return (edgesRemaining == 0);
    }

    public int getUtilityValue() {
        return utilityValue;
    }

    public void setUtilityValue(int utilityValue) {
        this.utilityValue = utilityValue;
    }

    Board copy() {

        boolean[][] xEdgesCopy = new boolean[size + 1][size];
        boolean[][] yEdgesCopy = new boolean[size][size + 1];
        Box[][] boxesCopy = new Box[size][size];

        // Deep copy the xEdges array
        for(int i = 0; i < size + 1; i++) {
            xEdgesCopy[i] = Arrays.copyOf(xEdges[i], size);
        }

        // Deep copy the yEdges array
        for(int i = 0; i < size; i++) {
            yEdgesCopy[i] = Arrays.copyOf(yEdges[i], size + 1);
        }

        // Deep copy the boxes array
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                boxesCopy[i][j] = boxes[i][j].copy();
            }
        }

        return new Board(xEdgesCopy, yEdgesCopy, boxesCopy, size, edgesRemaining, blackScore, whiteScore, lastAction);
    }

    void drawBoard() {
        drawHeader();
        for(int row = 0; row < size + 1; row++) {
            drawXRow(row);
            if (row < size) {
                drawYRow(row);
            }
        }

        System.out.println("\nEDGES REMAINING: " + edgesRemaining);
        System.out.println("    BLACK SCORE: " + blackScore);
        System.out.println("    WHITE SCORE: " + whiteScore + "\n");
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
