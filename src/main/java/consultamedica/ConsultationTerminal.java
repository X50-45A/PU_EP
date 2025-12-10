package main.java.consultamedica;

import main.java.data.HealthCardID;

public class ConsultationTerminal {
        ??? // The class members
        ??? // The constructor/s
    // Input events
    public void initRevision(HealthCardID cip, String illness) { . . . }
throws ConnectException, HealthCardIDException,
    AnyCurrentPrescriptionException;
    public void enterMedicalAssessmentInHistory(String assess) { . . . };
    public void initMedicalPrescriptionEdition() { . . . };
    public void enterMedicineWithGuidelines(ProductID prodID, String[] instruc)
    { . . . } throws ProductAlreadyInPrescriptionException,
    IncorrectTakingGuidelinesException;
    public void modifyDoseInLine(ProductID prodID, float newDose)
    { . . . } throws ProductNotInPrescriptionException;
    public void removeLine(ProductID prodID) { . . . }
throws ProductNotInPrescriptionException;
    public void enterTreatmentEndingDate(Date date) { . . . } throws
    IncorrectEndingDateException;
    public void finishMedicalPrescriptionEdition() { . . . };
    public void stampeeSignature() { . . . } throws eSignatureException;
    public MedicalPrescription sendHistoryAndPrescription() { . . . }
throws ConnectException, HealthCardIDException,
    AnyCurrentPrescriptionException,
    NotCompletedMedicalPrescription;
    public void printMedicalPrescrip() { . . . } throws printingException;
    // Input events regarding the AI operation
    public void callDecisionMakingAI() { . . . } throws AIException;
    public void askAIForSuggest(String prompt) { . . . } throws BadPromptException;
    public void extractGuidelinesFromSugg() { . . . };
    // internal operations
    private void createMedPrescriptionLine(ProductID, prodID, String[] instruc)
    { . . . };
    private void setPrescDateAndEndDate(Date date) { . . . };
(. . .) // Setter methods for injecting dependences
}