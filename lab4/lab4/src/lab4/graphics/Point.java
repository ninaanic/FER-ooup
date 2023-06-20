package lab4.graphics;

public class Point {

    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public Point translate(Point dp) {
        // vraća NOVU točku translatiranu za argument tj. THIS+DP...
        return new Point(this.x + dp.getX(), this.y + dp.getY());
    }

    public Point difference(Point p) {
        // vraća NOVU točku koja predstavlja razliku THIS-P...
        return new Point(this.x - p.getX(), this.y - p.getY());
    }
}