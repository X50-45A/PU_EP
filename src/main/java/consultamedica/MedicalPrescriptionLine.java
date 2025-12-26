package main.java.consultamedica;

import main.java.data.ProductID;

/**
 * Representa una línea de prescripción médica
 * Asocia un producto (medicamento) con sus pautas de administración
 */
public class MedicalPrescriptionLine {
    private ProductID productID;
    private TakingGuideline guidelines;

    /**
     * Constructor de MedicalPrescriptionLine
     * @param productID Identificador del producto
     * @param guidelines Pautas de administración
     */
    public MedicalPrescriptionLine(ProductID productID, TakingGuideline guidelines) {
        if (productID == null) {
            throw new IllegalArgumentException("ProductID no puede ser null");
        }
        if (guidelines == null) {
            throw new IllegalArgumentException("TakingGuideline no puede ser null");
        }

        this.productID = productID;
        this.guidelines = guidelines;
    }

    public ProductID getProductID() {
        return productID;
    }

    public TakingGuideline getGuidelines() {
        return guidelines;
    }

    public void setGuidelines(TakingGuideline guidelines) {
        if (guidelines == null) {
            throw new IllegalArgumentException("TakingGuideline no puede ser null");
        }
        this.guidelines = guidelines;
    }

    @Override
    public String toString() {
        return "MedicalPrescriptionLine{" +
                "productID=" + productID +
                ", guidelines=" + guidelines +
                '}';
    }
}