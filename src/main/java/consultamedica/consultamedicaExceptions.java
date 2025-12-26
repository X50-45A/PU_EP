package main.java.consultamedica;

/**
 * Excepción lanzada cuando los parámetros proporcionados son incorrectos
 */
class IncorrectParametersException extends Exception {
    public IncorrectParametersException(String message) {
        super(message);
    }
}

/**
 * Excepción lanzada cuando se intenta añadir un producto que ya existe en la prescripción
 */
class ProductAlreadyInPrescriptionException extends Exception {
    public ProductAlreadyInPrescriptionException(String message) {
        super(message);
    }
}

/**
 * Excepción lanzada cuando las pautas de medicación son incorrectas o incompletas
 */
class IncorrectTakingGuidelinesException extends Exception {
    public IncorrectTakingGuidelinesException(String message) {
        super(message);
    }
}

/**
 * Excepción lanzada cuando se intenta modificar/eliminar un producto que no existe
 */
class ProductNotInPrescriptionException extends Exception {
    public ProductNotInPrescriptionException(String message) {
        super(message);
    }
}
