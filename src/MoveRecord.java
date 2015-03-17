/**
 * MoveRecord holds coordinates for move been made
 * Created by ainozemtsev on 26.11.14.
 */
public class MoveRecord {
    final int x;
    final int y;
    final Piece piece;
    boolean dragged;

    public MoveRecord(int x, int y, Piece piece) {
        this.x = x;
        this.y = y;
        this.piece = piece;
        this.dragged = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        MoveRecord that = (MoveRecord) o;

        return (this.x == that.x) && (this.y == that.y) && (piece == that.piece);
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + piece.hashCode();
        return result;
    }

    public void setDragged() {
        this.dragged = true;
    }
}

