package utn.frbb.tup.service;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.frbb.tup.DTO.TransferenciaDTO;
import utn.frbb.tup.exceptions.CuentaNotFoundException;
import utn.frbb.tup.exceptions.MontoInsuficienteException;
import utn.frbb.tup.exceptions.TransferenciaFallidaException;
import utn.frbb.tup.model.Cuenta;
import utn.frbb.tup.model.Movimiento;
import utn.frbb.tup.model.TipoMoneda;
import utn.frbb.tup.model.Transferencia;
import utn.frbb.tup.repository.DAO.CuentaDAO;
import utn.frbb.tup.repository.DAO.MovimientoDAO;
import utn.frbb.tup.service.validations.TransferenciaValidation;
import java.time.LocalDateTime;

@Service
public class TransferenciaService {
    private final CuentaDAO cuentaDAO;
    private final MovimientoDAO movimientoDAO;
    private final BanelcoService banelcoService;
    private final TransferenciaValidation transferenciaValidation;

    @Autowired
    public TransferenciaService(CuentaDAO cuentaDAO, MovimientoDAO movimientoDAO, BanelcoService banelcoService, TransferenciaValidation transferenciaValidation) {
        this.cuentaDAO = cuentaDAO;
        this.movimientoDAO = movimientoDAO;
        this.banelcoService = banelcoService;
        this.transferenciaValidation = transferenciaValidation;
    }

    @Transactional
    public TransferenciaDTO transferir(long nroOrigen, String descripcion, long nroDestino, TipoMoneda moneda, float monto) {
        Cuenta cuentaOrigen = cuentaDAO.findByAsociado(nroOrigen);
        boolean esExterna = false;

        if (cuentaOrigen == null) throw new CuentaNotFoundException("Error: Cuenta origen no encontrada.");

        Cuenta cuentaDestino = cuentaDAO.findByAsociado(nroDestino);
        // si la cuenta destino no se encuentra, entonces corresponde a otro banco
        if (cuentaDestino == null) {
            esExterna = true;
            boolean transferenciaExitosa = banelcoService.transferir(nroOrigen, nroDestino, moneda, monto);
            if (!transferenciaExitosa) throw new TransferenciaFallidaException("");
        } else {
            // validar que ambas cuentas tengan la misma moneda
            transferenciaValidation.validarTipoMoneda(cuentaDestino, moneda);
            // se realiza la transferencia dentro del banco
            if (cuentaOrigen.getSaldo() < monto) throw new MontoInsuficienteException("Cuenta: " + cuentaOrigen.getNroAsociado());
            cuentaDestino.setSaldo(cuentaDestino.getSaldo() + monto);
            cuentaDAO.update(cuentaDestino);
        }
        cuentaOrigen.setSaldo(cuentaOrigen.getSaldo() - monto);
        cuentaDAO.update(cuentaOrigen);
        LocalDateTime fecha = LocalDateTime.now();
        long nroAsociado = (cuentaDestino != null) ? cuentaDestino.getNroAsociado() : nroDestino;
        Movimiento transferencia = new Transferencia(cuentaOrigen.getNroAsociado(), nroAsociado, fecha, moneda, descripcion, esExterna, monto);
        movimientoDAO.save(transferencia);
        return new TransferenciaDTO(transferencia);
    }
}