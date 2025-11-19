package use_case.search;

import use_case.login.LoginInputData;

public interface SearchInputBoundary {

    /**
     * Executes the login use case.
     * @param inputData the input data
     */
    void execute(SearchInputData inputData);
}
