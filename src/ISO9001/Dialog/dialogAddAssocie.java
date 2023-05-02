package ISO9001.Dialog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Optional;

import ISO9001.Associe;
import ISO9001.Params;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class dialogAddAssocie {
    
    static Associe ass;
    static List<List<String>> lst = new ArrayList<>();

    public static Associe launcher(Integer i) {
        List<String> res = dial(i);
        if (res.get(0).equals("") && res.get(1).equals("") && res.get(2).equals("") && res.get(3).equals("")) {
            return null;
        }
        if(checkNumeric(res.get(0))) {
            return launcher(1);
        }
        if(checkNumeric(res.get(1))) {
            return launcher(2);
        }
        if(!res.get(2).contains("@")) {
            return launcher(3);
        }
        ass = new Associe(res.get(0), res.get(1), res.get(2));
        return new Associe(res.get(0), res.get(1), res.get(2), true);
    }

    public static Associe dialChoose() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Input Dialog");
        dialog.setHeaderText("Ajouter un associer:");
        ButtonType dejaBut = new ButtonType("Déjà enregistrer");
        ButtonType pasBut = new ButtonType("Pas enregistrer");
        dialog.getDialogPane().getButtonTypes().addAll(dejaBut, pasBut);
        Optional<ButtonType> option = dialog.showAndWait();
        if (option.get() == dejaBut) {
            selectAssocier();
        }
        if(option.get() == pasBut) {
            launcher(0);
        }
         return ass;
    }

    private static void selectAssocier() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Input Dialog");
        dialog.setHeaderText("Choissisez l'associer à ajouter au projet:");
        ComboBox comboAs = new ComboBox<>(getComboAssocier());
        comboAs.setPrefHeight(50);
        comboAs.setPrefWidth(75);
       dialog.getDialogPane().setContent(comboAs);
       dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL); 
       Optional<ButtonType> option = dialog.showAndWait();
       if(option.get() == ButtonType.OK) {
            int idx = comboAs.getSelectionModel().getSelectedIndex();
             ass = new Associe(lst.get(idx).get(1), lst.get(idx).get(2), lst.get(idx).get(3));
       }
    }

    public static ObservableList<String> getComboAssocier() {
        File[] userList = new File(Params.basePath + "/Users/").listFiles();
        ObservableList<String> obsList = FXCollections.observableArrayList();
        for (File f: userList) {
            List<String> res = readAssocierInfo(f);
            lst.add(res);
            obsList.add(res.get(1) + " " + res.get(2) + " " + res.get(0));
        }
        return obsList;
    }

    private static List<String> readAssocierInfo(File f) {
        String line;
        String username = null;
        String nom = null;
        String prenom = null;
        String mail = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(f.toString() + "/Info.txt"));
            while((line=reader.readLine()) != null) {
                String[] splitLine = line.split("#!#");
                if(splitLine[0].equals("username")) {
                    username = splitLine[1];
                }
                else if(splitLine[0].equals("nom")) {
                    nom = splitLine[1];
                }
                else if(splitLine[0].equals("prenom")) {
                    prenom = splitLine[1];
                }
                else if(splitLine[0].equals("mail")) {
                    mail = splitLine[1]; 
                }
            }
            if (username != null && nom != null && prenom != null && mail != null) {
                List<String> res = new ArrayList<>();
                res.add(username);
                res.add(nom);
                res.add(prenom);
                res.add(mail);
                return res;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    private static List<String> dial(Integer i) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Input Dialog");
        dialog.setHeaderText("Information du nouvelle associer:");
        Label nameLab = new Label("Nom:");
        Label prenomLab = new Label("Prenom");
        Label mailLab = new Label("Mail:");
        TextField nameField = new TextField();
        TextField prenomField = new TextField();
        TextField mailField = new TextField();
        VBox dialVBox = new VBox();
        dialVBox.setAlignment(Pos.CENTER);
        dialVBox.setSpacing(30);
        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);
        gridPane.setVgap(10);
        gridPane.add(nameLab,1,1);
        gridPane.add(nameField,1,2);
        gridPane.add(prenomLab,2,1);
        gridPane.add(prenomField,2,2);
        gridPane.add(mailLab,1,3);
        gridPane.add(mailField,1,4);
        dialVBox.getChildren().add(gridPane);
        if(i == 1) {
            Label error = new Label("Nom invalide");
            error.setTextFill(Color.RED);
            dialVBox.getChildren().add(error);
        }
        if(i == 2) {
            Label error = new Label("Prenom invalide");
            error.setTextFill(Color.RED);
            dialVBox.getChildren().add(error);
        }
        if(i == 3) {
            Label error = new Label("Mail invalide");
            error.setTextFill(Color.RED);
            dialVBox.getChildren().add(error);
        }
        if(i == 4) {
            Label error = new Label("Rôle invalide");
            dialVBox.getChildren().add(error);
        }
        dialog.getDialogPane().setContent(dialVBox);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> option = dialog.showAndWait();
        List<String> res = new ArrayList<>();
        if(option.get() == ButtonType.OK) {
            res.add(nameField.getText());
            res.add(prenomField.getText());
            res.add(mailField.getText());
            return res;
        }
        if (option.get() == ButtonType.CANCEL) {
            res.add("");
            res.add("");
            res.add("");
            res.add("");
            return res;
        }
        return null;
    }

    private static Boolean checkNumeric(String field) {
      char[] chars = field.toCharArray();
      for(char i: chars) {
         if (Character.isDigit(i)) {
            return true;
         }
      }
      return false;
   }
    
}
