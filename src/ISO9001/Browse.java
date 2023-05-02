package ISO9001;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.cert.PKIXRevocationChecker.Option;
import java.sql.Date;
import java.text.StringCharacterIterator;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.apache.batik.transcoder.ToSVGAbstractTranscoder;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.poi.hpsf.Array;
import org.apache.poi.sl.draw.geom.Outline;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.tool.Extension.Param;
import ISO9001.Dialog.dialogAddAssocie;
import ISO9001.Dialog.dialogMarquage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class Browse {

    private static VBox browseVBox;

    public static BorderPane build() {

        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(10, 20, 0, 20));
        
        // Search
        HBox searchHBox = new HBox();
        VBox searchVBox = new VBox();
        searchVBox.setSpacing(10);
        searchHBox.setSpacing(10);
        Label searchLab = new Label("Recherche:");
        TextField searchField = new TextField();
        searchField.setMinWidth(920);
        ImageView loupe = new ImageView(new Image("ISO9001/File/loupe.png"));
        Button searchBtn = new Button();
        searchBtn.setGraphic(loupe);
        searchHBox.getChildren().addAll(searchField, searchBtn);
        searchVBox.getChildren().addAll(searchLab, searchHBox);
        pane.setTop(searchVBox);

        // Browse Proj

        ScrollPane browsePane = new ScrollPane();
        browsePane.setPadding(new Insets(20));
        pane.setCenter(browsePane);
        pane.setMargin(browsePane, new Insets(20,0,10,0));
        browseVBox = new VBox();
        browseVBox.setSpacing(10);
        browsePane.setContent(browseVBox);
        reloadBrowser(User.projList);


        searchBtn.setOnAction(e -> {
            if (searchField.getText().equals("VDPink")) {
                Main.enregGridPane.setStyle("-fx-background-color: pink");
                Main.browseScene.setStyle("-fx-background-color: pink");
                Main.mapBox.setStyle("-fx-background-color: pink");
                Main.adminPane.setStyle("-fx-background-color: pink");
            }
            else if (searchField.getText().isEmpty()) {
                reloadBrowser(User.projList);
            } else {
                String search = searchField.getText();
                reloadBrowser(searchEngine.searchProj(search));
            }
        });

        searchField.setOnKeyPressed(e -> {
            if (e.getCode().toString().equals("ENTER")) {
                searchBtn.fire();
            }
        });
        
        return pane;
    }

    public static void reloadBrowser(List<Project> lst) {
        User.loadProject();
        List<String> notificationNote = Notification.note;
        browseVBox.getChildren().clear();
        Deque<VBox> tmpList = new ArrayDeque<>();
        try {
            for(Project proj: removeDuplicate(lst)) {
                VBox tile;
                if (notificationNote.contains(proj.nProject)) {
                    tile = createTile(proj, true);
                } else {
                    tile = createTile(proj, false);
                }
                if (proj.getPrio().equals("URGENCE")) {
                    tmpList.addFirst(tile);
                }
            }
            List<Project> lstProj = removeDuplicate(lst);
            while (lstProj.size() > 0) {
                int min = 10000*10000*10000*10000;
                int idx = 0;
                for (int i=0; i< lstProj.size(); i++) {
                    if (Integer.parseInt(lstProj.get(i).getNproject()) < min && !lstProj.get(i).getPrio().equals("URGENCE")) {
                            min = Integer.parseInt(lstProj.get(i).getNproject());
                            idx = i;
                    }
                }
                if (!lstProj.get(idx).getPrio().equals("URGENCE")) {
                    if (notificationNote.contains(lstProj.get(idx).nProject)) {
                        tmpList.add(createTile(lstProj.get(idx), true));
                    } else {
                        tmpList.add(createTile(lstProj.get(idx), false));
                    }
                }
                lstProj.remove(idx);
            }
            for (VBox t: tmpList) {
                Separator sep = new Separator();
                sep.setMinWidth(200);
                browseVBox.getChildren().addAll(t, sep);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Project> removeDuplicate(List<Project> lst) {
        List<Project> uniq = new ArrayList<>();
        for (Project p: lst) {
            if (!uniq.contains(p)) {
                uniq.add(p);
            }
        }
        return uniq;
    }

    private static VBox createTile(Project proj, Boolean notifNote) throws Exception {
        VBox globVBox = new VBox();
        globVBox.setSpacing(0);
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setMinWidth(900);
        hBox.setAlignment(Pos.CENTER);
        //hBox.setAlignment(Pos.CENTER);
        Label nProjectLab = new Label("P." + proj.nProject);
        Label ownerLab = new Label("Demandeur:  " + proj.getOwner());
        Label dateFinLab = new Label("Date Fin:  " + proj.getDateFin());
        Label nameLab = new Label("Name:    " + proj.name);
        Label coupeLab = new Label("Coupe:  " + proj.nombreCoupe);
        Label echantillonLab = new Label("Echantillon:  " + (Excel_util.getNumberCoupe(proj) - 8) + "/" + proj.echantillon);
        Label especeLab = new Label("Espece:  " + proj.espece);
        Label refLab = new Label("Référent:  " + proj.referent.getPrenom() + " " + proj.referent.getName());
        Label dateLab = new Label("Date:    " + proj.getDate());
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(30);
        gridPane.setVgap(20);
        gridPane.add(ownerLab, 1,1);
        gridPane.add(dateFinLab, 2,1);
        gridPane.add(nameLab, 1, 2);
        gridPane.add(coupeLab, 2, 2);
        gridPane.add(echantillonLab, 1,3);
        gridPane.add(especeLab,2,3);
        gridPane.add(refLab,1,4);
        gridPane.add(dateLab,2,4);
        GridPane gridPaneBut = new GridPane();
        gridPaneBut.setVgap(15);
        gridPaneBut.setHgap(15);
        gridPaneBut.setAlignment(Pos.TOP_CENTER);
        Button enregEchanBut = new Button("Echantillons");
        ImageView imgEchan = new ImageView(new Image("ISO9001/File/echantillon.png"));
        enregEchanBut.setGraphic(imgEchan);
        enregEchanBut.setPrefHeight(55);
        Button anoBut = new Button("Anonymiser");
        ImageView imgAno = new ImageView(new Image("ISO9001/File/ano.png"));
        anoBut.setGraphic(imgAno);
        anoBut.setPrefHeight(55);
        if (Excel_util.isAno(proj)) {
            enregEchanBut.setDisable(true);
            anoBut.setDisable(true);
        }
        Button genCoupBut = new Button("Coupe");
        ImageView imgCoupe = new ImageView(new Image("ISO9001/File/microtome.png"));
        genCoupBut.setGraphic(imgCoupe);
        Button genMarqBut = new Button("Marquages\n(" + getNumberProcededMarquage(proj) + "/" + getNumberMarquage(proj) + ")");
        ImageView imgMrq = new ImageView(new Image("ISO9001/File/As48.png"));
        genMarqBut.setGraphic(imgMrq);
        genMarqBut.setPrefHeight(55);
        Button genNumBut = new Button("Numérisation\n(" + getNumberProcededNumerisation(proj) + "/" + getNumberNumerisation(proj) + ")");
        ImageView imgNum = new ImageView(new Image("ISO9001/File/scanner.png"));
        genNumBut.setGraphic(imgNum);
        genNumBut.setPrefHeight(55);
        Button updateTkBut = new Button("Suivi TK");
        ImageView imgTK = new ImageView(new Image("ISO9001/File/folder.png"));
        updateTkBut.setGraphic(imgTK);
        updateTkBut.setPrefHeight(55);
        Button etiquetteBut = new Button();
        ImageView imageEtiquette = new ImageView(new Image("ISO9001/File/qrCode.png"));
        etiquetteBut.setGraphic(imageEtiquette);
        etiquetteBut.setPrefHeight(25);
        Button archiveBut = new Button();
        ImageView imageArchive = new ImageView(new Image("ISO9001/File/archive.png"));
        archiveBut.setGraphic(imageArchive);
        archiveBut.setPrefHeight(25);
        if (User.grade.equals("User")) {
            anoBut.setDisable(true);
            updateTkBut.setDisable(true);
        }
        if (!proj.coupe && !proj.copeaux) {
            genCoupBut.setDisable(true);
        }
        if (!proj.scan) {
            genNumBut.setDisable(true);
        }
        if (proj.listMarquage.size() == 0) {
            genMarqBut.setDisable(true);
        }
        Boolean check = true;
        for (Technique tech: proj.listMarquage) {
            if (tech instanceof Marquage) {
                Marquage m = (Marquage)tech;
                if (!m.isCustom()) {
                    check = false;
                }
            } else {
                check = false;
            }
        }
        if (check) {
            genMarqBut.setDisable(true);
        }
        gridPaneBut.add(enregEchanBut, 1, 1);
        gridPaneBut.add(anoBut, 2,1);
        gridPaneBut.add(genCoupBut, 1, 2);
        gridPaneBut.add(genMarqBut, 2, 2);
        gridPaneBut.add(genNumBut, 3, 2);
        gridPaneBut.add(updateTkBut, 3, 1);
        VBox editVBox = new VBox();
        editVBox.setAlignment(Pos.CENTER);
        editVBox.setSpacing(20);
        Button msgBut = new Button("Note");
        Button editBut = new Button("Editer");
        msgBut.setPrefHeight(55);
        msgBut.setPrefWidth(70);
        if (notifNote) {
            msgBut.setStyle(Main.redStyle);
        }
        editBut.setPrefHeight(55);
        editBut.setPrefWidth(70);
        editVBox.getChildren().addAll(msgBut, editBut);
        Separator sep = new Separator();
        Separator sep2 = new Separator();
        sep.setOrientation(Orientation.VERTICAL);
        sep2.setOrientation(Orientation.VERTICAL);
        if (proj.getPrio().equals("URGENCE")) {
            ImageView imgUrg = new ImageView(new Image("ISO9001/File/Urgent.png"));
            hBox.getChildren().addAll(imgUrg, gridPane, sep, gridPaneBut, sep2, editVBox);
        } else {
            hBox.getChildren().addAll(gridPane, sep, gridPaneBut, sep2, editVBox);
        }
        hBox.setHgrow(gridPane, Priority.ALWAYS);
        hBox.setHgrow(gridPaneBut, Priority.ALWAYS);
        HBox projNHbox = new HBox();
        projNHbox.setSpacing(15);
        if (proj.getPrio().equals("URGENCE")) {
            projNHbox.getChildren().addAll(nProjectLab, etiquetteBut, archiveBut, new ImageView(new Image("ISO9001/File/chrono.png")));
        } else {
            projNHbox.getChildren().addAll(nProjectLab, etiquetteBut, archiveBut);
        }
        globVBox.getChildren().addAll(projNHbox, hBox);

        enregEchanBut.setOnMouseClicked(e -> {
            Logger.logOpenFile(proj, "sample_list");
            try {
                Runtime.getRuntime().exec("CMD /C START " + proj.path + "/Echantillon/sample_list.xlsx");
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });

        genMarqBut.setOnMouseClicked(e -> {
            Excel_util.updateMarquage(proj);
            dialGenMarquage(proj);
        });

        genCoupBut.setOnMouseClicked(e -> {
            Logger.logOpenFile(proj, "Coupe");
            try {
                Excel_util.updateCoupe(proj);
                Runtime.getRuntime().exec("CMD /C START " + proj.path + "/SuiviTK/Coupe.xlsx");
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });

        genNumBut.setOnMouseClicked(e -> {
            dialGenNum(proj);
        });

        updateTkBut.setOnMouseClicked(e -> {
            Logger.logSuiviTK(proj);
            try {
                Runtime.getRuntime().exec("explorer.exe /select," + proj.path.replace("/", "\\") + "\\SuiviTK\\Marquage");
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });

        anoBut.setOnMouseClicked(e -> {
            Logger.logAno(proj);
            dialAno(proj);
        });

        editBut.setOnMouseClicked(e -> {
            Logger.logEdit(proj);
            dialProj(proj);
        });

        msgBut.setOnMouseClicked(e -> {
            Notification.deleteNoteNotification(proj, User.username);
            dialMessage(proj);
        });

        etiquetteBut.setOnMouseClicked(e -> {
            File fi = new File(proj.getPath() + "/Etiquette.jpg");
            try {
                byte[] fileContent = Files.readAllBytes(fi.toPath());
                Printer.printLabel(fileContent);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });

        archiveBut.setOnMouseClicked(e -> {
            archive(proj);
        });

        return globVBox;
    }

    private static String dialPathArchive() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Archivage");
        dialog.setHeaderText("Ou les archivés vous?");
        TextField path = new TextField();
        dialog.getDialogPane().setContent(path);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        Optional<ButtonType> option = dialog.showAndWait();

        if (option.get() == ButtonType.OK) {
            return path.getText();
        }
        return "";
    }

    private static Boolean dialArchive(Project proj) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Archivage");
        dialog.setHeaderText("Archivage Physique:");
        VBox globVBox = new VBox();
        globVBox.setSpacing(10);
        HBox checkBlocHBox = new HBox();
        checkBlocHBox.setSpacing(10);
        HBox fieldBlocHbox = new HBox();
        fieldBlocHbox.setSpacing(10);
        Label blocLab = new Label("Blocs:");
        CheckBox ouiBlocCheck = new CheckBox("Oui");
        CheckBox nonBlocCheck = new CheckBox("Non");
        CheckBox rienBlocCheck = new CheckBox("Pas de blocs");
        checkBlocHBox.getChildren().addAll(ouiBlocCheck, nonBlocCheck, rienBlocCheck);
        Label boiteBlocLab = new Label("Boite:");
        ComboBox<String> boiteBlocCombo = new ComboBox<>();
        
        Label tiroirBlocLab = new Label("Tiroir:");
        ComboBox<String> tiroirBlocCombo = new ComboBox<>();
        tiroirBlocCombo.getItems().addAll("1a", "1b", "2a", "2b", "3a", "3b", "4a", "4b");
        TextField descBlocField = new TextField();
        fieldBlocHbox.getChildren().addAll(boiteBlocLab, boiteBlocCombo, tiroirBlocLab, tiroirBlocCombo, descBlocField);
        HBox checkLamesHBox = new HBox();
        checkLamesHBox.setSpacing(10);
        HBox fieldLamesHbox = new HBox();
        fieldLamesHbox.setSpacing(10);
        Label lameLab = new Label("Lames:");
        CheckBox ouiLamesCheck = new CheckBox("Oui");
        CheckBox nonLamesCheck = new CheckBox("Non");
        CheckBox rienLamesCheck = new CheckBox("Pas de blocs");
        checkLamesHBox.getChildren().addAll(ouiLamesCheck, nonLamesCheck, rienLamesCheck);
        Label boiteLamesLab = new Label("Boite:");
        ComboBox<String> boiteLamesCombo = new ComboBox<>();
        for (int i=1; i<StockagePhysique.getNumberBoite()+1; i++) {
            boiteBlocCombo.getItems().add(String.valueOf(i));
            boiteLamesCombo.getItems().add(String.valueOf(i));
        }
        Label tiroirLamesLab = new Label("Tiroir:");
        ComboBox<String> tiroirLamesCombo = new ComboBox<>();
        tiroirLamesCombo.getItems().addAll("1a", "1b", "2a", "2b", "3a", "3b", "4a", "4b");
        TextField descLamesField = new TextField();
        fieldLamesHbox.getChildren().addAll(boiteLamesLab, boiteLamesCombo, tiroirLamesLab, tiroirLamesCombo, descLamesField);
        globVBox.getChildren().addAll(blocLab, checkBlocHBox, fieldBlocHbox, lameLab, checkLamesHBox, fieldLamesHbox);
        boiteBlocLab.setVisible(false);
        boiteBlocCombo.setVisible(false);
        tiroirBlocLab.setVisible(false);
        tiroirBlocCombo.setVisible(false);
        descBlocField.setVisible(false);
        boiteLamesLab.setVisible(false);
        boiteLamesCombo.setVisible(false);
        tiroirLamesLab.setVisible(false);
        tiroirLamesCombo.setVisible(false);
        descLamesField.setVisible(false);
        dialog.getDialogPane().setContent(globVBox);
        
        ouiBlocCheck.setOnAction(e -> {
            if (nonBlocCheck.isSelected()) {
                nonBlocCheck.setSelected(false);
            } if (rienBlocCheck.isSelected()) {
                rienBlocCheck.setSelected(false);
            }
            if (ouiBlocCheck.isSelected()) {
                fieldBlocHbox.getChildren().clear();
                fieldBlocHbox.getChildren().addAll(boiteBlocLab, boiteBlocCombo, tiroirBlocLab, tiroirBlocCombo);
                boiteBlocLab.setVisible(true);
                boiteBlocCombo.setVisible(true);
                tiroirBlocLab.setVisible(true);
                tiroirBlocCombo.setVisible(true);
                descBlocField.setVisible(false);
            }
            else {
                boiteBlocLab.setVisible(false);
                boiteBlocCombo.setVisible(false);
                tiroirBlocLab.setVisible(false);
                tiroirBlocCombo.setVisible(false);
                descBlocField.setVisible(false);
            }
        });
        nonBlocCheck.setOnAction(e -> {
            if (ouiBlocCheck.isSelected()) {
                ouiBlocCheck.setSelected(false);
            } if (rienBlocCheck.isSelected()) {
                rienBlocCheck.setSelected(false);
            }
            if (nonBlocCheck.isSelected()) {
                fieldBlocHbox.getChildren().clear();
                fieldBlocHbox.getChildren().add(descBlocField);
                boiteBlocLab.setVisible(false);
                boiteBlocCombo.setVisible(false);
                tiroirBlocLab.setVisible(false);
                tiroirBlocCombo.setVisible(false);
                descBlocField.setVisible(true);
            } else {
                boiteBlocLab.setVisible(false);
                boiteBlocCombo.setVisible(false);
                tiroirBlocLab.setVisible(false);
                tiroirBlocCombo.setVisible(false);
                descBlocField.setVisible(false);
            }
        });
        rienBlocCheck.setOnAction(e -> {
            if (ouiBlocCheck.isSelected()) {
                ouiBlocCheck.setSelected(false);
            } if (nonBlocCheck.isSelected()) {
                nonBlocCheck.setSelected(false);
            }
            if (rienBlocCheck.isSelected()) {
                boiteBlocLab.setVisible(false);
                boiteBlocCombo.setVisible(false);
                tiroirBlocLab.setVisible(false);
                tiroirBlocCombo.setVisible(false);
                descBlocField.setVisible(false);
            } else {
                boiteBlocLab.setVisible(false);
                boiteBlocCombo.setVisible(false);
                tiroirBlocLab.setVisible(false);
                tiroirBlocCombo.setVisible(false);
                descBlocField.setVisible(false);
            }
        });

        ouiLamesCheck.setOnAction(e -> {
            if (nonLamesCheck.isSelected()) {
                nonLamesCheck.setSelected(false);
            } if (rienLamesCheck.isSelected()) {
                rienLamesCheck.setSelected(false);
            }
            if (ouiLamesCheck.isSelected()) {
                fieldLamesHbox.getChildren().clear();
                fieldLamesHbox.getChildren().addAll(boiteLamesLab, boiteLamesCombo, tiroirLamesLab, tiroirLamesCombo);
                boiteLamesLab.setVisible(true);
                boiteLamesCombo.setVisible(true);
                tiroirLamesLab.setVisible(true);
                tiroirLamesCombo.setVisible(true);
                descLamesField.setVisible(false);
            } else {
                boiteLamesLab.setVisible(false);
                boiteLamesCombo.setVisible(false);
                tiroirLamesLab.setVisible(false);
                tiroirLamesCombo.setVisible(false);
                descLamesField.setVisible(false);
            }
        });
        nonLamesCheck.setOnAction(e -> {
            if (ouiLamesCheck.isSelected()) {
                ouiLamesCheck.setSelected(false);
            } if (rienLamesCheck.isSelected()) {
                rienLamesCheck.setSelected(false);
            } if (nonLamesCheck.isSelected()) {
                fieldLamesHbox.getChildren().clear();
                fieldLamesHbox.getChildren().add(descLamesField);
                boiteLamesLab.setVisible(false);
                boiteLamesCombo.setVisible(false);
                tiroirLamesLab.setVisible(false);
                tiroirLamesCombo.setVisible(false);
                descLamesField.setVisible(true);
            } else {
                boiteLamesLab.setVisible(false);
                boiteLamesCombo.setVisible(false);
                tiroirLamesLab.setVisible(false);
                tiroirLamesCombo.setVisible(false);
                descLamesField.setVisible(false);
            }
        });
        rienLamesCheck.setOnAction(e -> {
            if (ouiLamesCheck.isSelected()) {
                ouiLamesCheck.setSelected(false);
            } if (nonLamesCheck.isSelected()) {
                nonLamesCheck.setSelected(false);
            } if (rienLamesCheck.isSelected()) {
                boiteLamesLab.setVisible(false);
                boiteLamesCombo.setVisible(false);
                tiroirLamesLab.setVisible(false);
                tiroirLamesCombo.setVisible(false);
                descLamesField.setVisible(false);
            } else {
                boiteLamesLab.setVisible(false);
                boiteLamesCombo.setVisible(false);
                tiroirLamesLab.setVisible(false);
                tiroirLamesCombo.setVisible(false);
                descLamesField.setVisible(false);
            }
        });
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> option = dialog.showAndWait();

        if (option.get() == ButtonType.CANCEL) {
            dialog.close();
            return false;
        } else {
            if (rienBlocCheck.isSelected() && rienLamesCheck.isSelected()) {
                System.out.println("1");
                return true;
            }
            if (ouiBlocCheck.isSelected()) {
                System.out.println("2");
                new PhysicalArchive(proj, boiteBlocCombo.getValue(), tiroirBlocCombo.getValue(), "Blocs");
            } 
            if (ouiLamesCheck.isSelected()) {
                System.out.println("3");
                new PhysicalArchive(proj, boiteLamesCombo.getValue(), tiroirLamesCombo.getValue(), "Lames");
            }
            if (nonBlocCheck.isSelected()) {
                System.out.println("4");
                new PhysicalArchive(proj, descBlocField.getText(), "Blocs");
            } if (nonLamesCheck.isSelected()) {
                System.out.println("5");
                new PhysicalArchive(proj, descLamesField.getText(), "Lames");
            }
            return true;
        }
    }

    private static List<String> dialReport(Project proj) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Rapport");
        Label labReport = new Label("Rapport:");
        ToggleGroup reportGroup = new ToggleGroup();
        RadioButton ouiRad = new RadioButton("Oui");
        RadioButton nonRad = new RadioButton("Non");
        ouiRad.setToggleGroup(reportGroup);
        nonRad.setToggleGroup(reportGroup);
        HBox reportHBox = new HBox();
        reportHBox.setSpacing(10);
        reportHBox.getChildren().addAll(labReport, ouiRad, nonRad);
        DatePicker datePicker = new DatePicker();
        datePicker.setVisible(false);
        VBox reportVBox = new VBox();
        reportVBox.setSpacing(10);
        reportVBox.getChildren().addAll(reportHBox, datePicker);

        ouiRad.setOnAction(e -> {
            if (ouiRad.isSelected()) {
                datePicker.setVisible(true);
            }
        });
         nonRad.setOnAction(e -> {
             if (nonRad.isSelected()) {
                 datePicker.setVisible(false);
             }
         });

         dialog.getDialogPane().setContent(reportVBox);
         dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

         Optional<ButtonType> option = dialog.showAndWait();

         if (option.get() == ButtonType.OK) {
             if (ouiRad.isSelected()) {
                 List<String> res = new ArrayList<>();
                 res.add("1");
                 res.add(datePicker.getValue().toString());
                 return res;
             } else {
                 List<String> res = new ArrayList<>();
                 res.add("1");
                 res.add("null");
                 return res;
             }
         } else {
             List<String> res = new ArrayList<>();
             res.add("0");
             res.add("null");
             return res;
         }

    }

    private static void archive(Project proj) {
        String physicArchive = "";
        Boolean res = dialArchive(proj);
        List<String> res2 = dialReport(proj);
        if (!res || res2.get(0).equals("0")) {
            return;
        }
        PhysicalArchiveLoader.load();
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
                updateMessage("Traitement des utilisateurs...");
                updateProgress(10, 100);
                for (Associe a: proj.listAssocie) {
                    try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter(proj.getPath() + "/Info.txt", true));
                        writer.write("DateArchive#!#" + java.time.LocalDate.now().toString());
                        writer.close();
                        BufferedReader reader = new BufferedReader(new FileReader(Params.basePath + "/Users/" + a.getUsername() + "/Project.txt"));
                        List<String> projList = new ArrayList<>();
                        String line;
                        while((line=reader.readLine()) != null) {
                            projList.add(line);
                        }
                        reader.close();
                        BufferedWriter projWriter = new BufferedWriter(new FileWriter(Params.basePath + "/Users/" + a.getUsername() + "/Project.txt"));
                        BufferedWriter archiWriter = new BufferedWriter(new FileWriter(Params.basePath + "/Users/" + a.getUsername() + "/Archive.txt", true));
                        for (String l: projList) {
                            if (l.contains("P." + proj.getNumber())) {
                                archiWriter.write(l.replace("Project", "Archivage") + "\n");
                            } else {
                                projWriter.write(l + "\n");
                            }
                        }
                        projWriter.close();
                        archiWriter.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                updateMessage("Traitement Projet...");
                updateProgress(20, 100);
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(Params.basePath + "/Users/" + proj.owner.getUser() + "/Project.txt"));
                    BufferedWriter writer = new BufferedWriter(new FileWriter(Params.basePath + "/Users/" + proj.owner.getUser() + "/Project.txt"));
                    BufferedWriter archiWriter = new BufferedWriter(new FileWriter(Params.basePath + "/Users/" + proj.owner.getUser() + "/Archive.txt"));
                    String line;
                    while((line=reader.readLine()) != null) {
                        if (line.contains("P." + proj.getNumber())) {
                            archiWriter.write(Params.archivePath + "/P." + proj.getNumber());
                        } else {
                            writer.write(line);
                        }
                    }
                    reader.close();
                    writer.close();
                    archiWriter.close();
                } catch (Exception e) {}
                new File(Params.basePath + "/Archivage/P." + proj.getNumber()).mkdir();
                List<String> slides = Excel_util.getAllSlide(proj);
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(Params.basePath + "/Archivage/P." + proj.getNumber() + "/Slides.txt"));
                    for (String line: slides) {
                        writer.write(line + "\n");
                    }
                    writer.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (!res2.get(1).equals("null")) {
                    new File(Params.basePath + "/pendingReport/P." + proj.getNproject()).mkdir();
                    try {
                    Files.copy(Paths.get(proj.getPath() + "/Info.txt"), Paths.get(Params.basePath + "/pendingReport/P." + proj.getNproject() + "/Info.txt"), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter(Params.basePath + "/pendingReport/P." + proj.getNproject() + "/dateReport.txt"));
                        writer.write("Init > " + res2.get(1) + "\n");
                        writer.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                updateMessage("Copie du projet...");
                updateProgress(35, 100);
                Administration.copyRecusrivelyTxt(proj.getPath(), Params.basePath + "/Archivage/P." + proj.getNumber());
                new File(Params.archivePath + "/P." + proj.getNumber()).mkdirs();
                Administration.copyRecusrively(proj.getPath(), Params.archivePath + "/P." + proj.getNumber());
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
    }

    private static void dialMessage(Project proj) {
        ObservableList<String> msg = FXCollections.observableArrayList();
        try {
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(proj.path + "/Echange.txt"));
            while((line=reader.readLine()) != null) {
                msg.add(line);
            }
            reader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Note");
        VBox globVBox = new VBox();
        globVBox.setSpacing(20);
        ListView<String> msgList = new ListView<>();
        msgList.setPrefSize(500, 350);
        msgList.setItems(msg);
        HBox inputHBox = new HBox();
        TextField inputField = new TextField();
        inputField.setPrefWidth(450);
        inputField.setPrefHeight(100);
        inputField.setAlignment(Pos.TOP_LEFT);
        inputHBox.setSpacing(20);
        Button sendBut = new Button("Envoyer");
        sendBut.setPrefHeight(100);
        inputHBox.getChildren().addAll(inputField, sendBut);
        globVBox.getChildren().addAll(msgList, inputHBox);
        dialog.getDialogPane().setContent(globVBox);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        
        sendBut.setOnAction(e -> {
            Logger.logNote(proj);
            String text = inputField.getText();
            Notification.addNoteNotification(proj, User.username);
            if (!text.equals("")) {
                String date = java.time.LocalDate.now().toString();
                LocalTime time = java.time.LocalTime.now();
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(proj.path + "/Echange.txt", true));
                    writer.write("[" + User.nom + " " + User.prenom + "] le " + date + " à " + time.getHour() + ":" + time.getMinute() +" :\t" + text + "\n");
                    writer.close();
                } catch (IOException r) {
                    r.printStackTrace();
                }
                msg.add("[" + User.nom + " " + User.prenom + "] le " + date + " à " + time.getHour() + ":" + time.getMinute() + " :\t" + text);
                inputField.setText("");
            }
        });

        inputField.setOnKeyPressed(e -> {
            if (e.getCode().toString().equals("ENTER")) {
                sendBut.fire();
            }
        });

        dialog.showAndWait();
    }

    private static List<Boolean> getProcededMarquage(Project proj) {
        File[] listFile = new File(proj.getPath() + "/SuiviTK/Marquage").listFiles();
        List<String> doneMarquageList = new ArrayList<>();
        List<Boolean> rtrn = new ArrayList<>();
        for (File f: listFile) {
            if (f.getName().contains("pdf")) {
                doneMarquageList.add(f.getName().split("_")[2]);
            }
        }
        for (Technique m: proj.listMarquage) {
            if (doneMarquageList.contains(m.getNum())) {
                rtrn.add(true);
            } else {
                rtrn.add(false);
            }
        }
        return rtrn;
    }

    public static int getNumberProcededMarquage(Project proj) {
        File[] lis = new File(proj.getPath() + "/SuiviTK/Marquage/").listFiles();
        int counter = 0;
        for (File f: lis) {
            if (f.getName().contains("pdf")) {
                counter++;
            }
        }
        return counter;
    }

    public static HashMap<String, Boolean> getProcededNumerisation(Project proj) {
        File[] listScan = new File(proj.getPath() + "/Scan/").listFiles();
        HashMap<String, Boolean> rtrn = new HashMap<>();
        for (int i=0; i<proj.listMarquage.size(); i++) {
            for (File scan: listScan) {
                if (scan.getName().contains("txt")) {
                    continue;
                }
                if (scan.getName().split("_")[1].equals(proj.listMarquage.get(i).getNum())) {
                    if (scan.listFiles().length >= Integer.parseInt(proj.getEchantillon())-1) {
                        rtrn.put(scan.getName().split("_")[1],true);
                    } else {
                        rtrn.put(scan.getName().split("_")[1],false);
                    }
                    break;
                } else if (scan.getName().equals(proj.listMarquage.get(i).getName())) {
                    if (scan.listFiles().length >= Integer.parseInt(proj.getEchantillon())-1) {
                        rtrn.put(scan.getName().split("_")[1],true);
                    } else {
                        rtrn.put(scan.getName().split("_")[1],false);
                    }
                    break;
                }
            }
        }
        return rtrn;
    }

    private static int getNumberProcededNumerisation(Project proj) {
        int c = 0;
        HashMap<String, Boolean> lst = getProcededNumerisation(proj);
        for (Boolean i: lst.values()) {
            if (i) {
                c++;
            }
        }
        return c;
    }

    private static int getNumberNumerisation(Project proj) {
        int c = 0;
        for (String g: proj.listGrossisement) {
            if (!g.equals("NA")) {
                c++;
            }
        }
        return c;
    }

    private static void dialGenMarquage(Project proj) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setWidth(200);
        ListView<String> listView = new ListView<>();
        List<Boolean> done = getProcededMarquage(proj);
        for(int i=0; i<proj.listMarquage.size(); i++) {
            if (proj.listMarquage.get(i) instanceof Marquage) {
                Marquage mr = (Marquage)proj.listMarquage.get(i);
                if (done.get(i)) {
                        listView.getItems().add(mr.getName() + "_" + mr.getNum() + "_" + proj.espece + "_" + mr.getFourniseur() + "_" + mr.getReference() + " \u2713");
                } else {
                        listView.getItems().add(mr.getName() + "_" + mr.getNum() + "_" + proj.espece + "_" + mr.getFourniseur() + "_" + mr.getReference());
                }
            } else {
                Multiplex mr = (Multiplex)proj.listMarquage.get(i);
                if (done.get(i)) {
                    listView.getItems().add(mr.getFullName() + " \u2713");
                } else {
                    listView.getItems().add(mr.getFullName());
                }
            }
        }
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
        dialog.getDialogPane().setContent(listView);
        Optional<ButtonType> option = dialog.showAndWait();

        if(option.get() == ButtonType.OK) {
            ObservableList<String> item = listView.getSelectionModel().getSelectedItems();
            Logger.logOpenFile(proj, item.get(0).toString());
            try {
                System.out.println(proj.path + "/SuiviTK/Marquage/Marquage_" + item.get(0).toString() + ".xlsx");
                Runtime.getRuntime().exec("CMD /C START " + proj.path + "/SuiviTK/Marquage/Marquage_" + item.get(0).toString().replace(" \u2713", "") + ".xlsx");
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    private static void dialGenNum(Project proj) {
        Dialog<ButtonType> dialog = new Dialog<>();
        ListView<String> listView = new ListView<>();
        File[] listNum = new File(proj.getPath() + "/SuiviTK/Numerisation/").listFiles();
        HashMap<String, Boolean> lst = getProcededNumerisation(proj);
        for(File f: listNum) {
            if(f.getName().toString().substring(f.getName().toString().lastIndexOf(".")).equals(".xlsx")) {
                System.out.println(f);
                try {
                    if (lst.get(f.getName().split("_")[2])) {
                        listView.getItems().add(f.getName() + " \u2713");
                    } else {
                        listView.getItems().add(f.getName());
                    }
                } catch (Exception e) {
                    listView.getItems().add(f.getName());
                }
            }
        }
        ButtonType csv = new ButtonType("CSV");
        ButtonType view = new ButtonType("View/Edit");
        listView.setOnMouseClicked(e -> {
            if (new File(proj.path + "/SuiviTK/Numerisation/" + listView.getSelectionModel().getSelectedItem().toString().replace("xlsx", "csv").replace(" \u7213", "")).exists()) {
                dialog.getDialogPane().getButtonTypes().clear();
                dialog.getDialogPane().getButtonTypes().addAll(csv, view);
            } else {
                dialog.getDialogPane().getButtonTypes().clear();
                dialog.getDialogPane().getButtonTypes().add(view);
            }
        });
        dialog.getDialogPane().setContent(listView);
        Optional<ButtonType> option = dialog.showAndWait();
        if(option.get() == view) {
            Excel_util.updateNum(proj);
            ObservableList<String> item = listView.getSelectionModel().getSelectedItems();
            Logger.logOpenFile(proj, item.get(0).toString());
            try {
                Runtime.getRuntime().exec("CMD /C START " + proj.path + "/SuiviTK/Numerisation/" + item.get(0).toString().replace(" \u7213", ""));
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            Excel_util.csv(proj.path + "/SuiviTK/Numerisation/" + item.get(0).toString().replace(" \u7213", ""));
        } else if (option.get() == csv) {
            ObservableList<String> item = listView.getSelectionModel().getSelectedItems();
            Excel_util.csv(proj.path + "/SuiviTK/Numerisation/" + item.get(0).toString().replace(" \u7213", ""));
            try {
                System.out.println(proj.path + "/SuiviTK/Numerisation/" + item.get(0).toString());
                Runtime.getRuntime().exec("explorer.exe /select," + (proj.path + "/SuiviTK/Numerisation/" + item.get(0).toString()).replace("/", "\\").replace("xlsx", "csv").replace(" \u7213", ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void dialAno(Project proj) {
        List<String> echantillonList;
        try {
            FileInputStream inputStream = new FileInputStream(new File(proj.path + "/Echantillon/sample_list.xlsx"));
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
            int max_line = sheet.getLastRowNum();
            echantillonList = new ArrayList<>();
            for(int i=9; i<max_line + 1; i++) {
                echantillonList.add(sheet.getRow(i).getCell(0).getStringCellValue());
            }
            inputStream.close();
            workbook.close();
        } catch (Exception e) {
            echantillonList = new ArrayList<>();
            e.printStackTrace();
        }
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("L'Anonymisation est irreversible, les echantillons ne pourront plus être éditer.");
        alert.setContentText("Êtes vous sûre de votre choix?");
                
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            anoSlide(echantillonList, proj);
            reloadBrowser(User.projList);
        }
    }

    private static void anoSlide(List<String> anoList, Project proj) {
        Collections.shuffle(anoList);
        try {
            FileInputStream inputStream = new FileInputStream(new File(proj.path + "/Echantillon/Pivot.xlsx"));
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
            int i = sheet.getLastRowNum() + 1;
            for(String ech: anoList) {
                sheet.createRow(i).createCell(0).setCellValue(ech);
                sheet.getRow(i).createCell(1).setCellValue(String.valueOf(i));
                i++;
            }
            inputStream.close();
            FileOutputStream outputStream = new FileOutputStream(proj.path + "/Echantillon/Pivot.xlsx");
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dialProj(Project proj) {
        List<Technique> tempMrq = new ArrayList<>(proj.listMarquage);
        List<String> tempGross = new ArrayList<>(proj.listGrossisement);
        Collections.copy(tempMrq, proj.listMarquage);
        Collections.copy(tempGross, proj.listGrossisement);
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit P." + proj.nProject);
        BorderPane borderPane = new BorderPane();
        Insets insets = new Insets(10);
        VBox labVBox = new VBox();
        labVBox.setSpacing(115);
        VBox globVbox = new VBox();
        Label nameLab = new Label("Titre:");
        Label descLab = new Label("Description:");
        VBox nameVBox = new VBox();
        nameVBox.setSpacing(30);
        nameVBox.getChildren().addAll(nameLab, descLab);
        TextField nameField = new TextField(proj.getTitle());
        TextArea descField = new TextArea(proj.getDescription());
        //descField.setAlignment(Pos.TOP_LEFT);
        descField.setPrefSize(350, 105);
        VBox butVBox = new VBox();
        butVBox.setSpacing(75);
        globVbox.setSpacing(20);
        globVbox.setAlignment(Pos.CENTER);
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
        tableAssocie.setItems(Enregistrement.getAssocieTable(proj.listAssocie));
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
        Label marquageLab = new Label("Marquage:");
        Button addMarq = new Button("Ajouter");
        Button delMarq = new Button("Retirer");
        addMarq.setPrefHeight(50);
        addMarq.setPrefWidth(75);
        delMarq.setPrefHeight(50);
        delMarq.setPrefWidth(75);
        VBox marqButVBox = new VBox();
        marqButVBox.getChildren().addAll(addMarq, delMarq);
        marqButVBox.setSpacing(20);
        TableView<Technique> marquageTable = new TableView<>();
        marquageTable.setPrefWidth(300);
        marquageTable.setPrefHeight(200);
        TableColumn<Technique, String> marquageCol = new TableColumn<>("Marquage");
        TableColumn<Technique, String> techniqueCol = new TableColumn<>("Révélation");
        TableColumn<Technique, String> grooCol = new TableColumn<>("Grossisement");
        marquageCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        techniqueCol.setCellValueFactory(new PropertyValueFactory<>("technique"));
        grooCol.setCellValueFactory(new PropertyValueFactory<>("grossisement"));
        marquageCol.setMinWidth(150);
        techniqueCol.setMinWidth(150);
        grooCol.setMinWidth(150);
        Label coupeLab = new Label("Nombre de coupe:");
        TextField coupeField = new TextField(String.valueOf(proj.nombreCoupe));
        coupeField.setMaxWidth(35);
        HBox inputHbox = new HBox();
        Separator sep = new Separator();
        sep.setOrientation(Orientation.VERTICAL);
        inputHbox.setSpacing(50);
        HBox coupeHBox = new HBox();
        coupeHBox.setSpacing(10);
        Label echanLab = new Label("Nombre d'échantillon:");
        TextField echanField = new TextField(String.valueOf(proj.echantillon));
        echanField.setMaxWidth(35);
        HBox echanHbox = new HBox();
        echanHbox.getChildren().addAll(echanLab, echanField);
        echanHbox.setSpacing(10);
        coupeHBox.getChildren().addAll(coupeLab, coupeField);
        inputHbox.getChildren().addAll(coupeHBox, sep, echanHbox);
        inputHbox.translateXProperty().set(25);
        HBox dateUrgenceHBox = new HBox();
        HBox dateHBox = new HBox();
        dateHBox.setSpacing(20);
        HBox urgenceHBox = new HBox();
        urgenceHBox.setSpacing(20);
        Label dateFinProjLab = new Label("Date de fin de projet:");
        Label nivUrgLab = new Label("Niveau d'urgence:");
        DatePicker datePick = new DatePicker();
        datePick.setValue(LocalDate.parse(proj.getDateFin()));
        ToggleGroup urgGroup = new ToggleGroup();
        RadioButton noramlRad = new RadioButton("NORMAL");
        RadioButton urgRad = new RadioButton("URGENCE");
        if (proj.getPrio().equals("URGENCE")) {
            urgRad.setSelected(true);
        } else {
            noramlRad.setSelected(true);
        }
        noramlRad.setToggleGroup(urgGroup);
        urgRad.setToggleGroup(urgGroup);
        dateUrgenceHBox.setSpacing(50);
        dateHBox.getChildren().addAll(dateFinProjLab, datePick);
        urgenceHBox.getChildren().addAll(nivUrgLab, noramlRad, urgRad);
        dateUrgenceHBox.getChildren().addAll(dateHBox, urgenceHBox);
        Separator urgSep = new Separator();
        marquageTable.getColumns().addAll(marquageCol, techniqueCol, grooCol);
        marquageTable.setItems(Enregistrement.getMarquageTable(getMarquageEnrg(proj)));
        VBox labelVBox = new VBox();
        labelVBox.setSpacing(150);
        labelVBox.getChildren().addAll(associeLab, marquageLab);
        labVBox.getChildren().addAll(nameVBox, labelVBox);
        //labVBox.setPadding(new Insets(170, 0, 0, 0));
        butVBox.setPadding(new Insets(170, 0, 0, 0));
        borderPane.setLeft(labVBox);
        borderPane.setMargin(labVBox, insets);
        globVbox.getChildren().addAll(nameField, descField, tableAssocie, marquageTable, inputHbox, dateUrgenceHBox);
        borderPane.setCenter(globVbox);
        borderPane.setMargin(globVbox, insets);
        butVBox.getChildren().addAll(buttonAssocieVBox, marqButVBox);
        borderPane.setRight(butVBox);
        borderPane.setMargin(butVBox, insets);
        dialog.getDialogPane().setContent(borderPane);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        addAssocie.setOnMouseClicked(e -> {
            Associe newAs = dialogAddAssocie.dialChoose();
            proj.listAssocie.add(newAs);
            ObservableList<Associe> obsList = Enregistrement.getAssocieTable(proj.listAssocie);
            tableAssocie.setItems(obsList);
        });

        delAssocie.setOnMouseClicked(e -> {
            Associe selected = tableAssocie.getSelectionModel().getSelectedItems().get(0);
            proj.listAssocie.remove(selected);
            tableAssocie.getItems().remove(selected);
        });

        addMarq.setOnMouseClicked(e -> {
            Technique newMrq = dialogMarquage.launcher();
            proj.listMarquage.add(newMrq);
            proj.listGrossisement.add(newMrq.getGrossisement());
            proj.scan = true;
            proj.listBleech.add(newMrq.getBleeched());
            proj.listRev.add(newMrq.getRev());
            ObservableList<Technique> table = Enregistrement.getMarquageTable(getMarquageEnrg(proj));
            marquageTable.setItems(table);
        });
        
        delMarq.setOnMouseClicked(e -> {
            Technique selected = marquageTable.getSelectionModel().getSelectedItem();
            for(int i=0; i<proj.listMarquage.size(); i++) {
                if(selected == proj.listMarquage.get(i)) {
                    proj.listMarquage.remove(i);
                    proj.listGrossisement.remove(i);
                    break;
                }
            }
            marquageTable.getItems().removeAll(marquageTable.getSelectionModel().getSelectedItems());
        });

        Optional<ButtonType> option = dialog.showAndWait();

        if (option.get() == ButtonType.OK) {
            if (!datePick.getValue().toString().equals(proj.getDateFin())) {
                Dialog<ButtonType> dial = new Dialog<>();
                dial.setTitle("Justification");
                dial.setHeaderText("Justification de changement de date:");
                TextField justiField = new TextField();
                dial.getDialogPane().setContent(justiField);
                dial.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
                Optional<ButtonType> op = dial.showAndWait();

                if (op.get() == ButtonType.OK) {
                    try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter(proj.getPath() + "/justifDate.txt", true));
                        writer.write(proj.getDateFin() + " > " + datePick.getValue().toString() + " : " + justiField.getText().toString() + "\n");
                        writer.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            int nbCoupe = Integer.parseInt(coupeField.getText());
            int nbEchan = Integer.parseInt(echanField.getText());
            File[] listMarquageFile = new File(proj.getPath() + "/SuiviTK/Marquage").listFiles();
            for (File f: listMarquageFile) {
                try {
                    if (!f.getName().contains("pdf")) {
                        Files.delete(f.toPath());
                    }
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
            File[] listNumFile = new File(proj.getPath() + "/SuiviTK/Numerisation/").listFiles();
            for (File f: listNumFile) {
                try {
                    if (!f.getName().contains("pdf")) {
                        Files.delete(f.toPath());
                    }
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
            proj.nombreCoupe = nbCoupe;
            proj.echantillon = nbEchan;
            proj.setTitle(nameField.getText());
            proj.dateFin = datePick.getValue().toString();
            RadioButton radSelected = (RadioButton) urgGroup.getSelectedToggle();
            proj.prio = radSelected.getText();
            proj.setDescription(descField.getText());
            proj.save(Params.basePath + "/Project/");
            Excel_util.updateMarquage(proj);
            Excel_util.updateNum(proj);
            reloadBrowser(User.projList);
        }

        if (option.get() == ButtonType.CANCEL) {
            proj.listGrossisement = tempGross;
            proj.listMarquage.clear();
            for (Technique t: tempMrq) {
                try {
                    proj.listMarquage.add((Marquage)t);
                } catch (Exception e) {
                    proj.listMarquage.add((Multiplex)t);
                }
            }
        }
    }

    private static List<Technique> getMarquageEnrg(Project proj) {
        List<Technique> listRes = new ArrayList<>();
        for(int i=0; i< proj.listMarquage.size(); i++) {
         listRes.add(proj.listMarquage.get(i));
        }
        return listRes;
    }

    private static Associe chooseReferent(Project proj) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Input Dialog");
        dialog.setHeaderText("Choisir un referent pour le projet:");
        ComboBox comboBox = new ComboBox<>();
        ObservableList<String> obsList = FXCollections.observableArrayList();
        for(Associe a: proj.listAssocie) {
            obsList.add(a.getName() + " " + a.getPrenom());
        }
        comboBox.setItems(obsList);
        dialog.getDialogPane().setContent(comboBox);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().setMinHeight(135);
        Optional<ButtonType> option = dialog.showAndWait();
        int idx = (Integer) null;
        if (option.get() == ButtonType.OK) {
            idx = comboBox.getSelectionModel().getSelectedIndex();
        }
        if (idx != (Integer) null) {
            return proj.listAssocie.get(idx);
        }
        return null;
    }

    public static void setNoteColor(String proj) {
    }

    public static int getNumberMarquage(Project proj) {
        File[] lis = new File(proj.getPath() + "/SuiviTK/Marquage/").listFiles();
        int count = 0;
        for (File f: lis) {
            if (!f.toString().contains(".pdf")) {
                count += Excel_util.getNbMarquage(f);
            }
        }
        return count;
    }
}
