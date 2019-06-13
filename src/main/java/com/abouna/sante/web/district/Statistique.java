package com.abouna.sante.web.district;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public class Statistique extends CustomComponent implements View{
    
    public Statistique(){
        VerticalLayout layout = new VerticalLayout();
        Label lbl;
        layout.addComponent(lbl = new Label("Génération des statistiques"));
        lbl.addStyleName(Runo.LABEL_H1);
        setCompositionRoot(layout);
    }

    public void enter(ViewChangeListener.ViewChangeEvent event) {
        
    }
    
}
