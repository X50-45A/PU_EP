package main.java.consultamedica;


import main.java.data.DigitalSignature;
import main.java.data.HealthCardID;
import main.java.data.ProductID;
import main.java.services.DecisionMakingAI;
import main.java.services.HealthNationalService;
import main.java.services.Suggestion;

import java.net.ConnectException;
import java.util.*;

public class ConsultationTerminal {
    private HealthNationalService hns;
    private DecisionMakingAI ai;

    // Estado del caso de uso
    private HealthCardID currentCip;
    private String currentIllness;
    private MedicalHistory currentHistory;
    private MedicalPrescription currentPrescription;
    private boolean revisionInitiated;
    private boolean prescriptionEditionStarted;
    private boolean prescriptionEditionFinished;
    private boolean signatureStamped;
    private String lastAIResponse;

    public ConsultationTerminal() {
        this.revisionInitiated = false;
        this.prescriptionEditionStarted = false;
        this.prescriptionEditionFinished = false;
        this.signatureStamped = false;
    }

    // Setters para inyección de dependencias
    public void setHealthNationalService(HealthNationalService hns) {
        this.hns = hns;
    }

    public void setDecisionMakingAI(DecisionMakingAI ai) {
        this.ai = ai;
    }

    // ============= EVENTOS DE ENTRADA =============

    public void initRevision(HealthCardID cip, String illness)
            throws ConnectException, HealthCardIDException,
            AnyCurrentPrescriptionException {
        if (cip == null || illness == null || illness.trim().isEmpty()) {
            throw new IllegalArgumentException("Parámetros inválidos");
        }

        // Descargar historia clínica y prescripción médica del SNS
        currentHistory = hns.getMedicalHistory(cip);
        currentPrescription = hns.getMedicalPrescription(cip, illness);

        currentCip = cip;
        currentIllness = illness;
        revisionInitiated = true;
        prescriptionEditionStarted = false;
        prescriptionEditionFinished = false;
        signatureStamped = false;
    }

    public void enterMedicalAssessmentInHistory(String assess)
            throws ProceduralException {
        if (!revisionInitiated) {
            throw new ProceduralException("Debe iniciar la revisión primero");
        }

        currentHistory.addMedicalHistoryAnnotations(assess);
    }

    public void initMedicalPrescriptionEdition() throws ProceduralException {
        if (!revisionInitiated) {
            throw new ProceduralException("Debe iniciar la revisión primero");
        }

        prescriptionEditionStarted = true;
        prescriptionEditionFinished = false;
    }

    public void enterMedicineWithGuidelines(ProductID prodID, String[] instruc)
            throws ProductAlreadyInPrescriptionException,
            IncorrectTakingGuidelinesException, ProceduralException {
        if (!prescriptionEditionStarted) {
            throw new ProceduralException(
                    "Debe iniciar la edición de la prescripción primero");
        }
        if (prescriptionEditionFinished) {
            throw new ProceduralException(
                    "La edición de la prescripción ya ha finalizado");
        }

        createMedPrescriptionLine(prodID, instruc);
    }

    public void modifyDoseInLine(ProductID prodID, float newDose)
            throws ProductNotInPrescriptionException, ProceduralException {
        if (!prescriptionEditionStarted) {
            throw new ProceduralException(
                    "Debe iniciar la edición de la prescripción primero");
        }
        if (prescriptionEditionFinished) {
            throw new ProceduralException(
                    "La edición de la prescripción ya ha finalizado");
        }

        currentPrescription.modifyDoseInLine(prodID, newDose);
    }

    public void removeLine(ProductID prodID)
            throws ProductNotInPrescriptionException, ProceduralException {
        if (!prescriptionEditionStarted) {
            throw new ProceduralException(
                    "Debe iniciar la edición de la prescripción primero");
        }
        if (prescriptionEditionFinished) {
            throw new ProceduralException(
                    "La edición de la prescripción ya ha finalizado");
        }

        currentPrescription.removeLine(prodID.getCode());
    }

