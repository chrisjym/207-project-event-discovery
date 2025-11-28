package interface_adapter.login;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.signup.SignupViewModel;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;
import view.SignupView;

/**
 * The Presenter for the Login Use Case.
 */
public class LoginPresenter implements LoginOutputBoundary {

    private final LoginViewModel loginViewModel;
    private final LoggedInViewModel loggedInViewModel;
    private final ViewManagerModel viewManagerModel;
    private final SignupViewModel signUpViewModel;

    public LoginPresenter(ViewManagerModel viewManagerModel,
                          LoggedInViewModel loggedInViewModel,
                          LoginViewModel loginViewModel, SignupViewModel signupViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = loggedInViewModel;
        this.loginViewModel = loginViewModel;
        this.signUpViewModel = signupViewModel;
    }

    // OLD CODE
//    @Override
//    public void prepareSuccessView(LoginOutputData response) {
//        // On success, update the loggedInViewModel's state
//        final LoggedInState loggedInState = loggedInViewModel.getState();
//        loggedInState.setUsername(response.getUsername());
//        this.loggedInViewModel.firePropertyChange();
//
//        // and clear everything from the LoginViewModel's state
//        loginViewModel.setState(new LoginState());
//
//        // switch to the logged in view
//        viewManagerModel.setState("display local events");
//        viewManagerModel.firePropertyChange();
//    }

    @Override
    public void prepareSuccessView(LoginOutputData response) {
        final LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.setUsername(response.getUsername());
        this.loggedInViewModel.firePropertyChange();

        loginViewModel.setState(new LoginState());

        // The view will load the user's location when it becomes visible
        viewManagerModel.setState("display local events");
        viewManagerModel.firePropertyChange();
    }


    @Override
    public void prepareFailView(String error) {
        final LoginState loginState = loginViewModel.getState();
        loginState.setLoginError(error);
        loginViewModel.firePropertyChange();
    }

    @Override
    public void switchToSignUpView() {
        viewManagerModel.setState(signUpViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}
