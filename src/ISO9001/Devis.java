package ISO9001;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bouncycastle.asn1.x509.sigi.PersonalData;

import ISO9001.Dialog.dialogMarquage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Devis {

    static List<Technique> marquageList = new ArrayList<>();
    static List<String> comList = new ArrayList<>();
    static List<Float> priceList = new ArrayList<>();
    static boolean as48 = false;
    static boolean vs2000;
    static boolean coupeDevis;
    static boolean marquage;
    static boolean numerisation;
    static boolean analyse;
    static String titre;
    static String nom;
    static String prenom;
    static String date;
    static String mail;
    static boolean coupe;
    static String nbCoupe;
    static String epaisseur;
    static String nbEchantillon;
    static String espece;
    static String nProj;
    static String clientType;
    static String quoteNumber;
    static ListView<VBox> list = new ListView<>();
    static ObservableList<VBox> listView = FXCollections.observableArrayList();
    
    public static SplitPane build() {
        SplitPane splitPane = new SplitPane();
        
        VBox leftVBox = new VBox();
        Button newImportBut = new Button("Nouveau/Import");
        newImportBut.setMinWidth(150);
        newImportBut.setStyle("-fx-font-size:15; -fx-background-color: transparent;");
        newImportBut.setMinHeight(75);
        newImportBut.setOnMouseExited(e -> newImportBut.setStyle("-fx-background-color: transparent; -fx-font-size:15;"));
        newImportBut.setOnMouseEntered(e -> newImportBut.setStyle("-fx-background-color: lightgrey; -fx-font-size:15;"));
        Button viewDevisBut = new Button("Voir");
        viewDevisBut.setMinWidth(150);
        viewDevisBut.setStyle("-fx-font-size:15; -fx-background-color: transparent;");
        viewDevisBut.setMinHeight(75);
        viewDevisBut.setOnMouseExited(e -> viewDevisBut.setStyle("-fx-background-color: transparent; -fx-font-size:15;"));
        viewDevisBut.setOnMouseEntered(e -> viewDevisBut.setStyle("-fx-background-color: lightgrey; -fx-font-size:15;"));
        Button updateBut = new Button("Update");
        updateBut.setMinWidth(150);
        updateBut.setStyle("-fx-font-size:15; -fx-background-color: transparent;");
        updateBut.setMinHeight(75);
        updateBut.setOnMouseExited(e -> updateBut.setStyle("-fx-background-color: transparent; -fx-font-size:15;"));
        updateBut.setOnMouseEntered(e -> updateBut.setStyle("-fx-background-color: lightgrey; -fx-font-size:15;"));
        Separator sep = new Separator();
        Separator sep2 = new Separator();
        Separator sep3 = new Separator();
        leftVBox.getChildren().addAll(newImportBut, sep, viewDevisBut, sep2, updateBut, sep3);
        leftVBox.setMaxWidth(150);
        leftVBox.setMinWidth(150);
        leftVBox.setSpacing(10);
        leftVBox.setPadding(new Insets(50, 0, 0, 0));
        VBox simpleVBox = new VBox();
        simpleVBox.getChildren().add(newGui());
        splitPane.getItems().addAll(leftVBox, simpleVBox);

        updateBut.setOnAction(e -> {
            update();
        });

        newImportBut.setOnAction(e -> {
            simpleVBox.getChildren().clear();
            simpleVBox.getChildren().add(newGui());
        });

        viewDevisBut.setOnAction(e -> {
            simpleVBox.getChildren().clear();
            simpleVBox.getChildren().add(seeGui());
        });

        return splitPane;
    }

    
    private static VBox seeGui() {
        VBox globVBox = new VBox();
        globVBox.setSpacing(25);
        globVBox.setPadding(new Insets(30));
        Label title = new Label("Quotes:");
        list = new ListView<>();
        list.setMinHeight(775);
        listView.clear();
        loadList();
        list.setItems(listView);
        globVBox.getChildren().addAll(title, list);
        return globVBox;
    }

    private static void loadList() {
        File[] listQuoteFile = new File(Params.basePath + "/Quotes/").listFiles();
        for (File f: listQuoteFile) {
            listView.add(makeList(new Quote(f.toString())));
        }
    }

    private static VBox makeList(Quote q) {
        VBox globVBox = new VBox();
        HBox globHBox = new HBox();
        globHBox.setPadding(new Insets(15, 5, 15, 5));
        globHBox.setAlignment(Pos.CENTER_LEFT);
        globHBox.setSpacing(100);
        GridPane gridPane = new GridPane();
        gridPane.setHgap(30);
        gridPane.setVgap(20);
        Label quoteNumLab = new Label("Q.");
        Label titleLab = new Label("Titre:");
        Label demandeurLab = new Label("Demandeur:");
        Label clientTypeLab = new Label("Type de client:");
        Label priceLab = new Label("Prix:");
        Label especeLab = new Label("Espèce:");
        Label coupeLab = new Label("Coupe:");
        Label echanLab = new Label("Échantillon:");
        Label marquageLab = new Label("Marquage:");

        HBox quoteRefresh = new HBox();
        quoteRefresh.setSpacing(10);
        Button refreshBut = new Button();
        ImageView imgRefresh = new ImageView(new Image("ISO9001/File/refresh.png"));
        refreshBut.setGraphic(imgRefresh);
        HBox quoteNumHBox = new HBox();
        quoteNumHBox.setSpacing(2);
        Label quoteNum = new Label(q.getNumber());
        quoteNumHBox.getChildren().addAll(quoteNumLab, quoteNum);
        quoteRefresh.getChildren().addAll(quoteNumHBox, refreshBut);

        HBox titleHBox = new HBox();
        titleHBox.setSpacing(10);
        Label title = new Label(q.getTitle());
        titleHBox.getChildren().addAll(titleLab, title);

        HBox demandeurHBox = new HBox();
        demandeurHBox.setSpacing(10);
        Label demandeur = new Label(q.getDemandeur());
        demandeurHBox.getChildren().addAll(demandeurLab, demandeur);
        
        HBox clientTypeHBox = new HBox();
        clientTypeHBox.setSpacing(10);
        Label clientType = new Label(q.getClient());
        clientTypeHBox.getChildren().addAll(clientTypeLab, clientType);

        HBox priceHBox = new HBox();
        priceHBox.setSpacing(10);
        Label price = new Label(q.getPrice());
        priceHBox.getChildren().addAll(priceLab, price);

        HBox especeHBox = new HBox();
        especeHBox.setSpacing(10);
        Label espece = new Label(q.getEspece());
        especeHBox.getChildren().addAll(especeLab, espece);

        HBox coupeHBox = new HBox();
        coupeHBox.setSpacing(10);
        Label coupe = new Label(q.getCoupe());
        coupeHBox.getChildren().addAll(coupeLab, coupe);

        HBox echanHBox = new HBox();
        echanHBox.setSpacing(10);
        Label echan = new Label(q.getEchantillon());
        echanHBox.getChildren().addAll(echanLab, echan);

        HBox marquageHBox = new HBox();
        marquageHBox.setSpacing(10);
        Label marquage = new Label(q.getMarquageNumber());
        marquageHBox.getChildren().addAll(marquageLab, marquage);

        gridPane.add(quoteRefresh,0, 0);
        gridPane.add(titleHBox,0, 1);
        gridPane.add(demandeurHBox, 0, 2);
        gridPane.add(clientTypeHBox, 1 ,0);
        gridPane.add(priceHBox, 1, 1);
        gridPane.add(especeHBox, 1, 2);
        gridPane.add(coupeHBox, 2, 0);
        gridPane.add(echanHBox, 2, 1);
        gridPane.add(marquageHBox, 2, 2);

        HBox buttonHBox = new HBox();
        buttonHBox.setSpacing(50);
        Button openPDFBut = new Button();
        ImageView imgPDF = new ImageView(new Image("ISO9001/File/pdf.png"));
        openPDFBut.setGraphic(imgPDF);
        openPDFBut.setPrefSize(75, 75);
        Button importToProjBut = new Button();
        ImageView imgImport = new ImageView(new Image("ISO9001/File/import.png"));
        importToProjBut.setGraphic(imgImport);
        importToProjBut.setPrefSize(75, 75);
        buttonHBox.setAlignment(Pos.CENTER);
        buttonHBox.getChildren().addAll(openPDFBut, importToProjBut);


        globHBox.getChildren().addAll(gridPane, buttonHBox);

        Separator sep = new Separator();
        globVBox.getChildren().addAll(globHBox, sep);

        openPDFBut.setOnAction( e -> {
            try {
                Runtime.getRuntime().exec("CMD /C START " + q.path + "/Devis.pdf");
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });

        importToProjBut.setOnAction( e -> {
            Enregistrement.importProj(q);
            Main.openEnrg();
        });

        refreshBut.setOnAction(e -> {
            DevisPDF.createPDF(q);
        });


        return globVBox;
    }


    private static VBox newGui() {
        VBox globVBox = new VBox();
        globVBox.setSpacing(25);
        globVBox.setPadding(new Insets(50, 20, 20, 20));

        Button clearBut = new Button("Clear");
        clearBut.setTranslateX(150);
        clearBut.setTranslateY(-30);

        HBox demandeurHBox = new HBox();
        demandeurHBox.setAlignment(Pos.CENTER);
        demandeurHBox.setSpacing(25);
        Label demandeurLab = new Label("Demandeur:");
        Label nameLab = new Label(User.nom);
        Label prenomLab = new Label(User.prenom);
        Label mailLab = new Label(User.mail);
        Button modifyPourBut = new Button("Modifier");
        demandeurHBox.getChildren().addAll(demandeurLab, nameLab, prenomLab, mailLab, modifyPourBut, clearBut);

        HBox titleHBox = new HBox();
        titleHBox.setSpacing(25);
        titleHBox.setAlignment(Pos.CENTER);
        Label titleLab = new Label("Titre:");
        TextField titleField = new TextField();
        titleField.setMinWidth(300);
        titleHBox.getChildren().addAll(titleLab, titleField);

        Separator sep1 = new Separator();
        globVBox.getChildren().addAll(demandeurHBox, titleHBox,sep1);

        HBox manuBox = new HBox();
        manuBox.setSpacing(20);
        VBox marquageVBox = new VBox();
        marquageVBox.setSpacing(10);
        HBox marquageHBox = new HBox();
        marquageHBox.setSpacing(20);
        Label marquageLab = new Label("Marquage:");
        Button addMarquage = new Button("Ajouter");
        Button delMarquage = new Button("Retirer");
        TableView<Technique> marquageTable = new TableView<>();
        marquageTable.setPrefWidth(380);
        marquageTable.setPrefHeight(200);
        TableColumn<Technique, String> marquageCol = new TableColumn<>("Marquage");
        TableColumn<Technique, String> techniqueCol = new TableColumn<>("Révélation");
        TableColumn<Technique, String> grooCol = new TableColumn<>("Grossisement");

        marquageCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        techniqueCol.setCellValueFactory(new PropertyValueFactory<>("technique"));
        grooCol.setCellValueFactory(new PropertyValueFactory<>("grossisement"));
        marquageTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        marquageCol.setMinWidth(130);
        techniqueCol.setMinWidth(130);
        grooCol.setMinWidth(130);
        marquageTable.getColumns().addAll(marquageCol, techniqueCol, grooCol);
        marquageHBox.getChildren().addAll(marquageLab, addMarquage, delMarquage);  
        marquageVBox.getChildren().addAll(marquageHBox, marquageTable);
        manuBox.getChildren().addAll(marquageVBox);

        VBox rightVBox = new VBox();
        rightVBox.setPadding(new Insets(50, 0, 0, 0));
        rightVBox.setSpacing(30);

        HBox coupeHBox = new HBox();
        coupeHBox.setSpacing(15);
        Label coupeLab = new Label("Coupe:");
        CheckBox coupeCheck = new CheckBox();
        Label nbCoupeLab = new Label("nombre de coupes:");
        TextField nbCoupeField = new TextField();
        nbCoupeField.setMaxWidth(35);
        Label epaisseurLab = new Label("epaisseur:");
        TextField epaisseurField = new TextField("4");
        epaisseurField.setMaxWidth(35);
        Label umLab = new Label("µm");
        coupeHBox.getChildren().addAll(coupeLab, coupeCheck);
        rightVBox.getChildren().add(coupeHBox);

        HBox nbEchanHBox = new HBox();
        nbEchanHBox.setSpacing(25);
        Label nbEchanLab = new Label("Nombre échantillon:");
        TextField nbEchanField = new TextField("0");
        nbEchanField.setMaxWidth(35);
        nbEchanHBox.getChildren().addAll(nbEchanLab, nbEchanField);
        rightVBox.getChildren().add(nbEchanHBox);

        HBox especeHBox = new HBox();
        especeHBox.setSpacing(45);
        Label espLab = new Label("Espèce:");
        ToggleGroup groupEspece = new ToggleGroup();
        RadioButton humanRadio = new RadioButton("Human");
        RadioButton mouseRadio = new RadioButton("Mouse");
        RadioButton otherRadio = new RadioButton("Other");
        humanRadio.setToggleGroup(groupEspece);
        mouseRadio.setToggleGroup(groupEspece);
        otherRadio.setToggleGroup(groupEspece);
        especeHBox.getChildren().addAll(espLab, humanRadio, mouseRadio, otherRadio);
        rightVBox.getChildren().add(especeHBox);

        manuBox.getChildren().add(rightVBox);

        VBox importVBox = new VBox();
        importVBox.setSpacing(15);
        Label importLab = new Label("Import Project:");

        TableView<Project> tableProj = new TableView<>();
        TableColumn<Project, String> numCol = new TableColumn<>("N°");
        TableColumn<Project, String> demandeurCol = new TableColumn<>("Demandeur");
        TableColumn<Project, String> espCol = new TableColumn<>("Espèce");
        TableColumn<Project, String> nameCol = new TableColumn<>("Name");
        TableColumn<Project, String> echanCol = new TableColumn<>("Echantillon");
        TableColumn<Project, String> coupeCol = new TableColumn<>("Coupe");
        TableColumn<Project, String> marquageColumn = new TableColumn<>("Marquage");
        numCol.setPrefWidth(50);
        demandeurCol.setPrefWidth(100);
        espCol.setPrefWidth(80);
        nameCol.setPrefWidth(200);
        echanCol.setPrefWidth(75);
        coupeCol.setPrefWidth(75);
        marquageColumn.setPrefWidth(250);
        numCol.setCellValueFactory(new PropertyValueFactory<>("number"));
        demandeurCol.setCellValueFactory(new PropertyValueFactory<>("Owner"));
        espCol.setCellValueFactory(new PropertyValueFactory<>("esp"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        echanCol.setCellValueFactory(new PropertyValueFactory<>("echantillon"));
        coupeCol.setCellValueFactory(new PropertyValueFactory<>("coupe"));
        marquageColumn.setCellValueFactory(new PropertyValueFactory<>("marquage"));
        tableProj.getColumns().addAll(numCol, demandeurCol, espCol, nameCol, echanCol, coupeCol, marquageColumn);
        tableProj.setItems(Administration.loadProj());
        tableProj.setMaxHeight(200);

        Separator sep2 = new Separator();

        importVBox.getChildren().addAll(importLab, tableProj);


        VBox devisVBox = new VBox();
        devisVBox.setSpacing(15);
        HBox indusAcadHBox = new HBox();
        indusAcadHBox.setSpacing(25);
        Label selectLab = new Label("Select:");
        ToggleGroup indusAcadGroup = new ToggleGroup();
        RadioButton indusRadio = new RadioButton("Industrielle");
        RadioButton acadRadio = new RadioButton("Académique");
        indusRadio.setToggleGroup(indusAcadGroup);
        acadRadio.setToggleGroup(indusAcadGroup);
        indusAcadHBox.getChildren().addAll(selectLab, indusRadio, acadRadio);

        HBox selectHBox = new HBox();
        selectHBox.setSpacing(45);
        VBox materielVBox = new VBox();
        materielVBox.setSpacing(5);
        Label materielTitleLab = new Label("Matériel:");
        CheckBox as48Check = new CheckBox("AS48");
        CheckBox vs2000Check = new CheckBox("VS2000");
        Separator sepMateriel = new Separator();
        materielVBox.getChildren().addAll(materielTitleLab, sepMateriel, as48Check, vs2000Check);

        VBox personelVBox = new VBox();
        personelVBox.setSpacing(5);
        Label personnelTitleLab = new Label("Personnel:");
        CheckBox coupeDevisCheck = new CheckBox("Coupe");
        CheckBox marquageCheck = new CheckBox("Marquage");
        CheckBox numerisationCheck = new CheckBox("Numérisation");
        CheckBox analyseCheck = new CheckBox("Analyse");
        Separator sepPersonel = new Separator();
        personelVBox.getChildren().addAll(personnelTitleLab, sepPersonel, coupeDevisCheck, marquageCheck, numerisationCheck, analyseCheck);
        Separator horizSep = new Separator();
        horizSep.setOrientation(Orientation.VERTICAL);
        selectHBox.getChildren().addAll(materielVBox, horizSep, personelVBox);

        Separator sep = new Separator();

        devisVBox.getChildren().addAll(indusAcadHBox, selectHBox);

        HBox devisHbox = new HBox();
        devisHbox.setSpacing(50);
        devisHbox.setAlignment(Pos.CENTER);
        Button devisBut = new Button("Devis");
        devisBut.setPrefSize(100, 75);

        StackPane stackPane = new StackPane();
        Rectangle rect = new Rectangle(200,100);
        rect.setFill(Color.TRANSPARENT);
        rect.setStroke(Color.GRAY);
        VBox stackVBox = new VBox();
        Label projectNumLab = new Label("Numero de Devis:");
        HBox rectHBox = new HBox();
        Label pLab = new Label("    Q.");
        TextField numProjField = new TextField(getCounter());
        rectHBox.getChildren().addAll(pLab, numProjField);
        rectHBox.setPadding(new Insets(0, 30, 0, 0));
        rectHBox.setSpacing(10);
        stackVBox.getChildren().addAll(projectNumLab, rectHBox);
        stackVBox.setPadding(new Insets(35, 0, 0, 30));
        stackVBox.setSpacing(20);
        numProjField.setEditable(false);
        stackPane.getChildren().addAll(rect, stackVBox);
        HBox projRecordHBox = new HBox();
        projRecordHBox.setSpacing(20);

        Button addBut = new Button("+");
        addBut.setPrefSize(50, 50);

        devisHbox.getChildren().addAll(devisVBox, addBut, stackPane, devisBut);

        globVBox.getChildren().addAll(manuBox, sep2, importVBox, sep, devisHbox);

        clearBut.setOnAction(e -> {
            titleField.setText("");
            marquageList.clear();
            marquageTable.setItems(getMarquageTable(marquageList));
            nameLab.setText(User.nom);
            prenomLab.setText(User.prenom);
            mailLab.setText(User.mail);
            coupeCheck.setSelected(false);
            coupeHBox.getChildren().clear();
            coupeHBox.getChildren().addAll(coupeLab, coupeCheck);
            nbCoupeField.setText("0");
            epaisseurField.setText("4");
            nbEchanField.setText("0");
            humanRadio.setSelected(false);
            mouseRadio.setSelected(false);
            otherRadio.setSelected(false);
            tableProj.getSelectionModel().clearSelection();
            indusRadio.setSelected(false);
            acadRadio.setSelected(false);
            as48Check.setSelected(false);
            vs2000Check.setSelected(false);
            coupeDevisCheck.setSelected(false);
            marquageCheck.setSelected(false);
            numerisationCheck.setSelected(false);
            analyseCheck.setSelected(false);
            priceList.clear();
            comList.clear();
        });

        delMarquage.setOnMouseClicked(e -> {
            Technique selected = marquageTable.getSelectionModel().getSelectedItem();
            marquageList.remove(selected);
            marquageTable.getItems().removeAll(marquageTable.getSelectionModel().getSelectedItems());
        });

        coupeCheck.setOnAction(e -> {
            if (coupeCheck.isSelected()) {
                coupeHBox.getChildren().clear();
                coupeHBox.getChildren().addAll(coupeLab, coupeCheck, nbCoupeLab, nbCoupeField, epaisseurLab, epaisseurField, umLab);
            } else {
                coupeHBox.getChildren().clear();
                coupeHBox.getChildren().addAll(coupeLab, coupeCheck);
            }
        });

        addMarquage.setOnMouseClicked(e -> {
            Technique newMrq = dialogMarquage.launcher();
            marquageList.add(newMrq);
            ObservableList<Technique> table = getMarquageTable(marquageList);
            marquageTable.setItems(table);
        });

        tableProj.setOnMouseClicked(e -> {
            marquageList.clear();
            Project selected = tableProj.getSelectionModel().getSelectedItem();
            for (int i=0; i<selected.listMarquage.size(); i++) {
                if (selected.listMarquage.get(i) instanceof Marquage) {
                    marquageList.add(selected.listMarquage.get(i));
                } else {
                    marquageList.add(selected.listMarquage.get(i));
                }
            }
            ObservableList<Technique> table = getMarquageTable(marquageList);
            marquageTable.setItems(table);
            titleField.setText(selected.getTitle());
            if (selected.coupe) {
                coupeCheck.setSelected(true);
                coupeCheck.fire();
                nbCoupeField.setText(selected.getNombreCoupe());
                epaisseurField.setText(selected.getEpaisseur());
            }
            nbEchanField.setText(selected.getEchantillon());
            if (selected.getEsp().equals("Human")) {
                humanRadio.setSelected(true);
            } else if (selected.getEsp().equals("Mouse")) {
                mouseRadio.setSelected(true);
            } else if (selected.getEsp().equals("Other")) {
                otherRadio.setSelected(true);
            }
            nameLab.setText(selected.getOwnerObj().getName());
            prenomLab.setText(selected.getOwnerObj().getSurname());
            mailLab.setText(selected.getOwnerObj().getMail());
        });

        indusRadio.setOnAction(e -> {
            if (indusRadio.isSelected()) {
                as48Check.setSelected(true);
                vs2000Check.setSelected(true);
                coupeDevisCheck.setSelected(true);
                marquageCheck.setSelected(true);
                numerisationCheck.setSelected(true);
                analyseCheck.setSelected(true);
            }
        });

        acadRadio.setOnAction(e -> {
            as48Check.setSelected(true);
            vs2000Check.setSelected(true);
            coupeDevisCheck.setSelected(false);
            marquageCheck.setSelected(false);
            numerisationCheck.setSelected(false);
            analyseCheck.setSelected(false);
        });

        addBut.setOnAction(e -> {
            dialAdd();
        });

        devisBut.setOnAction(e -> {
            as48 = as48Check.isSelected();
            vs2000 = vs2000Check.isSelected();
            coupeDevis = coupeDevisCheck.isSelected();
            marquage = marquageCheck.isSelected();
            numerisation = numerisationCheck.isSelected();
            analyse = analyseCheck.isSelected();
            if (indusRadio.isSelected()) {
                clientType = "Industrielle";
            } else {
                clientType = "Académique";
            }
            titre = titleField.getText();
            if (coupeCheck.isSelected()) {
                coupe = coupeCheck.isSelected();
                nbCoupe = nbCoupeField.getText();
                epaisseur = String.valueOf(epaisseurField.getText());
            }
            nbEchantillon = nbEchanField.getText();
            if (humanRadio.isSelected()) {
                espece = "Human";
            } else if (mouseRadio.isSelected()) {
                espece = "Mouse";
            } else if (otherRadio.isSelected()) {
                espece = "Other";
            }
            quoteNumber = numProjField.getText();
            prenom = prenomLab.getText();
            nom = nameLab.getText();
            mail = mailLab.getText();
            save();
            clearBut.fire();
            incCounter();
            numProjField.setText(getCounter());
        });
        return globVBox;
    }

     public static ObservableList<Technique> getMarquageTable(List<Technique> marquList) {
        ObservableList<Technique> mrqList = FXCollections.observableArrayList();
        for(int i=0; i<marquList.size(); i++) {
            Technique actual = marquList.get(i);
            mrqList.add(actual);
        }
        return mrqList;
    }

    private static void dialAdd() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Price ajustement");
        VBox globVBox = new VBox();
        globVBox.setSpacing(20);
        globVBox.setAlignment(Pos.BASELINE_CENTER);
        VBox paneVBox = new VBox();
        paneVBox.setSpacing(5);
        Button addBut = new Button("+");
        addBut.setMinWidth(250);
        globVBox.getChildren().addAll(addBut, paneVBox);
        paneVBox.getChildren().add(newLine());

        for (int i=0; i<priceList.size(); i++) {
            HBox globHBox = new HBox();
            globHBox.setSpacing(10);
            TextField comField = new TextField(comList.get(i));
            comField.setMinWidth(200);
            TextField moneyField = new TextField(String.valueOf(priceList.get(i)));
            moneyField.setMaxWidth(40);
            Button delBut = new Button("-");
            Label eurLab = new Label("€");
            globHBox.getChildren().addAll(comField, moneyField, eurLab, delBut);
            paneVBox.getChildren().add(globHBox);
            delBut.setOnAction(e -> {
                globHBox.getChildren().clear();
            });
        }

        addBut.setOnAction(e -> {
            dialog.setHeight(dialog.getHeight() + 40);
            Separator sep = new Separator();
            paneVBox.getChildren().addAll(sep, newLine());
        });

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(globVBox);
        Optional<ButtonType> option = dialog.showAndWait();

        if (option.get() == ButtonType.OK) {
            comList.clear();
            priceList.clear();
            for(int i=0; i<paneVBox.getChildren().size(); i++) {
                try {
                    HBox selected = (HBox) paneVBox.getChildren().get(i);
                    TextField com = (TextField) selected.getChildren().get(0);
                    TextField money = (TextField) selected.getChildren().get(1);
                    if (!com.getText().equals("")) {
                        comList.add(com.getText());
                        priceList.add(Float.parseFloat(money.getText()));
                    }
                } catch (Exception e) {}
            }
        }
    }

    private static HBox newLine() {
        HBox globHBox = new HBox();
        globHBox.setSpacing(10);
        TextField comField = new TextField();
        comField.setMinWidth(200);
        TextField moneyField = new TextField();
        moneyField.setMaxWidth(40);
        Label eurLab = new Label("€");
        Button delBut = new Button("-");
        globHBox.getChildren().addAll(comField, moneyField, eurLab, delBut);

        delBut.setOnAction(e -> {
            globHBox.getChildren().clear();
        });
        return globHBox;
    }

    private static String getCounter() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(Params.basePath + "/counter.txt"));
            String line;
            String count = "";
            while((line=reader.readLine()) != null) {
                String[] splitLine = line.split("#!#");
                if (splitLine[0].equals("devis")) {
                    count = splitLine[1];
                    break;
                }
            }
            reader.close();
            return count;
        } catch (Exception e) {}
        return null;
    }

    private static void incCounter() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(Params.basePath + "/counter.txt"));
            List<String> file = new ArrayList<>();
            String line;
            while((line=reader.readLine()) != null) {
                String[] splitLine = line.split("#!#");
                if (splitLine[0].equals("devis")) {
                    String count = String.valueOf(Integer.parseInt(splitLine[1]) + 1);
                    file.add("devis#!#" + count + "\n");
                } else {
                    file.add(line + "\n");
                }
            }
            reader.close();
            BufferedWriter writer = new BufferedWriter(new FileWriter(Params.basePath + "/counter.txt"));
            for (String l: file) {
                writer.write(l);
            }
            writer.close();
        } catch (Exception e) {}
    }

    private static void save() {
        new File(Params.basePath + "/Quotes/Q." + quoteNumber).mkdir();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(Params.basePath + "/Quotes/Q." + quoteNumber + "/Info.txt"));
            writer.write("clientType#!#" + clientType + "\n");
            writer.write("as48#!#" + as48 + "\n");
            writer.write("vs2000#!#" + vs2000 + "\n");
            writer.write("coupeDevis#!#" + coupeDevis + "\n");
            writer.write("marquage#!#" + marquage + "\n");
            writer.write("numerisation#!#" + numerisation + "\n");
            writer.write("analyse#!#" + analyse + "\n");
            writer.write("quoteNumber#!#" + quoteNumber + "\n");
            writer.write("demandeurName#!#" + nom + "\n");
            writer.write("demandeurPrenom#!#" + prenom + "\n");
            writer.write("demandeurMail#!#" + mail + "\n");
            writer.write("titre#!#" + titre + "\n");
            writer.write("coupe#!#" + coupe + "\n");
            if (coupe) {
                writer.write("nbCoupe#!#" + nbCoupe + "\n");
                writer.write("epaisseur#!#" + epaisseur + "\n");
            }
            writer.write("nombreEchantillon#!#" + nbEchantillon + "\n");
            writer.write("date#!#" + java.time.LocalDate.now().toString() + "\n");
            date = java.time.LocalDate.now().toString();
            writer.write("espece#!#" + espece + "\n");
            writer.close();
            writer = new BufferedWriter(new FileWriter(Params.basePath + "/Quotes/Q." + quoteNumber + "/MarquageList.txt"));
            for (Technique m: marquageList) {
                System.out.println(m.getName());
                try {
                    writer.write(m.getPath() + "#!#" + m.getGrossisement() + "#!#" + m.getNum() + "#!#" + m.getRev() + "\n");
                } catch (Exception e) {
                    writer.write(m.getPath() + "#!#" + m.getGrossisement() + "#!#" + m.getNum() + "#!#" + "NA" + "\n");
                }
            }
            writer.close();
            writer = new BufferedWriter(new FileWriter(Params.basePath + "/Quotes/Q." + quoteNumber + "/Suplement.txt"));
             for (int i=0; i<priceList.size(); i++) {
                writer.write(comList.get(i) + "#!#" + priceList.get(i) + "\n");
            }
            writer.close();
            DevisPDF.createPDF(new Quote(Params.basePath + "/Quotes/Q." + quoteNumber));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void update() {
        try {
            InputStream inputStream = new FileInputStream(new File(Params.basePath + "/Template/Reactif/Fiches_produits_Acs.xlsx"));
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(5);
            int max = sheet.getLastRowNum();
            for(int i=2; i<max+1;i++) {
                BufferedWriter writer;
                String namePrice;
                try {
                    namePrice = sheet.getRow(i).getCell(0).getStringCellValue().replace(".0", "") + ".txt";
                } catch (Exception e) {
                    namePrice = String.valueOf(sheet.getRow(i).getCell(0).getNumericCellValue()).replace(".0", "") + ".txt";
                }
                System.out.println(namePrice);
                writer = new BufferedWriter(new FileWriter(Params.basePath + "/Template/Quotes/parsedPrice/" + namePrice));
                writer.write("name#!#" + sheet.getRow(i).getCell(1).getStringCellValue() + "\n");
                writer.write("coutMateriel#!#" + sheet.getRow(i).getCell(6).getNumericCellValue() + "\n");
                writer.write("coutMaterielCoupe#!#" + sheet.getRow(i).getCell(4).getNumericCellValue() + "\n");
                writer.write("coutAC#!#" + sheet.getRow(i).getCell(3).getNumericCellValue() + "\n");
                writer.write("coutReactif#!#" + sheet.getRow(i).getCell(5).getNumericCellValue() + "\n");
                writer.write("coutAs48#!#" + sheet.getRow(i).getCell(7).getNumericCellValue() + "\n");
                writer.write("coutVs2000#!#" + sheet.getRow(i).getCell(8).getNumericCellValue() + "\n");
                writer.write("coutCoupe#!#" + sheet.getRow(i).getCell(9).getNumericCellValue() + "\n");
                writer.write("coutMarquage#!#" + sheet.getRow(i).getCell(10).getNumericCellValue() + "\n");
                writer.write("coutNumerisation#!#" + sheet.getRow(i).getCell(11).getNumericCellValue() + "\n");
                writer.write("coutAnalyse#!#" + sheet.getRow(i).getCell(12).getNumericCellValue() + "\n");
                writer.close();
                System.out.println("Update: " + i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
