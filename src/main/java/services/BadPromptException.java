package services;

/**
 * Excepción lanzada cuando el prompt de la IA no es claro o tiene inconsistencias
 */
public class BadPromptException extends Exception {
    public BadPromptException(String message) {
        super(message);
    }
}
