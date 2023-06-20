package lab4.util;

import lab4.graphics.Point;

import java.awt.geom.Line2D;

public class GeometryUtil {

    public static double distanceFromPoint(Point point1, Point point2) {
        // izračunaj euklidsku udaljenost između dvije točke ...
        return Math.sqrt(Math.pow(point1.getX() - point2.getX(), 2.0) + Math.pow(point1.getY() - point2.getY(), 2.0));
    }

    public static double distanceFromLineSegment(Point s, Point e, Point p) {
        // Izračunaj koliko je točka P udaljena od linijskog segmenta određenog
        // početnom točkom S i završnom točkom E. Uočite: ako je točka P iznad/ispod
        // tog segmenta, ova udaljenost je udaljenost okomice spuštene iz P na S-E.
        // Ako je točka P "prije" točke S ili "iza" točke E, udaljenost odgovara
        // udaljenosti od P do početne/konačne točke segmenta.
        return Line2D.ptSegDist(s.getX(), s.getY(), e.getX(), e.getY(), p.getX(), p.getY());
    }
}