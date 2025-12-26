package services.doubles;

import services.HealthNationalService;
import medicalconsultation.MedicalHistory;
import medicalconsultation.MedicalPrescription;
import data.HealthCardID;
import data.ePrescripCode;
import java.net.ConnectException;

/**
 * Stub para HealthNationalService - simula comportamiento exitoso
 * Nunca lanza excepciones
 */
public class HealthNationalServiceStub implements HealthNationalService {

    @Override
    public MedicalHistory getMedicalHistory(HealthCardID cip)
            throws ConnectException {
        if (cip == null) {
            throw new IllegalArgumentException("CIP cannot be null");
        }
        return new MedicalHistory(cip, 100);
    }

    @Override
    public MedicalPrescription getMedicalPrescription(
            HealthCardID cip, String illness)
            throws ConnectException {
        if (cip == null) {
            throw new IllegalArgumentException("CIP cannot be null");
        }
        if (illness == null || illness.isEmpty()) {
            throw new IllegalArgumentException("Illness cannot be null or empty");
        }
        return new MedicalPrescription(cip, 100, illness);
    }

    @Override
    public MedicalPrescription sendHistoryAndPrescription(
            HealthCardID cip, MedicalHistory hce, String illness,
            MedicalPrescription mPresc)
            throws ConnectException {
        if (mPresc == null) {
            throw new IllegalArgumentException("Prescription cannot be null");
        }
        // Simula asignación de código de tratamiento
        mPresc.setPrescCode(new ePrescripCode("TRAT" + System.currentTimeMillis()));
        return mPresc;
    }

    @Override
    public MedicalPrescription generateTreatmCodeAndRegister(
            MedicalPrescription ePresc)
            throws ConnectException {
        if (ePresc == null) {
            throw new IllegalArgumentException("ePrescription cannot be null");
        }
        ePresc.setPrescCode(new ePrescripCode("CODE" + System.currentTimeMillis()));
        return ePresc;
    }
}
