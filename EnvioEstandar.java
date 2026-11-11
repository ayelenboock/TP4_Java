public class EnvioEstandar extends Envio {

    public EnvioEstandar(Cliente cliente, int codigoSeguimiento) {
        super(cliente, codigoSeguimiento);
    }

    @Override
    public double calcularCostoEnvio() {
        double totalPeso = 0;
        for (Paquete paq : paquetes) {
            totalPeso += paq.getPeso();
        }
        return 25000 + (totalPeso * 1.1);
    }

    @Override
    public boolean validarTiempoEscaneo() {
        return codigoEscaneado && (diasTranscurridos >= 5 && diasTranscurridos <= 14);
    }
}
