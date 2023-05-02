package ISO9001;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.management.Descriptor;

import org.apache.commons.codec.binary.BaseNCodecOutputStream;
import org.apache.commons.math3.genetics.SelectionPolicy;

import java.io.FileReader;
import java.io.FileWriter;
import ISO9001.Dialog.dialogAddAssocie;
import ISO9001.Dialog.dialogDemandeur;
import ISO9001.Dialog.dialogMarquage;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Separator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Enregistrement extends Main{

    static List<Associe> associeList = new ArrayList<>();
    static List<Technique> marquageList = new ArrayList<>();
    static HashMap<Integer, String> color = new HashMap<>();
    static TextField titreField;
    static CheckBox checkCoupe;
    static TextField nbCoupeField;
    static TextField epaisseurField;
    static TextField nbEchan;
    static RadioButton humanRadio;
    static RadioButton sourisRadio;
    static RadioButton  autreRadio;
    static Label nameLab;
    static Label prenomLab;
    static Label mailLab;
    static TableView<Technique> marquageTable;

    public static GridPane build() {
        color.put(0, "white");
        color.put(1, "yellow");
        color.put(2, "green");
        color.put(3, "red");
        color.put(4, "blue");
        color.put(5, "pink");
        color.put(6, "orange");
        GridPane enregGridPane = new GridPane();
        enregGridPane.setHgap(5);
        enregGridPane.setPadding(new Insets(0, 50, 5, 50));
        
        Label quiLabel = new Label("Qui?");
        quiLabel.translateYProperty().set(150);
        Label quoiLabel = new Label("Quoi?");
        quoiLabel.translateYProperty().set(140);
        Separator enregistrementSep = new Separator();
        
        enregistrementSep.setOrientation(Orientation.VERTICAL);
        enregistrementSep.setPrefHeight(550);
        
        VBox leftEnregVBox = new VBox();
        leftEnregVBox.setMaxWidth(50);
        leftEnregVBox.setSpacing(330);
        leftEnregVBox.getChildren().addAll(quiLabel, quoiLabel);
        
        // Pour
        VBox rightEnregVBox = new VBox();
        rightEnregVBox.setSpacing(20);
        HBox pourEnrgHBox = new HBox();
        pourEnrgHBox.setSpacing(75);
        pourEnrgHBox.setPadding(new Insets(20, 0, 0, 30));
        Label pourLab = new Label("Pour:");
        nameLab = new Label(User.nom);
        prenomLab = new Label(User.prenom);
        mailLab = new Label(User.mail);
        Button modifyPourButton = new Button("Modifier");
        pourEnrgHBox.getChildren().addAll(pourLab, nameLab, prenomLab, mailLab, modifyPourButton);

        Separator quiSep = new Separator();
        quiSep.setPadding(new Insets(20, 0, 0, 75));

        // Associé
        HBox associeHBox = new HBox();
        associeHBox.setSpacing(50);
        associeHBox.setPadding(new Insets(0, 0, 0, 30));
        Label associeLab = new Label("Associé:");
        TableView<Associe> tableAssocie = new TableView<>();
        tableAssocie.setMaxHeight(150);
        tableAssocie.setPrefWidth(450);
        TableColumn<Associe, String> nameCol = new TableColumn<>("Nom");
        TableColumn<Associe, String> prenCol = new TableColumn<>("Prenom");
        TableColumn<Associe, String> roleCol = new TableColumn<>("Mail");

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        prenCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        roleCol.setCellValueFactory(new PropertyValueFactory<>("mail"));
        nameCol.setPrefWidth(150);
        prenCol.setPrefWidth(150);
        roleCol.setPrefWidth(150);
        tableAssocie.getColumns().addAll(nameCol,prenCol, roleCol);
        VBox buttonAssocieVBox = new VBox();
        buttonAssocieVBox.setSpacing(20);
        buttonAssocieVBox.setPadding(new Insets(15, 0, 0, 0));
        Button addAssocie = new Button("Ajouter");
        Button delAssocie = new Button("Retirer");
        addAssocie.setPrefHeight(50);
        addAssocie.setPrefWidth(75);
        delAssocie.setPrefHeight(50);
        delAssocie.setPrefWidth(75);
        buttonAssocieVBox.getChildren().addAll(addAssocie, delAssocie);
        associeHBox.getChildren().addAll(associeLab, tableAssocie, buttonAssocieVBox);
        Separator betweenSep = new Separator();
        betweenSep.setPadding(new Insets(20, 0, 0, 20));

        //Quoi
        HBox globQuoiHBox = new HBox();
        globQuoiHBox.setSpacing(20);
        VBox quoiVBox = new VBox();
        quoiVBox.setSpacing(35);
        quoiVBox.setPadding(new Insets(20, 0, 0, 20));
        HBox coupeHBox = new HBox();
        coupeHBox.setSpacing(10);
        Label coupeLab = new Label("Coupe:");
        checkCoupe = new CheckBox();
        Label numLab = new Label("nombre de coupes:");
        nbCoupeField = new TextField();
        nbCoupeField.setMaxWidth(35);
        coupeHBox.getChildren().addAll(coupeLab, checkCoupe);
        Label epaisseurLab = new Label("epaisseur:");
        epaisseurField = new TextField("4");
        epaisseurField.setMaxWidth(35);
        Label umLab = new Label("µm");
        coupeHBox.getChildren().addAll(numLab, nbCoupeField, epaisseurLab, epaisseurField, umLab);
        numLab.setVisible(false);
        nbCoupeField.setVisible(false);
        epaisseurLab.setVisible(false);
        epaisseurField.setVisible(false);
        umLab.setVisible(false);
        HBox copeauxHBox = new HBox();
        copeauxHBox.setSpacing(10);
        Label copeauLab = new Label("Copeaux:");
        CheckBox checkCopeaux = new CheckBox();
        Label nbCopeauxLab = new Label("nombre de copeaux:");
        TextField nbCopeauField = new TextField("5");
        nbCopeauField.setMaxWidth(35);
        Label epaisseurCopeauLab = new Label("epaisseur:");
        TextField epaisseurCopeauField = new TextField("8");
        epaisseurCopeauField.setMaxWidth(35);
        Label umLab2 = new Label("µm");
        copeauxHBox.getChildren().addAll(copeauLab, checkCopeaux, nbCopeauxLab, nbCopeauField, epaisseurCopeauLab, epaisseurCopeauField, umLab2);
        nbCopeauxLab.setVisible(false);
        nbCopeauField.setVisible(false);
        epaisseurCopeauLab.setVisible(false);
        epaisseurCopeauField.setVisible(false);
        umLab2.setVisible(false);
        
        //Marquage

        VBox marquageVBox = new VBox();
        marquageVBox.setSpacing(10);
        HBox marquageHBox = new HBox();
        marquageHBox.setSpacing(20);
        Label marquageLab = new Label("Marquage/Numerisation:");
        Button addMarquage = new Button("Ajouter");
        Button delMarquage = new Button("Retirer");
        Button linkMarquage = new Button("Lier");
        Button unlinkMarquage = new Button("Delier");
        marquageTable = new TableView<>();
        marquageTable.setPrefWidth(300);
        marquageTable.setPrefHeight(200);
        TableColumn<Technique, String> marquageCol = new TableColumn<>("Marquage");
        TableColumn<Technique, String> techniqueCol = new TableColumn<>("Révélation");
        TableColumn<Technique, String> grooCol = new TableColumn<>("Grossisement");
        marquageTable.setRowFactory(row -> new TableRow<Technique>() {
            @Override
            public void updateItem(Technique item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setStyle("");
                } else if (item.getLink() != 0) {
                    this.setStyle("-fx-background-color: " + color.get(item.getLink()));
                }
            }
        });

        marquageCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        techniqueCol.setCellValueFactory(new PropertyValueFactory<>("technique"));
        grooCol.setCellValueFactory(new PropertyValueFactory<>("grossisement"));
        marquageTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        marquageCol.setMinWidth(130);
        techniqueCol.setMinWidth(130);
        grooCol.setMinWidth(130);
        marquageTable.getColumns().addAll(marquageCol, techniqueCol, grooCol);
        marquageHBox.getChildren().addAll(marquageLab, addMarquage, delMarquage, linkMarquage, unlinkMarquage);  
        marquageVBox.getChildren().addAll(marquageHBox, marquageTable);

        HBox nbEchanHBox = new HBox();
        nbEchanHBox.setSpacing(20);
        Label nombreEchanLab = new Label("Nombre échantillon:");
        nbEchan = new TextField("0");
        nbEchan.setMaxWidth(50);
        nbEchanHBox.getChildren().addAll(nombreEchanLab, nbEchan);

        HBox especeHBox = new HBox();
        especeHBox.setSpacing(60);
        Label especeLab = new Label("Espèce:");
        ToggleGroup groupEspece = new ToggleGroup();
        humanRadio = new RadioButton("Human");
        sourisRadio = new RadioButton("Mouse");
        autreRadio = new RadioButton("Other");
        humanRadio.setToggleGroup(groupEspece);
        sourisRadio.setToggleGroup(groupEspece);
        autreRadio.setToggleGroup(groupEspece);
        especeHBox.getChildren().addAll(especeLab, humanRadio, sourisRadio, autreRadio);

        quoiVBox.getChildren().addAll(coupeHBox, copeauxHBox, marquageVBox, nbEchanHBox, especeHBox);

        Separator sepQuoi = new Separator();
        sepQuoi.setOrientation(Orientation.VERTICAL);
        sepQuoi.setPrefHeight(100);

        VBox rightQuoiVBox = new VBox();
        rightQuoiVBox.setPadding(new Insets(20, 0, 0, 0));
        rightQuoiVBox.setSpacing(10);
        VBox titreVBox = new VBox();
        titreVBox.setSpacing(10);
        Label titreLab = new Label("Titre / Numéro de manipulation:");
        titreField = new TextField();
        titreField.setMinWidth(350);
        titreVBox.getChildren().addAll(titreLab, titreField);
        ToggleGroup financeGroup = new ToggleGroup();
        RadioButton ouiRad = new RadioButton("Oui");
        RadioButton nonRad = new RadioButton("Non");
        ouiRad.setToggleGroup(financeGroup);
        nonRad.setToggleGroup(financeGroup);
        Label labFinance = new Label("Projet financé:");
        HBox financeHBox = new HBox();
        financeHBox.setSpacing(10);
        financeHBox.getChildren().addAll(labFinance, ouiRad, nonRad);
        HBox numFinanceHBox = new HBox();
        numFinanceHBox.setSpacing(10);
        Label numFinanceLab = new Label("Numéro de financement:");
        TextField financeNumField = new TextField();
        numFinanceHBox.getChildren().addAll(numFinanceLab, financeNumField);
        numFinanceHBox.setDisable(true);
        ouiRad.setOnAction(e -> {
            if (ouiRad.isSelected()) {
                numFinanceHBox.setDisable(false);
            }
        });
        nonRad.setOnAction(e -> {
            numFinanceHBox.setDisable(true);
        });
        VBox descVBox = new VBox();
        descVBox.setSpacing(10);
        Label descLab = new Label("Description:");
        TextArea descField = new TextArea();
        descField.setMinHeight(200);
        descField.setMaxWidth(350);
        //descField.setAlignment(Pos.TOP_LEFT);
        descVBox.getChildren().addAll(descLab, descField);

        StackPane stackPane = new StackPane();
        Rectangle rect = new Rectangle(200,100);
        rect.setFill(Color.TRANSPARENT);
        rect.setStroke(Color.GRAY);
        VBox stackVBox = new VBox();
        Label projectNumLab = new Label("Numero de Projet:");
        HBox rectHBox = new HBox();
        Label pLab = new Label("    P.");
        TextField numProjField = new TextField(String.valueOf(Integer.parseInt(getProjectNumber()) + 1));
        rectHBox.getChildren().addAll(pLab, numProjField);
        rectHBox.setPadding(new Insets(0, 30, 0, 0));
        rectHBox.setSpacing(10);
        stackVBox.getChildren().addAll(projectNumLab, rectHBox);
        stackVBox.setPadding(new Insets(10, 0, 0, 85));
        stackVBox.setSpacing(20);
        numProjField.setEditable(false);
        stackPane.getChildren().addAll(rect, stackVBox);
        HBox projRecordHBox = new HBox();
        projRecordHBox.setSpacing(20);
        
        // Date URGENCE

        HBox dateUrgenceHBox = new HBox();
        HBox dateHBox = new HBox();
        dateHBox.setSpacing(20);
        HBox urgenceHBox = new HBox();
        urgenceHBox.setSpacing(20);
        Label dateFinProjLab = new Label("Date de fin de projet:");
        Label nivUrgLab = new Label("Niveau d'urgence:");
        DatePicker datePick = new DatePicker();
        ToggleGroup urgGroup = new ToggleGroup();
        RadioButton noramlRad = new RadioButton("NORMAL");
        RadioButton urgRad = new RadioButton("URGENCE");
        noramlRad.setToggleGroup(urgGroup);
        urgRad.setToggleGroup(urgGroup);
        dateUrgenceHBox.setSpacing(50);
        dateUrgenceHBox.setPadding(new Insets(0, 0, 0, 50));
        dateHBox.getChildren().addAll(dateFinProjLab, datePick);
        urgenceHBox.getChildren().addAll(nivUrgLab, noramlRad, urgRad);
        dateUrgenceHBox.getChildren().addAll(dateHBox, urgenceHBox);
        Separator urgSep = new Separator();


        rightQuoiVBox.getChildren().addAll(titreVBox, financeHBox, numFinanceHBox, descVBox, stackPane);

        globQuoiHBox.getChildren().addAll(quoiVBox, sepQuoi, rightQuoiVBox);
        rightEnregVBox.getChildren().addAll(pourEnrgHBox, quiSep, associeHBox, betweenSep, globQuoiHBox, urgSep, dateUrgenceHBox);
        
        HBox leftEnregHBox = new HBox();
        leftEnregHBox.setSpacing(30);
        leftEnregHBox.getChildren().addAll(leftEnregVBox, enregistrementSep);
        enregGridPane.setVgap(20);
        Button recordButton2 = new Button("Record");
        recordButton2.translateXProperty().set(775);
        recordButton2.setMinHeight(50);
        recordButton2.setMinWidth(75);
        enregGridPane.add(leftEnregHBox, 1,1);
        enregGridPane.add(rightEnregVBox,2,1);
        enregGridPane.add(recordButton2,2,2);

        modifyPourButton.setOnMouseClicked(e -> {
            List<String> res = dialogDemandeur.launcher(0);
            if (!res.get(0).equals("") && !res.get(1).equals("") && !res.get(2).equals("")) {
                nameLab.setText(res.get(0));
                prenomLab.setText(res.get(1));
                mailLab.setText(res.get(2));
            }
        });

        addAssocie.setOnMouseClicked(e -> {
            Associe newAs = dialogAddAssocie.dialChoose();
            if(newAs.getName().equals("") && newAs.getPrenom().equals("") && newAs.getMail().equals("")) {}
            else {
                associeList.add(newAs);
            }
            ObservableList<Associe> obsList = getAssocieTable(associeList);
            tableAssocie.setItems(obsList);
        });

        delAssocie.setOnMouseClicked(e -> {
            Associe selected = tableAssocie.getSelectionModel().getSelectedItems().get(0);
            associeList.remove(selected);
            tableAssocie.getItems().remove(selected);
        });

        checkCoupe.setOnAction(e -> {
            if (checkCopeaux.isSelected()) {
                nbCopeauxLab.setVisible(false);
                nbCopeauField.setVisible(false);
                epaisseurCopeauLab.setVisible(false);
                epaisseurCopeauField.setVisible(false);
                umLab2.setVisible(false);
                checkCopeaux.setSelected(false);
            }
            if (checkCoupe.isSelected()) {
                numLab.setVisible(true);
                nbCoupeField.setVisible(true);
                epaisseurLab.setVisible(true);
                epaisseurField.setVisible(true);
                umLab.setVisible(true);
            }
            else {
                numLab.setVisible(false);
                nbCoupeField.setVisible(false);
                epaisseurLab.setVisible(false);
                epaisseurField.setVisible(false);
                umLab.setVisible(false);
            }
        });

        checkCopeaux.setOnAction(e -> {
            if (checkCoupe.isSelected()) {
                numLab.setVisible(false);
                nbCoupeField.setVisible(false);
                epaisseurLab.setVisible(false);
                epaisseurField.setVisible(false);
                umLab.setVisible(false);
                checkCoupe.setSelected(false);
            }
            if (checkCopeaux.isSelected()) {
                nbCopeauxLab.setVisible(true);
                nbCopeauField.setVisible(true);
                epaisseurCopeauLab.setVisible(true);
                epaisseurCopeauField.setVisible(true);
                umLab2.setVisible(true);
            } else {
                nbCopeauxLab.setVisible(false);
                nbCopeauField.setVisible(false);
                epaisseurCopeauLab.setVisible(false);
                epaisseurCopeauField.setVisible(false);
                umLab2.setVisible(false);
            }
        });

        addMarquage.setOnMouseClicked(e -> {
            Technique newMrq = dialogMarquage.launcher();
            marquageList.add(newMrq);
            ObservableList<Technique> table = getMarquageTable(marquageList);
            marquageTable.setItems(table);
        });

        delMarquage.setOnMouseClicked(e -> {
            Technique selected = marquageTable.getSelectionModel().getSelectedItem();
            marquageList.remove(selected);
            marquageTable.getItems().removeAll(marquageTable.getSelectionModel().getSelectedItems());
        });

        linkMarquage.setOnMouseClicked(e -> {
            boolean bleech = bleechDial();
            ObservableList<Technique> marqs = marquageTable.getSelectionModel().getSelectedItems();
            int mx = 0;
            for(Technique mrq: marqs) {
                if(mrq.getLink() > mx) {
                    mx = mrq.getLink();
                }
            }
            if (mx == 0) {
                int max = 0;
                for(Technique mrq: marquageList) {
                    if (mrq.getLink() > max)  {
                        max = mrq.getLink();
                    }
                }
                for(Technique mrq: marqs) {
                    mrq.setLink(max + 1);
                    mrq.setBleeched(bleech);
                }
            } else {
                for(Technique mrq: marqs) {
                    mrq.setLink(mx);
                }
                for (Technique mrq: marquageList) {
                    if (mrq.getLink() == mx) {
                        mrq.setBleeched(bleech);
                    }
                }
            }
            marquageTable.refresh();
        });

        unlinkMarquage.setOnMouseClicked(e -> {
            ObservableList<Technique> marqs = marquageTable.getSelectionModel().getSelectedItems();
            for (Technique mrq: marqs) {
                mrq.setLink(0);
                mrq.setBleeched(false);
            }
            marquageTable.refresh();
        });

        recordButton2.setOnMouseClicked(e -> {
            incProjectNumber();
            Boolean coupe = false;
            String nbCoupe = "0";
            String epaisseurCoupe = "0";
            String echantillon = "0";
            Boolean copeaux = false;
            String nbCopeaux = "0";
            String epaisseurCopeaux = "0";
            Boolean finance = false;
            String numeroFinance = "null";
            if (checkCoupe.isSelected()) {
                coupe = true;
                nbCoupe = nbCoupeField.getText();
                epaisseurCoupe = epaisseurField.getText();
            }
            if (checkCopeaux.isSelected()) {
                copeaux = true;
                nbCopeaux = nbCopeauField.getText();
                epaisseurCopeaux = epaisseurCopeauField.getText();
            }
            if (ouiRad.isSelected()) {
                if (financeNumField.getText().length() == 0) {
                    showDial("Numéro de financement manquant");
                    e.consume();
                } else {
                    finance = true;
                    numeroFinance = financeNumField.getText();
                }
            }
            RadioButton selectEs = (RadioButton) groupEspece.getSelectedToggle();
            Users owner = new Users(nameLab.getText(), prenomLab.getText(), mailLab.getText());
            
            RadioButton slelectUrg = (RadioButton) urgGroup.getSelectedToggle();

            if (titreField.getText().equals("")) {
                showDial("Titre manquant");
                e.consume();
            } 
            if (humanRadio.isSelected() == false && sourisRadio.isSelected() == false && autreRadio.isSelected() == false) {
                showDial("Espèce manquante");
                e.consume();
            }
            try {
                Integer.parseInt(nbCoupe);
            } catch (Exception i) {
                showDial("Nombre de coupe incorrect");
                e.consume();
            }
            try {
                Integer.parseInt(epaisseurCoupe);
            } catch (Exception i) {
                showDial("Epaisseur de coupe incorrect");
                e.consume();
            }
            try {
                Integer.parseInt(nbEchan.getText());
            } catch (Exception i) {
                showDial("Nombre d'echantillon incorrect");
                e.consume();
            }
            echantillon = nbEchan.getText();
            if (!e.isConsumed()) {
                Project proj = new Project(titreField.getText(), descField.getText(), associeList, marquageList, epaisseurCoupe, nbCoupe, coupe, echantillon, selectEs.getText(), numProjField.getText(), owner, null, datePick.getValue().toString(), slelectUrg.getText(), copeaux, nbCopeaux, epaisseurCopeaux, finance, numeroFinance);
                for (Technique m: marquageList) {
                    if (m instanceof Marquage) {
                        Marquage marq = (Marquage)m;
                        System.out.println("IS CUUUSTOOM:" + marq.isCustom());
                        if(marq.isCustom()) {
                            new File(Params.basePath + "/pendingProject/P." + numProjField.getText() + "/MarquageCustom/").mkdir();
                            marq.saveCustom(Params.basePath + "/pendingProject/P." + numProjField.getText() + "/MarquageCustom/");
                        }
                    }
                }
                User.loadProject();
                Browse.reloadBrowser(User.projList);
                Logger.logEnregistrement(proj);
                associeList.clear();
                marquageList.clear();
                tableAssocie.setItems(getAssocieTable(associeList));
                marquageTable.setItems(getMarquageTable(marquageList));
                numProjField.setText(String.valueOf(Integer.parseInt(numProjField.getText()) + 1));
                checkCoupe.setSelected(false);
                epaisseurField.setText("");
                epaisseurField.setVisible(false);
                titreField.setText("");
                descField.setText("");
                umLab.setVisible(false);
                epaisseurLab.setVisible(false);
                nbEchan.setText("0");
                humanRadio.setSelected(false);
                sourisRadio.setSelected(false);
                autreRadio.setSelected(false);
                datePick.setValue(null);
                noramlRad.setSelected(false);
                urgRad.setSelected(false);
            }
        });

        return enregGridPane;
    }

    private static boolean bleechDial() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Input Dialog");
        dialog.setHeaderText("Lien bleech?");
        ButtonType oui = new ButtonType("Oui");
        ButtonType non = new ButtonType("Non");
        dialog.getDialogPane().getButtonTypes().addAll(oui,non);
        Optional<ButtonType> option = dialog.showAndWait();
        if(option.get()  == oui) {
            return true;
        } else if (option.get() == non) {
            return false;
        }
        return false;
    }

    private static Associe chooseReferent() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Input Dialog");
        dialog.setHeaderText("Choisir un referent pour le projet:");
        ComboBox comboBox = new ComboBox<>();
        ObservableList<String> obsList = FXCollections.observableArrayList();
        for(Associe a: associeList) {
            obsList.add(a.getName() + " " + a.getPrenom());
        }
        comboBox.setItems(obsList);
        dialog.getDialogPane().setContent(comboBox);
        dialog.getDialogPane().setMinHeight(135);
        comboBox.setOnAction(e -> {
            if (!comboBox.getValue().toString().equals("")) {
                dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            }
        });
        Optional<ButtonType> option = dialog.showAndWait();
        int idx = 0;
        if (option.get() == ButtonType.OK) {
            idx = comboBox.getSelectionModel().getSelectedIndex();
        }
        return associeList.get(idx);
    }

    private static void showDial(String message) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Mauvaise entrée");
        dialog.setContentText(message);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
        dialog.showAndWait();
    }

    public static ObservableList<Technique> getMarquageTable(List<Technique> marquList) {
        ObservableList<Technique> mrqList = FXCollections.observableArrayList();
        for(int i=0; i<marquList.size(); i++) {
            Technique actual = marquList.get(i);
            mrqList.add(actual);
        }
        return mrqList;
    }

    public static ObservableList<Associe> getAssocieTable(List associeList2) {
        ObservableList<Associe> obsList = FXCollections.observableArrayList();
        for(int i=0; i<associeList2.size(); i++) {
            Associe actual = (Associe) associeList2.get(i);
            obsList.add(actual);
        }
        return obsList;
    }

    private static String getProjectNumber() {
        String nProj = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(Params.basePath + "/counter.txt"));
            String line;
            while((line=reader.readLine()) != null) {
                String[] splitLine = line.split("#!#");
                if (splitLine[0].equals("project")) {
                    nProj = splitLine[1];
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nProj;
    }
    
    private static void incProjectNumber() {
        try {
            List<String> file = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(Params.basePath + "/counter.txt"));
            String line;
            while((line=reader.readLine()) != null) {
                if (line.contains("project")) {
                    System.out.println(line);
                    System.out.println(line.replace(line.split("#!#")[1], String.valueOf(Integer.parseInt(line.split("#!#")[1]) + 1)));
                    file.add(line.replace(line.split("#!#")[1], String.valueOf(Integer.parseInt(line.split("#!#")[1]) + 1)));
                } else {
                    file.add(line);
                }
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(Params.basePath + "/counter.txt"));
            for (String l: file) {
                writer.write(l + "\n");
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void importProj(Quote q) {
        titreField.setText(q.title);
        if (q.coupe) {
            checkCoupe.fire();
            nbCoupeField.setText(String.valueOf(q.coupeNb));
            epaisseurField.setText(String.valueOf((int) (q.coupeEpaisseur)));
        }
        nbEchan.setText(String.valueOf(q.echantillon));
        if (q.espece.equals("Human")) {
            humanRadio.setSelected(true);
            sourisRadio.setSelected(false);
            autreRadio.setSelected(false);
        } else if (q.espece.equals("Mouse")) {
            sourisRadio.setSelected(true);
            humanRadio.setSelected(false);
            autreRadio.setSelected(false);
        } else if (q.espece.equals("Other")) {
            autreRadio.setSelected(true);
            humanRadio.setSelected(false);
            sourisRadio.setSelected(false);
        }
        marquageList.clear();
        for (int i=0; i<q.marquageList.size(); i++) {
            marquageList.add(q.marquageList.get(i));
        }
        ObservableList<Technique> table = getMarquageTable(marquageList);
        marquageTable.setItems(table);
        nameLab.setText(q.ownerName);
        prenomLab.setText(q.ownerSurname);
        mailLab.setText(q.ownerMail);
    }
}
