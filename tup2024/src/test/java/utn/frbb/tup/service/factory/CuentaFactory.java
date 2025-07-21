package utn.frbb.tup.service.factory;

import utn.frbb.tup.model.Cliente;
import utn.frbb.tup.model.Cuenta;
import utn.frbb.tup.model.TipoCuenta;
import utn.frbb.tup.model.TipoMoneda;

import java.time.LocalDate;

public class CuentaFactory {
    public static Cuenta crearCuentaDefault() {
        Cuenta cuenta = new Cuenta();
        cuenta.setNroAsociado(12345);
        cuenta.setTipoCuenta(TipoCuenta.AHORROS);
        cuenta.setTipoMoneda(TipoMoneda.DOLARES);
        cuenta.setSaldo(20000);
        cuenta.setFechaCreacion(LocalDate.now());

        Cliente cliente = new Cliente();
        cliente.setDni(12345678L);
        cliente.setNombre("Billy");
        cliente.setApellido("Loomis");
        cuenta.setTitular(cliente);

        return cuenta;
    }
}