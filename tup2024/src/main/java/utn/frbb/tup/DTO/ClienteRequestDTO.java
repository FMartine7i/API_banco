package utn.frbb.tup.DTO;
import utn.frbb.tup.model.TipoPersona;
import java.time.LocalDate;

public class ClienteRequestDTO implements ClienteDTO {
    private final String nombre;
    private final String apellido;
    private final long dni;
    private final LocalDate fechaNacimiento;
    private final TipoPersona tipoPersona;
    private final String banco;

    // constructor
    public ClienteRequestDTO(String nombre, String apellido, long dni, String banco, LocalDate fechaNacimiento, TipoPersona tipoPersona) {
        this.nombre = nombre;
        this. apellido = apellido;
        this.dni = dni;
        this.banco = banco;
        this.fechaNacimiento = fechaNacimiento;
        this.tipoPersona = tipoPersona;
    }
    // getters
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public long getDni() { return dni; }
    public String getBanco() { return banco; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public TipoPersona getTipoPersona() { return tipoPersona; }
}