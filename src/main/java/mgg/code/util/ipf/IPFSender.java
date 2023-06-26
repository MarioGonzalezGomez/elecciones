package mgg.code.util.ipf;

import mgg.code.conexion.ConexionIPF;

public class IPFSender {
    private ConexionIPF c;
    private IPFFaldonesMessageBuilder faldonesBuilder;

    public IPFSender() {
        c = ConexionIPF.getConexion();
        faldonesBuilder = IPFFaldonesMessageBuilder.getInstance();
    }

    //SENADO
    public String senadoEntra() {
        String mensaje = faldonesBuilder.senadoEntra();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String senadoActualiza() {
        String mensaje = faldonesBuilder.senadoActualiza();
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

    public String congresoSale() {
        String mensaje = faldonesBuilder.congresoSale();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    //CONGRESO_SONDEO
    public String congresoSondeoEntra() {
        String mensaje = faldonesBuilder.congresoSondeoEntra();
        c.enviarMensaje(mensaje);
        return mensaje;
    }

    public String congresoSondeoActualiza() {
        String mensaje = faldonesBuilder.congresoSondeoActualiza();
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
