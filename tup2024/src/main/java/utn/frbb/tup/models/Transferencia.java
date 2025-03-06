package utn.frbb.tup.models;

import java.time.LocalDateTime;

public class Transferencia extends Movimiento {
    private long cuentaDestino;
    private boolean esExterna;
    private static final TipoMovimiento tipoMovimiento = TipoMovimiento.TRANSFERENCIA;

    // constructor
    public Transferencia(long origen, long cuentaDestino, LocalDateTime fecha, TipoMoneda moneda, String descripcion, boolean esExterna, float monto) {
        super(origen, fecha, tipoMovimiento, moneda, descripcion, monto);
        this.cuentaDestino = cuentaDestino;
        this.esExterna = esExterna;
    }
    // getters
    public long getCuentaDestino() { return cuentaDestino; }
    public boolean getBoolean() { return esExterna; }
    // setters
    public void setCuentaDestino(long cuentaDestino) { this.cuentaDestino = cuentaDestino; }
    public void setBoolean(boolean esExterna) { this.esExterna = esExterna; }
}
