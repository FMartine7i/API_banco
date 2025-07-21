package utn.frbb.tup.DTO;

public class TransferenciaResponseDTO {
    private final String estado;
    private final String mensaje;

    public TransferenciaResponseDTO(String estado, String mensaje) {
        this.estado = estado;
        this.mensaje = mensaje;
    }
    // getters
    public String getEstado() { return estado; }
    public String getMensaje() { return mensaje; }
}