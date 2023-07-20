package mgg.code.util.ipf;

import mgg.code.model.CP;

import java.util.List;

public class IPFSender {
    private static IPFSender ipf;
    private ConexionIPF c;
    private IPFFaldonesMessageBuilder faldonesBuilder;

    private IPFSender() {
        c = ConexionIPF.getConexion();
        faldonesBuilder = IPFFaldonesMessageBuilder.getInstance();
    }

    public static IPFSender getInstance() {
        if (ipf == null) {
            ipf = new IPFSender();
        }
        return ipf;
    }

    //SENADO
    public String senadoEntra() {
        String mensaje = faldonesBuilder.senadoEntra();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String senadoActualizaEscrutado() {
        String mensaje = faldonesBuilder.senadoActualizaEscrutado();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String senadoActualizaDatos() {
        String mensaje = faldonesBuilder.senadoActualizaDatos();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String senadoActualizaPosiciones() {
        String mensaje = faldonesBuilder.senadoActualizaPosiciones();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String senadoSale() {
        String mensaje = faldonesBuilder.senadoSale();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    //CONGRESO
    public String congresoEntra() {
        String mensaje = faldonesBuilder.congresoEntra();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String congresoActualiza() {
        String mensaje = faldonesBuilder.congresoActualiza();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String congresoActualizaEscrutado() {
        String mensaje = faldonesBuilder.congresoActualizaEscrutado();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String congresoActualizaDatos() {
        String mensaje = faldonesBuilder.congresoActualizaDatos();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String congresoActualizaDatosIndividualizado(List<CP> codigos) {
        String mensaje = faldonesBuilder.congresoActualizaDatosIndividualizado(codigos);
        c.enviarMensaje(mensaje);
        return mensaje;
    }


    public String congresoYaNoEstaDatosIndividualizado(List<String> codigos) {
        String mensaje = "";
        if (codigos != null && !codigos.isEmpty()) {
            mensaje = faldonesBuilder.congresoYaNoEstaIndividualizado(codigos);
            c.enviarMensaje(mensaje);
        }
        return mensaje;
    }

    public String congresoActualizaPosiciones() {
        String mensaje = faldonesBuilder.congresoActualizaPosiciones();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String congresoActualizaNumPartidos() {
        String mensaje = faldonesBuilder.congresoActualizaNumPartidos();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String congresoSale() {
        String mensaje = faldonesBuilder.congresoSale();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String congresoVotosEntra() {
        String mensaje = faldonesBuilder.congresoVotosEntra();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String congresoVotosSale() {
        String mensaje = faldonesBuilder.congresoVotosSale();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String congresoHistoricosEntra() {
        String mensaje = faldonesBuilder.congresoHistoricosEntra();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String congresoHistoricosSale() {
        String mensaje = faldonesBuilder.congresoHistoricosSale();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    //DESPLIEGA_4
    public String cuatroPrimeros() {
        String mensaje = faldonesBuilder.cuatroPrimeros();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String despliego(String codPartido) {
        String mensaje = faldonesBuilder.despliego(codPartido);
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String repliego(String codPartido) {
        String mensaje = faldonesBuilder.repliego(codPartido);
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String esDirecto(boolean esDirecto, int tipoElecciones, String codPartido) {
        String mensaje = faldonesBuilder.esDirecto(esDirecto, tipoElecciones, codPartido);
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String recuperoTodos() {
        String mensaje = faldonesBuilder.recuperoTodos();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    //DESPLIEGO_4_SONDEO

    public String cuatroPrimerosSondeo() {
        String mensaje = faldonesBuilder.cuatroPrimerosSondeo();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String despliegoSondeo(int posicion) {
        String mensaje = faldonesBuilder.despliegoSondeo(posicion);
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String repliegoSondeo(int posicion) {
        String mensaje = faldonesBuilder.repliegoSondeo(posicion);
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String recuperoTodosSondeo() {
        String mensaje = faldonesBuilder.recuperoTodosSondeo();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String congresoMillonesEntra() {
        String mensaje = faldonesBuilder.congresoMillonesEntra();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String congresoMillonesSale() {
        String mensaje = faldonesBuilder.congresoMillonesSale();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    //CONGRESO_SONDEO
    public String congresoSondeoEntra() {
        String mensaje = faldonesBuilder.congresoSondeoEntra();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String congresoSondeoSale() {
        String mensaje = faldonesBuilder.congresoSondeoSale();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String congresoSondeoVotosEntra() {
        String mensaje = faldonesBuilder.congresoSondeoVotosEntra();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String congresoSondeoVotosSale() {
        String mensaje = faldonesBuilder.congresoSondeoVotosSale();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String congresoSondeoHistoricosEntra() {
        String mensaje = faldonesBuilder.congresoSondeoHistoricosEntra();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String congresoSondeoHistoricosSale() {
        String mensaje = faldonesBuilder.congresoSondeoHistoricosSale();
        c.enviarMensaje(mensaje);
        return mensaje;
    }


    //GIROS
    public String deCongresoASenado() {
        String mensaje = faldonesBuilder.deCongresoASenado();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String deSenadoACongreso() {
        String mensaje = faldonesBuilder.deSenadoACongreso();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String deSondeoACongreso() {
        String mensaje = faldonesBuilder.deSondeoACongreso();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    //SEDES
    public String sedesEntra() {
        String mensaje = faldonesBuilder.sedesEntra();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String sedesSale() {
        String mensaje = faldonesBuilder.sedesSale();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    //RESET
    public String reset() {
        String mensaje = faldonesBuilder.resetIPF();
        c.enviarMensaje(mensaje);
        return mensaje;
    }
}
