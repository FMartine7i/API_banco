package utn.frbb.tup.service.validations;
import org.springframework.stereotype.Component;
import utn.frbb.tup.exceptions.MonedaNoSoportadaException;
import utn.frbb.tup.model.Cuenta;
import utn.frbb.tup.model.TipoMoneda;

@Component
public class TransferenciaValidation {
    public void validarTipoMoneda(Cuenta cuenta, TipoMoneda moneda) {
        if (!cuenta.getTipoMoneda().equals(moneda)) throw new MonedaNoSoportadaException("Error: la moneda no coincide con el tipo de moneda en la cuenta de origen.");
    }
}