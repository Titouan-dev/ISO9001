package ISO9001.Dialog;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.security.GuardedObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import ISO9001.Excel_util;
import ISO9001.Marquage;
import ISO9001.Params;
import javafx.geometry.Orientation;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class dialogAnticorpValid {

    public static List<String> launcher(int d, String n, String prot, String esp, String fourni, String rev, String ref, Marquage selected) {
        List<String> res = dial(0, n, prot, esp, fourni, rev, ref, selected);
        if (res.size() == 1) {
            return res;
        }
        for(int i=0; i<6; i++) {
            if (res.get(i).equals("")) {
                return launcher(1, n, prot, esp, fourni, rev, ref, selected);
            }
        }
        return res;
    }

    private static List<String> dial(int i, String n, String prot, String esp, String fourni, String rev, String ref, Marquage selectedMrq) {
        Dialog<ButtonType> dialog = new Dialog<>();
        List<Map<String, String>> data = Excel_util.readReact(esp, rev);
        VBox globVbox = new VBox();
        globVbox.setSpacing(20);
        dialog.setTitle("Input Dialog");
        HBox diluantHBox = new HBox();
        diluantHBox.setSpacing(10);
        HBox dilutionHBox = new HBox();
        dilutionHBox.setSpacing(10);
        HBox anticorpHBox = new HBox();
        anticorpHBox.setSpacing(20);
        Label anticorpTitle = new Label("Anticorps Primaire:");
        TextField anticorpNameField = new TextField(n + " " + prot + " " + esp.charAt(0) + " " + fourni + " " + ref);
        anticorpHBox.getChildren().addAll(anticorpTitle, anticorpNameField);
        anticorpTitle.setStyle("-fx-font-weight: bold");
        Label dilutionLab = new Label("Dilution:");
        Label diluantLab = new Label("Diluant:");
        TextField dilutionField = new TextField();
        TextField diluantField = new TextField();
        diluantHBox.getChildren().addAll(diluantLab, diluantField);
        dilutionHBox.getChildren().addAll(dilutionLab, dilutionField);
        HBox tempHBox = new HBox();
        tempHBox.setSpacing(10);
        Label tempLab = new Label("Temps d'incubation:");
        Label minLab = new Label("minutes");
        TextField tempField = new TextField();
        tempHBox.getChildren().addAll(tempLab, tempField, minLab);
        HBox antiHBox = new HBox();
        antiHBox.setSpacing(10);
        Label antiLab = new Label("Anticorp retriever:");
        Label tpLab = new Label("TP:");
        Label temperaturLab = new Label("T°:");
        ToggleGroup groupAr = new ToggleGroup();
        RadioButton citraRad = new RadioButton("Citrate");
        RadioButton edtaRad = new RadioButton("EDTA");
        citraRad.setToggleGroup(groupAr);
        edtaRad.setToggleGroup(groupAr);
        TextField tpField = new TextField();
        TextField temperaturField = new TextField();
        Separator sepa = new Separator();
        HBox systemTitleHBox = new HBox();
        systemTitleHBox.setSpacing(20);
        Label systemLab = new Label("Systeme de visualisation:");
        TextField systemField = new TextField(n + " " + prot + " " + esp.charAt(0) + " " + rev);
        systemTitleHBox.getChildren().addAll(systemLab, systemField);
        systemLab.setStyle("-fx-font-weight: bold");
        GridPane leftGrid = new GridPane();
        leftGrid.setHgap(20);
        leftGrid.setVgap(20);
        GridPane rightGrid = new GridPane();
        rightGrid.setHgap(20);
        rightGrid.setVgap(20);
        Label refLab = new Label("Ref");
        Label refLab2 = new Label("Ref");
        Label minLabel = new Label("Min");
        Label minLabel2 = new Label("Min");
        Label nms = new Label("Normal Goat Serum");
        TextField refNMS = new TextField(data.get(1).get("normal"));
        TextField minNMS = new TextField(data.get(0).get("normal"));
        refNMS.setMaxWidth(75);
        minNMS.setMaxWidth(50);
        Label peroxLab = new Label("Perox block");
        TextField refPerox = new TextField(data.get(1).get("perox"));
        TextField minPerox = new TextField(data.get(0).get("perox"));
        refPerox.setMaxWidth(75);
        minPerox.setMaxWidth(50);
        Label polyLab = new Label("Polymères");
        TextField refPoly = new TextField(data.get(1).get("poly"));
        TextField minPoly = new TextField(data.get(0).get("poly"));
        refPoly.setMaxWidth(75);
        minPoly.setMaxWidth(50);
        HBox systemHBox = new HBox();
        systemHBox.setSpacing(20);
        Separator sep = new Separator();
        sep.setOrientation(Orientation.VERTICAL);
        Label chromoLab = new Label("Chromogène");
        TextField refChromo = new TextField(data.get(1).get("chromo"));
        TextField minChromo = new TextField(data.get(0).get("chromo"));
        refChromo.setMaxWidth(75);
        minChromo.setMaxWidth(50);
        Label hemaLab = new Label("Hematoxylline");
        TextField refHema = new TextField(data.get(1).get("hemma"));
        TextField minHema = new TextField(data.get(0).get("hemma"));
        refHema.setMaxWidth(75);
        minHema.setMaxWidth(50);
        leftGrid.add(refLab, 2, 1);
        leftGrid.add(minLabel, 3, 1);
        leftGrid.add(nms, 1,3);
        leftGrid.add(refNMS,2,3);
        leftGrid.add(minNMS,3,3);
        leftGrid.add(peroxLab, 1,2);
        leftGrid.add(refPerox, 2, 2);
        leftGrid.add(minPerox, 3, 2);
        leftGrid.add(polyLab, 1, 4);
        leftGrid.add(refPoly, 2, 4);
        leftGrid.add(minPoly, 3, 4);
        rightGrid.add(refLab2, 2, 1);
        rightGrid.add(minLabel2, 3, 1);
        rightGrid.add(chromoLab, 1, 2);
        rightGrid.add(refChromo, 2,2);
        rightGrid.add(minChromo, 3, 2);
        rightGrid.add(hemaLab, 1, 3);
        rightGrid.add(refHema, 2, 3);
        rightGrid.add(minHema, 3, 3);
        Label errorLab = new Label("Information invalide");
        errorLab.setTextFill(Color.RED);
        if (i == 1) { 
            errorLab.setVisible(true);
        }
        else {
            errorLab.setVisible(false);
        }
        systemHBox.getChildren().addAll(leftGrid, sep, rightGrid);
        antiHBox.getChildren().addAll(antiLab, citraRad, edtaRad, tpLab, tpField, temperaturLab, temperaturField);
        globVbox.getChildren().addAll(anticorpHBox, diluantHBox, dilutionHBox, tempHBox, antiHBox, sepa, systemTitleHBox, systemHBox);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(globVbox);
        Optional<ButtonType> option = dialog.showAndWait();

        if (option.get() == ButtonType.OK) {
            String name = n + " " + prot + " " + esp.charAt(0) + " " + fourni + " " + ref;
            List<String> ret = new ArrayList<>();
            ret.add(diluantField.getText());
            ret.add(dilutionField.getText());
            ret.add(tempField.getText());
            RadioButton selected = (RadioButton) groupAr.getSelectedToggle();
            save(diluantField.getText(), dilutionField.getText(), tempField.getText(), selected.getText(), tpField.getText(), temperaturField.getText(), refNMS.getText(), minNMS.getText(), refPerox.getText(), minPerox.getText(), refPoly.getText(), minPoly.getText(), refChromo.getText(), minChromo.getText(), refHema.getText(), minHema.getText(), name, selectedMrq);
            ret.add(selected.getText());
            ret.add(tpField.getText());
            ret.add(temperaturField.getText());
            return ret;
        }
        if (option.get() == ButtonType.CANCEL) {
            List<String> ret = new ArrayList<>();
            ret.add("");
            return ret;
        }
        return null;
    }

    private static void save(String diluant, String dilution, String tempIncub, String retriver, String tp, String temperature, String refNMS, String minNMS, String refPerox, String minPerox, String refPoly, String minPoly, String refHomo, String minHomo, String refHema, String minHema, String name, Marquage marq) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(Params.basePath + "/Anticorps_exp/" + name.replace(" ", "_") + "/Info_Valid.txt"));
            writer.write("diluant#!#" + diluant + "\n");
            writer.write("dilution#!#" + dilution + "\n");
            writer.write("tempIncub#!#" + tempIncub + "\n");
            writer.write("retriver#!#" + retriver + "\n");
            writer.write("tp#!#" + tp + "\n");
            writer.write("temperature#!#" + temperature + "\n");
            writer.write("refNMS#!#" + refNMS + "\n");
            writer.write("minNMS#!#" + minNMS + "\n");
            writer.write("refPerox#!#" + refPerox + "\n");
            writer.write("minPerox#!#" + minPerox + "\n");
            writer.write("refPoly#!#" + refPoly + "\n");
            writer.write("minPoly#!#" + minPoly + "\n");
            writer.write("refChromo#!#" + refHomo + "\n");
            writer.write("minChromo#!#" + minHomo + "\n");
            writer.write("refHema#!#" + refHema + "\n");
            writer.write("minHema#!#" + minHema + "\n");
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
}
