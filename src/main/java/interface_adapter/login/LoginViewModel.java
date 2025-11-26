package interface_adapter.login;

import interface_adapter.ViewModel;

/**
 * The View Model for the Login View.
 */
public class LoginViewModel extends ViewModel<LoginState> {
    public static final String TITLE_LABEL = "Log In";
    public static final String TITLE = "Event Gate";
    public static final String CAPTION = "Log in with your credentials below";



    public LoginViewModel() {
        super("log in");
        setState(new LoginState());
    }

}
