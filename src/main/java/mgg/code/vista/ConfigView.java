/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package mgg.code.vista;

import mgg.code.config.Config;
import mgg.code.controller.hibernate.HibernateControllerCongreso;
import mgg.code.controller.hibernate.HibernateControllerSenado;
import mgg.code.util.DB;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ConfigView extends JFrame {

    public static String conexionBD = "";
    public static boolean cambioBD = false;
    private JLabel labelTemp;


    public ConfigView(JLabel lblConexion) throws IOException {
        initComponents();
        cargarIni();
        labelTemp = lblConexion;
        conexionBD = DB.getInstance().getDb();
        dondeEstoy();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new JLabel();
        buttonGroup1 = new ButtonGroup();
        buttonGroup2 = new ButtonGroup();
        jLabel2 = new JLabel();
        jRadioButton1 = new JRadioButton();
        jRadioButton2 = new JRadioButton();
        jRadioButton3 = new JRadioButton();
        buttonGroup1.add(jRadioButton1);
        buttonGroup1.add(jRadioButton2);
        buttonGroup1.add(jRadioButton3);


        puertoTL = new JTextField();
        ip1TL = new JTextField();
        bd1TL = new JTextField();
        bd2TL = new JTextField();
        spTL = new JTextField();
        srTL = new JTextField();
        jLabel3 = new JLabel();
        jLabel4 = new JLabel();
        bdFaldones = new JLabel();
        bdCartones = new JLabel();
        jLabel7 = new JLabel();
        jLabel8 = new JLabel();
        jButton1 = new JButton();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("BASE DE DATOS");

        jRadioButton1.setText("PRINCIPAL");

        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        jRadioButton2.setText("RESERVA");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        jRadioButton3.setText("LOCAL");

        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel3.setText("IP IPF1:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel4.setText("PUERTO:");

        bdFaldones.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        bdFaldones.setText("BD FALDONES:");

        bdCartones.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        bdCartones.setText("BD CARTONES:");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel7.setText("IP SERVIDOR RESERVA:");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel8.setText("IP SERVIDOR PRINCIPAL:");

        jButton1.setBackground(new java.awt.Color(153, 255, 153));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton1.setText("GUARDAR CONFIGURACIÓN");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(69, 69, 69)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(jRadioButton1)
                                                        .addComponent(jRadioButton2)
                                                        .addComponent(jLabel2)
                                                        .addComponent(jRadioButton3))
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(194, 194, 194)
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                                                        .addComponent(jLabel4)
                                                                                        .addComponent(jLabel3))
                                                                                .addGap(11, 11, 11))
                                                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                                .addComponent(bdCartones)
                                                                                .addComponent(bdFaldones, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)))
                                                                .addGap(18, 18, 18))
                                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                                        .addComponent(jLabel7)
                                                                        .addComponent(jLabel8))
                                                                .addGap(27, 27, 27)))
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(srTL, GroupLayout.PREFERRED_SIZE, 226, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(spTL, GroupLayout.PREFERRED_SIZE, 226, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(ip1TL, GroupLayout.PREFERRED_SIZE, 226, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(puertoTL, GroupLayout.PREFERRED_SIZE, 226, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(bd1TL, GroupLayout.PREFERRED_SIZE, 226, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(bd2TL, GroupLayout.PREFERRED_SIZE, 226, GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(219, 219, 219)
                                                .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 298, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(49, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(ip1TL, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel3))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(puertoTL, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel4))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(bd1TL, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(bdFaldones))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(bd2TL, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(bdCartones))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(spTL, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel8))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(srTL, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel7)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel2)
                                                .addGap(33, 33, 33)
                                                .addComponent(jRadioButton1)
                                                .addGap(27, 27, 27)
                                                .addComponent(jRadioButton2)
                                                .addGap(27, 27, 27)
                                                .addComponent(jRadioButton3)))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                                .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
                                .addGap(21, 21, 21))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //IniFileReaderWriter iniFile = new IniFileReaderWriter("C:\\ELECCIONES2023\\config.ini");

    public static Config cargarConfiguracion() throws IOException {
        Config config = Config.getConfiguracion();
        config.loadConfig();
        return config;
    }

    private void cargarIni() throws IOException {
        Config config = cargarConfiguracion();
        String ip1 = config.getipIPF();
        ip1TL.setText(ip1);
        String puerto = config.getpuertoIPF();
        puertoTL.setText(puerto);
        String bdCartones = config.getBdCartones();
        bd1TL.setText(bdCartones);
        String bdFaldones = config.getBdFaldones();
        bd2TL.setText(bdFaldones);
        String ipServPrincipal = config.getIpDbPrincipal();
        spTL.setText(ipServPrincipal);
        String ipServReserva = config.getIpDbReserva();
        srTL.setText(ipServReserva);
    }

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // graficosController.conectPrincipal();
        conexionBD = "BD PRINCIPAL";
        System.out.println("AAAAA");
        cambioBD = true;

        HibernateControllerSenado.cambioPrincipal();
        HibernateControllerCongreso.cambioPrincipal();
        labelTemp.setText(conexionBD);
        cambioBD = false;
    }

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // graficosController.conectReserva();
        conexionBD = "BD RESERVA";
        cambioBD = true;

        HibernateControllerSenado.cambioReserva();
        HibernateControllerCongreso.cambioReserva();
        labelTemp.setText(conexionBD);
        cambioBD = false;

    }

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // graficosController.conectLocal();
        conexionBD = "BD LOCAL";
        cambioBD = true;

        HibernateControllerSenado.cambioLocal();
        HibernateControllerCongreso.cambioLocal();
        labelTemp.setText(conexionBD);
        cambioBD = false;

    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        try {

            Properties properties = new Properties();
            properties.setProperty("ipIPF", ip1TL.getText());
            properties.setProperty("rutaFicheros", "C:\\Elecciones2023\\DATOS");
            properties.setProperty("rutaColores", "C:\\Elecciones2023\\DATOS\\COLORES\\ColoresPartidos.csv");
            properties.setProperty("puertoIPF", puertoTL.getText());
            properties.setProperty("BDReserva", srTL.getText());
            properties.setProperty("BDPrincipal", spTL.getText());
            properties.setProperty("BDCartones", "<CARTONES>");
            properties.setProperty("BDFaldones", "<FALDONES>");

            FileOutputStream archivo = new FileOutputStream("C:\\Elecciones2023\\config.properties");
            properties.store(archivo, "Archivo de configuración");
            archivo.close();
            JOptionPane.showMessageDialog(null, "El archivo se ha guardado correctamente", "Configuracion guardada", JOptionPane.INFORMATION_MESSAGE);


            //CIERRE DE VENTANA CUANDO GUARDO

            dispose();

        } catch (IOException ex) {
            Logger.getLogger(ConfigView.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "El archivo no se ha guardado correctamente", "Configuracion no guardada", JOptionPane.INFORMATION_MESSAGE);
        }

    }//GEN-LAST:event_jButton1ActionPerformed


    private static String escapePropertyValue(String value) {
        StringBuilder sb = new StringBuilder(value.length());
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case ' ':
                    sb.append("\\ ");
                    break;
                case ':':
                    sb.append("\\:");
                    break;
                case '=':
                    sb.append("\\=");
                    break;
                case '<':
                    sb.append("\\<");
                    break;
                case '>':
                    sb.append("\\>");
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConfigView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(ConfigView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ConfigView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(ConfigView.class.getName()).log(Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new ConfigView(null).setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(ConfigView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void dondeEstoy() {
        //TODO:poner en check dónde estamos además de cambiar el lbl
        switch (conexionBD) {
            case "BD PRINCIPAL" -> jRadioButton1.setSelected(true);
            case "BD RESERVA" -> jRadioButton2.setSelected(true);
            case "BD LOCAL" -> jRadioButton3.setSelected(true);
            case "BD NO IDENTIFICADA" -> {
            }
        }
        ;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JTextField bd1TL;
    private JTextField bd2TL;
    private JLabel bdCartones;
    private JLabel bdFaldones;
    private ButtonGroup buttonGroup1;
    private ButtonGroup buttonGroup2;
    private JTextField ip1TL;
    private JButton jButton1;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JRadioButton jRadioButton1;
    private JRadioButton jRadioButton2;
    private JRadioButton jRadioButton3;
    private JTextField puertoTL;
    private JTextField spTL;
    private JTextField srTL;
    // End of variables declaration//GEN-END:variables
}
