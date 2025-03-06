package utn.frbb.tup.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.frbb.tup.DTO.CuentaDTO;
import utn.frbb.tup.DTO.CuentaRequestDTO;
import utn.frbb.tup.exceptions.CantidadNegativaException;
import utn.frbb.tup.exceptions.ClienteNotFoundException;
import utn.frbb.tup.exceptions.CuentaAlreadyExistsException;
import utn.frbb.tup.exceptions.CuentaNotFoundException;
import utn.frbb.tup.models.Cliente;
import utn.frbb.tup.models.Cuenta;
import utn.frbb.tup.repository.DAOs.ClienteDAO;
import utn.frbb.tup.repository.DAOs.CuentaDAO;
import utn.frbb.tup.service.validations.CuentaValidation;
import java.time.LocalDate;

@Service
public class CuentaService {
    private final CuentaDAO cuentaDAO;
    private final ClienteDAO clienteDAO;
    private final CuentaValidation cuentaValidation;
    // constructor
    @Autowired
    public CuentaService(CuentaDAO cuentaDAO, ClienteDAO clienteDAO, CuentaValidation cuentaValidation) {
        this.cuentaDAO = cuentaDAO;
        this.clienteDAO = clienteDAO;
        this.cuentaValidation = cuentaValidation;
    }

    public CuentaDTO agregarCuenta(CuentaRequestDTO cuentaRequest, long dni) {
        if (cuentaDAO.findByAsociado(cuentaRequest.getNroAsociado()) != null ) throw new CuentaAlreadyExistsException("Nro: " + cuentaRequest.getNroAsociado());

        cuentaValidation.validarCuenta(cuentaRequest);
        if (!cuentaValidation.estaSoportada(cuentaRequest.getTipoCuenta(), cuentaRequest.getTipoMoneda()))
            throw new IllegalArgumentException("Error: la cuenta " + cuentaRequest.getTipoCuenta() + " no está soportada por el sistema.");

        Cliente titular = clienteDAO.findByDni(dni);
        if (titular == null) throw new ClienteNotFoundException("Error: no se ha encontrado el cliente solicitado.");

        Cuenta cuenta = new Cuenta();
        cuenta.setNroAsociado(cuentaRequest.getNroAsociado());
        cuenta.setTipoCuenta(cuentaRequest.getTipoCuenta());
        cuenta.setFechaCreacion(LocalDate.now());
        cuenta.setTipoMoneda(cuentaRequest.getTipoMoneda());
        cuenta.setSaldo(cuentaRequest.getSaldo());
        cuenta.setTitular(titular);
        cuentaDAO.save(cuenta);
        return new CuentaDTO(cuenta);
    }

    public CuentaDTO buscarCuentaPorAsociado(long nroAsociado) {
        if (nroAsociado <= 0) throw new CantidadNegativaException("Error: no fue ingresado un número positivo.");

        Cuenta cuenta = cuentaDAO.findByAsociado(nroAsociado);
        if (cuenta == null) throw new CuentaNotFoundException("Error: cuenta no encontrada.");
        return new CuentaDTO(cuenta);
    }
}
