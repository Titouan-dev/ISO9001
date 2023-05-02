package ISO9001;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hpsf.Array;

public class Quote {
    String clientType;
    Boolean as48;
    Boolean vs2000;
    Boolean coupeDevis;
    Boolean marquage;
    boolean numerisation;
    boolean analyse;
    int number;
    int coupeNb = 0;
    double coupeEpaisseur = 0;
    String ownerName;
    String ownerSurname;
    String ownerMail;
    String title;
    boolean coupe;
    int echantillon;
    String espece;
    double price;
    String path;
    String date;
    List<Technique> marquageList = new ArrayList<>();
    List<String> grossList = new ArrayList<>();
    List<String> marquageNumberList = new ArrayList<>();
    List<String> supplementCom = new ArrayList<>();
    List<String> supplementMoney = new ArrayList<>();

    public Quote(String path) {
        try {
            this.path = path;
            BufferedReader reader = new BufferedReader(new FileReader(path + "/Info.txt"));
            String line;
            while((line=reader.readLine()) != null) {
                String[] splitLine = line.split("#!#");
                if (splitLine[0].equals("clientType")) {
                    this.clientType = splitLine[1];
                } else if (splitLine[0].equals("as48")) {
                    if (splitLine[1].equals("true")) {
                        this.as48 = true;
                    } else {
                        this.as48 = false;
                    }
                } else if (splitLine[0].equals("vs2000")) {
                    if (splitLine[1].equals("true")) {
                        this.vs2000 = true;
                    } else {
                        this.vs2000 = false;
                    }
                } else if (splitLine[0].equals("coupeDevis")) {
                    if (splitLine[1].equals("true")) {
                        this.coupeDevis = true;
                    } else {
                        this.coupeDevis = false;
                    }
                } else if (splitLine[0].equals("marquage")) {
                    if (splitLine[1].equals("true")) {
                        this.marquage = true;
                    } else {
                        this.marquage = false;
                    }
                }  else if (splitLine[0].equals("numerisation")) {
                    if (splitLine[1].equals("true")) {
                        this.numerisation = true;
                    }   else {
                        this.numerisation = false;
                    }
                } else if (splitLine[0].equals("analyse")) {
                    if (splitLine[1].equals("true")) {
                        this.analyse = true;
                    } else {
                        this.analyse = false;
                    }
                } else if (splitLine[0].equals("quoteNumber")) {
                    this.number = Integer.parseInt(splitLine[1]);
                } else if (splitLine[0].equals("demandeurName")) {
                    this.ownerName = splitLine[1];
                } else if (splitLine[0].equals("demandeurPrenom")) {
                    this.ownerSurname = splitLine[1];
                } else if (splitLine[0].equals("demandeurMail")) {
                    this.ownerMail = splitLine[1];
                } else if (splitLine[0].equals("titre")) {
                    this.title = splitLine[1];
                } else if (splitLine[0].equals("coupe")) {
                    if (splitLine[1].equals("true")) {
                        this.coupe = true;
                    } else {
                        this.coupe = false;
                    }
                } else if (splitLine[0].equals("nombreEchantillon")) {
                    this.echantillon = Integer.parseInt(splitLine[1]);
                } else if (splitLine[0].equals("espece")) {
                    this.espece = splitLine[1];
                } else if (splitLine[0].equals("nbCoupe")) {
                    this.coupeNb = Integer.parseInt(splitLine[1]);
                } else if (splitLine[0].equals("epaisseur")) {
                    this.coupeEpaisseur = Double.parseDouble(splitLine[1]);
                } else if (splitLine[0].equals("date")) {
                    this.date = splitLine[1];
                }
            }
            reader.close();
            reader = new BufferedReader(new FileReader(path + "/marquageList.txt"));
            while((line=reader.readLine()) != null) {
                String[] splitLine = line.split("#!#");
                Marquage m = new Marquage(splitLine[0]);
                m.setGrossisement(splitLine[1]);
                m.setRevelation(splitLine[2]);
                marquageList.add(m);
            }
            reader.close();
            reader = new BufferedReader(new FileReader(path + "/Suplement.txt"));
            while((line=reader.readLine()) != null) {
                String[] splitLine = line.split("#!#");
                supplementCom.add(splitLine[0]);
                supplementMoney.add(splitLine[1]);
            }
            reader.close();
            calculatePrice();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calculatePrice() {
        double price = 0;
        quotePrice baseP = new quotePrice("1");
        System.out.println("Nombre echantillon:" + this.echantillon);
        for (int i=0; i<marquageList.size(); i++) {
            Technique m = marquageList.get(i);
            System.out.println("Calulated price num: " + m);
            quotePrice p = new quotePrice(m.getNum());
            if (this.marquage) {
                if (this.as48) {
                   price = price + ((p.getMarquage() + p.getAC() + p.getReactif() + p.getAs48()) * this.echantillon);
                   System.out.println("marquage as48");
                } else {
                    price = price + ((p.getMarquage() + p.getAC() + p.getReactif()) * this.echantillon);
                   System.out.println("marquage");
                }
            } else {
                if (this.as48) {
                   price = price + ((p.getAC() + p.getReactif() + p.getAs48()) * this.echantillon);
                   System.out.println("marquage: " + (p.getAC() + p.getReactif() + p.getAs48()));
                   System.out.println("as48");
                } else {
                    price = price + ((p.getAC() + p.getReactif()) * this.echantillon);
                   System.out.println("just reactif");
                }
            }
            System.out.println(m.getName() + " marquage: " + price);
            if (!marquageList.get(i).getGrossisement().equals("NA")) {
                if (this.numerisation && this.vs2000) {
                    price = price + ((baseP.getVS2000() + baseP.getNumerisation()) * this.echantillon);
                } else if (this.numerisation) {
                    price = price + (baseP.getNumerisation() * this.echantillon);
                } else if (this.vs2000) {
                    price = price + (baseP.getVS2000() * this.echantillon);
                }
            }
            
        }
        System.out.println(price);
        if (this.coupe && this.coupeDevis) {
            price = price + ((baseP.getMaterielCoupe() + baseP.getCoupe()) * this.echantillon * this.coupeNb);
        } else if (this.coupe) {
            price = price + (baseP.getMaterielCoupe() * this.echantillon * this.coupeNb);
        }
        if (this.analyse) {
            price = price + (baseP.getAnalyse() * this.echantillon * this.coupeNb);
        }
        this.price = price;
    }

    public String getNumber() {
        return String.valueOf(this.number);
    }

    public String getClient() {
        return this.clientType;
    }

    public String getMarquageNumber() {
        return String.valueOf(this.marquageList.size());
    }

    public String getTitle() {
        return this.title;
    }

    public String getDemandeur() {
        return this.ownerName + " " + this.ownerSurname;
    }

    public String getEspece() {
        return this.espece;
    }

    public String getCoupe() {
        return String.valueOf(this.coupeNb);
    }

    public String getEchantillon() {
        return String.valueOf(this.echantillon);
    }

    public String getPrice() {
        return String.valueOf(Math.round(this.price * 100.0) / 100.0);
    }

    public String getPath() {
        return this.path;
    }

    public String getDate() {
        return this.date;
    }
}