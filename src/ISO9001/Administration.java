package ISO9001;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.Optional;

import javax.print.attribute.standard.Copies;

import org.apache.batik.ext.swing.GridBagConstants;
import org.apache.batik.gvt.Selectable;

import ISO9001.Dialog.dialogAddAssocie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ResizeFeaturesBase;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView.ResizeFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class Administration {

    public static SplitPane build() {
        SplitPane splitPane = new SplitPane();

        VBox leftVBox = new VBox();
        Button gestUser = new Button("Gestion utilisateurs");
        gestUser.setStyle("-fx-background-color: transparent;");
        gestUser.setMinWidth(150);
        gestUser.setStyle("-fx-font-size:15; -fx-background-color: transparent;");
        gestUser.setMinHeight(75);
        gestUser.setOnMouseExited(e -> gestUser.setStyle("-fx-background-color: transparent; -fx-font-size:15;"));
        gestUser.setOnMouseEntered(e -> gestUser.setStyle("-fx-background-color: lightgrey; -fx-font-size:15;"));
        Button aprobUserBut = new Button("Aprobation\nUtilisateurs");
        aprobUserBut.setStyle("-fx-background-color: transparent; -fx-font-size:15;");
        aprobUserBut.setOnMouseExited(e -> aprobUserBut.setStyle("-fx-background-color: transparent; -fx-font-size:15;"));
        aprobUserBut.setOnMouseEntered(e -> aprobUserBut.setStyle("-fx-background-color: lightgrey; -fx-font-size:15;"));
        aprobUserBut.setMinWidth(150);
        aprobUserBut.setMinHeight(75);
        leftVBox.setSpacing(10);
        leftVBox.setPadding(new Insets(50, 0, 0, 0));
        Button aprobProjBut = new Button("Aprobation\nProject");
        aprobProjBut.setStyle("-fx-background-color: transparent; -fx-font-size:15;");
        aprobProjBut.setOnMouseExited(e -> aprobProjBut.setStyle("-fx-background-color: transparent; -fx-font-size:15;"));
        aprobProjBut.setOnMouseEntered(e -> aprobProjBut.setStyle("-fx-background-color: lightgrey; -fx-font-size:15;"));
        aprobProjBut.setMinWidth(150);
        aprobProjBut.setMinHeight(75);
        Button editRefBut = new Button("Editer Referent");
        editRefBut.setStyle("-fx-background-color: transparent; -fx-font-size:15;");
        editRefBut.setOnMouseExited(e -> editRefBut.setStyle("-fx-background-color: transparent; -fx-font-size:15;"));
        editRefBut.setOnMouseEntered(e -> editRefBut.setStyle("-fx-background-color: lightgrey; -fx-font-size:15;"));
        editRefBut.setMinWidth(150);
        editRefBut.setMinHeight(75);
        Separator sep = new Separator();
        Separator sep2 = new Separator();
        Separator sep3 = new Separator();
        leftVBox.getChildren().addAll(gestUser, sep, aprobUserBut, sep2, aprobProjBut, sep3, editRefBut);
        leftVBox.setMaxWidth(150);
        leftVBox.setMinWidth(150);
        VBox simpleVBox = new VBox();
        simpleVBox.getChildren().add(userAdmin());
        splitPane.getItems().addAll(leftVBox, simpleVBox);

        gestUser.setOnMouseClicked(e -> {
            simpleVBox.getChildren().clear();
            simpleVBox.getChildren().add(userAdmin());
            gestUser.setStyle("-fx-background-color: lightgrey; -fx-font-size:15;");
            aprobUserBut.setStyle("-fx-background-color: transparent; -fx-font-size:15;");
            aprobProjBut.setStyle("-fx-background-color: transparent; -fx-font-size:15;");
            editRefBut.setStyle("-fx-background-color: transparent; -fx-font-size:15;");
        });

        aprobUserBut.setOnMouseClicked(e -> {
            simpleVBox.getChildren().clear();
            simpleVBox.getChildren().add(aprobUser());
            gestUser.setStyle("-fx-background-color: transparent; -fx-font-size:15;");
            aprobUserBut.setStyle("-fx-background-color: lightgrey; -fx-font-size:15;");
            aprobProjBut.setStyle("-fx-background-color: transparent; -fx-font-size:15;");
            editRefBut.setStyle("-fx-background-color: transparent; -fx-font-size:15;");
        });

        aprobProjBut.setOnMouseClicked(e -> {
            simpleVBox.getChildren().clear();
            simpleVBox.getChildren().add(aprobProject());
            aprobProjBut.setStyle("-fx-background-color: lightgrey; -fx-font-size:15;");
            gestUser.setStyle("-fx-background-color: transparent; -fx-font-size:15;");
            aprobUserBut.setStyle("-fx-background-color: transparent; -fx-font-size:15;");
            editRefBut.setStyle("-fx-background-color: transparent; -fx-font-size:15;");
        });

        editRefBut.setOnMouseClicked(e -> {
            simpleVBox.getChildren().clear();
            simpleVBox.getChildren().add(editReferent());
            editRefBut.setStyle("-fx-background-color: lightgrey; -fx-font-size:15;");
            gestUser.setStyle("-fx-background-color: transparent; -fx-font-size:15;");
            aprobUserBut.setStyle("-fx-background-color: transparent; -fx-font-size:15;");
            aprobProjBut.setStyle("-fx-background-color: transparent; -fx-font-size:15;");
        });

        return splitPane;
    }

    private static ObservableList<Users> loadUser() {
        ObservableList<Users> userList = FXCollections.observableArrayList();
        File[] lisUserFile = new File(Params.basePath + "/Users/").listFiles();
        for(File f: lisUserFile) {
            userList.add(new Users(f.toString() + "/Info.txt"));
        }
        return userList;
    }

    private static ObservableList<Users> loadPendingUser() {
        ObservableList<Users> userList = FXCollections.observableArrayList();
        File[] listPending = new File(Params.basePath + "/PendingUsers/").listFiles();
        for(File f: listPending) {
            userList.add(new Users(f.toString() + "/Info.txt"));
        }
        return userList;
    }

    private static ObservableList<Project> loadPendingProj() {
        ObservableList<Project> projList = FXCollections.observableArrayList();
        File[] listPending = new File(Params.basePath + "/pendingProject").listFiles();
        for(File f: listPending) {
            projList.add(new Project(f.toString()));
        }
        return projList;
    }

    public static ObservableList<Project> loadProj() {
        ObservableList<Project> projList = FXCollections.observableArrayList();
        File[] listPending = new File(Params.basePath + "/Project").listFiles();
        for(File f: listPending) {
            projList.add(new Project(f.toString()));
        }
        return projList;
    }

    private static VBox editReferent() {
        VBox globVBox = new VBox();
        globVBox.setSpacing(20);
        globVBox.setPadding(new Insets(20));
        Label titleLab = new Label("Project courant:");
        titleLab.setStyle("-fx-font-size: 15;");
        Button editBut = new Button("Editer Référent");
        HBox butHBox = new HBox();
        butHBox.setAlignment(Pos.BASELINE_RIGHT);
        butHBox.getChildren().add(editBut);
        TableView<Project> projTable = new TableView<>();
        TableColumn<Project, String> nCol = new TableColumn<>("N°Project");
        TableColumn<Project, String> ownerCol = new TableColumn<>("Owner");
        TableColumn<Project, String> espCol = new TableColumn<>("Referent");
        TableColumn<Project, String> titleCol = new TableColumn<>("Titre");
        TableColumn<Project, String> echanCol = new TableColumn<>("Echantillon");
        TableColumn<Project, String> coupeCol = new TableColumn<>("Coupe");
        TableColumn<Project, String> marquageCol = new TableColumn<>("Marquage");
        nCol.setCellValueFactory(new PropertyValueFactory<>("number"));
        ownerCol.setCellValueFactory(new PropertyValueFactory<>("owner"));
        espCol.setCellValueFactory(new PropertyValueFactory<>("referent"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        echanCol.setCellValueFactory(new PropertyValueFactory<>("echantillon"));
        coupeCol.setCellValueFactory(new PropertyValueFactory<>("coupe"));
        marquageCol.setCellValueFactory(new PropertyValueFactory<>("marquage"));

        nCol.setPrefWidth(75);
        ownerCol.setPrefWidth(135);
        espCol.setPrefWidth(135);
        titleCol.setPrefWidth(135);
        marquageCol.setPrefWidth(200);
        projTable.setMinHeight(750);
        projTable.getColumns().addAll(nCol, ownerCol, espCol, titleCol, echanCol, coupeCol, marquageCol);
        projTable.setItems(loadProj());

        globVBox.getChildren().addAll(titleLab, projTable, butHBox);

        editBut.setOnMouseClicked(e -> {
            String ref = chooseAssocie();
            if (ref == null) {
                e.consume();
            }
            ref = ref.split(" ")[2];
            Project selected = projTable.getSelectionModel().getSelectedItem();
            Project.aprobedProj(selected);
            selected.setReferent(ref);
            selected.save(Params.basePath + "/Project/");
            User.loadProject();
            Browse.reloadBrowser(User.projList);
            projTable.setItems(loadProj());
        });

        return globVBox;
    }


    private static VBox aprobProject() {
        VBox globVBox = new VBox();
        globVBox.setSpacing(20);
        globVBox.setPadding(new Insets(20));
        Label titleLab = new Label("Project en attente:");
        titleLab.setStyle("-fx-font-size: 15");
        Button refreshBut = new Button("Raffraichir");
        HBox labHBox = new HBox();
        labHBox.setSpacing(615);
        labHBox.getChildren().addAll(titleLab, refreshBut);
        Button acceptBut = new Button("Accepter");
        acceptBut.setStyle("-fx-font-size: 15");
        Button rejectBut = new Button("Rejeter");
        rejectBut.setStyle("-fx-font-size: 15");
        HBox buttonHBox = new HBox();
        buttonHBox.setAlignment(Pos.BASELINE_RIGHT);
        buttonHBox.setSpacing(20);
        buttonHBox.getChildren().addAll(acceptBut, rejectBut);
        TableView<Project> projTable = new TableView<>();
        TableColumn<Project, String> nCol = new TableColumn<>("N°Project");
        TableColumn<Project, String> ownerCol = new TableColumn<>("Owner");
        TableColumn<Project, String> espCol = new TableColumn<>("Espece");
        TableColumn<Project, String> titleCol = new TableColumn<>("Titre");
        TableColumn<Project, String> echanCol = new TableColumn<>("Echantillon");
        TableColumn<Project, String> coupeCol = new TableColumn<>("Coupe");
        TableColumn<Project, String> marquageCol = new TableColumn<>("Marquage");
        nCol.setCellValueFactory(new PropertyValueFactory<>("number"));
        ownerCol.setCellValueFactory(new PropertyValueFactory<>("owner"));
        espCol.setCellValueFactory(new PropertyValueFactory<>("esp"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        echanCol.setCellValueFactory(new PropertyValueFactory<>("echantillon"));
        coupeCol.setCellValueFactory(new PropertyValueFactory<>("coupe"));
        marquageCol.setCellValueFactory(new PropertyValueFactory<>("marquage"));

        nCol.setPrefWidth(75);
        ownerCol.setPrefWidth(135);
        espCol.setPrefWidth(135);
        titleCol.setPrefWidth(135);
        marquageCol.setPrefWidth(200);
        projTable.setMinHeight(750);
        projTable.getColumns().addAll(nCol, ownerCol, espCol, titleCol, echanCol, coupeCol, marquageCol);
        projTable.setItems(loadPendingProj());
        globVBox.getChildren().addAll(labHBox, projTable, buttonHBox);

        refreshBut.setOnAction(e -> {
            projTable.setItems(loadPendingProj());
        });

        acceptBut.setOnAction(e -> {
            String ref = chooseAssocie();
            if (ref == null) {
                e.consume();
            }
            ref = ref.split(" ")[2];
            Project selected = projTable.getSelectionModel().getSelectedItem();
            Project.aprobedProj(selected);
            selected.setReferent(ref);
            //copyRecusrively(selected.path, Params.basePath + "/Project/P." + selected.getNumber());
            deleteDirectory(new File(selected.path));
            selected.save(Params.basePath + "/Project/");
            System.out.println(selected.path);
            projTable.setItems(loadPendingProj());
            selected.setPath(Params.basePath + "/Project/P." + selected.getNumber());
            //Excel_util.ficheProj(selected);
            User.loadProject();
            //Browse.reloadBrowser(User.projList);
        });

        rejectBut.setOnAction(e -> {
            Project selected = projTable.getSelectionModel().getSelectedItem();
            deleteDirectory(new File(selected.getPath()));
            projTable.setItems(loadPendingProj());
        });

        return globVBox;
    }

    private static String chooseAssocie() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Input Dialog");
        dialog.setHeaderText("Choisir un référent:");
        ComboBox combo = new ComboBox<>(dialogAddAssocie.getComboAssocier());
        dialog.getDialogPane().setContent(combo);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> option = dialog.showAndWait();

        if (option.get() == ButtonType.OK) {
            return (String) combo.getSelectionModel().getSelectedItem();

        } else {
            return null;
        }
    }

    private static VBox aprobUser() {
        VBox globVBox = new VBox();
        globVBox.setPadding(new Insets(20));
        globVBox.setSpacing(20);
        HBox labHBox = new HBox();
        Label userLab = new Label("Liste des utilisateurs en attente:");
        Button refreshBut = new Button("Raffraichir");
        labHBox.setSpacing(528);
        labHBox.getChildren().addAll(userLab, refreshBut);
        userLab.setStyle("-fx-font-size: 15");
        TableView<Users> userTable = new TableView<>();
        TableColumn<Users, String> userCol = new TableColumn<>("User");
        TableColumn<Users, String> nameCol = new TableColumn<>("Nom");
        TableColumn<Users, String> surnameCol = new TableColumn<>("Prenom");
        TableColumn<Users, String> mailCol = new TableColumn<>("Mail");
        userCol.setCellValueFactory(new PropertyValueFactory<>("user"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameCol.setCellValueFactory(new PropertyValueFactory<>("surname"));
        mailCol.setCellValueFactory(new PropertyValueFactory<>("mail"));
        double colWidth = 135.5;
        userCol.setPrefWidth(colWidth);
        nameCol.setPrefWidth(colWidth);
        surnameCol.setPrefWidth(colWidth);
        mailCol.setPrefWidth(colWidth);
        userTable.getColumns().addAll(userCol, nameCol, surnameCol, mailCol);
        userTable.setItems(loadPendingUser());
        userTable.setMinHeight(750);
        Button accBut = new Button("Accepter");
        accBut.setStyle("-fx-font-size: 15");
        Button rejBut = new Button("Rejeter");
        rejBut.setStyle("-fx-font-size: 15");
        HBox butHBox = new HBox();
        butHBox.setAlignment(Pos.BASELINE_RIGHT);
        butHBox.setSpacing(20);
        butHBox.getChildren().addAll(accBut, rejBut);
        globVBox.getChildren().addAll(labHBox, userTable, butHBox);
        
        accBut.setOnMouseClicked(e -> {
            Users selected = userTable.getSelectionModel().getSelectedItem();
            accUser(selected);
        });

        rejBut.setOnMouseClicked(e -> {
            Users selected = userTable.getSelectionModel().getSelectedItem();
            deleteDirectory(new File(Params.basePath + "/PendingUsers/" + selected.getUser()));
        });

        refreshBut.setOnMouseClicked(e -> {
            userTable.setItems(loadPendingUser());
        });
        
        return globVBox;
    }

    private static VBox userAdmin() {
        VBox globVBox = new VBox();
        globVBox.setPadding(new Insets(20));
        globVBox.setSpacing(20);
        HBox labHBox = new HBox();
        Label userLab = new Label("Liste des utilisateurs:");
        Button refreshBut = new Button("Raffraichir");
        labHBox.setSpacing(600);
        labHBox.getChildren().addAll(userLab, refreshBut);
        userLab.setStyle("-fx-font-size: 15");
        TableView<Users> userTable = new TableView<>();
        TableColumn<Users, String> userCol = new TableColumn<>("User");
        TableColumn<Users, String> nameCol = new TableColumn<>("Nom");
        TableColumn<Users, String> surnameCol = new TableColumn<>("Prenom");
        TableColumn<Users, String> mailCol = new TableColumn<>("Mail");
        TableColumn<Users, String> gradeCol = new TableColumn<>("Role");
        TableColumn<Users, String> superCol = new TableColumn<>("Superviseur");
        userCol.setCellValueFactory(new PropertyValueFactory<>("user"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameCol.setCellValueFactory(new PropertyValueFactory<>("surname"));
        mailCol.setCellValueFactory(new PropertyValueFactory<>("mail"));
        gradeCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        superCol.setCellValueFactory(new PropertyValueFactory<>("supervisor"));
        double colWidth = 135.5;
        userCol.setPrefWidth(colWidth);
        nameCol.setPrefWidth(colWidth);
        surnameCol.setPrefWidth(colWidth);
        mailCol.setPrefWidth(colWidth);
        gradeCol.setPrefWidth(colWidth);
        superCol.setPrefWidth(colWidth);
        userTable.getColumns().addAll(userCol, nameCol, surnameCol, mailCol, gradeCol, superCol);
        userTable.setItems(loadUser());
        userTable.setMinHeight(750);
        Button editBut = new Button("Edition");
        editBut.setStyle("-fx-font-size: 15");
        Button delBut = new Button("Supprimer");
        delBut.setStyle("-fx-font-size: 15");
        HBox butHBox = new HBox();
        butHBox.setAlignment(Pos.BASELINE_RIGHT);
        butHBox.setSpacing(20);
        butHBox.getChildren().addAll(editBut, delBut);
        globVBox.getChildren().addAll(labHBox, userTable, butHBox);
        
        editBut.setOnMouseClicked(e -> {
            Users selected = userTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                editUser(userTable.getSelectionModel().getSelectedItem());
                userTable.setItems(loadUser());
            }
        });

        delBut.setOnMouseClicked(e -> {
            Users selectedUser = userTable.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                try {
                    String selected = selectedUser.getUser();
                    BufferedReader reader = new BufferedReader(new FileReader(Params.basePath + "/Users/" + selected + "/Project.txt"));
                    String line;
                    while((line=reader.readLine()) != null) {
                        List<String> file = new ArrayList<>();
                        BufferedReader r = new BufferedReader(new FileReader(line + "/Associe.txt"));
                        String l;
                        while((l=r.readLine()) != null) {
                            if (!l.equals(selected)) {
                                file.add(l);
                            }
                        }
                        r.close();
                        BufferedWriter writer = new BufferedWriter(new FileWriter(line + "/Associe.txt"));
                        for(String j: file) {
                            writer.write(j + "\n");
                        }
                        writer.close();
                    }
                    reader.close();
                    Files.walk(Paths.get(Params.basePath + "/Users/" + selected)).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                userTable.setItems(loadUser());
            }
        });

        refreshBut.setOnMouseClicked(e -> {
            userTable.setItems(loadUser());
        });
        
        return globVBox;
    }

    private static void editUser(Users user) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edition utilisateur");
        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);
        gridPane.setVgap(10);
        VBox userVBox = new VBox();
        Label userLab = new Label("User:");
        TextField userField = new TextField(user.getUser());
        userVBox.setSpacing(5);
        userVBox.getChildren().addAll(userLab, userField);
        VBox nameVBox = new VBox();
        Label nameLab = new Label("Nom:");
        TextField nameField = new TextField(user.getName());
        nameVBox.setSpacing(5);
        nameVBox.getChildren().addAll(nameLab, nameField);
        VBox surnameVBox = new VBox();
        Label surnameLab = new Label("Prenom:");
        TextField surnameField = new TextField(user.getSurname());
        surnameVBox.setSpacing(5);
        surnameVBox.getChildren().addAll(surnameLab, surnameField);
        VBox gradVBox = new VBox();
        Label gradLab = new Label("Rôle:");
        ComboBox gradCombo = new ComboBox<>();
        gradCombo.setItems(FXCollections.observableArrayList(Params.roleList));
        gradCombo.setValue(user.getRole());
        gradVBox.setSpacing(5);
        gradVBox.getChildren().addAll(gradLab, gradCombo);
        VBox superVBox = new VBox();
        Label superLab = new Label("Superviseur");
        TextField superField = new TextField(user.getSupervisor());
        superVBox.setSpacing(5);
        superVBox.getChildren().addAll(superLab, superField);
        VBox mailVBox = new VBox();
        Label mailLab = new Label("Mail:");
        TextField mailField = new TextField(user.getMail());
        mailVBox.setSpacing(5);
        mailVBox.getChildren().addAll(mailLab, mailField);
        gridPane.add(nameVBox, 1, 1);
        gridPane.add(surnameVBox, 2, 1);
        gridPane.add(mailVBox, 3, 1);
        gridPane.add(userVBox, 1, 2);
        gridPane.add(gradVBox, 2,2);
        gridPane.add(superVBox, 3, 2);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.APPLY);
        dialog.getDialogPane().setContent(gridPane);
        Optional<ButtonType> option = dialog.showAndWait();

        if(option.get() == ButtonType.APPLY) {
            user.setName(nameField.getText());
            user.setSurname(surnameField.getText());
            user.setMail(mailField.getText());
            user.setUser(userField.getText());
            user.setRole(gradCombo.getValue().toString());
            user.setSupervisor(superField.getText());
            user.save();
        }
    }
    
    private static void accUser(Users user) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("User input");
        HBox globHBox = new HBox();
        globHBox.setSpacing(20);
        VBox gradVBox = new VBox();
        gradVBox.setSpacing(5);
        Label gradLab = new Label("Grade:");
        ComboBox gradCombo = new ComboBox<>(FXCollections.observableArrayList(Params.roleList));
        gradVBox.getChildren().addAll(gradLab, gradCombo);
        VBox superVBox = new VBox();
        superVBox.setSpacing(5);
        Label superLab = new Label("Superviseur:");
        TextField superField = new TextField();
        superVBox.getChildren().addAll(superLab, superField);
        globHBox.getChildren().addAll(gradVBox, superVBox);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(globHBox);
        Optional<ButtonType> option = dialog.showAndWait();

        if (option.get() == ButtonType.OK) {
            try {
                copyRecusrively(Params.basePath + "/PendingUsers/" + user.getUser(), Params.basePath + "/Users/" + user.getUser());
                BufferedWriter writer = new BufferedWriter(new FileWriter(Params.basePath + "/Users/" + user.getUser() + "/Info.txt", true));
                writer.write("grade#!#" + gradCombo.getValue().toString() + "\n");
                writer.write("supervisor#!#" + superField.getText());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }
    }

    public static void copyRecusrively(String source, String dest) {
        Path sourceDir = Paths.get(source);
        Path destinationDir = Paths.get(dest);
                // Traverse the file tree and copy each file/directory.
        try {
            Files.walk(sourceDir)
                    .forEach(sourcePath -> {
                        try {
                            Path targetPath = destinationDir.resolve(sourceDir.relativize(sourcePath));
                            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException ex) {}
                    });
            System.out.println(new File(source).getTotalSpace());
            System.out.println(new File(dest).getTotalSpace());
            if (new File(dest).getTotalSpace() == new File(source).getTotalSpace()) {
                Files.walk(Paths.get(source)).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return;
    }

    public static void copyRecusrivelyTxt(String source, String dest) {
        Path sourceDir = Paths.get(source);
        Path destinationDir = Paths.get(dest);
                // Traverse the file tree and copy each file/directory.
        try {
            Files.walk(sourceDir)
                    .forEach(sourcePath -> {
                        try {
                            if (sourcePath.getFileName().toString().split("\\.")[sourcePath.getFileName().toString().split("\\.").length - 1].equals("txt")) {
                                Path targetPath = destinationDir.resolve(sourceDir.relativize(sourcePath));
                                Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                            }
                        } catch (IOException ex) {}
                    });
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public static boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        directoryToBeDeleted.delete();
        return true;
    }
}
