package ISO9001;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.Font;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

public class CatalogueAnticorp {

    static private ToggleButton humanButton;
    static private ToggleButton mouseButton;
    static private ToggleButton monoButton;
    static private ToggleButton multiButton;
    static private ToggleButton dabButton;
    static private ToggleButton aecButton;
    static private ToggleButton vinaButton;
    static private ToggleButton hrpButton;
    static private ToggleButton fluoButton;
    static private TextField rechField;
    static private String defaultBg;
    static private List<ToggleButton> listButton = new ArrayList<>();
    static private ListView<HBox> listV = new ListView<>();
    static private Thread t;
   
    public static VBox build() {
        VBox globVBox = new VBox();
        globVBox.setSpacing(15);
        globVBox.setPadding(new Insets(50));
        HBox titleHBox = new HBox();
        Label titleLabel = new Label("Catalogue Anticorps de la PTBC");
        titleHBox.getChildren().add(titleLabel);
        titleHBox.setAlignment(Pos.CENTER);
        Label espLab = new Label("Espèce:");
        HBox espHBox = new HBox();
        espHBox.setSpacing(20);
        humanButton = new ToggleButton();
        mouseButton = new ToggleButton();
        listButton.add(humanButton);
        listButton.add(mouseButton);
        VBox humanVBox = new VBox();
        Label humanLab = new Label("Humain");
        VBox mouseVBox = new VBox();
        Label mouseLab = new Label("Souris");
        humanButton.setSelected(true);
        mouseButton.setSelected(true);
        ImageView imageMouse = new ImageView(new Image("ISO9001/File/Mouse.png"));
        mouseVBox.getChildren().addAll(imageMouse, mouseLab);
        mouseVBox.setAlignment(Pos.CENTER);
        mouseVBox.setSpacing(6);
        mouseButton.setGraphic(mouseVBox);
        defaultBg = mouseButton.getStyle();
        ImageView imageHuman = new ImageView(new Image("ISO9001/File/human.png"));
        humanVBox.getChildren().addAll(imageHuman, humanLab);
        humanVBox.setAlignment(Pos.CENTER);
        humanButton.setGraphic(humanVBox);
        mouseButton.setMinSize(75, 75);
        mouseButton.setMaxSize(75, 75);
        humanButton.setMaxSize(75, 75);
        humanButton.setMinSize(75, 75);
        espHBox.getChildren().addAll(humanButton, mouseButton);
        Label marqLabel = new Label("Marquage:");
        HBox marqHBox = new HBox();
        marqHBox.setSpacing(20);
        monoButton = new ToggleButton();
        multiButton = new ToggleButton();
        listButton.add(monoButton);
        listButton.add(multiButton);
        monoButton.setMaxSize(75,75);
        monoButton.setMinSize(75,75);
        multiButton.setMaxSize(75,75);
        multiButton.setMinSize(75,75);
        monoButton.setSelected(true);
        multiButton.setSelected(true);
        ImageView imageMono = new ImageView(new Image("ISO9001/File/Monoplex.png"));
        VBox monoVBox = new VBox();
        monoVBox.setAlignment(Pos.CENTER);
        Label monoLab = new Label("Monoplex");
        monoVBox.getChildren().addAll(imageMono, monoLab);
        monoVBox.setSpacing(7);
        monoButton.setGraphic(monoVBox);
        ImageView imageMulti = new ImageView(new Image("ISO9001/File/multiplex.png"));
        VBox multiVBox = new VBox();
        Label multiLab = new Label("Multiplex");
        multiVBox.setAlignment(Pos.CENTER);
        multiVBox.getChildren().addAll(imageMulti, multiLab);
        multiButton.setGraphic(multiVBox);
        marqHBox.getChildren().addAll(monoButton, multiButton);
        Label revLab = new Label("Revelation:");
        HBox revHBox = new HBox();
        revHBox.setSpacing(20);
        dabButton = new ToggleButton();
        aecButton = new ToggleButton();
        vinaButton = new ToggleButton();
        hrpButton = new ToggleButton();
        fluoButton = new ToggleButton();
        listButton.add(dabButton);
        listButton.add(aecButton);
        listButton.add(vinaButton);
        listButton.add(hrpButton);
        listButton.add(fluoButton);
        dabButton.setMinSize(90,75);
        dabButton.setMaxSize(90,75);
        aecButton.setMinSize(90,75);
        aecButton.setMaxSize(90,75);
        vinaButton.setMinSize(90,75);
        vinaButton.setMaxSize(90,75);
        hrpButton.setMinSize(90,75);
        hrpButton.setMaxSize(90,75);
        fluoButton.setMinSize(90,75);
        fluoButton.setMaxSize(90,75);
        dabButton.setSelected(true);
        aecButton.setSelected(true);
        vinaButton.setSelected(true);
        hrpButton.setSelected(true);
        fluoButton.setSelected(true);
        ImageView imageDAB = new ImageView(new Image("ISO9001/File/DAB_stain.png"));
        VBox dabVBox = new VBox();
        dabVBox.setAlignment(Pos.CENTER);
        Label dabLab = new Label("DAB");
        dabVBox.getChildren().addAll(imageDAB, dabLab);
        dabButton.setGraphic(dabVBox);
        ImageView imageAEC = new ImageView(new Image("ISO9001/File/aec_stain.png"));
        VBox aecVBox = new VBox();
        aecVBox.setAlignment(Pos.CENTER);
        Label aecLab = new Label("AEC");
        aecVBox.getChildren().addAll(imageAEC, aecLab);
        aecButton.setGraphic(aecVBox);
        ImageView imageVina = new ImageView(new Image("ISO9001/File/Vinagreen_stain.png"));
        VBox vinaVBox = new VBox();
        vinaVBox.setAlignment(Pos.CENTER);
        Label vinaLab = new Label("Vinagreen");
        vinaVBox.getChildren().addAll(imageVina, vinaLab);
        vinaButton.setGraphic(vinaVBox);
        ImageView imageMagenta = new ImageView(new Image("ISO9001/File/Magenta_stain.png"));
        VBox hrpVBox = new VBox();
        hrpVBox.setAlignment(Pos.CENTER);
        Label hrpLab = new Label("HRP Magenta");
        hrpVBox.getChildren().addAll(imageMagenta, hrpLab);
        hrpButton.setGraphic(hrpVBox);
        ImageView imageFluo = new ImageView(new Image("ISO9001/File/fluo_stain.png"));
        VBox fluoVBox = new VBox();
        fluoVBox.setAlignment(Pos.CENTER);
        Label fluoLab = new Label("Fluo");
        fluoVBox.getChildren().addAll(imageFluo, fluoLab);
        fluoButton.setGraphic(fluoVBox);
        revHBox.getChildren().addAll(dabButton, aecButton, vinaButton, hrpButton, fluoButton);
        Label rechLabel = new Label("Recherche:");
        rechField = new TextField();
        rechField.setMaxWidth(300);
        Label resLab = new Label("Résultat:");
        listV = new ListView<>();
        globVBox.getChildren().addAll(titleHBox, espLab, espHBox, marqLabel, marqHBox, revLab, revHBox, rechLabel, rechField, resLab, listV);
        verifButton();
        search();
        dabButton.setOnAction(e -> {
            search();
        });
        humanButton.setOnAction(e -> {
            search();
        });
        mouseButton.setOnAction(e -> {
            search();
        });
        monoButton.setOnAction(e -> {
            search();
        });
        multiButton.setOnAction(e -> {
            search();
        });
        aecButton.setOnAction(e -> {
            search();
        });
        vinaButton.setOnAction(e -> {
            search();
        });
        hrpButton.setOnAction(e -> {
            search();
        });
        fluoButton.setOnAction(e -> {
            search();
        });
        rechField.setOnAction(e -> {
            search();
        });
        return globVBox;
    }

