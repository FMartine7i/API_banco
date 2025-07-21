package utn.frbb.tup.service;
import utn.frbb.tup.DTO.ClienteUpdateDTO;
import utn.frbb.tup.exceptions.ClienteAlreadyExistsException;
import utn.frbb.tup.exceptions.ClienteNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.frbb.tup.repository.DAO.ClienteDAO;
import utn.frbb.tup.model.Cliente;
import utn.frbb.tup.DTO.ClienteResponseDTO;
import utn.frbb.tup.DTO.ClienteRequestDTO;
import utn.frbb.tup.service.validations.ClienteValidator;
import utn.frbb.tup.service.validations.DniValidator;
import java.time.LocalDate;

@Service
public class ClienteService {
    private final ClienteDAO clienteDAO;
    private final ClienteValidator clienteValidation;
    private final DniValidator dniValidation;
    // constructor
    @Autowired
    public ClienteService(ClienteDAO clienteDAO, ClienteValidator clienteValidation, DniValidator dniValidation) {
        this.clienteDAO = clienteDAO;
        this.clienteValidation = clienteValidation;
        this.dniValidation = dniValidation;
    }

    public ClienteResponseDTO agregarCliente(ClienteRequestDTO clienteRequest) throws ClienteAlreadyExistsException {
        Cliente nuevoCliente = clienteDAO.findByDni(clienteRequest.getDni());
        if (nuevoCliente == null) {
            clienteValidation.validarCliente(clienteRequest);
            clienteValidation.validarTipoPersona(clienteRequest.getTipoPersona());

            Cliente cliente = new Cliente();
            cliente.setNombre(clienteRequest.getNombre());
            cliente.setApellido(clienteRequest.getApellido());
            cliente.setDni(clienteRequest.getDni());
            cliente.setFechaNacimiento(clienteRequest.getFechaNacimiento());
            cliente.setTipoPersona(clienteRequest.getTipoPersona());
            cliente.setBanco(clienteRequest.getBanco());
            cliente.setFechaAlta(LocalDate.now());
            clienteDAO.save(cliente);
            return new ClienteResponseDTO(cliente);
        } else throw new ClienteAlreadyExistsException("Error: cliente con dni: " + clienteRequest.getDni() + " ya existe.");
    }

    private Cliente getClientePorDni(String dniStr) {
        long dni = dniValidation.validarDni(dniStr);
        Cliente cliente = clienteDAO.findByDni(dni);
        if (cliente == null) throw new ClienteNotFoundException("Error: el cliente no existe.");
        return  cliente;
    }

    public ClienteResponseDTO buscarClientePorDNI(String dniStr) {
        Cliente cliente = getClientePorDni(dniStr);
        return new ClienteResponseDTO(cliente);
    }

    public ClienteUpdateDTO actualizarCliente(String dniStr, ClienteUpdateDTO clienteRequestDTO) {
        clienteValidation.validarCliente(clienteRequestDTO);
        clienteValidation.validarTipoPersona(clienteRequestDTO.getTipoPersona());

        Cliente cliente = getClientePorDni(dniStr);
        cliente.setNombre(clienteRequestDTO.getNombre());
        cliente.setApellido(clienteRequestDTO.getApellido());
        cliente.setBanco(clienteRequestDTO.getBanco());
        cliente.setFechaNacimiento(clienteRequestDTO.getFechaNacimiento());
        cliente.setTipoPersona(clienteRequestDTO.getTipoPersona());
        clienteDAO.update(cliente);
        return new ClienteUpdateDTO(cliente);
    }

    public void eliminarCliente(String dniStr) {
        Cliente cliente = getClientePorDni(dniStr);
        clienteDAO.delete(cliente.getDni());
    }
}