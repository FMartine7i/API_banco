package utn.frbb.tup.model;

public interface Tipo {
    String getDescripcion();
    static Tipo fromString(String descripcion) { throw new UnsupportedOperationException("El método 'fromString' debe ser implementado en cada Enum."); }
}