package ISO9001;

import java.util.ArrayList;
import java.util.List;

public class Params {
   
    public static String basePath;
    public static List<String> roleList;
    public static String version;
    public static String archivePath;

    public Params() {
        roleList = new ArrayList<>();
        roleList.add("User");
        roleList.add("Moderateur");
        roleList.add("Administrateur");
        basePath = "K:/ISO9001";
        version = "1.0.1";
        archivePath = "K:/_Archivage/Histo/Project";
    }

    public String getBasePath() {
        return basePath;
    }
    
}
