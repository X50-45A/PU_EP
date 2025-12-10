package main.java.services;

import main.java.data.HealthCardID;

public interface HealthNationalService {
    MedicalHistory getMedicalHistory (HealthCardID cip)
            throws ConnectException, HealthCardIDException;
    MedicalPrescription5 getMedicalPrescription(HealthCardID cip, String illness)
            throws ConnectException, HealthCardIDException,
            AnyCurrentPrescriptionException;
    MedicalPrescription6 sendHistoryAndPrescription(HealthCardID cip,
                                                    History hce, String illness, MedicalPrescription mPresc)
            throws ConnectException, HealthCardIDException,
            AnyCurrentPrescriptionException, NotCompletedMedicalPrescription;
    // Internal operation
    MedicalPrescription generateTreatmCodeAndRegister(MedicalPrescription ePresc);
}
