package com.abouna.sante.web.district;

import com.abouna.sante.entities.Trimestre;
import com.abouna.sante.service.ISuiviService;
import com.abouna.sante.web.ApplicationContextFactory;
import com.abouna.sante.web.rapport.SuiviActivitesPDF;
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
public class SuiviActivites extends CustomComponent implements View{
    
    private ISuiviService suiviService;
    private ComboBox anneeComboBox, trimestreComboBox;
    private BeanItemContainer<Integer> anneeContainer;
    private BeanItemContainer<Trimestre> trimestreContainer;
    
    public SuiviActivites(){
        
        suiviService = ApplicationContextFactory.getApplicationContext().getBean(ISuiviService.class);
        VerticalLayout layout = new VerticalLayout();
        Label lbl;
        layout.addComponent(lbl = new Label("GÉNÉRATIONS DES RAPPORTS DE SUIVI DES ACTIVITES"));
        lbl.addStyleName(Runo.LABEL_H1);
        FormLayout fLayout = new FormLayout();
        anneeContainer = new BeanItemContainer<Integer>(Integer.class);
        fLayout.addComponent(anneeComboBox = new  ComboBox("Année", anneeContainer));
        anneeComboBox.setRequired(true);
        trimestreContainer = new BeanItemContainer<Trimestre>(Trimestre.class);
        fLayout.addComponent(trimestreComboBox = new  ComboBox("Trimestre", trimestreContainer));
        trimestreComboBox.setRequired(true);
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
                Integer annee = (Integer) anneeComboBox.getValue();
                Trimestre trimestre = (Trimestre) trimestreComboBox.getValue();
                StreamResource resource = new StreamResource(new SuiviActivitesPDF(trimestre, annee), "rapport.pdf");
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
        trimestreContainer.removeAllItems();
        trimestreContainer.addAll(suiviService.getAllTrimestre());
    }
    
}
