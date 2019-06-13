package com.abouna.sante.web.district;

import com.abouna.sante.entities.Indicateur;
import com.abouna.sante.service.ISuiviService;
import com.abouna.sante.web.ApplicationContextFactory;
import com.abouna.sante.web.rapport.EvolutionIndicateurPDF;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Runo;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public class EvolutionIndicateur extends CustomComponent implements View{

    private ISuiviService suiviService;
    private BeanItemContainer<Indicateur> indicateurContainer;
    private BeanItemContainer<Integer> anneeContainer;
    private ComboBox anneeComboBox, indicateurComboBox;
    
    public EvolutionIndicateur(){
        suiviService = ApplicationContextFactory.getApplicationContext().getBean(ISuiviService.class);
        VerticalLayout layout = new VerticalLayout();
        Label lbl;
        layout.addComponent(lbl = new Label("GÉNÉRATIONS DES RAPPORTS D'EVOLUTION D'INDICATEURS"));
        lbl.addStyleName(Runo.LABEL_H1);
        FormLayout fLayout = new FormLayout();
        indicateurContainer = new BeanItemContainer<Indicateur>(Indicateur.class);
        fLayout.addComponent(indicateurComboBox = new ComboBox("Indicateur", indicateurContainer));
        indicateurComboBox.setRequired(true);
        anneeContainer = new BeanItemContainer<Integer>(Integer.class);
        fLayout.addComponent(anneeComboBox = new  ComboBox("Année", anneeContainer));
        anneeComboBox.setRequired(true);
        fLayout.addComponent(new Button("Générer Rapport", new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                Window window = new Window();
                VerticalLayout vLayout = new VerticalLayout();
                vLayout.setSizeFull();
                //((VerticalLayout) window.getContent()).setSizeFull();
                window.setResizable(true);
                window.setWidth("800");
                window.setHeight("600");
                window.center();
                BrowserFrame e = new BrowserFrame();
                e.setSizeFull();
                // Here we create a new StreamResource which downloads our StreamSource,
                // which is our pdf.
                Indicateur trim = (Indicateur) indicateurComboBox.getValue();
                Integer annee = (Integer) anneeComboBox.getValue();
                StreamResource resource = new StreamResource(new EvolutionIndicateurPDF(trim, annee), "rapport.pdf");
                // Set the right mime type
                resource.setMIMEType("application/pdf");

                e.setSource(resource);
                vLayout.addComponent(e);
                window.setContent(vLayout);
                getUI().addWindow(window);
            }
        }));
        layout.addComponent(fLayout);
        setCompositionRoot(layout);
    }
    
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        anneeContainer.removeAllItems();
        anneeContainer.addAll(suiviService.getDistictYears());
        indicateurContainer.removeAllItems();
        indicateurContainer.addAll(suiviService.getAllIndicateur());
    }
    
}
