package lab4.graphics;

import lab4.render.Renderer;
import lab4.util.GeometryUtil;

import java.util.List;
import java.util.Stack;

import static java.lang.Integer.parseInt;

public class LineSegment extends AbstractGraphicalObject {
    public LineSegment() {
        super(new Point[]{
                new Point(-10,10),
                new Point(0,0)
        });
    }
    public LineSegment(Point pocetna, Point konacna) {
        super(new Point[]{
                pocetna,
                konacna
        });
    }

    @Override
    public Rectangle getBoundingBox() {
        Point p1 = getHotPoint(0);
        Point p2 = getHotPoint(1);

        int x = Math.min(p1.getX(), p2.getX());
        int y = Math.min(p1.getY(), p2.getY());
        int width = p1.getX() < p2.getX() ? p2.getX() - p1.getX() : p1.getX() - p2.getX();
        int height = p1.getY() < p2.getY() ? p2.getY() - p1.getY() : p1.getY() - p2.getY();

        return new Rectangle(x, y, width, height);
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        return GeometryUtil.distanceFromLineSegment(getHotPoint(0), getHotPoint(1), mousePoint);
    }

    @Override
    public void render(Renderer r) {
        r.drawLine(getHotPoint(0), getHotPoint(1));
    }

    @Override
    public String getShapeName() {
        return "Linija";
    }

    @Override
    public GraphicalObject duplicate() {
        return new LineSegment(getHotPoint(0), getHotPoint(1));
    }

    @Override
    public String getShapeID() {
        return "@LINE";
    }

    @Override
    public void load(Stack<GraphicalObject> stack, String data) {
        // stvori novi objekt
        String[] parts = data.trim().split(" ");
        setHotPoint(0, new Point(parseInt(parts[0]), parseInt(parts[1])));
        setHotPoint(1, new Point(parseInt(parts[2]), parseInt(parts[3])));
        // push na stog
        stack.push(this);
    }

    @Override
    public void save(List<String> rows) {
        rows.add(String.format("%s %d %d %d %d", getShapeID(), getHotPoint(0).getX(), getHotPoint(0).getY(), getHotPoint(1).getX(), getHotPoint(1).getY()));
    }
}
