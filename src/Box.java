public class Box {
    Player owner;
    int value;

    public Box(int value) {
        this.owner = Player.NONE;
        this.value = value;
    }

    public Box(Player owner, int value) {
        this.owner = owner;
        this.value = value;
    }

    boolean isComplete() {
        return owner != Player.NONE;
    }

    int getValue() {
        return value;
    }

    String getOwnerInitial() { return owner.toString().substring(0,1); }

    void setOwner(Player owner) { this.owner = owner; }

    Box copy() {
        return new Box(this.owner, this.value);
    }
}
