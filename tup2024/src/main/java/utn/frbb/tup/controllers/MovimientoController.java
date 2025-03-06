package utn.frbb.tup.controllers;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utn.frbb.tup.DTO.TransferenciaRequestDTO;
import utn.frbb.tup.DTO.TransferenciaResponse;
import utn.frbb.tup.exceptions.TransferenciaFallidaException;
import utn.frbb.tup.service.TransferenciaService;

@RestController
@RequestMapping("api/v1/movimientos")
public class MovimientoController {
    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);
    @Autowired
    private TransferenciaService transferenciaService;

    @PostMapping("/transferir")
    public ResponseEntity<TransferenciaResponse> realizarTransferencia(@Valid @RequestBody TransferenciaRequestDTO transferenciaRequest) {
        logger.debug("Emitiendo transferencia...");
        try {
            transferenciaService.tranferir(transferenciaRequest.getOrigen(), transferenciaRequest.getDescripcion(), transferenciaRequest.getDestino(), transferenciaRequest.getTipoMoneda(), transferenciaRequest.getMonto());
            return ResponseEntity.status(HttpStatus.CREATED).body(new TransferenciaResponse("EXITOSA", "Transferencia exitosa."));
        } catch (TransferenciaFallidaException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new TransferenciaResponse("FALLIDA", "Transferencia fallida."));
        }
    }
}
