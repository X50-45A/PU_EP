package data;

final public class ePrescripCode {
    private final String code;

    public ePrescripCode(String code) throws InvalidePrescripCodeException {
        if (code == null) {
            throw new InvalidePrescripCodeException("El código de prescripción no puede ser null");
        }
        if (code.length() < 8 || code.length() > 20) {
            throw new InvalidePrescripCodeException("El código debe tener entre 8 y 20 caracteres");
        }
        if (!code.matches("[a-zA-Z0-9-]{8,20}")) {
            throw new InvalidePrescripCodeException("El código debe ser alfanumérico y puede contener guiones");
        }
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ePrescripCode that = (ePrescripCode) o;
        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "ePrescripCode{" + "code='" + code + '\'' + '}';
    }
}
