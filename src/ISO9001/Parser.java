package ISO9001;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Parser extends Thread{

    public Parser() {
        super();
    }

    public void run() {
        while (true) {
            checkVersion.checkVersion();
           File[] files = new File("K:/Valentin/ISO9001/Archivage").listFiles();
           if (files.length > 0) {
               for (File f: files)  {
                String path = QRCode.read(f);
                try {
                    Files.copy(f.toPath(), new File(path).toPath(), StandardCopyOption.REPLACE_EXISTING);
                    f.delete();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        
                    }
               }
           }
           try {
            Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            File[] slides = new File(Params.basePath + "/pendingSlides").listFiles();
            if (slides.length > 0) {
                for (File slide: slides) {
                    validFolder(slide.toString());
                    if (String.valueOf(slide.getName().toString().charAt(0)).equals("D")) {
                        String path = foundAnticorp(String.valueOf(slide.getName().toString().split("_")[0].replace("D", "")));
                        Administration.copyRecusrively(slide.toString(), path + "/Scan/" + slide.getName());    
                    } else if (String.valueOf(slide.getName().toString().charAt(0)).equals("P")) {
                        String path = foundProject(slide.getName());
                        try {
                            BufferedWriter writer = new BufferedWriter(new FileWriter(path + "/../import.txt", true));
                            BufferedWriter writer2 = new BufferedWriter(new FileWriter(path + "/import.txt", true));
                            if (slide.isDirectory()) {
                                for (File f: slide.listFiles()) {
                                    System.out.println(f.toString());
                                    if (f.getName().endsWith("vsi") && !f.getName().contains("Overview")) {
                                        writer.write(path + "/" + slide.getName() + "/" + f.getName() + "\n");
                                        writer2.write(path + "/" + slide.getName() + "/" + f.getName() + "\n");
                                    }
                                }
                            } else {
                                writer.write(path + "/" + slide.getName() + "\n");
                                writer2.write(path + "/" + slide.getName() + "\n");
                            }
                            writer.close();
                            writer2.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Administration.copyRecusrively(slide.toString(), path + "/" + slide.getName());
                    }
                }
            }   
        }
    }

    private static String foundProject(String name) {
        File[] projList = new File(Params.basePath + "/Project").listFiles();
        String number = String.valueOf(name.split("_")[0].replace("P", ""));
        for(File proj: projList) {
            if (String.valueOf(proj.getName().replace("P.", "")).equals(number)) {
                File[] listScan = new File(proj.toString() + "/Scan/").listFiles();
                for (File scan: listScan) {
                    try {
                        if (scan.getName().split("_")[1].equals(name.split("_")[2])) {
                            return scan.toString();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    private static String foundAnticorp(String number) {
        File[] listAnticorps_exp = new File(Params.basePath + "/Anticorps_exp").listFiles();
        for (File exp: listAnticorps_exp) {
            if (String.valueOf(exp.getName().split("_")[0]).equals(number)) {
                return exp.toString();
            }
        }
        File[] listAnticors_valid = new File(Params.basePath + " /Anticorps_valid").listFiles(); 
        for (File valid: listAnticors_valid) {
            if (String.valueOf(valid.getName().split("_")[0]).equals(number)) {
                return valid.toString();
            }
        }
        return null;
    }

    private static boolean validFolder(String path) {
        boolean check = false;
        long predSize = new File(path).getTotalSpace();
        try {
            Thread.sleep(20000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        while(check == false) {
            System.out.println(predSize);
            if (new File(path).getTotalSpace() == predSize) {
                return true;
            } else {
                predSize = new File(path).getTotalSpace();
            }
            try {
                Thread.sleep(20000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    
}
