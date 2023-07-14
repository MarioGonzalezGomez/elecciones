
package mgg.code.vista;

import mgg.code.controller.*;
import mgg.code.controller.hibernate.HibernateControllerCongreso;
import mgg.code.controller.hibernate.HibernateControllerSenado;
import mgg.code.model.dto.SedesDTO;
import mgg.code.util.DB;
import mgg.code.util.Listeners;
import mgg.code.util.comparators.CPVotantes;
import mgg.code.util.ipf.ConexionIPF;
import mgg.code.model.Circunscripcion;
import mgg.code.model.dto.BrainStormDTO;
import mgg.code.model.dto.CircunscripcionDTO;
import mgg.code.util.ipf.IPFSender;
import mgg.code.vista.data.CpData;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

//TODO(CERRAR HC Y HS AL CERRAR VENTANA)

public class Home extends JFrame {
    private static Home instance;

    public static Home getInstance() {
        if (instance == null) {
            instance = new Home();
        }
        return instance;
    }

    public static BrainStormDTO bs;
    String selectedDb = "";
    CircunscripcionController circon = CircunscripcionController.getInstance();
    PartidoController parcon = PartidoController.getInstance();
    CPController cpcon = CPController.getInstance();
    SedesDTOController sedescon = SedesDTOController.getInstance();
    BrainStormDTOController bscon = BrainStormDTOController.getInstance();

    IPFSender ipf = IPFSender.getInstance();

    private List<Circunscripcion> autonomias = new ArrayList<>();
    private List<Circunscripcion> provincias = new ArrayList<>();
    //Congeso -> 1: Oficiales 2:Sondeo      Senado -> 3
    public static int tipoElecciones = 2;
    private boolean oficiales = false;

    //TODO:Add booleans para cada grafico
    private boolean resCongresoOfiIn = false;
    private boolean resCongresoSonIn = false;
    private boolean resSenadoIn = false;
    public static String avance = "1";

    private static final String CONFIG_FILE_PATH = "C:\\Elecciones2023\\config.properties";

    private JButton botonSeleccionado = null;
    private JButton botonSeleccionado2 = null;

    Listeners listeners;

    public void initCircunscripciones() {
        autonomias = circon.getAllCircunscripciones();
        provincias = autonomias.stream().filter(x -> x.getCodigo().endsWith("000")).toList();
        autonomias = autonomias.stream().filter(x -> x.getCodigo().endsWith("00000")).toList();
        ArrayList<Circunscripcion> temp = new ArrayList<>();
        provincias.forEach(cir -> {
            if (!autonomias.contains(cir)) {
                temp.add(cir);
            }
        });
        provincias = temp;

        listeners = Listeners.getInstance();
        listeners.listenCongreso();
        listeners.listenSenado();
    }

    public void showDataTable(BrainStormDTO bs) {
        if (tablaGraficos.getSelectedRow() == 1) {
            bs.setCpDTO(bs.getCpDTO().subList(0, 4));
        }
        List<CpData> datos = CpData.fromBrainStormDto(bs);
        printData(datos);
        cargarLabels(bs);
    }

    private void cargarLabels(BrainStormDTO bs) {
        lblEscrutado.setText(bs.getCircunscripcion().getEscrutado() + "");
        lblParticipacion.setText(bs.getCircunscripcion().getParticipacion() + "");
        lblPartHistorica.setText(bs.getCircunscripcion().getParticipacionHistorico() + "");
        lblEscanosTotales.setText(bs.getCircunscripcion().getEscanios() + "");
    }

