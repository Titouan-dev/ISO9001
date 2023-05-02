package ISO9001;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class checkVersion {

    public static void checkVersion() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(Params.basePath + "/version.txt"));
            if (reader.readLine().equals(Params.version)) {
                reader.close();
                return;
            } else {
                reader.close();
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setTitle("Version");
                dialog.setContentText("La version du logiciel n'est plus à jour, relancer la dernière version.");
                dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                Optional<ButtonType> option = dialog.showAndWait();
                if (option.get() == ButtonType.OK) {
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
}
