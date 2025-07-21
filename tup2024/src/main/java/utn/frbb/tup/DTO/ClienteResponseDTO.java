package utn.frbb.tup.DTO;
import utn.frbb.tup.model.Cliente;
import java.time.LocalDate;

public class ClienteResponseDTO implements ClienteDTO {
    private final String nombre;
    private final String apellido;
    private final long dni;
    private final LocalDate fechaNacimiento;
    private final String banco;

    // constructor
    public ClienteResponseDTO(Cliente cliente) {
        this.nombre = cliente.getNombre();
        this.apellido = cliente.getApellido();
        this.dni = cliente.getDni();
        this.fechaNacimiento = cliente.getFechaNacimiento();
        this.banco = cliente.getBanco();
    }
    // getters
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public long getDni() { return dni; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public String getBanco() { return banco; }
}