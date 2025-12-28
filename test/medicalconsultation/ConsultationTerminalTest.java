package medicalconsultation;

import consultamedica.ConsultationTerminal;
import consultamedica.MedicalPrescription;
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
    private HealthNationalServiceStub healthServiceStub;
    private HealthNationalServiceMock healthServiceMock;
    private HealthCardID validCIP;
    private ProductID medicine1;
    private ProductID medicine2;
    private String[] validGuidelines;
    private Date futureDate;

    @BeforeEach
    void setUp() throws InvalidProductIDException, InvalidHealthCardIDException {
        terminal = new ConsultationTerminal();
        healthServiceStub = new HealthNationalServiceStub();
        healthServiceMock = new HealthNationalServiceMock();

        validCIP = new HealthCardID("1234567890ABCDEF");
        medicine1 = new ProductID("123456789012");
        medicine2 = new ProductID("987654321098");

        validGuidelines = new String[]{
                "BEFORELUNCH", "15", "1", "1", "DAY", "Con agua", ""
        };

        // Fecha futura (15 días)
        futureDate = new Date(System.currentTimeMillis() +
                (long) 15 * 24 * 60 * 60 * 1000);
    }

    // ===================================================================
    // FLUJO EXITOSO COMPLETO
    // ===================================================================

    @Nested
    @DisplayName("Successful Complete Flow")
    class SuccessfulFlowTests {

        @Test
        @DisplayName("Complete workflow: init, edit, add medicine, modify, sign, send")
        void testCompleteSuccessfulFlow() {
            terminal.setHealthNationalService(healthServiceStub);

            assertDoesNotThrow(() -> {
                // 1. Iniciar revisión
                terminal.initRevision(validCIP, "Hipertensión");

                // 2. Añadir valoración médica
                terminal.enterMedicalAssessmentInHistory("Paciente mejora");

                // 3. Iniciar edición de prescripción
                terminal.initMedicalPrescriptionEdition();

                // 4. Añadir medicamento
                terminal.enterMedicineWithGuidelines(medicine1, validGuidelines);

                // 5. Modificar dosis
                terminal.modifyDoseInLine(medicine1, 2.0f);

                // 6. Fijar fecha de finalización
                terminal.enterTreatmentEndingDate(futureDate);

                // 7. Finalizar edición
                terminal.finishMedicalPrescriptionEdition();

                // 8. Estampar firma
                terminal.stampeeSignature();

                // 9. Enviar
                MedicalPrescription result = terminal.sendHistoryAndPrescription();
                assertNotNull(result);
            });
        }
    }

    // ===================================================================
    // EXCEPCIONES EN initRevision
    // ===================================================================

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

        @Test
        @DisplayName("initRevision throws AnyCurrentPrescriptionException")
        void testInitRevisionAnyCurrentPrescriptionException() {
            terminal.setHealthNationalService(healthServiceMock);
            healthServiceMock.setThrowAnyCurrentPrescriptionException(true);

            assertThrows(Exception.class, () -> {
                terminal.initRevision(validCIP, "Hipertensión");
            });
        }
    }

    // ===================================================================
    // EXCEPCIONES EN enterMedicineWithGuidelines
    // ===================================================================

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

            // Intenta añadir de nuevo el mismo medicamento
            assertThrows(Exception.class, () -> {
                terminal.enterMedicineWithGuidelines(medicine1, validGuidelines);
            });
        }

        @Test
        @DisplayName("enterMedicineWithGuidelines throws IncorrectTakingGuidelinesException (array incompleto)")
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

        @Test
        @DisplayName("enterMedicineWithGuidelines throws IncorrectTakingGuidelinesException (valores inválidos)")
        void testAddMedicineInvalidGuidelines() {
            terminal.setHealthNationalService(healthServiceStub);

            String[] invalidGuidelines = new String[]{
                    "INVALID_MOMENT", "15", "1", "1", "DAY", "Con agua", ""
            };

            assertDoesNotThrow(() -> {
                terminal.initRevision(validCIP, "Hipertensión");
                terminal.initMedicalPrescriptionEdition();
            });

            assertThrows(Exception.class, () -> {
                terminal.enterMedicineWithGuidelines(medicine1, invalidGuidelines);
            });
        }
    }

    // ===================================================================
    // EXCEPCIONES EN modifyDoseInLine
    // ===================================================================

    @Nested
    @DisplayName("modifyDoseInLine Exception Cases")
    class ModifyDoseExceptionTests {

        @Test
        @DisplayName("modifyDoseInLine throws ProductNotInPrescriptionException")
        void testModifyDoseProductNotFound() throws InvalidProductIDException {
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

    // ===================================================================
    // EXCEPCIONES EN removeLine
    // ===================================================================

    @Nested
    @DisplayName("removeLine Exception Cases")
    class RemoveLineExceptionTests {

        @Test
        @DisplayName("removeLine throws ProductNotInPrescriptionException")
        void testRemoveLineProductNotFound() throws InvalidProductIDException {
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

    // ===================================================================
    // EXCEPCIONES EN enterTreatmentEndingDate
    // ===================================================================

    @Nested
    @DisplayName("enterTreatmentEndingDate Exception Cases")
    class EnterTreatmentEndingDateTests {

        @Test
        @DisplayName("enterTreatmentEndingDate throws IncorrectEndingDateException (fecha pasada)")
        void testEndingDateInPast() {
            terminal.setHealthNationalService(healthServiceStub);

            Date pastDate = new Date(System.currentTimeMillis() -
                    (long) 10 * 24 * 60 * 60 * 1000);

            assertDoesNotThrow(() -> {
                terminal.initRevision(validCIP, "Hipertensión");
                terminal.initMedicalPrescriptionEdition();
                terminal.enterMedicineWithGuidelines(medicine1, validGuidelines);
            });

            assertThrows(Exception.class, () -> {
                terminal.enterTreatmentEndingDate(pastDate);
            });
        }

        @Test
        @DisplayName("enterTreatmentEndingDate throws IncorrectEndingDateException (fecha muy cercana)")
        void testEndingDateTooSoon() {
            terminal.setHealthNationalService(healthServiceStub);

            // Fecha en 1 hora
            Date tooSoonDate = new Date(System.currentTimeMillis() + 3600000);

            assertDoesNotThrow(() -> {
                terminal.initRevision(validCIP, "Hipertensión");
                terminal.initMedicalPrescriptionEdition();
                terminal.enterMedicineWithGuidelines(medicine1, validGuidelines);
            });

            assertThrows(Exception.class, () -> {
                terminal.enterTreatmentEndingDate(tooSoonDate);
            });
        }
    }

    // ===================================================================
    // PRECONDICIONES (ProceduralException - Orden de eventos)
    // ===================================================================

    @Nested
    @DisplayName("Procedural Constraints (Order of Events)")
    class ProcedureConstraintTests {

        @Test
        @DisplayName("enterMedicineWithGuidelines without initRevision throws ProceduralException")
        void testMedicineWithoutInitRevision() {
            terminal.setHealthNationalService(healthServiceStub);

            assertThrows(Exception.class, () -> {
                terminal.enterMedicineWithGuidelines(medicine1, validGuidelines);
            });
        }

        @Test
        @DisplayName("modifyDoseInLine without initMedicalPrescriptionEdition throws ProceduralException")
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
        @DisplayName("removeLine without initMedicalPrescriptionEdition throws ProceduralException")
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
        @DisplayName("stampeeSignature without finishing prescription edition throws ProceduralException")
        void testSignatureBeforeFinishEdition() {
            terminal.setHealthNationalService(healthServiceStub);

            assertDoesNotThrow(() -> {
                terminal.initRevision(validCIP, "Hipertensión");
                terminal.initMedicalPrescriptionEdition();
                terminal.enterMedicineWithGuidelines(medicine1, validGuidelines);
                terminal.enterTreatmentEndingDate(futureDate);
            });

            assertThrows(Exception.class, () -> {
                terminal.stampeeSignature();
            });
        }

        @Test
        @DisplayName("sendHistoryAndPrescription without stampeeSignature throws ProceduralException")
        void testSendWithoutSignature() {
            terminal.setHealthNationalService(healthServiceStub);

            assertDoesNotThrow(() -> {
                terminal.initRevision(validCIP, "Hipertensión");
                terminal.initMedicalPrescriptionEdition();
                terminal.enterMedicineWithGuidelines(medicine1, validGuidelines);
                terminal.enterTreatmentEndingDate(futureDate);
                terminal.finishMedicalPrescriptionEdition();
            });

            assertThrows(Exception.class, () -> {
                terminal.sendHistoryAndPrescription();
            });
        }

        @Test
        @DisplayName("enterTreatmentEndingDate without initMedicalPrescriptionEdition throws ProceduralException")
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
        @DisplayName("enterMedicalAssessmentInHistory without initRevision throws ProceduralException")
        void testAssessmentWithoutInitRevision() {
            terminal.setHealthNationalService(healthServiceStub);

            assertThrows(Exception.class, () -> {
                terminal.enterMedicalAssessmentInHistory("Paciente mejora");
            });
        }

        @Test
        @DisplayName("initMedicalPrescriptionEdition without initRevision throws ProceduralException")
        void testInitEditionWithoutRevision() {
            terminal.setHealthNationalService(healthServiceStub);

            assertThrows(Exception.class, () -> {
                terminal.initMedicalPrescriptionEdition();
            });
        }
    }

    // ===================================================================
    // EDGE CASES / CASOS ESPECIALES
    // ===================================================================

    @Nested
    @DisplayName("Edge Cases and Special Scenarios")
    class EdgeCasesTests {

        @Test
        @DisplayName("Add, modify, remove same product in sequence")
        void testAddModifyRemoveSequence() {
            terminal.setHealthNationalService(healthServiceStub);

            assertDoesNotThrow(() -> {
                terminal.initRevision(validCIP, "Hipertensión");
                terminal.initMedicalPrescriptionEdition();

                // Add
                terminal.enterMedicineWithGuidelines(medicine1, validGuidelines);

                // Modify
                terminal.modifyDoseInLine(medicine1, 2.0f);

                // Remove (ANTES de finish)
                terminal.removeLine(medicine1);

                // Añadir otro para completar (no puede estar vacía)
                terminal.enterMedicineWithGuidelines(medicine2, validGuidelines);

                terminal.enterTreatmentEndingDate(futureDate);
                terminal.finishMedicalPrescriptionEdition();
                terminal.stampeeSignature();
                terminal.sendHistoryAndPrescription();
            });
        }

        @Test
        @DisplayName("Modify same dose multiple times")
        void testModifySameDoseMultipleTimes() {
            terminal.setHealthNationalService(healthServiceStub);

            assertDoesNotThrow(() -> {
                terminal.initRevision(validCIP, "Hipertensión");
                terminal.initMedicalPrescriptionEdition();
                terminal.enterMedicineWithGuidelines(medicine1, validGuidelines);

                terminal.modifyDoseInLine(medicine1, 2.0f);
                terminal.modifyDoseInLine(medicine1, 3.0f);
                terminal.modifyDoseInLine(medicine1, 4.0f);

                terminal.enterTreatmentEndingDate(futureDate);
                terminal.finishMedicalPrescriptionEdition();
                terminal.stampeeSignature();
                terminal.sendHistoryAndPrescription();
            });
        }

        @Test
        @DisplayName("Flow with multiple medications")
        void testMultipleMedicationsFlow() {
            terminal.setHealthNationalService(healthServiceStub);

            assertDoesNotThrow(() -> {
                terminal.initRevision(validCIP, "Hipertensión");
                terminal.initMedicalPrescriptionEdition();

                terminal.enterMedicineWithGuidelines(medicine1, validGuidelines);
                terminal.enterMedicineWithGuidelines(medicine2, validGuidelines);

                terminal.modifyDoseInLine(medicine1, 2.0f);
                terminal.modifyDoseInLine(medicine2, 3.0f);

                // Remove ANTES de finish
                terminal.removeLine(medicine1);

                terminal.enterTreatmentEndingDate(futureDate);
                terminal.finishMedicalPrescriptionEdition();
                terminal.stampeeSignature();
                terminal.sendHistoryAndPrescription();
            });
        }

        @Test
        @DisplayName("Multiple assessment annotations")
        void testMultipleAssessmentAnnotations() {
            terminal.setHealthNationalService(healthServiceStub);

            assertDoesNotThrow(() -> {
                terminal.initRevision(validCIP, "Hipertensión");

                terminal.enterMedicalAssessmentInHistory("Paciente inicial estable");
                terminal.enterMedicalAssessmentInHistory("Presión arterial normal");
                terminal.enterMedicalAssessmentInHistory("Sin síntomas");

                terminal.initMedicalPrescriptionEdition();
                terminal.enterMedicineWithGuidelines(medicine1, validGuidelines);
                terminal.enterTreatmentEndingDate(futureDate);
                terminal.finishMedicalPrescriptionEdition();
                terminal.stampeeSignature();
                terminal.sendHistoryAndPrescription();
            });
        }

        @Test
        @DisplayName("Add medicine, remove it, add different medicine")
        void testAddRemoveAddDifferent() {
            terminal.setHealthNationalService(healthServiceStub);

            assertDoesNotThrow(() -> {
                terminal.initRevision(validCIP, "Hipertensión");
                terminal.initMedicalPrescriptionEdition();

                terminal.enterMedicineWithGuidelines(medicine1, validGuidelines);
                // Remove ANTES de finish
                terminal.removeLine(medicine1);
                terminal.enterMedicineWithGuidelines(medicine2, validGuidelines);

                terminal.enterTreatmentEndingDate(futureDate);
                terminal.finishMedicalPrescriptionEdition();
                terminal.stampeeSignature();
                terminal.sendHistoryAndPrescription();
            });
        }

        @Test
        @DisplayName("Different illness types")
        void testDifferentIllnessTypes() throws InvalidHealthCardIDException, InvalidProductIDException {
            String[] illnesses = {"Hipertensión", "Diabetes", "Asma", "Artritis"};

            for (String illness : illnesses) {
                ConsultationTerminal newTerminal = new ConsultationTerminal();
                newTerminal.setHealthNationalService(healthServiceStub);

                HealthCardID cip = new HealthCardID("1234567890ABCDEF");
                ProductID med = new ProductID("123456789012");
                Date future = new Date(System.currentTimeMillis() +
                        (long) 15 * 24 * 60 * 60 * 1000);

                assertDoesNotThrow(() -> {
                    newTerminal.initRevision(cip, illness);
                    newTerminal.initMedicalPrescriptionEdition();
                    newTerminal.enterMedicineWithGuidelines(med, validGuidelines);
                    newTerminal.enterTreatmentEndingDate(future);
                    newTerminal.finishMedicalPrescriptionEdition();
                    newTerminal.stampeeSignature();
                    newTerminal.sendHistoryAndPrescription();
                });
            }
        }

        @Test
        @DisplayName("Different dosage levels")
        void testDifferentDosageLevels() throws InvalidProductIDException, InvalidHealthCardIDException {
            float[] dosages = {0.5f, 1.0f, 2.5f, 5.0f, 10.0f};

            for (float dose : dosages) {
                ConsultationTerminal newTerminal = new ConsultationTerminal();
                newTerminal.setHealthNationalService(healthServiceStub);

                // Generar ProductID único
                String code = String.format("%012d", (int)(dose * 100000));
                ProductID tempMedicine = new ProductID(code);

                HealthCardID cip = new HealthCardID("1234567890ABCDEF");
                Date future = new Date(System.currentTimeMillis() +
                        (long) 15 * 24 * 60 * 60 * 1000);

                assertDoesNotThrow(() -> {
                    newTerminal.initRevision(cip, "Hipertensión");
                    newTerminal.initMedicalPrescriptionEdition();
                    newTerminal.enterMedicineWithGuidelines(tempMedicine, validGuidelines);
                    newTerminal.modifyDoseInLine(tempMedicine, dose);
                    newTerminal.enterTreatmentEndingDate(future);
                    newTerminal.finishMedicalPrescriptionEdition();
                    newTerminal.stampeeSignature();
                    newTerminal.sendHistoryAndPrescription();
                });
            }
        }
    }

    // ===================================================================
    // AI WORKFLOW TESTS
    // ===================================================================

    @Nested
    @DisplayName("AI Workflow Tests")
    class AIWorkflowTests {

        private DecisionMakingAIStub aiStub;
        private DecisionMakingAIMock aiMock;

        @BeforeEach
        void setUpAI() {
            aiStub = new DecisionMakingAIStub();
            aiMock = new DecisionMakingAIMock();
        }

        @Test
        @DisplayName("callDecisionMakingAI initializes successfully")
        void testCallAISuccess() {
            terminal.setHealthNationalService(healthServiceStub);
            terminal.setDecisionMakingAI(aiStub);

            assertDoesNotThrow(() -> {
                terminal.initRevision(validCIP, "Hipertensión");
                terminal.callDecisionMakingAI();
            });
        }

        @Test
        @DisplayName("callDecisionMakingAI throws AIException")
        void testCallAIException() {
            terminal.setHealthNationalService(healthServiceStub);
            terminal.setDecisionMakingAI(aiMock);
            aiMock.setThrowAIException(true);

            assertDoesNotThrow(() -> {
                terminal.initRevision(validCIP, "Hipertensión");
            });

            assertThrows(Exception.class, () -> {
                terminal.callDecisionMakingAI();
            });
        }

        @Test
        @DisplayName("askAIForSuggest returns suggestions")
        void testAskAIForSuggestSuccess() {
            terminal.setHealthNationalService(healthServiceStub);
            terminal.setDecisionMakingAI(aiStub);

            assertDoesNotThrow(() -> {
                terminal.initRevision(validCIP, "Hipertensión");
                terminal.callDecisionMakingAI();
                String response = terminal.askAIForSuggest("What medication do you recommend for hypertension?");
                assertNotNull(response);
            });
        }

        @Test
        @DisplayName("askAIForSuggest throws BadPromptException")
        void testAskAIForSuggestBadPrompt() {
            terminal.setHealthNationalService(healthServiceStub);
            terminal.setDecisionMakingAI(aiMock);
            aiMock.setThrowBadPromptException(true);

            assertDoesNotThrow(() -> {
                terminal.initRevision(validCIP, "Hipertensión");
                terminal.callDecisionMakingAI();
            });

            assertThrows(Exception.class, () -> {
                terminal.askAIForSuggest("???");
            });
        }

        @Test
        @DisplayName("extractGuidelinesFromSugg parses suggestions")
        void testExtractGuidelinesSuccess() {
            terminal.setHealthNationalService(healthServiceStub);
            terminal.setDecisionMakingAI(aiStub);

            assertDoesNotThrow(() -> {
                terminal.initRevision(validCIP, "Hipertensión");
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
                terminal.finishMedicalPrescriptionEdition();
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
                terminal.finishMedicalPrescriptionEdition();
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

        @Test
        @DisplayName("Full workflow with all operations")
        void testFullWorkflowAllOperations() {
            terminal.setHealthNationalService(healthServiceStub);

            assertDoesNotThrow(() -> {
                // Init
                terminal.initRevision(validCIP, "Hipertensión");
                terminal.enterMedicalAssessmentInHistory("Evaluación inicial completa");

                // Edit prescription
                terminal.initMedicalPrescriptionEdition();
                terminal.enterMedicineWithGuidelines(medicine1, validGuidelines);
                terminal.enterMedicineWithGuidelines(medicine2, validGuidelines);

                // Modify and remove (ANTES de finish)
                terminal.modifyDoseInLine(medicine1, 2.0f);
                terminal.removeLine(medicine2);

                // Complete and send
                terminal.enterTreatmentEndingDate(futureDate);
                terminal.finishMedicalPrescriptionEdition();
                terminal.stampeeSignature();
                MedicalPrescription result = terminal.sendHistoryAndPrescription();

                assertNotNull(result);
            });
        }

        @Test
        @DisplayName("Workflow recovery after exceptions")
        void testWorkflowRecoveryAfterExceptions() {
            terminal.setHealthNationalService(healthServiceStub);

            assertDoesNotThrow(() -> {
                terminal.initRevision(validCIP, "Hipertensión");
                terminal.initMedicalPrescriptionEdition();

                // Intenta hacer algo inválido
                try {
                    terminal.enterMedicineWithGuidelines(medicine1, new String[]{"INVALID"});
                    fail("Debería haber lanzado excepción");
                } catch (Exception e) {
                    // Excepción esperada
                }

                // Continúa normalmente después del error
                terminal.enterMedicineWithGuidelines(medicine1, validGuidelines);
                terminal.enterTreatmentEndingDate(futureDate);
                terminal.finishMedicalPrescriptionEdition();
                terminal.stampeeSignature();
                terminal.sendHistoryAndPrescription();
            });
        }
    }
}