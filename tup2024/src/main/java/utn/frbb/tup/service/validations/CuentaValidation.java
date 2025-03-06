package utn.frbb.tup.service.validations;
import org.springframework.stereotype.Component;
import utn.frbb.tup.DTO.CuentaRequestDTO;
import utn.frbb.tup.exceptions.CantidadNegativaException;
import utn.frbb.tup.exceptions.CuentaNotFoundException;
import utn.frbb.tup.model.TipoCuenta;
import utn.frbb.tup.model.TipoMoneda;

@Component
public class CuentaValidation {

    //constructor
    public CuentaValidation() {}

    public boolean estaSoportada(TipoCuenta tipo, TipoMoneda moneda) {
        TipoCuenta ahorros = TipoCuenta.AHORROS;
        TipoCuenta corriente = TipoCuenta.CORRIENTE;
        TipoMoneda dolares = TipoMoneda.DOLARES;
        TipoMoneda pesos = TipoMoneda.PESOS;

        return (tipo == ahorros || tipo == corriente)
            && (moneda == dolares || moneda == pesos);
    }

    public void validarCuenta(CuentaRequestDTO cuenta) {
        if (cuenta == null) throw new CuentaNotFoundException("Error: No se ha ingresado una cuenta.");
        if (cuenta.getTipoCuenta() == null) throw new IllegalArgumentException("Error: no se ha ingresado un tipo de cuenta.");
        if (cuenta.getSaldo() < 0) throw new CantidadNegativaException("Error: se ha ingresado una cantidad negativa de dinero.");
        if (cuenta.getTipoMoneda() == null) throw new IllegalArgumentException("Error: no se ha ingresado un tipo de moneda.");
    }
}