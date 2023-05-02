package ISO9001;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Technique {

    private SimpleStringProperty name;
    private Boolean scan;
    private SimpleStringProperty technique;
    private SimpleStringProperty grossisement;
    private SimpleStringProperty fluo;
    private Marquage marquage;
    private Multiplex multiplex;
    private SimpleIntegerProperty link;
    private boolean bleech;
    private SimpleStringProperty rev;
    private boolean isM;
    private String path;
    private String num;
    private String especeCible;

    public Technique(String n, String p, String especeCible, String num) {
        this.num = num;
        this.name = new SimpleStringProperty(n);
        this.especeCible = especeCible;
        this.path = p;
        this.link = new SimpleIntegerProperty(0);
    }

    public String getNum() {
       return this.num; 
    }

    public String getEspeceCible() {
        return this.especeCible;
    }

    public Boolean getScan() {
        return scan;
    }

    public String getGrossisement() {
        return grossisement.get();
    }

    public String getFluo() {
        return fluo.get();
    }

    public Technique getMarq() {
        return marquage;
    }

    public Integer getLink() {
        return link.get();
    }

    public String getRev() {
        return rev.get();
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

    public String getPath() {
        return this.path;
    }

    public void setNum(String n) {
        this.num = n;
    }
    
    public void setName(String n) {
        this.name = new SimpleStringProperty(n);
    }

    public void setPath(String p) {
        this.path=p;
    }

    public void setEspeceCible(String es) {
        this.especeCible = es;
    }

    public String getName() {
        return this.name.get();
    }

    public void setGrossisement(String g) {
        this.grossisement = new SimpleStringProperty(g);
    }

    public void setRevelation(String r) {
        this.rev = new SimpleStringProperty(r);
    }

}
