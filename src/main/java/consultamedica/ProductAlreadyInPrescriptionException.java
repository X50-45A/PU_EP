package consultamedica;

/**
 * Excepción lanzada cuando se intenta añadir un producto que ya existe en la prescripción
 */
public class ProductAlreadyInPrescriptionException extends Exception {
    public ProductAlreadyInPrescriptionException(String message) {
        super(message);
    }
}
