import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        // Una sola instancia del registro
        RegistroCentral registro = new RegistroCentral();

        Sucursal bariloche = new Sucursal("Bariloche", "8400");
        Sucursal viedma = new Sucursal("Viedma", "8500");
        registro.registrarSucursal(bariloche);
        registro.registrarSucursal(viedma);

        Cliente juan = new Cliente("Juan", "Mora", 34567843);
        Cliente ana = new Cliente("Ana", "Perez", 28111222);
        registro.registrarCliente(juan);
        registro.registrarCliente(ana);

        System.out.println("=== CLIENTES REGISTRADOS ===");
        for (Cliente c : registro.listarClientes()) {
            System.out.println(c);
        }

        System.out.println("\n=== PREPARANDO envios ===");
        Envio envio1 = new EnvioEstandar(juan, 1001);
        Envio envio2 = new EnvioExpress(ana, 1002);
        registro.registrarEnvio(envio1);
        registro.registrarEnvio(envio2);
        envio1.agregarPaquete(new Paquete(101, 1.5, "Viedma"));
        envio2.agregarPaquete(new Paquete(102, 3.0, "Viedma"));
        System.out.println("Costo del envio 1001: $" + envio1.calcularCostoEnvio());
        System.out.println("Costo del envio 1002: $" + envio2.calcularCostoEnvio());

        System.out.println("\n=== LLEGAN A BARILOCHE ===");
        // El registro solo ENCUENTRA; la acción la hace la sucursal
        bariloche.recibirEnvio(registro.buscarEnvio(1001));
        bariloche.recibirEnvio(registro.buscarEnvio(1002));
        System.out.println("Envios esperando en Bariloche: " + bariloche.cantidadPendientes());

        System.out.println("\n=== DESPACHO DESDE BARILOCHE (capacidad 1) ===");
        ArrayList<Envio> salieron = bariloche.despachar(1);
        for (Envio e : salieron) {
            System.out.println("Salio el envio : " + e.getCodigoSeguimiento());
        }
        System.out.println("Quedan esperando: " + bariloche.cantidadPendientes());

        System.out.println("\n=== EL envio LLEGA A VIEDMA Y SE ENTREGA ===");
        Envio enViaje = registro.buscarEnvio(1001);
        viedma.recibirEnvio(enViaje);
        viedma.despachar(1);
        enViaje.escanearCodigoEntrega();
        enViaje.pasarDias(6);
        enViaje.entregarPaquete();
        System.out.println("Envio entregado con éxito ");

        System.out.println("\n=== HISTORIAL DEL envio ===");
        for (Movimiento mov : enViaje.getHistorial()) {
            System.out.println(mov);
        }

        System.out.println("\n=== SUCURSALES POR LAS QUE pasael envio ===");
        for (Sucursal s : enViaje.obtenerSucursalesRecorridas()) {
            System.out.println("- " + s.getNombre());
        }

        System.out.println("\n=== ULTIMO MOVIMIENTO   ===");
        System.out.println(enViaje.obtenerUltimoMovimiento());

        System.out.println("\n=== BUSQUEDA SECUENCIAL DE CLIENTE ===");
        System.out.println("Encontrado: " + registro.buscarClienteSecuencial(28111222));

        System.out.println("\n=== DESPACHO CON MENOS ENVIADOS QUE LA CAPACIDAD ===");
        ArrayList<Envio> restantes = bariloche.despachar(5); // pedimos 5, solo hay 1
        System.out.println("Salieron " + restantes.size() + " envio(s)");

        System.out.println("\n=== PRUEBAS DE OPERACIONES NO validas ===");

        try {
            registro.registrarEnvio(envio1); // CODIGO repetido
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            registro.buscarEnvio(9999); // no existe
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            registro.buscarClienteSecuencial(1); // no existe
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            bariloche.despachar(1); // cola vacia
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            envio1.agregarPaquete(new Paquete(103, 2.0, "Buenos Aires")); // ya salió
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            bariloche.recibirEnvio(envio1); // ya fue entregado
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            viedma.despachar(1); // no hay nada en cola
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
