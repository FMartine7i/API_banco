package utn.frbb.tup.service;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.frbb.tup.DTO.TransferenciaDTO;
import utn.frbb.tup.DTO.TransferenciaRequestDTO;
import utn.frbb.tup.exceptions.SaldoInsuficienteException;
import utn.frbb.tup.exceptions.TransferenciaFallidaException;
import utn.frbb.tup.model.Cuenta;
import utn.frbb.tup.model.Movimiento;
import utn.frbb.tup.model.TipoMoneda;
import utn.frbb.tup.model.Transferencia;
import utn.frbb.tup.repository.DAO.CuentaDAO;
import utn.frbb.tup.repository.DAO.MovimientoDAO;
import utn.frbb.tup.service.validations.MovimientoValidator;
import java.time.LocalDateTime;

@Service
public class TransferenciaService {
    private final CuentaDAO cuentaDAO;
    private final MovimientoDAO movimientoDAO;
    private final BanelcoService banelcoService;
    private final MovimientoValidator transferenciaValidation;

    @Autowired
    public TransferenciaService(CuentaDAO cuentaDAO, MovimientoDAO movimientoDAO, BanelcoService banelcoService, MovimientoValidator transferenciaValidation) {
        this.cuentaDAO = cuentaDAO;
        this.movimientoDAO = movimientoDAO;
        this.banelcoService = banelcoService;
        this.transferenciaValidation = transferenciaValidation;
    }

    @Transactional
    public TransferenciaRequestDTO transferir(String nroOrigenStr, String descripcion, long nroDestino, float monto) {
        long nroOrigen = transferenciaValidation.validarMovimiento(nroOrigenStr, monto, descripcion);
        transferenciaValidation.validarCuentasDistintas(nroOrigen, nroDestino);
        Cuenta cuentaOrigen = cuentaDAO.findByAsociado(nroOrigen);
        Cuenta cuentaDestino = cuentaDAO.findByAsociado(nroDestino);
        TipoMoneda moneda = TipoMoneda.DOLARES;
        float nuevoMonto = 0;
        boolean esExterna = false;

        if (cuentaOrigen.getSaldo() < monto) throw new SaldoInsuficienteException("Cuenta: " + cuentaOrigen.getNroAsociado());
        // si la cuenta destino no se encuentra, entonces corresponde a otro banco
        if (cuentaDestino == null) {
            esExterna = true;
            boolean transferenciaExitosa = banelcoService.transferir(nroOrigen, monto);
            if (!transferenciaExitosa) throw new TransferenciaFallidaException("Error: la transferencia externa ha fallado.");
        } else {
            // validar que ambas cuentas tengan la misma moneda
            if (!cuentaDestino.getTipoMoneda().equals(moneda)) {
                nuevoMonto = transferenciaValidation.convertirMoneda(monto, cuentaOrigen.getTipoMoneda(), cuentaDestino.getTipoMoneda());
                moneda = cuentaDestino.getTipoMoneda();
            }
            // se realiza la transferencia dentro del banco
            cuentaDestino.setSaldo(cuentaDestino.getSaldo() + nuevoMonto);
            cuentaDAO.update(cuentaDestino);
        }
        cuentaOrigen.setSaldo(cuentaOrigen.getSaldo() - monto);
        cuentaDAO.update(cuentaOrigen);
        LocalDateTime fecha = LocalDateTime.now();
        long nroAsociado = (cuentaDestino != null) ? cuentaDestino.getNroAsociado() : nroDestino;
        Movimiento transferencia = new Transferencia(cuentaOrigen.getNroAsociado(), nroAsociado, fecha, moneda, descripcion, esExterna, monto);
        movimientoDAO.save(transferencia);
        return new TransferenciaRequestDTO(transferencia);
    }
}