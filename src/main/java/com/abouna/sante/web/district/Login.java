package com.abouna.sante.web.district;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public class Login extends CustomComponent implements View{
    private TextField usernameField;
    private PasswordField passwordField;

    public Login() {
        VerticalLayout layout = new VerticalLayout();
        Label lbl;
        layout.addComponent(lbl = new Label("Bienvenue a l'application. Commencer par vous identifier"));
        lbl.addStyleName(Runo.LABEL_H1);
        setCompositionRoot(layout);
        FormLayout flayout = new FormLayout();
        flayout.setWidth("500px");
        flayout.addStyleName("login");
        Panel panel= new Panel("Login");
        panel.setSizeFull();
        panel.setSizeUndefined();
        flayout.addComponent(usernameField = new TextField("Username"));
        flayout.addComponent(passwordField = new PasswordField("Password"));
        panel.setContent(flayout);
        layout.addComponent(panel);
        flayout.addComponent(new Button("Connexion", new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(usernameField.getValue().equals("abouna")
                        & passwordField.getValue().equals("admin")){
                   // VaadinSession.getCurrent().s
                    getUI().getNavigator().navigateTo("pageInitiale");
                }
            }
        }));
}

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {        
    }
}