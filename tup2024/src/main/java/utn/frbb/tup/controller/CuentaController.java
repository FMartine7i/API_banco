package utn.frbb.tup.controller;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frbb.tup.DTO.CuentaResponseDTO;
import utn.frbb.tup.DTO.CuentaRequestDTO;
import utn.frbb.tup.DTO.CuentaUpdateDTO;
import utn.frbb.tup.service.CuentaService;

import java.util.List;

@RestController
@RequestMapping("api/v1/cuentas")
public class CuentaController {
    private static final Logger logger = LoggerFactory.getLogger(CuentaController.class);
    @Autowired
    private CuentaService cuentaService;

    @PostMapping
    public ResponseEntity<CuentaResponseDTO> crearCuenta(@Valid @RequestBody CuentaRequestDTO cuentaRequest, String dni) {
        CuentaResponseDTO nuevaCuenta = cuentaService.agregarCuenta(cuentaRequest, dni);
        logger.debug("Agregando cuenta al cliente: {}", nuevaCuenta.getApellidoCliente());
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCuenta);
    }

    @GetMapping("/cuenta/{nro}")
    public ResponseEntity<CuentaResponseDTO> obtenerCuentaPorAsociado(@PathVariable String nro) {
        CuentaResponseDTO cuenta = cuentaService.buscarCuentaPorAsociado(nro);
        logger.debug("Obteniendo cuenta...");
        return ResponseEntity.ok(cuenta);
    }

    @GetMapping("/cliente/{dni}")
    public ResponseEntity<List<CuentaResponseDTO>> obtenerCuentasPorDni(@PathVariable String dni) {
        List<CuentaResponseDTO> cuentas = cuentaService.getCuentasPorDni(dni);
        logger.debug("Obteniendo cuentas...");
        return ResponseEntity.status(HttpStatus.OK).body(cuentas);
    }

    @PutMapping("/{nro}")
    public ResponseEntity<CuentaUpdateDTO> actualizarCuenta(@PathVariable String nro, @Valid @RequestBody CuentaUpdateDTO cuentaRequest) {
        CuentaUpdateDTO cuenta = cuentaService.actualizarCuenta(cuentaRequest, nro);
        return ResponseEntity.status(HttpStatus.OK).body(cuenta);
    }

    @DeleteMapping("/{nro}")
    public ResponseEntity<CuentaResponseDTO> eliminarCuenta(@PathVariable String nro) {
        cuentaService.eliminarCuenta(nro);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}