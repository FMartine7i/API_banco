package utn.frbb.tup.service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import utn.frbb.tup.DTO.CuentaResponseDTO;
import utn.frbb.tup.DTO.CuentaRequestDTO;
import utn.frbb.tup.DTO.CuentaUpdateDTO;
import utn.frbb.tup.exceptions.*;
import utn.frbb.tup.model.Cliente;
import utn.frbb.tup.model.Cuenta;
import utn.frbb.tup.model.TipoCuenta;
import utn.frbb.tup.model.TipoMoneda;
import utn.frbb.tup.repository.DAO.ClienteDAO;
import utn.frbb.tup.repository.DAO.CuentaDAO;
import utn.frbb.tup.service.factory.CuentaFactory;
import utn.frbb.tup.service.factory.DTOTestFactory;
import utn.frbb.tup.service.validations.CuentaValidator;
import utn.frbb.tup.service.validations.DniValidator;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CuentaServiceTest {
    @Mock
    private CuentaDAO cuentaDAO;
    @Mock
    private CuentaValidator cuentaValidation;
    @Mock
    private DniValidator dniValidation;
    @Mock
    private ClienteDAO clienteDAO;
    @InjectMocks
    private CuentaService cuentaService;

    // ====================== TESTS INSERT ============================
    @Test
    public void testCuentaAlreadyExists() {
        CuentaRequestDTO cuentaRequestDTO = DTOTestFactory.crearCuentaDefault();
        String dniStr = "12345678";
        when(cuentaDAO.findByAsociado(anyLong())).thenReturn(new Cuenta());
        assertThrows(CuentaAlreadyExistsException.class, () -> { cuentaService.agregarCuenta(cuentaRequestDTO, dniStr); });
        verify(cuentaDAO, never()).save(any(Cuenta.class));
    }

    // quiero probar que cuando no se encuentre al cliente se lanzará la excepción ClienteNotFound
    @Test
    public void testAgregarCuenta_ClienteNotFound() {
        String dniStr = "12345678";
        long dni = 1234578L;
        CuentaRequestDTO cuentaRequestDTO = DTOTestFactory.crearCuentaDefault();
        when(dniValidation.validarDni(dniStr)).thenReturn(dni);
        when(cuentaValidation.estaSoportada(cuentaRequestDTO.getTipoCuenta(), cuentaRequestDTO.getTipoMoneda())).thenReturn(Boolean.TRUE);
        when(clienteDAO.findByDni(dni)).thenReturn(null);
        assertThrows(ClienteNotFoundException.class, () -> { cuentaService.agregarCuenta(cuentaRequestDTO, dniStr); });
        verify(cuentaDAO, never()).save(any(Cuenta.class));
    }

    // quiero probar que cuando el tipo de cuenta no esté soportado se lanzará la excepción IllegalArgument
    @Test
    public void testAgregarCuentaNoSoportada() {
        String dniStr = "12345678";
        CuentaRequestDTO cuentaRequestDTO = DTOTestFactory.crearCuentaDefault();
        when(cuentaValidation.estaSoportada(cuentaRequestDTO.getTipoCuenta(), cuentaRequestDTO.getTipoMoneda())).thenReturn(Boolean.FALSE);
        assertThrows(IllegalArgumentException.class, () -> { cuentaService.agregarCuenta(cuentaRequestDTO, dniStr); });
        verify(cuentaDAO, never()).save(any(Cuenta.class));
    }

    // testear que cuando el tipo de cuenta sea nulo se lance la excepción DatosNulos
    @Test
    public void testAgregarCuenta_TipoCuentaNulo() {
        CuentaRequestDTO cuentaRequestDTO = new CuentaRequestDTO(null, TipoMoneda.DOLARES, 20000);
        assertThrows(DatosNulosException.class, () -> { new CuentaValidator().validarCuenta(cuentaRequestDTO); });
    }

    // testear que, si el validator lanza DatosNulosException, entonces el DAO no agregará la cuenta
    @Test
    public void testAgregarCuenta_throwTipoCuentaException() {
        CuentaRequestDTO cuentaRequestDTO = new CuentaRequestDTO(null, TipoMoneda.DOLARES, 20000);
        String dniStr = "12345678";
        doThrow(new DatosNulosException("Error: tipo de moneda nulo")).when(cuentaValidation).validarCuenta(cuentaRequestDTO);
        assertThrows(DatosNulosException.class, () -> { cuentaService.agregarCuenta(cuentaRequestDTO, dniStr); });
        verify(cuentaDAO, never()).save(any(Cuenta.class));
    }

    // testear que cuando el tipo de moneda sea nulo se lance la excepción DatosNulos
    @Test
    public void testAgregarCuenta_TipoMonedaNulo() {
        CuentaRequestDTO cuentaRequestDTO = new CuentaRequestDTO(TipoCuenta.AHORROS, null, 20000);
        assertThrows(DatosNulosException.class, () -> { new CuentaValidator().validarCuenta(cuentaRequestDTO); });
    }

    // testear que, si el validator lanza DatosNulosException, entonces el DAO no agregará la cuenta
    @Test
    public void testAgregarCuenta_throwMonedaException() {
        CuentaRequestDTO cuentaRequestDTO = new CuentaRequestDTO(TipoCuenta.AHORROS, null, 20000);
        String dniStr = "1234567";
        doThrow(new DatosNulosException("Error: tipo de cuenta nulo.")).when(cuentaValidation).validarCuenta(cuentaRequestDTO);
        assertThrows(DatosNulosException.class, () -> { cuentaService.agregarCuenta(cuentaRequestDTO, dniStr); });
        verify(cuentaDAO, never()).save(any(Cuenta.class));
    }

    // quiero probar que se lanzará CantidadNegativaException en el validator
    @Test
    public void testAgregarCuenta_saldoNegativoValidator() {
        CuentaRequestDTO cuentaRequestDTO = new CuentaRequestDTO(TipoCuenta.AHORROS, TipoMoneda.DOLARES, -1);
        assertThrows(CantidadNegativaException.class, () -> { new CuentaValidator().validarCuenta(cuentaRequestDTO); });
    }

    @Test
    public void testAgregarCuenta_saldoNegativo() {
        CuentaRequestDTO cuentaRequestDTO = new CuentaRequestDTO(TipoCuenta.AHORROS, TipoMoneda.DOLARES, -1);
        String dniStr = "1234567";
        doThrow(new CantidadNegativaException("Error: El saldo no puede ser negativo.")).when(cuentaValidation).validarCuenta(cuentaRequestDTO);
        assertThrows(CantidadNegativaException.class, () -> { cuentaService.agregarCuenta(cuentaRequestDTO, dniStr); });
        verify(cuentaDAO, never()).save(any(Cuenta.class));
    }

    // probar que cuando el DNI validator lance DatosNulosException, NumberFormatException o IllegalArgumentException no se insertará la nueva cuenta
    @Test
    public void testAgregarCuenta_clienteDNI_DatosNulosException() {
        CuentaRequestDTO cuentaRequestDTO = new CuentaRequestDTO(TipoCuenta.AHORROS, TipoMoneda.DOLARES, 20000);
        when(cuentaValidation.estaSoportada(any(), any())).thenReturn(true);
        doThrow(new DatosNulosException("Error: número de DNI nulo.")).when(dniValidation).validarDni(null);
        assertThrows(DatosNulosException.class, () -> { cuentaService.agregarCuenta(cuentaRequestDTO, null); });
        verify(cuentaDAO, never()).save(any((Cuenta.class)));
    }

    @Test
    public void testAgregarCuenta_clienteDNI_NumberFormatException() {
        CuentaRequestDTO cuentaRequestDTO = new CuentaRequestDTO(TipoCuenta.AHORROS, TipoMoneda.DOLARES, 20000);
        when(cuentaValidation.estaSoportada(any(), any())).thenReturn(true);
        doThrow(new NumberFormatException("Error: el DNI debe ser un número.")).when(dniValidation).validarDni("abc");
        assertThrows(NumberFormatException.class, () -> { cuentaService.agregarCuenta(cuentaRequestDTO, "abc"); });
        verify(cuentaDAO, never()).save(any((Cuenta.class)));
    }

    @Test
    public void testAgregarCuenta_clienteDNI_IllegalArgumentException() {
        CuentaRequestDTO cuentaRequestDTO = new CuentaRequestDTO(TipoCuenta.AHORROS, TipoMoneda.DOLARES, 20000);
        when(cuentaValidation.estaSoportada(any(), any())).thenReturn(true);
        doThrow(new IllegalArgumentException("Error: número de DNI no es positivo.")).when(dniValidation).validarDni("-1");
        assertThrows(IllegalArgumentException.class, () -> { cuentaService.agregarCuenta(cuentaRequestDTO, "-1"); });
        verify(cuentaDAO, never()).save(any((Cuenta.class)));
    }

    @Test
    public void testAgregarCuentaSuccess() {
        String dniStr = "12345678";
        long dni = 12345678L;
        Cliente titular = new Cliente();
        titular.setDni(dni);
        CuentaRequestDTO cuentaRequestDTO = DTOTestFactory.crearCuentaDefault();
        when(dniValidation.validarDni(dniStr)).thenReturn(dni);
        when(cuentaValidation.estaSoportada(cuentaRequestDTO.getTipoCuenta(), cuentaRequestDTO.getTipoMoneda())).thenReturn(Boolean.TRUE);
        when(clienteDAO.findByDni(dni)).thenReturn(titular);
        when(cuentaDAO.findByAsociado(anyLong())).thenReturn(null);
        CuentaResponseDTO cuentaDTO = cuentaService.agregarCuenta(cuentaRequestDTO, dniStr);
        assertNotNull(cuentaDTO);
        verify(cuentaDAO, times(1)).save(any(Cuenta.class));
    }

    // ====================== TESTS GET ALL ============================
    @Test
    public void testGetCuentas_dniInvalido() {
        String dniStr = "abc";
        when(dniValidation.validarDni(dniStr)).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () -> { cuentaService.getCuentasPorDni(dniStr); });
        verify(cuentaDAO, never()).getAllCuentasByDNI(anyLong());
    }

    @Test
    public void testGetCuentas_emptyList() {
        String dniStr = "12345678";
        long dni = 12345678L;
        when(dniValidation.validarDni(dniStr)).thenReturn(dni);
        when(cuentaDAO.getAllCuentasByDNI(dni)).thenReturn(Collections.emptyList());
        assertThrows(CuentaNotFoundException.class, () -> { cuentaService.getCuentasPorDni(dniStr); });
    }

    @Test
    public void testGetCuentasSuccess() {
        String dniStr = "12345678";
        long dni = 12345678L;
        List<Cuenta> cuentas = List.of(CuentaFactory.crearCuentaDefault());
        when(dniValidation.validarDni(dniStr)).thenReturn(dni);
        when(cuentaDAO.getAllCuentasByDNI(dni)).thenReturn(cuentas);
        List<CuentaResponseDTO> result = cuentaService.getCuentasPorDni(dniStr);
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(cuentaDAO).getAllCuentasByDNI(dni);
    }

    // ====================== TESTS FIND BY ============================
    @Test
    public void testFindByAsociado_nroAsociadoNulo() {
        assertThrows(DatosNulosException.class, ()-> cuentaService.buscarCuentaPorAsociado(null));
        verify(cuentaDAO, never()).findByAsociado(anyLong());
    }

    @Test
    public void testFindByAsociado_nroAsociadoNegativo() {
        String nroAsociadoStr = "-12345";
        assertThrows(CantidadNegativaException.class, ()-> cuentaService.buscarCuentaPorAsociado(nroAsociadoStr));
        verify(cuentaDAO, never()).findByAsociado(anyLong());
    }

    @Test
    public void testFindByAsociado_noEsNumero() {
        String nroAsociadoStr = "abc";
        assertThrows(IllegalArgumentException.class, ()-> cuentaService.buscarCuentaPorAsociado(nroAsociadoStr));
        verify(cuentaDAO, never()).findByAsociado(anyLong());
    }

    @Test
    public void testFindByAsociado_cuentaNoExiste() {
        String nroAsociadoStr = "12345";
        long nroAsociado = 12345;
        when(cuentaDAO.findByAsociado(nroAsociado)).thenReturn(null);
        assertThrows(CuentaNotFoundException.class, ()-> cuentaService.buscarCuentaPorAsociado(nroAsociadoStr));
    }

    @Test
    public void testFindByAsociadoSuccess() {
        String nroAsociadoStr = "12345";
        long nroAsociado = 12345;
        Cuenta cuenta = new Cuenta();
        Cliente cliente = new Cliente();
        cliente.setNombre("Stu");
        cuenta.setTitular(cliente);
        cuenta.setNroAsociado(nroAsociado);
        when(cuentaDAO.findByAsociado(nroAsociado)).thenReturn(cuenta);
        CuentaResponseDTO result = cuentaService.buscarCuentaPorAsociado(nroAsociadoStr);
        assertNotNull(result);
        assertEquals(nroAsociado, result.getNroAsociado());
        verify(cuentaDAO).findByAsociado(nroAsociado);
    }

    // ====================== TESTS UPDATE ============================
    @Test
    public void testActualizarCuenta_cuentaNotFound() {
        long nroAsociado = 12345;
        Cuenta cuenta = new Cuenta();
        cuenta.setNroAsociado(nroAsociado);
        CuentaUpdateDTO cuentaRequestDTO = new CuentaUpdateDTO(cuenta);
        String nroAsociadoStr = String.valueOf(nroAsociado);

        when(cuentaValidation.estaSoportada(any(), any())).thenReturn(true);
        doNothing().when(cuentaValidation).validarCuenta(cuentaRequestDTO);
        when(cuentaDAO.findByAsociado(nroAsociado)).thenReturn(null);

        assertThrows(CuentaNotFoundException.class, () -> cuentaService.actualizarCuenta(cuentaRequestDTO, nroAsociadoStr));
        verify(cuentaDAO, never()).update(any());
    }

    @Test
    public void testActualizarCuenta_tipoNoSoportado() {
        long nroAsociado = 12345;
        Cuenta cuenta = new Cuenta();
        cuenta.setNroAsociado(12345);
        CuentaUpdateDTO cuentaRequestDTO = new CuentaUpdateDTO(cuenta);
        String nroAsociadoStr = String.valueOf(nroAsociado);

        when(cuentaValidation.estaSoportada(any(), any())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> cuentaService.actualizarCuenta(cuentaRequestDTO, nroAsociadoStr));
        verify(cuentaDAO, never()).update(any());
    }

    @Test
    public void testActualizarCuenta_datosNulos() {
        long nroAsociado = 12345;
        Cuenta cuenta = new Cuenta();
        cuenta.setNroAsociado(12345);
        CuentaUpdateDTO cuentaRequestDTO = new CuentaUpdateDTO(cuenta);
        String nroAsociadoStr = String.valueOf(nroAsociado);

        doThrow(new DatosNulosException("Error: tipo de cuenta nulo.")).when(cuentaValidation).validarCuenta(cuentaRequestDTO);
        assertThrows(DatosNulosException.class, () -> cuentaService.actualizarCuenta(cuentaRequestDTO, nroAsociadoStr));
        verify(cuentaDAO, never()).update(any());
    }

    @Test
    public void testActualizarCuentaSuccess() {
        long nroAsociado = 12345;
        Cuenta cuenta = new Cuenta();
        cuenta.setNroAsociado(12345);
        String nroAsociadoStr = String.valueOf(nroAsociado);
        CuentaUpdateDTO cuentaRequestDTO = new CuentaUpdateDTO(cuenta);
        cuenta.setNroAsociado(nroAsociado);
        cuenta.setTitular(new Cliente());
        cuenta.setTipoCuenta(cuentaRequestDTO.getTipoCuenta());
        cuenta.setTipoMoneda(cuentaRequestDTO.getTipoMoneda());
        cuenta.setSaldo(cuentaRequestDTO.getSaldo());

        when(cuentaValidation.estaSoportada(any(), any())).thenReturn(true);
        doNothing().when(cuentaValidation).validarCuenta(cuentaRequestDTO);
        when(cuentaDAO.findByAsociado(nroAsociado)).thenReturn(cuenta);

        CuentaUpdateDTO result = cuentaService.actualizarCuenta(cuentaRequestDTO, nroAsociadoStr);

        assertEquals(cuentaRequestDTO.getTipoCuenta(), result.getTipoCuenta());
        assertEquals(cuentaRequestDTO.getTipoMoneda(), result.getTipoMoneda());
        assertEquals(cuentaRequestDTO.getSaldo(), result.getSaldo());
        verify(cuentaDAO).update(cuenta);
    }

    // ====================== TESTS DELETE ============================
    @Test
    public void testEliminarCliente_cuentaNotFound() {
        String nroAsociadoStr = "12345";
        long nroAsociado = 12345;
        when(cuentaDAO.findByAsociado(nroAsociado)).thenReturn(null);
        assertThrows(CuentaNotFoundException.class, () -> cuentaService.eliminarCuenta(nroAsociadoStr));
        verify(cuentaDAO, never()).delete(nroAsociado);
    }

    @Test
    public void testEliminarCliente_nroAsociadoNulo() {
        assertThrows(DatosNulosException.class, () -> cuentaService.eliminarCuenta(null));
        verify(cuentaDAO, never()).delete(anyLong());
    }

    @Test
    public void testEliminarCliente_nroAsociadoNoNumerico() {
        String nroAsociadoStr = "abc";
        assertThrows(IllegalArgumentException.class, () -> cuentaService.eliminarCuenta(nroAsociadoStr));
        verify(cuentaDAO, never()).delete(anyLong());
    }

    @Test
    public void testEliminarCliente_nroAsociadoNegativo() {
        String nroAsociadoStr = "-1";
        assertThrows(CantidadNegativaException.class, () -> cuentaService.eliminarCuenta(nroAsociadoStr));
        verify(cuentaDAO, never()).delete(anyLong());
    }

    @Test
    public void testEliminarClienteSuccess() {
        String nroAsociadoStr = "12345";
        long nroAsociado = 12345;
        Cuenta cuenta = new Cuenta();
        cuenta.setNroAsociado(nroAsociado);
        cuenta.setTitular(new Cliente());
        when(cuentaDAO.findByAsociado(nroAsociado)).thenReturn(cuenta);
        cuentaService.eliminarCuenta(nroAsociadoStr);
        verify(cuentaDAO).delete(nroAsociado);
    }
}