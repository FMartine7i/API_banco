package utn.frbb.tup.controller;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frbb.tup.DTO.MovimientoRequestDTO;
import utn.frbb.tup.DTO.MovimientoResponseDTO;
import utn.frbb.tup.DTO.TransferenciaRequestDTO;
import utn.frbb.tup.DTO.TransferenciaResponseDTO;
import utn.frbb.tup.exceptions.TransferenciaFallidaException;
import utn.frbb.tup.model.Movimiento;
import utn.frbb.tup.service.MovimientoService;
import utn.frbb.tup.service.TransferenciaService;

import java.util.Map;

@RestController
@RequestMapping("api/v1/cuentas/")
public class MovimientoController {
    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);
    private final TransferenciaService transferenciaService;
    private final MovimientoService movimientoService;

    @Autowired
    public MovimientoController(TransferenciaService transferenciaService, MovimientoService movimientoService) {
        this.transferenciaService = transferenciaService;
        this.movimientoService = movimientoService;
    }

    @GetMapping("{nro}/historial")
    public ResponseEntity<Map<String, Object>> verHistorial(@PathVariable String nro) {
        Map<String, Object> movimientos = movimientoService.obtenerMovimientos(nro);
        logger.debug("Buscando historial...");
        return ResponseEntity.ok(movimientos);
    }

    @PostMapping("{nro}/transferir")
    public ResponseEntity<TransferenciaResponseDTO> realizarTransferencia(@PathVariable String nro, @Valid @RequestBody TransferenciaRequestDTO transferenciaRequest) {
        logger.debug("Emitiendo transferencia...");
        try {
            transferenciaService.transferir(nro, transferenciaRequest.getDescripcion(), transferenciaRequest.getDestino(), transferenciaRequest.getMonto());
            return ResponseEntity.status(HttpStatus.CREATED).body(new TransferenciaResponseDTO("EXITOSA", "Transferencia exitosa."));
        } catch (TransferenciaFallidaException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new TransferenciaResponseDTO("FALLIDA", "Transferencia fallida."));
        }
    }

    @PostMapping("{nro}/depositar")
    public ResponseEntity<MovimientoResponseDTO> depositar(@PathVariable String nro, @Valid @RequestBody MovimientoRequestDTO movimientoRequest) {
        MovimientoResponseDTO movimiento = movimientoService.depositar(nro, movimientoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(movimiento);
    }

    @PostMapping("{nro}/retirar")
    public ResponseEntity<MovimientoResponseDTO> retirar(@PathVariable String nro, @Valid @RequestBody MovimientoRequestDTO movimientoRequest) {
        MovimientoResponseDTO movimiento = movimientoService.retirar(nro, movimientoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(movimiento);
    }
}