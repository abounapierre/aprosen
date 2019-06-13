package com.abouna.sante.web.district;

import com.abouna.sante.entities.CategorieIndicateur;
import com.abouna.sante.entities.District;
import com.abouna.sante.entities.Indicateur;
import com.abouna.sante.entities.SousCategorie;
import com.abouna.sante.entities.Trimestre;
import com.abouna.sante.service.ISuiviService;
import com.abouna.sante.web.ApplicationContextFactory;
import com.abouna.sante.web.graphique.RapportGraphique;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.vaadin.addon.JFreeChartWrapper;

/**
 *
 * @author Vincent Douwe<douwevincent@yahoo.fr>
 */
public class Graphique extends CustomComponent implements View {

    private ISuiviService suiviService;
    private final BeanItemContainer<CategorieIndicateur> categorieContainer;
    private BeanItemContainer<SousCategorie> sousCategorieContainer;
    private BeanItemContainer<Indicateur> indicateurContainer;
    private final BeanItemContainer<Integer> anneeContainer;
    private final BeanItemContainer<Trimestre> trimestreContainer;
    private final BeanItemContainer<District> districtContainer;
    private ComboBox cComboBox, scComboBox, indiComboBox, anneeComboBox, trimComboBox, districtComboBox;
    private Layout vLayout;
    private transient JFreeChart chart;

    public Graphique() {
        suiviService = ApplicationContextFactory.getApplicationContext().getBean(ISuiviService.class);
        VerticalLayout layout = new VerticalLayout();
        Label lbl;
        vLayout = new VerticalLayout();
        ((VerticalLayout) vLayout).setSpacing(true);
        vLayout.setSizeFull();
        layout.setSpacing(true);
        layout.addComponent(lbl = new Label("GÉNÉRATIONS DES RAPPORTS GRAPHIQUES DE SUIVI D'INDICATEURS"));
        lbl.addStyleName(Runo.LABEL_H1);
        HorizontalLayout fLayout = new HorizontalLayout();
        fLayout.setSpacing(true);
        categorieContainer = new BeanItemContainer<CategorieIndicateur>(CategorieIndicateur.class);
        sousCategorieContainer = new BeanItemContainer<SousCategorie>(SousCategorie.class);
        indicateurContainer = new BeanItemContainer<Indicateur>(Indicateur.class);
        trimestreContainer = new BeanItemContainer<Trimestre>(Trimestre.class);
        districtContainer = new BeanItemContainer<District>(District.class);
        fLayout.addComponent(cComboBox = new ComboBox("Catégorie Indicateur", categorieContainer));
        cComboBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                sousCategorieContainer.removeAllItems();
                CategorieIndicateur cc = (CategorieIndicateur) cComboBox.getValue();
                if (cc != null) {
                    sousCategorieContainer.addAll(suiviService.findSousFromCategorie(cc));
                }
            }
        });
        cComboBox.setRequired(true);
        fLayout.addComponent(scComboBox = new ComboBox("Sous Catégorie", sousCategorieContainer));
        scComboBox.setRequired(true);
        scComboBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                indicateurContainer.removeAllItems();
                SousCategorie sc = (SousCategorie) scComboBox.getValue();
                if (sc != null) {
                    indicateurContainer.addAll(suiviService.findIndicateurFromSousCategorie(sc));
                }
            }
        });
        fLayout.addComponent(indiComboBox = new ComboBox("Indicateur", indicateurContainer));
        indiComboBox.setRequired(true);
        anneeContainer = new BeanItemContainer<Integer>(Integer.class);
        fLayout.addComponent(anneeComboBox = new ComboBox("Année", anneeContainer));
        fLayout.addComponent(trimComboBox = new ComboBox("Trimestre", trimestreContainer));
        fLayout.addComponent(districtComboBox = new ComboBox("District", districtContainer));
        fLayout.addComponent(new Button("Générer", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Indicateur indicateur = (Indicateur) indiComboBox.getValue();
                Trimestre trimestre = (Trimestre) trimComboBox.getValue();
                Integer annee = (Integer) anneeComboBox.getValue();
                District district = (District) districtComboBox.getValue();
                if (indicateur == null) {
                    Notification.show("L'indicateur est obligatoire", Notification.Type.ERROR_MESSAGE);
                } else {
                    chart = new RapportGraphique(indicateur, annee, trimestre, district).getChart();
                    JFreeChartWrapper wrapper = new JFreeChartWrapper(chart);
                    wrapper.setWidth("90%");
                    wrapper.setHeight("600px");
                    vLayout.removeAllComponents();
                    vLayout.addComponent(wrapper);
                    StreamResource resource = new StreamResource(new StreamResource.StreamSource() {
                        @Override
                        public InputStream getStream() {
                            try {
                                ByteArrayOutputStream os = new ByteArrayOutputStream();
                                BufferedImage val = chart.createBufferedImage(1600,800);
                                ChartUtilities.writeBufferedImageAsPNG(os, val);
                                return new ByteArrayInputStream(os.toByteArray());
                            } catch (IOException ex) {
                                Logger.getLogger(Graphique.class.getName()).log(Level.SEVERE, null, ex);
                                return null;
                            }
                        }
                    }, "graphique.png");
                    FileDownloader fileDownloader = new FileDownloader(resource);
                    Button btn = new Button("Exporter comme image");
                    fileDownloader.extend(btn);
                    vLayout.addComponent(btn);
                }
//                vLayout.addComponent(new Button("Exporter comme image", new Button.ClickListener() {
//                    @Override
//                    public void buttonClick(Button.ClickEvent event) {
//                        Window window = new Window();
//                        VerticalLayout vLayout = new VerticalLayout();
//                        vLayout.setSizeFull();
//                        //((VerticalLayout) window.getContent()).setSizeFull();
//                        window.setResizable(true);
//                        window.setWidth("800");
//                        window.setHeight("600");
//                        window.center();
//                        Image e = new Image();
//                        e.setSizeFull();
//                       
//                        StreamResource resource = new StreamResource(new StreamResource.StreamSource() {
//
//                            @Override
//                            public InputStream getStream() {
//                                try {
//                                    ByteArrayOutputStream os = new ByteArrayOutputStream();
//                                    BufferedImage val = chart.createBufferedImage(400, 400);
//                                    ChartUtilities.writeBufferedImageAsPNG(os, val);
//                                    return new ByteArrayInputStream(os.toByteArray());
//                                } catch (IOException ex) {
//                                    Logger.getLogger(Graphique.class.getName()).log(Level.SEVERE, null, ex);
//                                    return null;
//                                }
//                            }
//                        }, "graphique.png");
//                        // Set the right mime type
//                        resource.setMIMEType("image/png");
//                        resource.getStream().setParameter("Content-Disposition", "attachment;filename=\"graphique.png\"");
//                        e.setSource(resource);
//                        vLayout.addComponent(e);
//                        window.setContent(vLayout);
//                        getUI().addWindow(window);
//                    }
//                }));
//
            }
        }));
        layout.addComponent(fLayout);
        layout.addComponent(vLayout);
        setCompositionRoot(layout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        categorieContainer.removeAllItems();
        categorieContainer.addAll(suiviService.getAllCategorieIndicateur());
        districtContainer.removeAllItems();
        districtContainer.addAll(suiviService.getAllDistrict());
        trimestreContainer.removeAllItems();
        trimestreContainer.addAll(suiviService.getAllTrimestre());
        anneeContainer.removeAllItems();
        anneeContainer.addAll(suiviService.getDistictYears());
        vLayout.removeAllComponents();
    }
}
