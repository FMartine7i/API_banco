package utn.frbb.tup.repository.DAOs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import utn.frbb.tup.models.Movimiento;
import utn.frbb.tup.models.TipoMovimiento;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Repository
public class MovimientoDAO {
    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(MovimientoDAO.class);

    @Autowired
    public MovimientoDAO(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public List<Movimiento> find() {
        String sqlQuery = "SELECT * FROM MOVIMIENTOS";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
            Movimiento movimiento = new Movimiento();
            movimiento.setOrigen(rs.getLong("origen"));
            movimiento.setFecha(rs.getDate("fecha").toLocalDate().atTime(LocalTime.now()));
            movimiento.setTipoMovimiento(TipoMovimiento.fromString(rs.getString("tipo")));
            movimiento.setDescripcion(rs.getString("descripcion"));
            movimiento.setMonto(rs.getFloat("monto"));
            return movimiento;
        });
    }

//    public List<Movimiento> findByNroCuenta() {
//        String sqlQuery = "SELECT m.*, c.";
//    }

    public void save(Movimiento movimiento) {
        String sqlQuery = "INSERT INTO MOVIMIENTOS (origen, fecha, tipo, descripcion, monto) VALUES (?, ?, ?, ?, ?)";
        try {
            int rowsAffected = jdbcTemplate.update(sqlQuery,
                movimiento.getOrigen(),
                movimiento.getFecha().atZone(ZoneId.systemDefault()).toInstant(),
                movimiento.getTipoMovimiento().getDescripcion(),
                movimiento.getDescripcion(),
                movimiento.getMonto()
            );
            logger.debug("Movimiento agregado con éxito a la BD.");
        } catch (DataAccessException e) {
            logger.error("Error: no se agregó el movimiento a la tabla.");
        }
    }
}