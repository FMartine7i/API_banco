package utn.frbb.tup.DTO;
import utn.frbb.tup.model.Movimiento;
import utn.frbb.tup.model.TipoMoneda;
import utn.frbb.tup.model.Transferencia;

public class TransferenciaDTO {
    private final long origen;
    private final long destino;
    private final TipoMoneda tipoMoneda;
    private final float monto;

    public TransferenciaDTO(Movimiento movimiento) {
        this.origen = movimiento.getOrigen();
        this.destino = ((Transferencia)movimiento).getCuentaDestino();
        this.tipoMoneda = movimiento.getTipoMoneda();
        this.monto = movimiento.getMonto();
    }
    // getters
    public long getOrigen() { return origen; }
    public long getDestino() { return destino; }
    public TipoMoneda getTipoMoneda() { return tipoMoneda; }
    public float getMonto() { return monto; }
}