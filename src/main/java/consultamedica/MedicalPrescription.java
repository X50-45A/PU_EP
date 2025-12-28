package consultamedica;

import data.DigitalSignature;
import data.HealthCardID;
import data.ProductID;
import data.ePrescripCode;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

/**
 * Representa una prescripción médica asociada a un paciente y una enfermedad específica.
 * Gestiona las líneas de prescripción médica con sus respectivas pautas de medicación.
 */
public class MedicalPrescription {

    private HealthCardID cip; // the healthcard ID of the patient
    private int membShipNumb; // the membership number of the family doctor
    private String illness; // illness associated
    private ePrescripCode prescCode; // the prescription code
    private Date prescDate; // the current date
    private Date endDate; // the date when the new treatment ends
    private DigitalSignature eSign; // the eSignature of the doctor

    // Componentes: conjunto de líneas de prescripción médica
    // Usamos HashMap para búsqueda eficiente O(1) por ProductID
    private Map<ProductID, MedicalPrescriptionLine> prescriptionLines;

    /**
     * Constructor principal de MedicalPrescription
     * @param cip Código de identificación del paciente (HealthCardID)
     * @param memberShipNum Número de membresía del médico de familia
     * @param illness Enfermedad asociada a esta prescripción
     * @throws IncorrectParametersException si algún parámetro es inválido
     */
    public MedicalPrescription(HealthCardID cip, int memberShipNum, String illness)
            throws IncorrectParametersException {

        // Validaciones
        if (cip == null) {
            throw new IncorrectParametersException("El CIP no puede ser null");
        }
        if (memberShipNum <= 0) {
            throw new IncorrectParametersException(
                    "El número de membresía debe ser positivo: " + memberShipNum);
        }
        if (illness == null || illness.trim().isEmpty()) {
            throw new IncorrectParametersException(
                    "La enfermedad no puede ser null o vacía");
        }

        // Inicialización
        this.cip = cip;
        this.membShipNumb = memberShipNum;
        this.illness = illness;
        this.prescriptionLines = new HashMap<>();

        // Los siguientes atributos se establecerán posteriormente
        this.prescCode = null;
        this.prescDate = null;
        this.endDate = null;
        this.eSign = null;
    }

    /**
     * Añade una línea de prescripción médica con un medicamento y sus pautas
     * @param prodID Identificador del producto (medicamento)
     * @param instruc Array de instrucciones con el formato:
     *                [0] = dayMoment (ej: "BEFORELUNCH")
     *                [1] = duration (días)
     *                [2] = dose (unidades)
     *                [3] = frequency (número)
     *                [4] = frequencyUnit (ej: "DAY")
     *                [5] = instructions (texto adicional)
     *                [6] = reservado para uso futuro
     * @throws ProductAlreadyInPrescriptionException si el producto ya existe
     * @throws IncorrectTakingGuidelinesException si las instrucciones son incorrectas
     */
    public void addLine(ProductID prodID, String[] instruc)
            throws ProductAlreadyInPrescriptionException,
            IncorrectTakingGuidelinesException {

        // Validar que el producto no existe ya
        if (prescriptionLines.containsKey(prodID)) {
            throw new ProductAlreadyInPrescriptionException(
                    "El producto " + prodID.getCode() + " ya está en la prescripción");
        }

        // Validar estructura del array de instrucciones
        if (instruc == null) {
            throw new IncorrectTakingGuidelinesException(
                    "Las instrucciones no pueden ser null");
        }
        if (instruc.length != 7) {
            throw new IncorrectTakingGuidelinesException(
                    "Las instrucciones deben contener exactamente 7 elementos. Recibidos: "
                            + instruc.length);
        }

        // Validar y parsear las instrucciones
        try {
            // Parsear dayMoment
            dayMoment dMoment = dayMoment.valueOf(instruc[0].trim());

            // Parsear duration
            float duration = Float.parseFloat(instruc[1].trim());
            if (duration <= 0) {
                throw new IncorrectTakingGuidelinesException(
                        "La duración debe ser positiva: " + duration);
            }

            // Parsear dose
            float dose = Float.parseFloat(instruc[2].trim());
            if (dose <= 0) {
                throw new IncorrectTakingGuidelinesException(
                        "La dosis debe ser positiva: " + dose);
            }

            // Parsear frequency
            float freq = Float.parseFloat(instruc[3].trim());
            if (freq <= 0) {
                throw new IncorrectTakingGuidelinesException(
                        "La frecuencia debe ser positiva: " + freq);
            }

            // Parsear frequency unit
            FqUnit freqUnit = FqUnit.valueOf(instruc[4].trim());

            // Instrucciones adicionales (puede estar vacío)
            String instructions = instruc[5] != null ? instruc[5] : "";

            // Crear la posología
            Posology posology = new Posology(dose, freq, freqUnit);

            // Crear las pautas de administración
            TakingGuideline guidelines = new TakingGuideline(
                    dMoment, duration, posology, instructions);

            // Crear y añadir la línea de prescripción
            MedicalPrescriptionLine line = new MedicalPrescriptionLine(prodID, guidelines);
            prescriptionLines.put(prodID, line);

        } catch (IllegalArgumentException e) {
            throw new IncorrectTakingGuidelinesException(
                    "Error al parsear las instrucciones: " + e.getMessage());
        } catch (NullPointerException e) {
            throw new IncorrectTakingGuidelinesException(
                    "Las instrucciones contienen valores null: " + e.getMessage());
        }
    }

