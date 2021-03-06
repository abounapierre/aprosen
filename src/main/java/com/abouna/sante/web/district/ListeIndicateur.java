package com.abouna.sante.web.district;

import com.abouna.sante.entities.CategorieIndicateur;
import com.abouna.sante.entities.Indicateur;
import com.abouna.sante.entities.SousCategorie;
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
public class ListeIndicateur extends CustomComponent implements View{
    
    private BeanItemContainer<Indicateur> container;
    private Table table;
    private Label noData;
    private BeanFieldGroup<Indicateur> fieldgroup;
    private Layout layout;
    private Window window;
    private ISuiviService suiviService;
    
    public ListeIndicateur(){
        suiviService = ApplicationContextFactory.getApplicationContext().getBean(ISuiviService.class);
        Layout form = new VerticalLayout();
        form.setSizeFull();
        layout = new VerticalLayout();
        table = new Table();
        layout.setSizeFull();
        table.setWidth("100%");
        noData = new Label("Aucun indicateur trouve");
        Label lbl = new Label("LA LISTE DES INDICATEURS DE SANTE");
        lbl.addStyleName(Runo.LABEL_H1);
        layout.addComponent(lbl);
        container = new BeanItemContainer<Indicateur>(Indicateur.class);
        table.setContainerDataSource(container);
        table.addGeneratedColumn("Action", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                HorizontalLayout hLayout = new HorizontalLayout();
                hLayout.addComponent(new Button("Modifier", new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        window = new ListeIndicateur.NouveauDepartementWindow(new BeanItem<Indicateur>((Indicateur) itemId),
                                "Nouveau Indicateur",
                                ajouterDepartement());
                        getUI().addWindow(window);
                    }
                }));
                hLayout.addComponent(new Button("Supprimer", new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        ConfirmWindow cWindow = new ConfirmWindow("Suppression d'un indicateur", "Etes vous sur de vouloir supprimer cet indicateur?",
                                "Supprimer", "Annuler");
                        Decision decision = new Decision() {
                            @Override
                            public void yes(Button.ClickEvent event) {
                                suiviService.deleteIndicateur(((Indicateur) itemId).getId());
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
        table.setVisibleColumns(new Object[]{"code", "designation","poids", "categorie", "sousCategorie", "Action"});
        table.setColumnHeaders(new String[]{"Code", "Designation", "Poids", "Categorie", "Sous Categorie", "Action"});
        table.setSortEnabled(false);
        table.setHeight("250px");
        Button addButton = new Button("Ajouter Indicateur");
        addButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                window = new ListeIndicateur.NouveauDepartementWindow(new BeanItem<Indicateur>(new Indicateur()),
                        "Nouvelle Indicateur",
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
                    Indicateur centre = (Indicateur) fieldgroup.getItemDataSource().getBean();
                    boolean test = centre.getId() == null;
                    suiviService.saveOrUpdateIndicateur(centre);
                    if (test) {
                        container.addBean(centre);
                    }
                    updateLayout();
                    window.close();
                } catch (FieldGroup.CommitException ex) {
                    Logger.getLogger(ListeIndicateur.class.getName()).log(Level.SEVERE, null, ex);
                    Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
                }
            }
        };
    }

    private void updateLayout() {
        List<Indicateur> vals = container.getItemIds();
        if (vals.isEmpty()) {
            layout.removeComponent(table);
            layout.addComponent(noData);
        } else {
            layout.removeComponent(noData);
            layout.addComponent(table);
        }
        table.sort(new Object[]{"code"}, new boolean[]{true});
        table.refreshRowCache();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        container.removeAllItems();
        container.addAll(suiviService.getAllIndicateur());
        updateLayout();
    }

    private class NouveauDepartementWindow extends Window {

        @PropertyId("code")
        private TextField nomText;
        @PropertyId("designation")
        private TextField designationText;
        @PropertyId("sousCategorie")
        private ComboBox sCategorieText;
        @PropertyId("poids")
        private TextField poidsText;
        @PropertyId("estFige")
        private CheckBox estFixe;
        @PropertyId("categorie")
        private ComboBox categorieText;
        private BeanItemContainer<SousCategorie> categorieContainer;
        

        public NouveauDepartementWindow(BeanItem item, String caption, Button.ClickListener listener) {
            super(caption);
            setModal(true);
            setResizable(false);
            center();
            categorieContainer = new BeanItemContainer<SousCategorie>(SousCategorie.class);
            FormLayout form = new FormLayout();
            form.setMargin(true);
            form.addComponent(nomText = new TextField("Nom"));
            nomText.setRequired(true);
            nomText.setNullRepresentation("");
            form.addComponent(designationText = new TextField("Désignation"));
            designationText.setRequired(true);
            designationText.setNullRepresentation("");
            form.addComponent(poidsText = new TextField("Poids (en %)"));
            poidsText.setRequired(true);
            poidsText.setNullRepresentation("");
            form.addComponent(categorieText =  new ComboBox("Catégorie", suiviService.getAllCategorieIndicateur()));
            categorieText.addValueChangeListener(new Property.ValueChangeListener() {

                @Override
                public void valueChange(Property.ValueChangeEvent event) {
                    categorieContainer.removeAllItems();
                    CategorieIndicateur c = (CategorieIndicateur)categorieText.getValue();
                    if (c != null){
                        categorieContainer.addAll(suiviService.findSousFromCategorie(c));
                    }
                }
            });
            form.addComponent(sCategorieText = new ComboBox("Sous Catégorie", categorieContainer));
            sCategorieText.setRequired(true);
            form.addComponent(estFixe =  new CheckBox("Est Fixe?"));
            fieldgroup = new BeanFieldGroup<Indicateur>(Indicateur.class);
            fieldgroup.bindMemberFields(ListeIndicateur.NouveauDepartementWindow.this);
            fieldgroup.setItemDataSource(item);
            Button addButton = new Button("Ajouter");
            addButton.addClickListener(listener);
            form.addComponent(addButton);
            setContent(form);
        }
    }
}
