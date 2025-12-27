package services;


/**
 * Excepción lanzada cuando el HealthCardID no está registrado en el SNS
 */
public class HealthCardIDException extends Exception {
    public HealthCardIDException(String message) {
        super(message);
    }
}

