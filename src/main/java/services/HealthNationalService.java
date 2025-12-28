package services;

import consultamedica.IncorrectParametersException;
import consultamedica.MedicalHistory;
import consultamedica.MedicalPrescription;
import data.HealthCardID;

import java.net.ConnectException;

import data.InvalidePrescripCodeException;
import services.NotCompletedMedicalPrescription;

public interface HealthNationalService {
    MedicalHistory getMedicalHistory (HealthCardID cip)
            throws ConnectException, HealthCardIDException, IncorrectParametersException;
    MedicalPrescription getMedicalPrescription(HealthCardID cip, String illness)
            throws ConnectException, HealthCardIDException,
            AnyCurrentPrescriptionException, IncorrectParametersException;
    MedicalPrescription sendHistoryAndPrescription(HealthCardID cip,
                                                   MedicalHistory hce, String illness, MedicalPrescription mPresc)
            throws ConnectException, HealthCardIDException,
            AnyCurrentPrescriptionException, NotCompletedMedicalPrescription, InvalidePrescripCodeException;
    // Internal operation
    MedicalPrescription generateTreatmCodeAndRegister(MedicalPrescription ePresc) throws ConnectException, InvalidePrescripCodeException;
}
