package utn.frbb.tup.service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import utn.frbb.tup.DTO.TransferenciaRequestDTO;
import utn.frbb.tup.exceptions.TransferenciaFallidaException;
import utn.frbb.tup.model.*;
import utn.frbb.tup.repository.DAO.CuentaDAO;
import utn.frbb.tup.repository.DAO.MovimientoDAO;
import utn.frbb.tup.service.validations.MovimientoValidator;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransferenciaServiceTest {
    @Mock
    private MovimientoDAO movimientoDAO;
    @Mock
    private CuentaDAO cuentaDAO;
    @Mock
    private MovimientoValidator movimientoValidator;
    @Mock
    private BanelcoService banelcoService;
    @InjectMocks
    private TransferenciaService transferenciaService;

    @Test
    public void testTransferencia_mismoOrigenYDestino() {
        String nroOrigenStr = "12345";
        long nro = 12345;
        float monto = 1000f;
        String descripcion = "Transferencia a la misma cuenta.";

        when(movimientoValidator.validarMovimiento(nroOrigenStr, monto, descripcion)).thenReturn(nro);
        doThrow(IllegalArgumentException.class).when(movimientoValidator).validarCuentasDistintas(nro, nro);
        assertThrows(IllegalArgumentException.class, () -> transferenciaService.transferir(nroOrigenStr, descripcion, nro, monto));
        verify(cuentaDAO, never()).update(any());
        verify(movimientoDAO, never()).save(any());
    }

    @Test
    public void testTransferencia_saldoInsuficiente() {
        String nroOrigenStr = "12345";
        long nro = 12345;
        float monto = 1000f;
        float saldo = 500f;
        String descripcion = "Transferencia a la misma cuenta.";

        Cuenta origen = new Cuenta();
        origen.setTitular(new Cliente());
        origen.setNroAsociado(nro);
        origen.setSaldo(saldo);

        when(movimientoValidator.validarMovimiento(nroOrigenStr, monto, descripcion)).thenReturn(nro);
        doThrow(IllegalArgumentException.class).when(movimientoValidator).validarCuentasDistintas(nro, nro);
        assertThrows(IllegalArgumentException.class, () -> transferenciaService.transferir(nroOrigenStr, descripcion, nro, monto));
        verify(cuentaDAO, never()).update(any());
        verify(movimientoDAO, never()).save(any());
    }

    @Test
    public void testTransferenciaSuccess_banelcoFalla() {
        String nroOrigenStr = "12345";
        long nroOrigen = 12345;
        long nroDestino = 54321;
        String descripcion = "Transferencia.";
        float monto = 1000f;
        TipoMoneda moneda = TipoMoneda.DOLARES;

        // cuenta origen
        Cuenta origen = new Cuenta();
        origen.setTitular(new Cliente());
        origen.setNroAsociado(nroOrigen);
        // el saldo de origen es superior al monto ingresado
        origen.setSaldo(20000f);
        origen.setTipoMoneda(moneda);

        // mocks
        when(movimientoValidator.validarMovimiento(nroOrigenStr, monto, descripcion)).thenReturn(nroOrigen);
        doNothing().when(movimientoValidator).validarCuentasDistintas(nroOrigen, nroDestino);
        when(cuentaDAO.findByAsociado(nroOrigen)).thenReturn(origen);
        when(cuentaDAO.findByAsociado(nroDestino)).thenReturn(null);
        when(banelcoService.transferir(nroOrigen, monto)).thenReturn(false);

        assertThrows(TransferenciaFallidaException.class, () -> transferenciaService.transferir(nroOrigenStr, descripcion, nroDestino, monto));
        verify(cuentaDAO, never()).update(origen);
        verify(movimientoDAO, never()).save(any(Transferencia.class));
    }

    @Test
    public void testTransferenciaSuccess_banelcoEncuentraCuenta() {
        String nroOrigenStr = "12345";
        long nroOrigen = 12345;
        long nroDestino = 54321;
        String descripcion = "Transferencia.";
        float monto = 1000f;
        TipoMoneda moneda = TipoMoneda.DOLARES;

        // cuenta origen
        Cuenta origen = new Cuenta();
        origen.setTitular(new Cliente());
        origen.setNroAsociado(nroOrigen);
        // el saldo de origen es superior al monto ingresado
        origen.setSaldo(20000f);
        origen.setTipoMoneda(moneda);

        // mocks
        when(movimientoValidator.validarMovimiento(nroOrigenStr, monto, descripcion)).thenReturn(nroOrigen);
        doNothing().when(movimientoValidator).validarCuentasDistintas(nroOrigen, nroDestino);
        when(cuentaDAO.findByAsociado(nroOrigen)).thenReturn(origen);
        when(banelcoService.transferir(nroOrigen, monto)).thenReturn(true);

        TransferenciaRequestDTO transferencia = transferenciaService.transferir(nroOrigenStr, descripcion, nroDestino, monto);

        assertNotNull(transferencia);
        assertEquals(monto, transferencia.getMonto());
        assertEquals(descripcion, transferencia.getDescripcion());
        verify(cuentaDAO).update(origen);
        verify(movimientoDAO).save(any(Transferencia.class));
    }

    @Test
    public void testTransferenciaSuccess_mismaMoneda() {
        String nroOrigenStr = "12345";
        long nroOrigen = 12345;
        long nroDestino = 54321;
        String descripcion = "Transferencia.";
        float monto = 1000f;
        TipoMoneda moneda = TipoMoneda.DOLARES;

        // cuenta origen
        Cuenta origen = new Cuenta();
        origen.setTitular(new Cliente());
        origen.setNroAsociado(nroOrigen);
        // el saldo de origen es superior al monto ingresado
        origen.setSaldo(20000f);
        origen.setTipoMoneda(moneda);

        // cuenta destino
        Cuenta destino = new Cuenta();
        destino.setNroAsociado(nroDestino);
        destino.setTitular(new Cliente());
        destino.setSaldo(1000f);
        // el destino tiene el mismo tipo de moneda que la cuenta de origen
        destino.setTipoMoneda(moneda);

        // mocks
        when(movimientoValidator.validarMovimiento(nroOrigenStr, monto, descripcion)).thenReturn(nroOrigen);
        doNothing().when(movimientoValidator).validarCuentasDistintas(nroOrigen, nroDestino);
        when(cuentaDAO.findByAsociado(nroOrigen)).thenReturn(origen);
        when(cuentaDAO.findByAsociado(nroDestino)).thenReturn(destino);

        TransferenciaRequestDTO transferencia = transferenciaService.transferir(nroOrigenStr, descripcion, nroDestino, monto);

        assertNotNull(transferencia);
        assertEquals(nroDestino, transferencia.getDestino());
        assertEquals(monto, transferencia.getMonto());
        assertEquals(descripcion, transferencia.getDescripcion());
        verify(cuentaDAO).update(destino);
        verify(cuentaDAO).update(origen);
        verify(movimientoDAO).save(any(Transferencia.class));
    }

    @Test
    public void testTransferenciaSuccess_distintaMoneda() {
        String nroOrigenStr = "12345";
        long nroOrigen = 12345;
        long nroDestino = 54321;
        String descripcion = "Transferencia.";
        float monto = 1000f;
        TipoMoneda moneda = TipoMoneda.DOLARES;

        // cuenta origen
        Cuenta origen = new Cuenta();
        origen.setTitular(new Cliente());
        origen.setNroAsociado(nroOrigen);
        // el saldo de origen es superior al monto ingresado
        origen.setSaldo(20000f);
        origen.setTipoMoneda(moneda);

        // cuenta destino
        Cuenta destino = new Cuenta();
        destino.setNroAsociado(nroDestino);
        destino.setTitular(new Cliente());
        destino.setSaldo(1000f);
        // el destino tiene otro tipo de moneda
        destino.setTipoMoneda(TipoMoneda.PESOS);

        // mocks
        when(movimientoValidator.validarMovimiento(nroOrigenStr, monto, descripcion)).thenReturn(nroOrigen);
        doNothing().when(movimientoValidator).validarCuentasDistintas(nroOrigen, nroDestino);
        when(movimientoValidator.convertirMoneda(monto, moneda, TipoMoneda.PESOS)).thenReturn(monto * 1000);
        when(cuentaDAO.findByAsociado(nroOrigen)).thenReturn(origen);
        when(cuentaDAO.findByAsociado(nroDestino)).thenReturn(destino);

        TransferenciaRequestDTO transferencia = transferenciaService.transferir(nroOrigenStr, descripcion, nroDestino, monto);

        assertNotNull(transferencia);
        assertEquals(nroDestino, transferencia.getDestino());
        assertEquals(monto * 1000, destino.getSaldo() - 1000);
        assertEquals(descripcion, transferencia.getDescripcion());
        verify(cuentaDAO).update(destino);
        verify(cuentaDAO).update(origen);
        verify(movimientoDAO).save(any(Transferencia.class));
    }
}