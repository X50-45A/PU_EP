package medicalconsultation;

import consultamedica.*;
import data.InvalidProductIDException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import data.HealthCardID;
import data.ProductID;
import data.ePrescripCode;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MedicalPrescription Tests")
public class MedicalPrescriptionTest {

    private MedicalPrescription prescription;
    private HealthCardID cip;
    private ProductID product1;
    private ProductID product2;
    private String[] validGuidelines;

    @BeforeEach
    void setUp() throws Exception {
        cip = new HealthCardID("1234567890ABCDEF");
        prescription = new MedicalPrescription(cip, 12345, "Hipertensión");

        product1 = new ProductID("123456789012");
        product2 = new ProductID("987654321098");

        // ✅ 7 elementos (no 6)
        validGuidelines = new String[]{
                "BEFORELUNCH", "15", "1", "1", "DAY", "Con agua", ""
        };
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Constructor accepts valid parameters")
        void testConstructorWithValidParameters() {
            assertEquals(cip, prescription.getCip());
            assertEquals(12345, prescription.getMembShipNumb()); // ← Cambiar aquí
            assertEquals("Hipertensión", prescription.getIllness());
            assertEquals(0, prescription.getLineCount());
        }

        @Test
        @DisplayName("Constructor throws exception when HealthCardID is null")
        void testConstructorWithNullCIP() {
            assertThrows(Exception.class, () -> {
                new MedicalPrescription(null, 100, "Hipertensión");
            });
        }

        @Test
        @DisplayName("Constructor throws exception when membership <= 0")
        void testConstructorWithInvalidMembership() {
            assertThrows(Exception.class, () -> {
                new MedicalPrescription(cip, 0, "Hipertensión");
            });

            assertThrows(Exception.class, () -> {
                new MedicalPrescription(cip, -5, "Hipertensión");
            });
        }

        @Test
        @DisplayName("Constructor throws exception when illness is null")
        void testConstructorWithNullIllness() {
            assertThrows(Exception.class, () -> {
                new MedicalPrescription(cip, 100, null);
            });
        }

        @Test
        @DisplayName("Constructor throws exception when illness is empty")
        void testConstructorWithEmptyIllness() {
            assertThrows(Exception.class, () -> {
                new MedicalPrescription(cip, 100, "");
            });
        }
    }

    @Nested
    @DisplayName("addLine() Success Tests")
    class AddLineSuccessTests {

        @Test
        @DisplayName("addLine adds medicine with valid guidelines")
        void testAddLineWithValidProduct() {
            assertDoesNotThrow(() -> {
                prescription.addLine(product1, validGuidelines);
            });
        }

        @Test
        @DisplayName("addLine medicine can be retrieved")
        void testAddLineAndRetrieve() throws ProductAlreadyInPrescriptionException, IncorrectTakingGuidelinesException {
            prescription.addLine(product1, validGuidelines);
            assertDoesNotThrow(() -> {
                prescription.containsProduct(product1);
            });
        }

        @Test
        @DisplayName("addLine multiple different products")
        void testAddMultipleDifferentProducts() throws ProductAlreadyInPrescriptionException, IncorrectTakingGuidelinesException {
            prescription.addLine(product1, validGuidelines);
            prescription.addLine(product2, validGuidelines);
            
            assertDoesNotThrow(() -> {
                prescription.containsProduct(product1);
                prescription.containsProduct(product2);
            });
        }
    }

    @Nested
    @DisplayName("addLine() Exception Tests")
    class AddLineExceptionTests {

        @Test
        @DisplayName("addLine throws exception with duplicate ProductID")
        void testAddLineDuplicateProduct() throws ProductAlreadyInPrescriptionException, IncorrectTakingGuidelinesException {
            prescription.addLine(product1, validGuidelines);
            assertThrows(Exception.class, () -> {
                prescription.addLine(product1, validGuidelines);
            });
        }

