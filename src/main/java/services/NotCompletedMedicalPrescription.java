package main.java.services;

/**
 * Excepción lanzada cuando la prescripción médica está incompleta
 */
public class NotCompletedMedicalPrescription extends Exception {
    public NotCompletedMedicalPrescription(String message) {
        super(message);
    }
}
