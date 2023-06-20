package lab4;

import lab4.graphics.GraphicalObject;
import lab4.graphics.Point;
import lab4.listener.DocumentModelListener;
import lab4.listener.GraphicalObjectListener;

import java.util.*;

public class DocumentModel {
    public final static double SELECTION_PROXIMITY = 10;

    // Kolekcija svih grafičkih objekata:
    private List<GraphicalObject> objects;
    // Read-Only proxy oko kolekcije grafičkih objekata:
    private List<GraphicalObject> roObjects;
    // Kolekcija prijavljenih promatrača:
    private List<DocumentModelListener> listeners;
    // Kolekcija selektiranih objekata:
    private List<GraphicalObject> selectedObjects;
    // Read-Only proxy oko kolekcije selektiranih objekata:
    private List<GraphicalObject> roSelectedObjects;

    // Promatrač koji će biti registriran nad svim objektima crteža...
    private final GraphicalObjectListener goListener = new GraphicalObjectListener() {

        @Override
        public void graphicalObjectChanged(GraphicalObject go) {
            notifyListeners();
        }

        @Override
        public void graphicalObjectSelectionChanged(GraphicalObject go) {
            int index = selectedObjects.indexOf(go);
            if (go.isSelected()) {
                // go je selektiran i nije još u listi -- dodaj
                if (index == -1) {
                    selectedObjects.add(go);
                }
                //go je selektiran i go je već u listi -- skip
                else {
                    return;
                }
            } else {
                // go nije selektiran i nije u listi -- skip
                if (index == -1) {
                    return;
                }
                // go nije selektiran i je u listi -- ukloni
                else {
                    selectedObjects.remove(go);
                }
            }
            notifyListeners();
        }
    };


    // Konstruktor...
    public DocumentModel() {
        objects = new ArrayList<GraphicalObject>();
        roObjects = Collections.unmodifiableList(objects);
        listeners = new ArrayList<DocumentModelListener>();
        selectedObjects = new ArrayList<GraphicalObject>();
        roSelectedObjects = Collections.unmodifiableList(selectedObjects);
    }

    // Brisanje svih objekata iz modela (pazite da se sve potrebno odregistrira)
    // i potom obavijeste svi promatrači modela
    public void clear() {
        for (GraphicalObject go : objects) {
            go.removeGraphicalObjectListener(goListener);
        }
        objects.clear();
        selectedObjects.clear();
        notifyListeners();
    }

    // Dodavanje objekta u dokument (pazite je li već selektiran; registrirajte model kao promatrača)
    public void addGraphicalObject(GraphicalObject obj) {
        if (obj.isSelected()) {
            selectedObjects.add(obj);
        }
        objects.add(obj);
        obj.addGraphicalObjectListener(goListener);
        notifyListeners();
    }

    // Uklanjanje objekta iz dokumenta (pazite je li već selektiran; odregistrirajte model kao promatrača)
    public void removeGraphicalObject(GraphicalObject obj) {
        obj.setSelected(false);
        objects.remove(obj);
        obj.removeGraphicalObjectListener(goListener);
        notifyListeners();
    }

    // Vrati nepromjenjivu listu postojećih objekata (izmjene smiju ići samo kroz metode modela)
    public List list() {
        return roObjects;
    }

    // Prijava...
    public void addDocumentModelListener(DocumentModelListener l) {
        listeners.add(l);
    }

    // Odjava...
    public void removeDocumentModelListener(DocumentModelListener l) {
        listeners.remove(l);
    }

    // Obavještavanje...
    public void notifyListeners() {
        for (DocumentModelListener l : listeners) {
            l.documentChange();
        }
    }

    // Vrati nepromjenjivu listu selektiranih objekata
    public List getSelectedObjects() {
        return roSelectedObjects;
    }

    // Pomakni predani objekt u listi objekata na jedno mjesto kasnije...
    // Time će se on iscrtati kasnije (pa će time možda veći dio biti vidljiv)
    public void increaseZ(GraphicalObject go) {
        int index = objects.indexOf(go);
        if (index != -1 && index < objects.size() - 1) {
            objects.set(index, objects.get(index+1));
            objects.set(index+1, go);
        }
        notifyListeners();
    }

    // Pomakni predani objekt u listi objekata na jedno mjesto ranije...
    public void decreaseZ(GraphicalObject go) {
        int index = objects.indexOf(go);
        if (index != -1 && index > 0) {
            objects.set(index, objects.get(index-1));
            objects.set(index-1, go);
        }
        notifyListeners();
    }

    // Pronađi postoji li u modelu neki objekt koji klik na točku koja je
    // predana kao argument selektira i vrati ga ili vrati null. Točka selektira
    // objekt kojemu je najbliža uz uvjet da ta udaljenost nije veća od
    // SELECTION_PROXIMITY. Status selektiranosti objekta ova metoda NE dira.
    public GraphicalObject findSelectedGraphicalObject(Point mousePoint) {
        Map<Double, GraphicalObject> moguceSelekcije = new HashMap<Double, GraphicalObject>();
        for (GraphicalObject go : objects) {
            double udaljenost = go.selectionDistance(mousePoint);
            if (udaljenost <= SELECTION_PROXIMITY) {
                moguceSelekcije.put(udaljenost, go);
            }
        }

        // ako je vise selektiranih vrati najblizi
        if (!moguceSelekcije.isEmpty()) {
            double min = SELECTION_PROXIMITY + 1;
            for (Map.Entry<Double, GraphicalObject> entry : moguceSelekcije.entrySet()) {
                if (entry.getKey() < min) {
                    min = entry.getKey();
                }
            }
            return moguceSelekcije.get(min);
        }
        return null;
    }

    // Pronađi da li u predanom objektu predana točka miša selektira neki hot-point.
    // Točka miša selektira onaj hot-point objekta kojemu je najbliža uz uvjet da ta
    // udaljenost nije veća od SELECTION_PROXIMITY. Vraća se indeks hot-pointa
    // kojeg bi predana točka selektirala ili -1 ako takve nema. Status selekcije
    // se pri tome NE dira.
    public int findSelectedHotPoint(GraphicalObject object, Point mousePoint) {
        Map<Integer, Double> moguceSelekcije = new HashMap<Integer, Double>();
        for (int i = 0; i < object.getNumberOfHotPoints(); i++) {
            double udaljenost = object.getHotPointDistance(i, mousePoint);
            if (udaljenost <= SELECTION_PROXIMITY) {
                moguceSelekcije.put(i, udaljenost);
            }
        }
        if (!moguceSelekcije.isEmpty()) {
            return Collections.min(moguceSelekcije.entrySet(), Map.Entry.comparingByValue()).getKey();
        }
        return -1;
    }

    public void deselectAllSelectedObjects() {
        while (selectedObjects.size() > 0) {
            selectedObjects.get(0).setSelected(false);
        }
    }
}
