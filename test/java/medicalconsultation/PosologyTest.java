package medicalconsultation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Posology Tests")
public class PosologyTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Constructor accepts valid parameters")
        void testConstructorWithValidParameters() {
            Posology pos = new Posology(1.0f, 1.0f, FqUnit.DAY);
            assertEquals(1.0f, pos.getDose());
            assertEquals(1.0f, pos.getFreq());
            assertEquals(FqUnit.DAY, pos.getFreqUnit());
        }

        @Test
        @DisplayName("Constructor throws exception when dose is negative")
        void testConstructorWithNegativeDose() {
            assertThrows(Exception.class, () -> {
                new Posology(-1.0f, 1.0f, FqUnit.DAY);
            });
        }

        @Test
        @DisplayName("Constructor throws exception when dose is zero")
        void testConstructorWithZeroDose() {
            assertThrows(Exception.class, () -> {
                new Posology(0.0f, 1.0f, FqUnit.DAY);
            });
        }

        @Test
        @DisplayName("Constructor throws exception when frequency is negative")
        void testConstructorWithNegativeFreq() {
            assertThrows(Exception.class, () -> {
                new Posology(1.0f, -1.0f, FqUnit.DAY);
            });
        }

        @Test
        @DisplayName("Constructor throws exception when frequency is zero")
        void testConstructorWithZeroFreq() {
            assertThrows(Exception.class, () -> {
                new Posology(1.0f, 0.0f, FqUnit.DAY);
            });
        }

        @Test
        @DisplayName("Constructor throws exception when FqUnit is null")
        void testConstructorWithNullFqUnit() {
            assertThrows(Exception.class, () -> {
                new Posology(1.0f, 1.0f, null);
            });
        }
    }

    @Nested
    @DisplayName("Getter Tests")
    class GetterTests {

        @Test
        @DisplayName("getDose returns correct value")
        void testGetDose() {
            Posology pos = new Posology(2.5f, 1.0f, FqUnit.DAY);
            assertEquals(2.5f, pos.getDose());
        }

        @Test
        @DisplayName("getFreq returns correct value")
        void testGetFreq() {
            Posology pos = new Posology(1.0f, 3.0f, FqUnit.HOUR);
            assertEquals(3.0f, pos.getFreq());
        }

        @Test
        @DisplayName("getFreqUnit returns correct value")
        void testGetFreqUnit() {
            Posology pos = new Posology(1.0f, 1.0f, FqUnit.WEEK);
            assertEquals(FqUnit.WEEK, pos.getFreqUnit());
        }
    }

    @Nested
    @DisplayName("Setter Tests")
    class SetterTests {

        @Test
        @DisplayName("setDose updates value correctly")
        void testSetDose() {
            Posology pos = new Posology(1.0f, 1.0f, FqUnit.DAY);
            pos.setDose(3.0f);
            assertEquals(3.0f, pos.getDose());
        }

        @Test
        @DisplayName("setDose throws exception when value is negative")
        void testSetDoseNegative() {
            Posology pos = new Posology(1.0f, 1.0f, FqUnit.DAY);
            assertThrows(Exception.class, () -> {
                pos.setDose(-1.0f);
            });
        }

        @Test
        @DisplayName("setFreq updates value correctly")
        void testSetFreq() {
            Posology pos = new Posology(1.0f, 1.0f, FqUnit.DAY);
            pos.setFreq(2.0f);
            assertEquals(2.0f, pos.getFreq());
        }

        @Test
        @DisplayName("setFreq throws exception when value is zero")
        void testSetFreqZero() {
            Posology pos = new Posology(1.0f, 1.0f, FqUnit.DAY);
            assertThrows(Exception.class, () -> {
                pos.setFreq(0.0f);
            });
        }

        @Test
        @DisplayName("setFreqUnit updates value correctly")
        void testSetFreqUnit() {
            Posology pos = new Posology(1.0f, 1.0f, FqUnit.DAY);
            pos.setFreqUnit(FqUnit.HOUR);
            assertEquals(FqUnit.HOUR, pos.getFreqUnit());
        }

        @Test
        @DisplayName("setFreqUnit throws exception when null")
        void testSetFreqUnitNull() {
            Posology pos = new Posology(1.0f, 1.0f, FqUnit.DAY);
            assertThrows(Exception.class, () -> {
                pos.setFreqUnit(null);
            });
        }
    }
}
