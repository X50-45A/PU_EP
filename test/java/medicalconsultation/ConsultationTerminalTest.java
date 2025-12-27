package java.medicalconsultation;

import consultamedica.ConsultationTerminal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import data.*;
import services.doubles.*;
import java.util.Date;
import java.net.ConnectException;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ConsultationTerminal Tests")
public class ConsultationTerminalTest {

    private ConsultationTerminal terminal;
    private services.doubles.HealthNationalServiceStub healthServiceStub;
    private services.doubles.HealthNationalServiceMock healthServiceMock;
    private HealthCardID validCIP;
    private ProductID medicine1;
    private String[] validGuidelines;

    @BeforeEach
    void setUp() throws InvalidProductIDException {
        terminal = new ConsultationTerminal();
        healthServiceStub = new services.doubles.HealthNationalServiceStub();
        healthServiceMock = new services.doubles.HealthNationalServiceMock();

        validCIP = new HealthCardID("1234567890ABCDEF");
        medicine1 = new ProductID("123456789012");

        validGuidelines = new String[]{
            "BEFORELUNCH", "15", "1", "1", "DAY", "Con agua"
        };
    }

    // FLUJO EXITOSO COMPLETO
    @Nested
    @DisplayName("Successful Complete Flow")
    class SuccessfulFlowTests {

        @Test
        @DisplayName("Complete workflow: init, edit, add medicine, modify, remove, sign, send")
        void testCompleteSuccessfulFlow() {
            terminal.setHealthNationalService(healthServiceStub);

            // 1. Iniciar revisión
            assertDoesNotThrow(() -> {
                terminal.initRevision(validCIP, "Hipertensión");
            });

            // 2. Añadir valoración médica
            assertDoesNotThrow(() -> {
                terminal.enterMedicalAssessmentInHistory("Paciente mejora");
            });

            // 3. Iniciar edición de prescripción
            assertDoesNotThrow(() -> {
                terminal.initMedicalPrescriptionEdition();
            });

            // 4. Añadir medicamento
            assertDoesNotThrow(() -> {
                terminal.enterMedicineWithGuidelines(medicine1, validGuidelines);
            });

            // 5. Modificar dosis
            assertDoesNotThrow(() -> {
                terminal.modifyDoseInLine(medicine1, 2.0f);
            });

            // 6. Fijar fecha de finalización
            Date futureDate = new Date(System.currentTimeMillis() +
                    (long) 15 * 24 * 60 * 60 * 1000);
            assertDoesNotThrow(() -> {
                terminal.enterTreatmentEndingDate(futureDate);
            });

            // 7. Estampar firma
            assertDoesNotThrow(() -> {
                terminal.stampeeSignature();
            });

            // 8. Enviar
            assertDoesNotThrow(() -> {
                terminal.sendHistoryAndPrescription();
            });
        }
    }
    
// EXCEPCIONES EN initRevision
    @Nested
    @DisplayName("initRevision Exception Cases")
    class InitRevisionExceptionTests {

        @Test
        @DisplayName("initRevision throws ConnectException")
        void testInitRevisionConnectException() {
            terminal.setHealthNationalService(healthServiceMock);
            healthServiceMock.setThrowConnectException(true);

            assertThrows(ConnectException.class, () -> {
                terminal.initRevision(validCIP, "Hipertensión");
            });
        }

        @Test
        @DisplayName("initRevision throws HealthCardIDException")
        void testInitRevisionHealthCardIDException() {
            terminal.setHealthNationalService(healthServiceMock);
            healthServiceMock.setThrowHealthCardIDException(true);

            assertThrows(Exception.class, () -> {
                terminal.initRevision(validCIP, "Hipertensión");
            });
        }
    }

    // EXCEPCIONES EN enterMedicineWithGuidelines
    @Nested
    @DisplayName("enterMedicineWithGuidelines Exception Cases")
    class AddMedicineExceptionTests {

        @Test
        @DisplayName("enterMedicineWithGuidelines throws ProductAlreadyInPrescriptionException")
        void testAddMedicineDuplicate() {
            terminal.setHealthNationalService(healthServiceStub);
            
            assertDoesNotThrow(() -> {
                terminal.initRevision(validCIP, "Hipertensión");
                terminal.initMedicalPrescriptionEdition();
                terminal.enterMedicineWithGuidelines(medicine1, validGuidelines);
            });

            // Intenta añadir de nuevo
            assertThrows(Exception.class, () -> {
                terminal.enterMedicineWithGuidelines(medicine1, validGuidelines);
            });
        }

