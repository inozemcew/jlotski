/**
 * Created by ainozemtsev on 26.11.14.
 */
public class MoveRecord {
    final int x;
    final int y;
    final Piece piece;

    public MoveRecord(int x, int y, Piece piece) {
        this.x = x;
        this.y = y;
        this.piece = piece;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        MoveRecord that = (MoveRecord) o;

        return (this.x == that.x) && (this.y == that.y) && (piece == that.piece);
        /*if (x != that.x) return false;
        if (y != that.y) return false;
        if (piece != that.piece) return false;

        return true;*/
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + piece.hashCode();
        return result;
    }

}

