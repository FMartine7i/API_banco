package utn.frbb.tup.models;
import java.util.ArrayList;
import java.util.List;

public class Banco {
    private List<Cliente> clientes;

    public Banco () {
        clientes = new ArrayList<>();
    }
    // setter
    public void setCliente(List<Cliente> clientes) { clientes = clientes; }
    // getter
    public List<Cliente> getCliente() { return clientes; }
}