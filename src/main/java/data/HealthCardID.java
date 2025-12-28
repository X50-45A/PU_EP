
package data;

import consultamedica.IncorrectParametersException;

/**
 * The personal identifying code in the National Health Service.
 */
final public class HealthCardID {
    private final String personalID;
    public HealthCardID(String code) throws InvalidHealthCardIDException {
        if (code == null) {
            throw new InvalidHealthCardIDException("La personalID no puede ser nula");
        }
        if (code.length() != 16) {
            throw new InvalidHealthCardIDException("El código debe tener 16 caracteres");
        }
        if (!code.matches("[a-zA-Z0-9]{16}")) {
            throw new InvalidHealthCardIDException("El código debe ser alfanumérico");
        }
        this.personalID = code;
    }
    public String getPersonalID() {
        return personalID;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HealthCardID hcardID = (HealthCardID) o;
        return personalID.equals(hcardID.personalID);
    }
    @Override
    public int hashCode() { return personalID.hashCode(); }
    @Override
    public String toString() {
        return "HealthCardID{" + "personal code='" + personalID + '\'' + '}';
    }
}