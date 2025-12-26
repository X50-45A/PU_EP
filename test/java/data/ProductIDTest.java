package data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductID Tests")
public class ProductIDTest {

    @Test
    @DisplayName("Constructor accepts valid 12-digit UPC code")
    void testConstructorWithValidCode() {
        String validCode = "123456789012";
        ProductID pid = new ProductID(validCode);
        assertEquals(validCode, pid.getProductID());
    }

    @Test
    @DisplayName("Constructor throws exception when code is null")
    void testConstructorWithNull() {
        assertThrows(Exception.class, () -> {
            new ProductID(null);
        });
    }

    @Test
    @DisplayName("Constructor throws exception when code has invalid format")
    void testConstructorWithInvalidFormat() {
        // Too short
        assertThrows(Exception.class, () -> {
            new ProductID("12345");
        });
        
        // Too long
        assertThrows(Exception.class, () -> {
            new ProductID("1234567890123456");
        });
        
        // Non-numeric
        assertThrows(Exception.class, () -> {
            new ProductID("12345678901A");
        });
    }

    @Test
    @DisplayName("equals returns true for same value")
    void testEqualsWithSameValue() {
        ProductID id1 = new ProductID("123456789012");
        ProductID id2 = new ProductID("123456789012");
        assertEquals(id1, id2);
    }

    @Test
    @DisplayName("equals returns false for different values")
    void testEqualsWithDifferentValue() {
        ProductID id1 = new ProductID("123456789012");
        ProductID id2 = new ProductID("210987654321");
        assertNotEquals(id1, id2);
    }

    @Test
    @DisplayName("hashCode is consistent for same value")
    void testHashCodeConsistency() {
        ProductID id1 = new ProductID("123456789012");
        ProductID id2 = new ProductID("123456789012");
        assertEquals(id1.hashCode(), id2.hashCode());
    }
}
