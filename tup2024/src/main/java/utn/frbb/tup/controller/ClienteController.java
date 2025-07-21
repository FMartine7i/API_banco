package utn.frbb.tup.controller;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frbb.tup.DTO.ClienteUpdateDTO;
import utn.frbb.tup.service.ClienteService;
import utn.frbb.tup.DTO.ClienteResponseDTO;
import utn.frbb.tup.DTO.ClienteRequestDTO;

@RestController
@RequestMapping("api/v1/clientes")
public class ClienteController {
    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);
    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> crearCliente(@Valid @RequestBody ClienteRequestDTO clienteRequest) {
        ClienteResponseDTO nuevoCliente = clienteService.agregarCliente(clienteRequest);
        logger.debug("Agregando cliente...");
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
    }

    @GetMapping("/{dni}")
    public ResponseEntity<ClienteResponseDTO> obtenerClientePorDni(@PathVariable String dni) {
        ClienteResponseDTO cliente = clienteService.buscarClientePorDNI(dni);
        logger.debug("Obteniendo cliente...");
        return ResponseEntity.ok(cliente);
    }

    @PutMapping("/{dni}")
    public ResponseEntity<ClienteUpdateDTO> editarCliente(@PathVariable String dni, @Valid @RequestBody ClienteUpdateDTO clienteUpdateDTO) {
        ClienteUpdateDTO cliente = clienteService.actualizarCliente(dni, clienteUpdateDTO);
        logger.debug("Actualizando cliente...");
        return ResponseEntity.ok(cliente);
    }
}