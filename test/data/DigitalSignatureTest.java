package data;

import data.DigitalSignature;
import data.InvalidDigitalSignatureException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DigitalSignature Tests")
public class DigitalSignatureTest {

    @Test
    @DisplayName("Constructor accepts valid non-empty byte array")
    void testConstructorWithValidByteArray() throws InvalidDigitalSignatureException {
        byte[] validSignature = new byte[]{1, 2, 3, 4, 5};
        DigitalSignature ds = new DigitalSignature(validSignature);
        assertNotNull(ds.getSignature());
        assertArrayEquals(validSignature, ds.getSignature());
    }

    @Test
    @DisplayName("Constructor throws exception when byte array is null")
    void testConstructorWithNull() {
        assertThrows(Exception.class, () -> {
            new DigitalSignature(null);
        });
    }

    @Test
    @DisplayName("Constructor throws exception when byte array is empty")
    void testConstructorWithEmptyByteArray() {
        assertThrows(Exception.class, () -> {
            new DigitalSignature(new byte[]{});
        });
    }

    @Test
    @DisplayName("equals returns true for same value")
    void testEqualsWithSameValue() throws InvalidDigitalSignatureException {
        byte[] sig = new byte[]{1, 2, 3, 4, 5};
        DigitalSignature ds1 = new DigitalSignature(sig);
        DigitalSignature ds2 = new DigitalSignature(sig.clone());
        assertEquals(ds1, ds2);
    }

    @Test
    @DisplayName("equals returns false for different values")
    void testEqualsWithDifferentValue() throws InvalidDigitalSignatureException {
        DigitalSignature ds1 = new DigitalSignature(new byte[]{1, 2, 3, 4, 5});
        DigitalSignature ds2 = new DigitalSignature(new byte[]{5, 4, 3, 2, 1});
        assertNotEquals(ds1, ds2);
    }

    @Test
    @DisplayName("hashCode is consistent for same value")
    void testHashCodeConsistency() throws InvalidDigitalSignatureException {
        byte[] sig = new byte[]{1, 2, 3, 4, 5};
        DigitalSignature ds1 = new DigitalSignature(sig);
        DigitalSignature ds2 = new DigitalSignature(sig.clone());
        assertEquals(ds1.hashCode(), ds2.hashCode());
    }
}