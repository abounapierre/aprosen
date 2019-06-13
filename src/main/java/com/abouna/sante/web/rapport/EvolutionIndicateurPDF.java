package com.abouna.sante.web.rapport;

import com.abouna.sante.entities.Centre;
import com.abouna.sante.entities.District;
import com.abouna.sante.entities.Indicateur;
import com.abouna.sante.entities.Trimestre;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public class EvolutionIndicateurPDF implements StreamResource.StreamSource {

    private ISuiviService suiviService;
    private final ByteArrayOutputStream os = new ByteArrayOutputStream();
    private Font titleFont, headerFont, mainFont;

    public EvolutionIndicateurPDF(Indicateur indicateur, Integer annee) {
        suiviService = ApplicationContextFactory.getApplicationContext().getBean(ISuiviService.class);
        Document document = null;
        titleFont = new Font();
        titleFont.setSize(12);
        titleFont.setStyle(Font.BOLD);
        headerFont = new Font();
        headerFont.setStyle(Font.BOLD);
        headerFont.setSize(10);
        mainFont = new Font();
        mainFont.setSize(9);
        try {
            document = new Document(PageSize.A4.rotate(), 50, 50, 50, 50);
            PdfWriter.getInstance(document, os);
            document.open();
            Paragraph p;
            p = new Paragraph("EVOLUTION DES ACTIVITES " + indicateur.getCode().toUpperCase(), titleFont);
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);

            List<Trimestre> trims = suiviService.getAllTrimestre(annee);
            int taille = trims.size();
            PdfPTable table = new PdfPTable(4 + 2 * taille);
            table.setSpacingBefore(10);
            table.setWidthPercentage(100);
            PdfPCell cell;
            cell = new PdfPCell(new Phrase("District", headerFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.YELLOW);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setRowspan(2);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("FOSA", headerFont));
            cell.setBackgroundColor(BaseColor.YELLOW);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setRowspan(2);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Population", headerFont));
            cell.setBackgroundColor(BaseColor.YELLOW);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setRowspan(2);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Cible", headerFont));
            cell.setBackgroundColor(BaseColor.YELLOW);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setRowspan(2);
            table.addCell(cell);
            for (Trimestre trimestre : trims) {
                cell = new PdfPCell(new Phrase(trimestre.getCode(), headerFont));
                cell.setBackgroundColor(BaseColor.YELLOW);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(2);
                table.addCell(cell);
                // Le nombre de realisations pour un trimestre, une annee, un indicateur et un centre                
            }
            long populationTotale = 0L;
            Map<String, Long> realisationTotale = new HashMap<String, Long>();
            for (int i = 0; i < taille; i++) {
                cell = new PdfPCell(new Phrase("Nombre", headerFont));
                cell.setBackgroundColor(BaseColor.YELLOW);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Taux", headerFont));
                cell.setBackgroundColor(BaseColor.YELLOW);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }
            List<District> districts = suiviService.getAllDistrict();
            for (District district : districts) {
                int population = 0;
                Map<String, Long> maps = new HashMap<String, Long>();
                List<Centre> centres = suiviService.findCentreFromDistrict(district);
                cell = new PdfPCell(new Phrase(district.getCode().toUpperCase(), headerFont));
                cell.setBackgroundColor(BaseColor.YELLOW);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setRowspan(centres.size() + 1);
                table.addCell(cell);
                for (Centre centre : centres) {
                    population += centre.getPopulation();
                    table.addCell(new PdfPCell(new Phrase(centre.getNom(), mainFont)));
                    table.addCell(new PdfPCell(new Phrase(String.valueOf(centre.getPopulation()), mainFont)));
                    long value;
                    Map<String, Long> reas = suiviService.getRealisationTrimestrielle(indicateur, centre, annee);
                    value = indicateur.isEstFige() ? 0 : Math.round(indicateur.getPoids() * centre.getPopulation() * 3 / 1200);
                    table.addCell(new PdfPCell(new Phrase(String.valueOf(value), mainFont)));
                    for (Trimestre trimestre : trims) {
                        Long rea = reas.get(trimestre.getCode());
                        if (rea == null) {
                            rea = 0L;
                        }
                        Long val = maps.get(trimestre.getCode());
                        if (val == null) {
                            maps.put(trimestre.getCode(), rea);
                        } else {
                            maps.put(trimestre.getCode(), val + rea);
                        }
                        table.addCell(new Phrase(String.valueOf(rea), mainFont));
                        table.addCell(new Phrase(String.format("%.2f", rea * 100.0 / value) + " %", mainFont));
                    }
                }
                // La derniere ligne
                table.addCell(new Phrase("DISTRICT", mainFont));
                table.addCell(new Phrase(String.valueOf(population), mainFont));
                populationTotale += population;
                long value = Math.round(indicateur.getPoids() * population * 3 / 1200);
                table.addCell(new Phrase(String.valueOf(value), mainFont));
                for (Trimestre trimestre : trims) {
                    table.addCell(new Phrase(String.valueOf(maps.get(trimestre.getCode())), mainFont));
                    table.addCell(new Phrase(String.format("%.2f", maps.get(trimestre.getCode()) * 100.0 / value) + " %", mainFont));
                    if (realisationTotale.get(trimestre.getCode()) == null) {
                        realisationTotale.put(trimestre.getCode(), maps.get(trimestre.getCode()));
                    } else {
                        realisationTotale.put(trimestre.getCode(), realisationTotale.get(trimestre.getCode()) + maps.get(trimestre.getCode()));
                    }
                }
            }
            cell = new PdfPCell(new Phrase("Total:", headerFont));
            cell.setBackgroundColor(BaseColor.YELLOW);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(2);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(String.valueOf(populationTotale), mainFont));
            cell.setBackgroundColor(BaseColor.YELLOW);
            table.addCell(cell);
            long value = Math.round(populationTotale * indicateur.getPoids() / 1200);
            cell = new PdfPCell(new Phrase(String.valueOf(value), mainFont));
            cell.setBackgroundColor(BaseColor.YELLOW);
            table.addCell(cell);
            for (Trimestre trimestre : trims) {
                cell = new PdfPCell(new Phrase(String.valueOf(realisationTotale.get(trimestre.getCode())), mainFont));
                cell.setBackgroundColor(BaseColor.YELLOW);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(String.format("%.2f", realisationTotale.get(trimestre.getCode()) * 100.0 / value) + " %", mainFont));
                cell.setBackgroundColor(BaseColor.YELLOW);
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
}
