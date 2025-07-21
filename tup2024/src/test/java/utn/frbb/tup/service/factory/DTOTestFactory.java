package utn.frbb.tup.service.factory;
import utn.frbb.tup.DTO.ClienteRequestDTO;
import utn.frbb.tup.DTO.CuentaRequestDTO;
import utn.frbb.tup.model.TipoCuenta;
import utn.frbb.tup.model.TipoMoneda;
import utn.frbb.tup.model.TipoPersona;
import java.time.LocalDate;

public class DTOTestFactory {
    public static ClienteRequestDTO crearClienteDefault() {
        return new ClienteRequestDTO("Fede", "Martinetti", 12345678, "Banco", LocalDate.of(1998, 5, 10), TipoPersona.FISICA);
    }

    public static CuentaRequestDTO crearCuentaDefault() {
        return new CuentaRequestDTO(TipoCuenta.AHORROS, TipoMoneda.DOLARES, 20000);
    }
}