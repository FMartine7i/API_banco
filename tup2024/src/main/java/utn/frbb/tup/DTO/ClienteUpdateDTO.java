package utn.frbb.tup.DTO;
import utn.frbb.tup.model.Cliente;
import utn.frbb.tup.model.TipoPersona;
import java.time.LocalDate;

public class ClienteUpdateDTO implements ClienteDTO {
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private TipoPersona tipoPersona;
    private String banco;

    public ClienteUpdateDTO() {}

    // constructor
    public ClienteUpdateDTO(Cliente cliente) {
        this.nombre = cliente.getNombre();
        this. apellido = cliente.getApellido();
        this.banco = cliente.getBanco();
        this.fechaNacimiento = cliente.getFechaNacimiento();
        this.tipoPersona = cliente.getTipoPersona();
    }

    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public TipoPersona getTipoPersona() { return tipoPersona; }
    public String getBanco() { return banco; }
}