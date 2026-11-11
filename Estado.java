public enum Estado {
    RECIBIDO("Recibido"),
    EN_PREPARACION("En preparacion"),
    EN_REPARTO("En camino"),
    ENTREGADO("Entregado");

    private final String descripcion;

    Estado(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return this.descripcion;
    }
}
