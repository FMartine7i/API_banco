package utn.frbb.tup.service;
import utn.frbb.tup.exceptions.ClienteAlreadyExistsException;
import utn.frbb.tup.exceptions.ClienteNotFoundException;
import utn.frbb.tup.model.TipoCuenta;
import utn.frbb.tup.model.TipoMoneda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.frbb.tup.repository.DAO.ClienteDAO;
import utn.frbb.tup.model.Cliente;
import utn.frbb.tup.DTO.ClienteDTO;
import utn.frbb.tup.DTO.ClienteRequestDTO;
import utn.frbb.tup.service.validations.ClienteValidation;
import java.time.LocalDate;

@Service
public class ClienteService {
    private final ClienteDAO clienteDAO;

    // constructor
    @Autowired
    public ClienteService(ClienteDAO clienteDAO) { this.clienteDAO = clienteDAO; }

    public ClienteDTO agregarCliente(ClienteRequestDTO clienteRequest) {
        try {
            clienteDAO.findByDni(clienteRequest.getDni());
            throw new ClienteAlreadyExistsException("Error: cliente con dni: " + clienteRequest.getDni() + " ya existe.");
        } catch (ClienteNotFoundException e) {
            ClienteValidation clienteValidation = new ClienteValidation();
            clienteValidation.validarCliente(clienteRequest);

            Cliente cliente = new Cliente();
            cliente.setNombre(clienteRequest.getNombre());
            cliente.setApellido(clienteRequest.getApellido());
            cliente.setDni(clienteRequest.getDni());
            cliente.setFechaNacimiento(clienteRequest.getFechaNacimiento());
            cliente.setTipoPersona(clienteRequest.getTipoPersona());
            cliente.setBanco(clienteRequest.getBanco());
            cliente.setFechaAlta(LocalDate.now());
            clienteDAO.save(cliente);
            return new ClienteDTO(cliente);
        }
    }

    public ClienteDTO buscarClientePorDNI(long dni) {
        if (dni <= 0) throw new IllegalArgumentException("Error: no fue ingresado un número positivo.");

        Cliente cliente = clienteDAO.findByDni(dni);
        if (cliente == null) throw new IllegalArgumentException("Error: el cliente no existe.");
        return new ClienteDTO(cliente);
    }

    public void actualizarCliente(long dni) {
        if (dni <= 0) throw new IllegalArgumentException("Error: No fue ingresado un número positivo.");

    }

    public boolean tieneCuenta(long dni, TipoCuenta tipoCuenta, TipoMoneda tipoMoneda) {
        Cliente cliente = clienteDAO.findByDni(dni);
        return cliente.getCuentas().stream().anyMatch(c -> c.getTipoCuenta().equals(tipoCuenta) && c.getTipoMoneda().equals(tipoMoneda));
    }
}