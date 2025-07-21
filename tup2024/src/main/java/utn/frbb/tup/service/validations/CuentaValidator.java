package utn.frbb.tup.service.validations;
import org.springframework.stereotype.Component;
import utn.frbb.tup.DTO.CuentaDTO;
import utn.frbb.tup.exceptions.CantidadNegativaException;
import utn.frbb.tup.exceptions.DatosNulosException;
import utn.frbb.tup.model.TipoCuenta;
import utn.frbb.tup.model.TipoMoneda;

@Component
public class CuentaValidator {

    //constructor
    public CuentaValidator() {}

    public boolean estaSoportada(TipoCuenta tipo, TipoMoneda moneda) {
        return (tipo == TipoCuenta.AHORROS || tipo == TipoCuenta.CORRIENTE) && (moneda == TipoMoneda.DOLARES || moneda == TipoMoneda.PESOS);
    }

    public void validarCuenta(CuentaDTO cuenta) {
        if (cuenta.getTipoCuenta() == null) throw new DatosNulosException("Error: tipo de cuenta nulo.");
        if (cuenta.getSaldo() < 0) throw new CantidadNegativaException("Error: el saldo no puede ser negativo.");
        if (cuenta.getTipoMoneda() == null) throw new DatosNulosException("Error: tipo de moneda nulo.");
    }
}