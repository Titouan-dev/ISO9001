package ISO9001;
import java.awt.image.BufferedImage;
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
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.text.StyledEditorKit.ForegroundAction;

import org.apache.commons.collections4.ListValuedMap;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbookFactory;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.tool.Extension.Param;

public class Excel_util {

    private static void putChecKBox(XSSFWorkbook workbook, Sheet sheet, int x, int y) {
        BufferedImage image;
        try {
            image = ImageIO.read(new File("ISO9001/File/checkBox.jpg"));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            byte[] data = baos.toByteArray();
            int pic_id = workbook.addPicture(data, Workbook.PICTURE_TYPE_JPEG);
            XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
            XSSFClientAnchor anchor = new XSSFClientAnchor();
            anchor.setCol1(y);
            anchor.setRow1(x);
            anchor.setCol2(y+1);
            anchor.setRow2(x+1);
            XSSFPicture pic = drawing.createPicture(anchor, pic_id);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //public static void ficheProj(Project proj) {
    //    try {
    //        FileInputStream inputStream = new FileInputStream(new File(Params.basePath + "/Template/Suivi_TK/Fiche_projet.xlsx"));
    //        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
    //        Sheet sheet = workbook.getSheetAt(0);
    //        XSSFCellStyle nProjectCellStyle = workbook.createCellStyle();
    //        XSSFCellStyle defaultStyle = workbook.createCellStyle();
    //        XSSFFont font = workbook.createFont();
    //        font.setBold(true);
    //        //defaultStyle.setAlignment(HorizontalAlignment.LEFT);
    //        defaultStyle.setFont(font);
    //        nProjectCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(221,235,247), null));
    //        nProjectCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    //        nProjectCellStyle.setFont(font);
    //        sheet.getRow(1).createCell(2).setCellValue(proj.getNproject());
    //        sheet.getRow(1).getCell(2).setCellStyle(nProjectCellStyle);
    //        sheet.getRow(4).createCell(1).setCellValue(proj.getOwner());
    //        sheet.getRow(4).getCell(1).setCellStyle(defaultStyle);
    //        sheet.getRow(4).createCell(1).setCellValue(proj.getOwner());
    //        sheet.getRow(4).getCell(1).setCellStyle(defaultStyle);
    //        for (int i=0; i<proj.listAssocie.size(); i++) {
    //            sheet.getRow(5+i).createCell(1).setCellValue(proj.listAssocie.get(i).getName() + " " + proj.listAssocie.get(i).getPrenom());
    //            sheet.getRow(5+i).getCell(1).setCellStyle(defaultStyle);
    //        }
    //        sheet.getRow(9).createCell(1).setCellValue(proj.getTitle());
    //        sheet.getRow(9).getCell(1).setCellStyle(defaultStyle);
    //        sheet.getRow(11).createCell(1).setCellValue(proj.getEsp());
    //        sheet.getRow(11).getCell(1).setCellStyle(defaultStyle);
    //        sheet.getRow(15).createCell(1).setCellValue(proj.getDate());
    //        sheet.getRow(15).getCell(1).setCellStyle(defaultStyle);
    //        sheet.getRow(11).createCell(4).setCellValue(proj.getEchantillon());
    //        sheet.getRow(11).getCell(4).setCellStyle(defaultStyle);
    //        sheet.getRow(15).createCell(4).setCellValue(proj.getReferent());
    //        sheet.getRow(15).getCell(4).setCellStyle(defaultStyle);
    //        if (proj.coupe) {
    //            sheet.getRow(21).createCell(1).setCellValue("Oui");
    //            sheet.getRow(21).getCell(1).setCellStyle(defaultStyle);
    //            sheet.getRow(22).createCell(1).setCellValue(proj.nombreCoupe);
    //            sheet.getRow(22).getCell(1).setCellStyle(defaultStyle);
    //            sheet.getRow(23).createCell(1).setCellValue(proj.epaisseurCoupe);
    //            sheet.getRow(23).getCell(1).setCellStyle(defaultStyle);
    //            putChecKBox(workbook, sheet, 21, 2);
    //        } else {
    //            sheet.getRow(21).createCell(1).setCellValue("Non");
    //            sheet.getRow(21).getCell(1).setCellStyle(defaultStyle);
    //            sheet.getRow(22).createCell(1).setCellValue("0");
    //            sheet.getRow(22).getCell(1).setCellStyle(defaultStyle);
    //            sheet.getRow(23).createCell(1).setCellValue("0");
    //            sheet.getRow(23).getCell(1).setCellStyle(defaultStyle);
    //        }
    //        for (int i=0; i<proj.listMarquage.size(); i++) {
    //            sheet.createRow(27+i).createCell(0).setCellValue(proj.listMarquage.get(i).getFullName());
    //            sheet.getRow(27+i).getCell(0).setCellStyle(defaultStyle);
    //            sheet.getRow(27+i).createCell(1).setCellValue(proj.listMarquage.get(i).getNum());
    //            sheet.getRow(27+i).getCell(1).setCellStyle(defaultStyle);
    //            if (!proj.listGrossisement.get(i).equals("NA")) {
    //                sheet.getRow(27+i).createCell(3).setCellValue(proj.listMarquage.get(i).getSystemVisu() + " (" + proj.listGrossisement.get(i).toString() + ")");
    //                sheet.getRow(27+i).getCell(3).setCellStyle(defaultStyle);
    //            }
    //        }
    //        inputStream.close();
    //        OutputStream outputStream = new FileOutputStream(new File(proj.getPath() + "/SuiviTK/Fiche_project.xlsx"));
    //        workbook.write(outputStream);
    //        workbook.close();
    //        outputStream.close();
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //    }
    //}

    public static void csv(String path) {
        try {
            FileInputStream inputStream = new FileInputStream(new File(path));
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            int nbLigne = sheet.getLastRowNum();
            List<String> slideList = new ArrayList<>();
            for(int i=1; i < nbLigne; i++) {
                String str = String.valueOf(sheet.getRow(i).getCell(0).getNumericCellValue()).replace(".0", "") + ";" + String.valueOf(sheet.getRow(i).getCell(1).getNumericCellValue()).replace(".0", "");
                if (sheet.getRow(i).getCell(2) != null) {
                    str = str + ";" + sheet.getRow(i).getCell(2).getStringCellValue();
                } else {
                    str = str + ";";
                }
                if (sheet.getRow(i).getCell(3) != null) {
                    str = str + ";" + sheet.getRow(i).getCell(3).getStringCellValue();
                } else {
                    str = str + ";";
                }
                if (sheet.getRow(i).getCell(4) != null) {
                    str = str + ";" + sheet.getRow(i).getCell(4).getStringCellValue();
                } else {
                    str = str + ";";
                }
                slideList.add(str);
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(path.replace("xlsx", "csv")));
            writer.write("Tray No.;Slide No.;Slide Barcode;Slide Name;Slide Info\n");
            for(String l: slideList) {
                writer.write(l + "\n");
            }
            writer.close();
            workbook.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void genMAP(String n, String prot, String espCible, String espHote, String num, String tissu, String antigen, String fourni, String ref, String clone, String revelation) {
        try {
            FileInputStream inputStream = new FileInputStream(new File(Params.basePath + "/Template/Suivi_TK/Mise_au_point_Ac.xlsx"));
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            sheet.getRow(2).createCell(1).setCellValue(prot);
            sheet.getRow(3).createCell(1).setCellValue(espCible);
            sheet.getRow(4).createCell(1).setCellValue(espHote);
            sheet.getRow(6).createCell(1).setCellValue(n);
            sheet.getRow(8).createCell(1).setCellValue(tissu);
            sheet.getRow(10).createCell(1).setCellValue(antigen);
            sheet.getRow(2).createCell(3).setCellValue(fourni);
            sheet.getRow(3).createCell(3).setCellValue(ref);
            sheet.getRow(4).createCell(3).setCellValue(clone);
            sheet.getRow(10).createCell(3).setCellValue(revelation);
            inputStream.close();
            System.out.println(Params.basePath + "/Anticorps_exp/" + n + "_" + prot + "_" + espCible + "_" + fourni + "_" + ref + "/Fiche_Travail/Fiche.xlsx");
            int pic_id = workbook.addPicture(QRCode.create(Params.basePath + "/Anticorps_exp/" + n + "_" + prot + "_" + espCible.charAt(0) + "_" + fourni + "_" + ref + "/Fiche_Travail/Fiche.pdf"), Workbook.PICTURE_TYPE_JPEG);
            XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
            XSSFClientAnchor anchor = new XSSFClientAnchor();
            anchor.setCol1(4);
            anchor.setRow1(3);
            anchor.setCol2(6);
            anchor.setRow2(9);
            XSSFPicture pic = drawing.createPicture(anchor, pic_id);
            FileOutputStream outputStream = new FileOutputStream(new File(Params.basePath + "/Anticorps_exp/" + n + "_" + prot + "_" + espCible.charAt(0) + "_" + fourni + "_" + ref + "/Fiche_Travail/Fiche.xlsx"));
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateCoupe(Project proj) {
        try {
            InputStream inputStream = new FileInputStream(new File(proj.getPath() + "/SuiviTk/Coupe.xlsx"));
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            List<String> echanList = getSampleList(proj);
            int c = 10;
            for(String echan: echanList) {
                sheet.createRow(c).createCell(0).setCellValue(echan);
                c++;
            }
            //setBorder(10, echanList.size()+9, 0, 3, sheet);
            inputStream.close();
            FileOutputStream outputStream = new FileOutputStream(new File(proj.getPath() + "/SuiviTK/Coupe.xlsx"));
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        } catch (EncryptedDocumentException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void updateMarquage(Project proj) {
        File[] marquageFiles = new File(proj.getPath() + "/SuiviTK/Marquage/").listFiles();
        if (isAno(proj)) {
            for(File file: marquageFiles) {
                if (file.getName().contains("pdf")) {
                    continue;
                }
                String esp = "";
                if (file.toString().split("_")[3].equals("Human")) {
                    esp = "0";
                } else if (file.toString().split("_")[3].equals("Mouse")) {
                    esp = "1";
                } else {
                    esp = "2";
                }
                if (file.getName().contains("$")) {
                    try {
                        InputStream inputStream = new FileInputStream(file);
                        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                        for (int j=0; j<workbook.getNumberOfSheets(); j++) {
                            String sheetName = workbook.getSheetName(j);
                            Sheet sheet = workbook.getSheet(sheetName);
                            int c = 16;
                            List<List<String>> anoList = getAno(proj);
                            List<String> echan = anoList.get(0);
                            List<String> ano = anoList.get(1);
                            for(int i=0; i<echan.size(); i++) {
                                sheet.createRow(c).createCell(0).setCellValue(echan.get(i));
                                sheet.getRow(c).createCell(1).setCellValue("P" + proj.getNumber() + "_" + esp + "_" + file.toString().split("_")[2] + "_" + ano.get(i));
                                c++;
                            }
                            setBorder(16, echan.size()+15, 0, 4, sheet);
                        }
                        inputStream.close();
                        OutputStream outputStream = new FileOutputStream(file);
                        workbook.write(outputStream);
                        workbook.close();
                        outputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        String antig = file.toString().split("_")[2];
                        InputStream inputStream = new FileInputStream(file);
                        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                        Sheet sheet = workbook.getSheetAt(0);
                        int c = 16;
                        List<List<String>> anoList = getAno(proj);
                        List<String> echan = anoList.get(0);
                        List<String> ano = anoList.get(1);
                        for(int i=0; i<echan.size(); i++) {
                            sheet.createRow(c).createCell(0).setCellValue(echan.get(i));
                            sheet.getRow(c).createCell(1).setCellValue("P" + proj.getNumber() + "_" + esp + "_" + file.toString().split("_")[2] + "_" + ano.get(i));
                            c++;
                        }
                        setBorder(16, echan.size()+15, 0, 4, sheet);
                        inputStream.close();
                        OutputStream outputStream = new FileOutputStream(file);
                        workbook.write(outputStream);
                        workbook.close();
                        outputStream.close();
                    } catch (EncryptedDocumentException | IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        } else {
            for(File file: marquageFiles) {
                if (file.getName().contains("pdf")) {
                    continue;
                }
                String esp = "";
                System.out.println(file.toString().split("_")[3]);
                if (file.toString().split("_")[3].equals("Human")) {
                    esp = "0";
                } else if (file.toString().split("_")[3].equals("Mouse")) {
                    esp = "1";
                } else {
                    esp = "2";
                }
                if (file.getName().contains("$")) {
                    System.out.println(file.getName() + " is Mulitplex");
                    try {
                        String antig = file.toString().split("_")[2];
                        InputStream inputStream = new FileInputStream(file);
                        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                        for (int j=0; j<workbook.getNumberOfSheets(); j++) {
                            String sheetName = workbook.getSheetName(j);
                            Sheet sheet = workbook.getSheet(sheetName);
                            int c = 16;
                            for(String echan: getSampleList(proj)) {
                                sheet.createRow(c).createCell(0).setCellValue(echan);
                                sheet.getRow(c).createCell(1).setCellValue("P" + proj.getNumber() + "_" + esp + "_" + file.toString().split("_")[2] + "_" + echan);
                                c++;
                            }
                            setBorder(16, getSampleList(proj).size() + 15, 0, 4, sheet);
                        }
                        inputStream.close();
                        OutputStream outputStream = new FileOutputStream(file);
                        workbook.write(outputStream);
                        workbook.close();
                        outputStream.close();
                    } catch (EncryptedDocumentException | IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    try {
                        String antig = file.toString().split("_")[2];
                        InputStream inputStream = new FileInputStream(file);
                        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                        Sheet sheet = workbook.getSheetAt(0);
                        int c = 16;
                        for(String echan: getSampleList(proj)) {
                            sheet.createRow(c).createCell(0).setCellValue(echan);
                            sheet.getRow(c).createCell(1).setCellValue("P" + proj.getNumber() + "_" + esp + "_" + file.toString().split("_")[2] + "_" + echan);
                            c++;
                        }
                        setBorder(16, getSampleList(proj).size() + 15, 0, 4, sheet);
                        inputStream.close();
                        OutputStream outputStream = new FileOutputStream(file);
                        workbook.write(outputStream);
                        workbook.close();
                        outputStream.close();
                    } catch (EncryptedDocumentException | IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static boolean isAno(Project proj) {
        try {
            InputStream inputStream = new FileInputStream(new File(proj.getPath() + "/Echantillon/Pivot.xlsx"));
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            int val = sheet.getLastRowNum();
            if(val == 0) {
                inputStream.close();
                workbook.close();
                return false;
            } else {
                inputStream.close();
                workbook.close();
                return true;
            }
        } catch (EncryptedDocumentException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    private static List<Map<String, String>> readReactFile(String path) {
        List<Map<String, String>> rtrn = new ArrayList<>();
        Map<String, String> rtrnRef = new HashMap<>();
        Map<String, String> rtrnMin = new HashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line;
            System.out.println(path);
            while((line=reader.readLine()) != null) {
                String[] splitLine = line.split(";");
                System.out.println(splitLine[0]);
                System.out.println(splitLine[1]);
                System.out.println(splitLine[2]);
                if (splitLine[0].equals("Perox Block")) {
                    rtrnMin.put("perox", splitLine[2]);
                    rtrnRef.put("perox", splitLine[1]);
                } else if (splitLine[0].equals("Normal Goat Serum")) {
                    rtrnMin.put("normal", splitLine[2]);
                    rtrnRef.put("normal", splitLine[1]);
                } else if (splitLine[0].equals("Polymères")) {
                    rtrnMin.put("poly", splitLine[2]);
                    rtrnRef.put("poly", splitLine[1]);
                } else if (splitLine[0].equals("Chromogène")) {
                    rtrnMin.put("chromo", splitLine[2]);
                    rtrnRef.put("chromo", splitLine[1]);
                } else if (splitLine[0].equals("Hématoxyline")) {
                    rtrnMin.put("hemma", splitLine[2]);
                    rtrnRef.put("hemma", splitLine[1]);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        rtrn.add(rtrnMin);
        rtrn.add(rtrnRef);
        return rtrn;
    }

    public static int getNbMarquage(File f) {
        System.out.println(f.toString());
        try {
            FileInputStream inputStream = new FileInputStream(f);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            int num = workbook.getNumberOfSheets();
            workbook.close();
            inputStream.close();
            return num;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static List<Map<String, String>> readReact(String esp, String rev) {
        if(esp.equals("Human") && rev.equals("DAB")) {
            return readReactFile(Params.basePath + "/Template/Reactif/Human_DAB.txt");
        } else if (esp.equals("Human") && rev.equals("AEC")) {
            return readReactFile(Params.basePath + "/Template/Reactif/Human_AEC.txt");
        } else if (esp.equals("Mouse") && rev.equals("DAB")) {
            return readReactFile(Params.basePath + "/Template/Reactif/Mouse_DAB.txt");
        } else if (esp.equals("Mouse") && rev.equals("AEC")) {
            return readReactFile(Params.basePath + "/Template/Reactif/Mouse_AEC.txt");
        } else if (esp.equals("Human") && rev.equals("Vinagreen")) {
            return readReactFile(Params.basePath + "/Template/Reactif/Human_VINAGREEN.txt");
        } else if (esp.equals("Mouse") && rev.equals("Vinagreen")) {
            return readReactFile(Params.basePath + "/Template/Reactif/Mouse_VINAGREEN.txt");
        } else if (esp.equals("Mouse") && rev.equals("ImmPACT Vector Red")) {
            return readReactFile(Params.basePath + "/Template/Reactif/Mouse_Vector_Red.txt");
        } else if (esp.equals("Mouse") && rev.equals("HRP Magenta")) {
            return readReactFile(Params.basePath + "/Template/Reactif/Mouse_HRP_Magenta.txt");
        } else if (esp.equals("Human") && rev.equals("HRP Magenta")) {
            return readReactFile(Params.basePath + "/Template/Reactif/Human_HRP_Magenta.txt");
        } else {
            List<Map<String, String>> rtrn = new ArrayList<>();
            Map<String, String> rtrnMin = new HashMap<>();
            Map<String, String> rtrnRef = new HashMap<>();
            rtrnMin.put("perox", "");
            rtrnMin.put("normal", "");
            rtrnMin.put("poly", "");
            rtrnMin.put("chromo", "");
            rtrnMin.put("hemma", "");
            rtrnRef.put("perox", "");
            rtrnRef.put("normal", "");
            rtrnRef.put("poly", "");
            rtrnRef.put("chromo", "");
            rtrnRef.put("hemma", "");
            rtrn.add(rtrnMin);
            rtrn.add(rtrnRef);
            return rtrn;
        }
    }

    private static List<List<String>> getAno(Project proj) {
        List<List<String>> globList = new ArrayList<>();
        List<String> echan = new ArrayList<>();
        List<String> ano = new ArrayList<>();
        globList.add(echan);
        globList.add(ano);
        try {
            InputStream inputStream = new FileInputStream(new File(proj.getPath() + "/Echantillon/Pivot.xlsx"));
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            for(int i=1; i<sheet.getLastRowNum() + 1; i++) {
                echan.add(String.valueOf(sheet.getRow(i).getCell(0).getStringCellValue()));
                ano.add(String.valueOf(sheet.getRow(i).getCell(1).getStringCellValue()));
            }
            inputStream.close();
            workbook.close();
            return globList;
        } catch (EncryptedDocumentException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return globList;
    }

    public static int getNumberCoupe(Project proj) {
            try {
                FileInputStream inputStream = new FileInputStream(new File(proj.getPath() + "/Echantillon/sample_list.xlsx"));
                XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                Sheet sheet = workbook.getSheetAt(0);
                int lstLine = sheet.getLastRowNum();
                workbook.close();
                inputStream.close();
                return lstLine;
            } catch (EncryptedDocumentException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return 0;
    }
    
    public static List<String> getAllSlide(Project proj) {
        List<String> res = new ArrayList<>();
        if (new File(proj.getPath() + "/SuiviTK/Coupe.xlsx").exists()) {
            try {
                FileInputStream inputStream = new FileInputStream(new File(proj.getPath() + "/SuiviTK/Coupe.xlsx"));
                XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                Sheet sheet = workbook.getSheetAt(0);
                int lstLine = sheet.getLastRowNum();
                for(int i=10; i<lstLine+1; i++) {
                    res.add(sheet.getRow(i).getCell(0).getStringCellValue());
                }
                workbook.close();
                inputStream.close();
            } catch (EncryptedDocumentException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        File[] lstMarquage = new File(proj.getPath() + "/SuiviTk/Marquage/").listFiles();
        for (File f: lstMarquage) {
            try {
                FileInputStream inputStream = new FileInputStream(new File(f.toString()));
                XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                Sheet sheet = workbook.getSheetAt(0);
                int lstLine = sheet.getLastRowNum();
                for(int i=16; i<lstLine+1;i++) {
                    res.add(sheet.getRow(i).getCell(0).getStringCellValue());
                    res.add(sheet.getRow(i).getCell(1).getStringCellValue());
                }
                workbook.close();
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        File[] lstNum = new File(proj.getPath() + "/SuiviTk/Numerisation/").listFiles();
        for (File f: lstNum) {
            try {
                FileInputStream inputStream = new FileInputStream(new File(f.toString()));
                XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                Sheet sheet = workbook.getSheetAt(0);
                int lstLine = sheet.getLastRowNum();
                if (f.toString().contains("Custom")) {
                    for(int i=8; i<lstLine+1; i++) {
                        res.add(sheet.getRow(i).getCell(0).getStringCellValue());
                        res.add(sheet.getRow(i).getCell(1).getStringCellValue());
                    }
                } else {
                    for (int i=1; i<lstLine+1;i++) {
                        try {
                            res.add(sheet.getRow(i).getCell(3).getStringCellValue());
                        } catch (Exception e) {
                            break;
                        }
                    }
                }
                workbook.close();
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    private static List<String> getSampleList(Project proj) {
        List<String> res = new ArrayList<>();
        try {
            FileInputStream inputStream = new FileInputStream(new File(proj.getPath() + "/Echantillon/sample_list.xlsx"));
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            int lstLine = sheet.getLastRowNum();
            for(int i=9; i<lstLine+1; i++) {
                try{
                    res.add(String.valueOf(sheet.getRow(i).getCell(0).getStringCellValue()));
                } catch (Exception e) {
                    res.add(String.valueOf(sheet.getRow(i).getCell(0).getNumericCellValue()).replace(".0", ""));
                }
            }
            workbook.close();
            inputStream.close();
        }  catch (EncryptedDocumentException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
    }

    public static double getDilution(int number) {
        double rtrn = 0;
        try {
            InputStream inputStream = new FileInputStream(new File(Params.basePath + "/Template/Reactif/Fiches_produits_Acs.xlsx"));
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(2);
            int lineNumber = sheet.getLastRowNum();
            for (int i=2; i<lineNumber; i++) {
                if (sheet.getRow(i).getCell(0).getNumericCellValue() == number) {
                    rtrn = sheet.getRow(i).getCell(8).getNumericCellValue();
                    break;
                }
            }
            return rtrn;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return rtrn;
    }

    private static void setBorder(int fromX, int toX, int fromY, int toY, org.apache.poi.ss.usermodel.Sheet sheet) {
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

    private static List<String> getMarqList(Project proj, String name) {
        List<String> echan = new ArrayList<>();
        String path = "";
        System.out.println(name);
        for (int i=0; i<proj.listMarquage.size(); i++) {
            if (proj.listMarquage.get(i) instanceof Marquage) {
                Marquage mrq = (Marquage)proj.listMarquage.get(i);
                if (name.contains(mrq.getName()) && name.contains(mrq.getNum())) {
                    path = "Marquage_" + mrq.getName() + "_" + mrq.getNum() + "_" + proj.espece + "_" + mrq.getFourniseur() + "_" + mrq.getReference();
                    break;
                }
            } else {
                Multiplex mrq = (Multiplex)proj.listMarquage.get(i);
                if (name.contains(mrq.getName()) && name.contains(mrq.getNum())) {
                    path = "Marquage_" + mrq.getName();
                    break;
                }
            }
        }
        try {
            InputStream inputStream = new FileInputStream(new File(proj.getPath() + "/SuiviTK/Marquage/" + path + ".xlsx"));
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            int lineNumber = sheet.getLastRowNum();
            for(int i=16; i<lineNumber + 1; i++) {
                echan.add(sheet.getRow(i).getCell(1).getStringCellValue());
            }
            inputStream.close();
            workbook.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return echan;
    }

    public static HashMap<String, String> getInfoValid(Marquage m) {
        HashMap<String, String> res = new HashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(m.getPath().replace("exp", "valid") + "/Info_Valid.txt"));
            String line;
            while((line=reader.readLine())!=null) {
                String[] splitLine = line.split("#!#");
                res.put(splitLine[0], splitLine[1]);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;

    }

    public static void createFichePTBC(Marquage m) {
        try{
            HashMap<String, String> data = getInfoValid(m);
            InputStream inputStream = new FileInputStream(Params.basePath + "/Template/Anticorps/Fiche_PTBC.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            sheet.getRow(1).getCell(0).setCellValue(m.getNum());
            sheet.getRow(1).getCell(1).setCellValue(m.getName());
            sheet.getRow(1).getCell(2).setCellValue(String.valueOf(m.getEspeceCible().charAt(0)));
            sheet.getRow(1).getCell(3).setCellValue(m.getFourniseur());
            sheet.getRow(1).getCell(5).setCellValue(m.getReference());
            sheet.getRow(1).getCell(6).setCellValue(m.getRevelation().replace("HRP ", "").replace("ina", ""));
            Date date = Calendar.getInstance().getTime();
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String today = formatter.format(date);
            sheet.getRow(10).getCell(0).setCellValue(today);
            sheet.getRow(13).getCell(1).setCellValue(m.getEspeceCible());
            sheet.getRow(13).getCell(3).setCellValue(m.getEspeceHote());
            System.out.println("Clonalité: " + m.clonalite);
            System.out.println("nMono:" + m.nMonoclonale);
            sheet.getRow(13).getCell(6).setCellValue(m.clonalite);
            sheet.getRow(14).getCell(6).setCellValue(m.nMonoclonale);
            sheet.getRow(14).getCell(1).setCellValue(m.tissu);
            sheet.getRow(18).getCell(1).setCellValue(m.antigene);
            sheet.getRow(18).getCell(3).setCellValue(data.get("temperature"));
            sheet.getRow(18).getCell(6).setCellValue(data.get("tp"));
            sheet.getRow(29).getCell(0).setCellValue(m.getNum() + "_" + m.getName() + "_" + m.getEspeceCible().charAt(0) + "_" + m.getRevelation());
            sheet.getRow(23).getCell(0).setCellValue(m.getNum() + "_" + m.getName() + "_" + m.getEspeceCible().charAt(0) + "_" + m.getFourniseur()+m.getReference());
            sheet.getRow(22).getCell(6).setCellValue(data.get("diluant"));
            sheet.getRow(23).getCell(6).setCellValue(data.get("dilution"));
            sheet.getRow(24).getCell(6).setCellValue(data.get("tempIncub"));
            sheet.getRow(33).getCell(0).setCellValue(data.get("refPerox"));
            sheet.getRow(32).getCell(1).setCellValue(data.get("minPerox"));
            sheet.getRow(32).getCell(6).setCellValue(data.get("minChromo"));
            sheet.getRow(33).getCell(5).setCellValue(data.get("refChromo"));
            sheet.getRow(36).getCell(0).setCellValue(data.get("refNMS"));
            sheet.getRow(35).getCell(1).setCellValue(data.get("minNMS"));
            sheet.getRow(36).getCell(5).setCellValue(data.get("refHema"));
            sheet.getRow(35).getCell(6).setCellValue(data.get("minHema"));
            sheet.getRow(38).getCell(1).setCellValue(data.get("minPoly"));
            sheet.getRow(39).getCell(0).setCellValue(data.get("refPoly"));
            sheet.getRow(10).getCell(6).setCellValue("Lame " + getImageFromProject(m).getName().replace(".png", ""));
            inputStream.close();
            InputStream imageStream = new FileInputStream(getImageFromProject(m));
            byte[] bytes = IOUtils.toByteArray(imageStream);
            int pic_id = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
            imageStream.close();
            XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
            XSSFClientAnchor anchor = new XSSFClientAnchor();
            anchor.setCol1(2);
            anchor.setRow1(3);
            anchor.setCol2(7);
            anchor.setRow2(10);
            XSSFPicture pic = drawing.createPicture(anchor, pic_id);
            OutputStream outputStream = new FileOutputStream(m.getPath().replace("exp", "valid") + "/Fiche_PTBC/Fiche_"+ m.getFullName().replace(" ", "_") +".xlsx");
            workbook.write(outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File getImageFromProject(Marquage m) {
        File[] listFile = new File(m.getPath().replace("exp", "valid") + "/Scan").listFiles();
        for (File f: listFile) {
            File[] l = f.listFiles();
            for(File fi: l) {
                if (fi.toString().contains("png")) {
                    return fi;
                }
            }
        }
        return null;
    }

    public static void updateNum(Project proj) {
        //
        updateMarquage(proj);
        File[] numList = new File(proj.getPath() + "/SuiviTK/Numerisation/").listFiles();
        for(File f: numList) {
            if (f.getName().toString().substring(f.getName().toString().lastIndexOf(".")).equals(".csv")) {
                continue;
            }
            String esp = "";
            if (f.toString().split("_")[4].equals("Human")) {
                esp = "0";
            } else if (f.toString().split("_")[4].equals("Mouse")) {
                esp = "1";
            } else {
                esp = "2";
            }
            if (f.toString().contains("Custom")) {
                if (isAno(proj)) {
                    List<List<String>> echan = new ArrayList<>();
                    echan.add(new ArrayList<>());
                    echan.add(new ArrayList<>());
                    List<String> tmp = getSampleList(proj);
                    List<List<String>> ano = getAno(proj);
                    
                    for(int i=0;i<ano.get(0).size();i++) {
                        echan.get(0).add("P" + proj.getNumber() + "_" + esp + "_" + f.toString().split("_")[2] + "_" + ano.get(0).get(i));
                        echan.get(1).add("P" + proj.getNumber() + "_" + esp + "_" +  f.toString().split("_")[2] + "_" + ano.get(1).get(i));
                    }
                    try {
                        InputStream inputStream = new FileInputStream(f);
                        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                        Sheet sheet = workbook.getSheetAt(0);
                        int i = 1;
                        for (int n=0;n<echan.get(0).size();n++) {
                            System.out.println(echan.get(1).get(n));
                            //sheet.createRow(i).createCell(0).setCellValue(echan.get(0).get(n));
                            sheet.getRow(i).createCell(3).setCellValue(echan.get(1).get(n));
                            i++;
                        }
                        //setBorder(8, 7 + echan.get(0).size(), 0, 2, sheet);
                        inputStream.close();
                        OutputStream outputStream = new FileOutputStream(f);
                        workbook.write(outputStream);
                        outputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    List<String> echan = getMarqList(proj, f.getName());
                    try {
                        InputStream inputStream = new FileInputStream(f);
                        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                        XSSFSheet sheet = workbook.getSheetAt(0);
                        int i = 1;
                        for (int n=0;n<echan.size();n++) {
                            //sheet.createRow(i).createCell(0).setCellValue(echan.get(n));
                            sheet.getRow(i).createCell(3).setCellValue(echan.get(n));
                            i++;
                        }
                        //setBorder(8, 7 + echan.size(), 0, 2, sheet);
                        inputStream.close();
                        OutputStream outputStream = new FileOutputStream(f);
                        workbook.write(outputStream);
                        outputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            } else {
                if (isAno(proj)) {
                    List<List<String>> echan = new ArrayList<>(); 
                    List<String> tmp = getMarqList(proj, f.getName());
                    List<List<String>> ano = getAno(proj);
                    echan.add(new ArrayList<>());
                    echan.add(new ArrayList<>());
                    System.out.println(ano);
                    System.out.println(tmp);
                    for(int i=0;i<ano.get(0).size();i++) {
                        echan.get(0).add("P" + proj.getNumber() + "_" + esp + "_" + f.toString().split("_")[2] + "_" + ano.get(0).get(i));
                        echan.get(1).add("P" + proj.getNumber() + "_" + esp + "_" + f.toString().split("_")[2] + "_" + ano.get(1).get(i));
                        /*
                        echan.get(0).add(tmp.get(i));
                        String toReplace = tmp.get(i).split("_")[tmp.get(i).split("_").length];
                        echan.get(1).add(tmp.get(i).replace(toReplace, ano.get(i)));
                        */
                    }
                    System.out.println(echan);
                    try {
                        InputStream inputStream = new FileInputStream(f);
                        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                        Sheet sheet = workbook.getSheetAt(0);
                        int i = 1;
                        for (int n=0;n<echan.get(0).size();n++) {
                            sheet.getRow(i).createCell(3).setCellValue(echan.get(1).get(n));
                            i++;
                        }
                        //setBorder(8, 7 + echan.get(0).size(), 0, 2, sheet);
                        inputStream.close();
                        OutputStream outputStream = new FileOutputStream(f);
                        workbook.write(outputStream);
                        outputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    List<List<String>> echan = new ArrayList<>();
                    echan.add(new ArrayList<>());
                    echan.add(new ArrayList<>());
                    List<String> tmp = getMarqList(proj, f.getName());
                    System.out.println(tmp);
                    if (tmp.size() == 0) {
                        continue;
                    }
                    for(int i=0;i<tmp.size();i++) {
                        echan.get(0).add(tmp.get(i));
                    }
                    try {
                        InputStream inputStream = new FileInputStream(f);
                        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                        Sheet sheet = workbook.getSheetAt(0);
                        int i = 1;
                        for (int n=0;n<echan.get(0).size();n++) {
                            sheet.getRow(i).createCell(3).setCellValue(echan.get(0).get(n));
                            i++;
                        }
                        //setBorder(8, 7 + echan.get(0).size(), 0, 2, sheet);
                        inputStream.close();
                        OutputStream outputStream = new FileOutputStream(f);
                        workbook.write(outputStream);
                        outputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void ficheTravailMultiplex(Multiplex m) {
        int count = 1;
        try{
            FileInputStream inputStream = new FileInputStream(new File(Params.basePath + "/Template/Suivi_TK/Mise_au_point_Multiplex.xlsx"));
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            for (Marquage marq: m.marquageList) {
                    Sheet sheet = workbook.cloneSheet(0, marq.getName());
                    sheet.getRow(2).createCell(2).setCellValue(m.getNum());
                    sheet.getRow(4).createCell(1).setCellValue(marq.getName());
                    sheet.getRow(6).createCell(1).setCellValue(marq.getEspeceCible());
                    sheet.getRow(7).createCell(1).setCellValue(marq.getEspeceHote());
                    sheet.getRow(9).createCell(1).setCellValue(marq.getNum());
                    sheet.getRow(4).createCell(3).setCellValue(count);
                    sheet.getRow(5).createCell(3).setCellValue(marq.getFourniseur());
                    sheet.getRow(6).createCell(3).setCellValue(marq.getReference());
                    sheet.getRow(7).createCell(3).setCellValue(marq.clonalite);
                    sheet.getRow(9).createCell(3).setCellValue(marq.getRevelation());
                    int pic_id = workbook.addPicture(QRCode.create(Params.basePath + "/Anticorp_exp/" + m.getName() + "/Fiche_Travail/Fiche_" + marq.getName() + "_" + count + ".pdf"), Workbook.PICTURE_TYPE_JPEG);
                    XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
                    XSSFClientAnchor anchor = new XSSFClientAnchor();
                    anchor.setCol1(4);
                    anchor.setRow1(1);
                    anchor.setCol2(7);
                    anchor.setRow2(7);
                    XSSFPicture pic = drawing.createPicture(anchor, pic_id);
                    count++;
            }
            workbook.removeSheetAt(0);
            inputStream.close();
            FileOutputStream outputStream = new FileOutputStream(new File(Params.basePath + "/Anticorps_exp/" + m.getName() + "/Fiche_Travail/Fiche.xlsx"));
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
