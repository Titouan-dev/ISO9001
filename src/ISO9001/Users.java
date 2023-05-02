package ISO9001;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


import javafx.beans.property.SimpleStringProperty;

public class Users {

    SimpleStringProperty user;
    SimpleStringProperty name;
    SimpleStringProperty surname;
    SimpleStringProperty mail;
    SimpleStringProperty role;
    SimpleStringProperty supervisor;

    public Users(String path) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path + "/"));
            String line;
            while((line=reader.readLine()) != null) {
                String[] splitLine = line.split("#!#");
                if (splitLine[0].equals("username")) {
                    this.user = new SimpleStringProperty(splitLine[1]);
                }
                else if (splitLine[0].equals("nom")) {
                    this.name = new SimpleStringProperty(splitLine[1]);
                }
                else if (splitLine[0].equals("prenom")) {
                    this.surname = new SimpleStringProperty(splitLine[1]);
                }
                else if(splitLine[0].equals("mail")) {
                    this.mail = new SimpleStringProperty(splitLine[1]);
                }
                else if (splitLine[0].equals("grade")) {
                    this.role = new SimpleStringProperty(splitLine[1]);
                }
                else if (splitLine[0].equals("supervisor")) {
                    this.supervisor = new SimpleStringProperty(splitLine[1]);
                }
            }
            reader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Users(String user, String name, String surname, String mail, String role, String supervisor) {
        this.user = new SimpleStringProperty(user);
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
        this.mail = new SimpleStringProperty(mail);
        this.role = new SimpleStringProperty(role);
        this.supervisor = new SimpleStringProperty(supervisor);
    }

    public Users(String nom, String prenom, String mail) {
        this.name = new SimpleStringProperty(nom);
        this.surname = new SimpleStringProperty(prenom);
        this.mail = new SimpleStringProperty(mail);
    }

    public void save() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(Params.basePath + "/Users/" + this.user.get() + "/Info.txt"));
            writer.write("username#!#" + this.user.get() + "\n");
            writer.write("nom#!#" + this.name.get() + "\n");
            writer.write("prenom#!#" + this.surname.get() + "\n");
            writer.write("mail#!#" + this.mail.get() + "\n");
            writer.write("grade#!#" + this.role.get() + "\n");
            writer.write("supervisor#!#" + this.supervisor.get());
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getUser() {
        try {
            return this.user.get();
        } catch (Exception e) {
            return null;
        }
    }

    public String getName() {
        return this.name.get();
    }

    public String getSurname() {
        return this.surname.get();
    }

    public String getMail() {
        return this.mail.get();
    }

    public String getRole() {
        return this.role.get();
    }

    public String getSupervisor() {
        return this.supervisor.get();
    }

    public void setUser(String u) {
        this.user = new SimpleStringProperty(u);
    }

    public void setName(String n) {
        this.name = new SimpleStringProperty(n);
    }

    public void setSurname(String s) {
        this.surname = new SimpleStringProperty(s);
    }

    public void setMail(String m) {
        this.mail = new SimpleStringProperty(m);
    }

    public void setRole(String r) {
        this.role = new SimpleStringProperty(r);
    }

    public void setSupervisor(String r ) {
        this.supervisor = new SimpleStringProperty(r);
    }
}
