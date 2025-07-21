package utn.frbb.tup.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.frbb.tup.DTO.MovimientoRequestDTO;
import utn.frbb.tup.DTO.MovimientoResponseDTO;
import utn.frbb.tup.exceptions.SaldoInsuficienteException;
import utn.frbb.tup.model.Cuenta;
import utn.frbb.tup.model.Movimiento;
import utn.frbb.tup.model.TipoMoneda;
import utn.frbb.tup.model.TipoMovimiento;
import utn.frbb.tup.repository.DAO.CuentaDAO;
import utn.frbb.tup.repository.DAO.MovimientoDAO;
import utn.frbb.tup.service.validations.MovimientoValidator;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MovimientoService {
    private final MovimientoDAO movimientoDAO;
    private final CuentaDAO cuentaDAO;
    private final MovimientoValidator movimientoValidator;

    // constructor
    @Autowired
    public MovimientoService(MovimientoDAO movimientoDAO, CuentaDAO cuentaDAO, MovimientoValidator movimientoValidator) {
        this.movimientoDAO = movimientoDAO;
        this.cuentaDAO = cuentaDAO;
        this.movimientoValidator = movimientoValidator;
    }

    private Movimiento crearMovimiento(Cuenta origen, float monto, String descripcion, TipoMoneda tipoMoneda, TipoMovimiento tipoMovimiento) {
        Movimiento movimiento = new Movimiento();
        movimiento.setOrigen(origen.getNroAsociado());
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setDescripcion(descripcion);
        movimiento.setTipoMoneda(origen.getTipoMoneda());
        movimiento.setTipoMoneda(tipoMoneda);
        movimiento.setTipoMovimiento(tipoMovimiento);
        movimiento.setMonto(monto);
        return movimiento;
    }

    // resto de las transacciones
    public MovimientoResponseDTO depositar(String nroOrigen, MovimientoRequestDTO movimientoDTO) {
        long nro = movimientoValidator.validarMovimiento(nroOrigen, movimientoDTO.getMonto(), movimientoDTO.getDescripcion());
        Cuenta origen = cuentaDAO.findByAsociado(nro);
        Movimiento movimiento = crearMovimiento(origen, movimientoDTO.getMonto(), movimientoDTO.getDescripcion(), origen.getTipoMoneda(), TipoMovimiento.DEPOSITO);
        origen.setSaldo(origen.getSaldo() + movimiento.getMonto());
        movimientoDAO.save(movimiento);
        cuentaDAO.update(origen);
        return new MovimientoResponseDTO(movimiento);
    }

    public MovimientoResponseDTO retirar(String nroOrigen, MovimientoRequestDTO movimientoDTO) {
        long nro = movimientoValidator.validarMovimiento(nroOrigen, movimientoDTO.getMonto(), movimientoDTO.getDescripcion());
        Cuenta origen = cuentaDAO.findByAsociado(nro);
        Movimiento movimiento = crearMovimiento(origen, movimientoDTO.getMonto(), movimientoDTO.getDescripcion(), origen.getTipoMoneda(), TipoMovimiento.RETIRO);
        if (origen.getSaldo() < movimientoDTO.getMonto()) throw new SaldoInsuficienteException("Error: saldo insuficiente.");
        origen.setSaldo(origen.getSaldo() - movimiento.getMonto());
        movimientoDAO.save(movimiento);
        cuentaDAO.update(origen);
        return new MovimientoResponseDTO(movimiento);
    }

    // ver historial de transacciones
    public Map<String, Object> obtenerMovimientos(String nroStr) {
        long nro = movimientoValidator.validarNro(nroStr);
        List<Movimiento> movimientos = movimientoDAO.findByNroCuenta(nro);
        Map<String, Object> response = new HashMap<>();
        response.put("origen", nro);
        response.put("transacciones", movimientos);
        return response;
    }
}