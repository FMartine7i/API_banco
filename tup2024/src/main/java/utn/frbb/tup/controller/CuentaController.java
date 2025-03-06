package utn.frbb.tup.controller;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frbb.tup.DTO.CuentaDTO;
import utn.frbb.tup.DTO.CuentaRequestDTO;
import utn.frbb.tup.service.CuentaService;

@RestController
@RequestMapping("api/v1/cuentas")
public class CuentaController {
    private static final Logger logger = LoggerFactory.getLogger(CuentaController.class);
    @Autowired
    private CuentaService cuentaService;

    @PostMapping
    public ResponseEntity<CuentaDTO> crearCuenta(@Valid @RequestBody CuentaRequestDTO cuentaRequest) {
        CuentaDTO nuevaCuenta = cuentaService.agregarCuenta(cuentaRequest, cuentaRequest.getClienteDni());
        logger.debug("Agregando cuenta al cliente: {}", cuentaRequest.getClienteDni());
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCuenta);
    }

    @GetMapping("/{nro}")
    public ResponseEntity<CuentaDTO> obtenerCuentaPorAsociado(@PathVariable long nro) {
        CuentaDTO cuenta = cuentaService.buscarCuentaPorAsociado(nro);
        logger.debug("Obteniendo cuenta...");
        return ResponseEntity.ok(cuenta);
    }
}