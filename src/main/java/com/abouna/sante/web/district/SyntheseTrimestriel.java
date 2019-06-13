package com.abouna.sante.web.district;

import com.abouna.sante.entities.Centre;
import com.abouna.sante.entities.District;
import com.abouna.sante.entities.Trimestre;
import com.abouna.sante.service.ISuiviService;
import com.abouna.sante.web.ApplicationContextFactory;
import com.abouna.sante.web.rapport.SuiviPDFGenerator;
import com.vaadin.data.Property;
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
public class SyntheseTrimestriel extends CustomComponent implements View {

    private ISuiviService suiviService;
    private ComboBox trimestreCombo, anneeCombo, districtCombo, centreCombo;
    private BeanItemContainer<Centre> centreContainer;
    private BeanItemContainer<Trimestre> trimestreContainer;
    private BeanItemContainer<Integer> anneeContainer;
    private BeanItemContainer<District> districtContainer;

    public SyntheseTrimestriel() {
        suiviService = ApplicationContextFactory.getApplicationContext().getBean(ISuiviService.class);
        VerticalLayout layout = new VerticalLayout();
        Label lbl;
        layout.addComponent(lbl = new Label("GÉNÉRATIONS DES SYNTHÈSES TRIMESTRIELLES"));
        lbl.addStyleName(Runo.LABEL_H1);
        FormLayout fLayout = new FormLayout();
        trimestreContainer = new BeanItemContainer<Trimestre>(Trimestre.class);
        fLayout.addComponent(trimestreCombo = new ComboBox("Trimestre", trimestreContainer));
        trimestreCombo.setRequired(true);
        anneeContainer = new BeanItemContainer<Integer>(Integer.class);
        fLayout.addComponent(anneeCombo = new ComboBox("Annee", anneeContainer));
        anneeCombo.setRequired(true);
        districtContainer = new BeanItemContainer<District>(District.class);
        fLayout.addComponent(districtCombo = new ComboBox("District", districtContainer));
        centreContainer = new BeanItemContainer<Centre>(Centre.class);
        districtCombo.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                District d = (District) districtCombo.getValue();
                centreCombo.removeAllItems();
                if (d != null) {
                    centreContainer.addAll(suiviService.findCentreFromDistrict(d));
                    centreCombo.setValue(null);
                }
            }
        });
        fLayout.addComponent(centreCombo = new ComboBox("Centre de Sante", centreContainer));
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
                District dis = (District) districtCombo.getValue();
                Centre cen = (Centre) centreCombo.getValue();
                StreamResource resource = new StreamResource(new SuiviPDFGenerator(trim, annee, dis, cen), "test.pdf");
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
        districtContainer.removeAllItems();
        districtContainer.addAll(suiviService.getAllDistrict());
        anneeContainer.removeAllItems();
        anneeContainer.addAll(suiviService.getDistictYears());
        trimestreContainer.removeAllItems();
        trimestreContainer.addAll(suiviService.getAllTrimestre());
    }
}
