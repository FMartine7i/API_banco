package utn.frbb.tup.model;

public enum TipoMoneda implements Tipo {
    PESOS("P"), DOLARES("D");
    private String descripcion;

    TipoMoneda(String descripcion) { this.descripcion = descripcion; }
    // getter
    @Override
    public String getDescripcion() { return descripcion; }
    // implementación específica de 'fromString' para TipoMoneda
    public static TipoMoneda fromString(String descripcion) {
        for (TipoMoneda tipo : TipoMoneda.values()) {
            if (tipo.descripcion.equalsIgnoreCase(descripcion)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("No se pudo encontrar el tipo: " + descripcion);
    }
}
