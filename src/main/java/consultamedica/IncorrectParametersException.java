package consultamedica;

/**
 * Excepción lanzada cuando los parámetros proporcionados son incorrectos
 */
public class IncorrectParametersException extends Exception {
    public IncorrectParametersException(String message) {
        super(message);
    }
}

/**
 * Excepción lanzada cuando la fecha de finalización del tratamiento es incorrecta
 * (fecha pasada, actual o demasiado cercana)
 */
class IncorrectEndingDateException extends Exception {
    public IncorrectEndingDateException(String message) {
        super(message);
    }
}

/**
 * Excepción lanzada cuando hay un problema al estampar la firma electrónica
 */
class eSignatureException extends Exception {
    public eSignatureException(String message) {
        super(message);
    }
}

/**
 * Excepción lanzada cuando se violan las precondiciones del flujo del caso de uso
 * (eventos llamados en orden incorrecto)
 */
class ProceduralException extends Exception {
    public ProceduralException(String message) {
        super(message);
    }
}

/**
 * Excepción lanzada cuando hay un error en el servicio de impresión
 */
class printingException extends Exception {
    public printingException(String message) {
        super(message);
    }
}