package ISO9001;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Marquage extends Technique {
    String especeHote;
    String fourniseur;
    String reference;
    String clonalite;
    String nMonoclonale;
    String antigene;
    String revelation;
    String chanel;
    String tissu;
    String diluant;
    int dilution;
    String incubation;
    String tp;
    int temp;
    Boolean custom;
    Boolean colorisation;
    int minPoly;
    int minChromo;

    public Marquage(String n, String num, String ec, String eh, String f, String r, String c, String numC, String a, String rev, String ch, String t, Boolean custom) {
        super(n, Params.basePath + "/Anticorps_exp/" + num + "_" + n + "_" + ec.charAt(0) + "_" + f + "_" + r, ec, num);
        this.especeHote = eh;
        this.fourniseur = f;
        this.reference = r;
        this.clonalite = c;
        this.nMonoclonale = numC;
        this.antigene = a;
        this.revelation = rev;
        this.chanel = ch;
        this.tissu = t;
        this.custom = custom;
        this.colorisation = false;
    }

    public Marquage(String path) {
        super("", "", "", "");
        String name= "";
        String paths = "";
        String especeCible = "";
        String num = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path + "/Info.txt"));
            String line;
            while ((line=reader.readLine()) != null) {
                String[] splitLine = line.split("#!#");
                if (splitLine[0].equals("name")) {
                    name = splitLine[1];
                }
                else if (splitLine[0].equals("especeCible")) {
                    especeCible = splitLine[1];
                }
                else if (splitLine[0].equals("especeHote")) {
                    this.especeHote = splitLine[1];
                }
                else if (splitLine[0].equals("fourniseur")) {
                    this.fourniseur = splitLine[1];
                }
                else if (splitLine[0].equals("reference")) {
                    this.reference = splitLine[1];
                }
                else if (splitLine[0].equals("clonalite")) {
                    this.clonalite = splitLine[1];
                }
                else if (splitLine[0].equals("nMonoclonale")) {
                    this.nMonoclonale = splitLine[1];
                }
                else if (splitLine[0].equals("antigene")) {
                    this.antigene = splitLine[1];
                }
                else if (splitLine[0].equals("revelation")) {
                    this.revelation = splitLine[1];
                }
                else if (splitLine[0].equals("chanel")) {
                    this.chanel = splitLine[1];
                }
                else if (splitLine[0].equals("tissu")) {
                    this.tissu = splitLine[1];
                }
                else if (splitLine[0].equals("path")) {
                    paths = splitLine[1];
                }
                else if (splitLine[0].equals("num")) {
                    num = splitLine[1];
                }
                else if (splitLine[0].equals("custom")) {
                    if (splitLine[1].equals("false")) {
                        this.custom = false;
                    } else {
                        this.custom = true;
                    }
                }
                else if (splitLine[0].equals("colo")) {
                    if (splitLine[1].equals("false")) {
                        this.colorisation = false;
                    } else {
                        this.colorisation = true;
                    }
                }
            }
            reader.close();
            if (new File(path + "/Info_Valid.txt").exists()) {
                reader = new BufferedReader(new FileReader(path + "/Info_Valid.txt"));
                while((line=reader.readLine())!=null) {
                    String[] splitLine = line.split("#!#");
                    if (splitLine[0].equals("dilution")) {
                        this.dilution = Integer.parseInt(splitLine[1].split("/")[1]);
                    } else if (splitLine[0].equals("minPoly")) {
                        this.minPoly = Integer.parseInt(splitLine[1].replace(" ", "").replace(".0", ""));
                    } else if (splitLine[0].equals("tempIncub")) {
                        this.temp = Integer.parseInt(splitLine[1]);
                    } else if (splitLine[0].equals("minChromo")) {
                        this.minChromo = Integer.parseInt(splitLine[1].replace(" ", "").replace(".0", ""));
                    }
                }
                reader.close();
            }
            super.setName(name);
            super.setNum(num);
            super.setPath(paths);
            super.setEspeceCible(especeCible);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveCustom(String path) {
        super.setPath(path + super.getName() + "_" + revelation.replace(" ", "_") + "_custom");
        try{
            new File(path + "/" + super.getName() + "_" + revelation + "_custom").mkdirs();
            new File(path + "/" + super.getName() + "_" + revelation.replace(" ", "_") + "_custom/Info.txt").createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(path + "/" + super.getName() + "_" + revelation.replace(" ", "_") + "_custom/Info.txt"));
            writer.write("name#!#" + super.getName() + "#!#\n");
            writer.write("especeCible#!#" + super.getEspeceCible() + "#!#\n");
            writer.write("especeHote#!#" + this.especeHote + "#!#\n");
            writer.write("fourniseur#!#" + this.fourniseur + "#!#\n");
            writer.write("reference#!#" + this.reference + "#!#\n");
            writer.write("clonalite#!#" + this.clonalite + "#!#\n");
            writer.write("nMonoclonale#!#" + this.nMonoclonale + "#!#\n");
            writer.write("antigene#!#" + this.antigene + "#!#\n");
            writer.write("revelation#!#" + this.revelation + "#!#\n");
            writer.write("chanel#!#" + this.chanel + "#!#\n");
            writer.write("tissu#!#" + this.tissu + "#!#\n");
            writer.write("path#!#" + super.getPath() + "#!#\n");
            writer.write("num#!#" + super.getNum() + "#!#\n");
            writer.write("custom#!#" + this.custom + "\n");
            writer.write("colo#!#" + this.colorisation);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveExp() {
        String basePath = Params.basePath + "/Anticorps_exp/" + super.getNum() + "_" + super.getName() + "_" + super.getEspeceCible().charAt(0) + "_" + this.fourniseur + "_" + this.reference;
        new File(basePath).mkdirs();
        new File(basePath + "/Scan").mkdir();
        new File(basePath + "/Fiche_Travail").mkdir();
        new File(basePath + "/Fiche_fournisseur").mkdir();
        new File(basePath + "/Fiche_PTBC").mkdir();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(basePath + "/Info.txt"));
            writer.write("name#!#" + super.getName() + "#!#\n");
            writer.write("especeCible#!#" + super.getEspeceCible() + "#!#\n");
            writer.write("especeHote#!#" + this.especeHote + "#!#\n");
            writer.write("fourniseur#!#" + this.fourniseur + "#!#\n");
            writer.write("reference#!#" + this.reference + "#!#\n");
            writer.write("clonalite#!#" + this.clonalite + "#!#\n");
            writer.write("nMonoclonale#!#" + this.nMonoclonale + "#!#\n");
            writer.write("antigene#!#" + this.antigene + "#!#\n");
            writer.write("revelation#!#" + this.revelation + "#!#\n");
            writer.write("chanel#!#" + this.chanel + "#!#\n");
            writer.write("tissu#!#" + this.tissu + "#!#\n");
            writer.write("path#!#" + super.getPath() + "#!#\n");
            writer.write("num#!#" + super.getNum() + "\n");
            writer.write("custom#!#" + this.custom + "\n");
            writer.write("colo#!#" + this.colorisation + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getEspeceHote() {
        return this.especeHote;
    }

    public String getFourniseur() {
        return this.fourniseur;
    }

    public String getReference() {
        return this.reference;
    }

    public String getRevelation() {
        return this.revelation;
    }

    public boolean isColo() {
        return this.colorisation;
    }

    public String getFullName() {
        if (this.colorisation) {
            return super.getNum() + " " + super.getName() + " " + this.reference + " " + this.fourniseur;
        } else {
            return super.getNum() + " " + super.getName() + " " + super.getEspeceCible().charAt(0) + " " + this.fourniseur + " " + this.reference;
        }
    }

    public String getSystemVisu() {
        if (!this.colorisation) { 
            return this.getNum() + " " + this.getName() + " " + super.getEspeceCible().charAt(0) + " " + this.revelation;
        } else {
            return "";
        }
    }

    public void setDiluant(String val) {
        this.diluant = val;
    }

    public void setIncubation(String val) {
        this.incubation = val;
    }

    public void setAntigene(String val) {
        this.antigene = val;
    }

    public void setTp(String val) {
        this.tp = val;
    } 

    public void setTemp(int val) {
        this.temp = val;
    }

    public void setMinPoly(int val) {
        this.minPoly = val;
    }

    public void setMinChromo(int val) {
        this.minChromo = val;
    }

    public void setDilution(int val) {
        this.dilution = val;
    }

    public boolean isCustom() {
        if (this.custom) {
            return true;
        } else {
            return false;
        }
    }
}
