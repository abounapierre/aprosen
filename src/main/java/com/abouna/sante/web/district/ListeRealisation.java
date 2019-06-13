package com.abouna.sante.web.district;

import com.abouna.sante.entities.CategorieIndicateur;
import com.abouna.sante.entities.Centre;
import com.abouna.sante.entities.Indicateur;
import com.abouna.sante.entities.Mois;
import com.abouna.sante.entities.Realisation;
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
public class ListeRealisation extends CustomComponent implements View{
    private BeanItemContainer<Realisation> container;
    private Table table;
    private Label noData;
    private BeanFieldGroup<Realisation> fieldgroup;
    private Layout layout;
    private Window window;
    private ISuiviService suiviService;
    //List<Integer>  annees;
    private ComboBox centreBox, indicateurBox, moisBox, anneeBox;
    private BeanItemContainer<Centre> centreContainer;
    private BeanItemContainer<Indicateur> indicateurContainer;
    private BeanItemContainer<Mois> moisContainer;
    private BeanItemContainer<Integer> anneeContainer;
    
    private Button filtreBtn;
    
    public ListeRealisation(){
        suiviService = ApplicationContextFactory.getApplicationContext().getBean(ISuiviService.class);
        Layout form = new VerticalLayout();
        form.setSizeFull();
        layout = new VerticalLayout();
        ((VerticalLayout)layout).setSpacing(true);
        table = new Table();
        layout.setSizeFull();
        table.setWidth("100%");
        HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.setSpacing(true);
        noData = new Label("Aucune realisation trouvee");
        Label lbl = new Label("LA LISTE DES REALISATIONS");
        lbl.addStyleName(Runo.LABEL_H1);
        layout.addComponent(lbl);
        centreContainer = new BeanItemContainer<Centre>(Centre.class);
        indicateurContainer = new BeanItemContainer<Indicateur>(Indicateur.class);
        moisContainer = new BeanItemContainer<Mois>(Mois.class);
        anneeContainer = new BeanItemContainer<Integer>(Integer.class);
        hLayout.addComponent(centreBox = new ComboBox("Centre", centreContainer));
        hLayout.addComponent(indicateurBox = new ComboBox("Indicateur", indicateurContainer));
        hLayout.addComponent(anneeBox = new ComboBox("Année", anneeContainer));
        hLayout.addComponent(moisBox = new ComboBox("Mois", moisContainer));
        filtreBtn = new Button("Filtrer", new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                Centre c = (Centre) centreBox.getValue();
                Indicateur indi = (Indicateur) indicateurBox.getValue();
                Mois m = (Mois) moisBox.getValue();
                Integer an = (Integer) anneeBox.getValue();
                List<Realisation> real = suiviService.getRealisation(c, indi, an, m);
                container.removeAllItems();
                container.addAll(real);
            }
        });
        hLayout.addComponent(filtreBtn);
        layout.addComponent(hLayout);
        container = new BeanItemContainer<Realisation>(Realisation.class);
        table.setContainerDataSource(container);
        table.addGeneratedColumn("Action", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                HorizontalLayout hLayout = new HorizontalLayout();
                hLayout.addComponent(new Button("Modifier", new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        window = new ListeRealisation.NouveauDepartementWindow(new BeanItem<Realisation>((Realisation) itemId),
                                "Nouveau Realisation",
                                ajouterDepartement());
                        getUI().addWindow(window);
                    }
                }));
                hLayout.addComponent(new Button("Supprimer", new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        ConfirmWindow cWindow = new ConfirmWindow("Suppression d'une realisation", "Etes vous sur de vouloir supprimer cette realisation?",
                                "Supprimer", "Annuler");
                        Decision decision = new Decision() {
                            @Override
                            public void yes(Button.ClickEvent event) {
                                suiviService.deleteRealisation(((Realisation) itemId).getId());
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
        table.setVisibleColumns(new Object[]{"centre", "indicateur", "mois","annee", "valeur", "Action"});
        table.setColumnHeaders(new String[]{"Centre", "Indicateur", "Mois", "Annee", "Valeur", "Action"});
        table.setSortEnabled(false);
        table.setHeight("250px");
        table.setWidth("90%");
        Button addButton = new Button("Ajouter Realisation");
        addButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                window = new ListeRealisation.NouveauDepartementWindow(new BeanItem<Realisation>(new Realisation()),
                        "Nouvelle Realisation",
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
                    Realisation centre = (Realisation) fieldgroup.getItemDataSource().getBean();
                    boolean test = centre.getId() == null;
                    suiviService.saveOrUpdateRealisation(centre);
                    if (test) {
                        container.addBean(centre);
                    }
                    updateLayout();
                    window.close();
                } catch (FieldGroup.CommitException ex) {
                    Logger.getLogger(ListeRealisation.class.getName()).log(Level.SEVERE, null, ex);
                    Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
                }
            }
        };
    }

    private void updateLayout() {
        List<Realisation> vals = container.getItemIds();
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
        container.addAll(suiviService.getAllRealisation());
        indicateurContainer.removeAllItems();
        indicateurContainer.addAll(suiviService.getAllIndicateur());
        centreContainer.removeAllItems();
        centreContainer.addAll(suiviService.getAllCentre());
        moisContainer.removeAllItems();
        moisContainer.addAll(suiviService.getAllMois());
        anneeContainer.removeAllItems();
        anneeContainer.addAll(suiviService.getDistictYears());
        updateLayout();
    }

    private class NouveauDepartementWindow extends Window {

        @PropertyId("valeur")
        private TextField valeurText;
        @PropertyId("centre")
        private ComboBox centreText;
        @PropertyId("indicateur")
        private ComboBox indicateurText;
        @PropertyId("mois")
        private ComboBox moisText;
        @PropertyId("annee")
        private TextField anneeText;
        @PropertyId("donneesCorrecte")
        private CheckBox correctText;
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
            form.addComponent(centreText = new ComboBox("Centre", suiviService.getAllCentre()));
            centreText.setRequired(true);
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
            form.addComponent(moisText = new ComboBox("Mois", suiviService.getAllMois()));
            moisText.setRequired(true);
            form.addComponent(anneeText = new TextField("Annee"));
            anneeText.addValidator(new IntegerRangeValidator("La date est une valeur entière", 2010, 2030));
            anneeText.setRequired(true);
            anneeText.setNullRepresentation("");
            form.addComponent(valeurText = new TextField("Valeur"));
            valeurText.setRequired(true);
            valeurText.addValidator(new IntegerRangeValidator("La date est une valeur entière", 0, Integer.MAX_VALUE));
            valeurText.setNullRepresentation("");
            form.addComponent(correctText = new CheckBox("Données Incorrectes ?"));
            fieldgroup = new BeanFieldGroup<Realisation>(Realisation.class);
            fieldgroup.bindMemberFields(ListeRealisation.NouveauDepartementWindow.this);
            fieldgroup.setItemDataSource(item);
            Button addButton = new Button("Ajouter");
            addButton.addClickListener(listener);
            form.addComponent(addButton);
            setContent(form);
        }
    }
}