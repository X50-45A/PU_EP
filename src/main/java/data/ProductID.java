package data;

final public class ProductID {
    private final String code;

    public ProductID(String code) throws InvalidProductIDException {
        if (code == null) {
            throw new InvalidProductIDException("El código de producto no puede ser null");
        }
        if (code.length() != 12) {
            throw new InvalidProductIDException("El código UPC debe tener 12 dígitos");
        }
        if (!code.matches("\\d{12}")) {
            throw new InvalidProductIDException("El código UPC debe ser numérico");
        }
        this.code = code;
    }

    public String getCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductID productID = (ProductID) o;
        return code.equals(productID.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "ProductID{" + "code='" + code + '\'' + '}';
    }
}