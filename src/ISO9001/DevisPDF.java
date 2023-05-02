package ISO9001;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DevisPDF {

    static int actualLine = 500;

    public static void createPDF(Quote quote) {
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);


            quotePrice baseP = new quotePrice("1");

            createHeader(contentStream, document);
            createFooter(contentStream);

            info(contentStream, quote.ownerName, quote.ownerSurname, quote.ownerMail, quote.title, quote.getDate());
            initDevis(contentStream);
            devisSep(contentStream, quote.getNumber());

            double total = 0.0;
            actualLine = 555;

            if (quote.coupe) {
                addLineDevis(contentStream, "Coupe", String.valueOf(quote.echantillon * quote.coupeNb), String.valueOf((baseP.coupe + baseP.materielCoupe)));
                total = total + ((baseP.coupe + baseP.materielCoupe) * quote.coupeNb * quote.echantillon);
                addSubActivity(contentStream, "Materiel", String.valueOf(quote.echantillon * quote.coupeNb), String.valueOf(baseP.materielCoupe));
                addSubActivity(contentStream, "Temps Personnel", String.valueOf(quote.echantillon * quote.coupeNb), String.valueOf(baseP.coupe));
                addSep(contentStream);
            }

            Boolean checkRistourne = false;
            Float ristourne = (float) 0.0;
            if (quote.marquageList.size() > 0) {
                addLineDevis(contentStream, "Marquage", String.valueOf(quote.marquageList.size()), "0");
            }
            for (int i=0; i<quote.marquageList.size(); i++) {
                quotePrice price;
                if (!quote.marquageList.get(i).getGrossisement().equals("NA")) {
                    Technique mrq = quote.marquageList.get(i);
                    price = new quotePrice(mrq.getNum());
                    addLineDevis(contentStream, mrq.getName(), quote.getEchantillon(), String.valueOf((price.ac + price.reactif + price.as48 + price.marquage + price.numerisation + price.vs2000)));
                    total = total + ((price.ac + price.reactif + price.as48 + price.marquage + price.numerisation + price.vs2000) * quote.echantillon);
                    addSubTitle(contentStream, "Marquage");
                    addSubActivity(contentStream, "Anticorps", quote.getEchantillon(), String.valueOf(price.getAC()));
                    addSubActivity(contentStream, "Reactifs", quote.getEchantillon(), String.valueOf(price.getReactif()));
                    addSubActivity(contentStream, "Utilisation Machine", quote.getEchantillon(), String.valueOf(price.as48));
                    addSubActivity(contentStream, "Temps Personnel", quote.getEchantillon(), String.valueOf(price.marquage));
                    addSubTitle(contentStream, "Numerisation");
                    addSubActivity(contentStream, "Utilisation Machine", quote.getEchantillon(), String.valueOf(price.getVS2000()));
                    addSubActivity(contentStream, "Temps Personnel", quote.getEchantillon(), String.valueOf(price.getNumerisation()));
                    addSep(contentStream);
                    if (quote.numerisation == false) {
                        ristourne = (float) (ristourne + price.numerisation);
                        checkRistourne = true;
                    } 
                    if (quote.vs2000 == false) {
                        ristourne = (float) (ristourne + price.getVS2000());
                        checkRistourne = true;
                    }
                } else {
                    Technique mrq = quote.marquageList.get(i);
                    price = new quotePrice(mrq.getNum());
                    addLineDevis(contentStream, mrq.getName(), quote.getEchantillon(), String.valueOf((price.ac + price.reactif + price.as48 + price.marquage + price.numerisation + price.vs2000)));
                    total = total + ((price.ac + price.reactif + price.as48 + price.marquage + price.numerisation + price.vs2000) * quote.echantillon);
                    addSubTitle(contentStream, "Marquage");
                    addSubActivity(contentStream, "Anticorps", quote.getEchantillon(), String.valueOf(price.getAC()));
                    addSubActivity(contentStream, "Reactifs", quote.getEchantillon(), String.valueOf(price.getReactif()));
                    addSubActivity(contentStream, "Utilisation Machine", quote.getEchantillon(), String.valueOf(price.as48));
                    addSubActivity(contentStream, "Temps Personnel", quote.getEchantillon(), String.valueOf(price.marquage));
                    addSep(contentStream);
                }
                if (quote.marquage == false) {
                    ristourne = (float) (ristourne + price.marquage);
                    checkRistourne = true;
                }
                if (quote.as48 == false) {
                    ristourne = (float) (ristourne + price.as48);
                    checkRistourne = true;
                }
                /*
                if (quote.marquage) {
                    if (quote.as48) {
                        if (!quote.marquageList.get(i).getGrossisement().equals("NA")) {
                            if (quote.vs2000) {
                                if (quote.numerisation) {
                                    quotePrice price = new quotePrice(mrq.getMarquage().getNum());
                                    addLineDevis(contentStream, mrq.getName(), quote.getEchantillon(), String.valueOf((price.ac + price.reactif + price.as48 + price.marquage + price.numerisation + price.vs2000)));
                                    total = total + ((price.ac + price.reactif + price.as48 + price.marquage + price.numerisation + price.vs2000) * quote.echantillon);
                                    addSubTitle(contentStream, "Marquage");
                                    addSubActivity(contentStream, "Anticorp", quote.getEchantillon(), String.valueOf(price.getAC()));
                                    addSubActivity(contentStream, "Reactifs", quote.getEchantillon(), String.valueOf(price.getReactif()));
                                    addSubActivity(contentStream, "Utilisation Machine", quote.getEchantillon(), String.valueOf(price.as48));
                                    addSubActivity(contentStream, "Temps Personnel", quote.getEchantillon(), String.valueOf(price.marquage));
                                    addSubTitle(contentStream, "Numerisation");
                                    addSubActivity(contentStream, "Utilisation Machine", quote.getEchantillon(), String.valueOf(price.getVS2000()));
                                    addSubActivity(contentStream, "Temps Personnel", quote.getEchantillon(), String.valueOf(price.getNumerisation()));
                                    addSep(contentStream);
                                } else {
                                    quotePrice price = new quotePrice(mrq.getMarquage().getNum());
                                    addLineDevis(contentStream, mrq.getName(), quote.getEchantillon(), String.valueOf((price.ac + price.reactif + price.as48 + price.marquage + price.vs2000)));
                                    total = total + ((price.ac + price.reactif + price.as48 + price.marquage + price.vs2000) * quote.echantillon);
                                    addSubTitle(contentStream, "Marquage");
                                    addSubActivity(contentStream, "Anticorp", quote.getEchantillon(), String.valueOf(price.getAC()));
                                    addSubActivity(contentStream, "Reactifs", quote.getEchantillon(), String.valueOf(price.getReactif()));
                                    addSubActivity(contentStream, "Utilisation Machine", quote.getEchantillon(), String.valueOf(price.as48));
                                    addSubActivity(contentStream, "Temps Personnel", quote.getEchantillon(), String.valueOf(price.marquage));
                                    addSubTitle(contentStream, "Numerisation");
                                    addSubActivity(contentStream, "Utilisation Machine", quote.getEchantillon(), String.valueOf(price.getVS2000()));
                                    addSep(contentStream);
                                }
                            } else {
                                if (quote.numerisation) {
                                    quotePrice price = new quotePrice(mrq.getMarquage().getNum());
                                    addLineDevis(contentStream, mrq.getName(), quote.getEchantillon(), String.valueOf((price.ac + price.reactif + price.as48 + price.marquage + price.numerisation)));
                                    total = total + ((price.ac + price.reactif + price.as48 + price.marquage + price.numerisation) * quote.echantillon);
                                    addSubTitle(contentStream, "Marquage");
                                    addSubActivity(contentStream, "Anticorp", quote.getEchantillon(), String.valueOf(price.getAC()));
                                    addSubActivity(contentStream, "Reactifs", quote.getEchantillon(), String.valueOf(price.getReactif()));
                                    addSubActivity(contentStream, "Utilisation Machine", quote.getEchantillon(), String.valueOf(price.as48));
                                    addSubActivity(contentStream, "Temps Personnel", quote.getEchantillon(), String.valueOf(price.marquage));
                                    addSubTitle(contentStream, "Numerisation");
                                    addSubActivity(contentStream, "Temps Personnel", quote.getEchantillon(), String.valueOf(price.getNumerisation()));
                                    addSep(contentStream);
                                } else {
                                    quotePrice price = new quotePrice(mrq.getMarquage().getNum());
                                    addLineDevis(contentStream, mrq.getName(), quote.getEchantillon(), String.valueOf((price.ac + price.reactif + price.as48 + price.marquage)));
                                    total = total + ((price.ac + price.reactif + price.as48 + price.marquage) * quote.echantillon);
                                    addSubTitle(contentStream, "Marquage");
                                    addSubActivity(contentStream, "Anticorp", quote.getEchantillon(), String.valueOf(price.getAC()));
                                    addSubActivity(contentStream, "Reactifs", quote.getEchantillon(), String.valueOf(price.getReactif()));
                                    addSubActivity(contentStream, "Utilisation Machine", quote.getEchantillon(), String.valueOf(price.as48));
                                    addSubActivity(contentStream, "Temps Personnel", quote.getEchantillon(), String.valueOf(price.marquage));
                                    addSubTitle(contentStream, "Numerisation");
                                }
                            }
                        } else {
                            quotePrice price = new quotePrice(mrq.getMarquage().getNum());
                            addLineDevis(contentStream, mrq.getName(), quote.getEchantillon(), String.valueOf((price.ac + price.reactif + price.as48 + price.marquage)));
                            total = total + ((price.ac + price.reactif + price.as48 + price.marquage) * quote.echantillon);
                            addSubTitle(contentStream, "Marquage");
                            addSubActivity(contentStream, "Anticorp", quote.getEchantillon(), String.valueOf(price.getAC()));
                            addSubActivity(contentStream, "Reactifs", quote.getEchantillon(), String.valueOf(price.getReactif()));
                            addSubActivity(contentStream, "Utilisation Machine", quote.getEchantillon(), String.valueOf(price.as48));
                            addSubActivity(contentStream, "Temps Personnel", quote.getEchantillon(), String.valueOf(price.marquage));
                        }
                    } else {
                        if (!quote.marquageList.get(i).getGrossisement().equals("NA")) {
                            if (quote.vs2000) {
                                if (quote.numerisation) {
                                    quotePrice price = new quotePrice(mrq.getMarquage().getNum());
                                    addLineDevis(contentStream, mrq.getName(), quote.getEchantillon(), String.valueOf((price.ac + price.reactif + price.marquage + price.numerisation + price.vs2000)));
                                    total = total + ((price.ac + price.reactif + price.marquage + price.numerisation + price.vs2000) * quote.echantillon);
                                    addSubTitle(contentStream, "Marquage");
                                    addSubActivity(contentStream, "Anticorp", quote.getEchantillon(), String.valueOf(price.getAC()));
                                    addSubActivity(contentStream, "Reactifs", quote.getEchantillon(), String.valueOf(price.getReactif()));
                                    addSubActivity(contentStream, "Temps Personnel", quote.getEchantillon(), String.valueOf(price.marquage));
                                    addSubTitle(contentStream, "Numerisation");
                                    addSubActivity(contentStream, "Utilisation Machine", quote.getEchantillon(), String.valueOf(price.getVS2000()));
                                    addSubActivity(contentStream, "Temps Personnel", quote.getEchantillon(), String.valueOf(price.getNumerisation()));
                                    addSep(contentStream);
                                } else {
                                    quotePrice price = new quotePrice(mrq.getMarquage().getNum());
                                    addLineDevis(contentStream, mrq.getName(), quote.getEchantillon(), String.valueOf((price.ac + price.reactif + price.marquage + price.vs2000)));
                                    total = total + ((price.ac + price.reactif + price.marquage + price.vs2000) * quote.echantillon);
                                    addSubTitle(contentStream, "Marquage");
                                    addSubActivity(contentStream, "Anticorp", quote.getEchantillon(), String.valueOf(price.getAC()));
                                    addSubActivity(contentStream, "Reactifs", quote.getEchantillon(), String.valueOf(price.getReactif()));
                                    addSubActivity(contentStream, "Temps Personnel", quote.getEchantillon(), String.valueOf(price.marquage));
                                    addSubTitle(contentStream, "Numerisation");
                                    addSubActivity(contentStream, "Utilisation Machine", quote.getEchantillon(), String.valueOf(price.getVS2000()));
                                    addSep(contentStream);
                                }
                            } else {
                                if (quote.numerisation) {
                                    quotePrice price = new quotePrice(mrq.getMarquage().getNum());
                                    addLineDevis(contentStream, mrq.getName(), quote.getEchantillon(), String.valueOf((price.ac + price.reactif + price.marquage + price.numerisation)));
                                    total = total + ((price.ac + price.reactif + price.marquage + price.numerisation) * quote.echantillon);
                                    addSubTitle(contentStream, "Marquage");
                                    addSubActivity(contentStream, "Anticorp", quote.getEchantillon(), String.valueOf(price.getAC()));
                                    addSubActivity(contentStream, "Reactifs", quote.getEchantillon(), String.valueOf(price.getReactif()));
                                    addSubActivity(contentStream, "Temps Personnel", quote.getEchantillon(), String.valueOf(price.marquage));
                                    addSubTitle(contentStream, "Numerisation");
                                    addSubActivity(contentStream, "Temps Personnel", quote.getEchantillon(), String.valueOf(price.getNumerisation()));
                                    addSep(contentStream);
                                } else {
                                    quotePrice price = new quotePrice(mrq.getMarquage().getNum());
                                    addLineDevis(contentStream, mrq.getName(), quote.getEchantillon(), String.valueOf((price.ac + price.reactif + price.marquage)));
                                    total = total + ((price.ac + price.reactif + price.marquage) * quote.echantillon);
                                    addSubTitle(contentStream, "Marquage");
                                    addSubActivity(contentStream, "Anticorp", quote.getEchantillon(), String.valueOf(price.getAC()));
                                    addSubActivity(contentStream, "Reactifs", quote.getEchantillon(), String.valueOf(price.getReactif()));
                                    addSubActivity(contentStream, "Temps Personnel", quote.getEchantillon(), String.valueOf(price.marquage));
                                    addSubTitle(contentStream, "Numerisation");
                                }
                            }
                        } else {
                            quotePrice price = new quotePrice(mrq.getMarquage().getNum());
                            addLineDevis(contentStream, mrq.getName(), quote.getEchantillon(), String.valueOf((price.ac + price.reactif + price.marquage)));
                            total = total + ((price.ac + price.reactif + price.marquage) * quote.echantillon);
                            addSubTitle(contentStream, "Marquage");
                            addSubActivity(contentStream, "Anticorp", quote.getEchantillon(), String.valueOf(price.getAC()));
                            addSubActivity(contentStream, "Reactifs", quote.getEchantillon(), String.valueOf(price.getReactif()));
                            addSubActivity(contentStream, "Temps Personnel", quote.getEchantillon(), String.valueOf(price.marquage));
                        }
                    }
                } else {
                    if (quote.as48) {
                        if (!quote.marquageList.get(i).getGrossisement().equals("NA")) {
                            if (quote.vs2000) {
                                if (quote.numerisation) {
                                    quotePrice price = new quotePrice(mrq.getMarquage().getNum());
                                    addLineDevis(contentStream, mrq.getName(), quote.getEchantillon(), String.valueOf((price.ac + price.reactif + price.as48 + price.numerisation + price.vs2000)));
                                    total = total + ((price.ac + price.reactif + price.as48 + price.numerisation + price.vs2000) * quote.echantillon);
                                    addSubTitle(contentStream, "Marquage");
                                    addSubActivity(contentStream, "Anticorp", quote.getEchantillon(), String.valueOf(price.getAC()));
                                    addSubActivity(contentStream, "Reactifs", quote.getEchantillon(), String.valueOf(price.getReactif()));
                                    addSubActivity(contentStream, "Utilisation Machine", quote.getEchantillon(), String.valueOf(price.as48));
                                    addSubTitle(contentStream, "Numerisation");
                                    addSubActivity(contentStream, "Utilisation Machine", quote.getEchantillon(), String.valueOf(price.getVS2000()));
                                    addSubActivity(contentStream, "Temps Personnel", quote.getEchantillon(), String.valueOf(price.getNumerisation()));
                                    addSep(contentStream);
                                } else {
                                    quotePrice price = new quotePrice(mrq.getMarquage().getNum());
                                    addLineDevis(contentStream, mrq.getName(), quote.getEchantillon(), String.valueOf((price.ac + price.reactif + price.as48 + price.vs2000)));
                                    total = total + ((price.ac + price.reactif + price.as48 + price.vs2000) * quote.echantillon);
                                    addSubTitle(contentStream, "Marquage");
                                    addSubActivity(contentStream, "Anticorp", quote.getEchantillon(), String.valueOf(price.getAC()));
                                    addSubActivity(contentStream, "Reactifs", quote.getEchantillon(), String.valueOf(price.getReactif()));
                                    addSubActivity(contentStream, "Utilisation Machine", quote.getEchantillon(), String.valueOf(price.as48));
                                    addSubTitle(contentStream, "Numerisation");
                                    addSubActivity(contentStream, "Utilisation Machine", quote.getEchantillon(), String.valueOf(price.getVS2000()));
                                    addSep(contentStream);
                                }
                            } else {
                                if (quote.numerisation) {
                                    quotePrice price = new quotePrice(mrq.getMarquage().getNum());
                                    addLineDevis(contentStream, mrq.getName(), quote.getEchantillon(), String.valueOf((price.ac + price.reactif + price.as48 + price.numerisation)));
                                    total = total + ((price.ac + price.reactif + price.as48 + price.numerisation) * quote.echantillon);
                                    addSubTitle(contentStream, "Marquage");
                                    addSubActivity(contentStream, "Anticorp", quote.getEchantillon(), String.valueOf(price.getAC()));
                                    addSubActivity(contentStream, "Reactifs", quote.getEchantillon(), String.valueOf(price.getReactif()));
                                    addSubActivity(contentStream, "Utilisation Machine", quote.getEchantillon(), String.valueOf(price.as48));
                                    addSubTitle(contentStream, "Numerisation");
                                    addSubActivity(contentStream, "Temps Personnel", quote.getEchantillon(), String.valueOf(price.getNumerisation()));
                                    addSep(contentStream);
                                } else {
                                    quotePrice price = new quotePrice(mrq.getMarquage().getNum());
                                    addLineDevis(contentStream, mrq.getName(), quote.getEchantillon(), String.valueOf((price.ac + price.reactif + price.as48)));
                                    total = total + ((price.ac + price.reactif + price.as48) * quote.echantillon);
                                    addSubTitle(contentStream, "Marquage");
                                    addSubActivity(contentStream, "Anticorp", quote.getEchantillon(), String.valueOf(price.getAC()));
                                    addSubActivity(contentStream, "Reactifs", quote.getEchantillon(), String.valueOf(price.getReactif()));
                                    addSubActivity(contentStream, "Utilisation Machine", quote.getEchantillon(), String.valueOf(price.as48));
                                    addSubTitle(contentStream, "Numerisation");
                                }
                            }
                        } else {
                            quotePrice price = new quotePrice(mrq.getMarquage().getNum());
                            addLineDevis(contentStream, mrq.getName(), quote.getEchantillon(), String.valueOf((price.ac + price.reactif + price.as48)));
                            total = total + ((price.ac + price.reactif + price.as48) * quote.echantillon);
                            addSubTitle(contentStream, "Marquage");
                            addSubActivity(contentStream, "Anticorp", quote.getEchantillon(), String.valueOf(price.getAC()));
                            addSubActivity(contentStream, "Reactifs", quote.getEchantillon(), String.valueOf(price.getReactif()));
                            addSubActivity(contentStream, "Utilisation Machine", quote.getEchantillon(), String.valueOf(price.as48));
                        }
                    } else {
                        if (!quote.grossList.get(i).equals("NA")) {
                            if (quote.vs2000) {
                                if (quote.numerisation) {
                                    quotePrice price = new quotePrice(mrq.getMarquage().getNum());
                                    addLineDevis(contentStream, mrq.getName(), quote.getEchantillon(), String.valueOf((price.ac + price.reactif + price.numerisation + price.vs2000)));
                                    total = total + ((price.ac + price.reactif + price.numerisation + price.vs2000) * quote.echantillon);
                                    addSubTitle(contentStream, "Marquage");
                                    addSubActivity(contentStream, "Anticorp", quote.getEchantillon(), String.valueOf(price.getAC()));
                                    addSubActivity(contentStream, "Reactifs", quote.getEchantillon(), String.valueOf(price.getReactif()));
                                    addSubTitle(contentStream, "Numerisation");
                                    addSubActivity(contentStream, "Utilisation Machine", quote.getEchantillon(), String.valueOf(price.getVS2000()));
                                    addSubActivity(contentStream, "Temps Personnel", quote.getEchantillon(), String.valueOf(price.getNumerisation()));
                                    addSep(contentStream);
                                } else {
                                    quotePrice price = new quotePrice(mrq.getMarquage().getNum());
                                    addLineDevis(contentStream, mrq.getName(), quote.getEchantillon(), String.valueOf((price.ac + price.reactif + price.vs2000)));
                                    total = total + ((price.ac + price.reactif + price.vs2000) * quote.echantillon);
                                    addSubTitle(contentStream, "Marquage");
                                    addSubActivity(contentStream, "Anticorp", quote.getEchantillon(), String.valueOf(price.getAC()));
                                    addSubActivity(contentStream, "Reactifs", quote.getEchantillon(), String.valueOf(price.getReactif()));
                                    addSubTitle(contentStream, "Numerisation");
                                    addSubActivity(contentStream, "Utilisation Machine", quote.getEchantillon(), String.valueOf(price.getVS2000()));
                                    addSep(contentStream);
                                }
                            } else {
                                if (quote.numerisation) {
                                    quotePrice price = new quotePrice(mrq.getMarquage().getNum());
                                    addLineDevis(contentStream, mrq.getName(), quote.getEchantillon(), String.valueOf((price.ac + price.reactif + price.numerisation)));
                                    total = total + ((price.ac + price.reactif + price.numerisation) * quote.echantillon);
                                    addSubTitle(contentStream, "Marquage");
                                    addSubActivity(contentStream, "Anticorp", quote.getEchantillon(), String.valueOf(price.getAC()));
                                    addSubActivity(contentStream, "Reactifs", quote.getEchantillon(), String.valueOf(price.getReactif()));
                                    addSubTitle(contentStream, "Numerisation");
                                    addSubActivity(contentStream, "Temps Personnel", quote.getEchantillon(), String.valueOf(price.getNumerisation()));
                                    addSep(contentStream);
                                } else {
                                    quotePrice price = new quotePrice(mrq.getMarquage().getNum());
                                    addLineDevis(contentStream, mrq.getName(), quote.getEchantillon(), String.valueOf((price.ac + price.reactif)));
                                    total = total + ((price.ac + price.reactif) * quote.echantillon);
                                    addSubTitle(contentStream, "Marquage");
                                    addSubActivity(contentStream, "Anticorp", quote.getEchantillon(), String.valueOf(price.getAC()));
                                    addSubActivity(contentStream, "Reactifs", quote.getEchantillon(), String.valueOf(price.getReactif()));
                                    addSubTitle(contentStream, "Numerisation");
                                }
                            }
                        } else {
                            quotePrice price = new quotePrice(mrq.getMarquage().getNum());
                            addLineDevis(contentStream, mrq.getName(), quote.getEchantillon(), String.valueOf((price.ac + price.reactif)));
                            total = total + ((price.ac + price.reactif) * quote.echantillon);
                            addSubTitle(contentStream, "Marquage");
                            addSubActivity(contentStream, "Anticorp", quote.getEchantillon(), String.valueOf(price.getAC()));
                            addSubActivity(contentStream, "Reactifs", quote.getEchantillon(), String.valueOf(price.getReactif()));
                        }
                    }
                }*/
            }
            
            if (quote.coupeDevis == false) {
                ristourne = ristourne + quote.echantillon * quote.coupeNb;
                checkRistourne = true;
            }

            if (quote.analyse == false) {
                ristourne = (float) (ristourne + (5.55 * quote.echantillon));
                checkRistourne = true;
            }

            System.out.println("ANALYYYYYYSE: " + quote.analyse);
            if (quote.analyse) {
                addLineDevis(contentStream, "Analyse", quote.getEchantillon(), String.valueOf(baseP.getAnalyse()));
                total = total + (baseP.getAnalyse() * quote.echantillon);
            }

            if (checkRistourne) {
                addTotal(contentStream, String.valueOf(total));
                intiAjustement(contentStream);
                addAjustement(contentStream, "Remise des fraies inputables au temps personel (technique/analytique) dans le cadre d'une collaboration académique \n (association d'un membre de la plateforme aux auteurs) de/des publications utilisant les données des marquages", ristourne.toString());
                total = total - ristourne;
            }


            if (quote.supplementCom.size() > 0) {
                for (int i=0; i<quote.supplementCom.size(); i++) {
                    addAjustement(contentStream, quote.supplementCom.get(i), quote.supplementMoney.get(i));
                    total = total + Double.parseDouble(quote.supplementMoney.get(i));
                }
            }

            addTotal(contentStream, String.valueOf(total));
            contentStream.close();
            document.save(quote.getPath() + "/Devis.pdf");

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addSep(PDPageContentStream contentStream) {
        try {
            actualLine = actualLine - 8;
            //contentStream.setNonStrokingColor(Color.gray);
            contentStream.addRect(60, actualLine, 490, 1);
            contentStream.fill();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addAjustement(PDPageContentStream contentStream, String com, String price) {
        try {
            actualLine = actualLine - 15;
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 8);
            String[] splitLine = com.split("\n");
            for(String l: splitLine) {
                contentStream.beginText();
                contentStream.newLineAtOffset(50, actualLine);
                contentStream.showText(l);
                contentStream.endText();
                actualLine = actualLine - 15;
            }
            actualLine = actualLine + 15;
            contentStream.beginText();
            contentStream.newLineAtOffset(520, actualLine);
            contentStream.showText(round(Double.parseDouble(price), 2) + " €");
            contentStream.endText();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addTotal(PDPageContentStream contentStream, String total) {
        try {
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            contentStream.setRenderingMode(RenderingMode.FILL_STROKE);
            actualLine = actualLine - 8;
//            contentStream.setNonStrokingColor(Color.black);
            contentStream.addRect(500, actualLine, 50, 1);
            actualLine = actualLine - 25;
            contentStream.fill();
            contentStream.beginText();
            contentStream.newLineAtOffset(520, actualLine);
            contentStream.showText(round(Double.parseDouble(total), 2)  + " €");
            contentStream.endText();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void intiAjustement(PDPageContentStream contentStream) {
        try  {
            actualLine = actualLine - 15;
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 8);
            contentStream.setRenderingMode(RenderingMode.FILL);
//            contentStream.setNonStrokingColor(Color.black);
            contentStream.addRect(50, actualLine, 500, 1);
            contentStream.fill();
            actualLine = actualLine - 8;
            contentStream.beginText();
            contentStream.newLineAtOffset(50, actualLine);
            contentStream.showText("Ajustement");
            contentStream.endText();
            actualLine = actualLine - 5;
  //          contentStream.setNonStrokingColor(Color.black);
            contentStream.addRect(50, actualLine, 500, 1);
            contentStream.fill();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void addLineDevis(PDPageContentStream contentStream, String desc, String quant, String price) {
        try {
//            contentStream.setNonStrokingColor(Color.BLACK);
            actualLine = actualLine - 15;
            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 8);
            contentStream.newLineAtOffset(50, actualLine);
            contentStream.showText(desc);
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(300, actualLine);
            contentStream.showText(quant);
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(400, actualLine);
            contentStream.showText(round(Double.parseDouble(price), 2) + " €");
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(520,actualLine);
            contentStream.showText(String.valueOf(round(Integer.parseInt(quant) * Float.parseFloat(price), 2)) + " €");
            contentStream.endText();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addSubTitle(PDPageContentStream contentStream, String title) {
        try {
//            contentStream.setNonStrokingColor(Color.black);
            actualLine = actualLine - 8;
            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 6);
            contentStream.newLineAtOffset(75, actualLine);
            contentStream.showText("- " + title + ":");
            contentStream.endText();
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }

    private static void addSubActivity(PDPageContentStream contentStream, String desc, String quant, String price) {
        try {
//            contentStream.setNonStrokingColor(Color.BLACK);
            actualLine = actualLine - 8;
            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 6);
            contentStream.newLineAtOffset(100, actualLine);
            contentStream.showText("- " + desc);
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(300, actualLine);
            contentStream.showText(quant);
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(400, actualLine);
            contentStream.showText(round(Double.parseDouble(price), 2) + " €");
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(520, actualLine);
            contentStream.showText(String.valueOf(round(Integer.parseInt(quant) * Float.parseFloat(price), 2)) + " €");
            contentStream.endText();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initDevis(PDPageContentStream contentStream) {
        try {
//            contentStream.setNonStrokingColor(Color.BLACK);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            contentStream.newLineAtOffset(50, 560);
            contentStream.showText("Description");
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(300, 560);
            contentStream.showText("Quantité");
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(400, 560);
            contentStream.showText("Prix unitaire");
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(520, 560);
            contentStream.showText("Total");
            contentStream.endText();
  //          contentStream.setNonStrokingColor(Color.black);
            contentStream.addRect(50, 550, 500, 1);
            contentStream.fill();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void devisSep(PDPageContentStream contentStream, String num) {
        try {
//            contentStream.setNonStrokingColor(Color.black);
            contentStream.addRect(10, 620, 590, 1);
            contentStream.fill();
            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            contentStream.newLineAtOffset(284, 605);
            contentStream.showText("Devis n°" + num);
            contentStream.endText();
  //          contentStream.setNonStrokingColor(Color.black);
            contentStream.addRect(10, 600, 590, 1);
            contentStream.fill();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void info(PDPageContentStream contentStream, String name, String surname, String mail, String title, String date) {
        try {
//            contentStream.setNonStrokingColor(Color.BLACK);
            contentStream.beginText();
            contentStream.newLineAtOffset(120, 690);
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            contentStream.showText("Client:");
            contentStream.endText();
            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 8);
            contentStream.newLineAtOffset(145, 670);
            contentStream.showText(name + " " + surname);
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(145, 650);
            contentStream.showText("Contact: " + mail);
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(320, 690);
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            contentStream.showText("Projet:");
            contentStream.endText();
            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 8);
            contentStream.newLineAtOffset(345, 670);
            contentStream.showText("Titre: " + title);
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(345, 650);
            contentStream.showText("Date: " + date);
            contentStream.endText();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createFooter(PDPageContentStream contentStream) {
        try {
//            contentStream.setNonStrokingColor(Color.BLACK);
            contentStream.addRect(10, 40, 590, 1);
            contentStream.fill();
            contentStream.beginText();
            contentStream.newLineAtOffset(210, 25);
            contentStream.showText("Laboratoire: Tel +33 (3) 80 73 75 00 - Demander poste 38-37");
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(240, 10);
            contentStream.showText("vderangere@cgfl.fr | ailie@cgfl.fr | drageot@cgfl.fr");
            contentStream.endText();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createHeader(PDPageContentStream contentStream, PDDocument document) {
        try {
            Path path = Paths.get(Params.basePath + "/Template/Image/logo_2.png");
            PDImageXObject image = PDImageXObject.createFromFile(path.toAbsolutePath().toString(), document);
            contentStream.drawImage(image, 30, 740);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 10);
            contentStream.newLineAtOffset(170, 780);
            contentStream.showText("PLATEFORME DE TRANSFERT EN BIOLOGIE DU CANCER");
            contentStream.endText();
            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 10);
            contentStream.newLineAtOffset(205, 765);
            contentStream.showText("CENTRE GEORGES-FRANCOIS LECLERC");
            contentStream.endText();
            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 8);
            contentStream.newLineAtOffset(200, 750);
            contentStream.showText("1, rue Professeur Marion - BP 77079 - 21079 Dijon cedex - France");
            contentStream.endText();
//            contentStream.setNonStrokingColor(Color.BLACK);
            contentStream.addRect(10, 730, 590, 1);
            contentStream.fill();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
