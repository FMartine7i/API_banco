package utn.frbb.tup.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.frbb.tup.DTO.MovimientoRequestDTO;
import utn.frbb.tup.model.Movimiento;
import utn.frbb.tup.model.TipoMovimiento;
import utn.frbb.tup.repository.DAO.MovimientoDAO;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MovimientoService {
    private final MovimientoDAO movimientoDAO;

    // constructor
    @Autowired
    public MovimientoService(MovimientoDAO movimientoDAO) {
        this.movimientoDAO = movimientoDAO;
    }

    // resto de las transacciones
    public Movimiento depositar(MovimientoRequestDTO movimientoDTO) {
        Movimiento movimiento = new Movimiento();
        movimiento.setOrigen(movimientoDTO.getNro());
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setDescripcion(movimientoDTO.getDescripcion());
        movimiento.setTipoMovimiento(TipoMovimiento.DEPOSITO);
        movimiento.setMonto(movimientoDTO.getMonto());
        movimientoDAO.save(movimiento);
        return movimiento;
    }

    public Movimiento retirar(MovimientoRequestDTO movimientoDTO) {
        Movimiento movimiento = new Movimiento();
        movimiento.setOrigen(movimientoDTO.getNro());
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setDescripcion(movimientoDTO.getDescripcion());
        movimiento.setTipoMovimiento(TipoMovimiento.DEPOSITO);
        movimiento.setMonto(movimientoDTO.getMonto());
        movimientoDAO.save(movimiento);
        return movimiento;
    }

    // ver historial de transacciones
    public Map<String, Object> obtenerMovimientos(long nro) {
        List<Movimiento> movimientos = movimientoDAO.findByNroCuenta(nro);
        Map<String, Object> response = new HashMap<>();
        response.put("origen", nro);
        response.put("transacciones", movimientos);
        return response;
    }
}
