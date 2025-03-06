package utn.frbb.tup.model;

public enum TipoPersona implements Tipo {
    FISICA("F"), JURIDICA("J");
    private final String descripcion;

    TipoPersona(String descripcion) { this.descripcion = descripcion; }
    // getter
    @Override
    public String getDescripcion() { return descripcion; }
    // implementación específica de fromString para TipoPersona
    public static TipoPersona fromString(String descripcion) {
        for (TipoPersona tipo : TipoPersona.values()) {
            if (tipo.descripcion.equalsIgnoreCase(descripcion)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("No se pudo encontrar el tipo: " + descripcion);
    }
}
