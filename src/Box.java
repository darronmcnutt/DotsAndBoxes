/**
 * Keeps track of the value of one box on the game board and which player owns that particular box
 */
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

    /**
     * Returns whether the box is complete (whether the box is owned by any player)
     * @return whether the box is complete (whether the box is owned by any player)
     */
    boolean isComplete() {
        return owner != Player.NONE;
    }

    int getValue() {
        return value;
    }

    /**
     * Returns the first character of the owner of this box
     * @return first character of the owner of the box
     */
    String getOwnerInitial() { return owner.toString().substring(0,1); }

    void setOwner(Player owner) { this.owner = owner; }

    Box copy() {
        return new Box(this.owner, this.value);
    }
}
