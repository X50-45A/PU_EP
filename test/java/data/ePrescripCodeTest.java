package java.data;

import data.InvalidePrescripCodeException;
import data.ePrescripCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ePrescripCode Tests")
public class ePrescripCodeTest {

    @Test
    @DisplayName("Constructor accepts valid alphanumeric code")
    void testConstructorWithValidCode() throws InvalidePrescripCodeException {
        String validCode = "PRESC123456";
        ePrescripCode epc = new ePrescripCode(validCode);
        assertEquals(validCode, epc.getePrescripCode());
    }

    @Test
    @DisplayName("Constructor throws exception when code is null")
    void testConstructorWithNull() {
        assertThrows(Exception.class, () -> {
            new ePrescripCode(null);
        });
    }

    @Test
    @DisplayName("Constructor throws exception when code is empty")
    void testConstructorWithEmptyCode() {
        assertThrows(Exception.class, () -> {
            new ePrescripCode("");
        });
    }

    @Test
    @DisplayName("Constructor throws exception when code has invalid format")
    void testConstructorWithInvalidFormat() {
        // Too short (less than 6)
        assertThrows(Exception.class, () -> {
            new ePrescripCode("ABC");
        });
        
        // Too long (more than 20)
        assertThrows(Exception.class, () -> {
            new ePrescripCode("ABCDEFGHIJKLMNOPQRSTU");
        });
    }

    @Test
    @DisplayName("equals returns true for same value")
    void testEqualsWithSameValue() {
        ePrescripCode epc1 = new ePrescripCode("PRESC123456");
        ePrescripCode epc2 = new ePrescripCode("PRESC123456");
        assertEquals(epc1, epc2);
    }

    @Test
    @DisplayName("equals returns false for different values")
    void testEqualsWithDifferentValue() {
        ePrescripCode epc1 = new ePrescripCode("PRESC123456");
        ePrescripCode epc2 = new ePrescripCode("PRESC654321");
        assertNotEquals(epc1, epc2);
    }

    @Test
    @DisplayName("hashCode is consistent for same value")
    void testHashCodeConsistency() {
        ePrescripCode epc1 = new ePrescripCode("PRESC123456");
        ePrescripCode epc2 = new ePrescripCode("PRESC123456");
        assertEquals(epc1.hashCode(), epc2.hashCode());
    }
}
