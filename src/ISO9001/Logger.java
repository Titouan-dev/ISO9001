package ISO9001;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {

    public static void logConnect() {
        writer(Params.basePath + "/Users/" + User.getUsername() + "/Log/", "Connection utilisateur");
    }

    public static void logOpenFile(Project proj, String file) {
        writer(Params.basePath + "/Users/" + User.getUsername() + "/Log/", "Consultation de P." + proj.getNumber() + " / " + file);
        writer(proj.getPath() + "/Log/", User.getUsername() + " a ouvert " + file);
    }

    public static void logAno(Project proj) {
        writer(Params.basePath + "/Users/" + User.getUsername() + "/Log/", "Anonimisation de P." + proj.getNumber());
        writer(proj.getPath() + "/Log/", User.getUsername() + " a anonymisez");
    }

    public static void logNote(Project proj) {
        writer(Params.basePath + "/Users/" + User.getUsername() + "/Log/", "A ecrit une note dans P." + proj.getNumber());
        writer(proj.getPath() + "/Log/", User.getUsername() + " a laisser une note"); 
    }

    public static void logEnregistrement(Project proj) {
        writer(Params.basePath + "/Users/" + User.getUsername() + "/Log/", "A crée le nouveau projet P." + proj.getNumber());
        writer(proj.getPath() + "/Log/", User.getUsername() + " a crée le projet");
    }

    public static void logEdit(Project proj) {
        writer(Params.basePath + "/Users/" + User.getUsername() + "/Log/", "A edité le projet P." + proj.getNumber());
        writer(proj.getPath() + "/Log/", User.getUsername() + " a edité le projet");
    }

    public static void logSuiviTK(Project proj) {
        writer(Params.basePath + "/Users/" + User.getUsername() + "/Log/", "A consulté le SuiviTK de P." + proj.getNumber());
        writer(proj.getPath() + "/Log/", User.getUsername() + " a consulter le SuiviTK");
    }

    private static void writer(String path, String data) {
        try {
            String date = java.time.LocalDate.now().toString();
            String time = java.time.LocalTime.now().toString();
            System.out.println(path + date + ".txt");
            new File(path).mkdir();
            new File(path + date + ".txt").createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(path + date + ".txt", true));
            writer.write("[" + time + "]:\t" + data + "\n");
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    public static void logGeneral(String msg) {
        writer(Params.basePath + "/Log/", msg);
    }
}