        @Test
        @DisplayName("enterMedicineWithGuidelines throws IncorrectTakingGuidelinesException")
        void testAddMedicineIncompleteGuidelines() {
            terminal.setHealthNationalService(healthServiceStub);

            String[] incompleteGuidelines = new String[]{"BEFORELUNCH", "15"};

            assertDoesNotThrow(() -> {
                terminal.initRevision(validCIP, "Hipertensión");
                terminal.initMedicalPrescriptionEdition();
            });

            assertThrows(Exception.class, () -> {
                terminal.enterMedicineWithGuidelines(medicine1, incompleteGuidelines);
            });
        }
    }

    // EXCEPCIONES EN modifyDoseInLine
    @Nested
    @DisplayName("modifyDoseInLine Exception Cases")
    class ModifyDoseExceptionTests {

        @Test
        @DisplayName("modifyDoseInLine throws ProductNotInPrescriptionException")
        void testModifyDoseProductNotFound() {
            terminal.setHealthNationalService(healthServiceStub);

            assertDoesNotThrow(() -> {
                terminal.initRevision(validCIP, "Hipertensión");
                terminal.initMedicalPrescriptionEdition();
            });

            ProductID nonExistentProduct = new ProductID("999999999999");

            assertThrows(Exception.class, () -> {
                terminal.modifyDoseInLine(nonExistentProduct, 2.0f);
            });
        }
    }

    // EXCEPCIONES EN removeLine
    @Nested
    @DisplayName("removeLine Exception Cases")
    class RemoveLineExceptionTests {

        @Test
        @DisplayName("removeLine throws ProductNotInPrescriptionException")
        void testRemoveLineProductNotFound() {
            terminal.setHealthNationalService(healthServiceStub);

            assertDoesNotThrow(() -> {
                terminal.initRevision(validCIP, "Hipertensión");
                terminal.initMedicalPrescriptionEdition();
            });

            ProductID nonExistentProduct = new ProductID("999999999999");

            assertThrows(Exception.class, () -> {
                terminal.removeLine(nonExistentProduct);
            });
        }
    }

    // PRECONDICIONES (ProceduralException)
    @Nested
    @DisplayName("Precondition (ProceduralException) Tests")
    class PreconditionTests {

        @Test
        @DisplayName("Calling enterMedicineWithGuidelines before initRevision throws exception")
        void testMedicineWithoutInitRevision() {
            terminal.setHealthNationalService(healthServiceStub);

            assertThrows(Exception.class, () -> {
                terminal.enterMedicineWithGuidelines(medicine1, validGuidelines);
            });
        }

