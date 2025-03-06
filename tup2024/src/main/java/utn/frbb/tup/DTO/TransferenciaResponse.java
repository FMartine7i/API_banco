package utn.frbb.tup.DTO;

public class TransferenciaResponse {
    private String estado;
    private String mensaje;

    public TransferenciaResponse(String estado, String mensaje) {
        this.estado = estado;
        this.mensaje = mensaje;
    }
    // getters
    public String getEstado() { return estado; }
    public String getMensaje() { return mensaje; }
    // setters
    public void setEstado(String estado) { this.estado = estado; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}