public class Action {
    int row;
    int col;
    boolean xEdge;

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

    public boolean isxEdge() {
        return xEdge;
    }
}
