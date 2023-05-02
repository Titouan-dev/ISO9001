package ISO9001;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.apache.poi.ss.formula.functions.ArrayFunction;
import org.apache.xmlbeans.impl.tool.Extension.Param;

public class Permissions {

    static List<Process> procList = new ArrayList<>();

    public static void updateUser() {
        for(String usr: getAllUser()) {
            ex("icacls " + Params.basePath + "/Users/" + " /grant " + usr + ":R /T");
        }
        for(String adm: getAdmin()) {
            ex("icacls " + Params.basePath + "/Users/" + " /grant " + adm + ":F /T");
        }
    }

    public static void updateProj(Project proj) {
        inherit(proj.getPath());
        grantSelfProj(proj);
        grantAdmin(proj);
        grantUser(proj);
        join();
    }

    private static void grantSelfProj(Project proj) {
        ex("icacls " + proj.getPath() + " /grant " + User.getUsername() + ":F /T");
    }

    private static void grantUser(Project proj) {
        for(String user: getUser(proj)) {
            ex("icacls " + proj.getPath() + " /grant " + user + ":R /T");
            ex("icacls " + proj.getPath() + "/Echange.txt" + " /grant " + user + ":F");
            ex("icacls " + proj.getPath() + "/Scan/" + " /grant " + user + ":F /T");
            ex("icacls " + proj.getPath() + "/Resultat/" + " /grant " + user + ":F /T");
            ex("icacls " + proj.getPath() + "/Echantillon/" + " /grant " + user + ":F /T");
        }
    }

    private static void grantAdmin(Project proj) {
        for(String adm: getAdmin()) {
            ex("icacls " + proj.getPath() + " /grant " + adm + ":F /T");
        }
    }

    private static void inherit(String p) {
        ex("icacls " + p + " /inheritance:r /T");
    }

    private static void ex(String command) {
        Process proc;
        try {
            proc = Runtime.getRuntime().exec(command);
            procList.add(proc);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void join() {
        for(Process p: procList) {
            try {
                p.waitFor();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    private static List<String> getAdmin() {
        File[] userList = new File(Params.basePath + "/Users/").listFiles();
        List<String> rtrnList = new ArrayList();
        for (File user: userList) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(user.toString() + "/Info.txt"));
                String line;
                while((line=reader.readLine())!=null) {
                    String[] splitLine = line.split("#!#");
                    if (splitLine[0].equals("grade") && splitLine[1].equals("Administrateur")) {
                        rtrnList.add(user.getName());
                    }
                }
                reader.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return rtrnList;
    }

    private static List<String> getUser(Project proj) {
        List<String> userList = new ArrayList<>();
        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(proj.getPath() + "/Associe.txt"));
            while((line=reader.readLine())!=null) {
                userList.add(line);
            }
            reader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return userList;
    }

    private static List<String> getAllUser() {
        List<String> userList = new ArrayList<>();
        File[] listUsr = new File(Params.basePath + "/Users/").listFiles();
        for (File f: listUsr) {
            userList.add(f.getName().toString());
        }
        return userList;
    }
}
