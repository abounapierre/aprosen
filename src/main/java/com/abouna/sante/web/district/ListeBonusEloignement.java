package com.abouna.sante.web.district;

import com.abouna.sante.entities.BonusEloignement;
import com.abouna.sante.service.ISuiviService;
import com.abouna.sante.web.ApplicationContextFactory;
import com.abouna.sante.web.template.ConfirmWindow;
import com.abouna.sante.web.template.Decision;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
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
public class ListeBonusEloignement extends CustomComponent implements View{
   private BeanItemContainer<BonusEloignement> container;
    private Table table;
    private Label noData;
    private BeanFieldGroup<BonusEloignement> fieldgroup;
    private Layout layout;
    private Window window;
    private ISuiviService suiviService;
    
    public ListeBonusEloignement(){
        suiviService = ApplicationContextFactory.getApplicationContext().getBean(ISuiviService.class);
        Layout form = new VerticalLayout();
        form.setSizeFull();
        layout = new VerticalLayout();
        table = new Table();
        layout.setSizeFull();
        noData = new Label("Aucune bonus trouvee");
        Label lbl = new Label("LA LISTE DES BONUS D'ÉLOIGNEMENT");
        lbl.addStyleName(Runo.LABEL_H1);
        layout.addComponent(lbl);
        container = new BeanItemContainer<BonusEloignement>(BonusEloignement.class);
        table.setContainerDataSource(container);
        table.addGeneratedColumn("Action", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                HorizontalLayout hLayout = new HorizontalLayout();
                hLayout.addComponent(new Button("Modifier", new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        window = new ListeBonusEloignement.NouveauDepartementWindow(new BeanItem<BonusEloignement>((BonusEloignement) itemId),
                                "Nouveau Bonus d'éloignement",
                                ajouterDepartement());
                        getUI().addWindow(window);
                    }
                }));
                hLayout.addComponent(new Button("Supprimer", new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        ConfirmWindow cWindow = new ConfirmWindow("Suppression d'un bonus d'éloignement", "Etes vous sur de vouloir supprimer ce bonus d'éloignement?",
                                "Supprimer", "Annuler");
                        Decision decision = new Decision() {
                            @Override
                            public void yes(Button.ClickEvent event) {
                                suiviService.deleteBonusEloignement(((BonusEloignement) itemId).getId());
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
        table.setVisibleColumns(new Object[]{"centre", "valeur","trimestre", "annee", "Action"});
        table.setColumnHeaders(new String[]{"Centre", "Valeur","Trimestre", "Annee", "Action"});
        table.setSortEnabled(false);
        table.setHeight("250px");
        table.setWidth("90%");
        Button addButton = new Button("Ajouter Bonus d'isolement");
        addButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                window = new ListeBonusEloignement.NouveauDepartementWindow(new BeanItem<BonusEloignement>(new BonusEloignement()),
                        "Nouveau Bonus d'éloignement",
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
                    BonusEloignement bonusEloignement = (BonusEloignement) fieldgroup.getItemDataSource().getBean();
                   // boolean test = centre.getId() == null;
                    suiviService.saveOrUpdateBonusEloignement(bonusEloignement);
                    
                        container.addBean(bonusEloignement);
                  
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
        List<BonusEloignement> vals = container.getItemIds();
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
        container.addAll(suiviService.getAllBonusEloignement());
        updateLayout();
    }
    
     private class NouveauDepartementWindow extends Window {

        @PropertyId("valeur")
        private TextField valeurText;
        @PropertyId("trimestre")
        private ComboBox trimestreText;
        @PropertyId("annee")
        private TextField anneeText;
        @PropertyId("centre")
        private ComboBox centreBox;
        
        

        public NouveauDepartementWindow(BeanItem item, String caption, Button.ClickListener listener) {
            super(caption);
            setResizable(false);
            setModal(true);
            center();  
            FormLayout form = new FormLayout();
            form.setMargin(true);
            form.addComponent(centreBox =  new ComboBox("Centre", suiviService.getAllCentre()));
            centreBox.setRequired(true);
            form.addComponent(trimestreText = new ComboBox("Trimestre", suiviService.getAllTrimestre()));
            trimestreText.setRequired(true);
            
            form.addComponent(anneeText = new TextField("Annee"));
            anneeText.setRequired(true);
            anneeText.setNullRepresentation("");
            anneeText.addValidator(new IntegerRangeValidator("La date est une valeur entière", 2010, 2030));
            form.addComponent(valeurText = new TextField("Valeur"));
            valeurText.setRequired(true);
            valeurText.setNullRepresentation("");
            //valeurText.addValidator(new DoubleRangeValidator("La valeur est une double", 0.0, Double.MIN_VALUE));
            fieldgroup = new BeanFieldGroup<BonusEloignement>(BonusEloignement.class);
            fieldgroup.bindMemberFields(ListeBonusEloignement.NouveauDepartementWindow.this);
            fieldgroup.setItemDataSource(item);
            Button addButton = new Button("Ajouter");
            addButton.addClickListener(listener);
            form.addComponent(addButton);
            setContent(form);
        }
    }
     
}
