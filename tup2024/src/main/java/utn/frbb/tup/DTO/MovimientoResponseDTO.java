package utn.frbb.tup.DTO;
import utn.frbb.tup.model.Movimiento;
import utn.frbb.tup.model.TipoMoneda;
import utn.frbb.tup.model.TipoMovimiento;
import java.time.LocalDateTime;

public class MovimientoResponseDTO {
    private final long origen;
    private final LocalDateTime fecha;
    private final TipoMovimiento tipoMovimiento;
    private final TipoMoneda tipoMoneda;
    private final String descripcion;
    private final float monto;

    // constructor
    public MovimientoResponseDTO(Movimiento movimiento) {
        this.origen = movimiento.getOrigen();
        this.fecha = movimiento.getFecha();
        this.tipoMovimiento = movimiento.getTipoMovimiento();
        this.tipoMoneda = movimiento.getTipoMoneda();
        this.descripcion = movimiento.getDescripcion();
        this.monto = movimiento.getMonto();
    }
    //  getters
    public long getOrigen() { return origen; }
    public LocalDateTime getFecha() { return fecha; }
    public TipoMovimiento getTipoMovimiento() { return tipoMovimiento; }
    public String getDescripcion() { return descripcion; }
    public float getMonto() { return monto; }
    public TipoMoneda getTipoMoneda() { return tipoMoneda; }
}