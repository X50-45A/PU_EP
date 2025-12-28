package services.doubles;

import consultamedica.IncorrectParametersException;
import consultamedica.MedicalHistory;
import consultamedica.MedicalPrescription;
import data.InvalidePrescripCodeException;
import services.HealthNationalService;
import data.HealthCardID;
import data.ePrescripCode;
import services.NotCompletedMedicalPrescription;

import java.net.ConnectException;

/**
 * Mock para HealthNationalService - configurable para lanzar excepciones
 */
public class HealthNationalServiceMock implements HealthNationalService {

    private boolean throwConnectException = false;
    private boolean throwHealthCardIDException = false;
    private boolean throwAnyCurrentPrescriptionException = false;
    private boolean throwNotCompletedMedicalPrescription = false;

    // Configuradores
    public void setThrowConnectException(boolean value) {
        this.throwConnectException = value;
    }

    public void setThrowHealthCardIDException(boolean value) {
        this.throwHealthCardIDException = value;
    }

    public void setThrowAnyCurrentPrescriptionException(boolean value) {
        this.throwAnyCurrentPrescriptionException = value;
    }

    public void setThrowNotCompletedMedicalPrescription(boolean value) {
        this.throwNotCompletedMedicalPrescription = value;
    }

    public void reset() {
        this.throwConnectException = false;
        this.throwHealthCardIDException = false;
        this.throwAnyCurrentPrescriptionException = false;
        this.throwNotCompletedMedicalPrescription = false;
    }

    @Override
    public MedicalHistory getMedicalHistory(HealthCardID cip)
            throws ConnectException, IncorrectParametersException {

        if (throwConnectException) {
            throw new ConnectException("Network connection failed");
        }
        if (throwHealthCardIDException) {
            throw new IllegalArgumentException("Health Card ID not registered in SNS");
        }

        return new MedicalHistory(cip, 100);
    }

    @Override
    public MedicalPrescription getMedicalPrescription(
            HealthCardID cip, String illness)
            throws ConnectException, IncorrectParametersException {

        if (throwConnectException) {
            throw new ConnectException("Network connection failed");
        }
        if (throwHealthCardIDException) {
            throw new IllegalArgumentException("Health Card ID not registered in SNS");
        }
        if (throwAnyCurrentPrescriptionException) {
            throw new IllegalArgumentException("No current prescription for this illness");
        }

        return new MedicalPrescription(cip, 100, illness);
    }


    @Override
    public MedicalPrescription sendHistoryAndPrescription(
            HealthCardID cip, MedicalHistory hce, String illness,
            MedicalPrescription mPresc)
            throws ConnectException, InvalidePrescripCodeException {

        if (throwConnectException) {
            throw new ConnectException("Network connection failed");
        }
        if (throwHealthCardIDException) {
            throw new IllegalArgumentException("Health Card ID not registered in SNS");
        }
        if (throwAnyCurrentPrescriptionException) {
            throw new IllegalArgumentException("No current prescription for this illness");
        }
        if (throwNotCompletedMedicalPrescription) {
            throw new IllegalArgumentException("Medical prescription is not completed");
        }

        mPresc.setPrescCode(new ePrescripCode("TRAT" + System.currentTimeMillis()));
        return mPresc;
    }

    @Override
    public MedicalPrescription generateTreatmCodeAndRegister(
            MedicalPrescription ePresc) throws ConnectException, InvalidePrescripCodeException {

        if (throwConnectException) {
            throw new ConnectException("Network connection failed");
        }

        ePresc.setPrescCode(new ePrescripCode("CODE" + System.currentTimeMillis()));
        return ePresc;
    }
}

