package lab4.state;

import lab4.DocumentModel;
import lab4.graphics.CompositeShape;
import lab4.graphics.GraphicalObject;
import lab4.graphics.Point;
import lab4.graphics.Rectangle;
import lab4.render.Renderer;

import java.awt.event.KeyEvent;
import java.util.List;

public class SelectShapeState implements State {

    private static final int BB_WIDTH = 3; // za hot pointove
    private DocumentModel model;
    private GraphicalObject go_selected;
    private int indexOfSelectedHotPoint;

    public SelectShapeState(DocumentModel model) {
        this.model = model;
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        GraphicalObject go = model.findSelectedGraphicalObject(mousePoint); // selektira objekt ako je klik misa dovoljno blizu objektu

        if (go == null) {
            model.deselectAllSelectedObjects();
            go_selected = null;
            return;
        }

        if (ctrlDown) {
            go_selected = null;
            if (!go.isSelected()) {
                go.setSelected(true);
            } else {
                go.setSelected(false);
            }
        } else {
            model.deselectAllSelectedObjects();
            go.setSelected(true);
            go_selected = go;
            // selektiranje (bounding box se dodaje u afterDraw, ne ovdje)
        }
        //System.out.print(go.isSelected());
    }

    @Override
    public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        if (go_selected != null && indexOfSelectedHotPoint >= 0 && indexOfSelectedHotPoint < go_selected.getNumberOfHotPoints()) {
            go_selected.setHotPointSelected(indexOfSelectedHotPoint, false);
            indexOfSelectedHotPoint = -1;
        }
    }

    @Override
    public void mouseDragged(Point mousePoint) {
        if (go_selected != null) {
            if (indexOfSelectedHotPoint >= 0) {
                // 1 objekt selektiran na hot pointu --> pomicanje hot pointova trenutnog objekta
                if (go_selected.isHotPointSelected(indexOfSelectedHotPoint)) {
                    go_selected.setHotPoint(indexOfSelectedHotPoint, mousePoint);
                }
            } else {
                // nadi najblizi selektirani hot point
                int index = model.findSelectedHotPoint(go_selected, mousePoint);
                if (index != -1) {
                    go_selected.setHotPoint(index, mousePoint);
                    go_selected.setHotPointSelected(index, true);
                    indexOfSelectedHotPoint = index;
                }
            }
        }
    }

    @Override
    public void keyPressed(int keyCode) {

        //System.out.println(keyCode);

        List<GraphicalObject> selected_objects = model.getSelectedObjects();

        if (keyCode == KeyEvent.VK_UP) {
            for (GraphicalObject o : selected_objects) {
                o.translate(new Point(0, -1));
            }
        } else if (keyCode == KeyEvent.VK_DOWN) {
            for (GraphicalObject o : selected_objects) {
                o.translate(new Point(0, 1));
            }
        } else if (keyCode == KeyEvent.VK_LEFT) {
            for (GraphicalObject o : selected_objects) {
                o.translate(new Point(-1, 0));
            }
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            for (GraphicalObject o : selected_objects) {
                o.translate(new Point(1, 0));
            }
        }
        //else if (keyCode == KeyEvent.VK_PLUS || keyCode == KeyEvent.VK_ADD) {
        // na mojoj tikovnici je "+" na 61 a 61 je VK_EQUALS
        else if (keyCode == KeyEvent.VK_EQUALS) {
            if (selected_objects.size() == 1) {
                model.increaseZ(selected_objects.get(0));
            }
        }
        //else if (keyCode == KeyEvent.VK_MINUS || keyCode == KeyEvent.VK_SUBTRACT) {
        // na mojoj tikovnici je "-" na 47 a 47 je VK_SLASH
        else if (keyCode == KeyEvent.VK_SLASH) {
            if (selected_objects.size() == 1) {
                model.decreaseZ(selected_objects.get(0));
            }
        }

        else if (keyCode == KeyEvent.VK_G) {
            List<GraphicalObject> so = model.getSelectedObjects();

            if (!so.isEmpty() && so.size() > 1) {
                GraphicalObject[] comp_obj = new GraphicalObject[so.size()];
                for (int i = 0; i < comp_obj.length; i++) {
                    comp_obj[i] = so.get(0);
                    model.removeGraphicalObject(so.get(0));
                }

                GraphicalObject go = new CompositeShape(List.of(comp_obj), false);
                model.addGraphicalObject(go);
                go.setSelected(true);
            }
        }

        else if (keyCode == KeyEvent.VK_U) {
            List<GraphicalObject> so = model.getSelectedObjects();

            if (so.size() == 1 && so.get(0).getShapeName() == "Kompozit") {
                CompositeShape comp = (CompositeShape) so.get(0);
                List<GraphicalObject> comp_obj = comp.getSelectedObjects();
                model.removeGraphicalObject(comp);

                for (GraphicalObject go : comp_obj) {
                    go.setSelected(true);
                    model.addGraphicalObject(go);
                }
            }
        }
    }

    @Override
    public void afterDraw(Renderer r, GraphicalObject go) {
        if (go.isSelected()) {
            Rectangle rec = go.getBoundingBox();
            // nacrtaj bounding box
            r.drawLine(new Point(rec.getX(), rec.getY()), new Point(rec.getX() + rec.getWidth(), rec.getY())); // gore
            r.drawLine(new Point(rec.getX(), rec.getY() + rec.getHeight()), new Point(rec.getX() + rec.getWidth(), rec.getY() + rec.getHeight())); // dolje
            r.drawLine(new Point(rec.getX(), rec.getY()), new Point(rec.getX(), rec.getY() + rec.getHeight())); // lijevo
            r.drawLine(new Point(rec.getX() + rec.getWidth(), rec.getY()), new Point(rec.getX() + rec.getWidth(), rec.getY() + rec.getHeight())); // desno

            if (go_selected != null && go_selected.equals(go)) {
                // selektiran samo 1 objekt -- ucrtaj hot pointove
                Point hp_0 = go.getHotPoint(0);
                // nacrtaj bounding box
                for (int i = 0; i < go.getNumberOfHotPoints(); i++) {
                    Point point = go.getHotPoint(i);
                    r.drawLine(new Point(point.getX() - BB_WIDTH, point.getY() - BB_WIDTH),
                            new Point(point.getX() + BB_WIDTH, point.getY() - BB_WIDTH));//upper line
                    r.drawLine(new Point(point.getX() - BB_WIDTH, point.getY() - BB_WIDTH),
                            new Point(point.getX() - BB_WIDTH, point.getY() + BB_WIDTH));//left line
                    r.drawLine(new Point(point.getX() - BB_WIDTH, point.getY() + BB_WIDTH),
                            new Point(point.getX() + BB_WIDTH, point.getY() + BB_WIDTH));//bottom line
                    r.drawLine(new Point(point.getX() + BB_WIDTH, point.getY() - BB_WIDTH),
                            new Point(point.getX() + BB_WIDTH, point.getY() + BB_WIDTH));//right line
                }
            }
        }
    }

    @Override
    public void afterDraw(Renderer r) {

    }

    @Override
    public void onLeaving() {
        go_selected = null;
        model.deselectAllSelectedObjects();
    }
}