    public void printDataEsp() throws IOException {
        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // establece todas las celdas no editables
            }
        };

        BrainStormDTO esp = null;

        tableModel.addColumn("ESCRUTADO");
        tableModel.addColumn("PARTICIPACION");
        tableModel.addColumn("PARTICIPACION H");


        tablaDatos.setModel(tableModel);
        switch (tipoElecciones) {
            //oficiales congreso
            case 1 -> {
                esp = bscon.getBrainStormDTOOficial("9900000", avance);
            }
            //sondeo congreso
            case 2 -> {
                esp = bscon.getBrainStormDTOSondeo("9900000", avance);
            }
            //senado
            case 3 -> {
                //   esp = bscon.getBrainStormDTOOficialSenado("9900000", avance);
                System.out.println("Aqui hemos sacado el senado");
            }
        }

        CircunscripcionDTO espCirc = esp.getCircunscripcion();
        Object[] rowData = {espCirc.getEscrutado(), espCirc.getParticipacion(), espCirc.getParticipacionHist()};
        tableModel.addRow(rowData);
        tablaDatos.setModel(tableModel);
        tablaDatos.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tablaDatos);
    }

    public void printDataVotantes() {
        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // establece todas las celdas no editables
            }
        };

        tableModel.addColumn("CODIGO");
        tableModel.addColumn("PARTIDO");
        tableModel.addColumn("VOTANTES");


        tablaDatos.setModel(tableModel);
        BrainStormDTO votantesDTO = bscon.getBrainStormDTOOficial("9900000", avance);
        var list = votantesDTO.getCpDTO().stream().sorted(new CPVotantes().reversed());
        list.forEach(cpDTO -> {
            Object[] rowData = {cpDTO.getCodigoPartido(), cpDTO.getSiglas(), cpDTO.getNumVotantes()};
            tableModel.addRow(rowData);
        });

        tablaDatos.setModel(tableModel);
        tablaDatos.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tablaDatos);
    }

    public void printData(List<CpData> list) {
        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // establece todas las celdas no editables
            }
        };
        tableModel.addColumn("COD");
        tableModel.addColumn("SIGLAS");
        tableModel.addColumn("E.D");
        tableModel.addColumn("E.H");
        tableModel.addColumn("HIST");
        tableModel.addColumn("% VOTO");
        tableModel.addColumn("VOTANTES");
        if (oficiales) {
            for (CpData cpDTO : list) {
                Object[] rowData = {cpDTO.getCodigo(), cpDTO.getSiglas(),
                        cpDTO.getEscanosDesde(), cpDTO.getEscanosHasta(), cpDTO.getEscanosHist(),
                        cpDTO.getPorcentajeVoto(), cpDTO.getVotantes()};
                tableModel.addRow(rowData);
            }

        } else {
            for (CpData cpDTO : list) {
                Object[] rowData = {cpDTO.getCodigo(), cpDTO.getSiglas(),
                        cpDTO.getEscanos_desde_sondeo(), cpDTO.getEscanos_hasta_sondeo(), cpDTO.getEscanosHist(), cpDTO.getPorcentajeVotoSondeo(), cpDTO.getVotantes()};
                tableModel.addRow(rowData);
            }
        }
        // JScrollPane scrollPane = new JScrollPane(tablaDatos);
        tablaDatos.setModel(tableModel);
        tablaDatos.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tablaDatos);

    }


    private Home() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                ConexionIPF.getConexion().desconectar();
                HibernateControllerCongreso.getInstance().close();
                HibernateControllerSenado.getInstance().close();
                System.exit(0);
            }
        });
        initCircunscripciones();
        initComponents();
        resaltarBotonAvances(btnAvance1);
        jCheckBox1.setVisible(false);
        lblConexion.setText(DB.getInstance().getDb());
        resaltarBoton(btnCongresoSondeo);
        tablaGraficos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaDatos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaComunidades.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tablaGraficos.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    tablaComunidades.clearSelection();
                    tablaDatos.clearSelection();
                }
            }
        });

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("imagenes/iconconfig.png");
        ImageIcon icon = null;
        try {
            icon = new ImageIcon(ImageIO.read(inputStream));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        jScrollPane1 = new JScrollPane();
        tablaDatos = new JTable();
        jScrollPane2 = new JScrollPane();
        tablaComunidades = new JTable();
        jScrollPane3 = new JScrollPane();
        tablaGraficos = new JTable();
        jScrollPane4 = new JScrollPane();
        tablaProvincias = new JTable();
        btnEntra = new JButton();
        btnSale = new JButton();
        btnReset = new JButton();
        btnDesplegarDirecto = new JButton();
        btnDesplegarVideo = new JButton();
        btnReplegar = new JButton();
        btnCongresoSondeo = new JButton();
        btnDatosSenado = new JButton();
        btnDatosCongreso = new JButton();
        jScrollPane5 = new JScrollPane();
        jCheckBox1 = new JCheckBox();
        //ButtonGroup buttonGroup = new ButtonGroup();
        jLabel2 = new JLabel();
        lblEscrutado = new JLabel();
        jLabel3 = new JLabel();
        lblParticipacion = new JLabel();
        btnAvance1 = new JButton();
        btnAvance2 = new JButton();
        btnAvance3 = new JButton();
        jLabel4 = new JLabel();
        lblPartHistorica = new JLabel();
        lblEscanosTotales = new JLabel();
        lblEscTotales = new JLabel();
        btnConfig = new JButton();
        btnActualizar = new JButton();
        lblConexion = new JLabel();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("ELECCIONES 2023");

        tablaDatos.setModel(new DefaultTableModel(
                new Object[][]{
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null}
                },
                new String[]{
                        "CODIGO", "SIGLAS", "E.D", "E.H", "% VOTO"
                }
        ) {
            boolean[] canEdit = new boolean[]{
                    false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablaDatos);
        if (tablaDatos.getColumnModel().getColumnCount() > 0) {
            tablaDatos.getColumnModel().getColumn(0).setResizable(false);
            tablaDatos.getColumnModel().getColumn(1).setResizable(false);
            tablaDatos.getColumnModel().getColumn(2).setResizable(false);
            tablaDatos.getColumnModel().getColumn(3).setResizable(false);
            tablaDatos.getColumnModel().getColumn(4).setResizable(false);
        }

        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // establece todas las celdas no editables
            }
        };
        tableModel.addColumn("COMUNIDADES");
        tablaComunidades.setModel(tableModel);
        rellenarCCAA();
        JScrollPane scrollPane = new JScrollPane(tablaComunidades);
        tablaComunidades.getTableHeader().setResizingAllowed(false);
        jScrollPane2.setViewportView(tablaComunidades);
        if (tablaComunidades.getColumnModel().getColumnCount() > 0) {
            tablaComunidades.getColumnModel().getColumn(0).setResizable(false);
        }

        tablaComunidades.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                if (tablaComunidades.getSelectedRow() != -1) {
                    String codAutonomia;
                    int selectedRow = tablaComunidades.getSelectedRow();
                    if (selectedRow != -1) {
                        codAutonomia = autonomias.stream().filter(aut -> aut.getNombreCircunscripcion().equals(tablaComunidades.getValueAt(selectedRow, 0))).findFirst().get().getCodigo();
                        loadSelectedCongreso(codAutonomia);
                        if (tipoElecciones == 1 || tipoElecciones == 2) {
                            if (oficiales) {
                                //TODO:Logica de si es esta selectedRow (es decir, faldon X) hace una cosa u otra
                                bs = bscon.getBrainStormDTOOficial(codAutonomia, avance);
                                bscon.getBrainStormDTOOficialCongresoInCsv(bs);
                            } else {
                                //TODO:Logica de si es esta selectedRow (es decir, faldon X) hace una cosa u otra
                                bs = bscon.getBrainStormDTOSondeo(codAutonomia, avance);
                                bscon.getBrainStormDTOSondeoEspecialInCsv(bs);
                            }
                        } else {
                            ((DefaultTableModel) tablaDatos.getModel()).setRowCount(0);
                            //TODO:Logica de si es esta selectedRow (es decir, faldon X) hace una cosa u otra
                            bs = bscon.getBrainStormDTOSenado(codAutonomia, avance);
                            bscon.getBrainStormDTOSenadoInCsv(bs);
                        }
                        showDataTable(bs);
                    }
                }
            }
            if (tablaComunidades.getColumnModel().getColumnCount() > 0) {
                tablaComunidades.getColumnModel().getColumn(0).setResizable(false);
            }
        });
        tablaDatos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                if (tablaGraficos.getSelectedRow() == 2 && tablaDatos.getSelectedRow() != -1) {
                    SedesDTO sede = sedescon.findById(bs.getCpDTO().get(tablaDatos.getSelectedRow()).getCodigoPartido());
                    sedescon.getSedesDTOInCsv(sede);
                }
            }
        });

        tablaGraficos.setModel(new DefaultTableModel(
                new Object[][]{
                        {"Ticker"},
                        {"Despliega 4"},
                        {"Sedes"},
                        {"Votantes"}
                },
                new String[]{"GRÁFICOS"}
        ) {
            boolean[] canEdit = new boolean[]{
                    false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        tablaGraficos.addHierarchyListener(this::tablaGraficosHierarchyChanged);
        jScrollPane3.setViewportView(tablaGraficos);
        if (tablaGraficos.getColumnModel().getColumnCount() > 0) {
            tablaGraficos.getColumnModel().getColumn(0).setResizable(false);
        }

        tablaProvincias.setModel(new DefaultTableModel(
                new Object[][]{
                        {null},
                        {null},
                        {null},
                        {null}
                },
                new String[]{"CIRCUNSCRIPCIONES"}
        ) {
            boolean[] canEdit = new boolean[]{
                    false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane4.setViewportView(tablaProvincias);
        if (tablaProvincias.getColumnModel().getColumnCount() > 0) {
            tablaProvincias.getColumnModel().getColumn(0).setResizable(false);
        }

        btnEntra.setBackground(new Color(153, 255, 153));
        btnEntra.setText("ENTRA");
        btnEntra.addActionListener(this::btnEntraActionPerformed);

        btnSale.setBackground(new Color(255, 102, 102));
        btnSale.setText("SALE");
        btnSale.addActionListener(this::btnSaleActionPerformed);

        btnReset.setBackground(new Color(153, 0, 51));
        btnReset.setForeground(new Color(255, 255, 255));
        btnReset.setText("RESET");
        btnReset.addActionListener(this::btnResetActionPerformed);

        btnCongresoSondeo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnCongresoSondeo.setText("SONDEO CONGRESO");

        btnCongresoSondeo.addActionListener(this::btnCongresoSondeoActionPerformed);

        btnDatosSenado.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnDatosSenado.setText("SENADO");
        btnDatosSenado.addActionListener(this::btnDatosSenadoActionPerformed);

        btnDatosCongreso.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnDatosCongreso.setText("OFICIALES CONGRESO");
        btnDatosCongreso.addActionListener(this::btnDatosCongresoActionPerformed);

        btnDesplegarDirecto.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnDesplegarDirecto.setText("DESPLEGAR DIRECTO");
        btnDesplegarDirecto.addActionListener(this::btnDesplegarDirectoActionPerformed);

        btnDesplegarVideo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnDesplegarVideo.setText("DESPLEGAR VIDEO");
        btnDesplegarVideo.addActionListener(this::btnDesplegarVideoActionPerformed);

        btnReplegar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnReplegar.setText("REPLEGAR");
        btnReplegar.addActionListener(this::btnReplegarActionPerformed);

        jCheckBox1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jCheckBox1.setText("ACTUALIZAR ");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel2.setText("ESCRUTADO:");

        lblEscrutado.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblEscrutado.setText("---");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel3.setText("PARTICIPACION:");

        lblParticipacion.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblParticipacion.setText("---");

        btnAvance1.setText("AVANCE 1");
        btnAvance1.addActionListener(this::btnAvance1ActionPerformed);

        btnAvance2.setText("AVANCE 2");
        btnAvance2.addActionListener(this::btnAvance2ActionPerformed);

        btnAvance3.setText("AVANCE 3");
        btnAvance3.addActionListener(this::btnAvance3ActionPerformed);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel4.setText("PART HISTORICO:");

        lblPartHistorica.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblPartHistorica.setText("---");

        lblEscanosTotales.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblEscanosTotales.setText("---");

        lblEscTotales.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblEscTotales.setText("ESC TOTALES:");

        btnConfig.setIcon(new ImageIcon(getClass().getResource("/imagenes/iconconfig.png"))); // NOI18N
        btnConfig.addActionListener(this::btnConfigActionPerformed);

        btnActualizar.setText("ACTUALIZAR");
        btnActualizar.addActionListener(this::btnActualizarActionPerformed);

        lblConexion.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblConexion.setHorizontalAlignment(SwingConstants.CENTER);
        lblConexion.setText("...");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().
                setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING).
                        addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().
                                addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).
                                        addGroup(layout.createSequentialGroup().
                                                addContainerGap().
                                                addComponent(lblConexion, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).
                                                addGap(18, 18, 18)).
                                        addGroup(layout.createSequentialGroup().
                                                addGap(48, 48, 48).
                                                addComponent(btnConfig, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE).
                                                addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))).
                                addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).
                                        addGroup(layout.createSequentialGroup().
                                                addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).
                                                        addComponent(btnDatosCongreso, GroupLayout.PREFERRED_SIZE, 220, GroupLayout.PREFERRED_SIZE).
                                                        addComponent(btnDatosSenado, GroupLayout.PREFERRED_SIZE, 220, GroupLayout.PREFERRED_SIZE)).
                                                addGap(49, 49, 49).
                                                addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).
                                                        addComponent(btnCongresoSondeo, GroupLayout.PREFERRED_SIZE, 220, GroupLayout.PREFERRED_SIZE)))).
                                addGap(31, 31, 31).
                                addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).
                                        addGroup(layout.createSequentialGroup().
                                                addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE).
                                                addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).
                                                addComponent(lblEscrutado, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)).
                                        addGroup(layout.createSequentialGroup().
                                                addComponent(lblEscTotales).
                                                addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).
                                                addComponent(lblEscanosTotales, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)).
                                        addGroup(layout.createSequentialGroup().
                                                addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).
                                                        addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE).
                                                        addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)).
                                                addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).
                                                        addGroup(layout.createSequentialGroup().
                                                                addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).
                                                                addComponent(lblParticipacion, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE)).
                                                        addGroup(layout.createSequentialGroup().
                                                                addGap(12, 12, 12).
                                                                addComponent(lblPartHistorica, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)))).
                                        addGroup(layout.createSequentialGroup().
                                                addComponent(jCheckBox1, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE).
                                                addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).
                                                addComponent(btnActualizar, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE))).
                                addGap(33, 33, 33).
                                addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).
                                        addComponent(btnAvance2, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE).
                                        addComponent(btnAvance3, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE).
                                        addComponent(btnAvance1, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)).
                                addGap(24, 24, 24)).
                        addGroup(layout.createSequentialGroup().
                                addGap(34, 34, 34).
                                addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING).
                                        addGroup(layout.createSequentialGroup().
                                                addComponent(btnReset, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE).
                                                addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 144, Short.MAX_VALUE).
                                                addComponent(btnEntra, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE).
                                                addGap(45, 45, 45).
                                                addComponent(btnSale, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE).
                                                addGap(50, 50, 50).
                                                addComponent(btnDesplegarDirecto).
                                                addGap(30, 30, 30).
                                                addComponent(btnDesplegarVideo).
                                                addGap(30, 30, 30).
                                                addComponent(btnReplegar)).
                                        addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup().
                                                addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING).
                                                        addGroup(layout.createSequentialGroup().
                                                                addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE).
                                                                addGap(19, 19, 19)).
                                                        addGroup(layout.createSequentialGroup().
                                                                addComponent(jScrollPane5, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE).
                                                                addGap(18, 18, 18))).
                                                addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE).
                                                addGap(18, 18, 18).
                                                addComponent(jScrollPane4, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE).
                                                addGap(31, 31, 31).
                                                addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 519, GroupLayout.PREFERRED_SIZE))).
                                addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING).
                        addGroup(layout.createSequentialGroup().
                                addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).
                                        addGroup(layout.createSequentialGroup().
                                                addGap(32, 32, 32).
                                                addComponent(btnConfig, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE).
                                                addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).
                                                addComponent(lblConexion, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)).
                                        addGroup(layout.createSequentialGroup().
                                                addGap(25, 25, 25).
                                                addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                                                        addComponent(jCheckBox1).
                                                        addComponent(btnActualizar)).
                                                addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).
                                                        addGroup(layout.createSequentialGroup().
                                                                addGap(18, 18, 18).
                                                                addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).
                                                                        addComponent(lblEscanosTotales).
                                                                        addComponent(lblEscTotales)).
                                                                addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).
                                                                addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                                                                        addComponent(jLabel2).
                                                                        addComponent(lblEscrutado))).
                                                        addGroup(layout.createSequentialGroup().
                                                                addGap(26, 26, 26).
                                                                addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                                                                        addComponent(btnDatosCongreso, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE).
                                                                        addComponent(btnCongresoSondeo, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)).
                                                                addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).
                                                                addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                                                                        addComponent(btnDatosSenado, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE).
                                                                        addComponent(jLabel3).
                                                                        addComponent(lblParticipacion))))).
                                        addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().
                                                addContainerGap().
                                                addComponent(btnAvance1).
                                                addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).
                                                addComponent(btnAvance2).
                                                addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).
                                                addComponent(btnAvance3))).
                                addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).
                                addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                                        addComponent(lblPartHistorica).
                                        addComponent(jLabel4)).
                                addGap(18, 18, 18).
                                addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).
                                        addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE).
                                        addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE).
                                        addComponent(jScrollPane4, GroupLayout.PREFERRED_SIZE, 341, GroupLayout.PREFERRED_SIZE).
                                        addGroup(layout.createSequentialGroup().
                                                addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE).
                                                addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).
                                                addComponent(jScrollPane5, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE))).
                                addGap(31, 31, 31).
                                addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                                        addComponent(btnEntra, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE).
                                        addComponent(btnSale, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE).
                                        addComponent(btnReset).
                                        addComponent(btnDesplegarDirecto).
                                        addComponent(btnDesplegarVideo).
                                        addComponent(btnReplegar)).
                                addGap(19, 19, 19))
        );
        tablaGraficos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String codAutonomia;
                if (tablaGraficos.getSelectedRow() == 1) {
                    vaciarTablas();
                    BrainStormDTO temp = bs;
                    temp.setCpDTO(bs.getCpDTO().subList(0, 4));
                    showDataTable(temp);
                } else if (tablaGraficos.getSelectedRow() == 2) {
                    vaciarTablas();
                    codAutonomia = "9900000";
                    bs = bscon.getBrainStormDTOOficial(codAutonomia, avance);
                    showDataTable(bs);
                } else if (tablaGraficos.getSelectedRow() != -1 && tablaComunidades.getSelectedRow() == -1) {
                    if (tablaComunidades.getRowCount() == 0) {
                        rellenarCCAA();
                    }
                    codAutonomia = "9900000";
                    if (tipoElecciones == 1 || tipoElecciones == 2) {
                        if (oficiales) {
                            bs = bscon.getBrainStormDTOOficial(codAutonomia, avance);
                            bscon.getBrainStormDTOOficialCongresoInCsv(bs);
                        } else {
                            bs = bscon.getBrainStormDTOSondeo(codAutonomia, avance);
                            bscon.getBrainStormDTOSondeoEspecialInCsv(bs);
                        }
                    } else {
                        ((DefaultTableModel) tablaDatos.getModel()).setRowCount(0);
                        bs = bscon.getBrainStormDTOSenado(codAutonomia, avance);
                        bscon.getBrainStormDTOSenadoInCsv(bs);
                    }
                    showDataTable(bs);
                }
            }
        });
        pack();

    }

    private void btnDesplegarVideoActionPerformed(ActionEvent actionEvent) {
        if (tipoElecciones == 1 || tipoElecciones == 2) {
            int position = tablaDatos.getSelectedRow();
            if (position != -1) {
                String codPartido = bs.getCpDTO().get(position).getCodigoPartido();
                ipf.esDirecto(false, tipoElecciones);
                ipf.despliego(codPartido);
            }
        }
    }

    private void btnReplegarActionPerformed(ActionEvent actionEvent) {
        if (tipoElecciones == 1 || tipoElecciones == 2) {
            int position = tablaDatos.getSelectedRow();
            if (position != -1) {
                String codPartido = bs.getCpDTO().get(position).getCodigoPartido();
                ipf.repliego(codPartido);
            }
        }
    }

    private void btnDesplegarDirectoActionPerformed(ActionEvent actionEvent) {
        if (tipoElecciones == 1 || tipoElecciones == 2) {
            int position = tablaDatos.getSelectedRow();
            if (position != -1) {
                String codPartido = bs.getCpDTO().get(position).getCodigoPartido();
                ipf.esDirecto(true, tipoElecciones);
                ipf.despliego(codPartido);
            }
        }
    }

    private void loadSelectedCongreso(String cod) {
        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // establece todas las celdas no editables
            }
        };
        tableModel.addColumn("PROVINCIAS");

        var circunscripciones = provincias.stream().filter(x -> x.getCodigo().startsWith(cod.substring(0, 2))).toList();
        for (Circunscripcion cir : circunscripciones) {
            if (!cir.getCodigo().endsWith("00000"))
                tableModel.addRow(new Object[]{cir.getNombreCircunscripcion()});
        }
        //JScrollPane scrollPane = new JScrollPane(tablaProvincias);
        tablaProvincias = new JTable(tableModel);
        tablaComunidades.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jScrollPane4.setViewportView(tablaProvincias);

        tablaProvincias.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tablaProvincias.getSelectedRow();
                if (selectedRow != -1) {
                    String nombreMunicipio = (String) tablaProvincias.getValueAt(selectedRow, 0);
                    String codProvincia = provincias.stream().filter(x -> x.getNombreCircunscripcion().equalsIgnoreCase(nombreMunicipio)).toList().get(0).getCodigo();
                    if (tipoElecciones == 1 || tipoElecciones == 2) {
                        if (oficiales) {
                            bs = bscon.getBrainStormDTOOficial(codProvincia, avance);
                            bscon.getBrainStormDTOOficialCongresoInCsv(bs);
                        } else {
                            bs = bscon.getBrainStormDTOSondeo(codProvincia, avance);
                            bscon.getBrainStormDTOSondeoEspecialInCsv(bs);
                        }
                    } else {
                        bs = bscon.getBrainStormDTOSenado(codProvincia, avance);
                        bscon.getBrainStormDTOSenadoInCsv(bs);
                    }
                    showDataTable(bs);
                }
            }
        });
    }

    private void btnEntraActionPerformed(ActionEvent evt) {
        switch (tipoElecciones) {
            //OFICIALES CONGRESO
            case 1 -> {
                switch (tablaGraficos.getSelectedRow()) {
                    //TICKER
                    case 0 -> {
                        if (resCongresoSonIn) {
                            ipf.deSondeoACongreso();
                            resCongresoSonIn = false;
                        } else if (resSenadoIn) {
                            ipf.deSenadoACongreso();
                            resSenadoIn = false;
                        } else {
                            ipf.congresoEntra();
                        }
                        resCongresoOfiIn = true;
                    }
                    case 1 -> {
                        if (resCongresoSonIn || resSenadoIn || resCongresoOfiIn) {
                            ipf.cuatroPrimeros();
                        }
                    }
                    //SEDES
                    case 2 -> {
                        System.out.println("SEDES");
                    }
                    //VOTANTES
                    case 3 -> {
                        System.out.println("VOTANTES");
                    }
                    default -> System.out.print("");
                }
            }
            //SONDEO CONGRESO
            case 2 -> {
                switch (tablaGraficos.getSelectedRow()) {
                    //RESULTADOS
                    case 0 -> {
                        if (resCongresoOfiIn) {
                            ipf.congresoSale();
                            resCongresoOfiIn = false;
                            ipf.congresoSondeoEntra();
                        } else if (resSenadoIn) {
                            ipf.senadoSale();
                            resSenadoIn = false;
                            ipf.congresoSondeoEntra();
                        } else {
                            ipf.congresoSondeoEntra();
                        }
                        resCongresoSonIn = true;
                    }
                    case 1 -> {
                        if (resCongresoSonIn || resSenadoIn || resCongresoOfiIn) {
                            ipf.cuatroPrimeros();
                        }
                    }
                    //SEDES
                    case 2 -> {
                        System.out.println("SEDES");
                    }
                    //VOTANTES
                    case 3 -> {
                        System.out.println("VOTANTES");
                    }
                    default -> System.out.print("");
                }
            }
            //SENADO
            case 3 -> {
                switch (tablaGraficos.getSelectedRow()) {
                    //RESULTADOS
                    case 0 -> {
                        if (resCongresoOfiIn) {
                            ipf.deCongresoASenado();
                            resCongresoOfiIn = false;
                        } else if (resCongresoSonIn) {
                            ipf.congresoSondeoSale();
                            resCongresoSonIn = false;
                            ipf.senadoEntra();
                        } else {
                            ipf.senadoEntra();
                        }
                        resSenadoIn = true;
                    }
                    case 1 -> {
                        if (resCongresoSonIn || resSenadoIn || resCongresoOfiIn) {
                            ipf.cuatroPrimeros();
                        }
                    }
                    //SEDES
                    case 2 -> {
                        System.out.println("SEDES");
                    }
                    //VOTANTES
                    case 3 -> {
                        System.out.println("VOTANTES");
                    }
                    default -> System.out.print("");
                }
            }
        }
    }

    private void btnSaleActionPerformed(ActionEvent evt) {
        switch (tipoElecciones) {
            //OFICIALES CONGRESO
            case 1 -> {
                switch (tablaGraficos.getSelectedRow()) {
                    //TICKER
                    case 0 -> {
                        ipf.congresoSale();
                        resCongresoOfiIn = false;
                    }
                    //DESPLIEGA
                    case 1 -> ipf.recuperoTodos();
                    //SEDES
                    case 2 -> {
                        System.out.println("Sale sedes");
                    }
                    //VOTANTES
                    case 3 -> {
                        System.out.println("Sale votantes");
                    }
                    default -> System.out.print("");
                }
            }
            //CONGRESO SONDEO
            case 2 -> {
                switch (tablaGraficos.getSelectedRow()) {
                    //RESULTADOS
                    case 0 -> {
                        ipf.congresoSondeoSale();
                        resCongresoSonIn = false;
                    }
                    //DESPLIEGA
                    case 1 -> ipf.recuperoTodos();
                    //SEDES
                    case 2 -> {
                        System.out.println("Sale sedes");
                    }
                    //VOTANTES
                    case 3 -> {
                        System.out.println("Sale votantes");
                    }
                    default -> System.out.print("");
                }
            }
            //SENADO
            case 3 -> {
                switch (tablaGraficos.getSelectedRow()) {
                    //RESULTADOS
                    case 0 -> {
                        ipf.senadoSale();
                        resSenadoIn = false;
                    }
                    //DESPLIEGA
                    case 1 -> ipf.recuperoTodos();
                    //SEDES
                    case 2 -> {
                        System.out.println("Sale sedes");
                    }
                    //VOTANTES
                    case 3 -> {
                        System.out.println("Sale votantes");
                    }
                    default -> System.out.print("");
                }
            }
        }
    }

    private void btnDatosSenadoActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnDatosSenadoActionPerformed
        vaciarTablas();
        tablaGraficos.clearSelection();
        tipoElecciones = 3;
        oficiales = true;
        resaltarBoton(btnDatosSenado);
        selectedDb = "SM";
        //rellenarCCAA();
    }

    private void btnCongresoSondeoActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnCongresoSondeoActionPerformed
        vaciarTablas();
        tablaGraficos.clearSelection();
        tipoElecciones = 2;
        oficiales = false;
        resaltarBoton(btnCongresoSondeo);
        selectedDb = "DA";
        rellenarCCAA();
    }

    private void btnDatosCongresoActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnDatosCongresoActionPerformed
        vaciarTablas();
        tablaGraficos.clearSelection();
        tipoElecciones = 1;
        oficiales = true;
        resaltarBoton(btnDatosCongreso);
        selectedDb = "DM";
        rellenarCCAA();
    }

    private void vaciarTablas() {
        ((DefaultTableModel) tablaComunidades.getModel()).setRowCount(0);
        ((DefaultTableModel) tablaProvincias.getModel()).setRowCount(0);
        ((DefaultTableModel) tablaDatos.getModel()).setRowCount(0);
    }

    private void rellenarCCAA() {
        vaciarTablas();
        DefaultTableModel tableModel = (DefaultTableModel) tablaComunidades.getModel();
        List<String> ccaa = autonomias.stream().map(Circunscripcion::getNombreCircunscripcion).toList();
        ccaa = ccaa.subList(0, ccaa.size() - 1);
        for (String s : ccaa) {
            tableModel.addRow(new Object[]{s});
        }
        tablaComunidades.setModel(tableModel);
    }

    private void tablaGraficosHierarchyChanged(HierarchyEvent evt) {
    }

    private void btnAvance1ActionPerformed(ActionEvent evt) {
        resaltarBotonAvances(btnAvance1);
        avance = "1";
    }

    private void btnAvance2ActionPerformed(ActionEvent evt) {
        resaltarBotonAvances(btnAvance2);
        avance = "2";
    }

    private void btnAvance3ActionPerformed(ActionEvent evt) {
        resaltarBotonAvances(btnAvance3);
        avance = "3";
    }

    private void btnResetActionPerformed(ActionEvent evt) {
        ipf.reset();
        vaciarTablas();
        tablaGraficos.clearSelection();
        resSenadoIn = false;
        resCongresoOfiIn = false;
        resCongresoSonIn = false;
    }

    private void btnConfigActionPerformed(ActionEvent evt) {

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int screenWidth = gd.getDisplayMode().getWidth();
        int screenHeight = gd.getDisplayMode().getHeight();


        File configFile = new File(CONFIG_FILE_PATH);

        if (configFile.exists()) {
            // El archivo ya existe
            JFrame config;
            try {
                config = new ConfigView(lblConexion);
                config.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                config.setLocation(screenWidth / 4, screenHeight / 4);
                config.setVisible(true);

            } catch (IOException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            // El archivo no existe, lo creamos
            try {

                JOptionPane.showMessageDialog(null, "El archivo se ha creado correctamente", "Archivo creado", JOptionPane.INFORMATION_MESSAGE);

                configFile.getParentFile().mkdirs();
                configFile.createNewFile();

                Properties properties = new Properties();
                properties.setProperty("ipIPF", "172.17.35.81");
                properties.setProperty("puertoIPF", "5123");
                properties.setProperty("BDReserva", "172.28.51.22");
                properties.setProperty("BDPrincipal", "172.28.51.21");
                properties.setProperty("BDCartones", "<CARTONES>");
                properties.setProperty("BDFaldones", "<FALDONES>");
                properties.setProperty("rutaFicheros", "C:\\Elecciones2023\\DATOS");
                properties.setProperty("rutaColores", "C:\\Elecciones2023\\DATOS\\COLORES\\ColoresPartidos.csv");


                FileOutputStream fos = new FileOutputStream(configFile);
                properties.store(fos, "#Archivo de configuracion");
                fos.close();

                System.out.println("Archivo creado en la ruta: " + configFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }//GEN-LAST:event_btnConfigActionPerformed

    private void btnActualizarActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        //graficosController.update();
    }

    private void resaltarBoton(JButton boton) {
        // Desactivar el resaltado del botón anteriormente seleccionado
        if (botonSeleccionado != null) {
            botonSeleccionado.setBackground(null);
            botonSeleccionado.setOpaque(false);
        }

        // Activar el resaltado del botón recién seleccionado
        botonSeleccionado = boton;
        botonSeleccionado.setBackground(Color.YELLOW);
        botonSeleccionado.setOpaque(true);
    }

    private void resaltarBotonAvances(JButton boton) {
        // Desactivar el resaltado del botón anteriormente seleccionado
        if (botonSeleccionado2 != null) {
            botonSeleccionado2.setBackground(null);
            botonSeleccionado2.setOpaque(false);
        }

        // Activar el resaltado del botón recién seleccionado
        botonSeleccionado2 = boton;
        botonSeleccionado2.setBackground(new Color(173, 216, 230));
        botonSeleccionado2.setOpaque(true);
    }

    private void entraParticipacionEsp() {
        vaciarTablas();
        rellenarCCAA();
        try {
            printDataEsp();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private JTable tablaGraficos;
    private JButton btnActualizar;
    private JButton btnAvance1;
    private JButton btnAvance2;
    private JButton btnAvance3;
    private JButton btnConfig;
    private JButton btnDatosSenado;
    private JButton btnDatosCongreso;
    private JButton btnEntra;
    private JButton btnReset;
    private JButton btnSale;
    private JButton btnDesplegarDirecto;
    private JButton btnDesplegarVideo;
    private JButton btnReplegar;
    private JButton btnCongresoSondeo;
    private JLabel lblConexion;
    private JCheckBox jCheckBox1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JScrollPane jScrollPane3;
    private JScrollPane jScrollPane4;
    private JScrollPane jScrollPane5;
    private JLabel lblEscTotales;
    private JLabel lblEscanosTotales;
    private JLabel lblEscrutado;
    private JLabel lblPartHistorica;
    private JLabel lblParticipacion;
    private JTable tablaComunidades;
    private JTable tablaDatos;
    private JTable tablaProvincias;
}
