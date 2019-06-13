package com.abouna.sante.web.district;

import com.abouna.sante.entities.District;
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
public class ListeDistrict extends CustomComponent implements View {

    private BeanItemContainer<District> container;
    private Table table;
    private Label noData;
    private BeanFieldGroup<District> fieldgroup;
    private Layout layout;
    private Window window;
    private ISuiviService suiviService;

    public ListeDistrict() {
        suiviService = ApplicationContextFactory.getApplicationContext().getBean(ISuiviService.class);
        Layout form = new VerticalLayout();
        form.setSizeFull();
        layout = new VerticalLayout();
        table = new Table();
        layout.setSizeFull();
        table.setWidth("100%");
        noData = new Label("Aucun district trouve");
        Label lbl = new Label("LA LISTE DES DISTRICTS DE SANTE");
        lbl.addStyleName(Runo.LABEL_H1);
        layout.addComponent(lbl);
        container = new BeanItemContainer<District>(District.class);
        table.setContainerDataSource(container);
        table.addGeneratedColumn("Action", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                HorizontalLayout hLayout = new HorizontalLayout();
                hLayout.addComponent(new Button("Modifier", new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        window = new NouveauDepartementWindow(new BeanItem<District>((District) itemId),
                                "Nouveau District",
                                ajouterDepartement());
                        getUI().addWindow(window);
                    }
                }));
                hLayout.addComponent(new Button("Supprimer", new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        ConfirmWindow cWindow = new ConfirmWindow("Suppression d'un département", "Etes vous sur de vouloir supprimer ce departement?",
                                "Supprimer", "Annuler");
                        Decision decision = new Decision() {
                            @Override
                            public void yes(Button.ClickEvent event) {
                                suiviService.deleteDistrict(((District) itemId).getId());
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
        table.setVisibleColumns(new Object[]{"code", "Action"});
        table.setColumnHeaders(new String[]{"Code", "Action"});
        table.setSortEnabled(false);
        table.setHeight("250px");
        table.setWidth("90%");
        Button addButton = new Button("Ajouter District");
        addButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                window = new NouveauDepartementWindow(new BeanItem<District>(new District()),
                        "Nouveau District",
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
                    District district = (District) fieldgroup.getItemDataSource().getBean();
                    boolean test = district.getId() == null;
                    suiviService.saveOrUpdateDistrict(district);
                    if (test) {
                        container.addBean(district);
                    }
                    updateLayout();
                    window.close();
                } catch (FieldGroup.CommitException ex) {
                    Logger.getLogger(ListeDistrict.class.getName()).log(Level.SEVERE, null, ex);
                    Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
                }
            }
        };
    }

    private void updateLayout() {
        List<District> vals = container.getItemIds();
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
        container.addAll(suiviService.getAllDistrict());
        updateLayout();
    }

    private class NouveauDepartementWindow extends Window {

        @PropertyId("code")
        private TextField codeText;

        public NouveauDepartementWindow(BeanItem item, String caption, Button.ClickListener listener) {
            super(caption);
            setResizable(false);
            center();
            setModal(true);
            FormLayout form = new FormLayout();
            form.setMargin(true);
            form.addComponent(codeText = new TextField("Code"));
            codeText.setRequired(true);
            codeText.setNullRepresentation("");
            fieldgroup = new BeanFieldGroup<District>(District.class);
            fieldgroup.bindMemberFields(NouveauDepartementWindow.this);
            fieldgroup.setItemDataSource(item);
            Button addButton = new Button("Ajouter");
            addButton.addClickListener(listener);
            form.addComponent(addButton);
            setContent(form);
        }
    }
}
