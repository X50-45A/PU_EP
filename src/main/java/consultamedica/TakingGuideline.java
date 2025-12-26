package consultamedica;

/**
 * Representa las pautas completas de administración de un medicamento
 */
public class TakingGuideline {
    private dayMoment dMoment;      // Momento del día
    private float duration;          // Duración en días
    private Posology posology;       // Posología (dosis y frecuencia)
    private String instructions;     // Instrucciones adicionales

    /**
     * Constructor completo con Posology separada
     *
     * @param dM       Momento del día
     * @param du       Duración en días
     * @param posology Objeto Posology
     * @param i        Instrucciones adicionales
     */
    public TakingGuideline(dayMoment dM, float du, Posology posology, String i) {
        if (dM == null) {
            throw new IllegalArgumentException("El momento del día no puede ser null");
        }
        if (du <= 0) {
            throw new IllegalArgumentException("La duración debe ser positiva: " + du);
        }
        if (posology == null) {
            throw new IllegalArgumentException("La posología no puede ser null");
        }

        this.dMoment = dM;
        this.duration = du;
        this.posology = posology;
        this.instructions = i != null ? i : "";
    }

    /**
     * Constructor alternativo con parámetros de posología separados
     *
     * @param dM Momento del día
     * @param du Duración en días
     * @param d  Dosis
     * @param f  Frecuencia
     * @param fu Unidad de frecuencia
     * @param i  Instrucciones adicionales
     */
    public TakingGuideline(dayMoment dM, float du, float d, float f, FqUnit fu, String i) {
        this(dM, du, new Posology(d, f, fu), i);
    }

    // Getters
    public dayMoment getdMoment() {
        return dMoment;
    }

    public float getDuration() {
        return duration;
    }

    public Posology getPosology() {
        return posology;
    }

    public String getInstructions() {
        return instructions;
    }

    // Setters
    public void setdMoment(dayMoment dMoment) {
        if (dMoment == null) {
            throw new IllegalArgumentException("El momento del día no puede ser null");
        }
        this.dMoment = dMoment;
    }

    public void setDuration(float duration) {
        if (duration <= 0) {
            throw new IllegalArgumentException("La duración debe ser positiva: " + duration);
        }
        this.duration = duration;
    }

    public void setPosology(Posology posology) {
        if (posology == null) {
            throw new IllegalArgumentException("La posología no puede ser null");
        }
        this.posology = posology;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions != null ? instructions : "";
    }

    @Override
    public String toString() {
        return "TakingGuideline{" +
                "momento=" + dMoment +
                ", duración=" + duration + " días" +
                ", posología=" + posology +
                ", instrucciones='" + instructions + '\'' +
                '}';
    }
}
