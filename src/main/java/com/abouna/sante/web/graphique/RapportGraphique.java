package com.abouna.sante.web.graphique;

import com.abouna.sante.entities.District;
import com.abouna.sante.entities.Indicateur;
import com.abouna.sante.entities.Trimestre;
import com.abouna.sante.service.ISuiviService;
import com.abouna.sante.transfer.GraphiqueElement;
import com.abouna.sante.web.ApplicationContextFactory;
import java.awt.Color;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Vincent Douwe<douwevincent@yahoo.fr>
 */
public class RapportGraphique {

    private JFreeChart chart;
    private transient ISuiviService suiviService;

    public RapportGraphique(Indicateur indicateur, Integer annee, Trimestre trimestre, District district) {
        suiviService = ApplicationContextFactory.getApplicationContext().getBean(ISuiviService.class);
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        if (annee == null) {
            List<Integer> annees = suiviService.getDistictYears();
            Collections.sort(annees, new Comparator<Integer>() {
                @Override
                public int compare(Integer t, Integer t1) {
                    return t - t1;
                }
            });
            for (Integer an : annees) {
                List<GraphiqueElement> gs;
                if (district != null) {
                    gs = suiviService.getGraphiqueElement(indicateur, an, null, district);
                } else {
                    gs = suiviService.getGraphiqueElement(indicateur, an, null);
                }
                for (GraphiqueElement graphiqueElement : gs) {
                    dataset.addValue(graphiqueElement.getValue(), String.valueOf(an), graphiqueElement.getLabel());
                }
            }
        } else {
            List<Trimestre> trimestres;
            if (trimestre == null) {
                trimestres = suiviService.getAllTrimestre();
            } else {
                trimestres = new ArrayList<Trimestre>();
                trimestres.add(trimestre);
            }
            for (Trimestre trimestre1 : trimestres) {
                List<GraphiqueElement> gs;
                if (district != null) {
                    gs = suiviService.getGraphiqueElement(indicateur, annee, trimestre1, district);
                } else {
                    gs = suiviService.getGraphiqueElement(indicateur, annee, trimestre1);
                }
                for (GraphiqueElement graphiqueElement : gs) {
                    dataset.addValue(graphiqueElement.getValue(), trimestre1.getCode(), graphiqueElement.getLabel());
                }
            }
        }
        chart = ChartFactory.createBarChart(
                indicateur.getCode(), // chart title
                (district != null) ? "Centre" : "District", // domain axis label
                "Realisations", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips?
                true // URLs?
                );
        chart.setBorderVisible(false);
        BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumFractionDigits(2);
        renderer.setBaseItemLabelGenerator(
                new StandardCategoryItemLabelGenerator("{2}%", nf));
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesPaint(1, Color.ORANGE);
        renderer.setSeriesPaint(2, Color.GREEN);
        renderer.setSeriesPaint(3, Color.CYAN);
        renderer.setSeriesPaint(4, Color.MAGENTA);
        renderer.setSeriesPaint(5, Color.PINK);
        renderer.setSeriesPaint(6, Color.YELLOW);
        renderer.setSeriesPaint(7, Color.GRAY);
        renderer.setDrawBarOutline(false);
        renderer.setSeriesItemLabelsVisible(0, true);
        renderer.setDefaultEntityRadius(3);
        CategoryAxis domainAxis = chart.getCategoryPlot().getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));

        chart.getCategoryPlot().setBackgroundPaint(Color.WHITE);
    }

    public JFreeChart getChart() {
        return chart;
    }
}
