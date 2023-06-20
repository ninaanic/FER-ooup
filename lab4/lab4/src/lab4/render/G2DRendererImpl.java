package lab4.render;

import lab4.graphics.Point;

import java.awt.*;

public class G2DRendererImpl implements Renderer {

    private Graphics2D g2d;

    public G2DRendererImpl(Graphics2D g2d) {
        this.g2d = g2d;
    }

    @Override
    public void drawLine(Point s, Point e) {
        // Postavi boju na plavu
        // Nacrtaj linijski segment od S do E
        // (sve to uporabom g2d dobivenog u konstruktoru)
        g2d.setColor(Color.blue);
        g2d.drawLine(s.getX(), s.getY(), e.getX(), e.getY());
    }

    @Override
    public void fillPolygon(Point[] points) {
        // Postavi boju na plavu
        // Popuni poligon definiran danim točkama
        // Postavi boju na crvenu
        // Nacrtaj rub poligona definiranog danim točkama
        // (sve to uporabom g2d dobivenog u konstruktoru)
        int[] xpoints = new int[points.length];
        int[] ypoints = new int[points.length];

        for (int i = 0; i < points.length; i++) {
            xpoints[i] = points[i].getX();
            ypoints[i] = points[i].getY();
        }

        g2d.setColor(Color.blue);
        g2d.fillPolygon(xpoints, ypoints, points.length);
        g2d.setColor(Color.red);
        g2d.drawPolygon(xpoints, ypoints, points.length);
    }
}
