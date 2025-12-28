package data;

class InvalidHealthCardIDException extends Exception {
    public InvalidHealthCardIDException(String message) {
        super(message);
    }
}

public class InvalidDigitalSignatureException extends Exception {
    public InvalidDigitalSignatureException(String message) {
        super(message);
    }
}