package utn.frbb.tup.repository.DAO;
import utn.frbb.tup.exceptions.ClienteNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import utn.frbb.tup.model.Cliente;
import utn.frbb.tup.model.TipoPersona;

@Repository
public class ClienteDAO {
    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(ClienteDAO.class);

    @Autowired
    public ClienteDAO(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public List<Cliente> getClientes() {
        String sqlQuery = "SELECT * FROM CLIENTES";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
            Cliente cliente = new Cliente();
            cliente.setNombre(rs.getString("nombre"));
            cliente.setApellido(rs.getString("apellido"));
            cliente.setDni(rs.getLong("dni"));
            cliente.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
            cliente.setTipoPersona(TipoPersona.valueOf(rs.getString("tipo")));
            cliente.setBanco(rs.getString("banco"));
            cliente.setFechaAlta(rs.getDate("alta").toLocalDate());
            logger.debug("Clientes encontrados con éxito en la BD.");
            return cliente;
        });
    }

    public Cliente findByDni(long dni) {
        String sqlQuery = "SELECT * FROM CLIENTES WHERE dni = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> {
                Cliente cliente = new Cliente();
                cliente.setNombre(rs.getString("nombre"));
                cliente.setApellido(rs.getString("apellido"));
                cliente.setDni(rs.getLong("dni"));
                cliente.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
                cliente.setTipoPersona(TipoPersona.fromString(rs.getString("tipo")));
                cliente.setBanco(rs.getString("banco"));
                cliente.setFechaAlta(rs.getDate("alta").toLocalDate());
                logger.debug("Cliente encontrado con éxito en la BD.");
                return cliente;
            }, dni);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void save(Cliente cliente) {
        String sqlQuery = "INSERT INTO CLIENTES (nombre, apellido, dni, fecha_nacimiento, tipo, banco, alta) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sqlQuery,
                    cliente.getNombre(),
                    cliente.getApellido(),
                    cliente.getDni(),
                    cliente.getFechaNacimiento(),
                    cliente.getTipoPersona().getDescripcion(),
                    cliente.getBanco(),
                    cliente.getFechaAlta()
            );
            logger.debug("Cliente agregado con éxito a la BD.");
        } catch (DataAccessException e) {
            logger.error("Error: No se pudo guardar al cliente");
        }
    }

    public void update(Cliente cliente) {
        String sqlQuery = "UPDATE CLIENTES SET nombre = ?, apellido = ?, fecha_nacimiento = ?, tipo = ?, banco = ? WHERE dni = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sqlQuery,
                cliente.getNombre(),
                cliente.getApellido(),
                cliente.getFechaNacimiento(),
                cliente.getTipoPersona().getDescripcion(),
                cliente.getBanco(),
                cliente.getDni()
            );
            logger.debug("Cliente agregado con éxito a la tabla.");
            if (rowsAffected == 0) throw new ClienteNotFoundException("No se encontró al cliente con ID: " + cliente.getDni());
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al actualizar el cliente con DNI: " + cliente.getDni(), e);
        }
    }

    public void delete(long dni) {
        String sqlQuery = "DELETE FROM CLIENTES WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sqlQuery, dni);
        logger.debug("Cliente eliminado con éxito de la tabla.");
        if (rowsAffected == 0) throw new ClienteNotFoundException("Error: no se encontró al cliente con el DNI: " + dni);
    }
}