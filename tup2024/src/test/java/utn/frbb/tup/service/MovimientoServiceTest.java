package utn.frbb.tup.service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import utn.frbb.tup.DTO.MovimientoRequestDTO;
import utn.frbb.tup.DTO.MovimientoResponseDTO;
import utn.frbb.tup.exceptions.CantidadNegativaException;
import utn.frbb.tup.exceptions.CuentaNotFoundException;
import utn.frbb.tup.exceptions.DatosNulosException;
import utn.frbb.tup.model.Cuenta;
import utn.frbb.tup.model.Movimiento;
import utn.frbb.tup.model.TipoMovimiento;
import utn.frbb.tup.repository.DAO.CuentaDAO;
import utn.frbb.tup.repository.DAO.MovimientoDAO;
import utn.frbb.tup.service.validations.MovimientoValidator;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovimientoServiceTest {
    @Mock
    private MovimientoDAO movimientoDAO;
    @Mock
    private CuentaDAO cuentaDAO;
    @Mock
    private MovimientoValidator movimientoValidator;
    @InjectMocks
    private MovimientoService movimientoService;

    // ---------------------- TESTS VALIDATORS ----------------------------
    @Test
    public void testRetirar_validatorCuentaNotFound() {
        long nro = 12345;
        assertThrows(CuentaNotFoundException.class, () -> new MovimientoValidator(cuentaDAO).validarCuenta(cuentaDAO, nro));
    }

    @Test
    public void testRetirar_validatorNro_origenNulo() {
        assertThrows(DatosNulosException.class, () -> new MovimientoValidator(cuentaDAO).validarNro(null));
    }

    @Test
    public void testRetirar_validatorNro_formatoNoValido() {
        String nroStr = "abc";
        assertThrows(IllegalArgumentException.class, () -> new MovimientoValidator(cuentaDAO).validarNro(nroStr));
    }

    // ====================== TESTS RETIRO ============================
    @Test
    public void testRetirar_cuentaNotFound() {
        String nroStr = "12345";
        float monto = 50000f;
        String descripcion = "Extracción.";
        MovimientoRequestDTO movimientoRequestDTO = new MovimientoRequestDTO(descripcion, monto);

        when(movimientoValidator.validarMovimiento(nroStr, monto, descripcion)).thenThrow(CuentaNotFoundException.class);
        assertThrows(CuentaNotFoundException.class, () -> movimientoService.retirar(nroStr, movimientoRequestDTO));
        verify(movimientoDAO, never()).save(any(Movimiento.class));
        verify(cuentaDAO, never()).update(any());
    }

    @Test
    public void testRetirar_montoNegativo() {
        String nroStr = "12345";
        float monto = -50000f;
        String descripcion = "Extracción.";
        MovimientoRequestDTO movimientoRequestDTO = new MovimientoRequestDTO(descripcion, monto);

        when(movimientoValidator.validarMovimiento(nroStr, monto, descripcion)).thenThrow(CantidadNegativaException.class);
        assertThrows(CantidadNegativaException.class, () -> movimientoService.retirar(nroStr, movimientoRequestDTO));
        verify(movimientoDAO, never()).save(any(Movimiento.class));
        verify(cuentaDAO, never()).update(any());
    }

    @Test
    public void testRetirar_nroOrigenNulo() {
        float monto = 50000f;
        String descripcion = "Extracción.";
        MovimientoRequestDTO movimientoRequestDTO = new MovimientoRequestDTO(descripcion, monto);

        when(movimientoValidator.validarMovimiento(null, monto, descripcion)).thenThrow(DatosNulosException.class);
        assertThrows(DatosNulosException.class, () -> movimientoService.retirar(null, movimientoRequestDTO));
        verify(movimientoDAO, never()).save(any(Movimiento.class));
        verify(cuentaDAO, never()).update(any());
    }

    @Test
    public void testRetirar_nroOrigenNoValido() {
        String nroStr = "abc";
        float monto = 50000f;
        String descripcion = "Extracción.";
        MovimientoRequestDTO movimientoRequestDTO = new MovimientoRequestDTO(descripcion, monto);

        when(movimientoValidator.validarMovimiento(nroStr, monto, descripcion)).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () -> movimientoService.retirar(nroStr, movimientoRequestDTO));
        verify(movimientoDAO, never()).save(any(Movimiento.class));
        verify(cuentaDAO, never()).update(any());
    }

    @Test
    public void testRetirarSuccess() {
        String nroStr = "12345";
        long nro = 12345;
        float monto = 50000f;
        String descripcion = "Extracción.";

        Cuenta cuenta = new Cuenta();
        cuenta.setNroAsociado(nro);
        cuenta.setSaldo(100000f);

        MovimientoRequestDTO movimientoRequestDTO = new MovimientoRequestDTO(descripcion, monto);
        when(movimientoValidator.validarMovimiento(nroStr, monto, descripcion)).thenReturn(nro);
        when(cuentaDAO.findByAsociado(nro)).thenReturn(cuenta);

        MovimientoResponseDTO movimiento = movimientoService.retirar(nroStr, movimientoRequestDTO);
        assertNotNull(movimiento);
        assertEquals(TipoMovimiento.RETIRO, movimiento.getTipoMovimiento());
        assertEquals(nro, movimiento.getOrigen());
        assertEquals(monto, movimiento.getMonto());
        verify(movimientoDAO).save(any(Movimiento.class));
        verify(cuentaDAO).update(cuenta);
    }

    // ====================== TESTS DEPOSITO ============================
    @Test
    public void testDepositar_cuentaNotFound() {
        String nroStr = "12345";
        float monto = 50000f;
        String descripcion = "Depósito.";
        MovimientoRequestDTO movimientoRequestDTO = new MovimientoRequestDTO(descripcion, monto);

        when(movimientoValidator.validarMovimiento(nroStr, monto, descripcion)).thenThrow(CuentaNotFoundException.class);
        assertThrows(CuentaNotFoundException.class, () -> movimientoService.depositar(nroStr, movimientoRequestDTO));
        verify(movimientoDAO, never()).save(any(Movimiento.class));
        verify(cuentaDAO, never()).update(any());
    }

    @Test
    public void testDepositar_montoNegativo() {
        String nroStr = "12345";
        float monto = -50000f;
        String descripcion = "´Depósito.";
        MovimientoRequestDTO movimientoRequestDTO = new MovimientoRequestDTO(descripcion, monto);

        when(movimientoValidator.validarMovimiento(nroStr, monto, descripcion)).thenThrow(CantidadNegativaException.class);
        assertThrows(CantidadNegativaException.class, () -> movimientoService.depositar(nroStr, movimientoRequestDTO));
        verify(movimientoDAO, never()).save(any(Movimiento.class));
        verify(cuentaDAO, never()).update(any());
    }

    @Test
    public void testDepositar_nroOrigenNulo() {
        float monto = 50000f;
        String descripcion = "Depósito.";
        MovimientoRequestDTO movimientoRequestDTO = new MovimientoRequestDTO(descripcion, monto);

        when(movimientoValidator.validarMovimiento(null, monto, descripcion)).thenThrow(DatosNulosException.class);
        assertThrows(DatosNulosException.class, () -> movimientoService.depositar(null, movimientoRequestDTO));
        verify(movimientoDAO, never()).save(any(Movimiento.class));
        verify(cuentaDAO, never()).update(any());
    }

    @Test
    public void testDepositar_nroOrigenNoValido() {
        String nroStr = "abc";
        float monto = 50000f;
        String descripcion = "Depósito.";
        MovimientoRequestDTO movimientoRequestDTO = new MovimientoRequestDTO(descripcion, monto);

        when(movimientoValidator.validarMovimiento(nroStr, monto, descripcion)).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () -> movimientoService.depositar(nroStr, movimientoRequestDTO));
        verify(movimientoDAO, never()).save(any(Movimiento.class));
        verify(cuentaDAO, never()).update(any());
    }

    @Test
    public void testDepositarSuccess() {
        String nroStr = "12345";
        long nro = 12345;
        float monto = 50000f;
        String descripcion = "Depósito.";

        Cuenta cuenta = new Cuenta();
        cuenta.setNroAsociado(nro);
        cuenta.setSaldo(100000f);

        MovimientoRequestDTO movimientoRequestDTO = new MovimientoRequestDTO(descripcion, monto);
        when(movimientoValidator.validarMovimiento(nroStr, monto, descripcion)).thenReturn(nro);
        when(cuentaDAO.findByAsociado(nro)).thenReturn(cuenta);

        MovimientoResponseDTO movimiento = movimientoService.depositar(nroStr, movimientoRequestDTO);
        assertNotNull(movimiento);
        assertEquals(TipoMovimiento.DEPOSITO, movimiento.getTipoMovimiento());
        assertEquals(nro, movimiento.getOrigen());
        assertEquals(monto, movimiento.getMonto());
        verify(movimientoDAO).save(any(Movimiento.class));
        verify(cuentaDAO).update(cuenta);
    }

    // ====================== TESTS HISTORIAL ============================
    @Test
    public void testObtenerMovimientos_emptyList() {
        String nroStr = "12345";
        long nro = 12345;

        when(movimientoValidator.validarNro(nroStr)).thenReturn(nro);
        when(movimientoDAO.findByNroCuenta(nro)).thenReturn(Collections.emptyList());

        Map<String, Object> response = movimientoService.obtenerMovimientos(nroStr);
        assertNotNull(response);
        assertEquals(nro, response.get("origen"));
        assertTrue(((List<?>) response.get("transacciones")).isEmpty());
    }

    @Test
    public void testObtenerMovimientos_nroNoValido() {
        String nroStr = "abc";
        when(movimientoValidator.validarNro(nroStr)).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () -> movimientoService.obtenerMovimientos(nroStr));
        verify(movimientoDAO, never()).findByNroCuenta(anyLong());
    }

    @Test
    public void testObtenerMovimientos_nroNulo() {
        when(movimientoValidator.validarNro(null)).thenThrow(DatosNulosException.class);
        assertThrows(DatosNulosException.class, () -> movimientoService.obtenerMovimientos(null));
        verify(movimientoDAO, never()).findByNroCuenta(anyLong());
    }

    @Test
    public void testObtenerMovimientosSuccess() {
        String nroStr = "12345";
        long nro = 12345;
        List<Movimiento> movimientosMock = List.of(new Movimiento(), new Movimiento());

        when(movimientoValidator.validarNro(nroStr)).thenReturn(nro);
        when(movimientoDAO.findByNroCuenta(nro)).thenReturn(movimientosMock);

        Map<String, Object> response = movimientoService.obtenerMovimientos(nroStr);
        assertNotNull(response);
        assertEquals(nro, response.get("origen"));
        assertEquals(movimientosMock, response.get("transacciones"));
        verify(movimientoDAO).findByNroCuenta(nro);
    }
}