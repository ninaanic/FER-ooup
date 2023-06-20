package lab4.state;

import lab4.DocumentModel;
import lab4.graphics.GraphicalObject;
import lab4.graphics.Point;
import lab4.render.Renderer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EraserState implements State {

    private DocumentModel model;

    private List<Point> points;

    public EraserState(DocumentModel model) {
        this.model = model;
        this.points = new ArrayList<Point>();
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {

    }

    @Override
    public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        // brisi sve s kojima se sijece mousePoint
        Set<GraphicalObject> obj_to_erase = new HashSet<GraphicalObject>();
        points.add(mousePoint);
        for (Point p : points) {
            obj_to_erase.add(model.findSelectedGraphicalObject(p));
        }
        for (GraphicalObject o : obj_to_erase) {
            model.removeGraphicalObject(o);
        }

        // ocisti listu
        points.clear();
        model.notifyListeners();

    }


    @Override
    public void mouseDragged(Point mousePoint) {
        // dodaj svaku poaziciju misa
        points.add(mousePoint);
        model.notifyListeners();
    }

    @Override
    public void keyPressed(int keyCode) {

    }

    @Override
    public void afterDraw(Renderer r, GraphicalObject go) {

    }

    @Override
    public void afterDraw(Renderer r) {
        for (int i = 0; i < points.size() - 1; i += 2) {
            r.drawLine(points.get(i), points.get(i+1));
        }
    }

    @Override
    public void onLeaving() {
    }
}
