package mgg.code.conexion;

import mgg.code.config.Config;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ConexionIPF {
    private static ConexionIPF conexion;

    private final Config c = Config.getConfiguracion();
    private Socket servidor;
    private DataInputStream datoEntrada = null;
    private DataOutputStream datoSalida = null;

    private final int TIMEOUT = 3000; // 3 segundos


    private ConexionIPF() {
        conectar();
    }


    public static ConexionIPF getConexion() {
        if (conexion == null) {
            conexion = new ConexionIPF();
        }
        return conexion;
    }

    public void enviarMensaje(String mensaje) {
        try {
            //No lo reconoce IPF al mandarlo como UTF. Añade caracteres especiales al inicio del String
            datoSalida.writeBytes(mensaje);
            System.out.println("Enviando: " + mensaje);
        } catch (IOException ex) {
            System.err.println("Cliente->ERROR: Al enviar mensaje " + ex.getMessage());
        }
    }

    private void conectar() {
        Thread conexionRemotaThread = new Thread(() -> {
            try {
                servidor = new Socket();
                servidor.connect(new InetSocketAddress(c.getipIPF(), Integer.parseInt(c.getpuertoIPF())), TIMEOUT);
                crearFlujosES();
                System.out.println("Cliente->Conectado al IPF...");
            } catch (IOException ex) {
                System.err.println("Cliente->ERROR: Al conectar al servidor en " + c.getipIPF() + " > " + ex.getMessage());
            }
        });

        Thread conexionLocalThread = new Thread(() -> {
            try {
                servidor = new Socket();
                servidor.connect(new InetSocketAddress("127.0.0.1", Integer.parseInt(c.getpuertoIPF())), TIMEOUT);
                crearFlujosES();
                System.out.println("Cliente->Conectado en local");
            } catch (IOException ex) {
                System.err.println("Cliente->ERROR: Al conectar al IPF local -> " + ex.getMessage());
            }
        });

        conexionRemotaThread.start();
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        conexionLocalThread.start();

        try {
            conexionRemotaThread.join();
            conexionLocalThread.join();
        } catch (InterruptedException e) {
            System.err.println("Cliente->ERROR: Al esperar por la finalización de los hilos de conexión -> " + e.getMessage());
        }
    }


    private void crearFlujosES() {
        try {
            datoEntrada = new DataInputStream(servidor.getInputStream());
            datoSalida = new DataOutputStream(servidor.getOutputStream());
        } catch (IOException ex) {
            System.err.println("ServidorGC->ERROR: crear flujos de entrada y salida " + ex.getMessage());
        }
    }

    public void desconectar() {
        try {
            servidor.close();
            datoEntrada.close();
            datoSalida.close();
            System.out.println("Cliente->Desconectado");
        } catch (IOException ex) {
            System.err.println("Cliente->ERROR: Al desconectar al servidor " + ex.getMessage());
            System.exit(-1);
        }
    }
}