        @Test
        @DisplayName("Calling stampeeSignature before completing edition throws exception")
        void testSignatureBeforeCompletion() {
            terminal.setHealthNationalService(healthServiceStub);

            assertDoesNotThrow(() -> {
                terminal.initRevision(validCIP, "Hipertensión");
            });

            assertThrows(Exception.class, () -> {
                terminal.stampeeSignature();
            });
        }
    }
}
// ===================================================================
    // PRECONDICIONES (ProceduralException - Orden de eventos)
    // ===================================================================

    @Nested
    @DisplayName("Procedural Constraints (Order of Events)")
    class ProcedureConstraintTests {

        @Test
        @DisplayName("enterMedicineWithGuidelines without initRevision throws exception")
        void testMedicineWithoutInitRevision() {
            terminal.setHealthNationalService(healthServiceStub);

            assertThrows(Exception.class, () -> {
                terminal.enterMedicineWithGuidelines(medicine1, validGuidelines);
            });
        }

        @Test
        @DisplayName("modifyDoseInLine without initMedicalPrescriptionEdition throws exception")
        void testModifyWithoutInitEdition() {
            terminal.setHealthNationalService(healthServiceStub);

            assertDoesNotThrow(() -> {
                terminal.initRevision(validCIP, "Hipertensión");
            });

            assertThrows(Exception.class, () -> {
                terminal.modifyDoseInLine(medicine1, 2.0f);
            });
        }

        @Test
        @DisplayName("removeLine without initMedicalPrescriptionEdition throws exception")
        void testRemoveWithoutInitEdition() {
            terminal.setHealthNationalService(healthServiceStub);

            assertDoesNotThrow(() -> {
                terminal.initRevision(validCIP, "Hipertensión");
            });

            assertThrows(Exception.class, () -> {
                terminal.removeLine(medicine1);
            });
        }

        @Test
        @DisplayName("stampeeSignature without completing prescription edition throws exception")
        void testSignatureBeforeCompletion() {
            terminal.setHealthNationalService(healthServiceStub);

            assertDoesNotThrow(() -> {
                terminal.initRevision(validCIP, "Hipertensión");
                terminal.initMedicalPrescriptionEdition();
            });

            assertThrows(Exception.class, () -> {
                terminal.stampeeSignature();
            });
        }

        @Test
        @DisplayName("sendHistoryAndPrescription without stampeeSignature throws exception")
        void testSendWithoutSignature() {
            terminal.setHealthNationalService(healthServiceStub);

            assertDoesNotThrow(() -> {
                terminal.initRevision(validCIP, "Hipertensión");
                terminal.initMedicalPrescriptionEdition();
                terminal.enterMedicineWithGuidelines(medicine1, validGuidelines);
                terminal.enterTreatmentEndingDate(futureDate);
            });

            assertThrows(Exception.class, () -> {
                terminal.sendHistoryAndPrescription();
            });
        }

        @Test
        @DisplayName("enterTreatmentEndingDate without initMedicalPrescriptionEdition throws exception")
        void testEndingDateWithoutInitEdition() {
            terminal.setHealthNationalService(healthServiceStub);

            assertDoesNotThrow(() -> {
                terminal.initRevision(validCIP, "Hipertensión");
            });

            assertThrows(Exception.class, () -> {
                terminal.enterTreatmentEndingDate(futureDate);
            });
        }

        @Test
        @DisplayName("enterMedicalAssessmentInHistory without initRevision throws exception")
        void testAssessmentWithoutInitRevision() {
            terminal.setHealthNationalService(healthServiceStub);

            assertThrows(Exception.class, () -> {
                terminal.enterMedicalAssessmentInHistory("Paciente mejora");
            });
        }
    }

    // ===================================================================
    // EDGE CASES / CASOS ESPECIALES
    // ===================================================================

    @Nested
    @DisplayName("Edge Cases and Special Scenarios")
    class EdgeCasesTests {

        @BeforeEach
        void setUp() {
            terminal.setHealthNationalService(healthServiceStub);
        }

        @Test
        @DisplayName("Add, modify, remove same product in sequence")
        void testAddModifyRemoveSequence() {
            assertDoesNotThrow(() -> {
                terminal.initRevision(validCIP, "Hipertensión");
                terminal.initMedicalPrescriptionEdition();
                
                // Add
                terminal.enterMedicineWithGuidelines(medicine1, validGuidelines);
                
                // Modify
                terminal.modifyDoseInLine(medicine1, 2.0f);
                
                // Remove
                terminal.removeLine(medicine1);
            });
        }

        @Test
        @DisplayName("Modify same dose multiple times")
        void testModifySameDoseMultipleTimes() {
            assertDoesNotThrow(() -> {
                terminal.initRevision(validCIP, "Hipertensión");
                terminal.initMedicalPrescriptionEdition();
                terminal.enterMedicineWithGuidelines(medicine1, validGuidelines);
                
                terminal.modifyDoseInLine(medicine1, 2.0f);
                terminal.modifyDoseInLine(medicine1, 3.0f);
                terminal.modifyDoseInLine(medicine1, 4.0f);
            });
        }

        @Test
        @DisplayName("Flow with multiple medications")
        void testMultipleMedicationsFlow() {
            assertDoesNotThrow(() -> {
                terminal.initRevision(validCIP, "Hipertensión");
                terminal.initMedicalPrescriptionEdition();
                
                terminal.enterMedicineWithGuidelines(medicine1, validGuidelines);
                terminal.enterMedicineWithGuidelines(medicine2, validGuidelines);
                
                terminal.modifyDoseInLine(medicine1, 2.0f);
                terminal.modifyDoseInLine(medicine2, 3.0f);
                
                terminal.removeLine(medicine1);
                
                terminal.enterTreatmentEndingDate(futureDate);
                terminal.stampeeSignature();
                terminal.sendHistoryAndPrescription();
            });
        }

        @Test
        @DisplayName("Change doctor during revision")
        void testChangeDoctorDuringRevision() {
            assertDoesNotThrow(() -> {
                terminal.initRevision(validCIP, "Hipertensión");
                terminal.enterMedicalAssessmentInHistory("Paciente estable");
                
                terminal.initMedicalPrescriptionEdition();
                terminal.enterMedicineWithGuidelines(medicine1, validGuidelines);
                
                terminal.enterTreatmentEndingDate(futureDate);
                terminal.stampeeSignature();
                terminal.sendHistoryAndPrescription();
            });
        }

        @Test
        @DisplayName("Multiple assessment annotations")
        void testMultipleAssessmentAnnotations() {
            assertDoesNotThrow(() -> {
                terminal.initRevision(validCIP, "Hipertensión");
                
                terminal.enterMedicalAssessmentInHistory("Paciente inicial estable");
                terminal.enterMedicalAssessmentInHistory("Presión arterial normal");
                terminal.enterMedicalAssessmentInHistory("Sin síntomas");
                
                terminal.initMedicalPrescriptionEdition();
                terminal.enterMedicineWithGuidelines(medicine1, validGuidelines);
                terminal.enterTreatmentEndingDate(futureDate);
                terminal.stampeeSignature();
                terminal.sendHistoryAndPrescription();
            });
        }

        @Test
        @DisplayName("Add medicine, remove it, add different medicine")
        void testAddRemoveAddDifferent() {
            assertDoesNotThrow(() -> {
                terminal.initRevision(validCIP, "Hipertensión");
                terminal.initMedicalPrescriptionEdition();
                
                terminal.enterMedicineWithGuidelines(medicine1, validGuidelines);
                terminal.removeLine(medicine1);
                terminal.enterMedicineWithGuidelines(medicine2, validGuidelines);
                
                terminal.enterTreatmentEndingDate(futureDate);
                terminal.stampeeSignature();
                terminal.sendHistoryAndPrescription();
            });
        }

        @Test
        @DisplayName("Different illness types")
        void testDifferentIllnessTypes() {
            String[] illnesses = {"Hipertensión", "Diabetes", "Asma", "Artritis"};
            
            for (String illness : illnesses) {
                terminal = new ConsultationTerminal();
                terminal.setHealthNationalService(healthServiceStub);
                
                assertDoesNotThrow(() -> {
                    terminal.initRevision(validCIP, illness);
                    terminal.initMedicalPrescriptionEdition();
                    terminal.enterMedicineWithGuidelines(medicine1, validGuidelines);
                    terminal.enterTreatmentEndingDate(futureDate);
                    terminal.stampeeSignature();
                    terminal.sendHistoryAndPrescription();
                });
            }
        }

        @Test
        @DisplayName("Different dosage levels")
        void testDifferentDosagelLevels() {
            float[] dosages = {0.5f, 1.0f, 2.5f, 5.0f, 10.0f};
            
            for (float dose : dosages) {
                terminal = new ConsultationTerminal();
                terminal.setHealthNationalService(healthServiceStub);
                ProductID tempMedicine = new ProductID(String.format("%012d", (int)dose * 1000));
                
                assertDoesNotThrow(() -> {
                    terminal.initRevision(validCIP, "Hipertensión");
                    terminal.initMedicalPrescriptionEdition();
                    terminal.enterMedicineWithGuidelines(tempMedicine, validGuidelines);
                    terminal.modifyDoseInLine(tempMedicine, dose);
                    terminal.enterTreatmentEndingDate(futureDate);
                    terminal.stampeeSignature();
                    terminal.sendHistoryAndPrescription();
                });
            }
        }
    }

    // ===================================================================
    // AI WORKFLOW TESTS (Opcional - si implementas IA)
    // ===================================================================

    @Nested
    @DisplayName("AI Workflow Tests")
    class AIWorkflowTests {

        private DecisionMakingAIStub aiStub;
        private DecisionMakingAIMock aiMock;

        @BeforeEach
        void setUp() {
            terminal.setHealthNationalService(healthServiceStub);
            aiStub = new DecisionMakingAIStub();
            aiMock = new DecisionMakingAIMock();
        }

        @Test
        @DisplayName("callDecisionMakingAI initializes successfully")
        void testCallAISuccess() {
            terminal.setDecisionMakingAI(aiStub);
            
            assertDoesNotThrow(() -> {
                terminal.callDecisionMakingAI();
            });
        }

        @Test
        @DisplayName("callDecisionMakingAI throws AIException")
        void testCallAIException() {
            terminal.setDecisionMakingAI(aiMock);
            aiMock.setThrowAIException(true);
            
            assertThrows(Exception.class, () -> {
                terminal.callDecisionMakingAI();
            });
        }

        @Test
        @DisplayName("askAIForSuggest returns suggestions")
        void testAskAIForSuggestSuccess() {
            terminal.setDecisionMakingAI(aiStub);
            
            assertDoesNotThrow(() -> {
                terminal.callDecisionMakingAI();
                terminal.askAIForSuggest("What medication do you recommend for hypertension?");
            });
        }

        @Test
        @DisplayName("askAIForSuggest throws BadPromptException")
        void testAskAIForSuggestBadPrompt() {
            terminal.setDecisionMakingAI(aiMock);
            aiMock.setThrowBadPromptException(true);
            
            assertDoesNotThrow(() -> {
                terminal.callDecisionMakingAI();
            });
            
            assertThrows(Exception.class, () -> {
                terminal.askAIForSuggest("???");
            });
        }

        @Test
        @DisplayName("extractGuidelinesFromSugg parses suggestions")
        void testExtractGuidelinesSuccess() {
            terminal.setDecisionMakingAI(aiStub);
            
            assertDoesNotThrow(() -> {
                terminal.callDecisionMakingAI();
                terminal.askAIForSuggest("What medication do you recommend?");
                terminal.extractGuidelinesFromSugg();
            });
        }

        @Test
        @DisplayName("AI suggestions applied to prescription")
        void testAISuggestionsAppliedToPrescription() {
            terminal.setHealthNationalService(healthServiceStub);
            terminal.setDecisionMakingAI(aiStub);
            
            assertDoesNotThrow(() -> {
                terminal.initRevision(validCIP, "Hipertensión");
                terminal.initMedicalPrescriptionEdition();
                
                // Llamar IA y obtener sugerencias
                terminal.callDecisionMakingAI();
                terminal.askAIForSuggest("What medication for hypertension?");
                terminal.extractGuidelinesFromSugg();
                
                // Completar flujo
                terminal.enterTreatmentEndingDate(futureDate);
                terminal.stampeeSignature();
                terminal.sendHistoryAndPrescription();
            });
        }

        @Test
        @DisplayName("Manual changes combined with AI suggestions")
        void testManualAndAIChanges() {
            terminal.setHealthNationalService(healthServiceStub);
            terminal.setDecisionMakingAI(aiStub);
            
            assertDoesNotThrow(() -> {
                terminal.initRevision(validCIP, "Hipertensión");
                terminal.initMedicalPrescriptionEdition();
                
                // Cambios manuales
                terminal.enterMedicineWithGuidelines(medicine1, validGuidelines);
                
                // Sugerencias de IA
                terminal.callDecisionMakingAI();
                terminal.askAIForSuggest("Improve the prescription");
                terminal.extractGuidelinesFromSugg();
                
                // Más cambios manuales
                terminal.modifyDoseInLine(medicine1, 2.0f);
                
                terminal.enterTreatmentEndingDate(futureDate);
                terminal.stampeeSignature();
                terminal.sendHistoryAndPrescription();
            });
        }
    }

    // ===================================================================
    // COMPREHENSIVE INTEGRATION TESTS
    // ===================================================================

    @Nested
    @DisplayName("Comprehensive Integration Tests")
    class IntegrationTests {

        @BeforeEach
        void setUp() {
            terminal.setHealthNationalService(healthServiceStub);
        }

        @Test
        @DisplayName("Full workflow with all operations")
        void testFullWorkflowAllOperations() {
            assertDoesNotThrow(() -> {
                // Init
                terminal.initRevision(validCIP, "Hipertensión");
                terminal.enterMedicalAssessmentInHistory("Evaluación inicial completa");
                
                // Edit prescription
                terminal.initMedicalPrescriptionEdition();
                terminal.enterMedicineWithGuidelines(medicine1, validGuidelines);
                terminal.enterMedicineWithGuidelines(medicine2, validGuidelines);
                
                // Modify and remove
                terminal.modifyDoseInLine(medicine1, 2.0f);
                terminal.removeLine(medicine2);
                
                // Complete and send
                terminal.enterTreatmentEndingDate(futureDate);
                terminal.stampeeSignature();
                MedicalPrescription result = terminal.sendHistoryAndPrescription();
                
                assertNotNull(result);
            });
        }

        @Test
        @DisplayName("Workflow recovery after exceptions")
        void testWorkflowRecoveryAfterExceptions() {
            assertDoesNotThrow(() -> {
                terminal.initRevision(validCIP, "Hipertensión");
                terminal.initMedicalPrescriptionEdition();
                
                // Intenta hacer algo inválido
                assertThrows(Exception.class, () -> {
                    terminal.enterMedicineWithGuidelines(medicine1, new String[]{"INVALID"});
                });
                
                // Continúa normalmente
                terminal.enterMedicineWithGuidelines(medicine1, validGuidelines);
                terminal.enterTreatmentEndingDate(futureDate);
                terminal.stampeeSignature();
                terminal.sendHistoryAndPrescription();
            });
        }
    }