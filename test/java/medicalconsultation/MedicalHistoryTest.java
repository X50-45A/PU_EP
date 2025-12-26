package medicalconsultation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import data.*;
import static org.junit.jupiter.api.Assertions.*;

public class MedicalHistoryTest {
    
    private HealthCardID validCIP;
    
    @BeforeEach
    void setUp() throws HealthCardIDException {
        validCIP = new HealthCardID("1234567890ABCDEF");
    }
    
    // Constructor success
    @Test
    void testConstructorWithValidParameters() throws IncorrectParametersException {
        MedicalHistory mh = new MedicalHistory(validCIP, 100);
        assertNotNull(mh);
        assertEquals(validCIP, mh.getHealthCardID());
        assertEquals(100, mh.getMembShipNumb());
    }
    
    // Constructor exceptions
    @Test
    void testConstructorWithNullCIP() {
        assertThrows(IncorrectParametersException.class, () -> {
            new MedicalHistory(null, 100);
        });
    }
    
    @Test
    void testConstructorWithInvalidMembshipNum() {
        assertThrows(IncorrectParametersException.class, () -> {
            new MedicalHistory(validCIP, 0);
        });
        
        assertThrows(IncorrectParametersException.class, () -> {
            new MedicalHistory(validCIP, -5);
        });
    }
    
    // addMedicalHistoryAnnotations
    @Test
    void testAddAnnotation() throws IncorrectParametersException {
        MedicalHistory mh = new MedicalHistory(validCIP, 100);
        mh.addMedicalHistoryAnnotations("Paciente con hipertensión");
        assertTrue(mh.getHistory().contains("hipertensión"));
    }
    
    @Test
    void testAddMultipleAnnotations() throws IncorrectParametersException {
        MedicalHistory mh = new MedicalHistory(validCIP, 100);
        mh.addMedicalHistoryAnnotations("Anotación 1");
        mh.addMedicalHistoryAnnotations("Anotación 2");
        String history = mh.getHistory();
        assertTrue(history.contains("Anotación 1"));
        assertTrue(history.contains("Anotación 2"));
    }
    
    @Test
    void testAddAnnotationWithNull() throws IncorrectParametersException {
        MedicalHistory mh = new MedicalHistory(validCIP, 100);
        assertThrows(IncorrectParametersException.class, () -> {
            mh.addMedicalHistoryAnnotations(null);
        });
    }
    
    // setNewDoctor
    @Test
    void testSetNewDoctorWithValidNumber() throws IncorrectParametersException {
        MedicalHistory mh = new MedicalHistory(validCIP, 100);
        mh.setNewDoctor(200);
        assertEquals(200, mh.getMembShipNumb());
    }
    
    @Test
    void testSetNewDoctorWithInvalidNumber() throws IncorrectParametersException {
        MedicalHistory mh = new MedicalHistory(validCIP, 100);
        assertThrows(IncorrectParametersException.class, () -> {
            mh.setNewDoctor(0);
        });
    }
}