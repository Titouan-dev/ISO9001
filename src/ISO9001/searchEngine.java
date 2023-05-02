package ISO9001;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hpsf.Array;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.application.Platform;
import javafx.scene.Cursor;

public class searchEngine {
    public static List<Project> searchProj(String search) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Main.scene.setCursor(Cursor.WAIT);
            }
        });
        String[] searchSplit = search.split("&");
        List<String> searchList = new ArrayList<>();
        for(String s: searchSplit) {
            searchList.add(s);
        }
        List<List<Project>> rtrn = new ArrayList<>();
        System.out.println("Start first");
        searchList.parallelStream().forEach(s -> {
            try {
                rtrn.add(search(Params.basePath + "/Project/", s));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        System.out.println("End first");
        List<Project> finalList = new ArrayList<>();
        if (rtrn.size() == 1) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Main.scene.setCursor(Cursor.DEFAULT);
                }
            });
            return rtrn.get(0);
        }
        System.out.println("Start second");
        for(Project proj: rtrn.get(0)) {
            boolean check = true;
            for(int i=1; i<rtrn.size(); i++) {
                boolean check2 = false;
                for (Project proj1: rtrn.get(i)) {
                    if (proj.equals(proj1)) {
                        check2 = true;
                        break;
                    }
                }
                if (!check2) {
                    check = false;
                }
            }
            if (check) {
                finalList.add(proj);
            }
        }
        System.out.println("End second");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Main.scene.setCursor(Cursor.DEFAULT);
            }
        });
        return finalList;
    }

    public static File searchArchiProject(String search) {
        File[] listArchive = new File(Params.basePath + "/Archivage").listFiles();
        for (File proj: listArchive) {
            try{
                BufferedReader reader = new BufferedReader(new FileReader(proj + "/Info.txt"));
                String line;
                while((line=reader.readLine())!=null) {
                    String[] splitLine = line.split("#!#");
                    if (splitLine[0].equals("nProject")) {
                        if (splitLine[1].equals(search)) {
                            reader.close();
                            return proj;
                        }
                    }
                }
            } catch (Exception e ) {
                e.printStackTrace();
            }
        }
        listArchive = new File(Params.basePath + "/ArchivePhysique/").listFiles();
        for (File f: listArchive) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(f));
                String line;
                while((line=reader.readLine())!=null) {
                    System.out.println(line + " " + search);
                    String[] splitLine = line.split("#!#");
                    if (splitLine[0].equals("nProj")) {
                        if (splitLine[1].equals(search)) {
                            reader.close();
                            return f;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public static File searchArchiSample(String search) {
        File[] listArchive = new File(Params.basePath + "/Archivage").listFiles();
        for (File proj: listArchive) {
            try{
                System.out.println(proj + "/Slides.txt");
                BufferedReader reader = new BufferedReader(new FileReader(proj + "/Slides.txt"));
                String line;
                while((line=reader.readLine())!=null) {
                    System.out.println(line);
                    if (line.equals(search)) {
                        reader.close();
                        return proj;
                    }
                }
            } catch (Exception e ) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public static List<File> searchArchi(String search) {
        List<File> archiveList = new ArrayList<>();
        File[] listArchive = new File(Params.basePath + "/Archivage").listFiles();
        for (File proj: listArchive) {
            File[] listFile = proj.listFiles();
            for(File file: listFile) {
                if (searchTxt(file.toString(), search)) {
                    archiveList.add(new File(proj.toString()));
                    break;
                }
            }
        }
        return archiveList;
    }

    public static List<File> searchArchiPhysiqueTitre(String search) {
        List<File> res = new ArrayList<>();
        File[] listFile = new File(Params.basePath + "/ArchivePhysique/").listFiles();
        for(File f: listFile) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(f));
                String line;
                while((line=reader.readLine())!=null) {
                    String[] splitLine = line.split("#!#");
                    if (splitLine[0].equals("titre") && splitLine[1].equals(search)) {
                        res.add(f);
                    }
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    private static List<Project> search(String path, String search) {
        List<Project> rtrn = new ArrayList<>();
        File[] listFile = new File(path).listFiles();
        List<File> fileList = new ArrayList<>();
        for (File f: listFile) {
            fileList.add(f);
        }
        //for(File f: listFile) {
        fileList.parallelStream().forEach(f -> {
            if (f.isDirectory()) {
                if (!f.toString().contains("Log")) {
                    try {
                        rtrn.addAll(search(f.toString() + "/", search));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (f.toString().split("\\.")[f.toString().split("\\.").length - 1].equals("txt")) {
                    if (searchTxt(f.toString(), search)) {
                        for (Project p: User.projList) {
                            System.out.println("File: " + f.toString().replace("\\", "/") + " | Path: " + p.path);
                            if (f.toString().replace("\\", "/").contains(p.path)) {
                                rtrn.add(p);
                                break;
                            }
                        }
                    }
                } else if (f.toString().split("\\.")[f.toString().split("\\.").length - 1].equals("xlsx")) {
                    if (searchXlsx(f.toString(), search)) {
                        for (Project p: User.projList) {
                            if (f.toString().replace("\\", "/").contains(p.path)) {
                                rtrn.add(p);
                                break;
                            }
                        }
                    }
                }
            }
        });
        return rtrn;
    }

    private static boolean searchTxt(String path, String search) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(path));
            String line;
            while((line=reader.readLine()) != null) {
                if (line.toLowerCase().contains(search.toLowerCase())) {
                    System.out.println(path);
                    reader.close();
                    return true;
                }
            }
            reader.close();
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    private static boolean searchXlsx(String path, String search) {
        try {
            InputStream inputStream = new FileInputStream(new File(path));
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            int lastRow = sheet.getLastRowNum();
            for (int line=0; line<lastRow + 1; line++) {
                for (int cell=0; cell<5;cell++) {
                    try {
                        if (sheet.getRow(line).getCell(cell).getStringCellValue().toString().contains(search)) {
                            inputStream.close();
                            workbook.close();
                            return true;
                        }
                    } catch (Exception e) {
                        continue;
                    }
                }
            }
            inputStream.close();
            workbook.close();
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
}
