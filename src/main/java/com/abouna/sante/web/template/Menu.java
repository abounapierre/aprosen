package com.abouna.sante.web.template;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public class Menu extends CustomComponent{
    
    private MenuBar.MenuItem rapport;
    private MenuBar.MenuItem user;
    
    
    public Menu(){
        VerticalLayout vLayout = new VerticalLayout();
        vLayout.addStyleName("navbar");
        vLayout.addStyleName("navbar-default");
        vLayout.addStyleName("clearfix");
        MenuBar bar = new MenuBar();
        user = bar.addItem("Utilisateurs", new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                getUI().getNavigator().navigateTo("utilisateurs");
            }
        });
        bar.addItem("District de Sante", new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                getUI().getNavigator().navigateTo("districts");
            }
        });
        MenuBar.MenuItem bb = bar.addItem("Centre de Santé", null, null);
        bb.addItem("Centre de sante", new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                getUI().getNavigator().navigateTo("centres");
            }
        });
        bb.addItem("Activités", new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                getUI().getNavigator().navigateTo("activites");
            }
        });
        bar.addItem("Categorie Indicateur", new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                getUI().getNavigator().navigateTo("categories");
            }
        });
        bar.addItem("Sous Categories", new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                getUI().getNavigator().navigateTo("scategories");
            }
        });
        bar.addItem("Indicateur", new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                getUI().getNavigator().navigateTo("indicateurs");
            }
        });
        MenuBar.MenuItem donnees = bar.addItem("Données", null, null);
        donnees.addItem("Cible", new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                getUI().getNavigator().navigateTo("cibles");
            }
        });
        donnees.addItem("Subvention", new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                getUI().getNavigator().navigateTo("subventions");
            }
        });
        donnees.addItem("Realisation", new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                getUI().getNavigator().navigateTo("realisations");
            }
        });
        donnees.addItem("Bonus éloignement", new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                getUI().getNavigator().navigateTo("bonusEloignement");
            }
        });
        donnees.addItem("Score de qualité",new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                getUI().getNavigator().navigateTo("scoreQualite");
            }
        });
        rapport = bar.addItem("Rapports", null, null);
        rapport.addItem("Synthèse Trimestrielle", new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                getUI().getNavigator().navigateTo("syntheseTrimestriel");
            }
        });
        rapport.addItem("Rapports Suivi", new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                getUI().getNavigator().navigateTo("rapportsSuivi");
            }
        });
        rapport.addItem("Suivi Activités", new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                getUI().getNavigator().navigateTo("suiviActivites");
            }
        });
        rapport.addItem("Evolution Indicateurs", new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                getUI().getNavigator().navigateTo("evolutionIndicateur");
            }
        });
        bar.addItem("Statistiques", new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                getUI().getNavigator().navigateTo("statistiques");
            }
        });
        bar.addItem("Déconnexion",new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                VaadinSession.getCurrent().setAttribute("user", null);
                getUI().getNavigator().navigateTo("");
            }
        });
        vLayout.addComponent(bar);
        setCompositionRoot(vLayout);
    }
    
    
    public void initMenu(){
        String role = (String) VaadinSession.getCurrent().getAttribute("role");
        if(role.contentEquals("admin")){
            user.setVisible(true);
        }else{
            user.setVisible(false);
        }
    }    

}
