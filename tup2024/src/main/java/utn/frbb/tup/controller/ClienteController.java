package utn.frbb.tup.controller;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frbb.tup.service.ClienteService;
import utn.frbb.tup.DTO.ClienteDTO;
import utn.frbb.tup.DTO.ClienteRequestDTO;

@RestController
@RequestMapping("api/v1/clientes")
public class ClienteController {
    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);
    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteDTO> crearCliente(@Valid @RequestBody ClienteRequestDTO clienteRequest) {
        ClienteDTO nuevoCliente = clienteService.agregarCliente(clienteRequest);
        logger.debug("Agregando cliente...");
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
    }

    @GetMapping("/{dni}")
    public ResponseEntity<ClienteDTO> obtenerClientePorDni(@PathVariable long dni) {
        ClienteDTO cliente = clienteService.buscarClientePorDNI(dni);
        logger.debug("Obteniendo cliente...");
        return ResponseEntity.ok(cliente);
    }
}