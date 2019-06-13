package com.abouna.sante.web.rapport;

import com.abouna.sante.entities.CategorieIndicateur;
import com.abouna.sante.entities.District;
import com.abouna.sante.entities.Indicateur;
import com.abouna.sante.entities.ScoreQualite;
import com.abouna.sante.entities.SousCategorie;
import com.abouna.sante.entities.Trimestre;
import com.abouna.abouna.projection.SyntheseActivite;
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
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public class SyntheseTrimestriel implements StreamResource.StreamSource {

    private final ByteArrayOutputStream os = new ByteArrayOutputStream();
    private Font titleFont, headerFont, mainFont;
    private transient ISuiviService suiviService;

    public SyntheseTrimestriel(Trimestre trimestre, Integer annee) {
        Document document = null;
        suiviService = ApplicationContextFactory.getApplicationContext().getBean(ISuiviService.class);
        titleFont = new Font();
        titleFont.setSize(12);
        titleFont.setStyle(Font.BOLD);
        headerFont = new Font();
        headerFont.setStyle(Font.BOLD);
        headerFont.setSize(9);
        mainFont = new Font();
        mainFont.setSize(8);
        NumberFormat nf = NumberFormat.getIntegerInstance(Locale.FRENCH);
        try {
            document = new Document(PageSize.A4.rotate(), 50, 50, 50, 50);
            PdfWriter.getInstance(document, os);
            document.open();
            Paragraph p;
            p = new Paragraph("SYNTHESE DES ACTIVITES DES FORMATIONS SANITAIRES DU " + trimestre.getCode().toUpperCase(), titleFont);
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);
            List<District> districts = suiviService.getAllDistrict();
            List<CategorieIndicateur> cats = suiviService.getAllCategorieIndicateur();
            int value = districts.size() + 5;
            for (CategorieIndicateur categorieIndicateur : cats) {
                long subsides = 0L;
                Integer population = suiviService.getPopulation(categorieIndicateur, annee);
                document.add(new Paragraph(categorieIndicateur.getCode(), titleFont));
                PdfPTable table = new PdfPTable(value);
                table.setSpacingBefore(12);
                table.setWidthPercentage(100);
                PdfPCell cell;
                // Premiere ligne
                table.addCell(new PdfPCell(new Phrase("Indicateurs", headerFont)));
                table.addCell(new PdfPCell(new Phrase("Base du ciblage", headerFont)));
                table.addCell(new PdfPCell(new Phrase("Cible mensuelle", headerFont)));
                for (District district : districts) {
                    table.addCell(new PdfPCell(new Phrase(district.getCode(), headerFont)));
                }
                table.addCell(new PdfPCell(new Phrase("Total", headerFont)));
                table.addCell(new PdfPCell(new Phrase("Subsides", headerFont)));
                List<SousCategorie> sousCats = suiviService.findSousFromCategorie(categorieIndicateur);
                for (SousCategorie sousCategorie : sousCats) {
                    cell = new PdfPCell(new Phrase(sousCategorie.getCode(), headerFont));
                    cell.setGrayFill(0.7f);
                    cell.setColspan(value);
                    table.addCell(cell);
                    List<Indicateur> inds = suiviService.findIndicateurFromSousCategorie(sousCategorie);
                    for (Indicateur indicateur : inds) {
                        table.addCell(new PdfPCell(new Phrase(indicateur.getCode(), mainFont)));
                        table.addCell(new PdfPCell(new Phrase(indicateur.getDesignation(), mainFont)));
                        cell = new PdfPCell(new Phrase(String.format("%d", Math.round(indicateur.isEstFige() ? indicateur.getPoids() : indicateur.getPoids() * population / 1200)), mainFont));
                        cell.setBackgroundColor(BaseColor.YELLOW);
                        table.addCell(cell);
                        SyntheseActivite ac = suiviService.getSynthese(indicateur, trimestre, annee);
                        HashMap<String, Long> maps = ac.getValues();
                        long total = 0;
                        for (District dis : districts) {
                            Long val = maps.get(dis.getCode());
                            if (val == null) {
                                val = 0l;
                            }
                            table.addCell(new PdfPCell(new Phrase(nf.format(val), mainFont)));
                            total += val;
                        }
                        long realisationReelle = suiviService.getValidRealisation(indicateur, trimestre, annee);
                        long vv = (ac.isEstProvisionne()) ? realisationReelle * ac.getSubvention() : 0;
                        subsides += vv;
                        table.addCell(new PdfPCell(new Phrase(nf.format(total), mainFont)));
                        table.addCell(new PdfPCell(new Phrase(nf.format(vv), mainFont)));
                    }

                }
                double num = suiviService.countBonusEloignement(null, null, categorieIndicateur, annee, trimestre);
                if (num > 0) {
                    cell = new PdfPCell(new Phrase("Bonus Isolement", headerFont));
                    cell.setGrayFill(0.7f);
                    cell.setColspan(value - 1);
                    table.addCell(cell);
                    long toto = Math.round(num / 100);
                    cell = new PdfPCell(new Phrase(String.valueOf(toto), headerFont));
                    cell.setGrayFill(0.7f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Sous - Total", headerFont));
                    cell.setGrayFill(0.7f);
                    cell.setColspan(value - 1);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(String.valueOf(subsides), headerFont));
                    cell.setGrayFill(0.7f);
                    table.addCell(cell);
                }
                ScoreQualite scoreQualite = suiviService.getScoreQualite(trimestre, annee);
                if (scoreQualite != null){
                    cell = new PdfPCell(new Phrase("Score Qualité", headerFont));
                    cell.setGrayFill(0.7f);
                    cell.setColspan(value - 1);
                    table.addCell(cell);
                    long toto = Math.round(subsides * scoreQualite.getValeur() / 100);
                    cell = new PdfPCell(new Phrase(String.valueOf(toto), headerFont));
                    cell.setGrayFill(0.7f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Total", headerFont));
                    cell.setGrayFill(0.7f);
                    cell.setColspan(value - 1);
                    table.addCell(cell);
                    subsides += toto;
                    cell = new PdfPCell(new Phrase(String.valueOf(subsides), headerFont));
                    cell.setGrayFill(0.7f);
                    table.addCell(cell);
                }
//                Parametre param = suiviService.findParametreByCategorieAnnee(categorieIndicateur, annee);
//                cell = new PdfPCell(new Phrase((param != null) ? "Total ": "Total "+categorieIndicateur.getCode(), headerFont));
//                cell.setGrayFill(0.7f);
//                cell.setColspan(value - 1);
//                table.addCell(cell);
//                cell = new PdfPCell(new Phrase(String.valueOf(subsides), headerFont));
//                cell.setGrayFill(0.7f);
//                table.addCell(cell);
//                if (param != null) {
//                    if (param.getBonusEloignement() > 0) {
//                        long bonus = Math.round(subsides * param.getBonusEloignement() / 100);
//                        subsides += bonus;
//                        cell = new PdfPCell(new Phrase("Bonus Isolement", headerFont));
//                        cell.setGrayFill(0.7f);
//                        cell.setColspan(value - 1);
//                        table.addCell(cell);
//                        cell = new PdfPCell(new Phrase(String.valueOf(bonus), headerFont));
//                        cell.setGrayFill(0.7f);
//                        table.addCell(cell);
//                        cell = new PdfPCell(new Phrase("Total 1", headerFont));
//                        cell.setGrayFill(0.7f);
//                        cell.setColspan(value - 1);
//                        table.addCell(cell);
//                        cell = new PdfPCell(new Phrase(String.valueOf(subsides), headerFont));
//                        cell.setGrayFill(0.7f);
//                        table.addCell(cell);
//                    }
//                    if (param.getScoreQualite() > 0) {
//                        long bonus = Math.round(subsides * param.getScoreQualite() / 100);
//                        subsides += bonus;
//                        cell = new PdfPCell(new Phrase("Score Qualité", headerFont));
//                        cell.setGrayFill(0.7f);
//                        cell.setColspan(value - 1);
//                        table.addCell(cell);
//                        cell = new PdfPCell(new Phrase(String.valueOf(bonus), headerFont));
//                        cell.setGrayFill(0.7f);
//                        table.addCell(cell);
//                        cell = new PdfPCell(new Phrase("Total "+categorieIndicateur.getCode(), headerFont));
//                        cell.setGrayFill(0.7f);
//                        cell.setColspan(value - 1);
//                        table.addCell(cell);
//                        cell = new PdfPCell(new Phrase(String.valueOf(subsides), headerFont));
//                        cell.setGrayFill(0.7f);
//                        table.addCell(cell);
//                    }
//                }
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
