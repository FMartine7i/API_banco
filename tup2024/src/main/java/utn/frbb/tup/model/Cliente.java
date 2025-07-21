package utn.frbb.tup.model;
import java.time.LocalDate;

public class Cliente extends Persona {
    private String banco;
    private LocalDate fechaAlta;

    // constructor vacío
    public Cliente() {}
    // setters
    public void setBanco (String banco) { this.banco = banco; }
    public void setFechaAlta (LocalDate fechaAlta) { this.fechaAlta = fechaAlta; }
    // getters
    public String getBanco() { return banco; }
    public LocalDate getFechaAlta() { return fechaAlta; }
}