        @Test
        @DisplayName("addLine throws exception with null ProductID")
        void testAddLineWithNullProduct() {
            assertThrows(Exception.class, () -> {
                prescription.addLine(null, validGuidelines);
            });
        }

        @Test
        @DisplayName("addLine throws exception with null guidelines")
        void testAddLineWithNullGuidelines() {
            assertThrows(Exception.class, () -> {
                prescription.addLine(product1, null);
            });
        }

        @Test
        @DisplayName("addLine throws exception with incomplete guidelines")
        void testAddLineWithIncompleteGuidelines() {
            String[] incompleteGuidelines = new String[]{"BEFORELUNCH", "15"};
            assertThrows(Exception.class, () -> {
                prescription.addLine(product1, incompleteGuidelines);
            });
        }

        @Test
        @DisplayName("addLine throws exception with malformed guidelines")
        void testAddLineWithMalformedGuidelines() {
            String[] malformedGuidelines = new String[]{"INVALIDMOMENT", "15", "1", "1", "DAY", "Con agua"};
            assertThrows(Exception.class, () -> {
                prescription.addLine(product1, malformedGuidelines);
            });
        }
    }

    @Nested
    @DisplayName("modifyDoseInLine() Success Tests")
    class ModifyDoseSuccessTests {

        @BeforeEach
        void setUp() throws ProductAlreadyInPrescriptionException, IncorrectTakingGuidelinesException {
            prescription.addLine(product1, validGuidelines);
        }

        @Test
        @DisplayName("modifyDoseInLine updates dose correctly")
        void testModifyDoseInExistingLine() throws ProductNotInPrescriptionException {
            prescription.modifyDoseInLine(product1, 2.0f);

            // Obtener la línea y verificar la dosis
            MedicalPrescriptionLine line = prescription.getLine(product1);
            assertNotNull(line);
            assertEquals(2.0f, line.getGuidelines().getPosology().getDose());
        }

        @Test
        @DisplayName("modifyDoseInLine does not affect other lines")
        void testModifyDoseDoesNotAffectOtherLines() throws ProductAlreadyInPrescriptionException,
                IncorrectTakingGuidelinesException, ProductNotInPrescriptionException {
            prescription.addLine(product2, validGuidelines);
            prescription.modifyDoseInLine(product1, 5.0f);

            // Verificar que product1 cambió
            MedicalPrescriptionLine line1 = prescription.getLine(product1);
            assertNotNull(line1);
            assertEquals(5.0f, line1.getGuidelines().getPosology().getDose());

            // Verificar que product2 NO cambió
            MedicalPrescriptionLine line2 = prescription.getLine(product2);
            assertNotNull(line2);
            assertEquals(1.0f, line2.getGuidelines().getPosology().getDose());
        }

        @Test
        @DisplayName("modifyDoseInLine multiple times")
        void testModifyDoseMultipleTimes() throws ProductNotInPrescriptionException {
            prescription.modifyDoseInLine(product1, 2.0f);
            prescription.modifyDoseInLine(product1, 3.0f);

            MedicalPrescriptionLine line = prescription.getLine(product1);
            assertNotNull(line);
            assertEquals(3.0f, line.getGuidelines().getPosology().getDose());
        }

        @Test
        @DisplayName("modifyDoseInLine with decimal values")
        void testModifyDoseWithDecimalValues() throws ProductNotInPrescriptionException {
            prescription.modifyDoseInLine(product1, 2.5f);

            MedicalPrescriptionLine line = prescription.getLine(product1);
            assertNotNull(line);
            assertEquals(2.5f, line.getGuidelines().getPosology().getDose(), 0.001);
        }
    }

    @Nested
    @DisplayName("modifyDoseInLine() Exception Tests")
    class ModifyDoseExceptionTests {

        @Test
        @DisplayName("modifyDoseInLine throws exception for non-existent ProductID")
        void testModifyDoseNonExistentProduct() {
            assertThrows(Exception.class, () -> {
                prescription.modifyDoseInLine(product1, 2.0f);
            });
        }

