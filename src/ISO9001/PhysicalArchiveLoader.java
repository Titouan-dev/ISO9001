package ISO9001;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhysicalArchiveLoader {
   
    public static List<PhysicalArchive> physicList = new ArrayList<>();

    public static void load() {
        physicList.clear();
        File[] listFile = new File(Params.basePath + "/Archivage/").listFiles();
        System.out.println(listFile[0]);
        for (File f: listFile) {
            File[] lst = f.listFiles();
            for (File fi: lst) {
                System.out.println(fi);
                if (fi.getName().contains("PhysicalArchive")) {
                    physicList.add(new PhysicalArchive(fi.toString()));
                }
            }
        }
        listFile = new File(Params.basePath + "/ArchivePhysique/").listFiles();
        for(File f: listFile) {
            physicList.add(new PhysicalArchive(f, true));
        }
    }
}
