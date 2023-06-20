package lab4.render;

import lab4.graphics.Point;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SVGRendererImpl implements Renderer {

    private List<String> lines;
    private String fileName;

    public SVGRendererImpl(String fileName) {
        // zapamti fileName; u lines dodaj zaglavlje SVG dokumenta:
        // <svg xmlns=... >
        // ...
        this.fileName = fileName;
        lines = new ArrayList<>();
        lines.add("<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">");

    }

    public void close() throws IOException {
        // u lines još dodaj završni tag SVG dokumenta: </svg>
        // sve retke u listi lines zapiši na disk u datoteku
        // ...
        lines.add("</svg>");
        FileWriter fileWriter = new FileWriter(new File(this.fileName));
        for (String line : lines) {
            fileWriter.write(line);
        }
        fileWriter.flush();
        fileWriter.close();
    }

    @Override
    public void drawLine(Point s, Point e) {
        // Dodaj u lines redak koji definira linijski segment:
        // <line ... />
        lines.add(String.format("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" style=\"stroke:#0000ff;\"/>", s.getX(), s.getY(), e.getX(), e.getY()));
    }

    @Override
    public void fillPolygon(Point[] points) {
        // Dodaj u lines redak koji definira popunjeni poligon:
        // <polygon points="..." style="stroke: ...; fill: ...;" />
        StringBuilder sb = new StringBuilder();
        sb.append("<polygon points=\"");
        for (Point p : points) {
            sb.append(p.getX()).append(",").append(p.getY()).append(' ');
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append("\" style=\"stroke:#ff0000; fill:#0000ff;\" />");
        lines.add(sb.toString());
    }
}
