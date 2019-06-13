package com.abouna.sante.web.district;

import com.abouna.sante.entities.CategorieIndicateur;
import com.abouna.sante.entities.Indicateur;
import com.abouna.sante.entities.SousCategorie;
import com.abouna.sante.entities.Subvention;
import com.abouna.sante.service.ISuiviService;
import com.abouna.sante.web.ApplicationContextFactory;
import com.abouna.sante.web.template.ConfirmWindow;
import com.abouna.sante.web.template.Decision;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
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
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public class ListeSubvention extends CustomComponent implements View{
    private BeanItemContainer<Subvention> container;
    private Table table;
    private Label noData;
    private BeanFieldGroup<Subvention> fieldgroup;
    private Layout layout;
    private Window window;
    private ISuiviService suiviService;
    //List<Integer>  annees;
    
    public ListeSubvention(){
        suiviService = ApplicationContextFactory.getApplicationContext().getBean(ISuiviService.class);
        Layout form = new VerticalLayout();
        form.setSizeFull();
        layout = new VerticalLayout();
        table = new Table();
        layout.setSizeFull();
        table.setWidth("100%");
//        annees = new ArrayList<Integer>();
//        annees.add(2014);
//        annees.add(2013);
        noData = new Label("Aucune subvention trouvee");
        Label lbl = new Label("LA LISTE DES SUBVENTIONS");
        lbl.addStyleName(Runo.LABEL_H1);
        layout.addComponent(lbl);
        container = new BeanItemContainer<Subvention>(Subvention.class);
        table.setContainerDataSource(container);
        table.addGeneratedColumn("Action", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                HorizontalLayout hLayout = new HorizontalLayout();
                hLayout.addComponent(new Button("Modifier", new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        window = new ListeSubvention.NouveauDepartementWindow(new BeanItem<Subvention>((Subvention) itemId),
                                "Nouvelle Subvention",
                                ajouterDepartement());
                        getUI().addWindow(window);
                    }
                }));
                hLayout.addComponent(new Button("Supprimer", new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        ConfirmWindow cWindow = new ConfirmWindow("Suppression d'une subvention", "Etes vous sur de vouloir supprimer cette subvention?",
                                "Supprimer", "Annuler");
                        Decision decision = new Decision() {
                            @Override
                            public void yes(Button.ClickEvent event) {
                                suiviService.deleteSubvention(((Subvention) itemId).getId());
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
        table.setVisibleColumns(new Object[]{"indicateur", "trimestre","annee", "valeur","provision", "Action"});
        table.setColumnHeaders(new String[]{"Indicateur", "Trimestre", "Annee", "Valeur","Provision", "Action"});
        table.setSortEnabled(false);
        table.setHeight("250px");
        table.setWidth("90%");
        Button addButton = new Button("Ajouter Subvention");
        addButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                window = new ListeSubvention.NouveauDepartementWindow(new BeanItem<Subvention>(new Subvention()),
                        "Nouvelle Subvention",
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
                    Subvention centre = (Subvention) fieldgroup.getItemDataSource().getBean();
                    boolean test = centre.getId() == null;
                    suiviService.saveOrUpdateSubvention(centre);
                    if (test) {
                        container.addBean(centre);
                    }
                    updateLayout();
                    window.close();
                } catch (FieldGroup.CommitException ex) {
                    Logger.getLogger(ListeSubvention.class.getName()).log(Level.SEVERE, null, ex);
                    Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
                }
            }
        };
    }

    private void updateLayout() {
        List<Subvention> vals = container.getItemIds();
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
        container.addAll(suiviService.getAllSubvention());
        updateLayout();
    }

    private class NouveauDepartementWindow extends Window {

        @PropertyId("valeur")
        private TextField valeurText;
        @PropertyId("trimestre")
        private ComboBox trimestreText;
        @PropertyId("indicateur")
        private ComboBox indicateurText;
        @PropertyId("annee")
        private TextField anneeText;
        @PropertyId("provision")
        private CheckBox provisionBox;
        private ComboBox categorieBox;
        private ComboBox sCategorieBox;
        private BeanItemContainer<SousCategorie> sousCategorieContainer;
        private BeanItemContainer<Indicateur> indicateurContainer;
        

        public NouveauDepartementWindow(BeanItem item, String caption, Button.ClickListener listener) {
            super(caption);
            setResizable(false);
            setModal(true);
            center();
            FormLayout form = new FormLayout();
            form.setMargin(true);
            sousCategorieContainer = new BeanItemContainer<SousCategorie>(SousCategorie.class);
            form.addComponent(categorieBox =  new ComboBox("Catégorie", suiviService.getAllCategorieIndicateur()));
            categorieBox.addValueChangeListener(new Property.ValueChangeListener() {

                @Override
                public void valueChange(Property.ValueChangeEvent event) {
                    sousCategorieContainer.removeAllItems();
                    CategorieIndicateur d = (CategorieIndicateur)categorieBox.getValue();
                    if(d != null){
                        sousCategorieContainer.addAll(suiviService.findSousFromCategorie(d));
                    }
                }
            });
            form.addComponent(sCategorieBox = new ComboBox("Sous Catégorie", sousCategorieContainer));
            sCategorieBox.addValueChangeListener(new Property.ValueChangeListener() {

                @Override
                public void valueChange(Property.ValueChangeEvent event) {
                    SousCategorie sc = (SousCategorie)sCategorieBox.getValue();
                    if(sc != null){
                        indicateurContainer.removeAllItems();
                        indicateurContainer.addAll(suiviService.findIndicateurFromSousCategorie(sc));
                    }
                }
            });
            indicateurContainer = new BeanItemContainer<Indicateur>(Indicateur.class);
            indicateurContainer.addAll(suiviService.getAllIndicateur());
            form.addComponent(indicateurText = new ComboBox("Indicateur", indicateurContainer));
            indicateurText.setRequired(true);
            form.addComponent(trimestreText = new ComboBox("Trimestre", suiviService.getAllTrimestre()));
            trimestreText.setRequired(true);
            
            form.addComponent(anneeText = new TextField("Annee"));
            anneeText.addValidator(new IntegerRangeValidator("La date est une valeur entière", 2010, 2030));
            anneeText.setRequired(true);
            anneeText.setNullRepresentation("");
            form.addComponent(valeurText = new TextField("Valeur"));
            valeurText.setRequired(true);
            valeurText.addValidator(new IntegerRangeValidator("La valeur est une valeur entière", 0, Integer.MAX_VALUE));
            valeurText.setNullRepresentation("");
            form.addComponent(provisionBox = new CheckBox("Est Approvisionné?"));
            fieldgroup = new BeanFieldGroup<Subvention>(Subvention.class);
            fieldgroup.bindMemberFields(ListeSubvention.NouveauDepartementWindow.this);
            fieldgroup.setItemDataSource(item);
            Button addButton = new Button("Ajouter");
            addButton.addClickListener(listener);
            form.addComponent(addButton);
            setContent(form);
        }
    }
}