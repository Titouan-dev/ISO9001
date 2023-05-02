package ISO9001;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;
import java.util.concurrent.DelayQueue;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.text.TabExpander;

import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.css.Style;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Main extends Application {

    public static Tab enregistrementTab;
    public static GridPane enregGridPane;
    public static BorderPane browseScene;
    public static VBox mapBox;
    public static SplitPane adminPane;
    public static Scene scene;
    public static BorderPane archivePane;
    public static SplitPane devisPane;
    public static VBox catalogueAnticorp;
    static TabPane tabPane;
    public static SplitPane TimeLineBox;
    public static VBox stockage;
    static Tab adminTab;
    static Tab editionTab;
    static Tab archiveTab;
    static Tab miseAuPointTab;
    static Tab devisTab;
    static Tab TimeLineTab;
    static Tab physcalTab;
    static Tab catalogueTab;
    static String baseStyle;
    static String redStyle = "-fx-background-color: #f28f8f;";


    @Override
    public void start(Stage primaryStage) throws Exception{

        new Params();
        checkVersion.checkVersion();
        Parser parserThread = new Parser();
        parserThread.setDaemon(true);
        parserThread.start();
        MarquageLoader.loadExp();
        MarquageLoader.load();
        PhysicalArchiveLoader.load();
        new User();
        //Notification.load();
        VBox root = new VBox();

        // TabCreation
        tabPane = new TabPane();
        enregistrementTab = new Tab("Enregistrement");
        editionTab = new Tab("Edition");
        archiveTab = new Tab("Archive");
        miseAuPointTab = new Tab("Mise au point anticorps");
        adminTab = new Tab("Administration");
        devisTab = new Tab("Devis");
        TimeLineTab = new Tab("TimeLine");
        physcalTab = new Tab("Stockage");
        catalogueTab = new Tab("Catalogue Anticorps");
        enregistrementTab.setClosable(false);
        editionTab.setClosable(false);
        archiveTab.setClosable(false);
        miseAuPointTab.setClosable(false);
        adminTab.setClosable(false);
        devisTab.setClosable(false);
        TimeLineTab.setClosable(false);
        physcalTab.setClosable(false);
        catalogueTab.setClosable(false);
        baseStyle = adminTab.getStyle();
        //adminTab.setStyle(" -fx-background-color: #f28f8f;");
        if (User.grade.equals("Administrateur")) {
        tabPane.getTabs().addAll(enregistrementTab, editionTab, archiveTab, miseAuPointTab, adminTab, devisTab, TimeLineTab, physcalTab, catalogueTab);
        } else if (User.grade.equals("Moderateur")) {
            tabPane.getTabs().addAll(enregistrementTab, editionTab, archiveTab, miseAuPointTab, TimeLineTab, catalogueTab);
        } else {
            tabPane.getTabs().addAll(enregistrementTab, editionTab, archiveTab,catalogueTab);
        }
        tabPane.tabMinWidthProperty().set(863 / tabPane.getTabs().size());
        tabPane.tabMaxWidthProperty().set(234);
        tabPane.tabMaxHeightProperty().set(30);
        tabPane.tabMinHeightProperty().set(30);

        enregGridPane = Enregistrement.build();
        enregistrementTab.setContent(enregGridPane);
        browseScene = Browse.build();
        editionTab.setContent(browseScene);
        mapBox = MiseAuPoint.build();
        miseAuPointTab.setContent(mapBox);
        adminPane = Administration.build();
        adminTab.setContent(adminPane);
        archivePane = Archivage.build();
        archiveTab.setContent(archivePane);
        devisPane = Devis.build();
        devisTab.setContent(devisPane);
        TimeLineBox = TimeLineProject.build();
        TimeLineTab.setContent(TimeLineBox);
        stockage = StockagePhysique.build();
        physcalTab.setContent(stockage);
        catalogueAnticorp = CatalogueAnticorp.build();
        catalogueTab.setContent(catalogueAnticorp);


        Notification.update();

        root.getChildren().add(tabPane);
        primaryStage.setTitle("ISO9001");
        scene = new Scene(root, 1025, 925);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    public static void setWait() {
        scene.setCursor(Cursor.WAIT);
    }

    public static void openEnrg() {
        tabPane.getSelectionModel().select(enregistrementTab);
    }


    public static void main(String[] args) {
        launch(args);
    }
    
    public static void setTabColor(String tab) {
        switch (tab) {
            case "Edition":
                editionTab.setStyle("-fx-background-color: #f28f8f;");
            case "Administration":
                adminTab.setStyle(redStyle);
        }
    }

    public static void defaultTabColor(String tab) {
        switch (tab) {
            case "Edition":
                editionTab.setStyle(baseStyle);
            case "Administration":
                adminTab.setStyle(baseStyle);
        }
    }
}