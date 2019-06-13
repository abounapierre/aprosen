package com.abouna.sante.web.district;

import com.abouna.sante.entities.Trimestre;
import com.abouna.sante.service.ISuiviService;
import com.abouna.sante.web.ApplicationContextFactory;
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
import java.util.List;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public class RapportSuivi extends CustomComponent implements View{
    
    private ISuiviService suiviService;
    private ComboBox anneeCombo, trimestreCombo;
    private BeanItemContainer<Trimestre> trimestreContainer;
    private BeanItemContainer<Integer> anneeContainer;
    
    public RapportSuivi(){
        suiviService = ApplicationContextFactory.getApplicationContext().getBean(ISuiviService.class);
        VerticalLayout layout = new VerticalLayout();
        Label lbl;
        layout.addComponent(lbl = new Label("GÉNÉRATIONS DES RAPPORTS DE SUIVI D'INDICATEURS"));
        lbl.addStyleName(Runo.LABEL_H1);
        FormLayout fLayout = new FormLayout();
        trimestreContainer = new BeanItemContainer<Trimestre>(Trimestre.class);
        fLayout.addComponent(trimestreCombo = new ComboBox("Trimestre", trimestreContainer));
        trimestreCombo.setRequired(true);
        anneeContainer = new BeanItemContainer<Integer>(Integer.class);
        fLayout.addComponent(anneeCombo = new ComboBox("Annee", anneeContainer));        
        anneeCombo.setRequired(true);
        fLayout.addComponent(new Button("Générer", new Button.ClickListener() {

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
                Trimestre trim = (Trimestre) trimestreCombo.getValue();
                Integer annee = (Integer) anneeCombo.getValue();
                StreamResource resource = new StreamResource(new com.abouna.sante.web.rapport.SyntheseTrimestriel(trim, annee), "test.pdf");
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
        trimestreContainer.removeAllItems();
        trimestreContainer.addAll(suiviService.getAllTrimestre());
        anneeContainer.removeAllItems();
        anneeContainer.addAll(suiviService.getDistictYears());
    }
    
}
