package utn.frbb.tup.DTO;
import utn.frbb.tup.models.Cliente;

public class ClienteDTO {
    private String nombre;
    private String apellido;
    private long dni;
    private String banco;

    // contructor
    public ClienteDTO(Cliente cliente) {
        this.nombre = cliente.getNombre();
        this.apellido = cliente.getApellido();
        this.dni = cliente.getDni();
        this.banco = cliente.getBanco();
    }
    // getters
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public long getDni() { return dni; }
    public String getBanco() { return banco; }
    // setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setDni(long dni) { this.dni = dni; }
    public void setBanco(String banco) { this.banco = banco; }
}