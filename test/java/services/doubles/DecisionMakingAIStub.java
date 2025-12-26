package medicalconsultation.services.doubles;
import services.*;
import data.*;
import java.util.Date;

public class HealthNationalServiceStub implements HealthNationalService {
    
    @Override
    public MedicalHistory getMedicalHistory(HealthCardID cip) 
            throws ConnectException, HealthCardIDException {
        // Devuelve siempre un historial válido
        return new MedicalHistory(cip, 100);
    }
    
    @Override
    public MedicalPrescription getMedicalPrescription(
            HealthCardID cip, String illness)
            throws ConnectException, HealthCardIDException, 
                   AnyCurrentPrescriptionException {
        // Devuelve siempre una prescripción válida
        return new MedicalPrescription(cip, 100, illness);
    }
    
    @Override
    public MedicalPrescription sendHistoryAndPrescription(
            HealthCardID cip, MedicalHistory hce, String illness, 
            MedicalPrescription mPresc)
            throws ConnectException, HealthCardIDException,
                   AnyCurrentPrescriptionException, 
                   NotCompletedMedicalPrescription {
        // Simula envío exitoso
        mPresc.setPrescCode(new ePrescripCode("CODE123456"));
        return mPresc;
    }
    
    @Override
    public MedicalPrescription generateTreatmCodeAndRegister(
            MedicalPrescription ePresc) throws ConnectException {
        return ePresc;
    }
}