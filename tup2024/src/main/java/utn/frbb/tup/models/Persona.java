package utn.frbb.tup.models;
import java.time.LocalDate;

public abstract class Persona {
    private String nombre;
    private String apellido;
    private long dni;
    private LocalDate fechaNacimiento;
    private TipoPersona tipoPersona;

    // constructor vacío para instanciar objetos al deserializar JSON
    public Persona() {}

    // setters
    public void setNombre (String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setDni(long dni) { this.dni = dni; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public void setTipoPersona(TipoPersona tipoPersona) { this.tipoPersona = tipoPersona; }

    // getters
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public long getDni() { return dni; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public TipoPersona getTipoPersona() { return tipoPersona; }
}