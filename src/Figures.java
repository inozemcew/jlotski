/**
 * Implementation of Piece subclasses for various game objects
 * Created by ainozemtsev on 20.11.14.
 */


class Figure extends Piece{
    @Override
    protected Cell newCell(int x, int y) {
        return new FigureCell(this,x,y);
    }

    @Override
    protected boolean allowOverlap(Piece piece) {
        return super.allowOverlap(piece) || (piece instanceof Target);
    }
}


class MainFigure extends Figure{
    @Override
    protected Cell newCell(int x, int y) {
        return new MainFigureCell(this,x,y);
    }

    @Override
    protected boolean allowOverlap(Piece piece) {
        return super.allowOverlap(piece) || (piece instanceof Gate) || (piece instanceof Target);
    }
}


class Wall extends Piece{
    @Override
    protected Cell newCell(int x, int y) {
        return new WallCell(this,x,y);
    }
}


class Gate extends Piece{
    @Override
    protected Cell newCell(int x, int y) {
        return new GateCell(this,x,y);
    }
}


class Target extends Piece{
    @Override
    protected Cell newCell(int x, int y) {
        return new TargetCell(this, x, y);
    }
}


