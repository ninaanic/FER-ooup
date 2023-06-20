package lab4.state;

import lab4.DocumentModel;
import lab4.graphics.GraphicalObject;
import lab4.render.Renderer;
import lab4.graphics.Point;

public class AddShapeState implements State {

    private GraphicalObject prototype;
    private DocumentModel model;

    public AddShapeState(DocumentModel model, GraphicalObject prototype) {
        this.model = model;
        this.prototype = prototype;
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        // dupliciraj zapamćeni prototip, pomakni ga na poziciju miša i dodaj u model
        GraphicalObject go = prototype.duplicate();
        go.translate(mousePoint);
        model.addGraphicalObject(go);
    }

    @Override
    public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {

    }

    @Override
    public void mouseDragged(Point mousePoint) {

    }

    @Override
    public void keyPressed(int keyCode) {

    }

    @Override
    public void afterDraw(Renderer r, GraphicalObject go) {

    }

    @Override
    public void afterDraw(Renderer r) {

    }

    @Override
    public void onLeaving() {

    }
}
