/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abouna.sante.web.district;

import com.abouna.sante.entities.Utilisateur;
import com.abouna.sante.service.ISuiviService;
import com.abouna.sante.util.Util;
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
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Runo;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author abouna
 */
public class ListeUtilisateurs extends CustomComponent implements View{
    private BeanItemContainer<Utilisateur> container;
    private Table table;
    private Label noData;
    private BeanFieldGroup<Utilisateur> fieldgroup;
    private Layout layout;
    private Window window;
    private ISuiviService suiviService;
    private List<String> roleList = new ArrayList<String>();
    private Map<String,String> roleMap = new HashMap<String, String>();

    public ListeUtilisateurs() {
        suiviService = ApplicationContextFactory.getApplicationContext().getBean(ISuiviService.class);
        Layout form = new VerticalLayout();
        form.setSizeFull();
        layout = new VerticalLayout();
        table = new Table();
        layout.setSizeFull();
        noData = new Label("Aucun utilisateur trouvé");
        Label lbl = new Label("LA LISTE DES UTILISATEURS");
        lbl.addStyleName(Runo.LABEL_H1);
        layout.addComponent(lbl);
        container = new BeanItemContainer<Utilisateur>(Utilisateur.class);
        table.setContainerDataSource(container);
        table.addGeneratedColumn("Action", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                HorizontalLayout hLayout = new HorizontalLayout();
                hLayout.addComponent(new Button("Modifier", new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        window = new ListeUtilisateurs.UpdateUtilisateurWindow(new BeanItem<Utilisateur>((Utilisateur) itemId),
                                "Modifier utilisateur",
                                updateUtilisateur());
                        getUI().addWindow(window);
                    }
                }));
                hLayout.addComponent(new Button("Supprimer", new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        ConfirmWindow cWindow = new ConfirmWindow("Suppression d'un utilisateur", "Etes vous sur de vouloir supprimer cet utilisateur?",
                                "Supprimer", "Annuler");
                        Decision decision = new Decision() {
                            @Override
                            public void yes(Button.ClickEvent event) {
                                suiviService.deleteUtilisateur(((Utilisateur) itemId).getUsername());
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
        table.setVisibleColumns(new Object[]{"username", "password","email","numero","role","Action"});
        table.setColumnHeaders(new String[]{"Nom utilisateur", "Mot de passe","Email","Numero telephone","Role","Action"});
        table.setSortEnabled(false);
        table.setHeight("250px");
        table.setWidth("90%");
        Button addButton = new Button("Ajouter Utilisateur");
        addButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                window = new ListeUtilisateurs.NouveauUtilisateurWindow(new BeanItem<Utilisateur>(new Utilisateur()),
                        "Nouvel Utilisateur",
                        ajouterUtilisateur());
                getUI().addWindow(window);
            }
        });
        form.addComponent(layout);
        form.addComponent(addButton);
        setCompositionRoot(form);
    }

    public Map<String, String> getRoleMap() {
        roleMap.put("admin","Administreur");
        roleMap.put("user", "Utilisateur simple");
        return roleMap;
    }

    public void setRoleMap(Map<String, String> roleMap) {
        this.roleMap = roleMap;
    }

    public List<String> getRoleList() {
        roleList.add("admin");
        roleList.add("user");
        return roleList;
    }

    public void setRoleList(List<String> roleList) {
        this.roleList = roleList;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        container.removeAllItems();
        container.addAll(suiviService.getAllUtilisateur());
        updateLayout();
    }

