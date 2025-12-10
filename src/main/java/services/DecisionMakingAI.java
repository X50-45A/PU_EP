package main.java.services;

public interface DecisionMakingAI {
    private void initDecisionMakingAI() throws AIException;
    private String getSuggestions(String prompt) throws BadPromptException;
    private List<Suggestion> parseSuggest(String aiAnswer);
}