    private static void verifButton() {
        for(ToggleButton b: listButton) {
            if (b.isSelected()) {
                b.setStyle("-fx-background-color: khaki; -fx-border-color: goldenrod; -fx-border-radius: 3px; -fx-border-width: 2px");
            } else {
                b.setStyle(defaultBg);
            }
        }
    }

    private static void search() {
        verifButton();
        t = null;
        listV.getItems().clear();
        t = new Thread() {
            public void run() {
                Thread thisThread = Thread.currentThread();
                if (monoButton.isSelected()) {
                    for(Marquage m: MarquageLoader.listValid) {
                        if (t != thisThread) {
                            break;
                        }
                        if (m.isColo()) {
                            continue;
                        }
                        if (!humanButton.isSelected() && m.getEspeceCible().equals("Human")) {
                            continue;
                        }
                        if (!mouseButton.isSelected() && m.getEspeceCible().equals("Mouse")) {
                            continue;
                        }
                        if (!dabButton.isSelected() && m.getRevelation().equals("DAB")) {
                            continue;
                        }
                        if (!aecButton.isSelected() && m.getRevelation().equals("AEC")) {
                            continue;
                        }
                        if (!vinaButton.isSelected() && m.getRevelation().equals("Vinagreen")) {
                            continue;
                        }
                        if (!hrpButton.isSelected() && m.getRevelation().equals("HRP Magenta")) {
                            continue;
                        }
                        if (!fluoButton.isSelected() && m.getRevelation().equals("Fluo")) {
                            continue;
                        }
                        if (rechField.getText().length() > 0) {
                            if (m.getName().toUpperCase().contains(rechField.getText().toUpperCase())) {
                                HBox ad = hBoxMarquageBuilder(m);
                                System.out.println(m.getName());
                                Platform.runLater(() -> listV.getItems().add(ad));
                            }
                        } else {
                                HBox ad = hBoxMarquageBuilder(m);
                                System.out.println(m.getName());
                                Platform.runLater(() -> listV.getItems().add(ad));
                        }
                    }
                }
                if (multiButton.isSelected()) {
                    for(Multiplex m: MarquageLoader.listMultiplex) {
                        if (t != thisThread) {
                            break;
                        }
                        for (Marquage marq: m.marquageList) {
                            if (t != thisThread) {
                                break;
                            }
                            if (checkMrq(marq) == true) {
                                HBox ad = hBoxMultiplexBuilder(m);
                                Platform.runLater(() -> listV.getItems().add(ad));
                                break;
                            }
                        }
                    }
                }
            }
        };
        t.start();
    }

