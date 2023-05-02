package ISO9001;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class parseData {

    private static void write(String name, int i, org.apache.poi.ss.usermodel.Sheet sheet, BufferedWriter writer) {
        try {
            try {
                writer.write(name + ";"+ sheet.getRow(i).getCell(1).getStringCellValue() + ";" + sheet.getRow(i).getCell(2).getNumericCellValue() + "\n");
            } catch (Exception e) {
                try {
                    writer.write(name + ";" + sheet.getRow(i).getCell(1).getStringCellValue() + "; \n");
                } catch (Exception g) {
                    writer.write("Perox Block" + "; ; \n");
                }
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

   public static void parseReactif() {
        try {
            InputStream inputStream = new FileInputStream(Params.basePath + "/Template/Reactif_prerempli.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
            int i = 0;
            while (i<sheet.getLastRowNum()) {
                try {
                    if (sheet.getRow(i).getCell(0).getStringCellValue().contains("Mouse")) {
                        if (sheet.getRow(i).getCell(0).getStringCellValue().contains("AEC")) {
                            System.out.println("Mouse AEC");
                            i = i + 3;
                            BufferedWriter writer = new BufferedWriter(new FileWriter(Params.basePath + "/Template/Reactif/Mouse_AEC.txt"));
                            write("Perox Block", i, sheet, writer);
                            i++;
                            write("Normal Goat Serum", i, sheet, writer);
                            i++;
                            write("Polymères", i, sheet, writer);
                            i++;
                            write("Chromogène", i, sheet, writer);
                            i++;
                            write("Hématoxyline", i, sheet, writer);
                            writer.close();
                        }
                        else if (sheet.getRow(i).getCell(0).getStringCellValue().contains("DAB")) {
                            i = i + 3;
                            BufferedWriter writer = new BufferedWriter(new FileWriter(Params.basePath + "/Template/Reactif/Mouse_DAB.txt"));
                            write("Perox Block", i, sheet, writer);
                            i++;
                            write("Normal Goat Serum", i, sheet, writer);
                            i++;
                            write("Polymères", i, sheet, writer);
                            i++;
                            write("Chromogène", i, sheet, writer);
                            i++;
                            write("Hématoxyline", i, sheet, writer);
                            writer.close();
                        } else if (sheet.getRow(i).getCell(0).getStringCellValue().contains("VINAGREEN")) {
                            i = i + 3;
                            BufferedWriter writer = new BufferedWriter(new FileWriter(Params.basePath + "/Template/Reactif/Mouse_VINAGREEN.txt"));
                            write("Perox Block", i, sheet, writer);
                            i++;
                            write("Normal Goat Serum", i, sheet, writer);
                            i++;
                            write("Polymères", i, sheet, writer);
                            i++;
                            write("Chromogène", i, sheet, writer);
                            i++;
                            write("Hématoxyline", i, sheet, writer);
                            writer.close();
                        } else if (sheet.getRow(i).getCell(0).getStringCellValue().contains("HRP Magenta")) {
                            i = i + 3;
                            BufferedWriter writer = new BufferedWriter(new FileWriter(Params.basePath + "/Template/Reactif/Mouse_HRP_Magenta.txt"));
                            write("Perox Block", i, sheet, writer);
                            i++;
                            write("Normal Goat Serum", i, sheet, writer);
                            i++;
                            write("Polymères", i, sheet, writer);
                            i++;
                            write("Chromogène", i, sheet, writer);
                            i++;
                            write("Hématoxyline", i, sheet, writer);
                            writer.close();
                        }
                    }
                    else if (sheet.getRow(i).getCell(0).getStringCellValue().contains("Humain")) {
                        if (sheet.getRow(i).getCell(0).getStringCellValue().contains("AEC")) {
                            i = i + 3;
                            BufferedWriter writer = new BufferedWriter(new FileWriter(Params.basePath + "/Template/Reactif/Human_AEC.txt"));
                            write("Perox Block", i, sheet, writer);
                            i++;
                            write("Normal Goat Serum", i, sheet, writer);
                            i++;
                            write("Polymères", i, sheet, writer);
                            i++;
                            write("Chromogène", i, sheet, writer);
                            i++;
                            write("Hématoxyline", i, sheet, writer);
                            writer.close();
                        }
                        else if (sheet.getRow(i).getCell(0).getStringCellValue().contains("DAB")) {
                            i = i + 3;
                            BufferedWriter writer = new BufferedWriter(new FileWriter(Params.basePath + "/Template/Reactif/Human_DAB.txt"));
                            write("Perox Block", i, sheet, writer);
                            i++;
                            write("Normal Goat Serum", i, sheet, writer);
                            i++;
                            write("Polymères", i, sheet, writer);
                            i++;
                            write("Chromogène", i, sheet, writer);
                            i++;
                            write("Hématoxyline", i, sheet, writer);
                            writer.close();
                        } else if (sheet.getRow(i).getCell(0).getStringCellValue().contains("VINAGREEN")) {
                            i = i + 3;
                            BufferedWriter writer = new BufferedWriter(new FileWriter(Params.basePath + "/Template/Reactif/Human_VINAGREEN.txt"));
                            write("Perox Block", i, sheet, writer);
                            i++;
                            write("Normal Goat Serum", i, sheet, writer);
                            i++;
                            write("Polymères", i, sheet, writer);
                            i++;
                            write("Chromogène", i, sheet, writer);
                            i++;
                            write("Hématoxyline", i, sheet, writer);
                            writer.close();
                        } else if (sheet.getRow(i).getCell(0).getStringCellValue().contains("HRP Magenta")) {
                            i = i + 3;
                            BufferedWriter writer = new BufferedWriter(new FileWriter(Params.basePath + "/Template/Reactif/Human_HRP_Magenta.txt"));
                            write("Perox Block", i, sheet, writer);
                            i++;
                            write("Normal Goat Serum", i, sheet, writer);
                            i++;
                            write("Polymères", i, sheet, writer);
                            i++;
                            write("Chromogène", i, sheet, writer);
                            i++;
                            write("Hématoxyline", i, sheet, writer);
                            writer.close();
                        }
                    }
                    i++;
                } catch (Exception e) {
                    i++;
                }
            }
            workbook.close();
            inputStream.close();
        } catch (IOException | EncryptedDocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
   }    
}