    /**
     * Modifica la dosis de un medicamento en una línea de prescripción existente
     * @param prodID Identificador del producto a modificar
     * @param newDose Nueva dosis a aplicar
     * @throws ProductNotInPrescriptionException si el producto no existe en la prescripción
     */
    public void modifyDoseInLine(ProductID prodID, float newDose)
            throws ProductNotInPrescriptionException {

        // Verificar que el producto existe
        if (!prescriptionLines.containsKey(prodID)) {
            throw new ProductNotInPrescriptionException(
                    "El producto " + prodID.getCode() + " no está en la prescripción");
        }

        // Validar que la nueva dosis es positiva
        if (newDose <= 0) {
            throw new IllegalArgumentException(
                    "La nueva dosis debe ser positiva: " + newDose);
        }

        // Obtener la línea y modificar la dosis
        MedicalPrescriptionLine line = prescriptionLines.get(prodID);
        line.getGuidelines().getPosology().setDose(newDose);
    }

    /**
     * Modifica la frecuencia de administración de un medicamento
     * @param prodID Identificador del producto a modificar
     * @param newFreq Nueva frecuencia
     * @throws ProductNotInPrescriptionException si el producto no existe
     */
    public void modifyFrequencyInLine(ProductID prodID, float newFreq)
            throws ProductNotInPrescriptionException {

        if (!prescriptionLines.containsKey(prodID)) {
            throw new ProductNotInPrescriptionException(
                    "El producto " + prodID.getCode() + " no está en la prescripción");
        }

        if (newFreq <= 0) {
            throw new IllegalArgumentException(
                    "La nueva frecuencia debe ser positiva: " + newFreq);
        }

        MedicalPrescriptionLine line = prescriptionLines.get(prodID);
        line.getGuidelines().getPosology().setFreq(newFreq);
    }

    /**
     * Modifica la duración del tratamiento de un medicamento
     * @param prodID Identificador del producto a modificar
     * @param newDuration Nueva duración en días
     * @throws ProductNotInPrescriptionException si el producto no existe
     */
    public void modifyDurationInLine(ProductID prodID, float newDuration)
            throws ProductNotInPrescriptionException {

        if (!prescriptionLines.containsKey(prodID)) {
            throw new ProductNotInPrescriptionException(
                    "El producto " + prodID.getCode() + " no está en la prescripción");
        }

        if (newDuration <= 0) {
            throw new IllegalArgumentException(
                    "La nueva duración debe ser positiva: " + newDuration);
        }

        MedicalPrescriptionLine line = prescriptionLines.get(prodID);
        line.getGuidelines().setDuration(newDuration);
    }

    /**
     * Elimina una línea de prescripción médica
     * @param prodID Identificador del producto a eliminar
     * @throws ProductNotInPrescriptionException si el producto no existe en la prescripción
     */
    public void removeLine(ProductID prodID)
            throws ProductNotInPrescriptionException {

        // Verificar que el producto existe
        if (!prescriptionLines.containsKey(prodID)) {
            throw new ProductNotInPrescriptionException(
                    "El producto " + prodID + " no está en la prescripción");
        }

        // Eliminar la línea
        prescriptionLines.remove(prodID);
    }

