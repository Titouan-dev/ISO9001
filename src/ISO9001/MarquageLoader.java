package ISO9001;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class MarquageLoader {

    public static List<Marquage> listExp = new ArrayList<>();
    public static List<Marquage> listHuman = new ArrayList<>();
    public static List<Marquage> listMouse = new ArrayList<>();
    public static List<Marquage> listOther = new ArrayList<>();
    public static List<Marquage> listValid = new ArrayList<>();
    public static List<Multiplex> listMultiplexExp = new ArrayList<>();
    public static List<Multiplex> listMultiplex = new ArrayList<>();

    public static void load() {
        File[] listFile = new File(Params.basePath + "/Anticorps_valid").listFiles();
        if (listFile == null) {
            listHuman = new ArrayList<Marquage>();
            listMouse = new ArrayList<Marquage>();
            listOther = new ArrayList<Marquage>();
        }
        else {
            for(File f: listFile) {
                try {
                    if (f.toString().contains("$")) {
                        listMultiplex.add(new Multiplex(f.toString()));
                    } else {
                        BufferedReader reader = new BufferedReader(new FileReader(f.toString() + "/Info.txt"));
                        String line;
                        while((line=reader.readLine()) != null) {
                            String[] splitLine = line.split("#!#");
                            if(splitLine[0].equals("especeCible")) {
                                if(splitLine[1].equals("Human")) {
                                    listHuman.add(new Marquage(f.toString()));
                                } else if (splitLine[1].equals("Mouse")) {
                                    listMouse.add(new Marquage(f.toString()));
                                } else {
                                    listOther.add(new Marquage(f.toString()));
                                }
                            }
                        }
                    }
                } catch (Exception e) {}
            }
        }

        
        // Load color
        File[] lisFiles = new File(Params.basePath + "/Coloration").listFiles();
        for (File f: lisFiles) {
            Marquage colo = new Marquage(f.toString());
            listHuman.add(colo);
            listMouse.add(colo);
            listOther.add(colo);
        }
        listValid.addAll(listHuman);
        listValid.addAll(listMouse);
        listValid.addAll(listOther);
        
    }

    public static void loadExp() {
        listExp = new ArrayList<>();
        listMultiplexExp = new ArrayList<>();
        File[] listFile = new File(Params.basePath + "/Anticorps_exp").listFiles();
        if (listFile == null) {
            listExp = new ArrayList<Marquage>();
        }
        else {
            for(File f: listFile) {
                if (f.toString().contains("$")) {
                    listMultiplexExp.add(new Multiplex(f.toString()));
                } else {
                    listExp.add(new Marquage(f.toString()));
                }
            }
        }
    }
    
}
