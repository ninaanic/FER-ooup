package lab4.graphics;

import lab4.listener.GraphicalObjectListener;
import lab4.util.GeometryUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGraphicalObject implements GraphicalObject {

    private Point[] hotPoints;
    private boolean[] hotPointsSelected;
    private boolean selected;
    private List<GraphicalObjectListener> listeners;

    public AbstractGraphicalObject(Point[] hotPoints) {
        this.hotPoints = hotPoints;
        hotPointsSelected = new boolean[hotPoints.length];
        listeners = new ArrayList<>();
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

    @Override
    public int getNumberOfHotPoints() {
        return hotPoints.length;
    }

    @Override
    public Point getHotPoint(int index) {
        if (index < hotPoints.length) {
           return hotPoints[index];
        }
        return null;
    }

    @Override
    public void setHotPoint(int index, Point point) {
        if (index < hotPoints.length) {
            hotPoints[index] = point;
        }
        notifyListeners();
    }

    @Override
    public boolean isHotPointSelected(int index) {
        if (index < hotPointsSelected.length) {
            return hotPointsSelected[index];
        }
        return false;
    }

    @Override
    public void setHotPointSelected(int index, boolean selected) {
        if (index < hotPointsSelected.length) {
            hotPointsSelected[index] = selected;
        }
        notifySelectionListeners();
    }

    @Override
    public double getHotPointDistance(int index, Point mousePoint) {
        if (index >= hotPoints.length) {
            throw new IndexOutOfBoundsException(index);
        }
        return GeometryUtil.distanceFromPoint(hotPoints[index], mousePoint);

    }

    @Override
    public void translate(Point delta) {
        int i = 0;
        for (Point p : hotPoints){
            Point newHP = new Point(p.getX() + delta.getX(), p.getY() + delta.getY());
            setHotPoint(i, newHP);
            i++;
        }
        notifyListeners();
    }

    @Override
    public void addGraphicalObjectListener(GraphicalObjectListener l) {
        listeners.add(l);
    }

    @Override
    public void removeGraphicalObjectListener(GraphicalObjectListener l) {
        listeners.remove(l);
    }

    public void notifyListeners() {
        for (GraphicalObjectListener listener : listeners) {
            listener.graphicalObjectChanged(this);
        }
    }

    public void notifySelectionListeners() {
        for (GraphicalObjectListener listener : listeners) {
            listener.graphicalObjectSelectionChanged(this);
        }
    }
}
