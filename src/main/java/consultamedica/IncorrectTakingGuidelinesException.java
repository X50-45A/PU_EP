package consultamedica;

/**
 * Excepción lanzada cuando las pautas de medicación son incorrectas o incompletas
 */
public class IncorrectTakingGuidelinesException extends Exception {
    public IncorrectTakingGuidelinesException(String message) {
        super(message);
    }
}
