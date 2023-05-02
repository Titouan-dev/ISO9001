package ISO9001.Dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Optional;

import ISO9001.Marquage;
import ISO9001.MarquageEnrg;
import ISO9001.MarquageLoader;
import ISO9001.Multiplex;
import ISO9001.Technique;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class dialogMarquage {
   
    private static final String Dialog = null;
    private static final String TextField = null;

    public static Technique launcher() {
        return dial();
    }

    private static Technique dial() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Input Dialog");
        dialog.setHeaderText("Ajouter un marquage:");
        Label especeLab = new Label("Espèce:");
        ComboBox especeCombo = new ComboBox<>();
        especeCombo.getItems().addAll("Human", "Mouse", "Other");
        Label revLab = new Label("Révélation:");
        ComboBox revCombo = new ComboBox<>();
        revCombo.getItems().addAll("DAB", "AEC", "Vinagreen", "HRP Magenta", "Fluo");
        revCombo.setValue("DAB");
        HBox revHBox = new HBox();
        revHBox.setSpacing(15);
        revHBox.getChildren().addAll(revLab, revCombo);
        Label marquageLab = new Label("Marquage:");
        ComboBox marquageCombo = new ComboBox<>();
        List<Technique> mrqList = new ArrayList<>();
        marquageCombo.getItems().add("Numerisation seul");
        Label scanLab = new Label("Numériser le marquage?");
        HBox autreHBox = new HBox();
        autreHBox.setSpacing(10);
        Label autreNameLab = new Label("Nom:");
        autreNameLab.setVisible(false);
        TextField autreNameField = new TextField();
        autreNameField.setVisible(false);
        autreHBox.getChildren().addAll(autreNameLab, autreNameField);
        CheckBox scanBox = new CheckBox();
        VBox globVBox = new VBox();
        globVBox.setPadding(new Insets(30, 30, 30, 30));
        VBox marquageVBox = new VBox();
        marquageVBox.setSpacing(30);
        HBox marquageHBox = new HBox();
        marquageHBox.setSpacing(15);
        HBox checkHBox = new HBox();
        checkHBox.setSpacing(30);
        HBox especeHBox = new HBox();
        especeHBox.setSpacing(30);
        especeHBox.getChildren().addAll(especeLab, especeCombo);
        marquageHBox.getChildren().addAll(marquageLab, marquageCombo);
        checkHBox.getChildren().addAll(scanLab, scanBox);
        marquageVBox.getChildren().addAll(especeHBox, revHBox, marquageHBox, checkHBox);
        globVBox.getChildren().add(marquageVBox);
        globVBox.setPrefHeight(500);
        dialog.getDialogPane().setContent(globVBox);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // scanSetup
        Label grossLab = new Label("Grossisement:");
        Label techLab = new Label("Révélation:");
        ToggleGroup grossGroup = new ToggleGroup();
        RadioButton vinghtRad = new RadioButton("20x");
        RadioButton quaranteRad = new RadioButton("40x");
        RadioButton soixanteRad = new RadioButton("63x");
        RadioButton centRad = new RadioButton("100x");
        vinghtRad.setToggleGroup(grossGroup);
        quaranteRad.setToggleGroup(grossGroup);
        soixanteRad.setToggleGroup(grossGroup);
        centRad.setToggleGroup(grossGroup);
        RadioButton brightRad = new RadioButton("BrightField");
        RadioButton phaseRad = new RadioButton("Contraste-de-phase");
        RadioButton polaRad = new RadioButton("Lumière-polarizé");
        RadioButton fluoRad = new RadioButton("Fluorescence");
        ToggleGroup revGroup = new ToggleGroup();
        brightRad.setToggleGroup(revGroup);
        phaseRad.setToggleGroup(revGroup);
        polaRad.setToggleGroup(revGroup);
        fluoRad.setToggleGroup(revGroup);
        ObservableList<String> fluoChoice = FXCollections.observableArrayList("DAPI", "A488", "A555", "A594", "A647");
        ComboBox fluoCombo = new ComboBox<String>(fluoChoice);
        fluoCombo.setVisible(false);
        VBox globgrosVBox = new VBox();
        VBox globTechVBox = new VBox();
        VBox techVBox = new VBox();
        VBox grosVBox = new VBox();
        HBox scanHBox = new HBox();
        scanHBox.setSpacing(50);
        globVBox.setSpacing(30);
        techVBox.setSpacing(10);
        grosVBox.setSpacing(10);
        globgrosVBox.setSpacing(10);
        globTechVBox.setSpacing(10);
        techVBox.setPadding(new Insets(0, 0, 0, 10));
        techVBox.getChildren().addAll(brightRad, phaseRad, polaRad, fluoRad, fluoCombo);
        grosVBox.setPadding(new Insets(0, 0, 0, 10));
        grosVBox.getChildren().addAll(vinghtRad, quaranteRad, soixanteRad, centRad);
        globgrosVBox.getChildren().addAll(grossLab, grosVBox);
        globTechVBox.getChildren().addAll(techLab, techVBox);
        globgrosVBox.setVisible(false);
        globTechVBox.setVisible(false);
        scanHBox.getChildren().addAll(globgrosVBox, globTechVBox);
        globVBox.getChildren().add(scanHBox);

        especeCombo.setOnAction(e -> {
            if (especeCombo.getValue().toString().equals("Human")) {
                marquageCombo.getItems().clear();
                for(Marquage m: filterMarquageRevelation(MarquageLoader.listHuman, revCombo.getValue().toString())) {
                    marquageCombo.getItems().add(m.getFullName());
                    mrqList.add(m);
                }
                for (Multiplex m: MarquageLoader.listMultiplex) {
                    if (m.getEsp().equals("Human")) {
                        marquageCombo.getItems().add(m.getFullName());
                        mrqList.add(m);
                    }
                }
                marquageCombo.getItems().add("Numerisation seul");
            } else if (especeCombo.getValue().toString().equals("Mouse")) {
                marquageCombo.getItems().clear();
                for(Marquage m: filterMarquageRevelation(MarquageLoader.listMouse, revCombo.getValue().toString())) {
                    marquageCombo.getItems().add(m.getFullName());
                    mrqList.add(m);
                }
                for (Multiplex m: MarquageLoader.listMultiplex) {
                    if (m.getEsp().equals("Mouse")) {
                        marquageCombo.getItems().add(m.getFullName());
                        mrqList.add(m);
                    }
                }
                marquageCombo.getItems().add("Numerisation seul");
            } else if (especeCombo.getValue().toString().equals("Other")) {
                marquageCombo.getItems().clear();
                for(Marquage m: filterMarquageRevelation(MarquageLoader.listOther, revCombo.getValue().toString())) {
                    marquageCombo.getItems().add(m.getFullName());
                    mrqList.add(m);
                }
                for(Multiplex m: MarquageLoader.listMultiplex) {
                    if(m.getEsp().equals("Other")) {
                        marquageCombo.getItems().add(m.getFullName());
                        mrqList.add(m);
                    }
                }
                marquageCombo.getItems().add("Numerisation seul");
            }
        });

        revCombo.setOnAction(e -> {
            especeCombo.fireEvent(e);
        });

        marquageCombo.setOnAction(e -> {
            if (marquageCombo.getValue().toString().equals("Numerisation seul")) {
                autreNameLab.setVisible(true);
                autreNameField.setVisible(true);
                scanBox.setSelected(true);
                marquageVBox.getChildren().remove(checkHBox);
                marquageVBox.getChildren().addAll(autreHBox, checkHBox);
                globTechVBox.setVisible(true);
                globgrosVBox.setVisible(true);
            } else if  (mrqList.get(marquageCombo.getSelectionModel().getSelectedIndex()) instanceof Marquage) {
                Marquage m = (Marquage)mrqList.get(marquageCombo.getSelectionModel().getSelectedIndex());
                if (m.isColo()) {
                    scanBox.setSelected(true);
                    marquageVBox.getChildren().remove(checkHBox);
                    marquageVBox.getChildren().addAll(autreHBox, checkHBox);
                    brightRad.setSelected(true);
                    globTechVBox.setVisible(true);
                    globgrosVBox.setVisible(true);
                }
            } else {
                autreNameLab.setVisible(false);
                autreNameField.setVisible(false);
                marquageVBox.getChildren().remove(autreHBox);
                scanBox.setSelected(false);
                globTechVBox.setVisible(false);
                globgrosVBox.setVisible(false);
            }
        });

        fluoRad.setOnAction(e -> {
            if (fluoRad.isSelected()) {
                fluoCombo.setVisible(true);
            } else {
                fluoCombo.setVisible(false);
            }
        });

        brightRad.setOnAction(e -> {
            if (brightRad.isSelected()) {
                fluoCombo.setVisible(false);
            }
        });

        phaseRad.setOnAction(e -> {
            if (phaseRad.isSelected()) {
                fluoCombo.setVisible(false);
            }
        });

        polaRad.setOnAction(e -> {
            if (polaRad.isSelected()) {
                fluoCombo.setVisible(false);
            }
        });

        scanBox.setOnAction(e -> {
            if(scanBox.isSelected()) {
                globgrosVBox.setVisible(true);
                vinghtRad.setSelected(true);
            }
            else {
                globgrosVBox.setVisible(false);
            }

        });

        Optional<ButtonType> option = dialog.showAndWait();
        if(option.get() == ButtonType.OK) {
            if(marquageCombo.getValue().toString().equals("Numerisation seul")) {
                RadioButton selectedRev = (RadioButton) revGroup.getSelectedToggle();
                if (revGroup.getSelectedToggle().toString().equals("Fluorescence")) {
                    Marquage mrq = new Marquage(autreNameField.getText(), autreNameField.getText(), especeCombo.getSelectionModel().getSelectedItem().toString(), "x", "Custom", "0000", "x", "0", "x", selectedRev.getText(), fluoCombo.getValue().toString(), "x", true);
                    if (scanBox.isSelected()) {
                        RadioButton gross = (RadioButton) grossGroup.getSelectedToggle();
                        mrq.setGrossisement(gross.getText());
                        mrq.setRevelation(selectedRev.getText());
                        return mrq;
                    } else {;
                        return mrq;
                    }
                    
                } else {
                    Marquage mrq = new Marquage(autreNameField.getText(), "0", especeCombo.getSelectionModel().getSelectedItem().toString(), "None", "Custom", "x", "0", "x", selectedRev.getText(), "x", "x", "x", true);
                    //Marquage mrq = new Marquage(autreNameField.getText(), autreNameField.getText(), especeCombo.getSelectionModel().getSelectedItem().toString(), "x", "Custom", "0000", "x", "0", "x", selectedRev.getText(), "x", "x",true);
                    if (scanBox.isSelected()) {
                        RadioButton gross = (RadioButton) grossGroup.getSelectedToggle();
                        mrq.setGrossisement(gross.getText());
                        mrq.setRevelation(selectedRev.getText());
                        return mrq;
                    }
                }
            }
            else if (scanBox.isSelected()) {
                if (mrqList.get(marquageCombo.getSelectionModel().getSelectedIndex()) instanceof Marquage) {
                    Marquage m = (Marquage)mrqList.get(marquageCombo.getSelectionModel().getSelectedIndex());
                    if (m.isColo()) {
                        RadioButton gross = (RadioButton) grossGroup.getSelectedToggle();
                        RadioButton rev = (RadioButton) revGroup.getSelectedToggle();
                        m.setGrossisement(gross.getText());
                        m.setRevelation(rev.getText());
                        return m;
                    } else {
                        RadioButton gross = (RadioButton) grossGroup.getSelectedToggle();
                        m.setGrossisement(gross.getText());
                        m.setRevelation(m.getRevelation());
                        return m;
                    }
                } else {
                    RadioButton gross = (RadioButton) grossGroup.getSelectedToggle();
                    Multiplex m = (Multiplex)mrqList.get(marquageCombo.getSelectionModel().getSelectedIndex());
                    m.setGrossisement(gross.getText());
                    m.setRevelation("NA");
                    return m;
                }
            }
            else {
                try {
                    Marquage m = (Marquage)mrqList.get(marquageCombo.getSelectionModel().getSelectedIndex());
                    m.setGrossisement("NA");
                    m.setRevelation("NA");
                    return m;
                } catch (Exception e) {
                    Multiplex m = (Multiplex)mrqList.get(marquageCombo.getSelectionModel().getSelectedIndex());
                    m.setGrossisement("NA");
                    m.setRevelation("NA");
                    return m; 
                }
            }
        }
        else if (option.get() == ButtonType.CANCEL) {

        }
        return null;
    }

    public static List<Marquage> filterMarquageRevelation(List<Marquage> mList, String rev) {
        List<Marquage> res = new ArrayList<>();
        for(Marquage m: mList) {
            if (m.isColo()) {
                res.add(m);
            } else if (m.getRevelation().equals(rev)) {
                res.add(m);
            }
        }
        return res;
    }
}
