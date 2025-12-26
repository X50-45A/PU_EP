package medicalconsultation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import data.*;
import static org.junit.jupiter.api.Assertions.*;

public class MedicalPrescriptionTest {
    
    private MedicalPrescription prescription;
    private HealthCardID cip;
    private ProductID product1;
    private ProductID product2;
    private String[] validGuidelines;
    
    @BeforeEach
    void setUp() throws Exception {
        cip = new HealthCardID("1234567890ABCDEF");
        product1 = new ProductID("123456789012");
        product2 = new ProductID("210987654321");
        
        // Guidelines: dayMoment, duration, dose, freq, freqUnit, instructions
        validGuidelines = new String[]{
            "BEFORELUNCH",  // dayMoment
            "15",           // duration
            "1",            // dose
            "1",            // frequency
            "DAY",          // freqUnit
            "Tomar con agua"// instructions
        };
        
        prescription = new MedicalPrescription(cip, 100, "Hipertensión");
    }
    
    // === ADDLINE TESTS ===
    
    @Test
    void testAddLineWithValidProduct() throws Exception {
        prescription.addLine(product1, validGuidelines);
        // Verificar que la línea se añadió
        assertDoesNotThrow(() -> prescription.getPrescriptionLine(product1));
    }
    
    @Test
    void testAddLineWithDuplicateProduct() throws Exception {
        prescription.addLine(product1, validGuidelines);
        // Intentar añadir el mismo producto otra vez
        assertThrows(ProductAlreadyInPrescriptionException.class, () -> {
            prescription.addLine(product1, validGuidelines);
        });
    }
    
    @Test
    void testAddLineWithNullProduct() {
        assertThrows(IllegalArgumentException.class, () -> {
            prescription.addLine(null, validGuidelines);
        });
    }
    
    @Test
    void testAddLineWithNullGuidelines() {
        assertThrows(IncorrectTakingGuidelinesException.class, () -> {
            prescription.addLine(product1, null);
        });
    }
    
    @Test
    void testAddLineWithIncompleteGuidelines() {
        String[] incompleteGuidelines = new String[]{
            "BEFORELUNCH",
            "15"
            // Faltan elementos
        };
        assertThrows(IncorrectTakingGuidelinesException.class, () -> {
            prescription.addLine(product1, incompleteGuidelines);
        });
    }
    
    @Test
    void testAddMultipleDifferentProducts() throws Exception {
        prescription.addLine(product1, validGuidelines);
        prescription.addLine(product2, validGuidelines);
        // Ambos productos deberían estar presentes
        assertDoesNotThrow(() -> prescription.getPrescriptionLine(product1));
        assertDoesNotThrow(() -> prescription.getPrescriptionLine(product2));
    }
    
    // === MODIFYDOSE TESTS ===
    
    @Test
    void testModifyDoseInExistingLine() throws Exception {
        prescription.addLine(product1, validGuidelines);
        prescription.modifyDoseInLine(product1, 2.5f);
        // Verificar que la dosis se modificó
        PrescriptionLine line = prescription.getPrescriptionLine(product1);
        assertEquals(2.5f, line.getTakingGuideline().getPosology().getDose());
    }
    
    @Test
    void testModifyDoseInNonExistentProduct() {
        assertThrows(ProductNotInPrescriptionException.class, () -> {
            prescription.modifyDoseInLine(product1, 2.5f);
        });
    }
    
    @Test
    void testModifyDoseWithNegativeValue() throws Exception {
        prescription.addLine(product1, validGuidelines);
        assertThrows(IllegalArgumentException.class, () -> {
            prescription.modifyDoseInLine(product1, -1.0f);
        });
    }
    
    @Test
    void testModifyDoseWithZero() throws Exception {
        prescription.addLine(product1, validGuidelines);
        assertThrows(IllegalArgumentException.class, () -> {
            prescription.modifyDoseInLine(product1, 0.0f);
        });
    }
    
    @Test
    void testModifyDoseDoesNotAffectOtherLines() throws Exception {
        prescription.addLine(product1, validGuidelines);
        prescription.addLine(product2, validGuidelines);
        
        prescription.modifyDoseInLine(product1, 5.0f);
        
        // product2 no debe haber cambiado
        PrescriptionLine line2 = prescription.getPrescriptionLine(product2);
        assertEquals(1.0f, line2.getTakingGuideline().getPosology().getDose());
    }
    
    // === REMOVELINE TESTS ===
    
    @Test
    void testRemoveExistingLine() throws Exception {
        prescription.addLine(product1, validGuidelines);
        prescription.removeLine(product1);
        
        assertThrows(ProductNotInPrescriptionException.class, () -> {
            prescription.getPrescriptionLine(product1);
        });
    }
    
    @Test
    void testRemoveNonExistentProduct() {
        assertThrows(ProductNotInPrescriptionException.class, () -> {
            prescription.removeLine(product1);
        });
    }
    
    @Test
    void testRemoveDoesNotAffectOtherLines() throws Exception {
        prescription.addLine(product1, validGuidelines);
        prescription.addLine(product2, validGuidelines);
        
        prescription.removeLine(product1);
        
        // product2 debe seguir siendo accesible
        assertDoesNotThrow(() -> prescription.getPrescriptionLine(product2));
    }
    
    @Test
    void testRemoveMultipleLines() throws Exception {
        prescription.addLine(product1, validGuidelines);
        prescription.addLine(product2, validGuidelines);
        
        prescription.removeLine(product1);
        prescription.removeLine(product2);
        
        assertThrows(ProductNotInPrescriptionException.class, () -> {
            prescription.getPrescriptionLine(product1);
        });
        
        assertThrows(ProductNotInPrescriptionException.class, () -> {
            prescription.getPrescriptionLine(product2);
        });
    }
}
