package utn.frbb.tup.DTO;

public class MovimientoRequestDTO {
    private long nro;
    private String descripcion;
    private float monto;

    public MovimientoRequestDTO() {}

    // getters
    public long getNro() { return nro; }
    public String getDescripcion() { return descripcion; }
    public float getMonto() { return monto; }
    // setters
    public void setNro(long nro) { this.nro = nro; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setMonto(float monto) { this.monto = monto; }
}
