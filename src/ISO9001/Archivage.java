package ISO9001;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.jcajce.provider.symmetric.Twofish.PBEWithSHAKeyFactory;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Archivage {
   
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


        searchBtn.setOnAction(e -> {
            if (searchField.getText().equals("VDPink")) {
                Main.enregGridPane.setStyle("-fx-background-color: pink");
                Main.browseScene.setStyle("-fx-background-color: pink");
                Main.mapBox.setStyle("-fx-background-color: pink");
                Main.adminPane.setStyle("-fx-background-color: pink");
            }
            else if (searchField.getText().isEmpty()) {
                reloadBrowser();
            } else {
                String search = searchField.getText();
                reloadBrowser(searchEngine.searchArchi(search));
            }
        });

        searchField.setOnKeyPressed(e -> {
            if (e.getCode().toString().equals("ENTER")) {
                searchBtn.fire();
            }
        });
        reloadBrowser();
        return pane;
    }

    private static void reloadBrowser() {
        browseVBox.getChildren().clear();
        File[] listArchivage = new File(Params.basePath + "/Archivage").listFiles();
        for(File f: listArchivage) {
            Separator sep = new Separator();
            sep.setMinWidth(900);
            browseVBox.getChildren().addAll(read(f.toString()), sep);
        }
    }

    private static void reloadBrowser(List<File> listArchivage) {
        browseVBox.getChildren().clear();
        for(File f: listArchivage) {
            Separator sep = new Separator();
            sep.setMinWidth(200);
            browseVBox.getChildren().addAll(read(f.toString()), sep);
        }
    }

    private static VBox read(String path) {   
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path + "/Info.txt"));
            String line;
            String nProject = "";
            String title = "";
            String description = "";
            String epaisseur = "";
            String nbCoupe = "";
            String echantillon = "";
            String espece = "";
            Associe referent = null;
            String owner = null;
            String date = "";
            String lamePhysicArchive = "None";
            String blocPhysicArchive = "None";
            while((line=reader.readLine()) != null) {
                String[] splitLine = line.split("#!#");
                if (splitLine[0].equals("nProject")) {
                    nProject = splitLine[1];
                }
                else if(splitLine[0].equals("titre")) {
                    title = splitLine[1];
                }
                else if (splitLine[0].equals("description")) {
                    try {
                    if (!splitLine[1].equals("null") || !splitLine[1].equals("")) {
                        description = splitLine[1];
                        if (splitLine.length > 1) {
                            for(int i=2; i<splitLine.length; i++) {
                                description = description + "\r\n" + splitLine[i];
                            }
                        }
                    }
                    } catch (Exception e) {
                        description = "";
                    } 
                }
                else if(splitLine[0].equals("epaisseurCoupe")) {
                    epaisseur = splitLine[1];
                }
                else if (splitLine[0].equals("nombreCoupe")) {
                    nbCoupe = splitLine[1];
                }
                else if (splitLine[0].equals("echantillon")) {
                    echantillon = splitLine[1];
                }
                else if (splitLine[0].equals("espece")) {
                    espece = splitLine[1];
                }
                else if (splitLine[0].equals("referent")) {
                    referent = new Associe(splitLine[1]);
                }
                else if (splitLine[0].equals("owner")) {
                    owner = splitLine[1];
                }
                else if (splitLine[0].equals("date")) {
                    date = splitLine[1];
                }
            }
            File[] listFiles=  new File(path).listFiles();
            for (File f: listFiles) {
                if (f.getName().contains("PhysicalArchive")) {
                    if (f.getName().contains("Lames")) {
                        reader = new BufferedReader(new FileReader(f));
                        while((line=reader.readLine())!=null) {
                            String[] splitLine = line.split("#!#");
                            if (splitLine[0].equals("fullSave")) {
                                if (splitLine[1].equals("true")) {
                                    lamePhysicArchive = "Interne";
                                }
                            } else if (splitLine[0].equals("desc")) {
                                lamePhysicArchive = splitLine[1];
                            }
                        }
                    } else if (f.getName().contains("Blocs")) {
                        reader = new BufferedReader(new FileReader(f));
                        while((line=reader.readLine())!=null) {
                            String[] splitLine = line.split("#!#");
                            if (splitLine[0].equals("fullSave")) {
                                if (splitLine[1].equals("true")) {
                                    blocPhysicArchive = "Interne";
                                }
                            } else if (splitLine[0].equals("desc")) {
                                blocPhysicArchive = splitLine[1];
                            }
                        }

                    }
                }
            }

            String marquageList = "";
            try {
                reader = new BufferedReader(new FileReader(path + "/Marquage.txt"));
                while((line=reader.readLine()) != null) {
                    marquageList = marquageList + "," +(new File(line).getName().split("_")[1].split("#!#")[0]);
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            reader.close();
            return createTile(nProject, title, description, epaisseur, nbCoupe, echantillon, espece, referent, owner, date, marquageList, lamePhysicArchive, blocPhysicArchive);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static VBox createTile(String nProject, String title, String description, String epaisseur, String nbCoupe, String echantillon, String espece, Associe referent, String owner, String date, String marquageList, String lamePhysicArchive, String blocPhysicalArchive) {
        VBox globVBox = new VBox();
        globVBox.setSpacing(0);
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setMinWidth(900);
        hBox.setAlignment(Pos.CENTER);
        Label nProjectLab = new Label("P." + nProject);
        Label nameLab = new Label("Name:    " + title);
        Label coupeLab = new Label("Coupe:  " + nbCoupe);
        Label echantillonLab = new Label("Echantillon:  " + echantillon);
        Label especeLab = new Label("Espece:  " + espece);
        Label refLab = new Label("Référent:  " + referent.getPrenom() + " " + referent.getName());
        Label dateLab = new Label("Date:    " + date);
        Label descLab = new Label("Description:     " + description);
        Label marquageLab = new Label("Marquage:    " + marquageList);
        Label ownerLab = new Label("Demandeur:  "  + owner);
        Label lamePhysicLab = new Label("Lame: " + lamePhysicArchive); 
        Label blocPhysicLab = new Label("Bloc: " + blocPhysicalArchive); 
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER_LEFT);
        gridPane.setHgap(40);
        gridPane.setVgap(35);
        gridPane.add(nameLab, 1, 1);
        gridPane.add(ownerLab, 2,1);
        gridPane.add(coupeLab, 2, 3 );
        gridPane.add(echantillonLab, 1,3);
        gridPane.add(especeLab,1,2);
        gridPane.add(refLab,2,2);
        gridPane.add(dateLab,3,2);
        gridPane.add(descLab, 3,1);
        gridPane.add(marquageLab, 3, 3);
        gridPane.add(lamePhysicLab, 4, 1);
        gridPane.add(blocPhysicLab, 4, 2);
        globVBox.getChildren().addAll(nProjectLab ,gridPane);
        return globVBox;
    }
}
