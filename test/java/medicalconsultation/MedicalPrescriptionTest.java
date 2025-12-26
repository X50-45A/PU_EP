package medicalconsultation;

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
    void setUp() {
        cip = new HealthCardID("1234567890ABCDEF");
        product1 = new ProductID("123456789012");
        product2 = new ProductID("210987654321");
        validGuidelines = new String[]{"BEFORELUNCH", "15", "1", "1", "DAY", "Tomar con agua"};
        prescription = new MedicalPrescription(cip, 100, "Hipertensión");
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Constructor accepts valid parameters")
        void testConstructorWithValidParameters() {
            assertNotNull(prescription);
            assertEquals(cip, prescription.getHealthCardID());
            assertEquals(100, prescription.getMembShipNumb());
            assertEquals("Hipertensión", prescription.getIllness());
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
        void testAddLineAndRetrieve() {
            prescription.addLine(product1, validGuidelines);
            assertDoesNotThrow(() -> {
                prescription.getPrescriptionLine(product1);
            });
        }

        @Test
        @DisplayName("addLine multiple different products")
        void testAddMultipleDifferentProducts() {
            prescription.addLine(product1, validGuidelines);
            prescription.addLine(product2, validGuidelines);
            
            assertDoesNotThrow(() -> {
                prescription.getPrescriptionLine(product1);
                prescription.getPrescriptionLine(product2);
            });
        }
    }

    @Nested
    @DisplayName("addLine() Exception Tests")
    class AddLineExceptionTests {

        @Test
        @DisplayName("addLine throws exception with duplicate ProductID")
        void testAddLineDuplicateProduct() {
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
        void setUp() {
            prescription.addLine(product1, validGuidelines);
        }

        @Test
        @DisplayName("modifyDoseInLine updates dose correctly")
        void testModifyDoseInExistingLine() {
            prescription.modifyDoseInLine(product1, 2.0f);
            assertEquals(2.0f, prescription.getPrescriptionLine(product1)
                    .getTakingGuideline().getPosology().getDose());
        }

        @Test
        @DisplayName("modifyDoseInLine does not affect other lines")
        void testModifyDoseDoesNotAffectOtherLines() {
            prescription.addLine(product2, validGuidelines);
            prescription.modifyDoseInLine(product1, 5.0f);
            
            assertEquals(1.0f, prescription.getPrescriptionLine(product2)
                    .getTakingGuideline().getPosology().getDose());
        }

        @Test
        @DisplayName("modifyDoseInLine multiple times")
        void testModifyDoseMultipleTimes() {
            prescription.modifyDoseInLine(product1, 2.0f);
            prescription.modifyDoseInLine(product1, 3.0f);
            
            assertEquals(3.0f, prescription.getPrescriptionLine(product1)
                    .getTakingGuideline().getPosology().getDose());
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
        void testModifyDoseNegative() {
            prescription.addLine(product1, validGuidelines);
            assertThrows(Exception.class, () -> {
                prescription.modifyDoseInLine(product1, -1.0f);
            });
        }

        @Test
        @DisplayName("modifyDoseInLine throws exception with zero dose")
        void testModifyDoseZero() {
            prescription.addLine(product1, validGuidelines);
            assertThrows(Exception.class, () -> {
                prescription.modifyDoseInLine(product1, 0.0f);
            });
        }
    }

    @Nested
    @DisplayName("removeLine() Success Tests")
    class RemoveLineSuccessTests {

        @BeforeEach
        void setUp() {
            prescription.addLine(product1, validGuidelines);
        }

        @Test
        @DisplayName("removeLine removes medicine correctly")
        void testRemoveExistingLine() {
            prescription.removeLine(product1);
            assertThrows(Exception.class, () -> {
                prescription.getPrescriptionLine(product1);
            });
        }

        @Test
        @DisplayName("removeLine does not affect other lines")
        void testRemoveDoesNotAffectOtherLines() {
            prescription.addLine(product2, validGuidelines);
            prescription.removeLine(product1);
            
            assertDoesNotThrow(() -> {
                prescription.getPrescriptionLine(product2);
            });
        }

        @Test
        @DisplayName("removeLine multiple medicines")
        void testRemoveMultipleLines() {
            prescription.addLine(product2, validGuidelines);
            prescription.removeLine(product1);
            prescription.removeLine(product2);
            
            assertThrows(Exception.class, () -> {
                prescription.getPrescriptionLine(product1);
            });
            assertThrows(Exception.class, () -> {
                prescription.getPrescriptionLine(product2);
            });
        }
    }

    @Nested
    @DisplayName("removeLine() Exception Tests")
    class RemoveLineExceptionTests {

        @Test
        @DisplayName("removeLine throws exception for non-existent ProductID")
        void testRemoveNonExistentProduct() {
            assertThrows(Exception.class, () -> {
                prescription.removeLine(product1);
            });
        }
    }

    @Nested
    @DisplayName("Getter Tests")
    class GetterTests {

        @Test
        @DisplayName("getHealthCardID returns correct CIP")
        void testGetHealthCardID() {
            assertEquals(cip, prescription.getHealthCardID());
        }

        @Test
        @DisplayName("getMembShipNumb returns correct number")
        void testGetMembShipNumb() {
            assertEquals(100, prescription.getMembShipNumb());
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
        void testAddModifyRemoveSequence() {
            prescription.addLine(product1, validGuidelines);
            prescription.modifyDoseInLine(product1, 2.0f);
            prescription.removeLine(product1);
            
            assertThrows(Exception.class, () -> {
                prescription.getPrescriptionLine(product1);
            });
        }
    }
}