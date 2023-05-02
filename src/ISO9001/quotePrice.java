package ISO9001;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class quotePrice {
    String name;
    double materiel;
    double as48;
    double vs2000;
    double coupe;
    double marquage;
    double numerisation;
    double analyse;
    double reactif;
    double ac;
    double materielCoupe;

    public quotePrice(String number) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(Params.basePath + "/Template/Quotes/parsedPrice/" + number + ".txt"));
            String line;
            while((line=reader.readLine()) != null) {
                String[] splitLine = line.split("#!#");
                if (splitLine[0].equals("name")) {
                    this.name = splitLine[1];
                } else if (splitLine[0].equals("coutMateriel")) {
                    this.materiel = Double.parseDouble(splitLine[1]);
                } else if (splitLine[0].equals("coutAs48")) {
                    this.as48 = Double.parseDouble(splitLine[1]);
                } else if (splitLine[0].equals("coutVs2000")) {
                    this.vs2000 = Double.parseDouble(splitLine[1]);
                } else if (splitLine[0].equals("coutCoupe")) {
                    this.coupe = Double.parseDouble(splitLine[1]);
                } else if (splitLine[0].equals("coutMarquage")) {
                    this.marquage = Double.parseDouble(splitLine[1]);
                } else if (splitLine[0].equals("coutNumerisation")) {
                    this.numerisation = Double.parseDouble(splitLine[1]);
                } else if (splitLine[0].equals("coutAnalyse")) {
                    this.analyse = Double.parseDouble(splitLine[1]);
                } else if (splitLine[0].equals("coutMaterielCoupe")) {
                    this.materielCoupe = Double.parseDouble(splitLine[1]);
                } else if (splitLine[0].equals("coutAC")) {
                    this.ac = Double.parseDouble(splitLine[1]);
                } else if (splitLine[0].equals("coutReactif")) {
                    this.reactif = Double.parseDouble(splitLine[1]);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return this.name;
    }

    public double getAs48() {
        return Math.round(this.as48 * 100.0) / 100.0;
    }

    public double getVS2000() {
        return Math.round(this.vs2000 * 100.0) / 100.0;
    }

    public double getCoupe() {
        return Math.round(this.coupe * 100.0) / 100.0;
    }

    public double getMarquage() {
        return Math.round(this.marquage * 100.0) / 100.0;
    }

    public double getNumerisation() {
        return Math.round(this.numerisation * 100.0) / 100.0;
    }

    public double getAnalyse() {
        return Math.round(this.analyse * 100.0) / 100.0;
    }

    public double getAC() {
        return Math.round(this.ac * 100.0) / 100.0;
    }

    public double getMaterielCoupe() {
        return Math.round(this.materielCoupe * 100.0) / 100.0;
    }

    public double getReactif() {
        return Math.round(this.reactif * 100.0) / 100.0;
    }
}
