package utn.frbb.tup.service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import utn.frbb.tup.DTO.ClienteResponseDTO;
import utn.frbb.tup.DTO.ClienteRequestDTO;
import utn.frbb.tup.DTO.ClienteUpdateDTO;
import utn.frbb.tup.exceptions.ClienteAlreadyExistsException;
import utn.frbb.tup.exceptions.ClienteMenorDeEdadException;
import utn.frbb.tup.exceptions.ClienteNotFoundException;
import utn.frbb.tup.exceptions.DatosNulosException;
import utn.frbb.tup.model.Cliente;
import utn.frbb.tup.model.TipoPersona;
import utn.frbb.tup.repository.DAO.ClienteDAO;
import utn.frbb.tup.service.factory.DTOTestFactory;
import utn.frbb.tup.service.validations.ClienteValidator;
import utn.frbb.tup.service.validations.DniValidator;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {
    @Mock
    private ClienteDAO clienteDAO;
    @Mock
    private ClienteValidator clienteValidation;
    @Mock
    private DniValidator dniValidation;
    @InjectMocks
    private ClienteService clienteService;

    // Probar que, si el cliente ya fue agregado, se lanzará la excepción correspondiente
    @Test
    public void testClienteAlreadyExists() throws ClienteAlreadyExistsException {
        ClienteRequestDTO clienteDTO = DTOTestFactory.crearClienteDefault();
        // simula que el cliente YA existe
        when(clienteDAO.findByDni(clienteDTO.getDni())).thenReturn(new Cliente());
        // si el cliente YA existe, lanza la excepción
        assertThrows(ClienteAlreadyExistsException.class, () -> { clienteService.agregarCliente(clienteDTO); });
        // verifica que el cliente no se guarde
        verify(clienteDAO, never()).save(any(Cliente.class));
    }

    // Probar que, al ingresarse una edad menor a 18 años, se lanzará la excepción ClienteMenorDeEdadException
    @Test
    public void testClienteMenorDeEdad() {
        ClienteRequestDTO clienteDTO = new ClienteRequestDTO("Fede", "Martinetti", 22888773L, "Banco", LocalDate.now().minusYears(17), TipoPersona.FISICA);
        assertThrows(ClienteMenorDeEdadException.class, () -> { new ClienteValidator().validarCliente(clienteDTO); });
        // verificar que nunca se guarde el nuevo cliente
        verify(clienteDAO, never()).save(any(Cliente.class));
    }

    // Quiero probar que se lanzará la excepción DatosNulosClienteException si no se completa alguno de los campos, ej.: nombre
    @Test
    public void testNombreNulo() {
        ClienteRequestDTO clienteDTO = new ClienteRequestDTO(null, "Martinetti", 22888773L, "Banco", LocalDate.of(1998, 10, 5), TipoPersona.FISICA);
        assertThrows(DatosNulosException.class, () -> { new ClienteValidator().validarCliente(clienteDTO); });
        // verificamos que nunca se guarde el nuevo cliente
        verify(clienteDAO, never()).save(any(Cliente.class));
    }

    // queremos probar que se lance la excepción IllegalArgumentException si se ingresa un carácter extraño en el nombre
    @Test
    public void testNombreNoPermitido() {
        ClienteRequestDTO clienteDTO = new ClienteRequestDTO("F3de", "Martinetti", 22888773L, "Banco", LocalDate.of(1998, 10, 5), TipoPersona.FISICA);
        assertThrows(IllegalArgumentException.class, () -> { new ClienteValidator().validarCliente(clienteDTO); });
        // verificamos que nunca se guarde el nuevo cliente
        verify(clienteDAO, never()).save(any(Cliente.class));
    }

    // queremos probar que, al ingresarse una fecha futura, se lanzará la excepción IllegalArgumentException
    @Test
    public void testFechaFutura() {
        ClienteRequestDTO clienteDTO = new ClienteRequestDTO("Fede", "Martinetti", 22888773L, "Banco", LocalDate.of(2027, 10, 5), TipoPersona.FISICA);
        assertThrows(IllegalArgumentException.class, () -> { new ClienteValidator().validarCliente(clienteDTO); });
        // verificamos que nunca se guarde el nuevo cliente
        verify(clienteDAO, never()).save(any(Cliente.class));
    }

    // Probar que, en caso de tener todos los campos validados, el cliente será agregado con éxito
    @Test
    public void testClienteSuccess() {
        ClienteRequestDTO clienteDTO = DTOTestFactory.crearClienteDefault();
        // simula que el validador no tira ninguna excepción
        doNothing().when(clienteValidation).validarCliente(clienteDTO);
        // simula que el cliente NO existe
        when(clienteDAO.findByDni(clienteDTO.getDni())).thenReturn(null);
        // se agrega al cliente por medio del servicio
        clienteService.agregarCliente(clienteDTO);
        verify(clienteDAO, times(1)).save(any(Cliente.class));
    }

    // probar que, en caso de ingresar un número negativo en DNI, se lanzará la excepción IllegalArgumentException
    @Test
    public void testBuscarPorDniNegativo() throws IllegalArgumentException {
        assertThrows(IllegalArgumentException.class, ()-> { new DniValidator().validarDni("-1"); });
    }

    // asegurar que se lanza DatosNulosClienteException cuando el campo DNI está vacío
    @Test
    public void testCampoNuloDNI() throws DatosNulosException {
        assertThrows(DatosNulosException.class, () -> { new DniValidator().validarDni(null); });
    }

    // cuando se ingresen solo espacios, también deberá lanzarse DatosNulosClienteException
    @Test
    public void testDniConEspacios() {
        assertThrows(DatosNulosException.class, () -> { new DniValidator().validarDni("  "); });
    }

    // deberá lanzarse IllegalArgumentException cuando se ingresen letras
    @Test
    public void testDniConLetras() {
        assertThrows(IllegalArgumentException.class, () -> { new DniValidator().validarDni("abc"); });
    }

    // probar que se lanzará la excepción ClienteNotFoundException si no encuentra el cliente
    @Test
    public void ClienteNotFound() {
        long dni = 1878985L;
        // simulamos que la función de la validación devuelve el tipo esperado
        when(dniValidation.validarDni(String.valueOf(dni))).thenReturn(dni);
        // usamos el DAO para buscar el cliente según el dni y simulamos que no devuelve nada
        when(clienteDAO.findByDni(dni)).thenReturn(null);
        assertThrows(ClienteNotFoundException.class, () -> clienteService.buscarClientePorDNI(String.valueOf(dni)));
    }

    // asegurar que la búsqueda por DNI devuelva al usuario con el DNI ingresado como argumento
    @Test
    public void testBuscarPorDniSuccess() {
        long dni = 12345678L;
        Cliente cliente = new Cliente();
        cliente.setDni(dni);
        cliente.setNombre("Fede");
        cliente.setApellido("Martinetti");
        when(dniValidation.validarDni(String.valueOf(dni))).thenReturn(dni);
        when(clienteDAO.findByDni(dni)).thenReturn(cliente);

        ClienteResponseDTO clienteBuscado = clienteService.buscarClientePorDNI(String.valueOf(dni));
        assertNotNull(clienteBuscado);
        assertEquals(dni, clienteBuscado.getDni());
        assertEquals("Fede", clienteBuscado.getNombre());
        assertEquals("Martinetti", clienteBuscado.getApellido());
    }

    // como actualizarCliente() depende de la búsqueda por DNI y, de las validaciones de los campos ya cubiertas, se buscará asegurar que actualice con éxito y que no haga nada si el cliente buscado es nulo
    @Test
    public void testActualizarClienteNotFound() {
        String dniStr = "12345678";
        long dni = 12345678L;
        ClienteUpdateDTO clienteRequestDTO =new ClienteUpdateDTO(new Cliente());
        // simulamos que el validator del DNI devuelve el long esperado
        when(dniValidation.validarDni(dniStr)).thenReturn(dni);
        // simulamos que todos los campos para cliente han sido validados correctamente
        doNothing().when(clienteValidation).validarCliente(clienteRequestDTO);
        when(clienteDAO.findByDni(dni)).thenReturn(null);
        // excepción esperada
        assertThrows(ClienteNotFoundException.class, () -> { clienteService.actualizarCliente(dniStr, clienteRequestDTO); });
        // probamos que no actualiza si no encuentra un usuario previo
        verify(clienteDAO, never()).update(any());
    }

    // probar que, en caso de que se encuentre un cliente, se actualice con éxito
    @Test
    public void testActualizarClienteSuccess() {
        String dniStr = "12345678";
        long dni = 12345678L;
        Cliente cliente = new Cliente();
        cliente.setDni(dni);
        cliente.setNombre("Fede");
        cliente.setApellido("Martinetti");
        cliente.setBanco("Banco");
        ClienteUpdateDTO clienteRequestDTO = new ClienteUpdateDTO(cliente);
        // simulamos que el validator del DNI devuelve el long esperado
        when(dniValidation.validarDni(dniStr)).thenReturn(dni);
        // simulamos que todos los campos para cliente han sido validados correctamente
        doNothing().when(clienteValidation).validarCliente(clienteRequestDTO);
        when(clienteDAO.findByDni(dni)).thenReturn(cliente);
        // creo un objeto del tipo DTO que almacena los datos llevados al cliente a través del ClienteRequestDTO
        ClienteUpdateDTO result = clienteService.actualizarCliente(dniStr, clienteRequestDTO);
        // comparamos los datos esperados con los datos almacenados en el DTO
        assertEquals("Fede", result.getNombre());
        assertEquals("Martinetti", result.getApellido());
        assertEquals("Banco", result.getBanco());
        // verificamos que el cliente se actualice luego de haber corroborado que los DTO transfieren los datos correctamente
        verify(clienteDAO).update(cliente);
    }

    @Test
    public void testEliminarClienteNotFound() {
        String dniStr = "12345678";
        long dni = 12345678L;
        when(dniValidation.validarDni(dniStr)).thenReturn(dni);
        when(clienteDAO.findByDni(dni)).thenReturn(null);
        assertThrows(ClienteNotFoundException.class, () -> { clienteService.eliminarCliente(dniStr); });
        verify(clienteDAO, never()).delete(dni);
    }

    @Test
    public void testEliminarClienteSuccess() {
        String dniStr = "12345678";
        long dni = 12345678L;
        Cliente cliente = new Cliente();
        cliente.setDni(dni);
        when(dniValidation.validarDni(dniStr)).thenReturn(dni);
        when(clienteDAO.findByDni(dni)).thenReturn(cliente);
        assertEquals(dni, cliente.getDni());
        clienteService.eliminarCliente(dniStr);
        verify(clienteDAO).delete(dni);
    }
}