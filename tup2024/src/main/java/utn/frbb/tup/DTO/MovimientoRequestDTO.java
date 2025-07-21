package utn.frbb.tup.DTO;

public class MovimientoRequestDTO {
    private String descripcion;
    private float monto;

    public MovimientoRequestDTO(String descripcion, float monto) {
        this.descripcion = descripcion;
        this.monto = monto;
    }

    // getters
    public String getDescripcion() { return descripcion; }
    public float getMonto() { return monto; }
}
