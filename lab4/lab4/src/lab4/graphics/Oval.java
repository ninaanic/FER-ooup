package lab4.graphics;

import lab4.render.Renderer;

import java.util.List;
import java.util.Stack;

import static java.lang.Integer.parseInt;

public class Oval extends AbstractGraphicalObject{

    private final static int NUMBER_OF_POINTS = 100;

    public Oval() {
        super(new Point[]{
                new Point(0,10),
                new Point(10,0)
        });
    }

    public Oval(Point donji, Point desni) {
        super(new Point[]{
                donji,
                desni
        });
    }

    @Override
    public Rectangle getBoundingBox() {
        Point donji = getHotPoint(0);
        Point desni = getHotPoint(1);

        int width = 2 * (desni.getX() - donji.getX());
        int height = 2 * (donji.getY() - desni.getY());
        int x = desni.getX() - width;
        int y = donji.getY() - height;

        return new Rectangle(x, y, width, height);
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        Point donji = getHotPoint(0);
        Point desni = getHotPoint(1);

        Point centar = new Point(donji.getX(), desni.getY());

        int semi_major_width = desni.getX() - donji.getX();
        int semi_major_height = desni.getY() - donji.getY();

        int dx = mousePoint.getX() - centar.getX();
        int dy = mousePoint.getY() - centar.getY();

        double distance = Math.sqrt((dx * dx) / (semi_major_width * semi_major_width) + (dy * dy) / (semi_major_height * semi_major_height));
        if (distance <= 1) {
            return 0; // miš unutar ovala
        }
        return distance;
    }

    @Override
    public void render(Renderer r) {
        r.fillPolygon(getPoints(NUMBER_OF_POINTS));
    }

    private Point[] getPoints(int numberOfPoints) {
        Point donji = getHotPoint(0);
        Point desni = getHotPoint(1);
        Point center = new Point(donji.getX(), desni.getY());
        int semi_major_width = desni.getX() - donji.getX();
        int semi_major_height = desni.getY() - donji.getY();

        Point[] points = new Point[numberOfPoints];
        for (int i = 0; i < numberOfPoints; i++) {
            double t = (2*Math.PI/numberOfPoints) * i;
            int x = (int)(semi_major_width * Math.cos(t)) + center.getX();
            int y = (int)(semi_major_height * Math.sin(t)) + center.getY();
            points[i] = new Point(x,y);
        }
        return points;
    }

    @Override
    public String getShapeName() {
        return "Oval";
    }

    @Override
    public GraphicalObject duplicate() {
        return new Oval(getHotPoint(0), getHotPoint(1));
    }

    @Override
    public String getShapeID() {
        return "@OVAL";
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
