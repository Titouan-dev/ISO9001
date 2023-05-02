package ISO9001.Dialog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import ISO9001.Associe;
import ISO9001.Params;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class dialogDemandeur {

   public static List launcher(Integer i) {
      List<String> res = dial(i);
      if (res.get(0).equals("") && res.get(1).equals("") && res.get(2).equals("")) {
         return res;
      }
      if (checkNumeric(res.get(0))) {
         return launcher(1);
      }
      if (checkNumeric(res.get(1))) {
         return launcher(2);
      }
      if (!res.get(2).contains("@")) {
         return launcher(3);
      }
      return res;
   }

   private static List dial(Integer i) {
      Dialog<ButtonType> dialog = new Dialog<>();
      dialog.setTitle("Input Dialog");
      dialog.setHeaderText("Information du demandeur:");
      Label nameLab = new Label("Nom:");
      Label prenomLab = new Label("Prenom:");
      Label mailLab = new Label("Mail:");
      ButtonType okBut = new ButtonType("OK");
      ButtonType annuBut = new ButtonType("Annuler");
      TextField nameField = new TextField();
      TextField prenomField = new TextField();
      TextField mailField = new TextField();
      Label error;
      VBox dialVBox = new VBox();
      dialVBox.setAlignment(Pos.CENTER);
      dialVBox.setSpacing(30);
      HBox dialHBox = new HBox();
      dialHBox.setSpacing(20);
      dialHBox.getChildren().addAll(nameLab, nameField, prenomLab, prenomField, mailLab, mailField);
      Separator sep = new Separator();
      ComboBox enregUser = new ComboBox<>();
      enregUser.getItems().addAll(read());
      dialVBox.getChildren().addAll(dialHBox, sep, enregUser);
      if(i == 1) {
         error = new Label("Nom invalide");
         error.setTextFill(Color.RED);
         dialVBox.getChildren().add(error);
      }
      if(i == 2) {
         error = new Label("Prenom invalide");
         error.setTextFill(Color.RED);
         dialVBox.getChildren().add(error);
      }
      if(i==3) {
         error = new Label("Mail invalide");
         error.setTextFill(Color.RED);
         dialVBox.getChildren().add(error);
      }
      dialog.getDialogPane().setContent(dialVBox);
      dialog.getDialogPane().getButtonTypes().addAll(okBut, annuBut);
      nameField.requestFocus();
      Optional<ButtonType> option = dialog.showAndWait();
      List<String> ret = new ArrayList<>();
      if(option.get() == okBut) {
         if (nameField.getText().equals("") && prenomField.getText().equals("") && mailField.getText().equals("")) {
            String[] splitLine = enregUser.getSelectionModel().getSelectedItem().toString().split(" | ");
            System.out.println(splitLine.length);
            ret.add(splitLine[0]);
            ret.add(splitLine[2]);
            ret.add(splitLine[4]);
            System.out.println(ret);
         } else {
            ret.add(nameField.getText());
            ret.add(prenomField.getText());
            ret.add(mailField.getText());
            save(ret);
         }
         return ret;
      }
      if(option.get() == annuBut) {
       ret.add("");
       ret.add("");  
       ret.add("");  
       return ret;
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

   private static void save(List<String> lst) {
      String w = "";
      for (String s: lst) {
         w = w + s + "#!#";
      }
      try {
         BufferedWriter writer = new BufferedWriter(new FileWriter(Params.basePath + "/Annuaire.txt", true));
         writer.write(w + "\n");
         writer.close();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private static List<String> read() {
      List<String> u = new ArrayList<>();
      try {
         BufferedReader reader = new BufferedReader(new FileReader(Params.basePath + "/Annuaire.txt"));
         String line;
         while((line=reader.readLine()) != null) {
            String[] splitLine = line.split("#!#");
            u.add(splitLine[0] + " | " + splitLine[1] + " | " + splitLine[2]);
         }
         reader.close();
      } catch (Exception e) {
         e.printStackTrace();
      }
      return u;
   }
}
