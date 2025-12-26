package services.doubles;

import main.java.services.BadPromptException;
import main.java.services.Suggestion;
import services.DecisionMakingAI;
import medicalconsultation.Suggestion;
import java.util.ArrayList;
import java.util.List;

/**
 * Mock para DecisionMakingAI - configurable para lanzar excepciones
 */
public class DecisionMakingAIMock implements DecisionMakingAI {

    private boolean throwAIException = false;
    private boolean throwBadPromptException = false;

    public void setThrowAIException(boolean value) {
        this.throwAIException = value;
    }

    public void setThrowBadPromptException(boolean value) {
        this.throwBadPromptException = value;
    }

    public void reset() {
        this.throwAIException = false;
        this.throwBadPromptException = false;
    }

    @Override
    public void initDecisionMakingAI() throws AIException {
        if (throwAIException) {
            throw new RuntimeException("AI Exception: Cannot initialize AI");
        }
    }

    @Override
    public String getSuggestions(String prompt) throws BadPromptException {
        if (throwAIException) {
            throw new RuntimeException("AI Exception: Error getting suggestions");
        }
        if (throwBadPromptException) {
            throw new IllegalArgumentException("BadPromptException: Prompt is not clear or has inconsistencies");
        }
        if (prompt == null || prompt.isEmpty()) {
            throw new IllegalArgumentException("Prompt cannot be null or empty");
        }

        return "I suggest: <I, 123456789012, BEFORELUNCH, 15, 1, 1, DAY, Con agua>";
    }

    @Override
    public List<Suggestion> parseSuggest(String aiAnswer) {
        List<Suggestion> suggestions = new ArrayList<>();
        
        if (aiAnswer == null || aiAnswer.isEmpty()) {
            return suggestions;
        }
        
        suggestions.add(new Suggestion("I", "123456789012", "BEFORELUNCH", "15", "1", "1", "DAY", "Con agua"));
        return suggestions;
    }
}