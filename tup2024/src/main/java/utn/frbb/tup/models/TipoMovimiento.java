package utn.frbb.tup.models;

public enum TipoMovimiento implements Tipo {
    DEPOSITO("D"), RETIRO("R"), TRANSFERENCIA("T");
    private String descripcion;

    TipoMovimiento(String descripcion) { this.descripcion = descripcion; }
    // getter

    public String getDescripcion () { return descripcion; }
    // implementación específica de 'fromString' para TipoMovimiento
    public static TipoMovimiento fromString(String descripcion) {
        for (TipoMovimiento tipo : TipoMovimiento.values()) {
            if (tipo.descripcion.equalsIgnoreCase(descripcion)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("No se pudo encontrar el tipo: " + descripcion);
    }
}
