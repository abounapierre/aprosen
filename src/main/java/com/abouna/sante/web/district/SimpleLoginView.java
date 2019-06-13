package com.abouna.sante.web.district;

import com.abouna.sante.entities.Utilisateur;
import com.abouna.sante.service.ISuiviService;
import com.abouna.sante.util.Util;
import com.abouna.sante.web.ApplicationContextFactory;
import com.abouna.sante.web.MainFrame;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

@SuppressWarnings("serial")
public class SimpleLoginView extends CustomComponent implements View,
		Button.ClickListener {

	public static final String NAME = "pageInitiale";
	private final TextField user;
	private final PasswordField password;
	private final Button loginButton;
        private boolean state;
        private ISuiviService suiviService;

	public SimpleLoginView() {
            suiviService = ApplicationContextFactory.getApplicationContext().getBean(ISuiviService.class);
		setSizeFull();

		// Create the user input field
		user = new TextField("User:");
		user.setWidth("300px");
		user.setRequired(true);
		user.setInputPrompt("Your username");
		

		// Create the password input field
		password = new PasswordField("Password:");
		password.setWidth("300px");
		//password.addValidator(new PasswordValidator());
		password.setRequired(true);
		password.setValue("");
		password.setNullRepresentation("");
                password.setInputPrompt("Your password");

		// Create login button
		loginButton = new Button("Login", this);

		// Add both to a panel
		VerticalLayout fields = new VerticalLayout(user, password, loginButton);
		fields.setCaption("Please login to access the application.");
		fields.setSpacing(true);
		fields.setMargin(new MarginInfo(true, true, true, false));
		fields.setSizeUndefined();

		// The view root layout
		VerticalLayout viewLayout = new VerticalLayout(fields);
		viewLayout.setSizeFull();
		viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
		viewLayout.setStyleName(Reindeer.LAYOUT_BLUE);
		setCompositionRoot(viewLayout);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// focus the username field when user arrives to the login view
		// user.focus();
            ((MainFrame) UI.getCurrent()).enableMenuLayout(false);
            user.setValue("");
            password.setValue("");
	}

	
	@Override
	public void buttonClick(ClickEvent event) {

		//
		// Validate the fields using the navigator. By using validors for the
		// fields we reduce the amount of queries we have to use to the database
		// for wrongly entered passwords
		//
		if (!user.isValid() || !password.isValid()) {
			user.focus();
			return;
		}

		String username = user.getValue();
		String pwd = this.password.getValue();
                pwd = Util.hacherMotDepasse("SHA1",pwd);
                Utilisateur u = suiviService.getUtilisateurByUsername(username);
		

		if (u != null) {
                    if(u.getPassword().equals(pwd)){
			// Store the current user in the service session
			getSession().setAttribute("user", username);
                        getSession().setAttribute("state",true);
                        VaadinSession.getCurrent().setAttribute("role", u.getRole());
			// Navigate to main view
			getUI().getNavigator().navigateTo(NAME);
                    }else{
                        getUI().getNavigator().navigateTo("");
                    }
		} else {

			// Wrong password clear the password field and refocuses it
			this.password.setValue(null);
			this.password.focus();
		}
	}
        
        
}