public class EnvioInternacional extends Envio {

    public EnvioInternacional(Cliente cliente, int codigoSeguimiento) {
        super(cliente, codigoSeguimiento);
    }

    @Override
    public double calcularCostoEnvio() {
        double totalPeso = 0;
        for (Paquete paq : paquetes) {
            totalPeso += paq.getPeso();
        }
        return 125000 + (totalPeso * 1.5);
    }

    @Override
    public boolean validarTiempoEscaneo() {
        return codigoEscaneado && (diasTranscurridos >= 20 && diasTranscurridos <= 30);
    }
}
