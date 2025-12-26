package main.java.services;

/**
 * Excepción lanzada cuando no existe prescripción activa para esa enfermedad
 */
public class AnyCurrentPrescriptionException extends Exception {
    public AnyCurrentPrescriptionException(String message) {
        super(message);
    }
}
