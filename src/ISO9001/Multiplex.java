package ISO9001;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;


public class Multiplex extends Technique {

    List<Marquage> marquageList = new ArrayList<>();
    List<List<Integer>> customList = new ArrayList<>();
    
    Multiplex(List<Marquage> list, String num, String espCible, List<List<Integer>> customList) {
        super("", "", espCible, num);
        String name = num + "$_";
        this.marquageList = list;
        for (Marquage m: list) {
            name = name + m.getName() + "_";
        }
        name = name + espCible;
        super.setName(name);
        System.out.println("Multiplex name:" + super.getName());
        this.customList = customList;
    }

    Multiplex(String path) {
        super("", "", "","");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path + "/Info.txt"));
            String line;
            int count = 0;
            super.setPath(path);
            while((line=reader.readLine())!=null) {
                String[] splitLine = line.split("#!#");
                if (splitLine[0].contains("Anticorp") && !splitLine[0].contains("Custom")) {
                    this.marquageList.add(new Marquage(Params.basePath + "/Anticorps_valid/" + splitLine[1].replace("\n", "").replace(" ", "_")));
                } else if (splitLine[0].contains("Custom")) {
                    this.marquageList.get(count).setDilution(Integer.parseInt(splitLine[1]));
                    this.marquageList.get(count).setTemp(Integer.parseInt(splitLine[2]));
                    this.marquageList.get(count).setMinPoly(Integer.parseInt(splitLine[3]));
                    this.marquageList.get(count).setMinChromo(Integer.parseInt(splitLine[4]));
                    count++;
                } else if (splitLine[0].equals("espCible")) {
                    super.setEspeceCible(splitLine[1]);
                } else if (splitLine[0].equals("num")) {
                    super.setNum(splitLine[1]);
                }
                String name = super.getNum() + "$_";
                for (Marquage m: marquageList) {
                    name = name + m.getName() + "_";
                }
                super.setName(name + super.getEspeceCible());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveExp() {
        super.setPath(Params.basePath + "/Anticorps_exp/" + super.getName());
        new File(Params.basePath + "/Anticorps_exp/" + super.getName()).mkdir();
        new File(Params.basePath + "/Anticorps_exp/" + super.getName() + "/Fiche_Travail").mkdir();
        new File(Params.basePath + "/Anticorps_exp/" + super.getName() + "/Fiche_PTBC").mkdir();
        new File(Params.basePath + "/Anticorps_exp/" + super.getName() + "/Scan").mkdir();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(Params.basePath + "/Anticorps_exp/" + super.getName()+ "/Info.txt"));
            int count = 1;
            for (Marquage m: this.marquageList) {
                writer.write("Anticorp" + String.valueOf(count) + "#!#" + m.getFullName() + "\n");
                count++;
            }
            count = 1;
            for(List<Integer> l: this.customList) {
                writer.write("AnticorpCustom" + String.valueOf(count) + "#!#" + l.get(0).toString() + "#!#" + l.get(1).toString() + "#!#" + l.get(2).toString() + "#!#" + l.get(3).toString() + "\n");
                count++;
            }
            writer.write("espCible#!#" + super.getEspeceCible() + "\n");
            writer.write("num#!#" + super.getNum());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getEsp() {
        return marquageList.get(0).getEspeceCible();
    }

    public String getFullName() {
        return super.getName();
    }
}
