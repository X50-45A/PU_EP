package main.java.services;

/**
 * Excepción lanzada cuando hay problemas al invocar la IA
 */
public class AIException extends Exception {
    public AIException(String message) {
        super(message);
    }
}
