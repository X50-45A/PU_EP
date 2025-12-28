package medicalconsultation;

import consultamedica.IncorrectParametersException;
import consultamedica.MedicalHistory;
import data.InvalidHealthCardIDException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import data.HealthCardID;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MedicalHistory Tests")
public class MedicalHistoryTest {

    private HealthCardID validCIP;

    @BeforeEach
    void setUp() throws IncorrectParametersException, InvalidHealthCardIDException {
        validCIP = new HealthCardID("1234567890ABCDEF");
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Constructor accepts valid HealthCardID and membership > 0")
        void testConstructorWithValidParameters() throws IncorrectParametersException {
            MedicalHistory mh = new MedicalHistory(validCIP, 100);
            assertNotNull(mh);
            assertEquals(validCIP.getPersonalID(), mh.getCip().getPersonalID());
            assertEquals(100, mh.getMembShipNumb());
        }

        @Test
        @DisplayName("Constructor throws exception when HealthCardID is null")
        void testConstructorWithNullCIP() {
            assertThrows(Exception.class, () -> {
                new MedicalHistory(null, 100);
            });
        }

        @Test
        @DisplayName("Constructor throws exception when membership is zero")
        void testConstructorWithZeroMembership() {
            assertThrows(Exception.class, () -> {
                new MedicalHistory(validCIP, 0);
            });
        }

        @Test
        @DisplayName("Constructor throws exception when membership is negative")
        void testConstructorWithNegativeMembership() {
            assertThrows(Exception.class, () -> {
                new MedicalHistory(validCIP, -5);
            });
        }
    }

    @Nested
    @DisplayName("addMedicalHistoryAnnotations Tests")
    class AnnotationTests {

        private MedicalHistory mh;

        @BeforeEach
        void setUp() throws IncorrectParametersException {
            mh = new MedicalHistory(validCIP, 100);
        }

        @Test
        @DisplayName("addAnnotation adds text to history")
        void testAddAnnotation() throws IncorrectParametersException {
            mh.addMedicalHistoryAnnotations("Paciente con hipertensión");
            String history = mh.getHistory();
            assertTrue(history.contains("hipertensión"));
        }

        @Test
        @DisplayName("addAnnotation multiple times concatenates")
        void testAddMultipleAnnotations() throws IncorrectParametersException {
            mh.addMedicalHistoryAnnotations("Anotación 1");
            mh.addMedicalHistoryAnnotations("Anotación 2");
            String history = mh.getHistory();
            assertTrue(history.contains("Anotación 1"));
            assertTrue(history.contains("Anotación 2"));
        }

        @Test
        @DisplayName("Add annotation with null is ignored")
        void testAddAnnotationWithNull() {
            String originalHistory = mh.getHistory();

            // NO lanza excepción, solo ignora
            assertDoesNotThrow(() -> {
                mh.addMedicalHistoryAnnotations(null);
            });

            // Verificar que no cambió
            assertEquals(originalHistory, mh.getHistory());
        }

        @Test
        @DisplayName("Add annotation with empty string is ignored")
        void testAddAnnotationWithEmpty() {
            String originalHistory = mh.getHistory();

            // NO lanza excepción, solo ignora
            assertDoesNotThrow(() -> {
                mh.addMedicalHistoryAnnotations("");
            });

            // Verificar que no cambió
            assertEquals(originalHistory, mh.getHistory());
        }
    }

    @Nested
    @DisplayName("setNewDoctor Tests")
    class DoctorTests {

        private MedicalHistory mh;

        @BeforeEach
        void setUp() throws IncorrectParametersException {
            mh = new MedicalHistory(validCIP, 100);
        }

        @Test
        @DisplayName("setNewDoctor updates membership number")
        void testSetNewDoctorWithValidNumber() {
            mh.setNewDoctor(200);
            assertEquals(200, mh.getMembShipNumb());
        }

        @Test
        @DisplayName("setNewDoctor throws exception when zero")
        void testSetNewDoctorWithZero() {
            assertThrows(Exception.class, () -> {
                mh.setNewDoctor(0);
            });
        }

        @Test
        @DisplayName("setNewDoctor throws exception when negative")
        void testSetNewDoctorWithNegative() {
            assertThrows(Exception.class, () -> {
                mh.setNewDoctor(-10);
            });
        }
    }

    @Nested
    @DisplayName("Getter Tests")
    class GetterTests {

        private MedicalHistory mh;

        @BeforeEach
        void setUp() throws IncorrectParametersException {
            mh = new MedicalHistory(validCIP, 100);
        }

        @Test
        @DisplayName("getHealthCardID returns correct CIP")
        void testGetHealthCardID() {
            assertEquals(validCIP.getPersonalID(), mh.getCip().getPersonalID());
        }

        @Test
        @DisplayName("getMembShipNumb returns correct number")
        void testGetMembShipNumb() {
            assertEquals(100, mh.getMembShipNumb());
        }

        @Test
        @DisplayName("getHistory returns history")
        void testGetHistory() throws IncorrectParametersException {
            mh.addMedicalHistoryAnnotations("Test");
            assertNotNull(mh.getHistory());
            assertTrue(mh.getHistory().contains("Test"));
        }
    }
}

