package main.java.services;

import main.java.consultamedica.MedicalHistory;
import main.java.consultamedica.MedicalPrescription;
import main.java.data.HealthCardID;

public interface HealthNationalService {
    MedicalHistory getMedicalHistory (HealthCardID cip)
            throws ConnectException, HealthCardIDException;
    MedicalPrescription getMedicalPrescription(HealthCardID cip, String illness)
            throws ConnectException, HealthCardIDException,
            AnyCurrentPrescriptionException;
    MedicalPrescription sendHistoryAndPrescription(HealthCardID cip,
                                                    History hce, String illness, MedicalPrescription mPresc)
            throws ConnectException, HealthCardIDException,
            AnyCurrentPrescriptionException, NotCompletedMedicalPrescription;
    // Internal operation
    MedicalPrescription generateTreatmCodeAndRegister(MedicalPrescription ePresc);
}
