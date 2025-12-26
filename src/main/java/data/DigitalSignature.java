package data;

final public class DigitalSignature {
    private final byte[] signature;

    public DigitalSignature(byte[] signature) throws InvalidDigitalSignatureException {
        if (signature == null) {
            throw new InvalidDigitalSignatureException("La firma digital no puede ser null");
        }
        if (signature.length == 0) {
            throw new InvalidDigitalSignatureException("La firma digital no puede estar vacía");
        }
        this.signature = signature.clone();
    }

    public byte[] getSignature() {
        return signature.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DigitalSignature that = (DigitalSignature) o;
        return java.util.Arrays.equals(signature, that.signature);
    }

    @Override
    public int hashCode() {
        return java.util.Arrays.hashCode(signature);
    }

    @Override
    public String toString() {
        return "DigitalSignature{" + "signature length=" + signature.length + '}';
    }
}