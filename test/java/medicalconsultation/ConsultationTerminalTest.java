package medicalconsultation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import data.*;
import services.*;
import medicalconsultation.services.doubles.*;
import static org.junit.jupiter.api.Assertions.*;

public class ConsultationTerminalTest {
    
    private ConsultationTerminal terminal;
    private HealthNationalServiceStub healthService;
    private HealthCardID validCIP;
    private ProductID medicine1;
    private String[] guidelines;
    private Date futureDate;
    
    @BeforeEach
    void setUp() throws Exception {
        terminal = new ConsultationTerminal();
        healthService = new HealthNationalServiceStub();
        terminal.setHealthNationalService(healthService);
        
        validCIP = new HealthCardID("1234567890ABCDEF");
        medicine1 = new ProductID("123456789012");
        
        guidelines = new String[]{
            "BEFORELUNCH", "15", "1", "1", "DAY", "Con agua"
        };
        
        futureDate = new Date(System.currentTimeMillis() + 
                             (long) 15 * 24 * 60 * 60 * 1000); // 15 días después
    }
    
    // === SUCCESSFUL FLOW ===
    
    @Test
    void testCompleteSuccessfulFlow() throws Exception {
        // 1. Iniciar revisión
        terminal.initRevision(validCIP, "Hipertensión");
        assertTrue