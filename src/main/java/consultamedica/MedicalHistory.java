package consultamedica;

import data.HealthCardID;

/**
 * Representa la historia clínica de un paciente
 */
public class MedicalHistory {
    private HealthCardID cip; // CIP del paciente
    private int membShipNumb; // Número de membresía del médico de familia
    private String history; // Anotaciones en la historia clínica del paciente

    /**
     * Constructor de MedicalHistory
     * @param cip Código de identificación del paciente
     * @param memberShipNum Número de membresía del médico
     * @throws IncorrectParametersException si los parámetros son inválidos
     */
    public MedicalHistory(HealthCardID cip, int memberShipNum)
            throws IncorrectParametersException {
        if (cip == null) {
            throw new IncorrectParametersException("El CIP no puede ser null");
        }
        if (memberShipNum <= 0) {
            throw new IncorrectParametersException(
                    "El número de membresía debe ser positivo: " + memberShipNum);
        }

        this.cip = cip;
        this.membShipNumb = memberShipNum;
        this.history = "";
    }

    /**
     * Añade nuevas anotaciones a la historia del paciente
     * @param annot Anotaciones a añadir
     */
    public void addMedicalHistoryAnnotations(String annot) {
        if (annot != null && !annot.trim().isEmpty()) {
            if (!this.history.isEmpty()) {
                this.history += "\n";
            }
            this.history += annot;
        }
    }

    /**
     * Modifica el médico de familia del paciente
     * @param mshN Nuevo número de membresía
     */
    public void setNewDoctor(int mshN) {
        if (mshN <= 0) {
            throw new IllegalArgumentException(
                    "El número de membresía debe ser positivo: " + mshN);
        }
        this.membShipNumb = mshN;
    }

    // Getters
    public HealthCardID getCip() {
        return cip;
    }

    public int getMembShipNumb() {
        return membShipNumb;
    }

    public String getHistory() {
        return history;
    }

    @Override
    public String toString() {
        return "MedicalHistory{" +
                "cip=" + cip +
                ", membShipNumb=" + membShipNumb +
                ", historyLength=" + history.length() +
                '}';
    }
}