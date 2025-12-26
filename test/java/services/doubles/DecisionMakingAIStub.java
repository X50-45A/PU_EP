package services.doubles;

import services.DecisionMakingAI;
import medicalconsultation.Suggestion;
import java.util.ArrayList;
import java.util.List;

/**
 * Stub para DecisionMakingAI - simula comportamiento exitoso
 */
public class DecisionMakingAIStub implements DecisionMakingAI {

    @Override
    public void initDecisionMakingAI() {
        // Simula inicialización exitosa
    }

    @Override
    public String getSuggestions(String prompt) {
        if (prompt == null || prompt.isEmpty()) {
            throw new IllegalArgumentException("Prompt cannot be null or empty");
        }
        // Devuelve respuesta simulada
        return "I suggest: <I, 123456789012, BEFORELUNCH, 15, 1, 1, DAY, Con agua> " +
               "<M, 210987654321, , , 3, , , > " +
               "<R, 640557143200>";
    }

    @Override
    public List<Suggestion> parseSuggest(String aiAnswer) {
        List<Suggestion> suggestions = new ArrayList<>();
        
        if (aiAnswer == null || aiAnswer.isEmpty()) {
            return suggestions;
        }
        
        // Parsea respuesta simulada
        suggestions.add(new Suggestion("I", "123456789012", "BEFORELUNCH", "15", "1", "1", "DAY", "Con agua"));
        suggestions.add(new Suggestion("M", "210987654321", "", "", "3", "", "", ""));
        suggestions.add(new Suggestion("R", "640557143200", "", "", "", "", "", ""));
        
        return suggestions;
    }
}

