package use_case.search;

public interface SearchOutputBoundary {

    /**
     * Prepare the success view for the Search Use Case
     * @param outputData
     */
    void prepareSuccessView(SearchOutputData outputData);

    /**
     * Prepares the failure view for the Search Use Case.
     * @param failMessage the explanation of the failure
     */
    void prepareFailureView(String failMessage);

    /**
     * Switch to the search by event view
     * @param outputData
     */
    void switchToEventSearchView(SearchOutputData outputData);
}
