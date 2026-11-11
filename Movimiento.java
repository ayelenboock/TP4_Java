import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Movimiento {

    private Estado estado;
    private Sucursal sucursal; // puede ser null (por ejemplo, cuando se prepara un paquete)
    private LocalDateTime fecha;

    public Movimiento(Estado estado, Sucursal sucursal) {
        this.estado = estado;
        this.sucursal = sucursal;
        this.fecha = LocalDateTime.now();
    }

    public Sucursal getSucursal() {
        return this.sucursal;
    }

    public Estado getEstado() {
        return this.estado;
    }

    public LocalDateTime getFecha() {
        return this.fecha;
    }

    //  quien quiera mostrarlo lo imprime 
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String lugar = (sucursal != null) ? " - Sucursal " + sucursal.getNombre() : "";
        return fecha.format(formatter) + " - " + estado.getDescripcion() + lugar;
    }
}
