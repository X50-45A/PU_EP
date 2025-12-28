package services;

import data.ProductID;
import data.InvalidProductIDException;

/**
 * Representa una sugerencia de la IA para ajustar la medicación.
 * Formato del patrón de la IA:
 * - INSERT: <I, productID, dayMoment, duration, dose, freq, freqUnit, instructions>
 * - REMOVE: <R, productID>
 * - MODIFY: <M, productID, campo1, campo2, ...> (campos vacíos si no se modifican)
 */
public class Suggestion {

    /**
     * Tipos de operación que sugiere la IA
     */
    public enum OperationType {
        INSERT("I"),  // Insertar nuevo medicamento
        REMOVE("R"),  // Eliminar medicamento (también acepta "E")
        MODIFY("M");  // Modificar medicamento existente

        private final String code;

        OperationType(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        /**
         * Parsea el código de operación desde String
         *
         * @param code Código ("I", "R", "E", "M")
         * @return OperationType correspondiente
         */
        public static OperationType fromCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new IllegalArgumentException("El código de operación no puede ser null o vacío");
            }

            String normalized = code.trim().toUpperCase();
            switch (normalized) {
                case "I":
                case "INSERT":
                    return INSERT;
                case "R":
                case "E": // Acepta ambos: R (Remove) y E (Eliminar)
                case "REMOVE":
                    return REMOVE;
                case "M":
                case "MODIFY":
                    return MODIFY;
                default:
                    throw new IllegalArgumentException("Código de operación inválido: " + code);
            }
        }
    }

    private OperationType operation;
    private ProductID productID;
    private String dayMoment;      // Campo 2: momento del día (o vacío en MODIFY)
    private String duration;       // Campo 3: duración (o vacío en MODIFY)
    private String dose;           // Campo 4: dosis (o vacío en MODIFY)
    private String frequency;      // Campo 5: frecuencia (o vacío en MODIFY)
    private String frequencyUnit;  // Campo 6: unidad de frecuencia (o vacío en MODIFY)
    private String instructions;   // Campo 7: instrucciones adicionales (o vacío en MODIFY)

    /**
     * Constructor completo (8 parámetros String)
     * Formato: operación, productID, dayMoment, duration, dose, freq, freqUnit, instructions
     *
     * @param operation     Tipo de operación ("I", "R"/"E", "M")
     * @param productID     Código del producto (12 dígitos)
     * @param dayMoment     Momento del día (puede estar vacío en MODIFY/REMOVE)
     * @param duration      Duración en días (puede estar vacío en MODIFY/REMOVE)
     * @param dose          Dosis (puede estar vacío en MODIFY/REMOVE)
     * @param frequency     Frecuencia (puede estar vacío en MODIFY/REMOVE)
     * @param frequencyUnit Unidad de frecuencia (puede estar vacío en MODIFY/REMOVE)
     * @param instructions  Instrucciones adicionales (puede estar vacío)
     * @throws InvalidProductIDException si el código de producto es inválido
     */
    public Suggestion(String operation, String productID, String dayMoment,
                      String duration, String dose, String frequency,
                      String frequencyUnit, String instructions)
            throws InvalidProductIDException {

        // Parsear y validar operación
        this.operation = OperationType.fromCode(operation);

        // Crear ProductID (puede lanzar InvalidProductIDException)
        this.productID = new ProductID(productID);

        // Almacenar los demás campos (pueden estar vacíos)
        this.dayMoment = (dayMoment != null) ? dayMoment.trim() : "";
        this.duration = (duration != null) ? duration.trim() : "";
        this.dose = (dose != null) ? dose.trim() : "";
        this.frequency = (frequency != null) ? frequency.trim() : "";
        this.frequencyUnit = (frequencyUnit != null) ? frequencyUnit.trim() : "";
        this.instructions = (instructions != null) ? instructions.trim() : "";

        // Validar según el tipo de operación
        validateFields();
    }

    /**
     * Constructor alternativo con OperationType enum y ProductID
     *
     * @param operation  Tipo de operación (enum)
     * @param productID  ProductID ya creado
     * @param guidelines Array con las pautas [dayMoment, duration, dose, freq, freqUnit, instructions]
     */
    public Suggestion(OperationType operation, ProductID productID, String[] guidelines) {
        if (operation == null) {
            throw new IllegalArgumentException("La operación no puede ser null");
        }
        if (productID == null) {
            throw new IllegalArgumentException("El ProductID no puede ser null");
        }

        this.operation = operation;
        this.productID = productID;

        if (guidelines != null && guidelines.length >= 6) {
            this.dayMoment = guidelines[0] != null ? guidelines[0].trim() : "";
            this.duration = guidelines[1] != null ? guidelines[1].trim() : "";
            this.dose = guidelines[2] != null ? guidelines[2].trim() : "";
            this.frequency = guidelines[3] != null ? guidelines[3].trim() : "";
            this.frequencyUnit = guidelines[4] != null ? guidelines[4].trim() : "";
            this.instructions = guidelines[5] != null ? guidelines[5].trim() : "";
        } else {
            this.dayMoment = "";
            this.duration = "";
            this.dose = "";
            this.frequency = "";
            this.frequencyUnit = "";
            this.instructions = "";
        }
    }

    /**
     * Constructor simplificado para REMOVE (sin pautas)
     */
    public Suggestion(OperationType operation, ProductID productID) {
        this(operation, productID, null);
    }

    /**
     * Valida que los campos sean consistentes con el tipo de operación
     */
    private void validateFields() {
        switch (operation) {
            case INSERT:
                // INSERT debe tener todos los campos obligatorios
                if (dayMoment.isEmpty() || duration.isEmpty() || dose.isEmpty() ||
                        frequency.isEmpty() || frequencyUnit.isEmpty()) {
                    throw new IllegalArgumentException(
                            "INSERT requiere todos los campos de pautas de medicación");
                }
                break;

            case REMOVE:
                // REMOVE solo necesita el productID (los demás campos pueden estar vacíos)
                break;

            case MODIFY:
                // MODIFY puede tener algunos campos vacíos (solo se modifican los no vacíos)
                // Al menos un campo debe estar presente
                if (dayMoment.isEmpty() && duration.isEmpty() && dose.isEmpty() &&
                        frequency.isEmpty() && frequencyUnit.isEmpty() && instructions.isEmpty()) {
                    throw new IllegalArgumentException(
                            "MODIFY debe especificar al menos un campo a modificar");
                }
                break;
        }
    }

    /**
     * Convierte la sugerencia a un array de String compatible con MedicalPrescription.addLine()
     * Formato: [dayMoment, duration, dose, frequency, frequencyUnit, instructions, reservado]
     *
     * @return Array de 7 elementos
     */
    public String[] toInstructionsArray() {
        return new String[]{
                dayMoment,
                duration,
                dose,
                frequency,
                frequencyUnit,
                instructions,
                "" // Campo reservado
        };
    }

    /**
     * Verifica si un campo específico debe ser modificado (no está vacío)
     */
    public boolean shouldModifyDayMoment() {
        return !dayMoment.isEmpty();
    }

    public boolean shouldModifyDuration() {
        return !duration.isEmpty();
    }

    public boolean shouldModifyDose() {
        return !dose.isEmpty();
    }

    public boolean shouldModifyFrequency() {
        return !frequency.isEmpty();
    }

    public boolean shouldModifyFrequencyUnit() {
        return !frequencyUnit.isEmpty();
    }

    public boolean shouldModifyInstructions() {
        return !instructions.isEmpty();
    }

    // ============= GETTERS =============

    public OperationType getOperation() {
        return operation;
    }

    public ProductID getProductID() {
        return productID;
    }

    public String getDayMoment() {
        return dayMoment;
    }

    public String getDuration() {
        return duration;
    }

    public String getDose() {
        return dose;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getFrequencyUnit() {
        return frequencyUnit;
    }

    public String getInstructions() {
        return instructions;
    }

    /**
     * Retorna las pautas como array (compatible con versión anterior)
     */
    public String[] getGuidelines() {
        return toInstructionsArray();
    }

    // ============= MÉTODOS AUXILIARES =============

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<").append(operation.getCode());
        sb.append(", ").append(productID.getCode());

        if (operation != OperationType.REMOVE) {
            if (!dayMoment.isEmpty()) sb.append(", ").append(dayMoment);
            else sb.append(", ");

            if (!duration.isEmpty()) sb.append(", ").append(duration);
            else sb.append(", ");

            if (!dose.isEmpty()) sb.append(", ").append(dose);
            else sb.append(", ");

            if (!frequency.isEmpty()) sb.append(", ").append(frequency);
            else sb.append(", ");

            if (!frequencyUnit.isEmpty()) sb.append(", ").append(frequencyUnit);
            else sb.append(", ");

            if (!instructions.isEmpty()) sb.append(", ").append(instructions);
        }

        sb.append(">");
        return sb.toString();
    }
}