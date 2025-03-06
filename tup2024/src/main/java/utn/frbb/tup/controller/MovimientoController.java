package utn.frbb.tup.controller;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frbb.tup.DTO.MovimientoRequestDTO;
import utn.frbb.tup.DTO.TransferenciaRequestDTO;
import utn.frbb.tup.DTO.TransferenciaResponse;
import utn.frbb.tup.exceptions.TransferenciaFallidaException;
import utn.frbb.tup.model.Movimiento;
import utn.frbb.tup.service.MovimientoService;
import utn.frbb.tup.service.TransferenciaService;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("api/v1/movimientos")
public class MovimientoController {
    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);
    private final TransferenciaService transferenciaService;
    private final MovimientoService movimientoService;

    @Autowired
    public MovimientoController(TransferenciaService transferenciaService, MovimientoService movimientoService) {
        this.transferenciaService = transferenciaService;
        this.movimientoService = movimientoService;
    }

    @GetMapping("/historial/{nro}")
    public ResponseEntity<Map<String, Object>> verHistorial(@PathVariable long nro) {
        Map<String, Object> movimientos = movimientoService.obtenerMovimientos(nro);
        logger.debug("Buscando historial...");
        return ResponseEntity.ok(movimientos);
    }

    @PostMapping("/transferir")
    public ResponseEntity<TransferenciaResponse> realizarTransferencia(@Valid @RequestBody TransferenciaRequestDTO transferenciaRequest) {
        logger.debug("Emitiendo transferencia...");
        try {
            transferenciaService.transferir(transferenciaRequest.getOrigen(), transferenciaRequest.getDescripcion(), transferenciaRequest.getDestino(), transferenciaRequest.getTipoMoneda(), transferenciaRequest.getMonto());
            return ResponseEntity.status(HttpStatus.CREATED).body(new TransferenciaResponse("EXITOSA", "Transferencia exitosa."));
        } catch (TransferenciaFallidaException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new TransferenciaResponse("FALLIDA", "Transferencia fallida."));
        }
    }

    @PostMapping("/depositar")
    public ResponseEntity<Movimiento> depositar(@Valid @RequestBody MovimientoRequestDTO movimientoRequest) {
        Movimiento movimiento = movimientoService.depositar(movimientoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(movimiento);
    }

    @PostMapping("/retirar")
    public ResponseEntity<Movimiento> retirar(@Valid @RequestBody MovimientoRequestDTO movimientoRequest) {
        Movimiento movimiento = movimientoService.retirar(movimientoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(movimiento);
    }
}
