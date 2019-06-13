package com.abouna.sante.web.district;

import com.abouna.sante.web.MainFrame;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public class PageInitiale extends CustomComponent implements View{
    
    public PageInitiale(){
        VerticalLayout layout = new VerticalLayout();
        Label lbl;
        layout.addComponent(lbl = new Label("Bienvenue dans l'application de suivi des activités de santé."));
        lbl.addStyleName(Runo.LABEL_H1);
        setCompositionRoot(layout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
       ((MainFrame) UI.getCurrent()).enableMenuLayout(true);
//       String user = (String) VaadinSession.getCurrent().getAttribute("username");
//       if(user == null)
//           getUI().getNavigator().navigateTo("");
    }
    
}
