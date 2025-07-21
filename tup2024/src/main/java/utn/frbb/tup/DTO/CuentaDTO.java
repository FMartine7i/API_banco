package utn.frbb.tup.DTO;

import utn.frbb.tup.model.TipoCuenta;
import utn.frbb.tup.model.TipoMoneda;

public interface CuentaDTO {
    float getSaldo();
    TipoCuenta getTipoCuenta();
    TipoMoneda getTipoMoneda();
}
