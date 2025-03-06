package utn.frbb.tup.model;

import java.time.LocalDateTime;

public class Movimiento {
    private long origen;
    private LocalDateTime fecha;
    private TipoMovimiento tipoMovimiento;
    private TipoMoneda tipoMoneda;
    private String descripcion;
    private float monto;

    // constructor vacío
    public Movimiento() {}
    // constructor
    public Movimiento(long origen, LocalDateTime fecha, TipoMovimiento tipoMovimiento, TipoMoneda tipoMoneda, String descripcion, float monto) {
        this.origen = origen;
        this.fecha = fecha;
        this.tipoMovimiento = tipoMovimiento;
        this.tipoMoneda = tipoMoneda;
        this.descripcion = descripcion;
        this.monto = monto;
    }
    //  getters
    public long getOrigen() { return origen; }
    public LocalDateTime getFecha() { return fecha; }
    public TipoMovimiento getTipoMovimiento() { return tipoMovimiento; }
    public String getDescripcion() { return descripcion; }
    public float getMonto() { return monto; }
    public TipoMoneda getTipoMoneda() { return tipoMoneda; }
    // setters
    public void setOrigen(long origen) { this.origen = origen; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public void setMonto(float monto) { this.monto = monto; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }
    public void setTipoMoneda(TipoMoneda tipoMoneda) { this.tipoMoneda = tipoMoneda; }
}
