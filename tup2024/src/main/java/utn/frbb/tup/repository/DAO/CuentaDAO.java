package utn.frbb.tup.repository.DAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import utn.frbb.tup.exceptions.CuentaNotFoundException;
import utn.frbb.tup.model.Cliente;
import utn.frbb.tup.model.Cuenta;
import utn.frbb.tup.model.TipoCuenta;
import utn.frbb.tup.model.TipoMoneda;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CuentaDAO {
    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(CuentaDAO.class);
    private final String joinQuery = "SELECT c.*, cl.nombre, cl.apellido, cl.dni FROM CUENTAS c JOIN CLIENTES cl ON c.cliente_dni = cl.dni";

    @Autowired
    public CuentaDAO(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public List<Cuenta> getAllCuentasByDNI(long dni) {
        String sqlQuery = joinQuery + " WHERE cl.dni = ?";

        List<Cuenta> cuentas = jdbcTemplate.query(sqlQuery, this::mapRowToCuenta, dni);
        return cuentas.isEmpty() ? null : cuentas;
    }

    public Cuenta findByAsociado(long nroAsociado) {
        String sqlQuery = joinQuery +  " WHERE nro_asociado = ?";

        List<Cuenta> cuentas = jdbcTemplate.query(sqlQuery, this::mapRowToCuenta, nroAsociado);
        return cuentas.isEmpty() ? null : cuentas.get(0);
    }

    public void save(Cuenta cuenta) {
        String sqlQuery = "INSERT INTO CUENTAS (nro_asociado, tipo, creacion, saldo, tipo_moneda, cliente_dni) VALUES (?, ?, ?, ?, ?, ?)";
         try {
            jdbcTemplate.update(sqlQuery,
                cuenta.getNroAsociado(),
                cuenta.getTipoCuenta().getDescripcion(),
                cuenta.getFechaCreacion(),
                cuenta.getSaldo(),
                cuenta.getTipoMoneda().getDescripcion(),
                cuenta.getTitular().getDni()
            );
        } catch (DataAccessException e) {
             logger.error("Error: no se pudo guardar la cuenta.", e);
         }
    }

    public void update(Cuenta cuenta) {
        String sqlQuery = "UPDATE CUENTAS SET tipo = ?, tipo_moneda = ?, saldo = ? WHERE nro_asociado = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sqlQuery, cuenta.getTipoCuenta().getDescripcion(), cuenta.getTipoMoneda().getDescripcion(), cuenta.getSaldo(), cuenta.getNroAsociado());
            if (rowsAffected == 0) throw new CuentaNotFoundException("No se encontró la cuenta con el nro. asociado: " + cuenta.getNroAsociado());
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al actualizar la cuenta con el nro. asociado: " + cuenta.getNroAsociado());
        }
    }

    public void delete(long nroAsociado) {
        String sqlQuery = "DELETE FROM CUENTAS WHERE nro_asociado = ?";
        int rowsAffected = jdbcTemplate.update(sqlQuery, nroAsociado);
        logger.debug("CuentaService eliminada con éxito de la tabla.");
        if (rowsAffected == 0) throw new CuentaNotFoundException("Error: no se encontró la cuenta con el nro. asociado: " + nroAsociado);
    }

    private Cuenta mapRowToCuenta(ResultSet rs, int rowNum) throws SQLException {
        Cuenta cuenta = new Cuenta();
        cuenta.setNroAsociado(rs.getLong("nro_asociado"));
        cuenta.setTipoCuenta(TipoCuenta.fromString(rs.getString("tipo")));
        cuenta.setFechaCreacion(rs.getDate("creacion").toLocalDate());
        cuenta.setSaldo(rs.getFloat("saldo"));
        cuenta.setTipoMoneda(TipoMoneda.fromString(rs.getString("tipo_moneda")));
        //crear titular de la cuenta
        Cliente titular = new Cliente();
        titular.setNombre(rs.getString("nombre"));
        titular.setApellido(rs.getString("apellido"));
        titular.setDni(rs.getLong("dni"));
        cuenta.setTitular(titular);
        return cuenta;
    }
}