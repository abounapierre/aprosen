package com.abouna.sante.web.rapport;

import com.abouna.sante.entities.CategorieIndicateur;
import com.abouna.sante.entities.Indicateur;
import com.abouna.sante.entities.ScoreQualite;
import com.abouna.sante.entities.SousCategorie;
import com.abouna.sante.entities.Trimestre;
import com.abouna.abouna.projection.RealisationTrimestrielle;
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
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public class SuiviActivitesPDF implements StreamResource.StreamSource {

    private ISuiviService suiviService;
    private final ByteArrayOutputStream os = new ByteArrayOutputStream();
    private Font titleFont, headerFont, mainFont;

    public SuiviActivitesPDF(Trimestre trimestre, Integer annee) {
        suiviService = ApplicationContextFactory.getApplicationContext().getBean(ISuiviService.class);
        Document document = null;
        titleFont = new Font();
        titleFont.setSize(12);
        titleFont.setStyle(Font.BOLD);
        headerFont = new Font();
        headerFont.setStyle(Font.BOLD);
        headerFont.setSize(7.5f);
        mainFont = new Font();
        mainFont.setSize(7.5f);
        try {
            document = new Document(PageSize.A4.rotate(), 50, 50, 50, 50);
            PdfWriter.getInstance(document, os);
            document.open();
            Paragraph p;
            PdfPCell cell;
            p = new Paragraph("RAPPORT DU " + trimestre.getCode().toUpperCase(), titleFont);
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);
            List<CategorieIndicateur> cats = suiviService.getAllCategorieIndicateur();
            for (CategorieIndicateur categorieIndicateur : cats) {
                long consommation = 0L;
                long prevision = 0L;
                Integer population = suiviService.getPopulation(categorieIndicateur, annee);
                p = new Paragraph(categorieIndicateur.getDesignation(), titleFont);
                p.setAlignment(Element.ALIGN_LEFT);
                document.add(p);
                p = new Paragraph("Population : " + population, titleFont);
                document.add(p);
                PdfPTable table = new PdfPTable(13);
                table.setSpacingBefore(10);
                table.setWidthPercentage(100);
                cell = new PdfPCell(new Phrase("Indicateurs", headerFont));
                cell.setRowspan(2);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Base du ciblage", headerFont));
                cell.setRowspan(2);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Cible mois", headerFont));
                cell.setRowspan(2);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Subvention PBF", headerFont));
                cell.setRowspan(2);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Taux", headerFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Objectifs", headerFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(2);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Score", headerFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(2);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Budget", headerFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(4);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("% achev. /cible", headerFont));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Prévision", headerFont));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Réalisation", headerFont));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Réalisation", headerFont));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Couverture", headerFont));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Consommation", headerFont));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Prévu", headerFont));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("% consommation", headerFont));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Reste - Réliquat", headerFont));
                table.addCell(cell);
                List<SousCategorie> scats = suiviService.findSousFromCategorie(categorieIndicateur);
                for (SousCategorie sousCategorie : scats) {
                    cell = new PdfPCell(new Phrase(sousCategorie.getCode(), headerFont));
                    cell.setGrayFill(0.7f);
                    cell.setColspan(13);
                    table.addCell(cell);
                    List<Indicateur> indis = suiviService.findIndicateurFromSousCategorie(sousCategorie);
                    for (Indicateur indicateur1 : indis) {
                        RealisationTrimestrielle trim = suiviService.getRealisationTrimestrielle(indicateur1, annee, trimestre);
                        table.addCell(new PdfPCell(new Phrase(indicateur1.getCode(), mainFont)));
                        table.addCell(new PdfPCell(new Phrase(indicateur1.getDesignation(), mainFont)));
                        long cibleMensuelle = Math.round(indicateur1.isEstFige() ? indicateur1.getPoids() : indicateur1.getPoids() * population / 1200);
                        int cible = (trim.getCible() == null) ? 0 : trim.getCible().intValue();
                        int subvention = (trim.getSubvention() == null) ? 0 : trim.getSubvention().intValue();
                        long realisation = (trim.getRealisation() == null) ? 0L : trim.getRealisation().longValue();
                        cell = new PdfPCell(new Phrase(String.valueOf(cibleMensuelle), mainFont));
                        cell.setBackgroundColor(BaseColor.YELLOW);
                        table.addCell(cell);
                        table.addCell(new PdfPCell(new Phrase(String.valueOf(subvention), mainFont)));
                        table.addCell(new PdfPCell(new Phrase(String.format("%.2f", (float) cible * 100 / (cibleMensuelle * 3)) + " %", mainFont)));
                        table.addCell(new PdfPCell(new Phrase(String.valueOf(cible), mainFont)));
                        table.addCell(new PdfPCell(new Phrase(String.valueOf(realisation), mainFont)));
                        table.addCell(new PdfPCell(new Phrase(String.format("%.2f", (float) realisation * 100 / cible) + " % ", mainFont)));
                        table.addCell(new PdfPCell(new Phrase(String.format("%.2f", (float) realisation * 100 / (cibleMensuelle * 3)) + " %", mainFont)));
                        long realisationReelle = suiviService.getValidRealisation(indicateur1, trimestre, annee);
                        long vv = (trim.isEstProvisionne()) ? realisationReelle * subvention : 0;
                        consommation += vv;
                        prevision += cible * subvention;
                        table.addCell(new PdfPCell(new Phrase(String.valueOf(vv), mainFont)));
                        table.addCell(new PdfPCell(new Phrase(String.valueOf(cible * subvention), mainFont)));
                        table.addCell(new PdfPCell(new Phrase(String.format("%.2f", (float) realisation * 100 / cible) + " %", mainFont)));
                        table.addCell(new PdfPCell(new Phrase(String.valueOf(cible * subvention - vv), mainFont)));
                    }

                }
                double num = suiviService.countBonusEloignement(null, null, categorieIndicateur, annee, trimestre);
                cell = new PdfPCell(new Phrase((num > 0) ? "Total " : "Total " + categorieIndicateur.getCode(), headerFont));
                cell.setGrayFill(0.7f);
                cell.setColspan(9);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(String.valueOf(consommation), headerFont));
                cell.setGrayFill(0.7f);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(String.valueOf(prevision), headerFont));
                cell.setGrayFill(0.7f);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(String.format("%.2f", consommation * 100.0 / prevision) + " %", headerFont));
                cell.setGrayFill(0.7f);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(String.valueOf(prevision - consommation), headerFont));
                cell.setGrayFill(0.7f);
                table.addCell(cell);
                if (num > 0) {
                    long bonus1 = Math.round(num / 100);
                    long bonus2 = Math.round(0 / 100);
                    consommation += bonus1;
                    prevision += bonus2;
                    cell = new PdfPCell(new Phrase("Bonus Isolement", headerFont));
                    cell.setGrayFill(0.7f);
                    cell.setColspan(9);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(String.valueOf(bonus1), headerFont));
                    cell.setGrayFill(0.7f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(String.valueOf(bonus2), headerFont));
                    cell.setGrayFill(0.7f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(String.format("%.2f", bonus1 * 100.0 / bonus2) + " %", headerFont));
                    cell.setGrayFill(0.7f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(String.valueOf(bonus2 - bonus1), headerFont));
                    cell.setGrayFill(0.7f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Total 1", headerFont));
                    cell.setGrayFill(0.7f);
                    cell.setColspan(9);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(String.valueOf(consommation), headerFont));
                    cell.setGrayFill(0.7f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(String.valueOf(prevision), headerFont));
                    cell.setGrayFill(0.7f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(String.format("%.2f", consommation * 100.0 / prevision) + " %", headerFont));
                    cell.setGrayFill(0.7f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(String.valueOf(prevision - consommation), headerFont));
                    cell.setGrayFill(0.7f);
                    table.addCell(cell);
                }
                ScoreQualite scoreQualite = suiviService.getScoreQualite(trimestre, annee);
                if (scoreQualite != null) {
                    long bonus1 = Math.round(consommation * scoreQualite.getValeur() / 100);
                    long bonus2 = Math.round(prevision * scoreQualite.getValeur() / 100);
                    consommation += bonus1;
                    prevision += bonus2;
                    cell = new PdfPCell(new Phrase("Score Qualité", headerFont));
                    cell.setGrayFill(0.7f);
                    cell.setColspan(9);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(String.valueOf(bonus1), headerFont));
                    cell.setGrayFill(0.7f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(String.valueOf(bonus2), headerFont));
                    cell.setGrayFill(0.7f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(String.format("%.2f", bonus1 * 100.0 / bonus2) + " %", headerFont));
                    cell.setGrayFill(0.7f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(String.valueOf(bonus2 - bonus1), headerFont));
                    cell.setGrayFill(0.7f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Total " + categorieIndicateur.getCode(), headerFont));
                    cell.setGrayFill(0.7f);
                    cell.setColspan(9);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(String.valueOf(consommation), headerFont));
                    cell.setGrayFill(0.7f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(String.valueOf(prevision), headerFont));
                    cell.setGrayFill(0.7f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(String.format("%.2f", consommation * 100.0 / prevision) + " %", headerFont));
                    cell.setGrayFill(0.7f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(String.valueOf(prevision - consommation), headerFont));
                    cell.setGrayFill(0.7f);
                    table.addCell(cell);
                }
                //Parametre param = suiviService.findParametreByCategorieAnnee(categorieIndicateur, annee);
