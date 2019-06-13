package com.abouna.sante.web.district;

import com.abouna.sante.entities.CategorieIndicateur;
import com.abouna.sante.entities.Centre;
import com.abouna.sante.entities.IndicateurCentre;
import com.abouna.sante.service.ISuiviService;
import com.abouna.sante.web.ApplicationContextFactory;
import com.abouna.sante.web.template.ConfirmWindow;
import com.abouna.sante.web.template.Decision;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Runo;
import java.util.List;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public class ListeIndicateurCentre extends CustomComponent implements View {

    private BeanItemContainer<CategorieIndicateur> categorieContainer;
    private BeanItemContainer<IndicateurCentre> indicateurContainer;
    private Table indicateurTable;
    private ComboBox centreBox;
    private TextField anneeText;
    private ISuiviService suiviService;
    private VerticalLayout layout;
    private Window window;

    public ListeIndicateurCentre() {
        suiviService = ApplicationContextFactory.getApplicationContext().getBean(ISuiviService.class);
        VerticalLayout vLayout = new VerticalLayout();
        vLayout.setMargin(true);
        Label lbl = new Label("CORRESPONDANCE CENTRE DE SANTE ET INDICATEURS");
        lbl.addStyleName(Runo.LABEL_H1);
        vLayout.addComponent(lbl);
        categorieContainer = new BeanItemContainer<CategorieIndicateur>(CategorieIndicateur.class);
        centreBox = new ComboBox("Catégorie Indicateur", categorieContainer);
        centreBox.setRequired(true);
        anneeText = new TextField("Année");
        anneeText.setRequired(true);
        anneeText.setNullRepresentation("");
        HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.setMargin(true);
        hLayout.setSpacing(true);
        hLayout.addComponent(centreBox);
        hLayout.addComponent(anneeText);
        hLayout.addComponent(new Button("Afficher", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                CategorieIndicateur centre = (CategorieIndicateur) centreBox.getValue();
                Integer annee;
                try {
                    annee = Integer.parseInt(anneeText.getValue());
                } catch (NumberFormatException bfe) {
                    annee = null;
                }
                if ((centre == null) || (annee == null)) {
                    Notification.show("Erreur categoie ou annee");
                    layout.setVisible(false);
                } else {
                    List<IndicateurCentre> cats = suiviService.getCategorieFromCategorieAnnee(centre, annee);
                    indicateurContainer.removeAllItems();
                    indicateurContainer.addAll(cats);
                    layout.setVisible(true);
                }
            }
        }));
        vLayout.addComponent(hLayout);
        indicateurContainer = new BeanItemContainer<IndicateurCentre>(IndicateurCentre.class);
        indicateurTable = new Table();
        indicateurTable.setWidth("50%");
        indicateurTable.setContainerDataSource(indicateurContainer);
        indicateurTable.addGeneratedColumn("Action", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                HorizontalLayout hLayout = new HorizontalLayout();
                hLayout.addComponent(new Button("Supprimer", new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        ConfirmWindow cWindow = new ConfirmWindow("Suppression d'une activité", "Etes vous sur de vouloir supprimer cette activité?",
                                "Supprimer", "Annuler");
                        Decision decision = new Decision() {
                            @Override
                            public void yes(Button.ClickEvent event) {
                                suiviService.deleteIndicateurCentre(((IndicateurCentre) itemId).getId());
                                indicateurContainer.removeItem(itemId);
                            }
                        };
                        cWindow.setDecision(decision);
                    }
                }));
                return hLayout;
            }
        });
        indicateurTable.setVisibleColumns(new Object[]{"centre", "Action"});
        indicateurTable.setColumnHeaders(new String[]{"Centre de Santé", "Action"});
        indicateurTable.setHeight("250px");
        layout = new VerticalLayout();
        layout.addComponent(indicateurTable);
        layout.addComponent(new Button("Ajouter", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                window = new NouvelleActiviteWindow("Nouvelle Activité");
                getUI().addWindow(window);                
            }
        }));
        vLayout.addComponent(layout);
        setCompositionRoot(vLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        layout.setVisible(false);
        categorieContainer.removeAllItems();
        categorieContainer.addAll(suiviService.getAllCategorieIndicateur());
        indicateurContainer.removeAllItems();
        centreBox.setValue(null);
        anneeText.setValue(null);
    }

    private class NouvelleActiviteWindow extends Window {
        
        private ComboBox categorieBox;

        public NouvelleActiviteWindow(String caption) {
            super(caption);
            setResizable(false);
            center();
            setModal(true);
            FormLayout form = new FormLayout();
            form.setMargin(true);      
            final CategorieIndicateur cat = (CategorieIndicateur) centreBox.getValue();
            final int annee = Integer.parseInt(anneeText.getValue());
            List<Centre> all = suiviService.getAllCentre();
            List<IndicateurCentre> css = suiviService.getCategorieFromCategorieAnnee(cat, annee);
            for (IndicateurCentre indicateurCentre : css) {
                all.remove(indicateurCentre.getCentre());
            }
            categorieBox = new ComboBox("Centre de Santé", all);
            categorieBox.setRequired(true);
            form.addComponent(categorieBox);
            Button addButton = new Button("Ajouter");
            addButton.addClickListener(new Button.ClickListener() {

                @Override
                public void buttonClick(Button.ClickEvent event) {                    
                    Centre centre = (Centre) categorieBox.getValue();
                    IndicateurCentre indi = new IndicateurCentre();
                    indi.setAnnee(annee);
                    indi.setCentre(centre);
                    indi.setCategorieIndicateur(cat);
                    indi = suiviService.saveOrUpdate(indi);
                    indicateurContainer.addBean(indi);
                }
            });
            form.addComponent(addButton);
            setContent(form);
        }
    }
}
