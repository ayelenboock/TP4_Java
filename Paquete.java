public class Paquete {

    // Atributos privados para proteger la información
    private int numGuia;
    private double peso;
    private String destino;
    private String descripcion; 
    private Estado estado;

    // Constructor 1 datos mínimos (llama al de abajo poniendo "Sin descripción")
    public Paquete(int numGuia, double peso, String destino) {
        this(numGuia, peso, destino, "Sin descripción"); 
    }

    // Constructor 2 sobrecargado con información completa
    public Paquete(int numGuia, double peso, String destino, String descripcion) {
        if (peso <= 0) {
            throw new IllegalArgumentException("El peso del paquete debe ser mayor que cero.");
        }
        this.numGuia = numGuia;
        this.peso = peso;
        this.destino = destino;
        this.descripcion = (descripcion != null && !descripcion.isEmpty()) ? descripcion : "Sin descripción";
        this.estado = Estado.RECIBIDO; // estado inicial
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    private boolean estaRecibido() {
        return this.estado == Estado.RECIBIDO;
    }

    private boolean estaEnPreparacion() {
        return this.estado == Estado.EN_PREPARACION;
    }

    private boolean estaEnReparto() {
        return this.estado == Estado.EN_REPARTO;
    }

    // ---- el paquete decide si puede cambiar ----
    public void iniciarPreparacion() {
        if (!estaRecibido()) {
            throw new IllegalStateException("El paquete " + numGuia + " ya fue preparado, no se puede preparar de nuevo.");
        }
        this.estado = Estado.EN_PREPARACION;
    }

    public void salirAReparto() {
        if (!estaEnPreparacion()) {
            throw new IllegalStateException("El paquete " + numGuia + " todavia no está preparado para salir.");
        }
        this.estado = Estado.EN_REPARTO;
    }

    // si viene en camino, vuelve a quedar "en preparación"
    public void recibir() {
        if (estaEnReparto()) {
            this.estado = Estado.EN_PREPARACION;
        } else if (!estaEnPreparacion()) {
            throw new IllegalStateException("El paquete " + numGuia + " no puede recibirse en una sucursal (estado actual: "
                    + estado.getDescripcion() + ").");
        }
    }

    public void registrarEntrega() {
        if (!estaEnReparto()) {
            throw new IllegalStateException("El paquete " + numGuia + " no se puede entregar porque todavia no salió a reparto.");
        }
        this.estado = Estado.ENTREGADO;
    }

    public double getPeso() {
        return this.peso;
    }

    public String getDestino() {
        return this.destino;
    }

    public int getNumGuia() {
        return this.numGuia;
    }

    public Estado getEstado() {
        return this.estado;
    }
}