    public void enterTreatmentEndingDate(Date date)
            throws IncorrectEndingDateException, ProceduralException {
        if (!prescriptionEditionStarted) {
            throw new ProceduralException(
                    "Debe iniciar la edición de la prescripción primero");
        }
        if (prescriptionEditionFinished) {
            throw new ProceduralException(
                    "La edición de la prescripción ya ha finalizado");
        }

        Date now = new Date();
        if (date == null || date.before(now)) {
            throw new IncorrectEndingDateException(
                    "La fecha debe ser futura");
        }

        // Verificar que no sea demasiado cercana (al menos 1 día de diferencia)
        long diff = date.getTime() - now.getTime();
        long dayInMillis = 24 * 60 * 60 * 1000;
        if (diff < dayInMillis) {
            throw new IncorrectEndingDateException(
                    "La fecha debe ser al menos un día en el futuro");
        }

        currentPrescription.setEndDate(date);
    }

    public void finishMedicalPrescriptionEdition() throws ProceduralException {
        if (!prescriptionEditionStarted) {
            throw new ProceduralException(
                    "Debe iniciar la edición de la prescripción primero");
        }

        prescriptionEditionFinished = true;
    }

    public void stampeeSignature() throws eSignatureException, ProceduralException {
        if (!prescriptionEditionFinished) {
            throw new ProceduralException(
                    "Debe finalizar la edición de la prescripción primero");
        }
        if (signatureStamped) {
            throw new ProceduralException("La firma ya ha sido estampada");
        }

        try {
            // Simular la generación de firma digital
            byte[] signature = new byte[256];
            new Random().nextBytes(signature);
            DigitalSignature ds = new DigitalSignature(signature);

            currentPrescription.seteSign(ds);
            setPrescDateAndEndDate(currentPrescription.getEndDate());

            signatureStamped = true;
        } catch (Exception e) {
            throw new eSignatureException("Error al estampar la firma: " + e.getMessage());
        }
    }

    public MedicalPrescription sendHistoryAndPrescription()
            throws ConnectException, HealthCardIDException,
            AnyCurrentPrescriptionException,
            NotCompletedMedicalPrescription, ProceduralException {
        if (!signatureStamped) {
            throw new ProceduralException("Debe estampar la firma primero");
        }

        MedicalPrescription updatedPresc = hns.sendHistoryAndPrescription(
                currentCip, currentHistory, currentIllness, currentPrescription);

        currentPrescription = updatedPresc;
        return updatedPresc;
    }

    public void printMedicalPrescrip() throws printingException {
        // No se pide implementar
    }

    // ============= EVENTOS DE IA =============

    public void callDecisionMakingAI() throws AIException, ProceduralException {
        if (!revisionInitiated) {
            throw new ProceduralException("Debe iniciar la revisión primero");
        }

        ai.initDecisionMakingAI();
    }

    public String askAIForSuggest(String prompt)
            throws BadPromptException, ProceduralException {
        if (!revisionInitiated) {
            throw new ProceduralException("Debe iniciar la revisión primero");
        }

        lastAIResponse = ai.getSuggestions(prompt);
        return lastAIResponse;
    }

    public List<Suggestion> extractGuidelinesFromSugg() throws ProceduralException {
        if (lastAIResponse == null) {
            throw new ProceduralException(
                    "Debe obtener sugerencias de la IA primero");
        }

        return ai.parseSuggest(lastAIResponse);
    }

    // ============= OPERACIONES INTERNAS =============

    private void createMedPrescriptionLine(ProductID prodID, String[] instruc)
            throws ProductAlreadyInPrescriptionException,
            IncorrectTakingGuidelinesException {
        currentPrescription.addLine(prodID, instruc);
    }

    private void setPrescDateAndEndDate(Date date) {
        currentPrescription.setPrescDate(new Date());
        if (date != null) {
            currentPrescription.setEndDate(date);
        }
    }

    // ============= GETTERS para testing =============

    public MedicalHistory getCurrentHistory() {
        return currentHistory;
    }

    public MedicalPrescription getCurrentPrescription() {
        return currentPrescription;
    }

    public boolean isRevisionInitiated() {
        return revisionInitiated;
    }

    public boolean isPrescriptionEditionStarted() {
        return prescriptionEditionStarted;
    }

    public boolean isPrescriptionEditionFinished() {
        return prescriptionEditionFinished;
    }

    public boolean isSignatureStamped() {
        return signatureStamped;
    }
}