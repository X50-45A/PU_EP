package services;

import services.BadPromptException;
import services.Suggestion;
import services.AIException;
import java.util.List;

public interface DecisionMakingAI {
    void initDecisionMakingAI() throws AIException;
    String getSuggestions(String prompt) throws BadPromptException;
    List<Suggestion> parseSuggest(String aiAnswer);
}