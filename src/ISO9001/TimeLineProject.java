package ISO9001;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAmount;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

import javax.swing.Scrollable;

import org.apache.poi.ss.formula.functions.Days360;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;


import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import java.time.temporal.ChronoUnit;

public class TimeLineProject {
    
    public static SplitPane build() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        ScrollPane horinzontScrollPane = new ScrollPane();
        horinzontScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        SplitPane splitPane = new SplitPane();
        TilePane dateTile = new TilePane();
        LocalDate minDate = getFirstDate();
        LocalDate maxDate = getLastDate();
        long numDay = ChronoUnit.DAYS.between(minDate, maxDate);
        dateTile.setPrefColumns((int) numDay);
        VBox tileVBox = new VBox();
        VBox leftVBox = new VBox();
        leftVBox.setSpacing(4);
        leftVBox.setTranslateY(5);
        Label nameLab = new Label("Project:");
        nameLab.setPrefWidth(100);
        int dateCellNum = 0;
        int i = 0;
        Boolean checkWeek = false;
        while(i<numDay) {
            Pane r = new Pane();
            r.setPrefSize(40, 28);
            Label dateLab = new Label(minDate.plusDays(i).getMonthValue() + "/" + minDate.plusDays(i).getDayOfMonth());
            if (checkWeek) {
                r.setStyle("-fx-background-color: darkgrey; -fx-border-color: black; -fx-border-width: 1;");
                if (minDate.plusDays(i).getDayOfWeek().toString().equals("FRIDAY")) {
                    checkWeek = false;
                }
            }
             else if (minDate.plusDays(i).isEqual(LocalDate.now())) {
                r.setStyle("-fx-background-color: grey; -fx-border-color: black; -fx-border-width: 1;");
                dateCellNum = i;
                checkWeek = true;
            } else {
                r.setStyle("-fx-background-color: gainsboro; -fx-border-color: black; -fx-border-width: 1;");
            }
            dateLab.setAlignment(Pos.CENTER);
            r.getChildren().add(dateLab);
            dateTile.getChildren().add(r);
            i++;
        }
        Separator sepNameLab = new Separator();
        leftVBox.getChildren().addAll(nameLab, sepNameLab);
        tileVBox.getChildren().add(dateTile);
        Deque<Project> tmpList = new ArrayDeque<>();
        for(Project proj: User.projList) {
            if (proj.getPrio().equals("URGENCE")) {
                tmpList.addFirst(proj);
            }
        }
        List<Project> lstProj = User.projList;
        while (lstProj.size() > 0) {
            int min = 10000*10000*10000*10000;
            int idx = 0;
            for (i=0; i< lstProj.size(); i++) {
                if (Integer.parseInt(lstProj.get(i).getNproject()) < min && !lstProj.get(i).getPrio().equals("URGENCE")) {
                        min = Integer.parseInt(lstProj.get(i).getNproject());
                        idx = i;
                }
            }
            if (!lstProj.get(idx).getPrio().equals("URGENCE")) {
                tmpList.add(lstProj.get(idx));
            }
            lstProj.remove(idx);
        }
        for (Project p: tmpList) {
            String color = getLevel(p);
            Label projLab;
            try {
                projLab = new Label("P." + p.getNproject() + "\n" + p.getOwner());
                projLab.setFont(new javafx.scene.text.Font(9.0));
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                break;
            }
            if (p.getPrio().equals("URGENCE")) {
                projLab.setBackground(new Background(new BackgroundFill(Color.CRIMSON, CornerRadii.EMPTY, Insets.EMPTY)));
            }
            projLab.setOnMouseClicked(e -> {
                Browse.dialProj(p);
            });
            projLab.setPrefWidth(100);
            //projLab.setPadding(new Insets(10, 0, 0, 0));
            TilePane tilePane = new TilePane();
            tilePane.setPrefColumns((int) numDay);
            int start = (int) ChronoUnit.DAYS.between(minDate, LocalDate.parse(p.getDate()));
            int stop = (int) ChronoUnit.DAYS.between(minDate, LocalDate.parse(p.getDateFin()));
            int now = (int) ChronoUnit.DAYS.between(minDate, LocalDate.now());
            System.out.println("Start date:" + start + ", Stop Date: " + stop);
            String gradient = "";
            if (p.getReferent().equals("ILIE")) {
                gradient = "linear-gradient(from 0% 30% to 0% 40%, saddlebrown, " + color + ")";
            // fini = meduimaquamaine
            
            } else if (p.getReferent().equals("RAGEOT")) {
                gradient = "linear-gradient(from 0% 30% to 0% 40%, royalblue, " + color + ")";
            }
            for (i=0; i < numDay; i++) {
                String colorBorder = "black";
                String widthBorder = "0.5";
                if (i == dateCellNum) {
                    colorBorder = "black";
                    widthBorder = "1.0";
                }
                Region r = new Region();
                r.setPrefSize(40, 37);
                if (i >= start && i <= stop) {
                    r.setStyle("-fx-background-color: " + gradient + "; -fx-border-color: " + colorBorder + "; -fx-border-width: " + widthBorder + ";");
                } else if (i >= stop && now > stop && i <= now) {
                    r.setStyle("-fx-background-color: crimson; -fx-border-color: " + colorBorder + "; -fx-border-width: " + widthBorder + ";");
                } 
                else {
                    r.setStyle("-fx-background-color: white; -fx-border-color: " + colorBorder + "; -fx-border-width: " + widthBorder + ";");
                }
                tilePane.getChildren().add(r);
            }
            Separator sep = new Separator();
            leftVBox.getChildren().addAll(projLab, sep);
            tileVBox.getChildren().add(tilePane);
        }
        for(pendingReport p: User.pendingList) {
            System.out.println("pending: " + p.numProject);
            Label projLab = new Label("");
            try {
                projLab = new Label("P." + p.getNproject() + "\n" + p.getOwner());
                projLab.setFont(new javafx.scene.text.Font(9.0));
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                break;
            }
            projLab.setOnMouseClicked(e -> {
                dialogDate(p);
            });
            projLab.setPrefWidth(100);
            TilePane tilePane = new TilePane();
            tilePane.setPrefColumns((int) numDay);
            int start = (int) ChronoUnit.DAYS.between(minDate, LocalDate.parse(p.getDate()));
            int stop = (int) ChronoUnit.DAYS.between(minDate, LocalDate.parse(p.getDateFin()));
            int now = (int) ChronoUnit.DAYS.between(minDate, LocalDate.now());
            for (i=0; i < numDay; i++) {
                String colorBorder = "black";
                String widthBorder = "0.5";
                if (i == dateCellNum) {
                    colorBorder = "black";
                    widthBorder = "1.0";
                }
                Region r = new Region();
                r.setPrefSize(40, 37);
                r.setStyle("-fx-background-color: gold; -fx-border-color: " + colorBorder + "; -fx-border-width: " + widthBorder + ";");
                tilePane.getChildren().add(r);
            }
            Separator sep = new Separator();
            leftVBox.getChildren().addAll(projLab, sep);
            tileVBox.getChildren().add(tilePane);
        }
        leftVBox.setMinWidth(100);
        leftVBox.setMaxWidth(100);
        //horinzontScrollPane.prefWidthProperty().bind(tileVBox.widthProperty());
        horinzontScrollPane.setContent(tileVBox);
        float hValue = (float) ChronoUnit.DAYS.between(minDate, LocalDate.now()) /  (float) numDay;
        horinzontScrollPane.setHvalue(hValue);
        splitPane.getItems().addAll(leftVBox, horinzontScrollPane);
        //globHBox.getChildren().addAll(leftVBox, scrollPane);
        return splitPane;
    }

    private static void dialogDate(pendingReport p) {
        List<String> rtrn = new ArrayList<>();
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Date de rapport");
        dialog.setHeaderText("Changer la date de rapport:");
        VBox globVBox = new VBox();
        globVBox.setSpacing(10);
        DatePicker datePicker = new DatePicker();
        TextField comField = new TextField();
        globVBox.getChildren().addAll(datePicker, comField);
        datePicker.setValue(LocalDate.parse(p.getDateFin()));
        dialog.getDialogPane().setContent(globVBox);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> option = dialog.showAndWait();
    
        if (option.get() == ButtonType.OK) {
            Dialog<ButtonType> dial = new Dialog<>();
            dial.setTitle("Confirmation");
            dial.setHeaderText("Attention ce projet peut-être financé, êtes vous sûre de vouloir modifier ca date de rapport?");
            dial.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> op = dial.showAndWait();
            if (op.get() == ButtonType.YES) {
                p.setDateFin(datePicker.getValue().toString(), comField.getText());
            }
        }
    }


    private static LocalDate getFirstDate() {
        LocalDate min = LocalDate.parse("3000-01-01");
        for(Project p: User.projList) {
            if (LocalDate.parse(p.getDate()).isBefore(min)) {
                min = LocalDate.parse(p.getDate());
            } 
        }
        for (pendingReport p: User.pendingList) {
            if (LocalDate.parse(p.getDate()).isBefore(min)) {
                min = LocalDate.parse(p.getDate());
            }
        }
        return min;
    }

    private static LocalDate getLastDate() {
        LocalDate max = LocalDate.parse("2000-01-01");
        for (Project p: User.projList) {
            if (LocalDate.parse(p.getDateFin()).isAfter(max)) {
                max = LocalDate.parse(p.getDateFin());
            }
        }
        for (pendingReport p: User.pendingList) {
            System.out.println(p.getDateFin());
            if (LocalDate.parse(p.getDateFin()).isAfter(max)) {
                max = LocalDate.parse(p.getDateFin());
            }
        }
        return max;
    }

    private static String getLevel(Project proj) {
        LocalDate now = LocalDate.now();
        if (proj.listMarquage.size() > 0 && Browse.getNumberProcededMarquage(proj) == proj.listMarquage.size()) {
            if (proj.listGrossisement.size() == 0) {
                return "mediumaquamarine";
            }
            Boolean check = true;
            for (Boolean b: Browse.getProcededNumerisation(proj).values()) {
                if (b == false) {
                    check = false;
                    break;
                }
            }
            if (check) {
                return "mediumaquamarine";
            }
        }
        if (now.plusDays(7).isAfter(LocalDate.parse(proj.getDateFin()))) {
            return "indianred";
        } else {
            return "lightskyblue";
        }
    }

}
