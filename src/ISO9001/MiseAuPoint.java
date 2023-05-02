package ISO9001;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import javax.imageio.ImageIO;

import org.apache.pdfbox.tools.PDFBox;
import org.apache.xmlbeans.impl.xb.xsdschema.SelectorDocument;

import ISO9001.Dialog.dialogAnticorpValid;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.WritableRaster;
import java.awt.image.DataBufferByte;
import javafx.application.Platform;

public class MiseAuPoint {

    private static ObservableList<VBox> listViewMono = FXCollections.observableArrayList();
    private static ObservableList<VBox> listViewMulti = FXCollections.observableArrayList();
    private static List<Marquage> listMrq = new ArrayList<>();
    private static ListView<VBox> list= new ListView<VBox>();
    private static List<columnMulti> listColumn = new ArrayList<>();
    private static boolean edit = false;
    private static String editPath = null;
    private static String anticNum = "";

    public static VBox build() {

        reloadList();
        
        VBox globVBox = new VBox();
        globVBox.setSpacing(20);
        //globVBox.setAlignment(Pos.CENTER);
        VBox inputVBox = new VBox();
        inputVBox.setPadding(new Insets(50, 30, 0, 70));
        inputVBox.setSpacing(25);
        anticNum = getNum();
        Label numLab = new Label("Protocole N°:\tD." + anticNum);
        numLab.setStyle("-fx-font-weight: bold");
        numLab.setPadding(new Insets(10,400,10,450));
        
        HBox cibleHBox = new HBox();
        cibleHBox.setSpacing(30);
        Label cibleLab = new Label("Cible:");
        TextField cibleField = new TextField();
        cibleHBox.getChildren().addAll(cibleLab, cibleField);
        
        HBox espcibHBox = new HBox();
        espcibHBox.setSpacing(30);
        HBox esphoHbox = new HBox();
        esphoHbox.setSpacing(30);
        Label espCibleLab = new Label("Espèce cible:");
        Label espHoteLab = new Label("Epsèce hote:");
        ToggleGroup groupEspCible = new ToggleGroup();
        RadioButton humanCibleRad = new RadioButton("Human");
        RadioButton mouseCibleRad = new RadioButton("Mouse");
        RadioButton autreCibleRad = new RadioButton("Autre");
        humanCibleRad.setToggleGroup(groupEspCible);
        mouseCibleRad.setToggleGroup(groupEspCible);
        autreCibleRad.setToggleGroup(groupEspCible);
        TextField autreCibleField = new TextField();
        autreCibleField.setVisible(false);
        espcibHBox.getChildren().addAll(espCibleLab, humanCibleRad, mouseCibleRad, autreCibleRad, autreCibleField);
        ToggleGroup groupEspHote = new ToggleGroup();
        RadioButton mouseHoRad = new RadioButton("Mouse");
        RadioButton rabbitHoRad = new RadioButton("Rabbit");
        RadioButton autreHoRad = new RadioButton("Autre");
        mouseHoRad.setToggleGroup(groupEspHote);
        rabbitHoRad.setToggleGroup(groupEspHote);
        autreHoRad.setToggleGroup(groupEspHote);
        TextField autreHoField = new TextField();
        autreHoField.setVisible(false);
        esphoHbox.getChildren().addAll(espHoteLab, mouseHoRad, rabbitHoRad, autreHoRad, autreHoField);
        HBox fourniHBox = new HBox();
        fourniHBox.setSpacing(30);
        Label fourniLab = new Label("Fournisseur:");
        Label refLab = new Label("Référence:");
        TextField fourniField = new TextField();
        TextField refField = new TextField();
        fourniHBox.getChildren().addAll(fourniLab, fourniField, refLab, refField);
        HBox clonaHBox = new HBox();
        clonaHBox.setSpacing(30);
        Label clonLab = new Label("Clonalité:");
        ToggleGroup groupClon = new ToggleGroup();
        RadioButton polyRad = new RadioButton("Polyclonale");
        RadioButton monoRad = new RadioButton("Monoclonale");
        polyRad.setToggleGroup(groupClon);
        monoRad.setToggleGroup(groupClon);
        TextField monoField = new TextField();
        monoField.setVisible(false);
        clonaHBox.getChildren().addAll(clonLab, polyRad, monoRad, monoField);
        HBox antiRetHBox = new HBox();
        antiRetHBox.setSpacing(30);
        Label antiRetLab = new Label("Antigène Retrival:");
        ToggleGroup groupRet = new ToggleGroup();
        RadioButton edtaRad = new RadioButton("EDTA");
        RadioButton citraRad = new RadioButton("Citrate");
        edtaRad.setToggleGroup(groupRet);
        citraRad.setToggleGroup(groupRet);
        antiRetHBox.getChildren().addAll(antiRetLab, edtaRad, citraRad);
        HBox techHBox = new HBox();
        techHBox.setSpacing(30);
        Label techLab = new Label("Revelation:");
        ToggleGroup groupTech = new ToggleGroup();
        RadioButton dabRad = new RadioButton("DAB");
        RadioButton aecRad = new RadioButton("AEC");
        RadioButton fluoRad = new RadioButton("Fluo");
        RadioButton vinaRad = new RadioButton("Vinagreen");
        RadioButton impactRad = new RadioButton("ImmPACT Vector Red");
        RadioButton magentaHrp = new RadioButton("HRP Magenta");
        ObservableList<String> fluoChoice = FXCollections.observableArrayList("DAPI", "A488", "A555", "A594", "A647");
        ComboBox fluoCombo = new ComboBox<String>(fluoChoice);
        fluoCombo.setVisible(false);
        dabRad.setToggleGroup(groupTech);
        aecRad.setToggleGroup(groupTech);
        fluoRad.setToggleGroup(groupTech);
        vinaRad.setToggleGroup(groupTech);
        impactRad.setToggleGroup(groupTech);
        magentaHrp.setToggleGroup(groupTech);
        techHBox.getChildren().addAll(techLab, dabRad, aecRad, vinaRad, impactRad, magentaHrp, fluoRad, fluoCombo);
        HBox tissuHBox = new HBox();
        tissuHBox.setSpacing(30);
        Label tissuLab = new Label("Tissu contrôle:");
        TextField tissuField = new TextField();
        Button genBut = new Button("Génerer");
        genBut.translateXProperty().set(250);
        genBut.setPrefHeight(40);
        genBut.setPrefWidth(75);
        tissuHBox.getChildren().addAll(tissuLab, tissuField, genBut);
        inputVBox.getChildren().addAll(cibleHBox, espcibHBox, esphoHbox, fourniHBox, clonaHBox, antiRetHBox, techHBox, tissuHBox);
        Separator sep = new Separator();

        VBox listVBox = new VBox();
        listVBox.setSpacing(20);
        listVBox.setPadding(new Insets(0, 70, 20, 70));
        HBox listHBox = new HBox();
        listHBox.setSpacing(10);
        Button editBut = new Button("Edit");
        Button valBut = new Button("Valid");
        Button rejBut = new Button("Reject");
        Button updateBut = new Button("Update Reactifs");
        if (!User.grade.equals("User")) {
            listHBox.getChildren().addAll(editBut, valBut, rejBut, updateBut);
        } else {
            listHBox.getChildren().addAll(editBut, valBut, rejBut);
        }
        list.setItems(listViewMono);
        listVBox.getChildren().addAll(listHBox, list);

        ToggleGroup radioGroup = new ToggleGroup();
        RadioButton monoRadio = new RadioButton("MonoMarquage");
        RadioButton multiRadio = new RadioButton("Multiplex");
        monoRadio.setToggleGroup(radioGroup);
        multiRadio.setToggleGroup(radioGroup);
        monoRadio.setSelected(true);
        HBox hBoxRad = new HBox();
        hBoxRad.setSpacing(50);
        hBoxRad.getChildren().addAll(monoRadio, multiRadio);
        hBoxRad.setAlignment(Pos.CENTER);
        listColumn.clear();
        listColumn.add(new columnMulti());
        listColumn.add(new columnMulti());
        globVBox.getChildren().addAll(numLab, hBoxRad, inputVBox, sep, listVBox);
        HBox colHBox = new HBox();
        colHBox.setSpacing(25);
        colHBox.setPadding(new Insets(15, 30, 0, 70));
        monoRadio.setOnAction(e -> {
            if (monoRadio.isSelected()) {
                reloadList();
                globVBox.getChildren().clear();
                listColumn.clear();
                listColumn.add(new columnMulti());
                listColumn.add(new columnMulti());
                globVBox.getChildren().addAll(numLab, hBoxRad, inputVBox, sep, listVBox);
            }
        });

        multiRadio.setOnAction(e -> {
            if (multiRadio.isSelected()) {
                reloadListMulti();
                globVBox.getChildren().clear();
                colHBox.getChildren().clear();
                VBox labVbox = new VBox();
                labVbox.setSpacing(10);
                labVbox.setTranslateY(11);
                Label nameLab = new Label("Anticorp:");
                Label cibleLabel = new Label("Cible:");
                Label fourniLabel = new Label("Fournisseur:");
                Label refLabel = new Label("Référence:");
                Label espCibleLabel = new Label("Espéce Cible:");
                Label revLab = new Label("Révélation:");
                Label diluLab = new Label("Dilution:");
                diluLab.setPadding(new Insets(10, 0, 0, 0));
                Label tempPrimLab = new Label("Temps exposition:");
                tempPrimLab.setPadding(new Insets(10,0,0,0));
                Label tempSecondLab = new Label("Temps polymère:");
                tempSecondLab.setPadding(new Insets(10,0,0,0));
                Label tempThirdLab = new Label("Temps chromogène:");
                tempThirdLab.setPadding(new Insets(10,0,0,0));
                labVbox.getChildren().addAll(nameLab, cibleLabel, fourniLabel, refLabel, espCibleLabel, revLab, diluLab, tempPrimLab, tempSecondLab, tempThirdLab);
                Button addBut = new Button("Ajouter");
                colHBox.getChildren().add(labVbox);
                for (columnMulti cool: listColumn) {
                    colHBox.getChildren().add(cool.getVBox());
                }
                colHBox.getChildren().add(addBut);
                addBut.setOnAction(t -> {
                    listColumn.add(new columnMulti());
                    colHBox.getChildren().clear();
                    colHBox.getChildren().add(labVbox);
                    for (columnMulti cool: listColumn) {
                        colHBox.getChildren().add(cool.getVBox());
                    }
                    colHBox.getChildren().add(addBut);
                });
                HBox butHbox = new HBox();
                butHbox.setSpacing(15);
                Button genButton = new Button("Générer");
                Button recetteButton = new Button("Recette");
                butHbox.getChildren().addAll(genButton, recetteButton);
                butHbox.setTranslateX(900);

                recetteButton.setOnAction(t -> {
                    Dialog<ButtonType> dialog = new Dialog<>();
                    dialog.setTitle("Recette");
                    VBox recetteVBox = new VBox();
                    recetteVBox.setSpacing(20);
                    Label title = new Label("Pour 500mL de solution");
                    Label glycineLab = new Label("Glycine:\t940mg");
                    Label sdsLab = new Label("SDS 20% de la solution:\t25mL");
                    Label eauLab = new Label("Eau distillé stérile:\tQSP 500mL");
                    Label hciLab = new Label("HCI\tQSP pour ajuster pH à 2");
                    Label soluPrepareLab = new Label("Une fois la solution préparée:");
                    Label txtLab = new Label("1 - Faire chauffer la solution à 50°C.\n\n2 - Une fois les 50°C atteints, placer les lames à stripper dans la solution sous agitation durant 30 minutes à 50°C.\n\n3 - Sortir les lames de la solution de stripping et les rincer par 3 bains sucessifs de Wash Buffer 1X à RT pour faire remonter le pH.\n\n4 - Continuer avec le marquage suivant.");
                    recetteVBox.getChildren().addAll(title, glycineLab, sdsLab, eauLab, hciLab, soluPrepareLab, txtLab);
                    dialog.getDialogPane().setContent(recetteVBox);
                    dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                    dialog.showAndWait();
                });

                genButton.setOnAction(t -> {
                    String num = getNum();
                    if (edit) {
                        int idx = list.getSelectionModel().getSelectedIndex();
                        Multiplex selected = MarquageLoader.listMultiplexExp.get(idx);
                        System.out.println(selected);
                        System.out.println(selected.getName());
                        num = selected.getNum();
                        try {
                            Files.walk(Paths.get(Params.basePath + "/Anticorps_exp/" + selected.getName())).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                    Multiplex multi = new Multiplex(getMutiplexMarquageList(), num, getMutiplexMarquageList().get(0).getEspeceCible(), getMultiplexCustom());
                    multi.saveExp();
                    Excel_util.ficheTravailMultiplex(multi);
                    if (!edit) {
                        MarquageLoader.listMultiplexExp.add(multi);
                        BufferedReader reader;
                        try {
                            reader = new BufferedReader(new FileReader(Params.basePath + "/counter.txt"));
                            String l;
                            List<String> file = new ArrayList<>();
                            while((l=reader.readLine()) != null) {
                                file.add(l);
                            }
                            reader.close();
                            BufferedWriter writer = new BufferedWriter(new FileWriter(Params.basePath + "/counter.txt"));
                            for(String line: file) {
                                System.out.println(line);
                                String[] splitLine = line.split("#!#");
                                if (splitLine[0].equals("anticorp")) {
                                    writer.write("anticorp#!#" + String.valueOf(Integer.parseInt(splitLine[1]) + 1) + "\n");
                                } else {
                                    writer.write(line + "\n");
                                }
                            }
                            writer.close();
                        } catch (NumberFormatException | IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        anticNum = getNum();
                    }
                    if (edit) {
                        monoRadio.fire();
                        multiRadio.fire();
                    }
                    edit = false;
                    reloadListMulti();
                    numLab.setText("Protocole N°:\tD." + anticNum);
                });

                globVBox.getChildren().addAll(numLab, hBoxRad, colHBox, butHbox, sep, listVBox);
            }
        });

        updateBut.setOnMouseClicked(e -> {
            parseData.parseReactif();
        });

        autreCibleRad.setOnMouseClicked(e -> {
            if (autreCibleRad.isSelected()) {
                autreCibleField.setVisible(true);
            }
        });

        humanCibleRad.setOnMouseClicked(e -> {
            if (!autreCibleRad.isSelected()) {
                autreCibleField.setVisible(false);
            }
        });

        mouseCibleRad.setOnMouseClicked(e -> {
            if (!autreCibleRad.isSelected()) {
                autreCibleField.setVisible(false);
            }
        });

        autreHoRad.setOnMouseClicked(e -> {
            if (autreHoRad.isSelected()) {
                autreHoField.setVisible(true);
            }
        });

        mouseHoRad.setOnMouseClicked(e -> {
            if (!autreHoRad.isSelected()) {
                autreHoField.setVisible(false);
            }
        });

        rabbitHoRad.setOnMouseClicked(e -> {
            if (!autreHoRad.isSelected()) {
                autreHoField.setVisible(false);
            }
        });

        monoRad.setOnMouseClicked(e -> {
            if (monoRad.isSelected()) {
                monoField.setVisible(true);
            }
        });

        polyRad.setOnMouseClicked(e -> {
            if (!monoRad.isSelected()) {
                monoField.setVisible(false);
            }
        });

        fluoRad.setOnMouseClicked(e -> {
            if (fluoRad.isSelected()) {
                fluoCombo.setVisible(true);
            }
        });

        dabRad.setOnMouseClicked(e -> {
            if (!fluoRad.isSelected()) {
                fluoCombo.setVisible(false);
            }
        });

        aecRad.setOnMouseClicked(e -> {
            if (!fluoRad.isSelected()) {
                fluoCombo.setVisible(false);
            }
        });

        genBut.setOnMouseClicked(e -> {
            String espCible;
            String espHote;
            String chanel;
            String numClon;

            if (edit) {
                try {
                Files.walk(Paths.get(editPath)).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            editPath = null;
            }

            if (autreCibleRad.isSelected()) {
                espCible = autreCibleField.getText();
            }
            else {
                RadioButton selected = (RadioButton) groupEspCible.getSelectedToggle();
                espCible = selected.getText();
            }

            if (autreHoRad.isSelected()) {
                espHote = autreHoField.getText();
            }
            else {
                RadioButton selected = (RadioButton) groupEspHote.getSelectedToggle();
                espHote = selected.getText();
            }

            if (monoRad.isSelected()) {
                numClon = monoField.getText();
            }
            else {
                numClon = "0";
            }

            if (fluoRad.isSelected()) {
                chanel = fluoCombo.getValue().toString();
            }
            else {
                chanel = "0";
            }

            RadioButton selectedClon = (RadioButton) groupClon.getSelectedToggle();
            RadioButton selectAnti = (RadioButton) groupRet.getSelectedToggle();
            RadioButton selectRev = (RadioButton) groupTech.getSelectedToggle();

            Marquage newMrq = new Marquage(cibleField.getText(), anticNum, espCible, espHote, fourniField.getText(), refField.getText(), selectedClon.getText(), numClon, selectAnti.getText(), selectRev.getText(), chanel, tissuField.getText(), false);
            newMrq.saveExp();
            MarquageLoader.loadExp();
            reloadList();
            if (selectedClon.getText().equals("Monoclonale")) {
                Excel_util.genMAP(numLab.getText().split("D.")[1], cibleField.getText(), espCible, espHote, getNum(), tissuField.getText(), selectAnti.getText(), fourniField.getText(), refField.getText(), monoField.getText(), selectRev.getText());
            } else {
                Excel_util.genMAP(numLab.getText().split("D.")[1], cibleField.getText(), espCible, espHote, getNum(), tissuField.getText(), selectAnti.getText(), fourniField.getText(), refField.getText(), selectedClon.getText(), selectRev.getText());
            }
            if (!edit) {
                System.out.println("EDIT");
                BufferedReader reader;
                    try {
                        reader = new BufferedReader(new FileReader(Params.basePath + "/counter.txt"));
                    String l;
                    List<String> file = new ArrayList<>();
                    while((l=reader.readLine()) != null) {
                        file.add(l);
                    }
                    reader.close();
                    BufferedWriter writer = new BufferedWriter(new FileWriter(Params.basePath + "/counter.txt"));
                    for(String line: file) {
                        System.out.println(line);
                        String[] splitLine = line.split("#!#");
                        if (splitLine[0].equals("anticorp")) {
                            writer.write("anticorp#!#" + String.valueOf(Integer.parseInt(splitLine[1]) + 1) + "\n");
                        } else {
                            writer.write(line + "\n");
                        }
                    }
                    writer.close();
                    } catch (NumberFormatException | IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
            }
            edit = false;
            anticNum = getNum();
            numLab.setText("Protocole N°:\tD." + anticNum);
            cibleField.setText("");
            humanCibleRad.setSelected(false);
            mouseCibleRad.setSelected(false);
            autreCibleRad.setSelected(false);
            autreCibleField.setText("");
            mouseHoRad.setSelected(false);
            rabbitHoRad.setSelected(false);
            autreHoRad.setSelected(false);
            autreHoField.setText("");
            fourniField.setText("");
            refField.setText("");
            polyRad.setSelected(false);
            monoRad.setSelected(false);
            monoField.setText("");
            edtaRad.setSelected(false);
            citraRad.setSelected(false);
            dabRad.setSelected(false);
            aecRad.setSelected(false);
            fluoRad.setSelected(false);
            tissuField.setText("");
        });

        rejBut.setOnMouseClicked(e -> {
            int idx = list.getSelectionModel().getSelectedIndex();
            if (monoRadio.isSelected()) {
                if (idx != -1) {
                    System.out.println(Params.archivePath + "/Developement/Rejected/" + listMrq.get(idx).getName() + "_" + listMrq.get(idx).getEspeceCible() + "_" + listMrq.get(idx).fourniseur + "_" + listMrq.get(idx).reference);
                    new File(Params.archivePath + "/Developement/Rejected/" + listMrq.get(idx).getName() + "_" + listMrq.get(idx).getEspeceCible() + "_" + listMrq.get(idx).fourniseur + "_" + listMrq.get(idx).reference).mkdirs();
                    Administration.copyRecusrively(listMrq.get(idx).getPath(), Params.archivePath + "/Developement/Rejected/" + listMrq.get(idx).getName() + "_" + listMrq.get(idx).getEspeceCible() + "_" + listMrq.get(idx).fourniseur + "_" + listMrq.get(idx).reference);
                    Administration.deleteDirectory(new File(listMrq.get(idx).getPath()));
                    new File(listMrq.get(idx).getPath()).delete();
                    listMrq.remove(idx);
                    list.getItems().remove(list.getSelectionModel().getSelectedItem());
                }
            } else {
                Multiplex selected = MarquageLoader.listMultiplexExp.get(idx);
                new File(Params.archivePath + "/Developement/Rejected/").mkdirs();
                Administration.copyRecusrively(Params.basePath + "/Anticorps_exp/" + selected.getName(), Params.archivePath + "/Developement/Rejected/" + selected.getName());
            }
        });

        editBut.setOnMouseClicked(e -> {
            if (monoRadio.isSelected()) {
                int idx = list.getSelectionModel().getSelectedIndex();
                if (idx != -1) {
                    Marquage selected = listMrq.get(idx);
                    edit = true;
                    editPath = selected.getPath();

                    cibleField.setText(selected.getName());

                    anticNum = selected.getNum();
                    numLab.setText("Anticorps N°:\tD." + anticNum);
                    //Espece cible
                    if(selected.getEspeceCible().equals("Human")) {
                        humanCibleRad.setSelected(true);
                    }
                    else if (selected.getEspeceCible().equals("Mouse")) {
                        mouseCibleRad.setSelected(true);
                    }
                    else {
                        autreCibleRad.setSelected(true);
                        autreCibleField.setVisible(true);
                        autreCibleField.setText(selected.getEspeceCible());
                    }

                    // Espece Hote
                    if(selected.especeHote.equals("Mouse")) {
                        mouseHoRad.setSelected(true);
                    }
                    else if (selected.especeHote.equals("Rabbit")) {
                        rabbitHoRad.setSelected(true);
                    }
                    else {
                        autreHoRad.setSelected(true);
                        autreHoField.setVisible(true);
                        autreHoField.setText(selected.especeHote);
                    }

                    fourniField.setText(selected.fourniseur);
                    refField.setText(selected.reference);

                    //Clonalité
                    if(selected.clonalite.equals("Polyclonale")) {
                        polyRad.setSelected(true);
                    }
                    else {
                        monoRad.setSelected(true);
                        monoField.setVisible(true);
                        monoField.setText(String.valueOf(selected.nMonoclonale));
                    }

                    //Anticorp
                    if (selected.antigene.equals("EDTA")) {
                        edtaRad.setSelected(true);
                    }
                    else {
                        citraRad.setSelected(true);
                    }

                    //Revelation
                    if(selected.revelation.equals("DAB")) {
                        dabRad.setSelected(true);
                    }
                    else if (selected.revelation.equals("AEC")) {
                        aecRad.setSelected(true);
                    } else if (selected.revelation.equals("Vinagreen")) {
                        vinaRad.setSelected(true);
                    } else if (selected.revelation.equals("ImmPACT Vector Reds")) {
                        impactRad.setSelected(true);
                    } else if (selected.revelation.equals("HRP Magenta")) {
                        magentaHrp.setSelected(true);
                    }
                    else {
                        fluoRad.setSelected(true);
                        fluoCombo.setVisible(true);
                        fluoCombo.setValue(String.valueOf(selected.chanel));
                    }
                    tissuField.setText(selected.tissu);
                }
            } else {
                edit = true;
                int idx = list.getSelectionModel().getSelectedIndex();
                Multiplex selected = MarquageLoader.listMultiplexExp.get(idx);
                listColumn.clear();
                while(colHBox.getChildren().size() > 2) {
                    colHBox.getChildren().remove(colHBox.getChildren().size()-2);
                }
                for (Marquage m: selected.marquageList) {
                    listColumn.add(new columnMulti(m));
                }
                for (columnMulti c: listColumn) {
                    colHBox.getChildren().add(colHBox.getChildren().size()-1 ,c.getVBox());
                }
            }
        });

        valBut.setOnMouseClicked(e -> {
            if (!multiRadio.isSelected()) {
                int idx = list.getSelectionModel().getSelectedIndex();
                Marquage selected = listMrq.get(idx);
                List<String> res = dialogAnticorpValid.launcher(0,selected.getNum(), selected.getName(), selected.getEspeceCible(), selected.getFourniseur(), selected.getRevelation(), selected.getReference(), selected);
                Label lblProgress = new Label();
                ProgressBar progressBar = new ProgressBar(0.0);
                progressBar.setMinWidth(300);
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.getDialogPane().setMinHeight(100);
                dialog.setTitle("Chargement...");
                VBox vBox = new VBox();
                vBox.setPadding(new Insets(35));
                vBox.setSpacing(10);
                dialog.setWidth(300);
                
                vBox.getChildren().addAll(lblProgress, progressBar);
                dialog.getDialogPane().setContent(vBox);
                dialog.show();
                Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                        if (monoRadio.isSelected()) {
                            if (idx != -1) {
                                //Marquage selected = listMrq.get(idx);
                                //List<String> res = dialogAnticorpValid.launcher(0,selected.num, selected.getName(), selected.getEspeceCible(), selected.getFourniseur(), selected.getRevelation(), selected.getReference(), selected);
                                if (res.size() == 1) {
                                    e.consume();
                                } else {
                                    updateProgress(10, 100);
                                    selected.setDiluant(res.get(0));
                                    selected.setDilution(Integer.parseInt(res.get(1).split("/")[1]));
                                    selected.setIncubation(res.get(2));
                                    selected.setAntigene(res.get(3));
                                    selected.setTp(res.get(4));
                                    selected.setTemp(Integer.parseInt(res.get(5)));
                                    Path sourceDir = Paths.get(selected.getPath());
                                    Path destinationDir = Paths.get(selected.getPath().replace("exp", "valid"));
                                
                                    // Traverse the file tree and copy each file/directory.
                                    updateMessage("Copie des anticorps...");
                                    updateProgress(25, 100);
                                    try {
                                        Files.walk(sourceDir)
                                                .forEach(sourcePath -> {
                                                    try {
                                                        Path targetPath = destinationDir.resolve(sourceDir.relativize(sourcePath));
                                                        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                                                    } catch (IOException ex) {}
                                                });
                                        Files.walk(Paths.get(selected.getPath())).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                    listMrq.remove(idx);
                                    Platform.runLater(() -> list.getItems().remove(list.getSelectionModel().getSelectedItem()));
                                    updateMessage("Finalisation...");
                                    updateProgress(80, 100);
                                    try {
                                        BufferedReader reader = new BufferedReader(new FileReader(destinationDir + "/Info.txt"));
                                        String line;
                                        List<String> finalList = new ArrayList<>();
                                        while((line=reader.readLine()) != null) {
                                            String[] splitLine = line.split("#!#");
                                            if (splitLine[0].equals("path")) {
                                                finalList.add(line.replace("exp", "valid"));
                                            }
                                            else {
                                                finalList.add(line);
                                            }
                                        }
                                        reader.close();
                                        BufferedWriter writer = new BufferedWriter(new FileWriter(destinationDir + "/Info.txt"));
                                        for(String l: finalList) {
                                            writer.write(l + "\n");
                                        }
                                        writer.close();
                                    } catch (IOException i) {
                                        i.printStackTrace();
                                    }
                                    MarquageLoader.load();
                                }
                                Excel_util.createFichePTBC(selected);
                            }
                        } else {
                                int idx = list.getSelectionModel().getSelectedIndex();
                                Multiplex selected = MarquageLoader.listMultiplexExp.get(idx);
                                Administration.copyRecusrively(Params.basePath + "/Anticorps_exp/" + selected.getName(), Params.basePath + "/Anticorps_valid/" + selected.getName());
                                MarquageLoader.loadExp();
                                reloadListMulti();
                        }
                        updateMessage("Finie.");
                        updateProgress(100, 100);
                        return null;
                    }
                };
                progressBar.progressProperty().bind(task.progressProperty());
                lblProgress.textProperty().bind(task.messageProperty());

                task.setOnSucceeded(wse -> {
                    dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                });
                new Thread(task).start();
            } else {
                int idx = list.getSelectionModel().getSelectedIndex();
                Multiplex selected = MarquageLoader.listMultiplexExp.get(idx);
                Administration.copyRecusrively(Params.basePath + "/Anticorps_exp/" + selected.getName(), Params.basePath + "/Anticorps_valid/" + selected.getName());
                MarquageLoader.loadExp();
                reloadListMulti();
            }
        });

        return globVBox;   
    }


    private static String getNum() {
        String num = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(Params.basePath + "/counter.txt"));
            String line;
            while((line=reader.readLine()) != null) {
                String[] splitLine = line.split("#!#");
                if (splitLine[0].equals("anticorp")) {
                    num = splitLine[1];
                }
            }
            reader.close();
        } catch(Exception e) {}
        return num;
    }

    private static void reloadList() {
        listMrq = new ArrayList<>();
        listViewMono = FXCollections.observableArrayList();
        list.setItems(listViewMono);
        for(Marquage m: MarquageLoader.listExp) {
            listViewMono.add(makeList(m));
            listMrq.add(m);
        }
        list.setItems(listViewMono);
    }

    private static void reloadListMulti() {
        listViewMulti = FXCollections.observableArrayList();
        list.setItems(listViewMulti);
        for(Multiplex m: MarquageLoader.listMultiplexExp) {
            listViewMulti.add(makeList(m));
        }
        list.setItems(listViewMulti);
    }

    private static VBox makeList(Multiplex multi) {
        HBox globHBox = new HBox();
        globHBox.setSpacing(10);
        Separator sep = new Separator();
        Label numLabel = new Label("Multiplex n°: " + multi.getNum());
        Label espCiblLab = new Label("Espécé Cible: " + multi.getEspeceCible());
        HBox hBox = new HBox();
        hBox.setSpacing(100);
        hBox.getChildren().addAll(numLabel, espCiblLab);
        String anti = "";
        for (Marquage m: multi.marquageList) {
            anti = anti + m.getFullName() + "\t|\t";
        }
        Label anticorpLab = new Label("Anticorps: \t" + anti);
        VBox globVBox1 = new VBox();
        HBox globHBox2 = new HBox();
        globVBox1.setSpacing(10);
        globVBox1.setAlignment(Pos.CENTER_LEFT);
        globVBox1.getChildren().addAll(hBox, anticorpLab);
        globHBox2.setAlignment(Pos.CENTER_RIGHT);
        ImageView imageFiche = new ImageView(new Image("ISO9001/File/fiche.png"));
        Button fichButton = new Button();
        fichButton.setGraphic(imageFiche);
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        globHBox2.setSpacing(20);
        globHBox2.getChildren().addAll(fichButton);
        globHBox.getChildren().addAll(globVBox1, region, globHBox2);

        fichButton.setOnAction(e -> {
            try {
                Runtime.getRuntime().exec("CMD /C START " + Params.basePath + "/Anticorps_exp/" + multi.getName() + "/Fiche_Travail/Fiche.xlsx");
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });

        return new VBox(globHBox);
    }

    private static VBox makeList(Marquage mrq) {
        VBox globVBox = new VBox();
        globVBox.setPadding(new Insets(0,0,0,0));
        Separator sep = new Separator();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(10);
        HBox cibleHBox = new HBox();
        cibleHBox.setSpacing(10);
        Label nameLab = new Label("Cible:");
        Label nameValLab = new Label(mrq.getName());
        cibleHBox.getChildren().addAll(nameLab, nameValLab);
        gridPane.add(cibleHBox, 1,1);
        HBox especeCibleHBox = new HBox();
        especeCibleHBox.setSpacing(10);
        Label especeCibleLab = new Label("Espèce cible:");
        Label especeCibleValLab = new Label(mrq.getEspeceCible());
        especeCibleHBox.getChildren().addAll(especeCibleLab, especeCibleValLab);
        gridPane.add(especeCibleHBox, 2, 1);
        HBox especeHoteHBox = new HBox();
        especeHoteHBox.setSpacing(10);
        Label especeHoteLab = new Label("Espèce hote:");
        Label esepeceHoteValLab = new Label(mrq.especeHote);
        especeHoteHBox.getChildren().addAll(especeHoteLab, esepeceHoteValLab);
        gridPane.add(especeHoteHBox, 3, 1);
        HBox fourniHBox = new HBox();
        fourniHBox.setSpacing(10);
        Label fourniLab = new Label("Fournisseur:");
        Label fourniValLab = new Label(mrq.fourniseur);
        fourniHBox.getChildren().addAll(fourniLab, fourniValLab);
        gridPane.add(fourniHBox,4,1);
        HBox refHBox = new HBox();
        refHBox.setSpacing(10);
        Label referLab = new Label("Référence:");
        Label referValLab = new Label(mrq.reference);
        refHBox.getChildren().addAll(referLab, referValLab);
        gridPane.add(refHBox,1,2);
        HBox clonHBox = new HBox();
        clonHBox.setSpacing(10);
        Label clonaLab = new Label("Clonalité:");
        Label clonaValLab = new Label(mrq.clonalite);
        clonHBox.getChildren().addAll(clonaLab, clonaValLab);
        gridPane.add(clonHBox,2,2);
        HBox antiHBox = new HBox();
        antiHBox.setSpacing(10);
        Label antiLab = new Label("Antigène:");
        Label antiValLab = new Label(mrq.antigene);
        antiHBox.getChildren().addAll(antiLab, antiValLab);
        gridPane.add(antiHBox, 3,2);
        HBox revHBox = new HBox();
        revHBox.setSpacing(10);
        Label revelLab = new Label("Révélation:");
        Label revelValLab = new Label(mrq.revelation);
        revHBox.getChildren().addAll(revelLab, revelValLab);
        gridPane.add(revHBox,4,2);
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        Button ficheBtn = new Button();
        ImageView imageFiche = new ImageView(new Image("ISO9001/File/fiche.png"));
        ficheBtn.setGraphic(imageFiche);
        ImageView imageQrCode = new ImageView(new Image("ISO9001/File/qrCode.png"));
        ficheBtn.setMinHeight(50);
        Button folderButton = new Button();
        ImageView imageFolder = new ImageView(new Image("ISO9001/File/folder2.png"));
        folderButton.setGraphic(imageFolder);
        folderButton.setMaxHeight(50);
        HBox gridHBox = new HBox();
        Button qrCode = new Button();
        qrCode.setGraphic(imageQrCode);
        qrCode.setMinHeight(50);
        gridHBox.setAlignment(Pos.CENTER_LEFT);
        gridHBox.getChildren().add(gridPane);
        HBox btnHBox = new HBox();
        btnHBox.setSpacing(30);
        btnHBox.getChildren().addAll(ficheBtn, qrCode, folderButton);
        hbox.getChildren().addAll(gridHBox, region, btnHBox);
        globVBox.getChildren().addAll(hbox, sep);

        folderButton.setOnAction(e -> {
            System.out.println(mrq.getPath());
            try {
                Runtime.getRuntime().exec("explorer.exe /select," + mrq.getPath().replace("/", "\\") + "\\Scan");
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });

        ficheBtn.setOnAction(e -> {
            try {
                Runtime.getRuntime().exec("CMD /C START " + Params.basePath + "/Anticorps_exp/" + mrq.getNum() + "_" + mrq.getName() + "_" + mrq.getEspeceCible().charAt(0) + "_" + mrq.fourniseur + "_" + mrq.reference + "/Fiche_Travail/Fiche.xlsx");
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });

        qrCode.setOnAction(e -> {
            byte[] qrcode = QRCode.createEtiquetteLogo(Params.basePath + "/Anticorps_exp/" + mrq.getNum() + "_" + mrq.getName() + "_" + mrq.getEspeceCible().charAt(0) + "_" + mrq.fourniseur + "_" + mrq.reference + "/Fiche_fournisseur/DataSheet.pdf");
            InputStream is = new ByteArrayInputStream(qrcode);
            try {
                BufferedImage newBi = ImageIO.read(is);
                Graphics2D g = newBi.createGraphics();
                g.setFont(new Font("TimesRoman", Font.BOLD, 20));
                g.setColor(Color.BLACK);
                g.drawString(mrq.getName() + " " + mrq.getEspeceCible() + " " + mrq.fourniseur + " " + mrq.reference, 160, 40);
                g.drawString("Anticorps#" + mrq.getNum(), 175, 20);
                g.dispose();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(newBi, "jpg", baos);
                byte[] bytes = baos.toByteArray();
                Printer.printLabel(bytes);
            } catch (IOException e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
        return globVBox;
    }

    public static byte[] getByteData(BufferedImage userSpaceImage) {
        WritableRaster raster = userSpaceImage.getRaster();
        DataBufferByte buffer = (DataBufferByte) raster.getDataBuffer();
        return buffer.getData();
    }

    private static List<Marquage> getMutiplexMarquageList() {
        List<Marquage> list = new ArrayList<>();
        for(columnMulti c: listColumn) {
            list.add(c.getMarquage());
        }
        return list;
    }

    private static List<List<Integer>> getMultiplexCustom() {
        List<List<Integer>> list = new ArrayList<>();
        for (columnMulti c: listColumn) {
            list.add(c.getCustom());
        }
        return list;
    }
}

class columnMulti{

    private VBox globVBox;
    private ComboBox<Marquage> comboBox;
    private TextField dilutionField;
    private TextField expoPrimField;
    private TextField expoSecondField;
    private TextField expoThirdField;

    columnMulti(Marquage m){
        this.globVBox = new VBox();
        this.globVBox.setSpacing(10);
        comboBox = new ComboBox<>();
        comboBox.setMaxWidth(100);
        comboBox.getItems().addAll(MarquageLoader.listValid);
        comboBox.getSelectionModel().select(m);
        comboBox.setConverter(new StringConverter<Marquage>(){
            @Override
            public String toString(Marquage object) {
                return object.getFullName();
            }

            @Override
            public Marquage fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

        Marquage selected = (Marquage) comboBox.getSelectionModel().getSelectedItem();
        Label cibLab = new Label(selected.getName());
        Label fourniLab = new Label(selected.getFourniseur());
        Label espCibleLab = new Label(selected.getEspeceCible());
        Label refLab = new Label(selected.getReference());
        Label revLab = new Label(selected.getRevelation());
        dilutionField = new TextField(String.valueOf(selected.dilution));
        dilutionField.setMaxWidth(45);
        expoPrimField = new TextField(String.valueOf(selected.temp));
        expoPrimField.setMaxWidth(45);
        expoSecondField = new TextField(String.valueOf(selected.minPoly));
        expoSecondField.setMaxWidth(45);
        expoThirdField = new TextField(String.valueOf(selected.minChromo));
        expoThirdField.setMaxWidth(45);
        this.globVBox.setAlignment(Pos.BASELINE_CENTER);
        this.globVBox.getChildren().addAll(comboBox, cibLab, fourniLab, refLab, espCibleLab, revLab, dilutionField, expoPrimField, expoSecondField, expoThirdField);
        // dilution
        //temp exposition primaire
        //temps expo polymere


        comboBox.setOnAction(e -> {
            Marquage s = (Marquage) comboBox.getSelectionModel().getSelectedItem();
            cibLab.setText(s.getName());
            fourniLab.setText(s.getFourniseur());
            espCibleLab.setText(s.getEspeceCible());
            refLab.setText(s.getReference());
            revLab.setText(s.getRevelation());
            dilutionField.setText(String.valueOf(s.dilution));
            expoPrimField.setText(String.valueOf(s.temp));
            expoSecondField.setText(String.valueOf(s.minPoly));
            expoThirdField.setText(String.valueOf(s.minChromo));
        });
    }

    columnMulti() {
        this.globVBox = new VBox();
        this.globVBox.setSpacing(10);
        comboBox = new ComboBox<>();
        comboBox.setMaxWidth(100);
        comboBox.getItems().addAll(MarquageLoader.listValid);
        comboBox.getSelectionModel().select(0);
        comboBox.setConverter(new StringConverter<Marquage>(){
            @Override
            public String toString(Marquage object) {
                return object.getFullName();
            }

            @Override
            public Marquage fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

        Marquage selected = (Marquage) comboBox.getSelectionModel().getSelectedItem();
        Label cibLab = new Label(selected.getName());
        Label fourniLab = new Label(selected.getFourniseur());
        Label espCibleLab = new Label(selected.getEspeceCible());
        Label refLab = new Label(selected.getReference());
        Label revLab = new Label(selected.getRevelation());
        dilutionField = new TextField(String.valueOf(selected.dilution));
        dilutionField.setMaxWidth(45);
        expoPrimField = new TextField(String.valueOf(selected.temp));
        expoPrimField.setMaxWidth(45);
        expoSecondField = new TextField(String.valueOf(selected.minPoly));
        expoSecondField.setMaxWidth(45);
        expoThirdField = new TextField(String.valueOf(selected.minChromo));
        expoThirdField.setMaxWidth(45);
        this.globVBox.setAlignment(Pos.BASELINE_CENTER);
        this.globVBox.getChildren().addAll(comboBox, cibLab, fourniLab, refLab, espCibleLab, revLab, dilutionField, expoPrimField, expoSecondField, expoThirdField);
        // dilution
        //temp exposition primaire
        //temps expo polymere


        comboBox.setOnAction(e -> {
            Marquage s = (Marquage) comboBox.getSelectionModel().getSelectedItem();
            cibLab.setText(s.getName());
            fourniLab.setText(s.getFourniseur());
            espCibleLab.setText(s.getEspeceCible());
            refLab.setText(s.getReference());
            revLab.setText(s.getRevelation());
            dilutionField.setText(String.valueOf(s.dilution));
            expoPrimField.setText(String.valueOf(s.temp));
            expoSecondField.setText(String.valueOf(s.minPoly));
            expoThirdField.setText(String.valueOf(s.minChromo));
        });
    }

    public VBox getVBox() {
        return this.globVBox;
    }

    public List<Integer> getCustom() {
        List<Integer> list = new ArrayList<>();
        list.add(Integer.parseInt(dilutionField.getText()));
        list.add(Integer.parseInt(expoPrimField.getText()));
        list.add(Integer.parseInt(expoSecondField.getText()));
        list.add(Integer.parseInt(expoThirdField.getText()));
        return list;
    }

    public Marquage getMarquage() {
        Marquage selected = (Marquage) comboBox.getSelectionModel().getSelectedItem();
        selected.setDilution(Integer.parseInt(dilutionField.getText()));
        selected.setTemp(Integer.parseInt(expoPrimField.getText()));
        selected.setMinPoly(Integer.parseInt(expoSecondField.getText()));
        selected.setMinChromo(Integer.parseInt(expoThirdField.getText()));
        return selected;
    }
}
