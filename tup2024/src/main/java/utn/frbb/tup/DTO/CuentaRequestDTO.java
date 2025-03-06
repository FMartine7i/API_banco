package utn.frbb.tup.DTO;
import utn.frbb.tup.model.TipoCuenta;
import utn.frbb.tup.model.TipoMoneda;

public class CuentaRequestDTO {
    private long nroAsociado;
    private TipoCuenta tipoCuenta;
    private TipoMoneda tipoMoneda;
    private float saldo;
    // obtenemos solo el DNI del cliente
    private long clienteDni;

    // constructor
    public CuentaRequestDTO() {}
    // getters
    public long getNroAsociado() { return nroAsociado; }
    public TipoCuenta getTipoCuenta() { return tipoCuenta; }
    public TipoMoneda getTipoMoneda() { return tipoMoneda; }
    public float getSaldo() { return saldo; }
    public long getClienteDni() { return clienteDni; }
    // setters
    public void setNroAsociado(long nroAsociado) { this.nroAsociado = nroAsociado; }
    public void setTipoCuenta(TipoCuenta tipoCuenta) { this.tipoCuenta = tipoCuenta; }
    public void setTipoMoneda(TipoMoneda tipoMoneda) { this.tipoMoneda = tipoMoneda; }
    public void setSaldo(float saldo) { this.saldo = saldo;}
    public void setClienteDni(long clienteDni) { this.clienteDni = clienteDni; }
}