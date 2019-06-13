package com.abouna.sante.web.template;

import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public class ConfirmWindow extends Window {

    private Decision decision;
    private Button btnYes = new Button();
    private Button btnNo = new Button();
    private VerticalLayout layout = new VerticalLayout();
    private HorizontalLayout buttonsLayout = new HorizontalLayout();

    public ConfirmWindow(String caption, String question,
            String yes, String no) {
        setCaption(caption);
        btnYes.setCaption(yes);
        btnYes.focus();
        btnNo.setCaption(no);
        setModal(true);
        center();
        buttonsLayout.addComponent(btnYes);
        buttonsLayout.setComponentAlignment(btnYes, Alignment.MIDDLE_CENTER);
        buttonsLayout.addComponent(btnNo);
        buttonsLayout.setComponentAlignment(btnNo, Alignment.MIDDLE_CENTER);
        layout.addComponent(new Label(question));
        layout.addComponent(buttonsLayout);
        setContent(layout);
        layout.setMargin(true);
        buttonsLayout.setMargin(true);
        buttonsLayout.setWidth("100%");
        setWidth("300px");
        setHeight("240px");
        setResizable(false);
        btnYes.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                decision.yes(event);
                close();
            }
        });
        btnNo.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                decision.no(event);
                close();
            }
        });
        addShortcutListener(new ShortcutListener("Close",
                ShortcutAction.KeyCode.ESCAPE, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                close();
            }
        });
        UI.getCurrent().addWindow(ConfirmWindow.this);
    }

    public void setDecision(Decision decision) {
        this.decision = decision;
    }
}
