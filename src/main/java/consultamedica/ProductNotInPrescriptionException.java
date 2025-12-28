package consultamedica;

/**
 * Excepción lanzada cuando se intenta modificar/eliminar un producto que no existe
 */
public class ProductNotInPrescriptionException extends Exception {
    public ProductNotInPrescriptionException(String message) {
        super(message);
    }
}
