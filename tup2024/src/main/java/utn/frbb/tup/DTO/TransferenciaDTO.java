package utn.frbb.tup.DTO;

import utn.frbb.tup.model.Movimiento;
import utn.frbb.tup.model.TipoMoneda;
import utn.frbb.tup.model.Transferencia;

public class TransferenciaDTO {
    private long origen;
    private long destino;
    private TipoMoneda tipoMoneda;
    private float monto;

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
    // setters
    public void setOrigen(long origen) { this.origen = origen; }
    public void setDestino(long destino) { this.destino = destino; }
    public void setTipoMoneda(TipoMoneda tipoMoneda) { this.tipoMoneda = tipoMoneda; }
    public void setMonto(float monto) { this.monto = monto; }
}