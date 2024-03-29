/**
 * Stores data needed to add an edge to the board
 */
public class Action {
    private final int row;
    private final int col;
    private final boolean xEdge;

    public Action(int row, int col, boolean xEdge) {
        this.row = row;
        this.col = col;
        this.xEdge = xEdge;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isXEdge() {
        return xEdge;
    }
}
