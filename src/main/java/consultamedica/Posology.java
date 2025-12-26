package consultamedica;

/**
 * Representa la posología de un medicamento (dosis y frecuencia)
 */
public class Posology {
    private float dose;      // Dosis en unidades del medicamento
    private float freq;      // Frecuencia (número)
    private FqUnit freqUnit; // Unidad de frecuencia

    /**
     * Constructor de Posology
     * @param d Dosis (unidades del medicamento)
     * @param f Frecuencia (número)
     * @param u Unidad de frecuencia (HOUR, DAY, WEEK, MONTH)
     */
    public Posology(float d, float f, FqUnit u) {
        if (d <= 0) {
            throw new IllegalArgumentException("La dosis debe ser positiva: " + d);
        }
        if (f <= 0) {
            throw new IllegalArgumentException("La frecuencia debe ser positiva: " + f);
        }
        if (u == null) {
            throw new IllegalArgumentException("La unidad de frecuencia no puede ser null");
        }

        this.dose = d;
        this.freq = f;
        this.freqUnit = u;
    }

    // Getters
    public float getDose() {
        return dose;
    }

    public float getFreq() {
        return freq;
    }

    public FqUnit getFreqUnit() {
        return freqUnit;
    }

    // Setters
    public void setDose(float dose) {
        if (dose <= 0) {
            throw new IllegalArgumentException("La dosis debe ser positiva: " + dose);
        }
        this.dose = dose;
    }

    public void setFreq(float freq) {
        if (freq <= 0) {
            throw new IllegalArgumentException("La frecuencia debe ser positiva: " + freq);
        }
        this.freq = freq;
    }

    public void setFreqUnit(FqUnit freqUnit) {
        if (freqUnit == null) {
            throw new IllegalArgumentException("La unidad de frecuencia no puede ser null");
        }
        this.freqUnit = freqUnit;
    }

    @Override
    public String toString() {
        return dose + " unidades cada " + freq + " " + freqUnit;
    }
}