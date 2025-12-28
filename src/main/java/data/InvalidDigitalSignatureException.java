package data;

public class InvalidDigitalSignatureException extends Exception {
    public InvalidDigitalSignatureException(String message) {
        super(message);
    }
}