package utn.frbb.tup.DTO;
import utn.frbb.tup.model.Cuenta;
import utn.frbb.tup.model.TipoCuenta;
import utn.frbb.tup.model.TipoMoneda;

public class CuentaUpdateDTO implements CuentaDTO {
    private TipoCuenta tipoCuenta;
    private TipoMoneda tipoMoneda;
    private float saldo;

    public CuentaUpdateDTO() {}

    public CuentaUpdateDTO(Cuenta cuenta) {
        this.tipoCuenta = cuenta.getTipoCuenta();
        this.tipoMoneda = cuenta.getTipoMoneda();
        this.saldo = cuenta.getSaldo();
    }
    // getters
    public TipoCuenta getTipoCuenta() { return tipoCuenta; }
    public TipoMoneda getTipoMoneda() { return tipoMoneda; }
    public float getSaldo() { return saldo; }
}