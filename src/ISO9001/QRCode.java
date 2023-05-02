package ISO9001;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;

import java.awt.image.BufferedImage;


public class QRCode {

    private static void generateQRCodeImage(String text, int width, int height, String filePath)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        Path path = (Path) FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", (java.nio.file.Path) path);
    }

    private static BufferedImage generate(String data, int x, int y, int lvl) {
        Hashtable hint = new Hashtable<>();
        if (lvl == 1) {
            hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        } else {
            hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);
        }
        MultiFormatWriter mw = new MultiFormatWriter();
        BitMatrix bm;
        try {
            bm = mw.encode(data, BarcodeFormat.QR_CODE, x, y, hint);
            return MatrixToImageWriter.toBufferedImage(bm);
        } catch (WriterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] createEtiquetteLogo(String text) {
        BufferedImage qrCode = generate(text, 450, 450, 2);
        File logo = new File(Params.basePath + "/Template/logo_cgfl.png");
        BufferedImage img;
        try {
            img = ImageIO.read(logo);
            for(int l=0; l<img.getWidth(); l++) {
                for(int p=0; p<img.getHeight(); p++) {
                    if (img.getRGB(l, p) != 0) {
                        qrCode.setRGB(150 + l, 150 + p, img.getRGB(l, p));              
                    }
                }
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(qrCode, "jpg", baos);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static byte[] create(String text) {
        BufferedImage qrCode = generate(text, 350, 350, 2);
        File logo = new File(Params.basePath + "/Template/logo_cgfl.png");
        BufferedImage img;
        try {
            img = ImageIO.read(logo);
            for(int l=0; l<img.getWidth(); l++) {
                for(int p=0; p<img.getHeight(); p++) {
                    if (img.getRGB(l, p) != 0) {
                        qrCode.setRGB(125 + l, 125 + p, img.getRGB(l, p));              
                    }
                }
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(qrCode, "jpg", baos);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] createEtiquette(String text) {
        BufferedImage qrCode = generate(text, 350, 350, 1);
        BufferedImage img;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(qrCode, "jpg", baos);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] bytes = baos.toByteArray();
        return bytes;
    }

    public static String decodeQRCode(BufferedImage bufferedImage) throws IOException {

        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (NotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String read(File path) {
        PDDocument document;
        BufferedImage bim;
        try {
            for (int t=0; t < 200; t = t+10) {
                document = PDDocument.load(path);
                PDFRenderer pdfRenderer = new PDFRenderer(document);
                bim = pdfRenderer.renderImageWithDPI(0, t, ImageType.RGB);
                
                // suffix in filename will be used as the file format
                document.close();
                String res = decodeQRCode(bim);
                if (res != null) {
                    return res;
                }
            }
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
