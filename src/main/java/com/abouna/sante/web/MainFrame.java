package com.abouna.sante.web;

import com.abouna.sante.web.district.EvolutionIndicateur;
import com.abouna.sante.web.district.Graphique;
import com.abouna.sante.web.district.ListeBonusEloignement;
import com.abouna.sante.web.district.ListeCategorieIndicateur;
import com.abouna.sante.web.district.ListeCentre;
import com.abouna.sante.web.district.ListeCible;
import com.abouna.sante.web.district.ListeDistrict;
import com.abouna.sante.web.district.ListeIndicateur;
import com.abouna.sante.web.district.ListeIndicateurCentre;
import com.abouna.sante.web.district.ListeMois;
import com.abouna.sante.web.district.ListeRealisation;
import com.abouna.sante.web.district.ListeScoreQualite;
import com.abouna.sante.web.district.ListeSousCategorie;
import com.abouna.sante.web.district.ListeSubvention;
import com.abouna.sante.web.district.ListeTrimestre;
import com.abouna.sante.web.district.ListeUtilisateurs;
import com.abouna.sante.web.district.PageInitiale;
import com.abouna.sante.web.district.RapportSuivi;
import com.abouna.sante.web.district.SimpleLoginView;
import com.abouna.sante.web.district.SuiviActivites;
import com.abouna.sante.web.district.SyntheseTrimestriel;
import com.abouna.sante.web.template.Menu;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
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
@Theme("suivi")
public class MainFrame extends UI{
    private CustomComponent menuLayout;
    private boolean state;
    private Menu menu;
    

    public CustomComponent getMenuLayout() {
        return menuLayout;
    }

    public void setMenuLayout(CustomComponent menuLayout) {
        this.menuLayout = menuLayout;
    }

    

    public boolean isState() {
        state = (Boolean)VaadinSession.getCurrent().getAttribute("state");
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
    
     public void enableMenuLayout(boolean state) {
        if (state) {
            ((Menu) menuLayout).initMenu();
        }
        menuLayout.setVisible(state);
    }

    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle("Le suivi des activites de sante");
        setSizeFull();
        VerticalLayout mainLayout = new VerticalLayout();
        
        VerticalLayout header = new VerticalLayout();
        VerticalLayout content = new VerticalLayout();
        
        header.setSizeFull();
        header.addStyleName("page-header");
        //Label lbl;
        //header.addComponent(lbl=new Label("APPLICATION DU SUIVI DES ACTIVITÉS DE SANTÉ POUR APROSEN AAP DE MAROUA"));
        //lbl.addStyleName(Runo.LABEL_H1);
        mainLayout.addComponent(header);
        menuLayout = new Menu();
        setContent(mainLayout);
        VerticalLayout dynamique = new VerticalLayout();
        
        dynamique.setMargin(true);
        dynamique.setSizeFull();
        content.setSizeFull();
        menuLayout.setVisible(false);
        content.addComponent(menuLayout);
        Navigator navig = new Navigator(this, dynamique);
        navig.addView("", new SimpleLoginView());
        navig.addView("pageInitiale", new PageInitiale());
        navig.addView("utilisateurs", new ListeUtilisateurs());
        navig.addView("districts", new ListeDistrict());
        navig.addView("centres", new ListeCentre());
        navig.addView("scoreQualite", new ListeScoreQualite());
        navig.addView("bonusEloignement",new ListeBonusEloignement());
        navig.addView("categories", new ListeCategorieIndicateur());
        navig.addView("scategories", new ListeSousCategorie());
        navig.addView("activites", new ListeIndicateurCentre());
        navig.addView("indicateurs", new ListeIndicateur());
        navig.addView("mois", new ListeMois());
        navig.addView("trimestres", new ListeTrimestre());
        navig.addView("cibles", new ListeCible());
        navig.addView("subventions", new ListeSubvention());
        navig.addView("realisations", new ListeRealisation());
        navig.addView("rapportsSuivi", new RapportSuivi());
        navig.addView("syntheseTrimestriel", new SyntheseTrimestriel());
        navig.addView("evolutionIndicateur", new EvolutionIndicateur());
        navig.addView("suiviActivites", new SuiviActivites());
        navig.addView("statistiques", new Graphique());
        mainLayout.addComponent(content);
        mainLayout.addComponent(dynamique);
    }
    
}
