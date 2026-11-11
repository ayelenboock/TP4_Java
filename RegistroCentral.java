import java.util.ArrayList;
import java.util.HashMap;

// Es "el archivo de la oficina": guarda, busca y lista. 
public class RegistroCentral {

    private ArrayList<Cliente> clientes;              // se busca recorriendo (busqueda secuencial)
    private HashMap<Integer, Envio> envios;           // clave código de seguimiento
    private HashMap<String, Sucursal> sucursales;     // clave código postal

    public RegistroCentral() {
        this.clientes = new ArrayList<>();
        this.envios = new HashMap<>();
        this.sucursales = new HashMap<>();
    }

    // ================== Clientes ==================

    public void registrarCliente(Cliente c) {
        if (c == null) {
            throw new IllegalArgumentException("No se puede registrar un cliente inexistente.");
        }
        if (encontrarCliente(c.getDni()) != null) {
            throw new IllegalArgumentException("Ya existe un cliente registrado con el DNI " + c.getDni() + ".");
        }
        clientes.add(c);
    }

    // Devuelve una copia para que nadie de afuera modifique la lista interna
    public ArrayList<Cliente> listarClientes() {
        return new ArrayList<>(clientes);
    }

    //  recorre desde el principio hasta encontrar el DNI
    public Cliente buscarClienteSecuencial(int dni) {
        Cliente encontrado = encontrarCliente(dni);
        if (encontrado == null) {
            throw new IllegalArgumentException("No existe un cliente con el DNI " + dni + ".");
        }
        return encontrado;
    }

    
    // Lo usan registrarCliente (para no repetir DNI) y buscarClienteSecuencial.
    private Cliente encontrarCliente(int dni) {
        for (Cliente c : clientes) {
            if (c.getDni() == dni) {
                return c;
            }
        }
        return null;
    }

    // ================== Sucursales ==================

    public void registrarSucursal(Sucursal s) {
        if (s == null) {
            throw new IllegalArgumentException("No se puede registrar una sucursal inexistente.");
        }
        if (sucursales.containsKey(s.getCodigoPostal())) {
            throw new IllegalArgumentException("Ya existe una sucursal con el código postal " + s.getCodigoPostal() + ".");
        }
        sucursales.put(s.getCodigoPostal(), s);
    }

    public Sucursal buscarSucursal(String codigoPostal) {
        Sucursal encontrada = sucursales.get(codigoPostal);
        if (encontrada == null) {
            throw new IllegalArgumentException("No existe una sucursal con el código postal " + codigoPostal + ".");
        }
        return encontrada;
    }

    // ================== Envios ==================

    public void registrarEnvio(Envio e) {
        if (e == null) {
            throw new IllegalArgumentException("No se puede registrar un envio inexistente.");
        }
        if (envios.containsKey(e.getCodigoSeguimiento())) {
            throw new IllegalArgumentException("Ya existe un envio con el código " + e.getCodigoSeguimiento() + ".");
        }
        envios.put(e.getCodigoSeguimiento(), e);
    }

    public Envio buscarEnvio(int codigo) {
        Envio encontrado = envios.get(codigo);
        if (encontrado == null) {
            throw new IllegalArgumentException("No existe un envio con el código " + codigo + ".");
        }
        return encontrado;
    }
}
