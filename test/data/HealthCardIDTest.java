package data;
import consultamedica.IncorrectParametersException;
import data.HealthCardID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.HealthCardIDException;

import static org.junit.jupiter.api.Assertions.*;

public class HealthCardIDTest {
    @Test
    @DisplayName("Constructor with valid code")
    void testConstructorWithValidCode() throws InvalidHealthCardIDException, IncorrectParametersException {
        HealthCardID id = new HealthCardID("1234567890ABCDEF");
        assertEquals("1234567890ABCDEF", id.getPersonalID());
    }

    @Test
    @DisplayName("Constructor with null throws exception")
    void testConstructorWithNull() {
        assertThrows(InvalidHealthCardIDException.class, () -> {
            new HealthCardID(null);
        });
    }

    @Test
    @DisplayName("Constructor with short code throws exception")
    void testConstructorWithShortCode() {
        assertThrows(InvalidHealthCardIDException.class, () -> {
            new HealthCardID("123"); // Solo 3 caracteres
        });
    }

    @Test
    @DisplayName("Constructor with long code throws exception")
    void testConstructorWithLongCode() {
        assertThrows(InvalidHealthCardIDException.class, () -> {
            new HealthCardID("12345678901234567"); // 17 caracteres
        });
    }

    @Test
    @DisplayName("Equals returns true for same code")
    void testEquals() throws InvalidHealthCardIDException, IncorrectParametersException {
        HealthCardID id1 = new HealthCardID("1234567890ABCDEF");
        HealthCardID id2 = new HealthCardID("1234567890ABCDEF");
        assertEquals(id1, id2);
    }

    @Test
    @DisplayName("HashCode is consistent")
    void testHashCode() throws InvalidHealthCardIDException, IncorrectParametersException {
        HealthCardID id1 = new HealthCardID("1234567890ABCDEF");
        HealthCardID id2 = new HealthCardID("1234567890ABCDEF");
        assertEquals(id1.hashCode(), id2.hashCode());
    }
}
