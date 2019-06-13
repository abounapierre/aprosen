package com.abouna.sante.web.district;

import com.abouna.sante.entities.SousCategorie;
import com.abouna.sante.service.ISuiviService;
import com.abouna.sante.web.ApplicationContextFactory;
import com.abouna.sante.web.template.ConfirmWindow;
import com.abouna.sante.web.template.Decision;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
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
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public class ListeSousCategorie extends CustomComponent implements View{
    
    private BeanItemContainer<SousCategorie> container;
    private Table table;
    private Label noData;
    private BeanFieldGroup<SousCategorie> fieldgroup;
    private Layout layout;
    private Window window;
    private ISuiviService suiviService;
    
    public ListeSousCategorie(){
        suiviService = ApplicationContextFactory.getApplicationContext().getBean(ISuiviService.class);
        Layout form = new VerticalLayout();
        form.setSizeFull();
        layout = new VerticalLayout();
        table = new Table();
        layout.setSizeFull();
        table.setWidth("100%");
        noData = new Label("Aucun sous catégorie d'indicateur trouve");
        Label lbl = new Label("LA LISTE DES SOUS CATEGORIE D'INDICATEURS");
        lbl.addStyleName(Runo.LABEL_H1);
        layout.addComponent(lbl);
        container = new BeanItemContainer<SousCategorie>(SousCategorie.class);
        table.setContainerDataSource(container);
        table.addGeneratedColumn("Action", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                HorizontalLayout hLayout = new HorizontalLayout();
                hLayout.addComponent(new Button("Modifier", new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        window = new ListeSousCategorie.NouveauDepartementWindow(new BeanItem<SousCategorie>((SousCategorie) itemId),
                                "Nouvelle Categorie",
                                ajouterDepartement());
                        getUI().addWindow(window);
                    }
                }));
                hLayout.addComponent(new Button("Supprimer", new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        ConfirmWindow cWindow = new ConfirmWindow("Suppression d'une catégorie", "Etes vous sur de vouloir supprimer cette catégorie?",
                                "Supprimer", "Annuler");
                        Decision decision = new Decision() {
                            @Override
                            public void yes(Button.ClickEvent event) {
                                suiviService.deleteSousCategorie(((SousCategorie) itemId).getId());
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
        table.setVisibleColumns(new Object[]{"code","categorie", "Action"});
        table.setColumnHeaders(new String[]{"Code", "Categorie", "Action"});
        table.setSortEnabled(false);
        table.setHeight("250px");
        Button addButton = new Button("Ajouter Catégorie");
        addButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                window = new ListeSousCategorie.NouveauDepartementWindow(new BeanItem<SousCategorie>(new SousCategorie()),
                        "Nouvelle Catégorie",
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
                    SousCategorie centre = (SousCategorie) fieldgroup.getItemDataSource().getBean();
                    boolean test = centre.getId() == null;
                    suiviService.saveOrUpdateSousCategorie(centre);
                    if (test) {
                        container.addBean(centre);
                    }
                    updateLayout();
                    window.close();
                } catch (FieldGroup.CommitException ex) {
                    Logger.getLogger(ListeSousCategorie.class.getName()).log(Level.SEVERE, null, ex);
                    Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
                }
            }
        };
    }

    private void updateLayout() {
        List<SousCategorie> vals = container.getItemIds();
        if (vals.isEmpty()) {
            layout.removeComponent(table);
            layout.addComponent(noData);
        } else {
            layout.removeComponent(noData);
            layout.addComponent(table);
        }
        table.sort(new Object[]{"categorie.code","code"}, new boolean[]{true, true});
        table.refreshRowCache();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        container.removeAllItems();
        container.addAll(suiviService.getAllSousCategorie());
        updateLayout();
    }

    private class NouveauDepartementWindow extends Window {

        @PropertyId("code")
        private TextField nomText;
        @PropertyId("categorie")
        private ComboBox categorieText;
        

        public NouveauDepartementWindow(BeanItem item, String caption, Button.ClickListener listener) {
            super(caption);
            setResizable(false);
            setModal(true);
            center();
            FormLayout form = new FormLayout();
            form.setMargin(true);
            form.addComponent(nomText = new TextField("Nom"));
            nomText.setRequired(true);
            nomText.setNullRepresentation("");
            form.addComponent(categorieText = new ComboBox("Categorie", suiviService.getAllCategorieIndicateur()));
            categorieText.setRequired(true);
            fieldgroup = new BeanFieldGroup<SousCategorie>(SousCategorie.class);
            fieldgroup.bindMemberFields(ListeSousCategorie.NouveauDepartementWindow.this);
            fieldgroup.setItemDataSource(item);
            Button addButton = new Button("Ajouter");
            addButton.addClickListener(listener);
            form.addComponent(addButton);
            setContent(form);
        }
    }
}
