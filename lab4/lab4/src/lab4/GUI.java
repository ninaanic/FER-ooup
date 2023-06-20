package lab4;

import lab4.graphics.*;
import lab4.graphics.Point;
import lab4.listener.DocumentModelListener;
import lab4.render.G2DRendererImpl;
import lab4.render.Renderer;
import lab4.render.SVGRendererImpl;
import lab4.state.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.List;

public class GUI extends JFrame {

    private DocumentModel model;
    private Canvas canvas; // platno za crtanje
    private State currentState;

    private Map<String, GraphicalObject> prototypes;


    public GUI(List<GraphicalObject> objects) {
        model = new DocumentModel();
        currentState = new IdleState();
        prototypes = new HashMap<>();

        addComponents(objects);
        addToolbar(objects);
        addListeners();

        setSize(600, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }


    private void addComponents(List<GraphicalObject> objects) {
        canvas = new Canvas();
        add(canvas, BorderLayout.CENTER);

        for (GraphicalObject go : objects) {
            prototypes.put(go.getShapeID(), go);
        }

        GraphicalObject comp = new CompositeShape();
        prototypes.put(comp.getShapeID(), comp);
    }

    private Action selectAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            currentState.onLeaving();
            currentState = new SelectShapeState(model);
        }
    };

    private Action eraseAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            currentState.onLeaving();
            currentState = new EraserState(model);
        }
    };

    private Action exportAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser file = new JFileChooser();
            file.setDialogTitle("SVG Export");
            if (file.showSaveDialog(GUI.this) == JFileChooser.APPROVE_OPTION) {
                String path = file.getSelectedFile().getPath();
                if (!path.endsWith(".svg")) {
                    path += ".svg";
                }
                SVGRendererImpl svgRenderer = new SVGRendererImpl(path);
                List<GraphicalObject> obj_to_draw = model.list();
                for (GraphicalObject o : obj_to_draw) {
                    o.render(svgRenderer);
                }

                try {
                    svgRenderer.close();
                    JOptionPane.showMessageDialog(GUI.this, "SVG file successfully saved.", "INFO", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e1){
                    JOptionPane.showMessageDialog(GUI.this, "While exporting to file " + path + ": " + e1, "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    };

    private Action saveAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser file = new JFileChooser();
            file.setDialogTitle("Save");
            if (file.showSaveDialog(GUI.this) == JFileChooser.APPROVE_OPTION) {
                String path = file.getSelectedFile().getPath();
                if (!path.endsWith(".txt")) {
                    path += ".txt";
                }
                List<GraphicalObject> obj_to_save = model.list();
                List<String> rows = new ArrayList<>();
                for (GraphicalObject o : obj_to_save) {
                    o.save(rows);
                }

                try {
                    FileWriter fw = new FileWriter(path);
                    for (String row : rows) {
                        fw.write(row);
                        fw.write("\n");
                    }
                    fw.flush();
                    fw.close();
                    JOptionPane.showMessageDialog(GUI.this, "SVG file successfully saved.", "INFO", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e2){
                    JOptionPane.showMessageDialog(GUI.this, "While exporting to file " + path + ": " + e2, "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    };

    private Action loadAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Load");
            if (fc.showSaveDialog(GUI.this) == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();

                try {
                    List<String> rows = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
                    Stack<GraphicalObject> stack = new Stack<>();

                    GraphicalObject go = null;
                    String id = null;
                    int index = -1;

                    for (String line : rows) {
                        if (line.startsWith("@")) {
                            index = line.indexOf(' ');

                            if (index != -1) {
                                id = line.substring(0, index);
                                go = prototypes.get(id);

                                if (go != null) {
                                    go.duplicate().load(stack, line.substring(index+1));
                                } else {
                                    JOptionPane.showMessageDialog(GUI.this, "Unknown shape: " + id, "ERROR", JOptionPane.ERROR_MESSAGE);
                                    break;
                                }
                            } else {
                                JOptionPane.showMessageDialog(GUI.this, "Error in line: " + line, "ERROR", JOptionPane.ERROR_MESSAGE);
                                break;
                            }
                        } else {
                            JOptionPane.showMessageDialog(GUI.this, "Error in line: " + line, "ERROR", JOptionPane.ERROR_MESSAGE);
                            break;
                        }
                    }

                    for (GraphicalObject o : stack) {
                        model.addGraphicalObject(o);
                    }

                } catch (IOException e3) {
                    JOptionPane.showMessageDialog(GUI.this, "While exporting to file " + file.getPath() + ": " + e3, "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    };

    private void addToolbar(List<GraphicalObject> objects) {
        JToolBar toolBar = new JToolBar();

        for (GraphicalObject go : objects) {
            Action action = new CanvasAction(go);
            action.putValue(Action.NAME, go.getShapeName());
            toolBar.add(action);
        }

        selectAction.putValue(Action.NAME, "Select");
        toolBar.add(selectAction);

        eraseAction.putValue(Action.NAME, "Erase");
        toolBar.add(eraseAction);

        exportAction.putValue(Action.NAME, "SVG Export");
        toolBar.add(exportAction);

        saveAction.putValue(Action.NAME, "Save");
        toolBar.add(saveAction);

        loadAction.putValue(Action.NAME, "Load");
        toolBar.add(loadAction);

        toolBar.setFloatable(false);
        add(toolBar, BorderLayout.NORTH);
    }

    private void addListeners() {
        model.addDocumentModelListener(new DocumentModelListener() {
            @Override
            public void documentChange() {
                canvas.repaint();
            }
        });

        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    currentState.onLeaving(); // prelazimo u novo stanje
                    currentState = new IdleState(); // novo stanje
                } else {
                    currentState.keyPressed(e.getKeyCode());
                }
            }
        });

        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                currentState.mouseDown(new Point(e.getX(), e.getY()), e.isShiftDown(), e.isControlDown());
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                currentState.mouseUp(new Point(e.getX(), e.getY()), e.isShiftDown(), e.isControlDown());
            }
        });

        canvas.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                currentState.mouseDragged(new Point(e.getX(), e.getY()));
            }
        });
    }


    // glavni program
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                List<GraphicalObject> objects = new ArrayList<GraphicalObject>();
                objects.add(new LineSegment());
                objects.add(new Oval());
                new GUI(objects);
            }
        });
    }


    public class Canvas extends JComponent {

        public Canvas() {
            setFocusable(true);
            requestFocusInWindow();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            List<GraphicalObject> objects = model.list();

            Graphics2D g2d = (Graphics2D)g;
            //g2d.scale(3.0, 3.0);
            Renderer r = new G2DRendererImpl(g2d);

            for (GraphicalObject o : objects) {
                o.render(r);
                currentState.afterDraw(r, o); //  nakon crtanja svakog objekta pozovite još i odgovarajuću metodu stanja
            }
            currentState.afterDraw(r); // te isto napravite i nakon što je nacrtan čitav crtež
            requestFocusInWindow();
        }
    }

    public class CanvasAction extends AbstractAction {

        private GraphicalObject go;
        public CanvasAction(GraphicalObject go) {
            this.go = go;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //model.addGraphicalObject(go);
            //canvas.repaint();

            currentState.onLeaving(); // prelazimo u ovo stanje
            currentState = new AddShapeState(model, go); // pritiskom na taj gumb promjeni trenutno stanje programa u primjerak stanja AddShapeState
        }
    }

}
