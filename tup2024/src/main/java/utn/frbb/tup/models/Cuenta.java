package utn.frbb.tup.models;
import java.time.LocalDate;

public class Cuenta {
    private long nroAsociado;
    private TipoCuenta tipoCuenta;
    private LocalDate fechaCreacion;
    private float saldo;
    private Cliente titular;
    private TipoMoneda tipoMoneda;

    // constructor
    public Cuenta() {}
    //setters
    public void setNroAsociado(long nroAsociado) { this.nroAsociado = nroAsociado; }
    public void setTipoCuenta(TipoCuenta tipoCuenta) { this.tipoCuenta = tipoCuenta; }
    public void setFechaCreacion(LocalDate fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public void setSaldo(float saldo) { this.saldo = saldo; }
    public void setTitular(Cliente titular) { this.titular = titular; }
    public void setTipoMoneda(TipoMoneda tipoMoneda) { this.tipoMoneda = tipoMoneda; }
    // getters
    public long getNroAsociado () { return nroAsociado; }
    public TipoCuenta getTipoCuenta() { return tipoCuenta; }
    public LocalDate getFechaCreacion () { return fechaCreacion; }
    public float getSaldo () { return saldo; }
    public Cliente getTitular() { return titular; }
    public TipoMoneda getTipoMoneda() { return tipoMoneda; }
}