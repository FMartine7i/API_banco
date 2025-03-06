package utn.frbb.tup.DTO;

import utn.frbb.tup.models.TipoMoneda;
import utn.frbb.tup.models.Transferencia;

public class TransferenciaRequestDTO {
    private long origen;
    private String descripcion;
    private long destino;
    private TipoMoneda tipoMoneda;
    private float monto;

    // getters
    public long getOrigen() { return origen; }
    public String getDescripcion() { return descripcion; }
    public long getDestino() { return destino; }
    public TipoMoneda getTipoMoneda() { return tipoMoneda; }
    public float getMonto() { return monto; }
    // setters
    public void setOrigen(long origen) { this.origen = origen; }
    public void setDestino(long destino) { this.destino = destino; }
    public void setTipoMoneda(TipoMoneda tipoMoneda) { this.tipoMoneda = tipoMoneda; }
    public void setMonto(float monto) { this.monto = monto; }
}