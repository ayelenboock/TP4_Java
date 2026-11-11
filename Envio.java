import java.util.ArrayList;

public abstract class Envio {

    protected ArrayList<Paquete> paquetes;
    protected int diasTranscurridos;
    protected boolean codigoEscaneado;
    protected Cliente cliente;
    private int codigoSeguimiento; // identifica al envio  (clave del HashMap en RegistroCentral)
    private ArrayList<Movimiento> historial;

    public Envio(Cliente cliente, int codigoSeguimiento) {
        if (cliente == null) {
            throw new IllegalArgumentException("Un envio debe estar asociado a un cliente.");
        }
        this.paquetes = new ArrayList<>();
        this.diasTranscurridos = 0;
        this.codigoEscaneado = false;
        this.cliente = cliente;
        this.codigoSeguimiento = codigoSeguimiento;
        this.historial = new ArrayList<>();
        this.cliente.registrarNuevoEnvioCliente(this);
    }

    // ================== Reglas del negocio  ==================

    private boolean hayLugar() {
        return paquetes.size() < 3;
    }

    // nunca estuvo en camino ni fue entregado
    private boolean todaviaNoSalio() {
        for (Movimiento mov : historial) {
            if (mov.getEstado() == Estado.EN_REPARTO || mov.getEstado() == Estado.ENTREGADO) {
                return false;
            }
        }
        return true;
    }

    private boolean estaEntregado() {
        if (historial.isEmpty()) {
            return false;
        }
        return obtenerUltimoMovimiento().getEstado() == Estado.ENTREGADO;
    }

    private boolean estaEnAlgunaSucursal() {
        return getSucursalActual() != null;
    }

    // Todos los paquetes salen juntos de la sucursal
    private void ponerPaquetesEnViaje() {
        for (Paquete paq : paquetes) {
            paq.salirAReparto();
        }
    }

    // ================== Operaciones ==================

    public void agregarPaquete(Paquete paq) {
        if (paq == null) {
            throw new IllegalArgumentException("No se puede agregar un paquete inexistente.");
        }
        if (!hayLugar()) {
            throw new IllegalStateException("No se pueden agregar más de 3 paquetes a un mismo envio.");
        }
        if (!todaviaNoSalio()) {
            throw new IllegalStateException("No se pueden agregar paquetes a un envio que ya salió. ");
        }
        paquetes.add(paq);
        paq.iniciarPreparacion();
        historial.add(new Movimiento(Estado.EN_PREPARACION, null));
    }

    public void pasarDias(int dias) {
        this.diasTranscurridos += dias;
    }

    public void escanearCodigoEntrega() {
        this.codigoEscaneado = true;
    }

    // La sucursal recibe el envio, valida que tenga sentido y avisa a sus paquetes
    public void recibir(Sucursal sucursal) {
        if (sucursal == null) {
            throw new IllegalArgumentException("Hay que indicar que sucursal recibe el envio.");
        }
        if (paquetes.isEmpty()) {
            throw new IllegalStateException("No se puede recibir un envio que no tiene paquetes.");
        }
        if (estaEntregado()) {
            throw new IllegalStateException("El envio : " + codigoSeguimiento + " ya fue entregado.");
        }
        if (estaEnAlgunaSucursal()) {
            throw new IllegalStateException("El envio : " + codigoSeguimiento + " ya está en la sucursal "
                    + getSucursalActual().getNombre() + " y todavia no fue despachado.");
        }
        for (Paquete paq : paquetes) {
            paq.recibir();
        }
        historial.add(new Movimiento(Estado.RECIBIDO, sucursal));
    }

    // Despachar el envio sale de la sucursal Y sus paquetes quedan en viaje 
    public void despachar(Sucursal sucursal) {
        if (estaEntregado()) {
            throw new IllegalStateException("No se puede despachar el envio " + codigoSeguimiento + " porque ya fue entregado.");
        }
        if (!estaEnSucursal(sucursal)) {
            throw new IllegalStateException("El envio " + codigoSeguimiento + " no esta en esa sucursal, no se puede despachar desde ahí");
        }
        ponerPaquetesEnViaje();
        historial.add(new Movimiento(Estado.EN_REPARTO, sucursal));
    }

    public void entregarPaquete() {
        if (paquetes.isEmpty()) {
            throw new IllegalStateException("No se puede entregar un envio que no tiene paquetes.");
        }
        if (!codigoEscaneado) {
            throw new IllegalStateException("Falta escanear el codigo de entrega.");
        }
        if (!validarTiempoEscaneo()) {
            throw new IllegalStateException("El envio no se puede entregar: está fuera del plazo permitido para su tipo.");
        }
        for (Paquete paq : paquetes) {
            paq.registrarEntrega();
        }
        historial.add(new Movimiento(Estado.ENTREGADO, null));
    }

    // ================== Consultas  ==================

    public Sucursal getSucursalActual() {
        for (int i = historial.size() - 1; i >= 0; i--) {
            Movimiento mov = historial.get(i);
            if (mov.getEstado() == Estado.RECIBIDO) {
                return mov.getSucursal();
            }
            if (mov.getEstado() == Estado.EN_REPARTO || mov.getEstado() == Estado.ENTREGADO) {
                return null;
            }
        }
        return null;
    }

    public boolean estaEnSucursal(Sucursal sucursal) {
        Sucursal actual = getSucursalActual();
        if (actual == null || sucursal == null) {
            return false;
        }
        return actual.equals(sucursal);
    }

    // Devuelve una COPIA para que nadie de afuera pueda modificar la lista interna
    public ArrayList<Movimiento> getHistorial() {
        return new ArrayList<>(this.historial);
    }

    
    public ArrayList<Sucursal> obtenerSucursalesRecorridas() {
        ArrayList<Sucursal> recorridas = new ArrayList<>();
        for (Movimiento mov : historial) {
            Sucursal sucursal = mov.getSucursal();
            if (sucursal != null && !recorridas.contains(sucursal)) {
                recorridas.add(sucursal);
            }
        }
        return recorridas;
    }

    // ultimo movimiento registrado
    public Movimiento obtenerUltimoMovimiento() {
        if (historial.isEmpty()) {
            throw new IllegalStateException("El envio " + codigoSeguimiento + " todavia no tiene movimientos.");
        }
        return historial.get(historial.size() - 1);
    }

    public int getCodigoSeguimiento() {
        return this.codigoSeguimiento;
    }

    public abstract double calcularCostoEnvio();

    public abstract boolean validarTiempoEscaneo();
}
