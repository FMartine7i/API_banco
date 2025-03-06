package utn.frbb.tup.DTO;
import utn.frbb.tup.models.TipoPersona;
import java.time.LocalDate;

public class ClienteRequestDTO {
    private String nombre;
    private String apellido;
    private long dni;
    private LocalDate fechaNacimiento;
    private TipoPersona tipoPersona;
    private String banco;

    // constructor
    public ClienteRequestDTO() {}
    // getters
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public long getDni() { return dni; }
    public String getBanco() { return banco; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public TipoPersona getTipoPersona() { return tipoPersona; }
    // setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setDni(long dni) { this.dni = dni; }
    public void setBanco(String banco) { this.banco = banco; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public void setTipoPersona(TipoPersona tipoPersona) { this.tipoPersona = tipoPersona; }
}