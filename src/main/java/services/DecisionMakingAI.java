package services;

import main.java.services.BadPromptException;
import main.java.services.Suggestion;
import main.java.services.AIException;
import java.util.List;

public interface DecisionMakingAI {
    void initDecisionMakingAI() throws AIException;
    String getSuggestions(String prompt) throws BadPromptException;
    List<Suggestion> parseSuggest(String aiAnswer);
}