//                cell = new PdfPCell(new Phrase((param != null) ? "Total ": "Total "+categorieIndicateur.getCode(), headerFont));
//                cell.setGrayFill(0.7f);
//                cell.setColspan(9);
//                table.addCell(cell);
//                cell = new PdfPCell(new Phrase(String.valueOf(consommation), headerFont));
//                cell.setGrayFill(0.7f);
//                table.addCell(cell);
//                cell = new PdfPCell(new Phrase(String.valueOf(prevision), headerFont));
//                cell.setGrayFill(0.7f);
//                table.addCell(cell);
//                cell = new PdfPCell(new Phrase(String.format("%.2f", consommation * 100.0 / prevision) + " %", headerFont));
//                cell.setGrayFill(0.7f);
//                table.addCell(cell);
//                cell = new PdfPCell(new Phrase(String.valueOf(prevision - consommation), headerFont));
//                cell.setGrayFill(0.7f);
//                table.addCell(cell);
//                if (param != null) {
//                    if (param.getBonusEloignement() > 0) {
//                        long bonus1 = Math.round(consommation * param.getBonusEloignement() / 100);
//                        long bonus2 = Math.round(prevision * param.getBonusEloignement() / 100);
//                        consommation += bonus1;
//                        prevision += bonus2;
//                        cell = new PdfPCell(new Phrase("Bonus Isolement", headerFont));
//                        cell.setGrayFill(0.7f);
//                        cell.setColspan(9);
//                        table.addCell(cell);
//                        cell = new PdfPCell(new Phrase(String.valueOf(bonus1), headerFont));
//                        cell.setGrayFill(0.7f);
//                        table.addCell(cell);
//                        cell = new PdfPCell(new Phrase(String.valueOf(bonus2), headerFont));
//                        cell.setGrayFill(0.7f);
//                        table.addCell(cell);
//                        cell = new PdfPCell(new Phrase(String.format("%.2f", bonus1 * 100.0 / bonus2) + " %", headerFont));
//                        cell.setGrayFill(0.7f);
//                        table.addCell(cell);
//                        cell = new PdfPCell(new Phrase(String.valueOf(bonus2 - bonus1), headerFont));
//                        cell.setGrayFill(0.7f);
//                        table.addCell(cell);
//                        cell = new PdfPCell(new Phrase("Total 1", headerFont));
//                        cell.setGrayFill(0.7f);
//                        cell.setColspan(9);
//                        table.addCell(cell);
//                        cell = new PdfPCell(new Phrase(String.valueOf(consommation), headerFont));
//                        cell.setGrayFill(0.7f);
//                        table.addCell(cell);
//                        cell = new PdfPCell(new Phrase(String.valueOf(prevision), headerFont));
//                        cell.setGrayFill(0.7f);
//                        table.addCell(cell);
//                        cell = new PdfPCell(new Phrase(String.format("%.2f", consommation * 100.0 / prevision) + " %", headerFont));
//                        cell.setGrayFill(0.7f);
//                        table.addCell(cell);
//                        cell = new PdfPCell(new Phrase(String.valueOf(prevision - consommation), headerFont));
//                        cell.setGrayFill(0.7f);
//                        table.addCell(cell);
//                    }
//                    if (param.getScoreQualite() > 0) {
//                        long bonus1 = Math.round(consommation * param.getScoreQualite() / 100);
//                        long bonus2 = Math.round(prevision * param.getScoreQualite() / 100);
//                        consommation += bonus1;
//                        prevision += bonus2;
//                        cell = new PdfPCell(new Phrase("Score Qualité", headerFont));
//                        cell.setGrayFill(0.7f);
//                        cell.setColspan(9);
//                        table.addCell(cell);
//                        cell = new PdfPCell(new Phrase(String.valueOf(bonus1), headerFont));
//                        cell.setGrayFill(0.7f);
//                        table.addCell(cell);
//                        cell = new PdfPCell(new Phrase(String.valueOf(bonus2), headerFont));
//                        cell.setGrayFill(0.7f);
//                        table.addCell(cell);
//                        cell = new PdfPCell(new Phrase(String.format("%.2f", bonus1 * 100.0 / bonus2) + " %", headerFont));
//                        cell.setGrayFill(0.7f);
//                        table.addCell(cell);
//                        cell = new PdfPCell(new Phrase(String.valueOf(bonus2 - bonus1), headerFont));
//                        cell.setGrayFill(0.7f);
//                        table.addCell(cell);
//                        cell = new PdfPCell(new Phrase("Total "+categorieIndicateur.getCode(), headerFont));
//                        cell.setGrayFill(0.7f);
//                        cell.setColspan(9);
//                        table.addCell(cell);
//                        cell = new PdfPCell(new Phrase(String.valueOf(consommation), headerFont));
//                        cell.setGrayFill(0.7f);
//                        table.addCell(cell);
//                        cell = new PdfPCell(new Phrase(String.valueOf(prevision), headerFont));
//                        cell.setGrayFill(0.7f);
//                        table.addCell(cell);
//                        cell = new PdfPCell(new Phrase(String.format("%.2f", consommation * 100.0 / prevision) + " %", headerFont));
//                        cell.setGrayFill(0.7f);
//                        table.addCell(cell);
//                        cell = new PdfPCell(new Phrase(String.valueOf(prevision - consommation), headerFont));
//                        cell.setGrayFill(0.7f);
//                        table.addCell(cell);
//                    }
                //}
                document.add(table);
                document.newPage();
            }

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
}
