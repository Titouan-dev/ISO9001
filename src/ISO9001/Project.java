package ISO9001;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.apache.commons.compress.compressors.lz4.BlockLZ4CompressorInputStream;
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.tool.Extension.Param;
import org.bouncycastle.util.encoders.BufferedDecoder;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Cell;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
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

public class Project {

    String name;
    String description;
    List<Associe> listAssocie = new ArrayList<>();
    List<Technique> listMarquage = new ArrayList<>();
    List<String> listGrossisement = new ArrayList<>();
    List<Boolean> listBleech = new ArrayList<>();
    List<String> listRev = new ArrayList<>();
    int epaisseurCoupe;
    int nombreCoupe;
    Boolean coupe;
    int echantillon;
    String espece;
    String nProject;
    Users owner;
    String path;
    Associe referent;
    Boolean scan;
    String date;
    String nEsp;
    String dateFin;
    String prio;
    Boolean copeaux;
    String nbCopeau;
    String epaisseurCopeaux;
    Boolean finance;
    String numeroFinance;

    public Project(String n, String d, List<Associe> a, List<Technique> m, String ec, String nc, Boolean c, String e, String es, String nP, Users owner, Associe referent, String dateFin, String prio, Boolean copeaux, String nbCopeaux, String epaisseurCopeaux, Boolean finance, String numeroFinance) {
        this.name = n;
        this.description = d;
        this.listAssocie = a;
        this.scan = false;
        this.finance = finance;
        System.out.println("list size: " + m.size());
        this.numeroFinance = numeroFinance;
        for(Technique mar: m) {
            if (mar instanceof Marquage) {
                Marquage marq = (Marquage)mar;
                if (!marq.getGrossisement().equals("NA")) {
                    this.scan = true;
                }
                this.listMarquage.add(marq);
                this.listGrossisement.add(marq.getGrossisement());
                this.listBleech.add(marq.getBleeched());
                if (marq.isCustom() || marq.isColo()) {
                    this.listRev.add(marq.getRev());
                } else {
                    this.listRev.add(null);
                }
            } else {
                if (!mar.getGrossisement().equals("NA")) {
                    this.scan = true;
                }
                this.listMarquage.add(mar);
                this.listGrossisement.add(mar.getGrossisement());
                this.listBleech.add(mar.getBleeched());
                this.listRev.add(null);
            }
        }   
        this.coupe = c;
        this.epaisseurCoupe = Integer.parseInt(ec);
        this.nombreCoupe = Integer.parseInt(nc);
        this.copeaux = copeaux;
        this.nbCopeau = nbCopeaux;
        this.epaisseurCopeaux = epaisseurCopeaux;
        this.echantillon = Integer.parseInt(e);
        this.espece = es;
        this.nProject = nP;
        this.owner = owner;
        this.referent = referent;
        if (this.espece.equals("Human")) {
            this.nEsp = "0";
        } else if (this.espece.equals("Souris")) {
            this.nEsp = "1";
        } else {
            this.nEsp = "2";
        }
        this.date = java.time.LocalDate.now().toString();
        this.dateFin = dateFin;
        this.prio = prio;
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
                updateMessage("Creation du projet...");
                updateProgress(25,100);
                save(Params.basePath + "/pendingProject/");
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

    public Project(String path_l) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path_l + "/Info.txt"));
            String line;
            while ((line=reader.readLine()) != null) {
                String[] splitLine = line.split("#!#");
                if (splitLine[0].equals("titre")) {
                    this.name = splitLine[1];
                }
                else if (splitLine[0].equals("description")) {
                    try {
                    if (!splitLine[1].equals("null") || !splitLine[1].equals("")) {
                        this.description = splitLine[1];
                        if (splitLine.length > 1) {
                            for(int i=2; i<splitLine.length; i++) {
                                this.description = this.description + "\r\n" + splitLine[i];
                            }
                        }
                    }
                    } catch (Exception e) {
                        this.description = "";
                    } 
                }
                else if (splitLine[0].equals("epaisseurCoupe")) {
                    this.epaisseurCoupe = Integer.parseInt(splitLine[1]);
                }
                else if (splitLine[0].equals("echantillon")) {
                    this.echantillon = Integer.parseInt(splitLine[1]);
                }
                else if (splitLine[0].equals("espece")) {
                    this.espece = splitLine[1];
                }
                else if (splitLine[0].equals("nProject")) {
                    this.nProject = splitLine[1];
                }
                else if (splitLine[0].equals("path")) {
                    this.path = splitLine[1];
                }
                else if (splitLine[0].equals("nombreCoupe")) {
                    this.nombreCoupe = Integer.parseInt(splitLine[1]);
                }
                else if (splitLine[0].equals("referent")) {
                    this.referent = new Associe(splitLine[1]);
                }
                else if (splitLine[0].equals("owner")) {
                    String[] info = splitLine[1].split("_");
                    this.owner = new Users(info[0], info[1], info[2]);
                }
                else if(splitLine[0].equals("nEsp")) {
                    this.nEsp= splitLine[1];
                }
                else if (splitLine[0].equals("scan")) {
                    if (splitLine[1].equals("true")) {
                        this.scan = true;
                    } else {
                        this.scan = false;
                    }
                }
                else if (splitLine[0].equals("date")) {
                    this.date = splitLine[1];
                }
                else if (splitLine[0].equals("dateFin")) {
                    this.dateFin = splitLine[1];
                }
                else if (splitLine[0].equals("prio")) {
                    this.prio = splitLine[1];
                }
                else if (splitLine[0].equals("copeaux")) {
                    if (splitLine[1].equals("true")) {
                        this.copeaux = true;
                    } else {
                        this.copeaux = false;
                    }
                }
                else if (splitLine[0].equals("nbCopeaux")) {
                    this.nbCopeau = splitLine[1];
                }
                else if (splitLine[0].equals("epaisseurCopeaux")) {
                    this.epaisseurCopeaux = splitLine[1];
                }
                else if (splitLine[0].equals("finance")) {
                    if (splitLine[1].equals("true")) {
                        this.finance = true;
                    } else {
                        this.finance = false;
                    }
                }
                else if (splitLine[0].equals("numeroFinance")) {
                    if (this.finance) {
                        numeroFinance = splitLine[1];
                    } else {
                        numeroFinance = "null";
                    }
                }
            }
            if (this.nombreCoupe > 0) {
                this.coupe = true;
            }
            else {
                this.coupe = false;
            }
            reader.close();
            reader = new BufferedReader(new FileReader(path + "/Marquage.txt"));
            System.out.println(path);
            while((line=reader.readLine()) != null) {
                String[] splitLine = line.split("#!#");
                if (splitLine[0].contains("$")) {
                    Multiplex m = new Multiplex(splitLine[0]);
                    m.setGrossisement(splitLine[1]);
                    m.setRevelation(splitLine[2]);
                    listMarquage.add(m);
                } else {
                    Marquage m = new Marquage(splitLine[0]);
                    m.setGrossisement(splitLine[1]);
                    m.setRevelation(splitLine[2]);
                    listMarquage.add(m);
                }
                listGrossisement.add(splitLine[1]);
                listRev.add(splitLine[2]);
                if (splitLine[3].equals("true")) {
                    listBleech.add(true);
                } else {
                    listBleech.add(false);
                }
            }
            reader.close();
            reader = new BufferedReader(new FileReader(path + "/Associe.txt"));
            while((line=reader.readLine()) != null) {
                String[] splitLine = line.split("#!#");
                BufferedReader r;
                try {
                    r = new BufferedReader(new FileReader(Params.basePath + "/Users/" + splitLine[0] + "/Info.txt"));
                } catch (Exception e) {
                    r = new BufferedReader(new FileReader(Params.basePath + "/PendingUsers/" + splitLine[0] + "/Info.txt"));
                }
                String l;
                String username;
                String nom = null;
                String prenom = null;
                String mail = null;
                String role;
                while((l=r.readLine()) != null) {
                    String[] splitL = l.split("#!#");
                    if (splitL[0].equals("username")) {
                        username = splitL[1];
                    } else if (splitL[0].equals("nom")) {
                        nom = splitL[1];
                    } else if (splitL[0].equals("prenom")) {
                        prenom = splitL[1];
                    } else if (splitL[0].equals("mail")) {
                        mail = splitL[1];
                    }
                }
                r.close();
                if(nom != null && prenom != null && mail != null) {
                    this.listAssocie.add(new Associe(nom, prenom, mail));
                }
            }
            reader.close();
            //referent

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(String path) {
        this.path = path + "P." + this.nProject;
        System.out.println("Save start");
        // Info
        try {
            new File(this.path).mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.path + "/Info.txt"));
            writer.write("nProject#!#" + this.nProject + "\n");
            writer.write("titre#!#" + this.name + "\n");
            String description = "";
            for(String line: this.description.split("\r?\n|\r")) {
                description = description + "#!#" + line;
            }
            writer.write("description" + description + "\n");
            writer.write("epaisseurCoupe#!#" + this.epaisseurCoupe + "\n");
            writer.write("nombreCoupe#!#" + this.nombreCoupe + "\n");
            writer.write("coupe#!#" + this.coupe + "\n");
            writer.write("echantillon#!#" + this.echantillon + "\n");
            writer.write("espece#!#" + this.espece + "\n");
            writer.write("path#!#" + this.path + "\n");
            if (this.referent != null) {
                writer.write("referent#!#" + this.referent.getUsername() + "\n");
            }
            writer.write("scan#!#" + this.scan + "\n");
            writer.write("owner#!#" + this.owner.getName() + "_" + this.owner.getSurname() + "_" + this.owner.getMail() + "\n");
            writer.write("nEsp#!#" + this.nEsp + "\n");
            writer.write("date#!#" + this.date + "\n");
            writer.write("dateFin#!#" +this.dateFin + "\n");
            writer.write("prio#!#" + this.prio + "\n");
            writer.write("copeaux#!#" + this.copeaux + "\n");
            writer.write("nbCopeaux#!#" + this.nbCopeau + "\n");
            writer.write("epaisseurCopeaux#!#" + this.epaisseurCopeaux + "\n");
            writer.write("finance#!#" + this.finance + "\n");
            writer.write("numeroFinance#!#" + this.numeroFinance + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Technique t: listMarquage) {
            if (t instanceof Marquage) {
                Marquage m = (Marquage)t;
                if (m.isCustom()) {
                    new File(this.path + "/MarquageCustom/").mkdir();
                    m.saveCustom(this.path + "/MarquageCustom/");
                }
            }
        }
        // Associe
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.path + "/Associe.txt"));
            for(Associe as: listAssocie) {
                writer.write(as.getUsername() + "#!#" + as.getName() + "#!#" + as.getPrenom() +  "\n");
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Owner
        //try {
        //    BufferedWriter writer = new BufferedWriter(new FileWriter(Params.basePath + "/Users/" + owner.username + "/Project.txt", true));
        //    writer.write(this.path + "#!#owner\n");
        //    writer.close();
        //    removeDuplicate(Params.basePath + "/Users/" + owner.username + "/Project.txt");
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}
        //for(Associe a: this.listAssocie) {
        //    String username = a.getUsername();
        //    if(!checkIfUserExists(username)) {
        //        if (dialogUser(username)) {
        //            User.pendingUser(a.getUsername(), a.getName(), a.getPrenom(), a.getMail());
        //            try {
        //                BufferedWriter writer = new BufferedWriter(new FileWriter(Params.basePath + "/PendingUsers/" + username + "/Project.txt", true));
        //                writer.write(Params.basePath + "/Project/P." + this.nProject + "\n");
        //                writer.close();
        //            } catch (IOException e) {
        //                e.printStackTrace();
        //            }
        //        }
        //        else {
        //            continue;
        //        }
        //    } else {
        //        try {
        //            BufferedWriter writer = new BufferedWriter(new FileWriter(Params.basePath + "/Users/" + username + "/Project.txt", true));
        //            writer.write(Params.basePath + "/Project/P." + this.nProject + "\n");
        //            writer.close();
        //            removeDuplicate(Params.basePath + "/Users/" + username + "/Project.txt");
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //        }
        //    }
        //}
        // Marquage
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.path + "/Marquage.txt"));
            for(int c=0; c<listMarquage.size(); c++) {
                writer.write(listMarquage.get(c).getPath().replace("\\", "/") + "#!#" + listGrossisement.get(c) + "#!#" + listRev.get(c) + "#!#" + listBleech.get(c) + "\n");
                System.out.println(listMarquage.get(c).getPath() + "#!#" + listGrossisement.get(c) + "#!#" + listRev.get(c) + "#!#" + listBleech.get(c) + "\n");
                System.out.println(this.path);
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Create archi
        new File(this.path + "/Scan").mkdir();
        for(int i=0; i<listMarquage.size(); i++) {
            if (listMarquage.get(i) instanceof Marquage) {
                Marquage marq = (Marquage)listMarquage.get(i);
                String grossisement = listGrossisement.get(i);
                new File(this.path + "/Scan/" + marq.getName()  + "_" + marq.getNum() + "_" + marq.getEspeceCible()).mkdir();
            } else {
                Multiplex marq = (Multiplex)listMarquage.get(i);
                String grossisement = listGrossisement.get(i);
                new File(this.path + "/Scan/" + marq.getName()).mkdir();
            }
        }
        new File(this.path + "/Echantillon").mkdir();
        new File(this.path + "/Resultat").mkdir();
        new File(this.path + "/Script").mkdir();
        new File(this.path + "/Log").mkdir();
        new File(this.path + "/SuiviTK").mkdir();
        new File(this.path + "/SuiviTK/").mkdir();
        new File(this.path + "/SuiviTK/Numerisation/").mkdirs();
        try {
            if (!new File(this.path + "/Echange.txt").exists()) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(this.path + "/Echange.txt"));
                writer.write("[System]:\tNote initialisé\n");
                writer.close();
            }
        } catch (IOException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        // pdf
        try {
            if (!new File(this.path + "/Echantillon/Pivot.xlsx").exists()) {
                Files.copy(Paths.get(Params.basePath + "/Template/Echantillons/Pivot.xlsx"), Paths.get(this.path + "/Echantillon/Pivot.xlsx"), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        // echantillon
        try  {
            if (!new File(this.path + "/Echantillon/sample_list.xlsx").exists()) {
                FileInputStream inputStream = new FileInputStream(new File(Params.basePath + "/Template/Echantillons/sample_list.xlsx"));
                XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
                org.apache.poi.ss.usermodel.Cell cell = sheet.getRow(2).getCell(0);
                cell.setCellValue("Project P." + this.nProject + ":");
                cell = sheet.getRow(4).getCell(0);
                cell.setCellValue("Demandeur: " + this.owner.getName() + " " + this.owner.getSurname());
                inputStream.close();
                FileOutputStream ouputStream = new FileOutputStream(this.path + "/Echantillon/sample_list.xlsx");
                workbook.write(ouputStream);
                workbook.close();
                ouputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Marquage / Numerisation
        new File(this.path + "/SuiviTK/Marquage").mkdir();
        try {
            for(int i=0; i<listMarquage.size(); i++) {
                if (listMarquage.get(i) instanceof Marquage) {
                    Marquage marq = (Marquage)listMarquage.get(i);
                    FileInputStream inputStream = new FileInputStream(new File(Params.basePath + "/Template/Suivi_TK/Marquage.xlsx"));
                    System.out.println("MARQUAAAAAAGE " + marq.getName());
                    XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                    XSSFCellStyle defaultStyle = workbook.createCellStyle();
                    defaultStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(221,235,247), null));
                    defaultStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    defaultStyle.setAlignment(HorizontalAlignment.LEFT);
                    XSSFCellStyle rightStyle = workbook.createCellStyle();
                    rightStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(221,235,247), null));
                    rightStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    rightStyle.setAlignment(HorizontalAlignment.RIGHT);
                    org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
                    sheet.getRow(2).createCell(1).setCellValue(this.nProject);
                    sheet.getRow(2).createCell(3).setCellValue(this.getOwner());
                    sheet.getRow(4).createCell(1).setCellValue(marq.getName());
                    sheet.getRow(5).createCell(1).setCellValue(marq.getNum());
                    sheet.getRow(6).createCell(1).setCellValue(marq.getSystemVisu());
                    if (marq.isColo() == false && marq.isCustom() == false) {
                        sheet.getRow(9).createCell(1).setCellValue(this.echantillon * 0.3);
                        sheet.getRow(9).getCell(1).setCellStyle(rightStyle);
                        double mort = 0;
                        if (this.echantillon <= 14) {
                            sheet.getRow(10).createCell(1).setCellValue("0.6");
                            mort = 0.6;
                        } else if (this.echantillon <= 35) {
                            sheet.getRow(10).createCell(1).setCellValue("1.2");
                            mort = 1.2;
                        } else if (this.echantillon <= 77) {
                            sheet.getRow(10).createCell(1).setCellValue("1.6");
                            mort = 1.6;
                        } else if (this.echantillon <= 158) {
                            sheet.getRow(10).createCell(1).setCellValue("2.3");
                            mort = 2.3;
                        }
                        sheet.getRow(10).getCell(1).setCellStyle(rightStyle);
                        sheet.getRow(11).createCell(1).setCellValue(this.echantillon * 0.3 + mort);
                        sheet.getRow(11).getCell(1).setCellStyle(rightStyle);
                        sheet.getRow(13).createCell(4).setCellValue(Excel_util.getDilution(Integer.parseInt(marq.getNum())));
                        sheet.getRow(13).getCell(4).setCellStyle(defaultStyle);
                        sheet.getRow(13).createCell(1).setCellValue((this.echantillon*0.3 + mort) / Excel_util.getDilution(Integer.parseInt(marq.getNum()))*1000);
                        sheet.getRow(13).getCell(1).setCellStyle(rightStyle);
                    }
                    int pic_id = workbook.addPicture(QRCode.create(this.path + "/SuiviTK/Marquage/Marquage_" + marq.getName() + "_" + marq.getNum() + "_" + this.espece + "_" + marq.getFourniseur() + "_" + marq.getReference() + ".pdf"), Workbook.PICTURE_TYPE_JPEG);
                    XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
                    XSSFClientAnchor anchor = new XSSFClientAnchor();
                    anchor.setCol1(4);
                    anchor.setRow1(1);
                    anchor.setCol2(5);
                    anchor.setRow2(7);
                    XSSFPicture pic = drawing.createPicture(anchor, pic_id);
                    inputStream.close();
                    FileOutputStream outputStream = new FileOutputStream(this.path + "/SuiviTK/Marquage/Marquage_" + marq.getName() + "_" + marq.getNum() + "_" + this.espece + "_" + marq.getFourniseur() + "_" + marq.getReference() + ".xlsx");
                    workbook.write(outputStream);
                    workbook.close();
                    outputStream.close();
                } else {
                    Multiplex multi = (Multiplex)listMarquage.get(i);
                    FileInputStream inputStream = new FileInputStream(new File(Params.basePath + "/Template/Suivi_TK/Marquage_multiplex.xlsx"));
                    XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                    for (int j=0; j<multi.marquageList.size(); j++) {
                        Marquage marq = (Marquage)multi.marquageList.get(j);
                        XSSFCellStyle defaultStyle = workbook.createCellStyle();
                        defaultStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(221,235,247), null));
                        defaultStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        defaultStyle.setAlignment(HorizontalAlignment.LEFT);
                        XSSFCellStyle rightStyle = workbook.createCellStyle();
                        rightStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(221,235,247), null));
                        rightStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        rightStyle.setAlignment(HorizontalAlignment.RIGHT);
                        org.apache.poi.ss.usermodel.Sheet sheet = workbook.cloneSheet(0, marq.getName());
                        sheet.getRow(2).createCell(1).setCellValue(this.nProject);
                        sheet.getRow(2).createCell(3).setCellValue(this.getOwner());
                        sheet.getRow(4).createCell(1).setCellValue(marq.getName());
                        sheet.getRow(5).createCell(1).setCellValue(multi.getNum());
                        sheet.getRow(6).createCell(1).setCellValue(marq.getSystemVisu().replace(marq.getNum(), multi.getNum()));
                        if (marq.isColo() == false && marq.isCustom() == false) {
                            sheet.getRow(9).createCell(1).setCellValue(this.echantillon * 0.3);
                            sheet.getRow(9).getCell(1).setCellStyle(rightStyle);
                            double mort = 0;
                            if (this.echantillon <= 14) {
                                sheet.getRow(10).createCell(1).setCellValue("0.6");
                                mort = 0.6;
                            } else if (this.echantillon <= 35) {
                                sheet.getRow(10).createCell(1).setCellValue("1.2");
                                mort = 1.2;
                            } else if (this.echantillon <= 77) {
                                sheet.getRow(10).createCell(1).setCellValue("1.6");
                                mort = 1.6;
                            } else if (this.echantillon <= 158) {
                                sheet.getRow(10).createCell(1).setCellValue("2.3");
                                mort = 2.3;
                            }
                            sheet.getRow(10).getCell(1).setCellStyle(rightStyle);
                            sheet.getRow(11).createCell(1).setCellValue(this.echantillon * 0.3 + mort);
                            sheet.getRow(11).getCell(1).setCellStyle(rightStyle);
                            sheet.getRow(13).createCell(4).setCellValue(Excel_util.getDilution(Integer.parseInt(marq.getNum())));
                            sheet.getRow(13).getCell(4).setCellStyle(defaultStyle);
                            sheet.getRow(13).createCell(1).setCellValue((this.echantillon*0.3 + mort) / Excel_util.getDilution(Integer.parseInt(marq.getNum()))*1000);
                            sheet.getRow(13).getCell(1).setCellStyle(rightStyle);
                        }
                        int pic_id = workbook.addPicture(QRCode.create(this.path + "/SuiviTK/Marquage/Marquage_" + multi.getNum() + "$_" + marq.getName() + "_" + marq.getNum() + "_" + this.espece + "_" + marq.getFourniseur() + "_" + marq.getReference() + ".pdf"), Workbook.PICTURE_TYPE_JPEG);
                        XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
                        XSSFClientAnchor anchor = new XSSFClientAnchor();
                        anchor.setCol1(4);
                        anchor.setRow1(1);
                        anchor.setCol2(5);
                        anchor.setRow2(7);
                        XSSFPicture pic = drawing.createPicture(anchor, pic_id);
                    }
                    workbook.removeSheetAt(0);
                    inputStream.close();
                    FileOutputStream outputStream = new FileOutputStream(this.path + "/SuiviTK/Marquage/Marquage_" + multi.getName() +".xlsx");

                    workbook.write(outputStream);
                    workbook.close();
                    outputStream.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (this.coupe) {
            try {
                FileInputStream inputStream = new FileInputStream(new File(Params.basePath + "/Template/Suivi_TK/Coupe.xlsx"));
                XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
                sheet.getRow(2).createCell(1).setCellValue(String.valueOf(this.nProject));
                sheet.getRow(4).createCell(1).setCellValue(this.owner.getName() + " " + this.owner.getSurname());
                sheet.getRow(6).createCell(1).setCellValue(this.nombreCoupe);
                sheet.getRow(7).createCell(1).setCellValue(this.epaisseurCoupe);
                createQRCode(workbook, sheet, this.path + "/SuiviTK/Coupe.pdf", 3, 4, 3, 9);
                inputStream.close();
                FileOutputStream outputStream = new FileOutputStream(this.path + "/SuiviTK/Coupe.xlsx");
                workbook.write(outputStream);
                workbook.close();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (this.copeaux) {
            try {
                FileInputStream inputStream = new FileInputStream(new File(Params.basePath + "/Template/Suivi_TK/Copeaux.xlsx"));
                XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
                sheet.getRow(2).createCell(1).setCellValue(String.valueOf(this.nProject));
                sheet.getRow(4).createCell(1).setCellValue(this.owner.getName() + " " + this.owner.getSurname());
                sheet.getRow(6).createCell(1).setCellValue(this.nbCopeau);
                sheet.getRow(7).createCell(1).setCellValue(this.epaisseurCopeaux);
                createQRCode(workbook, sheet, this.path + "/SuiviTK/Coupe.pdf", 3, 4, 3, 9);
                inputStream.close();
                FileOutputStream outputStream = new FileOutputStream(this.path + "/SuiviTK/Coupe.xlsx");
                workbook.write(outputStream);
                workbook.close();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
         try {
            for(int i=0; i<listMarquage.size(); i++) {
                if(listMarquage.get(i) instanceof Marquage) {
                    String gross = listGrossisement.get(i);
                    Marquage marq = (Marquage)listMarquage.get(i);
                    if (marq.isColo() || marq.isCustom()) {
                        if (!gross.equals("NA")) {
                            new File(this.path + "/SuiviTK/Numerisation/").mkdirs();
                            FileInputStream inputStream = new FileInputStream(new File(Params.basePath + "/Template/Suivi_TK/Numerisation_VS200.xlsx"));
                            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                            org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
                            inputStream.close();
                            FileOutputStream outputStream = new FileOutputStream(this.path + "/SuiviTK/Numerisation/Numerisation_" + marq.getName() + "_" + marq.getNum() + "_" + listRev.get(i) + "_" + gross + "_" + this.espece + "_" + marq.getFourniseur() + "_" + marq.getReference() + ".xlsx");
                            workbook.write(outputStream);
                            workbook.close();
                            outputStream.close();
                        }
                    } else {
                        if (!gross.equals("NA")) {
                            new File(this.path + "/SuiviTK/Numerisation/").mkdirs();
                            FileInputStream inputStream = new FileInputStream(new File(Params.basePath + "/Template/Suivi_TK/Numerisation_VS200.xlsx"));
                            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                            org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
                            inputStream.close();
                            FileOutputStream outputStream = new FileOutputStream(this.path + "/SuiviTK/Numerisation/Numerisation_" + marq.getName() + "_" + marq.getNum() + "_" + gross + "_" + this.espece + "_" + marq.getFourniseur() + "_" + marq.getReference() + ".xlsx");
                            workbook.write(outputStream);
                            workbook.close();
                            outputStream.close();
                        }
                    }
                } else {
                    String gross = listGrossisement.get(i);
                    Multiplex marq = (Multiplex)listMarquage.get(i);
                    new File(this.path + "/SuiviTK/Numerisation/").mkdirs();
                    FileInputStream inputStream = new FileInputStream(new File(Params.basePath + "/Template/Suivi_TK/Numerisation_VS200.xlsx"));
                    XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                    org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
                    inputStream.close();
                    FileOutputStream outputStream = new FileOutputStream(this.path + "/SuiviTK/Numerisation/Numerisation_" + marq.getName() + "_" + marq.getNum() + "_" + gross + "_" + this.espece + ".xlsx");
                    workbook.write(outputStream);
                    workbook.close();
                    outputStream.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Permissions.updateProj(this);
        String qrMessage = "Project: P." + this.nProject + "\n" + "Titre: " + this.name + "\n" + "Demandeur: " + this.owner.getName() + "\n" + "Date: " + this.date + "\nMarquage:\n";
        for (Technique t: this.listMarquage) {
            if (t instanceof Marquage) {
                Marquage m = (Marquage)t;
                qrMessage = qrMessage + " - " + m.getName() + "_" + m.getEspeceCible() + "_" + m.getFourniseur() + "_" + m.getReference() + "\n";
            } else {
                Multiplex m = (Multiplex)t;
                qrMessage = qrMessage + " - " + m.getName() + "_" + m.getEspeceCible() + "\n";
            }
        }
        byte[] qrCode = QRCode.createEtiquette(qrMessage);
        ByteArrayInputStream bis = new ByteArrayInputStream(qrCode);
        BufferedImage bImage2;
        try {
            bImage2 = ImageIO.read(bis);
            ImageIO.write(bImage2, "jpg", new File(this.getPath() + "/Etiquette.jpg"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Save finished");
    }

    private static void removeDuplicate(String path) {
        List<String> file = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line;
            while((line=reader.readLine()) != null) {
                if (!file.contains(line.replace("\n", ""))) {
                    file.add(line.replace("\n", ""));
                }
            }
            reader.close();
            BufferedWriter writer = new BufferedWriter(new FileWriter(path, false));
            for (String l: file) {
                writer.write(l + "\n");
            }
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static boolean checkIfUserExists(String u) {
        File[] listUser = new File(Params.basePath + "/Users").listFiles();
        for(File f: listUser) {
            String[] userSplit = f.toString().split("\\\\");
            String user = userSplit[userSplit.length - 1];
            if(user.equals(u)) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkIfProjExists(int n) {
        File[] listProj = new File(Params.basePath + "/Project").listFiles();
        for(File f: listProj) {
            String[] projSplit = f.toString().split("\\\\");
            String proj = projSplit[projSplit.length - 1];
            if (proj.replace("P.", "").equals(String.valueOf(n))) {
                return true;
            }
        }
        return false;
    }

    private static boolean dialogUser(String username) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Creation Utilisateur");
        alert.setHeaderText(username + " n'existe pas.");
        alert.setContentText("Voulez-vous le crée?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        }
        else {
            return false;
        }
    }

    private void setBorder(int fromX, int toX, int fromY, int toY, org.apache.poi.ss.usermodel.Sheet sheet) {
        for(int x=fromX; x<toX; x++) {
            RegionUtil.setBorderBottom(BorderStyle.MEDIUM, new CellRangeAddress(x,x+1,fromY,toY), sheet);
            RegionUtil.setBorderLeft(BorderStyle.MEDIUM, new CellRangeAddress(x,x+1,fromY,toY), sheet);
            RegionUtil.setBorderRight(BorderStyle.MEDIUM, new CellRangeAddress(x,x+1,fromY,toY), sheet);
            RegionUtil.setBorderTop(BorderStyle.MEDIUM, new CellRangeAddress(x,x+1,fromY,toY), sheet);
        }
        for(int y=fromY; y<toY; y++) {
            RegionUtil.setBorderBottom(BorderStyle.MEDIUM, new CellRangeAddress(fromX,toX,y,y+1), sheet);
            RegionUtil.setBorderLeft(BorderStyle.MEDIUM, new CellRangeAddress(fromX,toX,y,y+1), sheet);
            RegionUtil.setBorderRight(BorderStyle.MEDIUM, new CellRangeAddress(fromX,toX,y,y+1), sheet);
            RegionUtil.setBorderTop(BorderStyle.MEDIUM, new CellRangeAddress(fromX,toX,y,y+1), sheet);
        }
    }

    private static void createQRCode(XSSFWorkbook workbook, org.apache.poi.ss.usermodel.Sheet sheet, String path, int col1, int col2, int row1, int row2) {
        int pic_id = workbook.addPicture(QRCode.create(path), Workbook.PICTURE_TYPE_JPEG);
        XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = new XSSFClientAnchor();
        anchor.setCol1(col1);
        anchor.setRow1(row1);
        anchor.setCol2(col2);
        anchor.setRow2(row2);
        XSSFPicture pic = drawing.createPicture(anchor, pic_id);
    }

    public static void aprobedProj(Project proj) {
        try {
            if (proj.owner.getUser() != null) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(Params.basePath + "/Users/" + proj.owner.getUser() + "/Project.txt", true));
                writer.write(Params.basePath + "/Project/P." + proj.nProject + "#!#owner\n");
                writer.close();
            }
            removeDuplicate(Params.basePath + "/Users/" + proj.owner.getUser() + "/Project.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(Associe a: proj.listAssocie) {
            String username = a.getUsername();
            if(!checkIfUserExists(username)) {
                if (dialogUser(username)) {
                    User.pendingUser(a.getUsername(), a.getName(), a.getPrenom(), a.getMail());
                    try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter(Params.basePath + "/PendingUsers/" + username + "/Project.txt", true));
                        writer.write(Params.basePath + "/Project/P." + proj.nProject + "\n");
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    continue;
                }
            } else {
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(Params.basePath + "/Users/" + username + "/Project.txt", true));
                    writer.write(Params.basePath + "/Project/P." + proj.nProject + "\n");
                    writer.close();
                    removeDuplicate(Params.basePath + "/Users/" + username + "/Project.txt");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getPath() {
        return this.path;
    }

    public String getNproject() {
        return this.nProject;
    }

    public String getOwner() throws Exception{
        return this.owner.name.get();
    }

    public Users getOwnerObj() {
        return this.owner;
    }

    public String getEsp() {
        return this.espece;
    }

    public String getReferent() {
        return this.referent.getName();
    }

    public String getTitle() {
        return this.name;
    }

    public String getNumber() {
        return this.nProject;
    }

    public String getDate() {
        return this.date;
    }

    public String getNombreCoupe() {
        return String.valueOf(this.nombreCoupe);
    }

    public String getEpaisseur() {
        return String.valueOf(this.epaisseurCoupe);
    }

    public String getNEsp() {
        return this.nEsp;
    }

    public String getEchantillon() {
        return String.valueOf(this.echantillon);
    }

    public String getCoupe() {
        if (this.coupe) {
            return "Oui";
        } else {
            return "Non";
        }
    }

    public String getDescription() {
        return this.description;
    }

    //public String getMarquage() {
    //    String str = "";
    //    for(Marquage m: this.listMarquage) {
    //        str = str + m.name + ", ";
    //    }
    //    return str;
    //}

    public String getDateFin() {
        return this.dateFin;
    }

    public String getPrio() {
        return this.prio;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setReferent(String ref) {
        this.referent = new Associe(ref);
    }

    public void setTitle(String title) {
        this.name = title;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }
}
