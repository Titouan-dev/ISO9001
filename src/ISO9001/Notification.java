package ISO9001;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.xmlbeans.impl.tool.Extension.Param;

public class Notification {
    public static List<String> note = new ArrayList<>();
    public static void load() {
        String path = Params.basePath + "/Users/" + User.getUsername() + "/notification.txt";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line;
            while((line=reader.readLine())!=null) {
                String[] splitLine = line.split("#!#");
                if (splitLine[0].equals("Note")) {
                    note.add(splitLine[1]);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void update() {
        if (note.size() > 0) {
           Main.setTabColor("Edition");
           //Browse.reloadBrowser(User.projList);
        }
    }

    public static void deleteNoteNotification(Project proj, String user) {
        File[] userList = new File(Params.basePath + "/Users/").listFiles();
        for(File f: userList) {
            Boolean check = false;
            List<String> file = new ArrayList<>();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(f + "/Notification.txt"));
                String line;
                while((line=reader.readLine())!=null) {
                    if (line.equals("Note#!#" + proj.nProject)) {
                        check = true;
                    } else {
                        file.add(line);
                    }
                }
                reader.close();
                if (check) {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(f + "/Notification.txt"));
                    for (String l: file) {
                        writer.write(l + "\n");
                    }
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void addNoteNotification(Project proj, String user) {
        File[] userList = new File(Params.basePath + "/Users/").listFiles();
        for(File f: userList) {
            Boolean check = false;
            if (!f.getAbsoluteFile().equals(user)) {
                try {
                    try {
                        BufferedReader reader = new BufferedReader(new FileReader(f.toString() + "/Notification.txt"));
                        String line;
                        while((line=reader.readLine())!=null) {
                            if (line.equals("Note#!#" + proj.nProject)) {
                                reader.close();
                                check = true;
                                break;
                            }
                        }
                        reader.close();
                    } catch (Exception e ) {
                        e.printStackTrace();
                    }
                    if (check == false) {
                        BufferedWriter writer = new BufferedWriter(new FileWriter(f.toString() + "/Notification.txt", true));
                        writer.write("Note#!#" + proj.nProject + "\n");
                        writer.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
