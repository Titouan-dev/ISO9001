package ISO9001;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.xmlbeans.impl.tool.Extension.Param;

import javafx.beans.property.SimpleStringProperty;

public class Associe {

    private SimpleStringProperty name;
    private SimpleStringProperty prenom;
    private SimpleStringProperty mail;
    private String username;
    private boolean isReferent;
    private Boolean pending;

    public Associe(String n, String p, String m) {
        this.name = new SimpleStringProperty(n);
        this.prenom = new SimpleStringProperty(p);
        this.mail = new SimpleStringProperty(m);
        this.username = m.split("@")[0];
        this.isReferent = false;
        this.pending = false;
    }

    public Associe(String n, String p, String m, Boolean pen) {
        this.name = new SimpleStringProperty(n);
        this.prenom = new SimpleStringProperty(p);
        this.mail = new SimpleStringProperty(m);
        this.username = m.split("@")[0];
        this.isReferent = false;
        this.pending = pen;
    }

    public Associe(String username) {
        try {
            BufferedReader reader;
            try {
                reader = new BufferedReader(new FileReader(Params.basePath + "/Users/" + username + "/Info.txt"));
            } catch (Exception e) {
                reader = new BufferedReader(new FileReader(Params.basePath + "/PendingUsers/" + username + "/Info.txt"));
            }
            String line;
            while((line=reader.readLine()) != null) {
                String[] splitLine = line.split("#!#");
                if (splitLine[0].equals("username")) {
                    this.username = splitLine[1];
                } 
                else if (splitLine[0].equals("nom")) {
                    this.name = new SimpleStringProperty(splitLine[1]);
                }
                else if (splitLine[0].equals("prenom")) {
                    this.prenom = new SimpleStringProperty(splitLine[1]);
                }
                else if (splitLine[0].equals("mail")) {
                    this.mail = new SimpleStringProperty(splitLine[1]);
                }
            }
            this.isReferent = true;
            this.pending = false;
            reader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setReferent(Boolean r) {
        this.isReferent = r;
    }

    public String getName() {
        return name.get();
    }

    public String getPrenom() {
        return prenom.get();
    }

    public String getMail() {
        return mail.get();
    }

    public String getUsername() {
        return this.username;
    }
}
