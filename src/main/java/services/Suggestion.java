package main.java.services;

import main.java.data.ProductID;

/**
 * Representa una sugerencia de la IA para ajustar la medicación
 */
public class Suggestion {

    /**
     * Tipos de operación que sugiere la IA
     */
    public enum OperationType {
        INSERT,  // Insertar nuevo medicamento (I)
        REMOVE,  // Eliminar medicamento (E)
        MODIFY   // Modificar medicamento existente (M)
    }

    private OperationType operation;
    private ProductID productID;
    private String[] guidelines; // Pautas de medicación (puede tener valores null para MODIFY)

    /**
     * Constructor de Suggestion
     * @param operation Tipo de operación (INSERT, REMOVE, MODIFY)
     * @param productID Identificador del producto
     * @param guidelines Array con las pautas (puede contener nulls en MODIFY)
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
        this.guidelines = guidelines;
    }

    /**
     * Constructor simplificado para REMOVE (no necesita guidelines)
     */
    public Suggestion(OperationType operation, ProductID productID) {
        this(operation, productID, null);
    }

    public OperationType getOperation() {
        return operation;
    }

    public ProductID getProductID() {
        return productID;
    }

    public String[] getGuidelines() {
        return guidelines;
    }

    @Override
    public String toString() {
        return "Suggestion{" +
                "operation=" + operation +
                ", productID=" + productID +
                ", hasGuidelines=" + (guidelines != null) +
                '}';
    }
}