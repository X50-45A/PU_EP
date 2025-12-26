package data;

class InvalidHealthCardIDException extends Exception {
    public InvalidHealthCardIDException(String message) {
        super(message);
    }
}

class InvalidProductIDException extends Exception {
    public InvalidProductIDException(String message) {
        super(message);
    }
}

class InvalidePrescripCodeException extends Exception {
    public InvalidePrescripCodeException(String message) {
        super(message);
    }
}

public class InvalidDigitalSignatureException extends Exception {
    public InvalidDigitalSignatureException(String message) {
        super(message);
    }
}