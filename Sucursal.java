import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Sucursal {

    private String nombreCiudad;
    private String codigoPostal;
    // Cola FIFO el primer envio que llega, es el primero que sale
    private Queue<Envio> colaDespacho;

    public Sucursal(String nombreCiudad, String codigoPostal) {
        this.nombreCiudad = nombreCiudad;
        this.codigoPostal = codigoPostal;
        this.colaDespacho = new LinkedList<>();
    }

    public String getNombre() {
        return this.nombreCiudad;
    }

    public String getCodigoPostal() {
        return this.codigoPostal;
    }

    public int cantidadPendientes() {
        return colaDespacho.size();
    }

    // El envio valida si puede ser recibido; si todo está bien, entra AL FINAL de la cola
    public void recibirEnvio(Envio envio) {
        if (envio == null) {
            throw new IllegalArgumentException("No se puede recibir un envio inexistente.");
        }
        envio.recibir(this);       
        colaDespacho.add(envio);
    }

    // Salen como máximo "cantidad" envios, respetando el orden de llegada.
    // Devuelve los envi­os que salieron .
    public ArrayList<Envio> despachar(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a despachar debe ser mayor que cero.");
        }
        if (colaDespacho.isEmpty()) {
            throw new IllegalStateException("No hay envios esperando para despachar en la sucursal " + nombreCiudad + ".");
        }
        ArrayList<Envio> despachados = new ArrayList<>();
        while (despachados.size() < cantidad && !colaDespacho.isEmpty()) {
            Envio envio = colaDespacho.peek(); // mira el primero sin sacarlo
            envio.despachar(this);             // si falla, el envio sigue en la cola
            colaDespacho.poll();               // recien ahora lo saca
            despachados.add(envio);
        }
        return despachados;
    }
}
