package utn.frbb.tup.DTO;
import utn.frbb.tup.model.Movimiento;
import utn.frbb.tup.model.Transferencia;

public class TransferenciaRequestDTO {
    private String descripcion;
    private long destino;
    private float monto;

    public TransferenciaRequestDTO() {}

    public TransferenciaRequestDTO(Movimiento transferencia) {
        this.descripcion = transferencia.getDescripcion();
        this.destino = ((Transferencia)transferencia).getCuentaDestino();
        this.monto = transferencia.getMonto();
    }
    // getters
    public String getDescripcion() { return descripcion; }
    public long getDestino() { return destino; }
    public float getMonto() { return monto; }
}