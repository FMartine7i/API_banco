package utn.frbb.tup.models;

public enum TipoCuenta implements Tipo {
    AHORROS("A"), CORRIENTE("ClienteDTO");
    private final String descripcion;

    TipoCuenta(String descripcion) {
        this.descripcion = descripcion;
    }
    // getter
    @Override
    public String getDescripcion() { return descripcion; }
    // implementación específica de fromString para TipoCuenta
    public static TipoCuenta fromString(String descripcion) {
        for (TipoCuenta tipo : TipoCuenta.values()) {
            if (tipo.descripcion.equalsIgnoreCase(descripcion)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Error: no se pudo encontrar el tipo: " + descripcion);
    }
}