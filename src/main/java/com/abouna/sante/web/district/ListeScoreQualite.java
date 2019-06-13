package com.abouna.sante.web.district;

import com.abouna.sante.entities.ScoreQualite;
import com.abouna.sante.service.ISuiviService;
import com.abouna.sante.web.ApplicationContextFactory;
import com.abouna.sante.web.template.ConfirmWindow;
import com.abouna.sante.web.template.Decision;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.DoubleRangeValidator;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Runo;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author abouna
 */
public class ListeScoreQualite extends CustomComponent implements View{
    private BeanItemContainer<ScoreQualite> container;
    private Table table;
    private Label noData;
    private BeanFieldGroup<ScoreQualite> fieldgroup;
    private Layout layout;
    private Window window;
    private ISuiviService suiviService;
    
    public ListeScoreQualite(){
        suiviService = ApplicationContextFactory.getApplicationContext().getBean(ISuiviService.class);
        Layout form = new VerticalLayout();
        form.setSizeFull();
        layout = new VerticalLayout();
        table = new Table();
        layout.setSizeFull();
        noData = new Label("Aucun score trouvé");
        Label lbl = new Label("LA LISTE DES SCORES DE QUALITÉ");
        lbl.addStyleName(Runo.LABEL_H1);
        layout.addComponent(lbl);
        container = new BeanItemContainer<ScoreQualite>(ScoreQualite.class);
        table.setContainerDataSource(container);
        table.addGeneratedColumn("Action", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                HorizontalLayout hLayout = new HorizontalLayout();
                hLayout.addComponent(new Button("Modifier", new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        window = new ListeScoreQualite.NouveauDepartementWindow(new BeanItem<ScoreQualite>((ScoreQualite) itemId),
                                "Nouveau Score de qualité",
                                ajouterDepartement());
                        getUI().addWindow(window);
                    }
                }));
                hLayout.addComponent(new Button("Supprimer", new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        ConfirmWindow cWindow = new ConfirmWindow("Suppression d'un score", "Etes vous sur de vouloir supprimer ce score de qualité?",
                                "Supprimer", "Annuler");
                        Decision decision = new Decision() {
                            @Override
                            public void yes(Button.ClickEvent event) {
                                suiviService.deleteScoreQualite(((ScoreQualite) itemId).getId());
                                container.removeItem(itemId);
                                updateLayout();
                            }
                        };
                        cWindow.setDecision(decision);
                    }
                }));
                return hLayout;
            }
        });
        table.setVisibleColumns(new Object[]{"valeur","trimestre", "annee", "Action"});
        table.setColumnHeaders(new String[]{"Valeur","Trimestre", "Annee", "Action"});
        table.setSortEnabled(false);
        table.setHeight("250px");
        table.setWidth("90%");
        Button addButton = new Button("Ajouter Score de qualité");
        addButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                window = new ListeScoreQualite.NouveauDepartementWindow(new BeanItem<ScoreQualite>(new ScoreQualite()),
                        "Nouveau Score de qualité",
                        ajouterDepartement());
                getUI().addWindow(window);
            }
        });
        form.addComponent(layout);
        form.addComponent(addButton);
        setCompositionRoot(form);
    }
    
    
    private Button.ClickListener ajouterDepartement() {
        return new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    fieldgroup.commit();
                    ScoreQualite soQualite = (ScoreQualite) fieldgroup.getItemDataSource().getBean();
                   // boolean test = centre.getId() == null;
                    suiviService.saveOrUpdateScoreQualite(soQualite);
                    
                        container.addBean(soQualite);
                  
                    updateLayout();
                    window.close();
                } catch (FieldGroup.CommitException ex) {
                    Logger.getLogger(ListeCible.class.getName()).log(Level.SEVERE, null, ex);
                    Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
                }
            }
        };
    }

    private void updateLayout() {
        List<ScoreQualite> vals = container.getItemIds();
        if (vals.isEmpty()) {
            layout.removeComponent(table);
            layout.addComponent(noData);
        } else {
            layout.removeComponent(noData);
            layout.addComponent(table);
        }
        //table.sort(new Object[]{"nom"}, new boolean[]{true});
        table.refreshRowCache();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        container.removeAllItems();
        container.addAll(suiviService.getAllScoreQualite());
        updateLayout();
    }
    
     private class NouveauDepartementWindow extends Window {

        @PropertyId("valeur")
        private TextField valeurText;
        @PropertyId("trimestre")
        private ComboBox trimestreText;
        @PropertyId("annee")
        private TextField anneeText;   

        public NouveauDepartementWindow(BeanItem item, String caption, Button.ClickListener listener) {
            super(caption);
            setResizable(false);
            setModal(true);
            center();
            FormLayout form = new FormLayout();
            form.setMargin(true);           
            form.addComponent(trimestreText = new ComboBox("Trimestre", suiviService.getAllTrimestre()));
            trimestreText.setRequired(true);
            
            form.addComponent(anneeText = new TextField("Annee"));
            anneeText.setRequired(true);
            anneeText.setNullRepresentation("");
            anneeText.addValidator(new IntegerRangeValidator("La date est une valeur entière", 2010, 2030));
            form.addComponent(valeurText = new TextField("Valeur"));
            valeurText.setRequired(true);
            valeurText.setNullRepresentation("");
            valeurText.addValidator(new DoubleRangeValidator("La valeur est une valeur entière", 0.0, Double.MAX_VALUE));
            fieldgroup = new BeanFieldGroup<ScoreQualite>(ScoreQualite.class);
            fieldgroup.bindMemberFields(ListeScoreQualite.NouveauDepartementWindow.this);
            fieldgroup.setItemDataSource(item);
            Button addButton = new Button("Ajouter");
            addButton.addClickListener(listener);
            form.addComponent(addButton);
            setContent(form);
        }
    }
    
}
