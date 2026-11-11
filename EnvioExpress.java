public class EnvioExpress extends Envio {

    public EnvioExpress(Cliente cliente, int codigoSeguimiento) {
        super(cliente, codigoSeguimiento);
    }

    @Override
    public double calcularCostoEnvio() {
        double totalPeso = 0;
        for (Paquete paq : paquetes) {
            totalPeso += paq.getPeso();
        }
        return 75000 + (totalPeso * 1.2);
    }

    @Override
    public boolean validarTiempoEscaneo() {
        return codigoEscaneado && (diasTranscurridos >= 1 && diasTranscurridos <= 2);
    }
}
