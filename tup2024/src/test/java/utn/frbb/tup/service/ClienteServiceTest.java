package utn.frbb.tup.service;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import utn.frbb.tup.DTO.ClienteDTO;
import utn.frbb.tup.DTO.ClienteRequestDTO;
import utn.frbb.tup.exceptions.ClienteAlreadyExistsException;
import utn.frbb.tup.exceptions.ClienteNotFoundException;
import utn.frbb.tup.model.Cliente;
import utn.frbb.tup.model.TipoPersona;
import utn.frbb.tup.repository.DAO.ClienteDAO;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClienteServiceTest {
    @Mock
    private ClienteDAO clienteDAO;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeAll
    public void setup() { MockitoAnnotations.openMocks(this); }

    @Test
    public void testClienteMenorEdad() {
        ClienteRequestDTO cliente = new ClienteRequestDTO();
        cliente.setFechaNacimiento(LocalDate.of(2020, 10, 10));
        assertThrows(ClienteAlreadyExistsException.class, ()-> clienteService.agregarCliente(cliente));
    }

    @Test
    public void testClienteSuccess() throws ClienteAlreadyExistsException{
        Cliente cliente = new Cliente();
        cliente.setFechaNacimiento(LocalDate.of(1998, 10, 5));
        cliente.setNombre("Fede");
        cliente.setApellido("Martinetti");
        cliente.setDni(22888773);
        cliente.setTipoPersona(TipoPersona.FISICA);
        verify(clienteDAO, times(1)).save(cliente);
    }

    @Test
    public void testClienteAlreadyExistsException() throws ClienteAlreadyExistsException {
        ClienteRequestDTO cliente = new ClienteRequestDTO();
        cliente.setFechaNacimiento(LocalDate.of(1998, 10, 5));
        cliente.setNombre("Fede");
        cliente.setApellido("Martinetti");
        cliente.setDni(22888773);
        cliente.setTipoPersona(TipoPersona.FISICA);

        when(clienteDAO.findByDni(cliente.getDni())).thenReturn(new Cliente());
        assertThrows(ClienteAlreadyExistsException.class, () -> clienteService.agregarCliente(cliente));
    }

    @Test
    public void testBuscarPorDniSuccess() throws ClienteNotFoundException {
        long dni = 12345678L;
        Cliente cliente = new Cliente();
        cliente.setDni(dni);
        when(clienteDAO.findByDni(dni)).thenReturn(cliente);

        ClienteDTO clienteBuscado = clienteService.buscarClientePorDNI(dni);
        assertEquals(clienteBuscado.getDni(), cliente.getDni());
    }


}