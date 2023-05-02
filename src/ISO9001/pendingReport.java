package ISO9001;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;


import java.util.List;

public class pendingReport {

    Users owner;
    String date;
    String dateFin;
    String numProject;

    public pendingReport(String path) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path + "/dateReport.txt"));
            String line;
            String lastLine = "";
            while((line=reader.readLine())!=null) {lastLine = line;}
            System.out.println(line);
            if (lastLine.contains("Init")) {
                this.dateFin = lastLine.split(">")[1].replace(" ", "");
            } else {
                this.dateFin = lastLine.split(">")[1].split(":")[0].replace(" ","");
            }
            reader.close();
            reader = new BufferedReader(new FileReader(path + "/Info.txt"));
            while((line=reader.readLine()) != null) {
                String[] splitLine = line.split("#!#");
                if (splitLine[0].equals("owner")) {
                    String[] info = splitLine[1].split("_");
                    this.owner = new Users(info[0], info[1], info[2]);
                } else if (splitLine[0].equals("nProject")) {
                    this.numProject = splitLine[1];
                } else if (splitLine[0].equals("date")) {
                    this.date = splitLine[1];
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getNproject() {
        return this.numProject;
    }

    public String getDate() {
        return this.date;
    }

    public String getDateFin() {
        return this.dateFin;
    }

    public String getOwner() throws Exception{
        return this.owner.name.get();
    }

    public void setDateFin(String date, String justi) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(Params.basePath + "/pendingReport/P." + this.numProject + "/dateReport.txt", true));
            writer.write(this.dateFin + " > " + date + " : " + justi + "\n");
            writer.close();
            this.dateFin = date;
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }
    
    
}
