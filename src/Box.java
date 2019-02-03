public class Box {
    boolean complete;
    Player owner;
    int value;

    public Box(int value) {
        this.complete = false;
        this.owner = Player.NONE;
        this.value = value;
    }

    public Box(boolean complete, Player owner, int value) {
        this.complete = complete;
        this.owner = owner;
        this.value = value;
    }

    boolean isComplete() {
        return complete;
    }

    int getValue() {
        return value;
    }

    Player getOwner() { return owner; }

    String getOwnerInitial() { return owner.toString().substring(0,1); }

    void setOwner(Player owner) { this.owner = owner; }

    public void setComplete(boolean complete) { this.complete = complete; }

    Box copy() {
        return new Box(this.complete, this.owner, this.value);
    }
}
