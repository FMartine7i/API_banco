package utn.frbb.tup.models;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cliente extends Persona {
    private String banco;
    private LocalDate fechaAlta;
    // uso List en lugar de Set porque la BD se encarga de evitar duplicados
    private List<Cuenta> cuentas = new ArrayList<>();

    // constructor vacío
    public Cliente() {}

    // setters
    public void setBanco (String banco) { this.banco = banco; }
    public void setFechaAlta (LocalDate fechaAlta) { this.fechaAlta = fechaAlta; }
    public void setCuentas (List<Cuenta> cuentas) { this.cuentas = cuentas; }

    // getters
    public String getBanco() { return banco; }
    public LocalDate getFechaAlta() { return fechaAlta; }
    public List<Cuenta> getCuentas() { return cuentas; }
}