    private static boolean checkMrq(Marquage m) {
        if (!humanButton.isSelected() && m.getEspeceCible().equals("Human")) {
            return false;
        }
        if (!mouseButton.isSelected() && m.getEspeceCible().equals("Mouse")) {
            return false;
        }
        if (!dabButton.isSelected() && m.getRevelation().equals("DAB")) {
            return false;
        }
        if (!aecButton.isSelected() && m.getRevelation().equals("AEC")) {
            return false;
        }
        if (!vinaButton.isSelected() && m.getRevelation().equals("Vinagreen")) {
            return false;
        }
        if (!hrpButton.isSelected() && m.getRevelation().equals("HRP Magenta")) {
            return false;
        }
        if (!fluoButton.isSelected() && m.getRevelation().equals("Fluo")) {
            return false;
        }
        if (rechField.getText().length() > 0) {
            if (m.getName().toUpperCase().contains(rechField.getText().toUpperCase())) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    } 

    private static Color getColorRevel(Marquage m) {
        switch (m.getRevelation()) {
            case "DAB":
                return Color.BROWN;
            case "AEC":
                return Color.RED;
            case "Vinagreen":
                return Color.GREEN;
            case "HRP Magenta":
                return Color.MAGENTA;
            case "Fluo":
                return Color.YELLOW;
        }
        return null;
    }

    private static HBox hBoxMarquageBuilder(Marquage m) {
        HBox globHBox = new HBox();
        globHBox.setSpacing(30);
        //Label info = new Label("Cible: " + m.name + "\tRévélation: " + m.getRevelation() + "\tN°: " + m.getNum() + "\tEspèce cible: " + m.getEspeceCible() + "\tReférence: " + m.getReference() + "\tFournisseur: " + m.getFourniseur() + "\tPrix indicatifs: " + String.valueOf(Math.round(new quotePrice(m.getNum()).materiel)) + "€");
        TextFlow info = new TextFlow();
        Text cible = new Text("Cible: ");
        Text cibleName = new Text(m.getName());
        cibleName.setStyle("-fx-font-weight: bold");
        Text revelation = new Text("\tRévélation: ");
        Text revelationName = new Text(m.getRevelation());
        revelationName.setFill(getColorRevel(m));
        Text in = new Text("\tEspèce cible: " + m.getEspeceCible() + "\tRéf: " + m.getReference() + "\tFourn: " + m.getFourniseur() + "   \tPrix indicatifs: " + String.valueOf(Math.round(new quotePrice(m.getNum()).materiel)) + "€");
        info.getChildren().addAll(cible, cibleName, revelation, revelationName, in);
        Button ficheButton = new Button();
        ImageView imageFiche = new ImageView(new Image("ISO9001/File/fiche.png"));
        ficheButton.setGraphic(imageFiche);
        ImageView imageFolder = new ImageView(new Image("ISO9001/File/folder2.png"));
        Button folderButton = new Button();
        folderButton.setGraphic(imageFolder);
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        globHBox.getChildren().addAll(info, region,ficheButton, folderButton);

        ficheButton.setOnAction(e -> {
            try {
                Runtime.getRuntime().exec("CMD /C START " + findXlsx(m.getPath() + "/Fiche_PTBC/"));
            } catch (IOException e1) {
                // TODO Auto-generated catch block
            }
        });

        folderButton.setOnAction(e -> {
            System.out.println(m.getPath());
            try {
                Runtime.getRuntime().exec("explorer.exe /select," + m.getPath().replace("/", "\\") + "\\Scan");
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });

        return globHBox;
    }

    private static String findXlsx(String path) {
        File[] listFile = new File(path).listFiles();
        for (File f: listFile) {
            if (f.getName().contains("xlsx")) {
                return f.toString();
            }
        }
        return null;
    } 

    private static HBox hBoxMultiplexBuilder(Multiplex m) {
        HBox globHBox = new HBox();
        globHBox.setSpacing(30);
        String msg = "";
        String espCible = "";
        for(Marquage marq: m.marquageList) {
            msg = msg + marq.getName() + ",";
            espCible = marq.getEspeceCible();
        }
        Text cible = new Text(msg + "\t");
        Text revelation = new Text("Multiplex");
        cible.setStyle("-fx-font-weight: bold");
        revelation.setStyle("-fx-font-weight: bold");
        TextFlow flow = new TextFlow();
        msg = "";
        msg = msg + "\tExpéce Cible: " + m.getEsp();
        msg = msg + "\t Prix indicatifs: " + String.valueOf(Math.round(new quotePrice(m.getNum()).materiel));
        flow.getChildren().addAll(new Text("Cibles: "), cible, new Text("\tRévélation: ") ,revelation, new Text(msg));
        Label info = new Label(msg);
        Button ficheButton = new Button();
        ImageView imageFiche = new ImageView(new Image("ISO9001/File/fiche.png"));
        ficheButton.setGraphic(imageFiche);
        ImageView imageFolder = new ImageView(new Image("ISO9001/File/folder2.png"));
        Button folderButton = new Button();
        folderButton.setGraphic(imageFolder);
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        globHBox.getChildren().addAll(flow, region, ficheButton, folderButton);
        return globHBox;
    }
}
