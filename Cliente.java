import java.util.ArrayList;

public class Cliente {

    private String nombre;
    private String apellido;
    private int dni; // int, porque RegistroCentral busca con buscarClienteSecuencial(int dni)
    private ArrayList<Envio> misEnvios;

    public Cliente(String nombre, String apellido, int dni) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.misEnvios = new ArrayList<>();
        
    }

    public void registrarNuevoEnvioCliente(Envio envio) {
        this.misEnvios.add(envio);
    }

    public int getDni() {
        return this.dni;
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getApellido() {
        return this.apellido;
    }

    @Override
    public String toString() {
        return nombre + " " + apellido + " (DNI " + dni + ")";
    }
}

