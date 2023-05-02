package ISO9001;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.effect.Bloom;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class MarquageEnrg {
    private SimpleStringProperty name;
    private Boolean scan;
    private SimpleStringProperty technique;
    private SimpleStringProperty grossisement;
    private SimpleStringProperty fluo;
    private Marquage marquage;
    private SimpleIntegerProperty link;
    private boolean bleech;
    private SimpleStringProperty rev;

    public MarquageEnrg(String name, String g, String r) {
        this.name = new SimpleStringProperty(name);
    }

    public MarquageEnrg(Marquage n, String g, String rev) {
        this.link = new SimpleIntegerProperty(0);
        this.marquage = n;
        this.name = new SimpleStringProperty(n.getName());
        this.scan = true;
        this.technique = new SimpleStringProperty(n.getRevelation());
        this.grossisement = new SimpleStringProperty(g);
        this.fluo = new SimpleStringProperty(n.chanel);
        if (rev.equals("NA")) {
            this.rev = null;
        } else {
            this.rev = new SimpleStringProperty(rev);
        }
    }

    public MarquageEnrg(Marquage n) {
        this.link = new SimpleIntegerProperty(0);
        this.marquage = n;
        this.name = new SimpleStringProperty(n.getName());
        this.scan = false;
        this.technique = new SimpleStringProperty(n.getRevelation());
        this.grossisement = new SimpleStringProperty("");
        this.fluo = new SimpleStringProperty(n.chanel);
    }

    public String getName() {
        return name.get();
    }

    public Boolean getScan() {
        return scan;
    }

    public String getTechnique() {
        try {
            if (technique.get().toString().equals("Fluorescence")) {
                return technique.get() + " (" + fluo.get() + ")";    
            }
            else {
                return technique.get();
            }
        } catch (Exception e) {
            return null;
        }
    }

    public String getGrossisement() {
        return grossisement.get();
    }

    public String getFluo() {
        return fluo.get();
    }

    public Marquage getMarquage() {
        return this.marquage;
    }

    public Integer getLink() {
        return link.get();
    }

    public String getRev() {
        return this.rev.get();
    }

    public void setLink(int i) {
        this.link = new SimpleIntegerProperty(i);
    }

    public void setBleeched(boolean b) {
        this.bleech = b;
    }

    public boolean getBleeched() {
        return this.bleech;
    }
}