    /**
     * Verifica si la prescripción está completa (lista para enviar al SNS)
     * @return true si todos los campos obligatorios están completos
     */
    public boolean isComplete() {
        return prescCode != null &&
                prescDate != null &&
                endDate != null &&
                eSign != null &&
                !prescriptionLines.isEmpty();
    }

    /**
     * Obtiene el número de líneas de prescripción
     * @return cantidad de medicamentos en la prescripción
     */
    public int getLineCount() {
        return prescriptionLines.size();
    }

    /**
     * Verifica si contiene un producto específico
     * @param prodID Identificador del producto a buscar
     * @return true si el producto está en la prescripción
     */
    public boolean containsProduct(ProductID prodID) {
        return prescriptionLines.containsKey(prodID);
    }

    /**
     * Obtiene todas las líneas de prescripción
     * @return Colección de líneas (copia defensiva)
     */
    public Collection<MedicalPrescriptionLine> getAllLines() {
        return prescriptionLines.values();
    }

    /**
     * Obtiene una línea de prescripción específica
     * @param prodID Identificador del producto
     * @return La línea de prescripción o null si no existe
     */
    public MedicalPrescriptionLine getLine(ProductID prodID) {
        return prescriptionLines.get(prodID);
    }

    // ============= GETTERS =============

    public HealthCardID getCip() {
        return cip;
    }

    public int getMembShipNumb() {
        return membShipNumb;
    }

    public String getIllness() {
        return illness;
    }

    public ePrescripCode getPrescCode() {
        return prescCode;
    }

    public Date getPrescDate() {
        // Copia defensiva para evitar modificaciones externas
        return prescDate != null ? new Date(prescDate.getTime()) : null;
    }

    public Date getEndDate() {
        // Copia defensiva para evitar modificaciones externas
        return endDate != null ? new Date(endDate.getTime()) : null;
    }

    public DigitalSignature geteSign() {
        return eSign;
    }

    /**
     * Obtiene una copia del mapa de líneas de prescripción
     * @return Copia defensiva del mapa de líneas
     */
    public Map<ProductID, MedicalPrescriptionLine> getLines() {
        return new HashMap<>(prescriptionLines);
    }

    // ============= SETTERS =============

    /**
     * Establece el código de prescripción (asignado por el SNS)
     * @param prescCode Código de prescripción
     */
    public void setPrescCode(ePrescripCode prescCode) {
        this.prescCode = prescCode;
    }

    /**
     * Establece la fecha de prescripción
     * @param prescDate Fecha de prescripción
     */
    public void setPrescDate(Date prescDate) {
        // Copia defensiva para evitar modificaciones externas
        this.prescDate = prescDate != null ? new Date(prescDate.getTime()) : null;
    }

    /**
     * Establece la fecha de finalización del tratamiento
     * @param endDate Fecha de finalización
     */
    public void setEndDate(Date endDate) {
        // Copia defensiva para evitar modificaciones externas
        this.endDate = endDate != null ? new Date(endDate.getTime()) : null;
    }

    /**
     * Establece la firma electrónica del médico
     * @param eSign Firma digital
     */
    public void seteSign(DigitalSignature eSign) {
        this.eSign = eSign;
    }

    /**
     * Modifica el médico de familia asignado
     * @param membShipNumb Nuevo número de membresía
     */
    public void setMembShipNumb(int membShipNumb) {
        if (membShipNumb <= 0) {
            throw new IllegalArgumentException(
                    "El número de membresía debe ser positivo");
        }
        this.membShipNumb = membShipNumb;
    }

    // ============= MÉTODOS AUXILIARES =============

    @Override
    public String toString() {
        return "MedicalPrescription{" +
                "cip=" + cip +
                ", membShipNumb=" + membShipNumb +
                ", illness='" + illness + '\'' +
                ", prescCode=" + prescCode +
                ", prescDate=" + prescDate +
                ", endDate=" + endDate +
                ", lineCount=" + prescriptionLines.size() +
                ", isComplete=" + isComplete() +
                '}';
    }
}

