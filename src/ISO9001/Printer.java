package ISO9001;

import java.awt.image.BufferedImage;
import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.swing.ImageIcon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

public class Printer {

       public static String printLabel( byte[] outputImage) throws Exception {
        try {           

              PrinterJob printerJob = PrinterJob.getPrinterJob();

                PrintService printService=null;
                if(printerJob.printDialog())
                {
                    printService = printerJob.getPrintService();
                }
                DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
                Doc doc = new SimpleDoc(outputImage, flavor, null);
                if(printService!=null) {
                    DocPrintJob printJob = printService.createPrintJob();
                    printJob.print(doc, null);
                    return "success";
                }
                return "error";
        } catch (Exception e) {
            throw new Exception("Unable to print", e);
        }
    }

    public static byte[] scale(byte[] fileData, int width, int height) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(fileData);
        try {
            BufferedImage img = ImageIO.read(in);
            if(height == 0) {
                height = (width * img.getHeight())/ img.getWidth(); 
            }
            if(width == 0) {
                width = (height * img.getWidth())/ img.getHeight();
            }
            Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
            BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            imageBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(0,0,0), null);

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            ImageIO.write(imageBuff, "jpg", buffer);

            return buffer.toByteArray();
        } catch (IOException e) {
            throw new Exception("IOException in scale");
        }
    }

}
