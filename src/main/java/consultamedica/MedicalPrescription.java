package main.java.consultamedica;

import main.java.data.HealthCardID;

import java.util.Date;

public class MedicalPrescription {// A class that represents medical prescription
    private HealthCardID cip; // the healthcard ID of the patient
    private int membShipNumb; // the membership number of the family doctor
    private String illness; // illness associated
    private ePrescripCode prescCode; // the prescription code
    private Date prescDate; // the current date
    private Date endDate; // the date when the new treatment ends
    private DigitalSignature eSign; // the eSignature of the doctor
    // Its components, that is, the set of medical prescription lines
    public MedicalPrescription (. . .) { . . . } // Makes some inicialization
    public void addLine(ProductID prodID, String[] instruc) { . . . }
throws ProductAlreadyInPrescriptionException,
    IncorrectTakingGuidelinesException;
    public void modifyDoseInLine(ProductID prodID, float newDose) { . . . }
throws ProductNotInPrescriptionException;
    public void removeLine(ProductID prodID) { . . . }
throws ProductNotInPrescriptionException;
 // the getters and setters for some of the class members
}