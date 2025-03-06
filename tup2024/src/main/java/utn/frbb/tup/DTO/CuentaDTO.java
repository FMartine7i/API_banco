package utn.frbb.tup.DTO;
import utn.frbb.tup.model.Cuenta;
import utn.frbb.tup.model.TipoCuenta;
import utn.frbb.tup.model.TipoMoneda;

public class CuentaDTO {
    private long nroAsociado;
    private TipoCuenta tipoCuenta;
    private TipoMoneda tipoMoneda;
    private float saldo;
    // obtenemos solo el nombre del cliente
    private String nombreCliente;
    private String apellidoCliente;

    // constructor
    public CuentaDTO(Cuenta cuenta) {
        this.nroAsociado = cuenta.getNroAsociado();
        this.tipoCuenta = cuenta.getTipoCuenta();
        this.tipoMoneda = cuenta.getTipoMoneda();
        this.saldo = cuenta.getSaldo();
        this.nombreCliente = cuenta.getTitular().getNombre();
        this.apellidoCliente = cuenta.getTitular().getApellido();
    }
    // getters
    public long getNroAsociado() { return nroAsociado; }
    public TipoCuenta getTipoCuenta() { return tipoCuenta; }
    public TipoMoneda getTipoMoneda() { return tipoMoneda; }
    public float getSaldo() { return saldo; }
    public String getNombreCliente() { return nombreCliente; }
    public String getApellidoCliente() { return apellidoCliente; }
    // setters
    public void setTipoCuenta(TipoCuenta tipoCuenta) { this.tipoCuenta = tipoCuenta; }
    public void setTipoMoneda(TipoMoneda tipoMoneda) { this.tipoMoneda = tipoMoneda; }
    public void setSaldo(float saldo) { this.saldo = saldo;}
    public void setClienteDni(String nombreCliente) { this.nombreCliente = nombreCliente; }
    public void setApellidoCliente(String apellidoCliente) { this.apellidoCliente = apellidoCliente; }
    public void setNroAsociado(long nroAsociado) { this.nroAsociado = nroAsociado; }
}