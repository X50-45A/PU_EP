package services;

import consultamedica.MedicalHistory;
import consultamedica.MedicalPrescription;
import data.HealthCardID;

import java.net.ConnectException;
import services.NotCompletedMedicalPrescription;

public interface HealthNationalService {
    MedicalHistory getMedicalHistory (HealthCardID cip)
            throws ConnectException, HealthCardIDException;
    MedicalPrescription getMedicalPrescription(HealthCardID cip, String illness)
            throws ConnectException, HealthCardIDException,
            AnyCurrentPrescriptionException;
    MedicalPrescription sendHistoryAndPrescription(HealthCardID cip,
                                                   MedicalHistory hce, String illness, MedicalPrescription mPresc)
            throws ConnectException, HealthCardIDException,
            AnyCurrentPrescriptionException, NotCompletedMedicalPrescription;
    // Internal operation
    MedicalPrescription generateTreatmCodeAndRegister(MedicalPrescription ePresc);
}
