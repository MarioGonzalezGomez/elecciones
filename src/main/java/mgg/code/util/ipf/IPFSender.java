package mgg.code.util.ipf;

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

    public String congresoActualizaPosiciones() {
        String mensaje = faldonesBuilder.congresoActualizaPosiciones();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String congresoSale() {
        String mensaje = faldonesBuilder.congresoSale();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    //DESPLIEGA_4
    public String cuatroPrimeros() {
        String mensaje = faldonesBuilder.cuatroPrimeros();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String despliego(int posicion) {
        String mensaje = faldonesBuilder.despliego(posicion);
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String repliego(int posicion) {
        String mensaje = faldonesBuilder.repliego(posicion);
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String recuperoTodos() {
        String mensaje = faldonesBuilder.recuperoTodos();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    //CONGRESO_SONDEO
    public String congresoSondeoEntra() {
        String mensaje = faldonesBuilder.congresoSondeoEntra();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String congresoSondeoActualizaEscrutado() {
        String mensaje = faldonesBuilder.congresoSondeoActualizaEscrutado();
        c.enviarMensaje(mensaje);
        return mensaje;
    }
    public String congresoSondeoActualizaDatos() {
        String mensaje = faldonesBuilder.congresoSondeoActualizaDatos();
        c.enviarMensaje(mensaje);
        return mensaje;
    }
    public String congresoSondeoActualizaPosiciones() {
        String mensaje = faldonesBuilder.congresoSondeoActualizaPosiciones();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String congresoSondeoSale() {
        String mensaje = faldonesBuilder.congresoSondeoSale();
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

    //RESET
    public String reset() {
        String mensaje = faldonesBuilder.resetIPF();
        c.enviarMensaje(mensaje);
        return mensaje;
    }
}
