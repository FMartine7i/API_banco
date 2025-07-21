package utn.frbb.tup.DTO;
import utn.frbb.tup.model.TipoPersona;
import java.time.LocalDate;

public interface ClienteDTO {
    String getNombre();
    String getApellido();
    LocalDate getFechaNacimiento();
    String getBanco();
}
