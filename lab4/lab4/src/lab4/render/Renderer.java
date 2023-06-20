package lab4.render;

import lab4.graphics.Point;

public interface Renderer {
    void drawLine(Point s, Point e);
    void fillPolygon(Point[] points);
}