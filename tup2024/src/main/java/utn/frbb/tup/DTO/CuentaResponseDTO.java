package utn.frbb.tup.DTO;
import utn.frbb.tup.model.Cuenta;
import utn.frbb.tup.model.TipoCuenta;
import utn.frbb.tup.model.TipoMoneda;

public class CuentaResponseDTO implements CuentaDTO {
    private final long nroAsociado;
    private final TipoCuenta tipoCuenta;
    private final TipoMoneda tipoMoneda;
    private final float saldo;
    // obtenemos solo el nombre del cliente
    private final String nombreCliente;
    private final String apellidoCliente;

    // constructor
    public CuentaResponseDTO(Cuenta cuenta) {
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
}