package data;
import data.HealthCardID;
import org.junit.jupiter.api.Test;
import services.HealthCardIDException;

import static org.junit.jupiter.api.Assertions.*;

public class HealthCardIDTest {
    
    // Success case
    @Test
    void testConstructorWithValidCode() {
        String validCode = "1234567890ABCDEF"; // 16 caracteres
        HealthCardID hcid = new HealthCardID(validCode);
        assertEquals(validCode, hcid.getPersonalID());
    }
    
    // Exception: null
    @Test
    void testConstructorWithNull() {
        assertThrows(HealthCardIDException.class, () -> {
            new HealthCardID(null);
        });
    }
    
    // Exception: código muy corto
    @Test
    void testConstructorWithShortCode() {
        assertThrows(HealthCardIDException.class, () -> {
            new HealthCardID("123456789");
        });
    }
    
    // Exception: código muy largo
    @Test
    void testConstructorWithLongCode() {
        assertThrows(HealthCardIDException.class, () -> {
            new HealthCardID("1234567890ABCDEFGHIJ");
        });
    }
    
    // equals test
    @Test
    void testEqualsWithSameValue() {
        HealthCardID id1 = new HealthCardID("1234567890ABCDEF");
        HealthCardID id2 = new HealthCardID("1234567890ABCDEF");
        assertEquals(id1, id2);
    }
    
    @Test
    void testEqualsWithDifferentValue() {
        HealthCardID id1 = new HealthCardID("1234567890ABCDEF");
        HealthCardID id2 = new HealthCardID("FEDCBA0987654321");
        assertNotEquals(id1, id2);
    }
}
