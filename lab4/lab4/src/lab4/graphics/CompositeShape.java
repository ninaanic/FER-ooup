package lab4.graphics;

import lab4.listener.GraphicalObjectListener;
import lab4.render.Renderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import static java.lang.Integer.parseInt;

public class CompositeShape implements GraphicalObject {

    private List<GraphicalObject> selected_objects;
    private List<GraphicalObjectListener> listeners;
    private boolean selected;


    public CompositeShape() {
        this.selected = false;
        listeners = new ArrayList<GraphicalObjectListener>();
    }

    public CompositeShape(List<GraphicalObject> selected_objects, boolean selected) {
        this.selected_objects = selected_objects;
        this.selected = selected;
        listeners = new ArrayList<GraphicalObjectListener>();
    }


    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
        notifySelectionListeners();
    }


    // nema HP
    @Override
    public int getNumberOfHotPoints() {
        return 0;
    }

    // nema HP
    @Override
    public Point getHotPoint(int index) {
        return null;
    }

    // nema HP
    @Override
    public void setHotPoint(int index, Point point) {

    }

    // nema HP
    @Override
    public boolean isHotPointSelected(int index) {
        return false;
    }

    // nema HP
    @Override
    public void setHotPointSelected(int index, boolean selected) {

    }

    // nema HP
    @Override
    public double getHotPointDistance(int index, Point mousePoint) {
        return 0;
    }

    @Override
    public void translate(Point delta) {
        for (GraphicalObject o : selected_objects) {
            o.translate(delta); // delegiranje
        }
        notifyListeners();
    }

    @Override
    public Rectangle getBoundingBox() {
        GraphicalObject o = selected_objects.get(0);
        int minX = o.getBoundingBox().getX();
        int minY = o.getBoundingBox().getY();
        int maxX = minX + o.getBoundingBox().getWidth();
        int maxY = minY + o.getBoundingBox().getHeight();


        for (GraphicalObject selected_object : selected_objects) {
            Rectangle bb = selected_object.getBoundingBox();
            minX = Math.min(bb.getX(), minX);
            minY = Math.min(bb.getY(), minY);
            maxX = Math.max(bb.getX() + bb.getWidth(), maxX);
            maxY = Math.max(bb.getY() + bb.getHeight(), maxY);
        }

        return new Rectangle(minX, minY, maxX-minX, maxY-minY);
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        if (selected_objects.size() == 0) {
            return Double.MAX_VALUE;
        }

        List<Double> dist = new ArrayList<>(selected_objects.size());
        for (GraphicalObject o : selected_objects) {
            dist.add(o.selectionDistance(mousePoint));
        }

        return Collections.min(dist);
    }

    @Override
    public void render(Renderer r) {
        for (GraphicalObject o : selected_objects) {
            o.render(r);
        }
    }

    @Override
    public void addGraphicalObjectListener(GraphicalObjectListener l) {
        listeners.add(l);
    }

    @Override
    public void removeGraphicalObjectListener(GraphicalObjectListener l) {
        listeners.remove(l);
    }

    @Override
    public String getShapeName() {
        return "Kompozit";
    }

    @Override
    public GraphicalObject duplicate() {
        List<GraphicalObject> graphicalObjects = new ArrayList<GraphicalObject>(selected_objects.size());
        for (GraphicalObject o : selected_objects) {
            graphicalObjects.add(o.duplicate());
        }
        return new CompositeShape(graphicalObjects, false);
    }

    @Override
    public String getShapeID() {
        return "@COMP";
    }

    @Override
    public void load(Stack<GraphicalObject> stack, String data) {

        String numberOfObj = data.trim(); // pogledati koliko prethodno-stvorenih objekata na stogu čini djecu kompozita
        List<GraphicalObject> gos = new ArrayList<>();

        for (int i = 0; i < parseInt(numberOfObj); i++) {
            GraphicalObject obj = stack.pop(); // sa stoga skinuti utvrđeni broj objekata
            gos.add(obj);
        }
        selected_objects = gos; //  postaviti ih kao djecu novostvorenog kompozita

        stack.push(this); // kompozit treba gurnuti na stog
    }

    @Override
    public void save(List<String> rows) {
        for (GraphicalObject go : selected_objects) {
            go.save(rows);
        }
        rows.add(String.format("%s %d", getShapeID(), selected_objects.size()));
    }

    private void notifyListeners() {
        for (GraphicalObjectListener listener : listeners) {
            listener.graphicalObjectChanged(this);
        }
    }

    public void notifySelectionListeners() {
        for (GraphicalObjectListener listener : listeners) {
            listener.graphicalObjectSelectionChanged(this);
        }
    }

    public List<GraphicalObject> getSelectedObjects() {
        return this.selected_objects;
    }
}
