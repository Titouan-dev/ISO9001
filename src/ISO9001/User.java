package ISO9001;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.math3.ode.MultistepIntegrator;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Alert.AlertType;

public class User {

    public static String username;
    public static String nom;
    public static String prenom;
    public static String mail;
    public static String grade;
    public static List<Project> projList = new ArrayList<>();
    public static List<pendingReport> pendingList = new ArrayList<>();
    public static String supervisor;
    
    public User() {
        this.username = System.getProperty("user.name");
        this.mail = username + "@cgfl.fr";
        try {
            Process p = Runtime.getRuntime().exec("net user " + username + " /domain");
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            p.waitFor();
            String s = null;
            for (int i=0; i<3; i++) {
                stdInput.readLine();
            }
            String out = stdInput.readLine();
            String[] split_out = out.replace("  ", "#").split("#");
            this.nom = split_out[split_out.length - 1].split(" ")[0];
            this.prenom = split_out[split_out.length - 1].split(" ")[1];
            readUserConfig();
            loadProject();
            Logger.logConnect();
        } catch (Exception e) {}    
    }

    public User(String n, String p, String m) {
        if (m.contains("cgfl")) {
            this.username = m.split("@")[0];
        }
        this.nom = n;
        this.prenom = p;
        this.mail = m;
        readUserConfig();
        loadProject();
    }

    public User(String path) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(path + "/Info.txt"));
            String line;
            while((line=reader.readLine()) != null) {
                String[] splitLine = line.split("#!#");
                if (splitLine[0].equals("username")) {
                    this.username = splitLine[1];
                }
                else if(splitLine[0].equals("nom")) {
                    this.nom = splitLine[1];
                }
                else if(splitLine[0].equals("prenom")) {
                    this.prenom = splitLine[1];
                }
                else if(splitLine[0].equals("mail")) {
                    this.mail = splitLine[1];
                }
                else if(splitLine[0].equals("grade")) {
                    this.grade = splitLine[1];
                }
                else if (splitLine[0].equals("supervisor")) {
                    this.supervisor = splitLine[1];
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    public static void createUser(String username, String nom, String prenom, String mail, String grade, String supervisor) {
        try {
            new File(Params.basePath + "/Users/" + username).mkdir();
            new File(Params.basePath + "/Users/" + username + "/Log").mkdir();
            new File(Params.basePath + "/Users/" + username + "/Info.txt").createNewFile();
            new File(Params.basePath + "/Users/" + username + "/Project.txt").createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(Params.basePath + "/Users/" + username + "/Info.txt"));
            writer.write("username#!#" + username + "\n");
            writer.write("nom#!#" + nom + "\n");
            writer.write("prenom#!#" + prenom + "\n");
            writer.write("mail#!#" + mail + "\n");
            writer.write("grade#!#" + grade + "\n");
            writer.write("supervisor#!#" + supervisor + "\n");
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void pendingUser(String u, String n, String p, String m) {
        try {
            new File(Params.basePath + "/PendingUsers/" + u).mkdir();
            new File(Params.basePath + "/PendingUsers/" + u + "/Log").mkdir();
            new File(Params.basePath + "/PendingUsers/" + u + "/Info.txt").createNewFile();
            new File(Params.basePath + "/PendingUsers/" + u + "/Project.txt").createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(Params.basePath + "/PendingUsers/" + u + "/Info.txt"));
            writer.write("username#!#" + u + "\n");
            writer.write("nom#!#" + n + "\n");
            writer.write("prenom#!#" + p + "\n");
            writer.write("mail#!#" + m + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readUserConfig() {
        if (checkUserExistence()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(Params.basePath + "/Users/" + this.getUsername() + "/Info.txt"));
                String line;
                while((line=reader.readLine()) != null) {
                    String[] splitLine = line.split("#!#");
                    if (splitLine[0].equals("grade")) {
                        this.grade = splitLine[1];
                    }
                }
                reader.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else {
            if (checkPending()) {
                dialAprob();
            } else {
                Boolean res = dial(username);
                if (res == false) {
                    System.exit(0);
                }
                if (res == true) {
                    pendingUser(this.username, this.nom, this.prenom, this.mail);
                    dialAprob();
                }
            }
        }
    }

    private static void dialAprob() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("ISO90001");
        dialog.setContentText("Un administrateur doit aprouver votre profils.");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        Optional<ButtonType> option = dialog.showAndWait();

        if (option.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    private static boolean checkUserExistence() {
        File[] listUserFile = new File(Params.basePath + "/Users/").listFiles();
        for(File f: listUserFile) {
            String[] nameSplit = f.toString().split("\\\\");
            String name = nameSplit[nameSplit.length - 1];
            if(name.toLowerCase().equals(username.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkPending() {
        File[] listPending = new File(Params.basePath + "/PendingUsers/").listFiles();
        for(File f: listPending) {
            String[] nameSplit = f.toString().split("\\\\");
            String name = nameSplit[nameSplit.length -1];
            if(name.equals(username)) {
                return true;
            }
        }
        return false;
    }

    private static boolean dial(String username) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Nouvelle utilisateur");
        alert.setContentText("L'utilisateur " + username + " n'est pas connu.\nVoulez-vous le crée?");
        alert.setHeaderText("");
        Optional<ButtonType> option = alert.showAndWait(); 
        if (option.get() == ButtonType.OK) {
            return true;
        }
        if (option.get() == ButtonType.CANCEL) {
            return false;
        }
        return false;
    }

    public static void loadProject() {
        System.out.println("Loading user proj");
        projList.clear();
        if (User.grade.equals("User")) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(Params.basePath + "/Users/" + username + "/Project.txt"));
                String line;
                while((line=reader.readLine()) != null) {
                    String[] splitLine = line.split("#!#");
                    if (!splitLine[0].equals("")) {
                        projList.add(new Project(splitLine[0]));
                    }
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            File[] list = new File(Params.basePath + "/Project/").listFiles();
            for(File f: list) {
                Project newProj  = new Project(f.toString());
                if (newProj.getNumber() != null) {
                    projList.add(new Project(f.toString()));
                }
            }
        }
        if (User.grade.equals("Administrateur")) {
            File[] listPending = new File(Params.basePath + "/pendingReport").listFiles();
            for (File f: listPending) {
                pendingList.add(new pendingReport(f.toString()));
            }
        }
    }

    public static String getUsername() {
        return username;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