        @Test
        @DisplayName("modifyDoseInLine throws exception with negative dose")
        void testModifyDoseNegative() throws ProductAlreadyInPrescriptionException, IncorrectTakingGuidelinesException {
            prescription.addLine(product1, validGuidelines);
            assertThrows(Exception.class, () -> {
                prescription.modifyDoseInLine(product1, -1.0f);
            });
        }

        @Test
        @DisplayName("modifyDoseInLine throws exception with zero dose")
        void testModifyDoseZero() throws ProductAlreadyInPrescriptionException, IncorrectTakingGuidelinesException {
            prescription.addLine(product1, validGuidelines);
            assertThrows(Exception.class, () -> {
                prescription.modifyDoseInLine(product1, 0.0f);
            });
        }
    }

    @Nested
    @DisplayName("removeLine() Success Tests")
    class RemoveLineSuccessTests {



        @Test
        @DisplayName("removeLine removes existing line")
        void testRemoveExistingLine() throws ProductNotInPrescriptionException, ProductAlreadyInPrescriptionException, IncorrectTakingGuidelinesException {
            // Verificar que existen
            prescription.addLine(product1, validGuidelines);
            prescription.addLine(product2, validGuidelines);
            assertEquals(2, prescription.getLineCount());
            assertTrue(prescription.containsProduct(product1));

            // Remover
            prescription.removeLine(product1.getCode());

            // Verificar
            assertEquals(1, prescription.getLineCount());
            assertFalse(prescription.containsProduct(product1));
            assertTrue(prescription.containsProduct(product2)); // Sigue existiendo
        }

        @Test
        @DisplayName("removeLine does not affect other lines")
        void testRemoveDoesNotAffectOtherLines() throws Exception {
            prescription.addLine(product1, validGuidelines);
            prescription.addLine(product2, validGuidelines);
            prescription.removeLine(product1.getCode());

            // product2 sigue existiendo
            assertTrue(prescription.containsProduct(product2));
            assertEquals(1, prescription.getLineCount());
        }

        @Test
        @DisplayName("removeLine multiple lines")
        void testRemoveMultipleLines() throws Exception {
            prescription.addLine(product1, validGuidelines);
            prescription.addLine(product2, validGuidelines);
            prescription.removeLine(product1.getCode());
            prescription.removeLine(product2.getCode());

            assertEquals(0, prescription.getLineCount());
        }
    }

    @Nested
    @DisplayName("removeLine() Exception Tests")
    class RemoveLineExceptionTests {

        @Test
        @DisplayName("removeLine throws exception for non-existent ProductID")
        void testRemoveNonExistentProduct() {
            assertThrows(Exception.class, () -> {
                prescription.removeLine(product1.getCode());
            });
        }
    }

    @Nested
    @DisplayName("Getter Tests")
    class GetterTests {

        @Test
        @DisplayName("getHealthCardID returns correct CIP")
        void testGetHealthCardID() {
            assertEquals(cip, prescription.getCip());
        }

        @Test
        @DisplayName("getMembShipNumb returns correct number")
        void testGetMembShipNumb() {
            assertEquals(12345, prescription.getMembShipNumb());
        }

        @Test
        @DisplayName("getIllness returns correct illness")
        void testGetIllness() {
            assertEquals("Hipertensión", prescription.getIllness());
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Add, modify, remove same product in sequence")
        void testAddModifyRemoveSequence() throws ProductAlreadyInPrescriptionException, IncorrectTakingGuidelinesException, ProductNotInPrescriptionException {
            prescription.addLine(product1, validGuidelines);
            prescription.modifyDoseInLine(product1, 2.0f);
            prescription.removeLine(product1.getCode());
            
            assertThrows(Exception.class, () -> {
                prescription.getLine(product1);
            });
        }
    }
}