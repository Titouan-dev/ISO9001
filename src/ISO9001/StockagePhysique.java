package ISO9001;

import javafx.scene.paint.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Optional;

import javafx.scene.paint.*;

import org.apache.batik.ext.awt.g2d.GraphicContext;
import org.apache.xmlbeans.impl.tool.Extension.Param;
import org.openxmlformats.schemas.drawingml.x2006.diagram.STHierBranchStyle;

import com.itextpdf.awt.geom.Point;
import com.itextpdf.text.pdf.parser.RegionTextRenderFilter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class StockagePhysique {

    public static TableView<PhysicalArchive> table;

    private static ComboBox<String> comboBoite;

    private static ComboBox<String> searchTypeCombo;

    private static Label nothingFoundLab;

    public static Tiroir tiroir1;
    public static Tiroir tiroir2;
    public static Tiroir tiroir3;
    public static Tiroir tiroir4;

    public static VBox build() {
        VBox globVBox = new VBox();
        globVBox.setPadding(new Insets(50));
        globVBox.setSpacing(20);
        comboBoite = new ComboBox<>();
        HBox boiteHBox = new HBox();
        boiteHBox.setSpacing(20);
        comboBoite.setValue("Boite 1");
        for (int i=1; i<getNumberBoite()+1; i++) {
            comboBoite.getItems().add("Boite " + i);
        }
        comboBoite.setMinWidth(300);
        Button plusBoiteBtn = new Button("+");
        if (User.grade.equals("Administrateur")) {
            boiteHBox.getChildren().addAll(comboBoite, plusBoiteBtn);
        } else {
            boiteHBox.getChildren().addAll(comboBoite);
        }
        Pane pane = new Pane();
        pane.setPrefSize(800, 900);
        tiroir1 = new Tiroir("1");
        tiroir2 = new Tiroir("2");
        tiroir3 = new Tiroir("3");
        tiroir4 = new Tiroir("4");
        tiroir1.pane.setLayoutX(110);
        tiroir1.pane.setLayoutY(0);
        tiroir2.pane.setLayoutX(285);
        tiroir2.pane.setLayoutY(0);
        tiroir3.pane.setLayoutX(460);
        tiroir3.pane.setLayoutY(0);
        tiroir4.pane.setLayoutX(635);
        tiroir4.pane.setLayoutY(0);
        pane.getChildren().addAll(tiroir1.pane, tiroir2.pane, tiroir3.pane, tiroir4.pane);
        HBox searchHBox = new HBox();
        searchHBox.setPadding(new Insets(30));
        searchHBox.setSpacing(20);
        ComboBox<String> searchCombo = new ComboBox<>();
        searchCombo.setValue("Lames");
        searchCombo.getItems().addAll("Lames", "Blocs", "Autre");
        TextField searchField = new TextField();
        searchField.setMinWidth(450);
        Label searchLab = new Label("Recherche:");
        searchTypeCombo = new ComboBox<>();
        searchTypeCombo.getItems().addAll("Project", "Echantillon", "Titre");
        searchTypeCombo.setValue("Project");
        searchHBox.getChildren().addAll(searchLab, searchTypeCombo, searchField, searchCombo);
        table = new TableView<>();
        table.setMaxWidth(500);
        table.translateXProperty().set(200);
        table.translateYProperty().set(0);
        table.setMinHeight(200);
        TableColumn<PhysicalArchive, String> type = new TableColumn<>("Type");
        TableColumn<PhysicalArchive, String> titre = new TableColumn<>("Titre");
        TableColumn<PhysicalArchive, String> project = new TableColumn<>("Project");
        TableColumn<PhysicalArchive, Button> editBut = new TableColumn<>("Modifier");
        TableColumn<PhysicalArchive, Button> deleteBut = new TableColumn<>("Supprimer");
        type.setMinWidth(100);
        project.setMinWidth(50);
        project.setMaxWidth(50);
        editBut.setMinWidth(100);
        deleteBut.setMinWidth(100);
        titre.setMinWidth(150);
        type.setCellValueFactory(new PropertyValueFactory("type"));
        project.setCellValueFactory(new PropertyValueFactory("project"));
        editBut.setCellValueFactory(new PropertyValueFactory<>("edit"));
        deleteBut.setCellValueFactory(new PropertyValueFactory<>("delete"));
        titre.setCellValueFactory(new PropertyValueFactory<>("name"));
        table.getColumns().addAll(project, titre, type, editBut, deleteBut);
        nothingFoundLab = new Label("La recherche n'a pas donné de résultat");
        nothingFoundLab.setTextFill(Color.RED);
        nothingFoundLab.setVisible(false);
        nothingFoundLab.setTranslateX(350);
        Button addBut = new Button("+");
        addBut.setTranslateX(250);
        addBut.setPrefWidth(400);
        globVBox.getChildren().addAll(boiteHBox, searchHBox, nothingFoundLab, pane, table, addBut);

        addBut.setOnAction(e -> {
            List<Tiroir> listTiroir = new ArrayList<>();
            listTiroir.add(tiroir1);
            listTiroir.add(tiroir2);
            listTiroir.add(tiroir3);
            listTiroir.add(tiroir4);
            String activeTiroir = "";
            for(int i=0; i<listTiroir.size(); i++) {
                for (int t=0; t<2; t++) {
                    if (t==0) {
                        if (listTiroir.get(i).texta.getFill() == Color.RED) {
                            activeTiroir = String.valueOf(i+1) + "a";
                            break;
                        }
                    } else {
                        if (listTiroir.get(i).textb.getFill() == Color.RED) {
                            activeTiroir = String.valueOf(i+1) + "b";
                            break;
                        }
                    }
                }
            }
            dialAddStock(comboBoite.getValue().split(" ")[1], activeTiroir);
        });

        searchField.setOnKeyPressed(e -> {
            if (e.getCode().toString().equals("ENTER")) {
                String search = searchField.getText();
                String searchType = searchTypeCombo.getValue();
                if (searchType.equals("Project")) {
                    try {
                        String path = searchEngine.searchArchiProject(search).toString();
                        String res = getTiroirAndBoite(path, searchCombo.getValue());
                        String tiroir = res.split("#!#")[1];
                        String boite = res.split("#!#")[0];
                        comboBoite.setValue("Boite " + boite);
                        clearRed();
                        setRed(String.valueOf(tiroir.charAt(0)), String.valueOf(tiroir.charAt(1)));
                        setTable(tiroir);
                        nothingFoundLab.setVisible(false);
                    } catch (Exception e1) {
                        nothingFoundLab.setVisible(true);
                    }
                } else if (searchType.equals("Echantillon")) {
                    try {
                        String path = searchEngine.searchArchiSample(search).toString();
                        String res = getTiroirAndBoite(path, searchCombo.getValue());
                        String tiroir = res.split("#!#")[1];
                        String boite = res.split("#!#")[0];
                        comboBoite.setValue("Boite " + boite);
                        clearRed();
                        setRed(String.valueOf(tiroir.charAt(0)), String.valueOf(tiroir.charAt(1)));
                        setTable(tiroir);
                        nothingFoundLab.setVisible(false);
                    } catch (Exception e1) {
                        nothingFoundLab.setVisible(true);
                    }
                } else if (searchType.equals("Titre")) {
                    try {
                        List<File> res = searchEngine.searchArchiPhysiqueTitre(search);
                        if (res.size() > 1) {
                            Dialog<ButtonType> dialog = new Dialog<>();
                            dialog.setTitle("Résultat de recherche");
                            ListView<String> listV = new ListView<>();
                            for (File f: res) {
                                String re = getTiroirAndBoite(f.toString(), searchType);
                                listV.getItems().add("Boite: " + re.split("#!#")[0] + " Tirroir:" + re.split("#!#")[1]);
                            }
                            dialog.getDialogPane().setContent(listV);
                        }
                    } catch (Exception t) {
                        t.printStackTrace();
                    }
                }
            }
        });

        plusBoiteBtn.setOnAction(e -> {
            incrementBoite();
        });

        return globVBox;
    }

    private static ObservableList<PhysicalArchive> loadArchive(String boite, String tiroir) {
        ObservableList<PhysicalArchive> list = FXCollections.observableArrayList();
        for (PhysicalArchive p: PhysicalArchiveLoader.physicList) {
            System.out.println("Archive: " + p.boite + ", Request: " + boite);
            if (p.boite.equals(boite) && p.tiroir.equals(tiroir)) {
                list.add(p);
            }
        }
        return list;
    }

    private static String getTiroirAndBoite(String path, String type) {
        String boite = "";
        String tiroir = "";
        File fileToOpen = null;
        Boolean check = false;
        File[] listFile = new File(path).listFiles();
        if (path.contains(".txt")) {
            fileToOpen = new File(path);
            check = true;
        } else {
            for (File f: listFile) {
                if (f.toString().contains("PhysicalArchive") && f.toString().contains(type)) {
                    fileToOpen = f;
                    check = true;
                    break;
                }
            }
        }
        System.out.println(fileToOpen);
        if (check) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(fileToOpen));
                String line;
                while((line=reader.readLine())!=null) {
                    String[] splitLine = line.split("#!#");
                    System.out.println(line);
                    if (splitLine[0].equals("boite")) {
                        boite = splitLine[1];
                    }
                    else if (splitLine[0].equals("tiroir")) {
                        tiroir = splitLine[1];
                    }
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Boite: " + boite);
        System.out.println("Tiroir: " + tiroir);
        return boite + "#!#" + tiroir;
        /*
        comboBoite.setValue("Boite " + boite);
        clearRed();
        setRed(String.valueOf(tiroir.charAt(0)), String.valueOf(tiroir.charAt(1)));
        setTable(tiroir);
        */
    }

    public static void setTable(String tiroir) {
        String[] boites = comboBoite.getValue().toString().split(" ");
        table.setItems(loadArchive(boites[1], tiroir));
    }

    public static void clearRed() {
        tiroir1.clear();
        tiroir2.clear();
        tiroir3.clear();
        tiroir4.clear();
    }

    public static void setRed(String num, String letter) {
        if (num.equals("1")) {
            tiroir1.setRed(letter);
        }
        if (num.equals("2")) {
            tiroir2.setRed(letter);
        }
        if (num.equals("3")) {
            tiroir3.setRed(letter);
        }
        if (num.equals("4")) {
            tiroir4.setRed(letter);
        }
    }

    private static void incrementBoite() {
        try {
            List<String> file = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(Params.basePath + "/counter.txt"));
            String line;
            while((line=reader.readLine())!=null) {
                String[] splitLine = line.split("#!#");
                if (splitLine[0].equals("blocs")) {
                    file.add(splitLine[0] + "#!#" + (Integer.parseInt(splitLine[1]) + 1));
                } else {
                    file.add(line);
                }
            }
            reader.close();
            BufferedWriter writer = new BufferedWriter(new FileWriter(Params.basePath + "/counter.txt"));
            for(String s: file) {
                writer.write(s + "\n");
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 

    public static int getNumberBoite() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(Params.basePath + "/counter.txt"));
            String line;
            while((line=reader.readLine())!=null) {
                String[] splitLine = line.split("#!#");
                if (splitLine[0].equals("blocs")) {
                    reader.close();
                    return Integer.parseInt(splitLine[1]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (Integer) null;
    }

    private static void dialAddStock(String boite, String tiroir) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Ajouter element au stock");
        dialog.setHeaderText("Detail:");
        HBox globHBox = new HBox();
        globHBox.setSpacing(15);
        Label typeLab = new Label("Type:");
        ComboBox<String> comboType = new ComboBox<>();
        comboType.getItems().addAll("Lames", "Blocs", "Autre");
        Label nameLab = new Label("Nom:");
        TextField nameField = new TextField();
        Label titreLab = new Label("Titre:");
        TextField titreField = new TextField();
        globHBox.getChildren().addAll(typeLab, comboType, nameLab, nameField, titreLab, titreField);
        dialog.getDialogPane().setContent(globHBox);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> option = dialog.showAndWait();
        if (option.get() == ButtonType.CANCEL) {
        } else {
            String fileName = String.valueOf(Math.random()) + ".txt";
            while(new File(Params.basePath + "/ArchivePhysique/" + fileName).exists()) {
                fileName = String.valueOf(Math.random()) + ".txt";
            }
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(Params.basePath + "/ArchivePhysique/" + fileName));
                writer.write("type#!#" + comboType.getValue() + "\n");
                writer.write("nProj#!#" + nameField.getText() + "\n");
                writer.write("tiroir#!#" + tiroir + "\n");
                writer.write("boite#!#" + boite + "\n");
                writer.write("titre#!#" + titreField.getText() + "\n");
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            PhysicalArchiveLoader.load();
        }
    }
    
}
