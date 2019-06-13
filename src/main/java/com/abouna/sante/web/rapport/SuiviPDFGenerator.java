package com.abouna.sante.web.rapport;

import com.abouna.sante.entities.CategorieIndicateur;
import com.abouna.sante.entities.Centre;
import com.abouna.sante.entities.District;
import com.abouna.sante.entities.Indicateur;
import com.abouna.sante.entities.Mois;
import com.abouna.sante.entities.ScoreQualite;
import com.abouna.sante.entities.SousCategorie;
import com.abouna.sante.entities.Trimestre;
import com.abouna.abouna.projection.RealisationProjection;
import com.abouna.sante.service.ISuiviService;
import com.abouna.sante.service.impl.SuiviServiceImpl;
import com.abouna.sante.web.ApplicationContextFactory;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.vaadin.server.StreamResource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vincent Douwe<douwevincent@yahoo.fr>
 */
public class SuiviPDFGenerator implements StreamResource.StreamSource {

    private final ByteArrayOutputStream os = new ByteArrayOutputStream();
    private ISuiviService suiviService;
    private Font headerFont, corpsFont, titreFont;

    public SuiviPDFGenerator(Trimestre trimestre, Integer annee, District district, Centre centre) {
        Document document = null;
        headerFont = new Font();
        titreFont = new Font();
        titreFont.setStyle(Font.BOLD);
        titreFont.setSize(12);
        corpsFont = new Font();
        corpsFont.setSize(9);
        headerFont.setStyle(Font.BOLD);
        headerFont.setSize(9);
        suiviService = ApplicationContextFactory.getApplicationContext().getBean(ISuiviService.class);
        try {
            document = new Document(PageSize.A4.rotate(), 50, 50, 50, 50);
            PdfWriter.getInstance(document, os);
            document.open();
            Paragraph p;
            p = new Paragraph("RAPPORT DU " + trimestre.getCode().toUpperCase(), titreFont);
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);
            if (district != null) {
                document.add(new Paragraph("District : " + district.getCode(), titreFont));
            }
            if (centre != null) {
                document.add(new Paragraph("Centre de sante : " + centre.getNom(), titreFont));
            }
            PdfPTable table = new PdfPTable(9);
            table.setSpacingBefore(12f);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{3, 1, 1, 1, 1, 1, 1, 1, 1});
            PdfPCell cell;
            // premiere ligne
            cell = new PdfPCell(new Phrase("Indicateurs", headerFont));
            cell.setRowspan(2);
            cell.setGrayFill(0.7f);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("PU", headerFont));
            cell.setGrayFill(0.7f);
            cell.setRowspan(2);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(trimestre.getCode(), headerFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setGrayFill(0.7f);
            cell.setColspan(10);
            table.addCell(cell);
            // deuxieme ligne
            List<Mois> moiss = suiviService.getMoisFromTrimestre(trimestre);
            for (Mois mois : moiss) {
                cell = new PdfPCell(new Phrase(mois.getNom(), headerFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setGrayFill(0.7f);
                table.addCell(cell);
            }
            cell = new PdfPCell(new Phrase("Trimestre", headerFont));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setGrayFill(0.7f);
            table.addCell(cell);

            // troisieme ligne            
            table.addCell(createCell("Indicateurs", headerFont, 0.7f));
            table.addCell(createCell("Subsides", headerFont, 0.7f));
            for (int i = 0; i < 3; i++) {
                table.addCell(createCell("Realisation", headerFont, 0.7f));
            }
            table.addCell(createCell("Total", headerFont, 0.7f));
            table.addCell(createCell("Prix total", headerFont, 0.7f));
            table.addCell(createCell("Prevu", headerFont, 0.7f));
            table.addCell(createCell("Pourcentage", headerFont, 0.7f));
            // Les autres lignes 
            List<CategorieIndicateur> cats = suiviService.getAllCategorieIndicateur();
            for (CategorieIndicateur categorieIndicateur : cats) {
                long prix = 0L;
                cell = createCell(categorieIndicateur.getCode(), headerFont, 0.7f);
                cell.setColspan(12);
                table.addCell(cell);
                List<SousCategorie> sousCats = suiviService.findSousFromCategorie(categorieIndicateur);
                for (SousCategorie sousCategorie : sousCats) {
                    cell = createCell(sousCategorie.getCode(), headerFont, 0.7f);
                    cell.setColspan(12);
                    table.addCell(cell);
                    List<Indicateur> inds = suiviService.findIndicateurFromSousCategorie(sousCategorie);
                    for (Indicateur indicateur : inds) {
                        RealisationProjection rp = suiviService.getRealisation(indicateur, trimestre, annee, district, centre);
                        table.addCell(new PdfPCell(new Phrase(rp.getIndicateur(), corpsFont)));
                        cell = new PdfPCell(new Phrase(rp.getSubvention().toString(), corpsFont));
                        cell.setBackgroundColor(BaseColor.YELLOW);
                        table.addCell(cell);
                        for (Mois mois : moiss) {
                            Long rea = rp.getRealisations().get(mois.getNom());
                            if (rea == null) {
                                rea = 0L;
                            }
                            table.addCell(new PdfPCell(new Phrase(rea.toString(), corpsFont)));
                        }
                        table.addCell(new PdfPCell(new Phrase(String.valueOf(rp.getRealisationTotale()), corpsFont)));
                        //System.out.println(" null 1 "+rp.getTotal()+" null 2 "+rp.getSubvention()+ " ok");
                        long tmp = (rp.isEstProvisionne()) ? rp.getTotal() * rp.getSubvention() : 0;
                        prix += tmp;
                        table.addCell(new PdfPCell(new Phrase(String.valueOf(tmp), corpsFont)));
                        table.addCell(new PdfPCell(new Phrase(rp.getCible().toString(), corpsFont)));
                        table.addCell(new PdfPCell(new Phrase(String.format("%.2f", (float) rp.getTotal() * 100 / rp.getCible()) + " %", corpsFont)));
                    }
                }
                cell = new PdfPCell(new Phrase("Total :", headerFont));
                cell.setColspan(6);
                cell.setGrayFill(0.7f);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(String.valueOf(prix), headerFont));
                cell.setColspan(3);
                cell.setGrayFill(0.7f);
                table.addCell(cell);
                double num = suiviService.countBonusEloignement(district, centre, categorieIndicateur, annee, trimestre);
                long value = prix;
                if (num > 0) {
                    cell = new PdfPCell(new Phrase("Bonus d'isolement :", headerFont));
                    cell.setColspan(6);
                    cell.setGrayFill(0.7f);
                    table.addCell(cell);
                    long isolement = Math.round(num / 100);
                    cell = new PdfPCell(new Phrase(String.valueOf(isolement), headerFont));
                    cell.setColspan(3);
                    cell.setGrayFill(0.7f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Sous - Total:", headerFont));
                    cell.setColspan(6);
                    cell.setGrayFill(0.7f);
                    table.addCell(cell);
                    value += isolement;
                    cell = new PdfPCell(new Phrase(String.valueOf(value), headerFont));
                    cell.setColspan(3);
                    cell.setGrayFill(0.7f);
                    table.addCell(cell);
                }
                ScoreQualite scoreQualite = suiviService.getScoreQualite(trimestre, annee);
                if (scoreQualite != null){
                    cell = new PdfPCell(new Phrase("Score Qualité", headerFont));
                    cell.setGrayFill(0.7f);
                    cell.setColspan(6);
                    table.addCell(cell);
                    long toto = Math.round(value * scoreQualite.getValeur() / 100);
                    cell = new PdfPCell(new Phrase(String.valueOf(toto), headerFont));
                    cell.setGrayFill(0.7f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Total", headerFont));
                    cell.setGrayFill(0.7f);
                    cell.setColspan(6);
                    table.addCell(cell);
                    value += toto;
                    cell = new PdfPCell(new Phrase(String.valueOf(value), headerFont));
                    cell.setGrayFill(0.7f);
                    table.addCell(cell);
                }
//                Parametre param = suiviService.findParametreByCategorieAnnee(categorieIndicateur, annee);
//
//                if (param != null) {
//                    long value = prix;
//                    if (param.getBonusEloignement() > 0) {
//                        cell = new PdfPCell(new Phrase("Bonus d'isolement :", headerFont));
//                        cell.setColspan(6);
//                        cell.setGrayFill(0.7f);
//                        table.addCell(cell);
//                        long isolement = Math.round(prix * param.getBonusEloignement() / 100);
//                        cell = new PdfPCell(new Phrase(String.valueOf(isolement), headerFont));
//                        cell.setColspan(3);
//                        cell.setGrayFill(0.7f);
//                        table.addCell(cell);
//                        cell = new PdfPCell(new Phrase("Total 1:", headerFont));
//                        cell.setColspan(6);
//                        cell.setGrayFill(0.7f);
//                        table.addCell(cell);
//                        value += isolement;
//                        cell = new PdfPCell(new Phrase(String.valueOf(value), headerFont));
//                        cell.setColspan(3);
//                        cell.setGrayFill(0.7f);
//                        table.addCell(cell);
//                    }
//                    if (param.getScoreQualite() > 0) {
//                        cell = new PdfPCell(new Phrase("Score Qualité :", headerFont));
//                        cell.setColspan(6);
//                        cell.setGrayFill(0.7f);
//                        table.addCell(cell);
//                        long qualite = Math.round(value * param.getScoreQualite() / 100);
//                        cell = new PdfPCell(new Phrase(String.valueOf(qualite), headerFont));
//                        cell.setColspan(3);
//                        cell.setGrayFill(0.7f);
//                        table.addCell(cell);
//                        value += qualite;
//                        cell = new PdfPCell(new Phrase("Total : "+categorieIndicateur.getCode(), headerFont));
//                        cell.setColspan(6);
//                        cell.setGrayFill(0.7f);
//                        table.addCell(cell);
//                        cell = new PdfPCell(new Phrase(String.valueOf(value), headerFont));
//                        cell.setColspan(3);
//                        cell.setGrayFill(0.7f);
//                        table.addCell(cell);
//                    }
//                }
                cell = new PdfPCell(new Phrase("  ", corpsFont));
                cell.setColspan(9);
                table.addCell(cell);
            }
            document.add(table);

        } catch (Exception e) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }

    @Override
    public InputStream getStream() {
        return new ByteArrayInputStream(os.toByteArray());
    }

    private PdfPCell createCell(String indicateurs, Font titleFont, float f) {
        PdfPCell cell = new PdfPCell(new Phrase(indicateurs, titleFont));
        cell.setGrayFill(f);
        return cell;
    }
}
