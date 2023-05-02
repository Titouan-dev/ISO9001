package ISO9001;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Optional;

import org.omg.CORBA.RepositoryIdHelper;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PhysicalArchive {
   String nProj;
   String type;
   String tiroir;
   String boite;
   String desc;
   Boolean fullSave;
   Boolean custom;
   String path;
   String titre;

public PhysicalArchive(Project proj, String boite, String tiroir, String type) {
    this.fullSave = true;
    this.nProj = "P." + proj.getNproject();
    this.type = type;
    this.tiroir = tiroir;
    this.boite = boite;
    this.custom = false;
    this.titre = proj.name;
    this.save();
}

public PhysicalArchive(Project proj, String desc, String type) {
    this.fullSave = false;
    this.nProj = "P." + proj.getNproject();
    this.type = type;
    this.desc = desc;
    this.custom = false;
    this.save();
}

public PhysicalArchive(File f, Boolean custom) {
    this.custom = true;
    this.fullSave = true;
    this.path = f.toString();
    try {
        BufferedReader reader = new BufferedReader(new FileReader(f));
        String line;
        while((line=reader.readLine())!=null) {
            String[] splitLine = line.split("#!#");
            if (splitLine[0].equals("type")) {
                this.type = splitLine[1];
            } else if (splitLine[0].equals("nProj")) {
                this.nProj = splitLine[1];
            } else if (splitLine[0].equals("boite")) {
                this.boite = splitLine[1];
            } else if (splitLine[0].equals("tiroir")) {
                this.tiroir = splitLine[1];
            } else if (splitLine[0].equals("titre")) {
                this.titre = splitLine[1];
            }
        }
        if (this.boite == null) {
            System.out.println("Probleme: " + f);
        }  
        reader.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

private void save() {
    try {
        BufferedWriter writer;
        String archiString;
        if (this.nProj.contains("P.")) {
            archiString = "/Archivage/";
        } else {
            archiString = "/Archivage/P.";
        }
        if (this.custom) {
            writer = new BufferedWriter(new FileWriter(this.path));
        } else {
            new File(Params.basePath + archiString + this.nProj).mkdirs();
            writer = new BufferedWriter(new FileWriter(Params.basePath + archiString + this.nProj + "/PhysicalArchive" + this.type + ".txt"));
        }
        writer.write("fullSave#!#" + this.fullSave + "\n");
        writer.write("nProj#!#" + this.nProj + "\n");
        writer.write("type#!#" + this.type + "\n");
        writer.write("titre#!#" + this.titre);
        if (this.fullSave) {
            writer.write("tiroir#!#" + this.tiroir + "\n");
            writer.write("boite#!#" + this.boite + "\n");
        } else {
            writer.write("desc#!#" + this.desc + "\n");
        }
        writer.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

public PhysicalArchive(String path) {
    this.custom = false;
    try {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line;
        while((line=reader.readLine())!=null) {
            String[] splitLine = line.split("#!#");
            if (splitLine[0].equals("fullSave")) {
                if (splitLine[1].equals("true")) {
                    this.fullSave = true;
                } else {
                    this.fullSave = false;
                }
            } else if (splitLine[0].equals("nProj")) {
                this.nProj = splitLine[1];
            }
            else if (splitLine[0].equals("type")) {
                this.type = splitLine[1];
            }
            if (this.fullSave) {
                if (splitLine[0].equals("tiroir")) {
                    this.tiroir = splitLine[1];
                }
                else if (splitLine[0].equals("boite")) {
                    this.boite = splitLine[1];
                }
            } else {
                if (splitLine[0].equals("desc")) {
                    this.desc = splitLine[1];
                    this.boite = "";
                    this.tiroir = "";
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

public String getProject() {
    return this.nProj;
}

public String getType() {
    return this.type;
}

public Button getEdit() {
    Button editBut = new Button("Modifier");
    editBut.setOnAction(e -> {
        dialArchive();
    });
    return editBut;
}

public Button getDelete() {
    Button deleteBut = new Button("Supprimer");
    deleteBut.setOnAction(e -> {
        delete();
    });
    return deleteBut;
}

private Boolean dialArchive() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Archivage");
        dialog.setHeaderText("Modifier emplacement:");
        HBox globHBox = new HBox();
        globHBox.setSpacing(15);
        Label boiteLab = new Label("Boite:");
        Label tiroirLab = new Label("Tiroir:");
        ComboBox<String> boiteCombo = new ComboBox<>();
        for (int i=1; i<StockagePhysique.getNumberBoite()+1;i++) {
            boiteCombo.getItems().add("Boite " + i);
        }
        boiteCombo.setValue("Boite " + this.boite);
        ComboBox<String> tiroirCombo = new ComboBox<>();
        tiroirCombo.getItems().addAll("1a", "1b", "2a", "2b", "3a", "3b", "4a", "4b");
        tiroirCombo.setValue(this.tiroir);
        globHBox.getChildren().addAll(boiteLab, boiteCombo, tiroirLab, tiroirCombo);
        dialog.getDialogPane().setContent(globHBox);
        
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> option = dialog.showAndWait();

        if (option.get() == ButtonType.CANCEL) {
            dialog.close();
            return false;
        } else {
            this.boite = boiteCombo.getValue().split(" ")[1];
            this.tiroir = tiroirCombo.getValue();
            this.save();
            PhysicalArchiveLoader.load();
            return true;
        }
    }

    private Boolean delete() {
        if (this.custom) {
            new File(this.path).delete();
            PhysicalArchiveLoader.load();
            return true;
        }
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Suppression de l'archive physisque");
        dialog.setHeaderText("Commentaire:");
        TextField comField = new TextField();
        dialog.getDialogPane().setContent(comField);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> option = dialog.showAndWait();
        if (option.get() == ButtonType.CANCEL) {
            dialog.close();
            return false;
        } else {
            if (comField.getText().equals("")) {
                File[] listFile = new File(Params.basePath + "/Archivage/P." + this.nProj).listFiles();
                for (File f: listFile) {
                    if (f.getName().contains("PhysicalArchive") && f.getName().contains(this.type)) {
                        new File(f.toString()).delete();
                        return true;
                    }
                }
            }
            this.fullSave = false;
            this.desc = comField.getText();
            this.save();
            PhysicalArchiveLoader.load();
            return true;
        }
    }

}