    private Button.ClickListener ajouterUtilisateur() {
        return new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    fieldgroup.commit();
                    Utilisateur utilisateur = (Utilisateur) fieldgroup.getItemDataSource().getBean();
                    boolean test = utilisateur.getUsername() == null;
                    utilisateur.setPassword(Util.hacherMotDepasse("SHA1",utilisateur.getPassword()));
                    suiviService.saveUtilisateur(utilisateur);
                    container.addBean(utilisateur);
                    updateLayout();
                    window.close();
                } catch (FieldGroup.CommitException ex) {
                    Logger.getLogger(ListeCentre.class.getName()).log(Level.SEVERE, null, ex);
                    Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
                }
            }
        };
    }
    
    private Button.ClickListener updateUtilisateur() {
        return new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    fieldgroup.commit();
                    Utilisateur utilisateur = (Utilisateur) fieldgroup.getItemDataSource().getBean();
                   // boolean test = utilisateur.getUsername() == null;
                    utilisateur.setPassword(Util.hacherMotDepasse("SHA1",utilisateur.getPassword()));
                    suiviService.updateUtilisateur(utilisateur);
                    //container.addBean(utilisateur);
                    updateLayout();
                    window.close();
                } catch (FieldGroup.CommitException ex) {
                    Logger.getLogger(ListeCentre.class.getName()).log(Level.SEVERE, null, ex);
                    Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
                }
            }
        };
    }
    
    private void updateLayout() {
        List<Utilisateur> vals = container.getItemIds();
        if (vals.isEmpty()) {
            layout.removeComponent(table);
            layout.addComponent(noData);
        } else {
            layout.removeComponent(noData);
            layout.addComponent(table);
        }
        table.sort(new Object[]{"username"}, new boolean[]{true});
        table.refreshRowCache();
    }
    
    private class NouveauUtilisateurWindow extends Window {

        @PropertyId("username")
        private TextField nomText;
        @PropertyId("password")
        private PasswordField passwordText;
        @PropertyId("role")
        private ComboBox roleText;
        @PropertyId("email")
        private TextField emailText;
        @PropertyId("numero")
        private TextField numeroText;
        

        public NouveauUtilisateurWindow(BeanItem item, String caption, Button.ClickListener listener) {
            super(caption);
            setResizable(false);
            center();
            setModal(true);
            FormLayout form = new FormLayout();
            form.setMargin(true);
            form.addComponent(nomText = new TextField("Nom utilisateur"));
            nomText.setRequired(true);
            nomText.setNullRepresentation("");
            form.addComponent(passwordText = new PasswordField("Mot de passe"));
            passwordText.setRequired(true);
            passwordText.setNullRepresentation("");
            form.addComponent(emailText = new TextField("Adresse mail"));
            emailText.setNullRepresentation("");
            form.addComponent(numeroText = new TextField("Numero Téléphone"));
            numeroText.setNullRepresentation("");
            form.addComponent(roleText = new ComboBox("Role", getRoleList()));
            roleText.setRequired(true);
            fieldgroup = new BeanFieldGroup<Utilisateur>(Utilisateur.class);
            fieldgroup.bindMemberFields(ListeUtilisateurs.NouveauUtilisateurWindow.this);
            fieldgroup.setItemDataSource(item);
            Button addButton = new Button("Ajouter");
            addButton.addClickListener(listener);
            form.addComponent(addButton);
            setContent(form);
        }
    }
 
    private class UpdateUtilisateurWindow extends Window {

        @PropertyId("username")
        private TextField nomText;
        @PropertyId("password")
        private PasswordField passwordText;
        @PropertyId("role")
        private ComboBox roleText;
        @PropertyId("email")
        private TextField emailText;
        @PropertyId("numero")
        private TextField numeroText;
        

        public UpdateUtilisateurWindow(BeanItem item, String caption, Button.ClickListener listener) {
            super(caption);
            setResizable(false);
            center();
            setModal(true);
            FormLayout form = new FormLayout();
            form.setMargin(true);
            form.addComponent(nomText = new TextField("Nom utilisateur"));
            nomText.setRequired(true);
            nomText.setNullRepresentation("");
            form.addComponent(passwordText = new PasswordField("Mot de passe"));
            passwordText.setRequired(true);
            passwordText.setNullRepresentation("");
            form.addComponent(emailText = new TextField("Adresse mail"));
            emailText.setNullRepresentation("");
            form.addComponent(numeroText = new TextField("Numero Téléphone"));
            numeroText.setNullRepresentation("");
            form.addComponent(roleText = new ComboBox("Role", getRoleList()));
            roleText.setRequired(true);
            fieldgroup = new BeanFieldGroup<Utilisateur>(Utilisateur.class);
            fieldgroup.bindMemberFields(ListeUtilisateurs.UpdateUtilisateurWindow.this);
            fieldgroup.setItemDataSource(item);
            Button addButton = new Button("Ajouter");
            addButton.addClickListener(listener);
            form.addComponent(addButton);
            setContent(form);
        }
    }

}
