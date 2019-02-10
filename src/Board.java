import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Weighted Dots and Boxes game board. Also includes bookkeeping data useful for adversarial search algorithms
 */
public class Board {

    // Array of all completed horizontal edges on the board
    // [row number] [column number == start dot (drawing left-to-right)]
    private final boolean[][] xEdges;

    // Array of all completed vertical edges on the board
    // [row number == start dot (drawing top-to-bottom)] [col number]
    private final boolean[][] yEdges;

    // Array of boxes on the board, keeps track of owner and value of each box
    // [row number][col number] of upper-left coordinate
    private final Box[][] boxes;

    // Bookkeeping for board
    private final int size;
    private int edgesRemaining;
    private int blackScore;
    private int whiteScore;

    // Bookkeeping for Minimax and Alpha-beta
    private Action lastAction;
    private int utilityValue;

    public Board(int size) {

        this.blackScore = 0;
        this.whiteScore = 0;
        this.utilityValue = 0;

        this.size = size;

        // Initialize the xEdges array
        xEdges = new boolean[size + 1][size];
        for(boolean[] row : xEdges) {
            Arrays.fill(row, false);
        }

        // Initialize the yEdges array
        yEdges = new boolean[size][size + 1];
        for(boolean[] row : yEdges) {
            Arrays.fill(row, false);
        }

        Random generator = new Random();

        // Initialize the boxes array with random values
        boxes = new Box[size][size];
        for(Box[] row : boxes) {
            for(int j = 0; j < row.length; j++) {
                row[j] = new Box(generator.nextInt(5) + 1);
            }
        }

        // Calculate the number of edges remaining
        edgesRemaining = 2 * ( (size + 1) * (size) );
    }

    private Board(boolean[][] xEdges, boolean[][] yEdges, Box[][] boxes, int size, int edgesRemaining, int blackScore, int whiteScore, Action lastAction, int utilityValue) {
        this.xEdges = xEdges;
        this.yEdges = yEdges;
        this.boxes = boxes;
        this.size = size;
        this.edgesRemaining = edgesRemaining;
        this.blackScore = blackScore;
        this.whiteScore = whiteScore;
        this.lastAction = lastAction;
        this.utilityValue = utilityValue;
    }

    /**
     * Adds an edge (line connecting two adjacent dots) to the board
     * @param row Y coordinate of the starting point
     * @param col X coordinate of the starting point
     * @param isXEdge True: draw right from the starting point. False: draw down from the starting point.
     * @return true if edge successfully added, false otherwise
     */
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

        System.out.println("\nThis edge already exists\n");
        return false;
    }

    /**
     * Checks whether a horizontal or vertical edge can be drawn from the specified starting point
     * @param row Y coordinate
     * @param col X coordinate
     * @param isXEdge horizontal or vertical edge
     * @return true if coordinates are not valid
     */
    private boolean coordinatesAreNotValid(int row, int col, boolean isXEdge) {
        if(isXEdge) {
            return (row < 0 || row > size       || col < 0 || col > (size - 1));
        } else {
            return (row < 0 || row > (size - 1) || col < 0 || col > size);
        }
    }

    /**
     * Checks whether the specified edge completes a box. If so, sets the specified player as the owner of the box
     * and updates the score.
     * @param row Y coordinate of the edge starting point
     * @param col X coordinate of the edge starting point
     * @param isXEdge horizontal or vertical edge
     * @param player player who added this edge to the board
     */
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

    /**
     * Helper function for checkBox. Checks all four edges of a box for completeness. If complete, assigns the specified
     * player as the owner of the box, updates the score, and updates the utility value.
     * @param row Y coordinate of upper left corner of the box
     * @param col X coordinate of upper left corner of the box
     * @param player player to be assigned as the owner of the box if complete
     */
    private void checkBoxHelper(int row, int col, Player player) {

        if (xEdges[row][col] && xEdges[row+1][col] && yEdges[row][col] && yEdges[row][col+1]) {

            boxes[row][col].setOwner(player);

            if (player == Player.BLACK) {
                blackScore += boxes[row][col].getValue();
            } else {
                whiteScore += boxes[row][col].getValue();
            }

            utilityValue = blackScore - whiteScore;
        }

    }

    /**
     * Returns an ArrayList of Board objects representing all possible moves from the current state
     * @param player specifies the current player
     * @return ArrayList of Board objects representing all possible moves from the current state
     */
    public ArrayList<Board> getActions(Player player) {
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

        // Not necessary and adds some time, but can make the game more interesting
        Collections.shuffle(actions);

        return actions;
    }

    public Action getLastAction() {
        return lastAction;
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

    public int getEdgesRemaining() {
        return edgesRemaining;
    }

    /**
     * Creates a deep copy of the current Board
     * @return a deep copy of the current board as a new Board object
     */
    private Board copy() {

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

        return new Board(xEdgesCopy, yEdgesCopy, boxesCopy, size, edgesRemaining, blackScore, whiteScore, lastAction, utilityValue);
    }

    /**
     * Renders the current board state in the console
     */
    void drawBoard() {
        //System.out.print("\033[H\033[2J");
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

    private void drawHeader() {
        StringBuilder rowString = new StringBuilder();
        rowString.append("  ");

        for(int col = 0; col < size + 1; col ++) {
            rowString.append(col);
            rowString.append("     ");
        }

        System.out.println(rowString.toString());

    }
    private void drawXRow(int row) {
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

    private void drawYRow(int row) {
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
