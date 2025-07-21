package utn.frbb.tup.service.validations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utn.frbb.tup.exceptions.CantidadNegativaException;
import utn.frbb.tup.exceptions.CuentaNotFoundException;
import utn.frbb.tup.exceptions.DatosNulosException;
import utn.frbb.tup.exceptions.MonedaNoSoportadaException;
import utn.frbb.tup.model.Cuenta;
import utn.frbb.tup.model.TipoMoneda;
import utn.frbb.tup.repository.DAO.CuentaDAO;

@Component
public class MovimientoValidator {
    private final CuentaDAO cuentaDAO;

    @Autowired
    public MovimientoValidator(CuentaDAO cuentaDAO) {
        this.cuentaDAO = cuentaDAO;
    }

    public long validarMovimiento(String nroStr, float monto, String descripcion) {
        long nro = validarNro(nroStr);
        validarCuenta(cuentaDAO, nro);
        validarMonto(monto);
        validarDescripcion(descripcion);
        return nro;
    }

    public long validarNro(String nroStr) {
        if (nroStr == null || nroStr.isBlank()) throw new DatosNulosException("Error: nro. origen nulo.");
        try {
            long nro = Long.parseLong(nroStr);
            if (nro <= 0) throw new CantidadNegativaException("Error: el número debe ser mayor a 0.");
            return nro;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Error: formato no soportado.");
        }
    }

    public void validarCuenta(CuentaDAO cuentaDAO, long nro) {
        Cuenta cuenta = cuentaDAO.findByAsociado(nro);
        if (cuenta == null) throw new CuentaNotFoundException("Error: cuenta no encontrada.");
    }

    public void validarMonto(float monto) {
        if (monto < 0) throw new CantidadNegativaException("Error: monto negativo.");
    }

    public void validarDescripcion(String descripcion) {
        if (descripcion.length() > 255) throw new IllegalArgumentException("Error: la descripción supera los 255 caracteres.");
    }

    public float convertirMoneda(float monto, TipoMoneda origen, TipoMoneda destino) {
        if (origen == destino) return monto;
        // siempre devuelve el tipo de cambio de la cuenta de destino
        if (origen == TipoMoneda.DOLARES && destino == TipoMoneda.PESOS) return monto * 1000f;
        if (origen == TipoMoneda.PESOS && destino == TipoMoneda.DOLARES) return monto / 1000f;
        throw new MonedaNoSoportadaException("Error: conversión no soportada entre: " + origen + " y " + destino);
    }

    public void validarCuentasDistintas(long nroOrigen, long nroDestino) {
        if (nroOrigen == nroDestino) throw new IllegalArgumentException("Error: la cuenta de destino no puede ser igual a la cuenta de origen.");
    }
}