package utn.frbb.tup.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.frbb.tup.DTO.CuentaDTO;
import utn.frbb.tup.DTO.CuentaResponseDTO;
import utn.frbb.tup.DTO.CuentaRequestDTO;
import utn.frbb.tup.DTO.CuentaUpdateDTO;
import utn.frbb.tup.exceptions.*;
import utn.frbb.tup.model.Cliente;
import utn.frbb.tup.model.Cuenta;
import utn.frbb.tup.repository.DAO.ClienteDAO;
import utn.frbb.tup.repository.DAO.CuentaDAO;
import utn.frbb.tup.service.validations.CuentaValidator;
import utn.frbb.tup.service.validations.DniValidator;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CuentaService {
    private final CuentaDAO cuentaDAO;
    private final ClienteDAO clienteDAO;
    private final CuentaValidator cuentaValidation;
    private final DniValidator dniValidation;

    // constructor
    @Autowired
    public CuentaService(CuentaDAO cuentaDAO, ClienteDAO clienteDAO, CuentaValidator cuentaValidation, DniValidator dniValidation) {
        this.cuentaDAO = cuentaDAO;
        this.clienteDAO = clienteDAO;
        this.cuentaValidation = cuentaValidation;
        this.dniValidation = dniValidation;
    }

    private long getNroAsociado() {
        return (long) (Math.random() * 9000) + 1000;
    }

    private Cliente getClientePorDni(String dniStr) {
        long dni = dniValidation.validarDni(dniStr);
        Cliente titular = clienteDAO.findByDni(dni);
        if (titular == null) throw new ClienteNotFoundException("Error: No se ha encontrado el cliente solicitado.");
        return titular;
    }

    private Cuenta getCuentaPorNroAsociado(String nroAsociadoStr) {
        long nroAsociado = validarNroAsociado(nroAsociadoStr);
        Cuenta cuenta = cuentaDAO.findByAsociado(nroAsociado);
        if (cuenta == null) throw new CuentaNotFoundException("Error: la cuenta buscada no existe.");
        return cuenta;
    }

    private void validarCuenta(CuentaDTO cuentaDTO) {
        cuentaValidation.validarCuenta(cuentaDTO);
        if (!cuentaValidation.estaSoportada(cuentaDTO.getTipoCuenta(), cuentaDTO.getTipoMoneda()))
            throw new IllegalArgumentException("Error: la cuenta " + cuentaDTO.getTipoCuenta() + " no está soportada por el sistema.");
    }

    private long validarNroAsociado(String nroAsociadoStr) {
        if (nroAsociadoStr == null || nroAsociadoStr.isBlank()) throw new DatosNulosException("Error: El nro. de asociado no ha sido ingresado.");
        try {
            long nroAsociado = Long.parseLong(nroAsociadoStr);
            if (nroAsociado <= 0) throw new CantidadNegativaException("Error: no fue ingresado un número positivo.");
            return nroAsociado;
        }
        catch(NumberFormatException e) { throw new IllegalArgumentException("Error: el número asociado ingresado no es válido."); }
    }

    public CuentaResponseDTO agregarCuenta(CuentaRequestDTO cuentaRequest, String dni) {
        long nroAsociado = getNroAsociado();
        if (cuentaDAO.findByAsociado(nroAsociado) != null ) throw new CuentaAlreadyExistsException("Error: cuenta con nro: " + nroAsociado + " ya existe.");

        validarCuenta(cuentaRequest);

        Cliente titular = getClientePorDni(dni);
        Cuenta cuenta = new Cuenta();
        cuenta.setNroAsociado(nroAsociado);
        cuenta.setTipoCuenta(cuentaRequest.getTipoCuenta());
        cuenta.setFechaCreacion(LocalDate.now());
        cuenta.setTipoMoneda(cuentaRequest.getTipoMoneda());
        cuenta.setSaldo(cuentaRequest.getSaldo());
        cuenta.setTitular(titular);
        cuentaDAO.save(cuenta);
        return new CuentaResponseDTO(cuenta);
    }

    public List<CuentaResponseDTO> getCuentasPorDni(String dniStr){
        long dni = dniValidation.validarDni(dniStr);
        List<Cuenta> cuentas = cuentaDAO.getAllCuentasByDNI(dni);
        if (cuentas == null || cuentas.isEmpty()) throw new CuentaNotFoundException("Error: no se han encontrado cuentas para el usuario con DNI " + dniStr);
        return cuentas.stream().map(CuentaResponseDTO::new).collect(Collectors.toList());
    }

    public CuentaResponseDTO buscarCuentaPorAsociado(String nroAsociadoStr) {
        Cuenta cuenta = getCuentaPorNroAsociado(nroAsociadoStr);
        return new CuentaResponseDTO(cuenta);
    }

    public CuentaUpdateDTO actualizarCuenta(CuentaUpdateDTO cuentaRequestDTO, String nroAsociadoStr) {
        validarCuenta(cuentaRequestDTO);
        Cuenta cuenta = getCuentaPorNroAsociado(nroAsociadoStr);
        cuenta.setTipoCuenta(cuentaRequestDTO.getTipoCuenta());
        cuenta.setTipoMoneda(cuentaRequestDTO.getTipoMoneda());
        cuenta.setSaldo(cuentaRequestDTO.getSaldo());
        cuentaDAO.update(cuenta);
        return new CuentaUpdateDTO(cuenta);
    }

    public void eliminarCuenta(String nroAsociadoStr) {
        Cuenta cuenta = getCuentaPorNroAsociado(nroAsociadoStr);
        cuentaDAO.delete(cuenta.getNroAsociado());
    }
}