
package mgg.code.vista;

import mgg.code.repository.CircunscripcionRepository;
import mgg.code.service.CircunscripcionService;
import mgg.code.util.DB;
import mgg.code.util.Listeners;
import mgg.code.util.ipf.ConexionIPF;
import mgg.code.controller.BrainStormDTOController;
import mgg.code.controller.CPController;
import mgg.code.controller.CircunscripcionController;
import mgg.code.controller.PartidoController;
import mgg.code.model.Circunscripcion;
import mgg.code.model.dto.BrainStormDTO;
import mgg.code.model.dto.CircunscripcionDTO;
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

public class Home extends JFrame {
    String selectedDb = "";
    CircunscripcionController circon = CircunscripcionController.getInstance();
    PartidoController parcon = PartidoController.getInstance();
    CPController cpcon = CPController.getInstance();
    BrainStormDTOController bscon = BrainStormDTOController.getInstance();

    private List<Circunscripcion> autonomias = new ArrayList<>();

    private int tipoElecciones = 2;
    private boolean oficiales = true;
    //TODO:Add booleans para cada grafico
    private String avance = "1";

    private static final String CONFIG_FILE_PATH = "C:\\Elecciones2023\\config.properties";

    private JButton botonSeleccionado = null;
    private JButton botonSeleccionado2 = null;

    public void initCircunscripciones() {
        autonomias = circon.getAllCircunscripciones();
        autonomias = autonomias.stream().filter(x -> x.getCodigo().endsWith("00000")).toList();

    }

    public void showDataTable(BrainStormDTO bs) {
        List<CpData> datos = CpData.fromBrainStormDto(bs);
        printData(datos);
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
        var list = votantesDTO.getCpDTO();
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


    public Home() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                ConexionIPF.getConexion().desconectar();
            }
        });
        //TODO: initListeners();
        initCircunscripciones();
        initComponents();
        resaltarBotonAvances(btnAvance1);
        jCheckBox1.setVisible(false);
        lblConexion.setText(DB.getInstance().getDb());
        resaltarBoton(btnDatosAutonomicas);
        TablaCartones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        TablaFaldones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaDatos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaComunidades.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        TablaCartones.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    tablaComunidades.clearSelection();
                    tablaDatos.clearSelection();
                    TablaFaldones.clearSelection();
                }
            }
        });
        TablaFaldones.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    tablaComunidades.clearSelection();
                    tablaDatos.clearSelection();
                    TablaCartones.clearSelection();
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {


        jScrollPane1 = new JScrollPane();
        tablaDatos = new JTable();
        jScrollPane2 = new JScrollPane();
        tablaComunidades = new JTable();
        jScrollPane3 = new JScrollPane();
        TablaCartones = new JTable();
        jScrollPane4 = new JScrollPane();
        tablaMunicipios = new JTable();
        btnEntra = new JButton();
        btnSale = new JButton();
        btnPactos = new JButton();
        btnReset = new JButton();
        btnReplegar = new JButton();
        btnSondeoAutonomicas = new JButton();
        btnSondeoMunicipales = new JButton();
        btnDatosAutonomicas = new JButton();
        btnDatosMunicipales = new JButton();
        jScrollPane5 = new JScrollPane();
        TablaFaldones = new JTable();

        jCheckBox1 = new JCheckBox();
        ButtonGroup buttonGroup = new ButtonGroup();
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
        cbRegional = new JCheckBox();
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
        rellenarCCAA(2);
        JScrollPane scrollPane = new JScrollPane(tablaComunidades);
        tablaComunidades.getTableHeader().setResizingAllowed(false);
        jScrollPane2.setViewportView(tablaComunidades);
        if (tablaComunidades.getColumnModel().getColumnCount() > 0) {
            tablaComunidades.getColumnModel().getColumn(0).setResizable(false);
        }

        tablaDatos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                if (tablaDatos.getSelectedRow() != -1) {
                    if (TablaFaldones.getSelectedRow() == 2) {
                        if (tablaDatos.getSelectedRow() != -1) {
                            String codigo = tablaDatos.getValueAt(tablaDatos.getSelectedRow(), 0).toString();
                            //TODO: sedescon.descargarInCsv(codigo)
                        }
                    }
                }
            }
        });

        TablaFaldones.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                if (TablaFaldones.getSelectedRow() != -1) {
                    if (TablaFaldones.getSelectedRow() == 1) {
                        //TODO: update();
                    }
                    if (TablaFaldones.getSelectedRow() == 2) {
                        vaciarTablas();
                        // List<CircunscripcionPartido> cps = graficosController.getCpsEspania();
                        // List<CpData> cpdatas = new ArrayList<>();
                        // cps.forEach(cp -> {
                        //     String siglas = graficosController.getPartido(cp.getKey().getPartido()).getSiglas();
                        //     CpData data = CpData.fromCP(cp, siglas);
                        //     cpdatas.add(data);
                        // });
                        // printData(cpdatas);
                    } else if (TablaFaldones.getSelectedRow() == 3) {
                        // graficosController.updateEspania(avance);
                        // vaciarTablas();
                        // printDataVotantes();
                    } else if (TablaFaldones.getSelectedRow() != -1) {
                        rellenarCCAA(tipoElecciones);
                    }
                }
            }
        });

        tablaComunidades.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                if (tablaComunidades.getSelectedRow() != -1) {
                    String codAutonomia;
                    BrainStormDTO bs = null;
                    int selectedRow = tablaComunidades.getSelectedRow();
                    if (selectedRow != -1) {
                        if (tipoElecciones == 2 || tipoElecciones == 4) {
                            codAutonomia = autonomias.stream().filter(aut -> aut.getCodigo().equals(((String) tablaComunidades.getValueAt(selectedRow, 0)).replaceAll(" ", ""))).findFirst().get().getCodigo();
                            if (codAutonomia.equals("1800000") || codAutonomia.equals("1900000")) {
                                if (oficiales) {
                                    if (TablaCartones.getSelectedRow() != 3) {
                                        bs = bscon.getBrainStormDTOOficial(codAutonomia, avance);
                                        if (TablaCartones.getSelectedRow() == 2) {
                                            // graficosController.selectCircunscripcionMapaOficialMuni(codAutonomia, avance);
                                        }
                                        if (TablaFaldones.getSelectedRow() == 0 || TablaFaldones.getSelectedRow() == 3) {
                                            //graficosController.selectCircunscripcionAutonomiaOficialMuni(codAutonomia, avance);
                                        }
                                        if (TablaCartones.getSelectedRow() == 0) {
                                            if (tablaComunidades.getSelectedRow() != -1) {
                                                //  graficosController.descargarResultadosCsvMuniOficial(codAutonomia);
                                                //  graficosController.selectCircunscripcionMapaOficialMuni(codAutonomia, avance);
                                            }
                                        }
                                    }
                                } else {
                                    if (TablaCartones.getSelectedRow() != 3) {
                                        bs = bscon.getBrainStormDTOSondeo(codAutonomia, avance);
                                        if (TablaCartones.getSelectedRow() == 1 || TablaCartones.getSelectedRow() == 2) {
                                            // graficosController.selectCircunscripcionMapaSondeoMuni(codAutonomia, avance);
                                        }
                                        if (TablaFaldones.getSelectedRow() == 0 || TablaFaldones.getSelectedRow() == 3) {
                                            // graficosController.selectCircunscripcionAutnomiaSondeoMuni(codAutonomia, avance);
                                        }
                                        if (TablaCartones.getSelectedRow() == 0) {
                                            if (tablaComunidades.getSelectedRow() != -1) {
                                                //  graficosController.descargarResultadosCsvMuniSondeo(codAutonomia);
                                                // graficosController.selectCircunscripcionMapaSondeoMuni(codAutonomia, avance);
                                            }
                                        }
                                    }
                                }
                                showDataTable(bs);
                            } else {
                                if (oficiales) {
                                    if (TablaCartones.getSelectedRow() != 3) {
                                        // bs = bscon.getBrainStormDTOOficialSenado(codAutonomia, avance);
                                        if (TablaCartones.getSelectedRow() == 1 || TablaCartones.getSelectedRow() == 2) {
                                            // graficosController.selectCircunscripcionMapaOficialAuto(codAutonomia, avance);
                                        }
                                        if (TablaFaldones.getSelectedRow() == 0 || TablaFaldones.getSelectedRow() == 3) {
                                            // graficosController.selectCircunscripcionAutonomiaOficialAuto(codAutonomia, avance);
                                        }
                                        if (TablaCartones.getSelectedRow() == 0) {
                                            if (tablaComunidades.getSelectedRow() != -1) {
                                                // String nombreCCAA = tablaComunidades.getValueAt(tablaComunidades.getSelectedRow(), 0).toString();
                                                // String codigo = nombreCodigoAuto.get(nombreCCAA);
                                                // graficosController.descargarResultadosCsvAutoOficial(codigo);
                                                // graficosController.selectCircunscripcionMapaOficialAuto(codigo, avance);
                                            }
                                        }
                                    } else {
                                        //  bs = bscon.getBrainStormDTOOficialAuto("9900000", avance).execute().body();
                                        //   graficosController.selectCircunscripcionMapaOficialAuto("9900000", avance);
                                    }
                                } else {
                                    if (TablaCartones.getSelectedRow() != 3) {
                                        // bs = clienteApi.getBrainStormDTOSondeoAuto(codAutonomia, avance).execute().body();
                                        if (TablaCartones.getSelectedRow() == 2) {
                                            //    graficosController.selectCircunscripcionMapaSondeoAuto(codAutonomia, avance);
                                        }
                                        if (TablaFaldones.getSelectedRow() == 0 || TablaFaldones.getSelectedRow() == 3) {
                                            //   graficosController.selectCircunscripcionAutnomiaSondeoAuto(codAutonomia, avance);
                                        }
                                        if (TablaCartones.getSelectedRow() == 0) {
                                            if (tablaComunidades.getSelectedRow() != -1) {
                                                //    String nombreCCAA = tablaComunidades.getValueAt(tablaComunidades.getSelectedRow(), 0).toString();
                                                //    String codigo = nombreCodigoAuto.get(nombreCCAA);
                                                //    graficosController.descargarResultadosCsvAutoSondeo(codigo);
                                                //    graficosController.selectCircunscripcionMapaSondeoAuto(codigo, avance);
                                            }
                                        }
                                    } else {
                                        //  bs = clienteApi.getBrainStormDTOSondeoAuto("9900000", avance).execute().body();
                                        //  graficosController.selectCircunscripcionMapaSondeoAuto("9900000", avance);
                                    }
                                }
                                showDataTable(bs);
                            }
                        } else {
                            codAutonomia = autonomias.stream().filter(aut -> aut.getCodigo().equals(((String) tablaComunidades.getValueAt(selectedRow, 0)).replaceAll(" ", ""))).findFirst().get().getCodigo();
                            ((DefaultTableModel) tablaDatos.getModel()).setRowCount(0);
                            if (oficiales) {
                                if (TablaCartones.getSelectedRow() != 3) {
                                    // bs = clienteApi.getBrainStormDTOOficialMuni(codAutonomia, avance).execute().body();
                                    if (TablaCartones.getSelectedRow() == 1) {
                                        //   String nombreCCAA = tablaComunidades.getValueAt(tablaComunidades.getSelectedRow(), 0).toString();
                                        //   String codigo = nombreCodigo.get(nombreCCAA);
                                        //   graficosController.selectCircunscripcionMapaOficialMuni(codigo, avance);
                                    }
                                    if (TablaFaldones.getSelectedRow() == 0 || TablaFaldones.getSelectedRow() == 3) {
                                        //    graficosController.selectCircunscripcionAutonomiaOficialMuni(codAutonomia);
                                    }
                                    if (TablaCartones.getSelectedRow() == 0) {
                                        if (tablaComunidades.getSelectedRow() != -1) {
                                            //    String nombreCCAA = tablaComunidades.getValueAt(tablaComunidades.getSelectedRow(), 0).toString();
                                            //    String codigo = nombreCodigo.get(nombreCCAA);
                                            //    graficosController.descargarResultadosCsvMuniOficial(codigo);
                                        }
                                    }
                                } else {
                                    //  bs = clienteApi.getBrainStormDTOOficialMuni("9900000", avance).execute().body();
                                    //  graficosController.selectCircunscripcionMapaOficialMuni("9900000", avance);
                                }
                            } else {
                                if (TablaCartones.getSelectedRow() != 3) {
                                    // bs = clienteApi.getBrainStormDTOSondeoMuni(codAutonomia, avance).execute().body();
                                    if (TablaCartones.getSelectedRow() == 1) {
                                        //   String nombreCCAA = tablaComunidades.getValueAt(tablaComunidades.getSelectedRow(), 0).toString();
                                        //    String codigo = nombreCodigo.get(nombreCCAA);
                                        //    graficosController.selectCircunscripcionMapaSondeoMuni(codigo, avance);
                                    }
                                    if (TablaFaldones.getSelectedRow() == 0 || TablaFaldones.getSelectedRow() == 3) {
                                        //graficosController.selectCircunscripcionAutnomiaSondeoMuni(codAutonomia);
                                    }
                                    if (TablaCartones.getSelectedRow() == 0) {
                                        if (tablaComunidades.getSelectedRow() != -1) {
                                            //      String nombreCCAA = tablaComunidades.getValueAt(tablaComunidades.getSelectedRow(), 0).toString();
                                            //      String codigo = nombreCodigo.get(nombreCCAA);
                                            //      graficosController.descargarResultadosCsvMuniSondeo(codigo);
                                        }
                                    }
                                } else {
                                    //   bs = clienteApi.getBrainStormDTOSondeoMuni("9900000", avance).execute().body();
                                    //   graficosController.selectCircunscripcionMapaSondeoMuni("9900000", avance);
                                }
                            }
                        }
                        lblEscrutado.setText(bs.getCircunscripcion().getEscrutado() + "");
                        lblParticipacion.setText(bs.getCircunscripcion().getParticipacion() + "");
                        lblPartHistorica.setText(bs.getCircunscripcion().getParticipacionHistorico() + "");
                        lblEscanosTotales.setText(bs.getCircunscripcion().getEscanios() + "");
                    }
                }
            }
            if (tablaComunidades.getColumnModel().getColumnCount() > 0) {
                tablaComunidades.getColumnModel().getColumn(0).setResizable(false);
            }
        });

        TablaCartones.setModel(new DefaultTableModel(
                new Object[][]

                        {
                                {"Resultados"},
                                {"Participación"},
                                {"Arco"},
                                {"Participacion España"}
                        },
                new String[]

                        {"CARTONES"}
        ) {
            boolean[] canEdit = new boolean[]{
                    false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        TablaCartones.addHierarchyListener(new HierarchyListener() {
            public void hierarchyChanged(HierarchyEvent evt) {
                TablaCartonesHierarchyChanged(evt);
            }
        });
        jScrollPane3.setViewportView(TablaCartones);
        if (TablaCartones.getColumnModel().

                getColumnCount() > 0) {
            TablaCartones.getColumnModel().getColumn(0).setResizable(false);
        }

        tablaMunicipios.setModel(new DefaultTableModel(
                new Object[][]
                        {
                                {null},
                                {null},
                                {null},
                                {null}
                        },
                new String[]

                        {
                                "CIRCUNSCRIPCIONES"
                        }
        ) {
            boolean[] canEdit = new boolean[]{
                    false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane4.setViewportView(tablaMunicipios);
        if (tablaMunicipios.getColumnModel().

                getColumnCount() > 0) {
            tablaMunicipios.getColumnModel().getColumn(0).setResizable(false);
        }

        btnEntra.setBackground(new Color(153, 255, 153));
        btnEntra.setText("ENTRA");
        btnEntra.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //  btnEntraActionPerformed(evt);
            }
        });

        btnSale.setBackground(new Color(255, 102, 102));
        btnSale.setText("SALE");
        btnSale.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                // btnSaleActionPerformed(evt);
            }
        });

        btnPactos.setText("PACTOS");
        btnPactos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //  btnPactosActionPerformed(evt);
            }
        });

        btnReset.setBackground(new Color(153, 0, 51));
        btnReset.setForeground(new Color(255, 255, 255));
        btnReset.setText("RESET");
        btnReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                // btnResetActionPerformed(evt);
            }
        });

        btnReplegar.setBackground(new Color(255, 153, 51));
        btnReplegar.setText("REPLEGAR");
        btnReplegar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //  btnReplegarActionPerformed(evt);
            }
        });

        btnSondeoAutonomicas.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnSondeoAutonomicas.setText("SONDEO AUTONOMICAS");
        btnSondeoAutonomicas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnSondeoAutonomicasActionPerformed(evt);
                //TablaCartones.getSelectionModel().clearSelection();
            }
        });

        btnSondeoMunicipales.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnSondeoMunicipales.setText("SONDEO MUNICIPALES");
        btnSondeoMunicipales.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnSondeoMunicipalesActionPerformed(evt);
                //TablaCartones.getSelectionModel().clearSelection();
            }
        });

        btnDatosAutonomicas.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnDatosAutonomicas.setText("DATOS AUTONOMICAS");
        btnDatosAutonomicas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnDatosAutonomicasActionPerformed(evt);
                //TablaCartones.getSelectionModel().clearSelection();
            }
        });

        btnDatosMunicipales.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnDatosMunicipales.setText("DATOS MUNICIPALES");
        btnDatosMunicipales.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnDatosMunicipalesActionPerformed(evt);
                //TablaCartones.getSelectionModel().clearSelection();
            }
        });

        TablaFaldones.setModel(new DefaultTableModel(
                new Object[][]

                        {
                                {"Inferior"},
                                {"Lateral"},
                                {"Sedes"},
                                {"Votantes"}
                        },
                new String[]

                        {
                                "FALDONES"
                        }
        ) {
            boolean[] canEdit = new boolean[]{
                    false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane5.setViewportView(TablaFaldones);
        if (TablaFaldones.getColumnModel().

                getColumnCount() > 0) {
            TablaFaldones.getColumnModel().getColumn(0).setResizable(false);
        }

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
        btnAvance1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnAvance1ActionPerformed(evt);
            }
        });

        btnAvance2.setText("AVANCE 2");
        btnAvance2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnAvance2ActionPerformed(evt);
            }
        });

        btnAvance3.setText("AVANCE 3");
        btnAvance3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnAvance3ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel4.setText("PART HISTORICO:");

        lblPartHistorica.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblPartHistorica.setText("---");

        lblEscanosTotales.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblEscanosTotales.setText("---");

        lblEscTotales.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblEscTotales.setText("ESC TOTALES:");

        btnConfig.setIcon(new ImageIcon(getClass().getResource("/imagenes/iconconfig.png"))); // NOI18N
        btnConfig.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnConfigActionPerformed(evt);
            }
        });

        btnActualizar.setText("ACTUALIZAR");
        btnActualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        lblConexion.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblConexion.setHorizontalAlignment(SwingConstants.CENTER);
        lblConexion.setText("...");

        cbRegional.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        cbRegional.setText("REGIONAL");


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
                                                        addComponent(btnDatosMunicipales, GroupLayout.PREFERRED_SIZE, 220, GroupLayout.PREFERRED_SIZE).
                                                        addComponent(btnDatosAutonomicas, GroupLayout.PREFERRED_SIZE, 220, GroupLayout.PREFERRED_SIZE)).
                                                addGap(49, 49, 49).
                                                addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).
                                                        addComponent(btnSondeoAutonomicas, GroupLayout.PREFERRED_SIZE, 220, GroupLayout.PREFERRED_SIZE).
                                                        addComponent(btnSondeoMunicipales, GroupLayout.PREFERRED_SIZE, 220, GroupLayout.PREFERRED_SIZE))).
                                        addComponent(cbRegional)).
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
                                                addGap(45, 45, 45).
                                                addComponent(btnReplegar, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE).
                                                addGap(136, 136, 136).
                                                addComponent(btnPactos, GroupLayout.PREFERRED_SIZE, 122, GroupLayout.PREFERRED_SIZE).
                                                addGap(33, 33, 33)).
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
                                                        addComponent(btnActualizar).
                                                        addComponent(cbRegional)).
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
                                                                        addComponent(btnDatosMunicipales, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE).
                                                                        addComponent(btnSondeoMunicipales, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)).
                                                                addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).
                                                                addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                                                                        addComponent(btnSondeoAutonomicas, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE).
                                                                        addComponent(btnDatosAutonomicas, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE).
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
                                        addComponent(btnPactos, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE).
                                        addComponent(btnReplegar).
                                        addComponent(btnReset)).
                                addGap(19, 19, 19))
        );
        TablaCartones.getSelectionModel().

                addListSelectionListener(e ->

                {
                    if (!e.getValueIsAdjusting()) {
                        if (TablaCartones.getSelectedRow() != -1) {
                            if (TablaCartones.getSelectedRow() == 3) {
                                entreParticipacionEsp();
                            } else if (TablaCartones.getSelectedRow() != -1) {
                                rellenarCCAA(tipoElecciones);
                            }
                        }
                    }
                });

        pack();

    }// </editor-fold>//GEN-END:initComponents

    /*private void loadSelectedAutonomicas(String cod) {
        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // establece todas las celdas no editables
            }
        };
        tableModel.addColumn("CIRCUNSCRIPCIONES");
        var municipios = circunscripcionesAutonomicas.get(cod);
        municipios = municipios.stream().distinct().collect(Collectors.toList());
        municipios.sort(Comparator.comparing(Circunscripcion::getCodigo));
        for (Circunscripcion municipio : municipios) {
            if (!municipio.getCodigo().endsWith("00000"))
                tableModel.addRow(new Object[]{municipio.getNombreCircunscripcion()});
        }
        JScrollPane scrollPane = new JScrollPane(tablaMunicipios);
        tablaMunicipios = new JTable(tableModel);
        tablaComunidades.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jScrollPane4.setViewportView(tablaMunicipios);

        tablaMunicipios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tablaMunicipios.getSelectedRow();
                if (selectedRow != -1) {
                    String nombreMunicipio = (String) tablaMunicipios.getValueAt(selectedRow, 0);
                    String codMunicipio;
                    BrainStormDTO bs = null;
                    isComunidad = false;
                    isMunicipio = true;
                    if (tipoElecciones == 2 || tipoElecciones == 4) {
                        codMunicipio = nombreCodigoAutonomicas.get(tablaMunicipios.getValueAt(selectedRow, 0));
                        if (oficiales) {
                            try {
                                bs = clienteApi.getBrainStormDTOOficialAuto(codMunicipio, avance).execute().body();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            if (TablaCartones.getSelectedRow() == 1 || TablaCartones.getSelectedRow() == 2) {
                                graficosController.selectCircunscripcionMapaOficialAuto(codMunicipio, avance);
                            }
                            if (TablaFaldones.getSelectedRow() == 0 || TablaFaldones.getSelectedRow() == 3) {
                                graficosController.selectCircunscripcionAutonomiaOficialAuto(codMunicipio, avance);
                            }
                            if (TablaCartones.getSelectedRow() == 0) {
                                graficosController.descargarResultadosCsvAutoOficial(codMunicipio);
                                graficosController.selectCircunscripcionMapaOficialAuto(codMunicipio, avance);
                            }
                        } else {
                            try {
                                bs = clienteApi.getBrainStormDTOSondeoAuto(codMunicipio, avance).execute().body();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            if (TablaCartones.getSelectedRow() == 1 || TablaCartones.getSelectedRow() == 2) {
                                graficosController.selectCircunscripcionMapaSondeoAuto(codMunicipio, avance);
                            }
                            if (TablaFaldones.getSelectedRow() == 0 || TablaFaldones.getSelectedRow() == 3) {
                                graficosController.selectCircunscripcionAutnomiaSondeoAuto(codMunicipio, avance);
                            }
                            if (TablaCartones.getSelectedRow() == 0) {
                                graficosController.descargarResultadosCsvAutoSondeo(codMunicipio);
                                graficosController.selectCircunscripcionMapaSondeoAuto(codMunicipio, avance);
                            }
                        }
                    } else {
                        codMunicipio = nombreCodigoMunicipal.get(tablaMunicipios.getValueAt(selectedRow, 0));
                        if (oficiales) {
                            try {
                                bs = clienteApi.getBrainStormDTOOficialMuni(codMunicipio, avance).execute().body();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            if (TablaCartones.getSelectedRow() == 1 || TablaCartones.getSelectedRow() == 2) {
                                graficosController.selectCircunscripcionMapaOficialMuni(codMunicipio, avance);
                            }
                            if (TablaFaldones.getSelectedRow() == 0 || TablaFaldones.getSelectedRow() == 3) {
                                graficosController.selectCircunscripcionAutonomiaOficialMuni(codMunicipio, avance);
                            }
                            if (TablaCartones.getSelectedRow() == 0) {
                                graficosController.descargarResultadosCsvMuniOficial(codMunicipio);
                                graficosController.selectCircunscripcionMapaOficialMuni(codMunicipio, avance);
                            }
                        } else {
                            try {
                                bs = clienteApi.getBrainStormDTOSondeoMuni(codMunicipio, avance).execute().body();
                                graficosController.selectCircunscripcionMapaSondeoMuni(codMunicipio, avance);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            if (TablaCartones.getSelectedRow() == 1 || TablaCartones.getSelectedRow() == 2) {
                                graficosController.selectCircunscripcionMapaSondeoMuni(codMunicipio, avance);
                            }
                            if (TablaFaldones.getSelectedRow() == 0 || TablaFaldones.getSelectedRow() == 3) {
                                graficosController.selectCircunscripcionAutnomiaSondeoMuni(codMunicipio, avance);
                            }
                            if (TablaCartones.getSelectedRow() == 0) {
                                graficosController.descargarResultadosCsvMuniSondeo(codMunicipio);
                                graficosController.selectCircunscripcionMapaSondeoMuni(codMunicipio, avance);
                            }
                            //graficosController.selectedMunicipalesSondeo(codMunicipio);
                        }
                    }
                    lblEscrutado.setText(bs.getCircunscripcion().getEscrutado() + "");
                    lblParticipacion.setText(bs.getCircunscripcion().getParticipacion() + "");
                    lblPartHistorica.setText(bs.getCircunscripcion().getParticipacionHistorico() + "");
                    lblEscanosTotales.setText(bs.getCircunscripcion().getEscanios() + "");
                    switch (selectedDb) {
                        case "DA" -> showDataTableOficialAutonomicas(bs);
                        case "SA" -> showDataTableSondeoAutonomicas(bs);
                        case "SM" -> showDataTableSondeoMunicipio(bs);
                        default -> showDataTableOficialMunicipio(bs);
                    }
                }
            }
        });
    }*/

   /* private void loadSelectedMunicipales(String cod) {
        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // establece todas las celdas no editables
            }
        };
        tableModel.addColumn("CIRCUNSCRIPCIONES");
        List<Circunscripcion> municipios = new ArrayList<>();
        if (!cbRegional.isSelected()) {
            if (circunscripcionesMunicipales.get(cod).size() != 0) {
                String codComunidad = circunscripcionesMunicipales.get(cod).get(0).getCodigoComunidad();
                municipios = graficosController.filtradasPorMostrarMuni(codComunidad);
            }
        } else {
            municipios = circunscripcionesMunicipales.get(cod.replaceAll(" ", "")).stream().toList();
            municipios = new ArrayList<>(municipios.stream()
                    .distinct()
                    .collect(Collectors.toMap(Circunscripcion::getCodigo, Function.identity(), (municipio1, municipio2) -> municipio1))
                    .values());
            municipios = municipios.stream().filter(muni -> !muni.getCodigo().endsWith("000")).collect(Collectors.toList());
            municipios.sort(Comparator.comparing(Circunscripcion::getCodigo));

            List<Circunscripcion> municipiosSinDuplicados = new ArrayList<>();
            Set<String> nombresUnicos = new HashSet<>();
            for (Circunscripcion municipio : municipios) {
                String nombre = municipio.getNombreCircunscripcion();
                if (!nombresUnicos.contains(nombre)) {
                    nombresUnicos.add(nombre);
                    municipiosSinDuplicados.add(municipio);
                }
            }
            municipios = municipiosSinDuplicados;
        }

        for (Circunscripcion municipio : municipios) {
            tableModel.addRow(new Object[]{municipio.getNombreCircunscripcion()});
        }

        JScrollPane scrollPane = new JScrollPane(tablaMunicipios);
        tablaMunicipios = new JTable(tableModel);
        jScrollPane4.setViewportView(tablaMunicipios);
        tablaMunicipios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tablaMunicipios.getSelectedRow();
                if (selectedRow != -1) {
                    String nombreMunicipio = (String) tablaMunicipios.getValueAt(selectedRow, 0);
                    String codMunicipio;
                    BrainStormDTO bs = null;
                    isComunidad = false;
                    isMunicipio = true;
                    if (tipoElecciones == 2 || tipoElecciones == 4) {
                        codMunicipio = nombreCodigoAutonomicas.get(tablaMunicipios.getValueAt(selectedRow, 0));
                        if (oficiales) {
                            try {
                                bs = clienteApi.getBrainStormDTOOficialAuto(codMunicipio, avance).execute().body();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            if (TablaCartones.getSelectedRow() == 1 || TablaCartones.getSelectedRow() == 2) {
                                graficosController.selectCircunscripcionMapaOficialAuto(codMunicipio, avance);
                            }
                            if (TablaFaldones.getSelectedRow() == 0 || TablaFaldones.getSelectedRow() == 3) {
                                graficosController.selectCircunscripcionAutonomiaOficialAuto(codMunicipio, avance);
                            }
                            if (TablaCartones.getSelectedRow() == 0) {
                                graficosController.descargarResultadosCsvAutoOficial(codMunicipio);
                                graficosController.selectCircunscripcionMapaOficialAuto(codMunicipio, avance);
                            }
                        } else {
                            try {
                                bs = clienteApi.getBrainStormDTOSondeoAuto(codMunicipio, avance).execute().body();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            if (TablaCartones.getSelectedRow() == 1 || TablaCartones.getSelectedRow() == 2) {
                                graficosController.selectCircunscripcionMapaSondeoAuto(codMunicipio, avance);
                            }
                            if (TablaFaldones.getSelectedRow() == 0 || TablaFaldones.getSelectedRow() == 3) {
                                graficosController.selectCircunscripcionAutnomiaSondeoAuto(codMunicipio, avance);
                            }
                            if (TablaCartones.getSelectedRow() == 0) {
                                graficosController.descargarResultadosCsvAutoSondeo(codMunicipio);
                                graficosController.selectCircunscripcionMapaSondeoAuto(codMunicipio, avance);
                            }
                        }
                    } else {
                        codMunicipio = nombreCodigoMunicipal.get(tablaMunicipios.getValueAt(selectedRow, 0));
                        if (oficiales) {
                            try {
                                bs = clienteApi.getBrainStormDTOOficialMuni(codMunicipio, avance).execute().body();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            if (TablaCartones.getSelectedRow() == 1 || TablaCartones.getSelectedRow() == 2) {
                                graficosController.selectCircunscripcionMapaOficialMuni(codMunicipio, avance);
                            }
                            if (TablaFaldones.getSelectedRow() == 0 || TablaFaldones.getSelectedRow() == 3) {
                                graficosController.selectCircunscripcionAutnomiaOficialMuni(codMunicipio, avance);
                            }
                            if (TablaCartones.getSelectedRow() == 0) {
                                graficosController.descargarResultadosCsvMuniOficial(codMunicipio);
                                graficosController.selectCircunscripcionMapaOficialMuni(codMunicipio, avance);
                            }
                        } else {
                            try {
                                graficosController.selectCircunscripcionMapaSondeoMuni(codMunicipio, avance);
                                bs = clienteApi.getBrainStormDTOSondeoMuni(codMunicipio, avance).execute().body();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            if (TablaCartones.getSelectedRow() == 1 || TablaCartones.getSelectedRow() == 2) {
                                graficosController.selectCircunscripcionMapaSondeoMuni(codMunicipio, avance);
                            }
                            if (TablaFaldones.getSelectedRow() == 0 || TablaFaldones.getSelectedRow() == 3) {
                                graficosController.selectCircunscripcionAutnomiaSondeoMuni(codMunicipio, avance);
                            }
                            if (TablaCartones.getSelectedRow() == 0) {
                                graficosController.descargarResultadosCsvMuniSondeo(codMunicipio);
                                graficosController.selectCircunscripcionMapaSondeoMuni(codMunicipio, avance);
                            }
                            //graficosController.selectedMunicipalesSondeo(codMunicipio);
                        }
                    }
                    lblEscrutado.setText(bs.getCircunscripcion().getEscrutado() + "");
                    lblParticipacion.setText(bs.getCircunscripcion().getParticipacion() + "");
                    lblPartHistorica.setText(bs.getCircunscripcion().getParticipacionHistorico() + "");
                    lblEscanosTotales.setText(bs.getCircunscripcion().getEscanios() + "");
                    switch (selectedDb) {
                        case "DA" -> showDataTableOficialAutonomicas(bs);
                        case "SA" -> showDataTableSondeoAutonomicas(bs);
                        case "SM" -> showDataTableSondeoMunicipio(bs);
                        default -> showDataTableOficialMunicipio(bs);
                    }
                }
            }
        });
    }*/

   /* private boolean dejoEntrarEntra() {
        boolean valido = false;
        int rowComunidad = tablaComunidades.getSelectedRow();

        if (rowComunidad != -1) {
            if (tablaMunicipios.getSelectedRow() != -1 || TablaCartones.getSelectedRow() == 1 || tipoElecciones == 2 || tipoElecciones == 4 || nombreCodigo.get(tablaComunidades.getValueAt(rowComunidad, 0)).equals("1800000") || nombreCodigo.get(tablaComunidades.getValueAt(rowComunidad, 0)).equals("1900000")) {
                valido = true;
            }
        }
        if (TablaCartones.getSelectedRow() == 3 || TablaFaldones.getSelectedRow() == 3) {
            valido = true;
        }
        if (TablaFaldones.getSelectedRow() == 2) {
            valido = true;
        }
        return valido;
    }*/

    /*private boolean dejoEntrarPactos() {
        boolean valido = false;
        int rowComunidad = tablaComunidades.getSelectedRow();

        if (rowComunidad != -1) {
            if (tablaMunicipios.getSelectedRow() != -1 || tipoElecciones == 2 || tipoElecciones == 4 || nombreCodigo.get(tablaComunidades.getValueAt(rowComunidad, 0)).equals("1800000") || nombreCodigo.get(tablaComunidades.getValueAt(rowComunidad, 0)).equals("1900000")) {
                valido = true;
            }
        }
        return valido;
    }*/

   /* private void btnEntraActionPerformed(ActionEvent evt) {
        if (dejoEntrarEntra()) {
            switch (tipoElecciones) {
                //OFICIALES MUNICIPALES
                case 1 -> {
                    switch (TablaCartones.getSelectedRow()) {
                        //RESULTADOS
                        case 0 -> {
                            if (!resultadosIn) {
                                if (sacarCartonAnteriorMuni()) {
                                    graficosController.entraResultadosMuniDelay();
                                } else {
                                    graficosController.entraResultadosMuni();
                                }
                                resultadosIn = true;
                            } else if (isComunidad) {
                                graficosController.cambiaResultadosComunidad();
                            } else if (isMunicipio) {
                                graficosController.cambiaResultadosMunicipio();
                            }
                        }
                        //PARTICIPACION
                        case 1 -> {
                            if (!participacionIn) {
                                if (sacarCartonAnteriorMuni()) {
                                    graficosController.entraParticipacionMuniDelay();
                                } else {
                                    graficosController.entraParticipacionMuni();
                                }
                                participacionIn = true;
                            } else if (isComunidad) {
                                graficosController.cambiaParticipacionComunidad();
                            } else if (isMunicipio) {
                                graficosController.cambiaParticipacionMunicipio();
                            }
                        }
                        //ARCO
                        case 2 -> {
                            if (!arcoIn) {
                                if (sacarCartonAnteriorMuni()) {
                                    graficosController.entraArcoMuniDelay();
                                } else {
                                    graficosController.entraArcoMuni();
                                }
                                arcoIn = true;
                            }
                        }
                        case 3 -> {
                            if (!participacionEspIn) {
                                if (sacarCartonAnteriorMuni()) {
                                    graficosController.entraParticipacionEspMuniDelay();

                                } else {
                                    graficosController.entraParticipacionEspMuni();
                                }

                                participacionEspIn = true;
                            }

                        }
                        default -> System.out.print("");
                    }
                    switch (TablaFaldones.getSelectedRow()) {
                        //INFERIOR
                        case 0 -> {
                            if (!inferiorMuniIn && !inferiorAutoIn && !inferiorAutoSondeoIn && !inferiorMuniSondeoIn) {
                                graficosController.entraFaldonMuni();
                                inferiorMuniIn = true;
                            } else if (inferiorAutoIn) {
                                graficosController.deAutoaMuniFaldonAuto();
                                inferiorMuniIn = true;
                                inferiorAutoIn = false;
                            } else if (inferiorMuniIn) {
                                graficosController.encadenaFaldonMuni();
                            } else if (inferiorAutoSondeoIn) {
                                graficosController.deAutoSondeoAMuni();
                                inferiorAutoSondeoIn = false;
                                inferiorMuniIn = true;
                            } else if (inferiorMuniSondeoIn) {
                                graficosController.deMuniSondeoAMuni();
                                inferiorMuniSondeoIn = false;
                                inferiorMuniIn = true;
                            }
                        }
                        //LATERAL
                        case 1 -> {
                            String codCCAA = null;
                            if (tablaComunidades.getSelectedRow() != -1) {
                                codCCAA = nombreCodigoMunicipal.get(tablaComunidades.getValueAt(tablaComunidades.getSelectedRow(), 0)).substring(0, 2);
                                graficosController.actualizaLateralMunicipales(codCCAA);
                            }
                            if (!lateralIn) {
                                graficosController.entraLateralMunicipales();
                                //TODO:Poner lateralIN =false en el sale o al pasar a otro gráfico compatible
                                lateralIn = true;
                            } else {
                                if (codCCAA != null) {
                                    graficosController.despliegaLateralMunicipales(codCCAA);
                                }
                            }
                        }
                        //SEDES
                        case 2 -> graficosController.faldonSedesEntra();
                        //VOTOS MILLONES
                        case 3 -> {
                            if (votantesIn) {
                                graficosController.faldonVotantesHistEntra();
                            } else {
                                graficosController.faldonVotantesEntra();
                                votantesIn = true;
                            }
                        }
                        default -> System.out.print("");
                    }
                }
                //OFICIALES AUTONOMICAS
                case 2 -> {
                    switch (TablaCartones.getSelectedRow()) {
                        //RESULTADOS
                        case 0 -> {
                            if (!resultadosIn) {
                                if (sacarCartonAnteriorAuto()) {
                                    graficosController.entraResultadosAutoDelay();
                                } else {
                                    graficosController.entraResultadosAuto();
                                }
                                resultadosIn = true;
                            } else if (isComunidad) {
                                graficosController.cambiaResultadosComunidad();
                            } else if (isMunicipio) {
                                graficosController.cambiaResultadosMunicipio();
                            }
                        }
                        //PARTICIPACION
                        case 1 -> {
                            if (!participacionIn) {
                                if (sacarCartonAnteriorAuto()) {
                                    graficosController.entraParticipacionAutoDelay();
                                } else {
                                    graficosController.entraParticipacionAuto();
                                }
                                participacionIn = true;
                            } else if (isComunidad) {
                                graficosController.cambiaParticipacionComunidad();
                            } else if (isMunicipio) {
                                graficosController.cambiaParticipacionMunicipio();
                            }
                        }
                        //ARCO
                        case 2 -> {
                            if (!arcoIn) {
                                if (sacarCartonAnteriorAuto()) {
                                    graficosController.entraArcoAutoDelay();
                                } else {
                                    graficosController.entraArcoAuto();
                                }
                                arcoIn = true;
                            }
                        }

                        case 3 -> {
                            if (!participacionEspIn) {
                                if (sacarCartonAnteriorMuni()) {
                                    graficosController.entraParticipacionEspAutoDelay();
                                } else {
                                    graficosController.entraParticipacionEspAuto();
                                }
                                participacionEspIn = true;
                            }
                        }
                        default -> System.out.print("");
                    }
                    switch (TablaFaldones.getSelectedRow()) {
                        //INFERIOR
                        case 0 -> {
                            if (!inferiorMuniIn && !inferiorAutoIn && !inferiorAutoSondeoIn && !inferiorMuniSondeoIn) {
                                graficosController.entraFaldonAuto();
                                inferiorAutoIn = true;
                            } else if (inferiorMuniIn) {
                                graficosController.deMuniaAutoFaldonMuni();
                                inferiorMuniIn = false;
                                inferiorAutoIn = true;
                            } else if (inferiorAutoIn) {
                                graficosController.encadenaFaldonAuto();
                            } else if (inferiorAutoSondeoIn) {
                                graficosController.deAutoSondeoAAuto();
                                inferiorAutoSondeoIn = false;
                                inferiorAutoIn = true;
                            } else if (inferiorMuniSondeoIn) {
                                graficosController.deMuniSondeoAAuto();
                                inferiorMuniSondeoIn = false;
                                inferiorAutoIn = true;
                            }
                        }
                        //LATERAL
                        case 1 -> {
                            String codCCAA = null;
                            if (tablaComunidades.getSelectedRow() != -1) {
                                codCCAA = nombreCodigoMunicipal.get(tablaComunidades.getValueAt(tablaComunidades.getSelectedRow(), 0)).substring(0, 2);
                                graficosController.actualizaLateralAutonomicas(codCCAA);
                            }
                            if (!lateralIn) {
                                graficosController.entraLateralAutonomicas();
                                //TODO:Poner lateralIN =false en el sale o al pasar a otro gráfico compatible
                                lateralIn = true;
                            } else {
                                if (codCCAA != null) {
                                    graficosController.despliegaLateralAutonomicas(codCCAA);
                                }
                            }
                        }
                        //SEDES
                        case 2 -> graficosController.faldonSedesEntra();
                        //VOTOS MILLONES
                        case 3 -> {
                            if (votantesIn) {
                                graficosController.faldonVotantesHistEntra();
                            } else {
                                graficosController.faldonVotantesEntra();
                                votantesIn = true;
                            }
                        }
                        default -> System.out.print("");
                    }
                }
                //SONDEO MUNICIPALES
                case 3 -> {
                    switch (TablaCartones.getSelectedRow()) {
                        //RESULTADOS
                        case 0 -> {
                            if (!resultadosIn) {
                                if (sacarCartonAnteriorMuni()) {
                                    graficosController.entraSondeoResultadosMuniDelay();
                                } else {
                                    graficosController.entraSondeoResultadosMuni();
                                }
                                resultadosIn = true;
                            } else if (isComunidad) {
                                graficosController.cambiaSondeoResultadosComunidad();
                            } else if (isMunicipio) {
                                graficosController.cambiaSondeoResultadosMunicipio();
                            }
                        }
                        //PARTICIPACION
                        case 1 -> {
                        }
                        //          if (!participacionIn) {
                        //              if (sacarCartonAnteriorMuni()) {
                        //                  graficosController.entraParticipacionMuniDelay();
                        //              } else {
                        //                  graficosController.entraParticipacionMuni();
                        //              }
                        //              participacionIn = true;
                        //          } else if (isComunidad) {
                        //              graficosController.cambiaParticipacionComunidad();
                        //          } else if (isMunicipio) {
                        //              graficosController.cambiaParticipacionMunicipio();
                        //          }
                        //      }
                        //ARCO
                        case 2 -> {
                            if (!arcoIn) {
                                if (sacarCartonAnteriorMuni()) {
                                    graficosController.entraArcoMuniSondeoDelay();
                                } else {
                                    graficosController.entraArcoMuniSondeo();
                                }

                                arcoIn = true;
                            }
                        }
                        case 3 -> {
                            if (!participacionEspIn) {
                                if (sacarCartonAnteriorMuni()) {
                                    graficosController.entraParticipacionEspMuniDelay();
                                } else {
                                    graficosController.entraParticipacionEspMuni();
                                }
                                participacionEspIn = true;
                            }

                        }
                        default -> System.out.print("");
                    }
                    switch (TablaFaldones.getSelectedRow()) {
                        //INFERIOR

                        case 0 -> {
                            if (!inferiorMuniIn && !inferiorAutoIn && !inferiorAutoSondeoIn && !inferiorMuniSondeoIn) {
                                graficosController.entraFaldonMuniSondeo();
                                inferiorMuniSondeoIn = true;
                            } else if (inferiorAutoSondeoIn) {
                                graficosController.deAutoSondeoAMuniSondeo();
                                inferiorMuniSondeoIn = true;
                                inferiorAutoSondeoIn = false;
                            } else if (inferiorMuniSondeoIn) {
                                graficosController.encadenaFaldonMunicipalesSondeo();
                            } else if (inferiorMuniIn || inferiorAutoIn) {
                                inferiorMuniSondeoIn = true;
                                graficosController.entraFaldonMuniSondeo();
                                inferiorMuniIn = false;
                                inferiorAutoIn = false;
                            }
                        }
                        //LATERAL
                        case 1 -> {
                            String codCCAA = null;
                            if (tablaComunidades.getSelectedRow() != -1) {
                                codCCAA = nombreCodigoMunicipal.get(tablaComunidades.getValueAt(tablaComunidades.getSelectedRow(), 0)).substring(0, 2);
                                graficosController.actualizaLateralMunicipales(codCCAA);
                            }
                            if (!lateralIn) {
                                graficosController.entraLateralMunicipales();
                                lateralIn = true;
                            } else {
                                if (codCCAA != null) {
                                    graficosController.despliegaLateralMunicipales(codCCAA);
                                }
                            }
                        }
                        //SEDES
                        case 2 -> graficosController.faldonSedesEntra();
                        //VOTOS MILLONES
                        case 3 -> {
                            if (votantesIn) {
                                graficosController.faldonVotantesHistEntra();
                            } else {
                                graficosController.faldonVotantesEntra();
                                votantesIn = true;
                            }
                        }
                        default -> System.out.print("");
                    }
                }
                //SONDEO AUTONOMICAS
                case 4 -> {
                    switch (TablaCartones.getSelectedRow()) {
                        //RESULTADOS
                        case 0 -> {
                            if (!resultadosIn) {
                                if (sacarCartonAnteriorAuto()) {
                                    graficosController.entraSondeoResultadosAutoDelay();
                                } else {
                                    graficosController.entraSondeoResultadosAuto();
                                }
                                resultadosIn = true;
                            } else if (isComunidad) {
                                graficosController.cambiaSondeoResultadosComunidad();
                            } else if (isMunicipio) {
                                graficosController.cambiaSondeoResultadosMunicipio();
                            }
                        }
                        //PARTICIPACION
                        case 1 -> {
                        }
                        //      if (!participacionIn) {
                        //          sacarCartonAnteriorAuto();
                        //          graficosController.entraParticipacionAuto();
                        //          participacionIn = true;
                        //      } else if (isComunidad) {
                        //          graficosController.cambiaParticipacionComunidad();
                        //      } else if (isMunicipio) {
                        //          graficosController.cambiaParticipacionMunicipio();
                        //      }
                        //  }
                        //ARCO
                        case 2 -> {
                            if (!arcoIn) {
                                sacarCartonAnteriorAuto();
                                graficosController.entraArcoAutoSondeo();
                                arcoIn = true;
                            }
                        }
                        case 3 -> {
                            if (!participacionEspIn) {
                                if (sacarCartonAnteriorMuni()) {
                                    graficosController.entraParticipacionEspAutoDelay();
                                } else {
                                    graficosController.entraParticipacionEspAuto();
                                }
                                participacionEspIn = true;
                            }
                        }
                        default -> System.out.print("");
                    }
                    switch (TablaFaldones.getSelectedRow()) {
                        //INFERIOR
                        case 0 -> {
                            if (!inferiorMuniIn && !inferiorAutoIn && !inferiorAutoSondeoIn && !inferiorMuniSondeoIn) {
                                graficosController.entraFaldonAutoSondeo();
                                inferiorAutoSondeoIn = true;
                            } else if (inferiorMuniSondeoIn) {
                                graficosController.deMuniSondeoAAutoSondeo();
                                inferiorMuniSondeoIn = false;
                                inferiorAutoSondeoIn = true;
                            } else if (inferiorAutoSondeoIn) {
                                graficosController.encadenaFaldonAutonomicasSondeo();
                            } else if (inferiorMuniIn) {
                                inferiorAutoSondeoIn = true;
                                graficosController.deMuniASondeoAuto();
                                inferiorMuniIn = false;
                            } else if (inferiorAutoIn) {
                                inferiorAutoSondeoIn = true;
                                graficosController.entraFaldonAutoSondeo();
                                inferiorAutoIn = false;
                            }
                        }
                        //LATERAL
                        case 1 -> {
                            String codCCAA = null;
                            if (tablaComunidades.getSelectedRow() != -1) {
                                codCCAA = nombreCodigoMunicipal.get(tablaComunidades.getValueAt(tablaComunidades.getSelectedRow(), 0)).substring(0, 2);
                                graficosController.actualizaLateralAutonomicas(codCCAA);
                            }
                            if (!lateralIn) {
                                graficosController.entraLateralAutonomicas();
                                lateralIn = true;
                            } else {
                                if (codCCAA != null) {
                                    graficosController.despliegaLateralAutonomicas(codCCAA);
                                }
                            }
                        }
                        //SEDES
                        case 2 -> graficosController.faldonSedesEntra();
                        //VOTOS MILLONES
                        case 3 -> {
                            if (votantesIn) {
                                graficosController.faldonVotantesHistEntra();
                            } else {
                                graficosController.faldonVotantesEntra();
                                votantesIn = true;
                            }
                        }
                        default -> System.out.print("");
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "La selección no es válida para hacer ENTRA", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }*/

    /*private void btnPactosActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnPactosActionPerformed
        if (dejoEntrarPactos()) {
            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            int screenWidth = gd.getDisplayMode().getWidth();
            int screenHeight = gd.getDisplayMode().getHeight();

            JFrame pactos = null;
            int arcoOFaldon = 0;
            if (TablaCartones.getSelectedRow() == 2) {
                arcoOFaldon = 1;
            }
            if (TablaFaldones.getSelectedRow() == 0) {
                arcoOFaldon = 2;
            }
            switch (tipoElecciones) {
                case 1, 3 -> {
                    String codigo;
                    if (tablaMunicipios.getSelectedRow() != -1) {
                        codigo = nombreCodigoMunicipal.get(tablaMunicipios.getValueAt(tablaMunicipios.getSelectedRow(), 0).toString());
                    } else if (tablaComunidades.getSelectedRow() != -1) {
                        codigo = nombreCodigo.get(tablaComunidades.getValueAt(tablaComunidades.getSelectedRow(), 0).toString());
                    } else {
                        codigo = null;
                    }
                    pactos = new PactosOpcion2(arcoOFaldon, codigo, tipoElecciones, oficiales, avance);
                }

                case 2, 4 -> {
                    String codigo;
                    if (tablaMunicipios.getSelectedRow() != -1) {
                        codigo = nombreCodigoAutonomicas.get(tablaMunicipios.getValueAt(tablaMunicipios.getSelectedRow(), 0).toString());
                    } else if (tablaComunidades.getSelectedRow() != -1) {
                        codigo = nombreCodigoAutonomicas.get(tablaComunidades.getValueAt(tablaComunidades.getSelectedRow(), 0).toString());
                    } else {
                        codigo = null;
                    }
                    pactos = new PactosOpcion2(arcoOFaldon, codigo, tipoElecciones, oficiales, avance);
                }
            }
            pactos.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            pactos.setLocation(screenWidth / 4, screenHeight / 2);
            pactos.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Pactos no válido con la selección actual", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }*/

   /* private void btnReplegarActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnReplegarActionPerformed
        String codCCAA = null;
        if (tablaComunidades.getSelectedRow() != -1) {
            codCCAA = nombreCodigoMunicipal.get(tablaComunidades.getValueAt(tablaComunidades.getSelectedRow(), 0)).substring(0, 2);
        }
        if (tipoElecciones == 2 || tipoElecciones == 4) {
            graficosController.repliegaLateralAutonomicas(codCCAA);
        }
        if (tipoElecciones == 1 || tipoElecciones == 3) {
            graficosController.repliegaLateralMunicipales(codCCAA);
        }
    }*/

    /*private void btnSaleActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnSaleActionPerformed
        switch (tipoElecciones) {
            //OFICIALES MUNICIPALES
            case 1 -> {
                switch (TablaCartones.getSelectedRow()) {
                    //RESULTADOS
                    case 0 -> {
                        graficosController.saleResultadosMuni();
                        resultadosIn = false;
                    }
                    //PARTICIPACION
                    case 1 -> {
                        graficosController.saleParticipacionMuni();
                        participacionIn = false;
                    }
                    //ARCO
                    case 2 -> {
                        graficosController.saleArcoMuni();
                        arcoIn = false;
                    }
                    case 3 -> {
                        graficosController.saleParticipacionEspMuni();
                        participacionEspIn = false;

                    }

                    default -> System.out.print("");
                }
                switch (TablaFaldones.getSelectedRow()) {
                    //INFERIOR
                    case 0 -> {
                        graficosController.saleFaldonMuni();
                        inferiorMuniIn = false;
                    }
                    //LATERAL
                    case 1 -> {
                        if (lateralIn) {
                            graficosController.saleLateralMunicipales();
                            lateralIn = false;
                        }
                    }
                    //SEDES
                    case 2 -> graficosController.faldonSedesSale();
                    //VOTANTES
                    case 3 -> {
                        graficosController.faldonVotantesSale();
                        votantesIn = false;
                    }
                    default -> System.out.print("");
                }
            }
            //OFICIALES AUTONOMICAS
            case 2 -> {
                switch (TablaCartones.getSelectedRow()) {
                    //RESULTADOS
                    case 0 -> {
                        graficosController.saleResultadosAuto();
                        resultadosIn = false;
                    }
                    //PARTICIPACION
                    case 1 -> {
                        graficosController.saleParticipacionAuto();
                        participacionIn = false;
                    }
                    //ARCO
                    case 2 -> {
                        graficosController.saleArcoAuto();
                        arcoIn = false;
                    }
                    case 3 -> {
                        graficosController.saleParticipacionEspAuto();
                        participacionEspIn = false;

                    }
                    default -> System.out.print("");
                }
                switch (TablaFaldones.getSelectedRow()) {
                    //INFERIOR
                    case 0 -> {
                        graficosController.saleFaldonAuto();
                        inferiorAutoIn = false;
                    }
                    //LATERAL
                    case 1 -> {
                        if (lateralIn) {
                            graficosController.saleLateralAutonomicas();
                            lateralIn = false;
                        }
                    }
                    //SEDES
                    case 2 -> graficosController.faldonSedesSale();
                    //VOTANTES
                    case 3 -> {
                        graficosController.faldonVotantesSale();
                        votantesIn = false;
                    }
                    default -> System.out.print("");
                }
            }
            //SONDEO MUNICIPALES
            case 3 -> {
                switch (TablaCartones.getSelectedRow()) {
                    //RESULTADOS
                    case 0 -> {
                        graficosController.saleSondeoResultadosMuni();
                        resultadosIn = false;
                    }
                    //PARTICIPACION
                    case 1 -> {
                        graficosController.saleParticipacionMuni();
                        participacionIn = false;
                    }
                    //ARCO
                    case 2 -> {
                        graficosController.saleArcoMuniSondeo();
                        arcoIn = false;
                    }
                    case 3 -> {
                        graficosController.saleParticipacionEspMuni();
                        participacionEspIn = false;

                    }
                    default -> System.out.print("");
                }
                switch (TablaFaldones.getSelectedRow()) {
                    //INFERIOR
                    case 0 -> {
                        graficosController.saleFaldonMuniSondeo();
                        inferiorMuniSondeoIn = false;
                    }
                    //LATERAL
                    case 1 -> {
                        if (lateralIn) {
                            graficosController.saleLateralMunicipales();
                            lateralIn = false;
                        }
                    }
                    //SEDES
                    case 2 -> graficosController.faldonSedesSale();
                    //VOTANTES
                    case 3 -> {
                        graficosController.faldonVotantesSale();
                        votantesIn = false;
                    }
                    default -> System.out.print("");
                }
            }
            //SONDEO AUTONOMICAS
            case 4 -> {
                switch (TablaCartones.getSelectedRow()) {
                    //RESULTADOS
                    case 0 -> {
                        graficosController.saleSondeoResultadosAuto();
                        resultadosIn = false;
                    }
                    //PARTICIPACION
                    case 1 -> {
                        graficosController.saleParticipacionAuto();
                        participacionIn = false;
                    }
                    //ARCO
                    case 2 -> {
                        graficosController.saleArcoAutoSondeo();
                        arcoIn = false;
                    }
                    case 3 -> {
                        graficosController.saleParticipacionEspAuto();
                        participacionEspIn = false;
                    }
                    default -> System.out.print("");
                }
                switch (TablaFaldones.getSelectedRow()) {
                    //INFERIOR
                    case 0 -> {
                        graficosController.saleFaldonAutoSondeo();
                        inferiorAutoSondeoIn = false;
                    }
                    //LATERAL
                    case 1 -> {
                        if (lateralIn) {
                            graficosController.saleLateralAutonomicas();
                            lateralIn = false;
                        }
                    }
                    //SEDES
                    case 2 -> graficosController.faldonSedesSale();
                    //VOTANTES
                    case 3 -> {
                        graficosController.faldonVotantesSale();
                        votantesIn = false;
                    }
                    default -> System.out.print("");
                }
            }
        }
    }*/

    private void btnSondeoAutonomicasActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnSondeoAutonomicasActionPerformed
        vaciarTablas();
        tipoElecciones = 4;
        oficiales = false;
        resaltarBoton(btnSondeoAutonomicas);
        selectedDb = "SA";
        rellenarCCAA(tipoElecciones);
    }//GEN-LAST:event_btnSondeoAutonomicasActionPerformed

    private void btnDatosAutonomicasActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnDatosAutonomicasActionPerformed
        vaciarTablas();
        tipoElecciones = 2;
        oficiales = true;
        resaltarBoton(btnDatosAutonomicas);
        selectedDb = "DA";
        rellenarCCAA(tipoElecciones);
    }//GEN-LAST:event_btnDatosAutonomicasActionPerformed

    private void btnSondeoMunicipalesActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnSondeoMunicipalesActionPerformed
        vaciarTablas();
        tipoElecciones = 3;
        oficiales = false;
        resaltarBoton(btnSondeoMunicipales);
        selectedDb = "SM";
        rellenarCCAA(tipoElecciones);
    }//GEN-LAST:event_btnSondeoMunicipalesActionPerformed

    private void btnDatosMunicipalesActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnDatosMunicipalesActionPerformed
        vaciarTablas();
        tipoElecciones = 1;
        oficiales = true;
        resaltarBoton(btnDatosMunicipales);
        selectedDb = "DM";
        rellenarCCAA(tipoElecciones);
    }//GEN-LAST:event_btnDatosMunicipalesActionPerformed

    private void vaciarTablas() {
        ((DefaultTableModel) tablaComunidades.getModel()).setRowCount(0);
        ((DefaultTableModel) tablaMunicipios.getModel()).setRowCount(0);
        ((DefaultTableModel) tablaDatos.getModel()).setRowCount(0);
    }

    private void rellenarCCAA(int tipo) {
        vaciarTablas();
        switch (tipo) {
            case 1, 3 -> {
                DefaultTableModel tableModel = (DefaultTableModel) tablaComunidades.getModel();
                List<String> ccaa = autonomias.stream().map(Circunscripcion::getNombreCircunscripcion).toList();
                ccaa = ccaa.subList(0, ccaa.size() - 1);
                for (String s : ccaa) {
                    tableModel.addRow(new Object[]{s});
                }
                tablaComunidades.setModel(tableModel);
            }
            case 2, 4 -> {
                DefaultTableModel tableModel = (DefaultTableModel) tablaComunidades.getModel();
                List<String> lista = new ArrayList<>(autonomias.stream().map(Circunscripcion::getNombreCircunscripcion).toList().subList(0, autonomias.stream().toList().size() - 1));
                for (String s : lista) {
                    tableModel.addRow(new Object[]{s});
                }
                tablaComunidades.setModel(tableModel);
            }
            case 5 -> {
                DefaultTableModel tableModel = (DefaultTableModel) tablaComunidades.getModel();
                List<String> ccaa = autonomias.stream().map(Circunscripcion::getNombreCircunscripcion).filter(nombreCircunscripcion -> nombreCircunscripcion.equalsIgnoreCase("total nacional")).toList();

                for (String s : ccaa) {
                    tableModel.addRow(new Object[]{s});
                }
                tablaComunidades.setModel(tableModel);
            }
        }
    }

   /* private boolean sacarCartonAnteriorAuto() {
        boolean venimosDeOtro = false;
        if (participacionIn) {
            graficosController.saleParticipacionAuto();
            participacionIn = false;
            venimosDeOtro = true;
        }
        if (resultadosIn) {
            if (oficiales) {
                graficosController.saleResultadosAuto();

            } else {
                graficosController.saleSondeoResultadosAuto();
            }
            resultadosIn = false;
            venimosDeOtro = true;
        }
        if (arcoIn) {
            if (oficiales) {
                graficosController.saleArcoAuto();
            } else {
                graficosController.saleArcoAutoSondeo();
            }
            arcoIn = false;
            venimosDeOtro = true;
        }
        if (participacionEspIn) {
            graficosController.saleParticipacionEspAuto();
            participacionEspIn = false;
            venimosDeOtro = true;
        }
        return venimosDeOtro;
    }*/

   /* private boolean sacarCartonAnteriorMuni() {
        boolean venimosDeOtro = false;
        if (participacionIn) {
            graficosController.saleParticipacionMuni();
            participacionIn = false;
            venimosDeOtro = true;
        }
        if (resultadosIn) {
            if (oficiales) {
                graficosController.saleResultadosMuni();
            } else {
                graficosController.saleSondeoResultadosMuni();
            }
            resultadosIn = false;
            venimosDeOtro = true;
        }
        if (arcoIn) {
            if (oficiales) {
                graficosController.saleArcoMuni();
            } else {
                graficosController.saleArcoMuniSondeo();
            }
            arcoIn = false;
            venimosDeOtro = true;
        }
        if (participacionEspIn) {
            graficosController.saleParticipacionEspMuni();
            participacionEspIn = false;
            venimosDeOtro = true;
        }
        return venimosDeOtro;
    }*/

    private void TablaCartonesHierarchyChanged(HierarchyEvent evt) {//GEN-FIRST:event_TablaCartonesHierarchyChanged
    }//GEN-LAST:event_TablaCartonesHierarchyChanged

    private void btnAvance1ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnAvance1ActionPerformed
        resaltarBotonAvances(btnAvance1);
        avance = "1";
    }//GEN-LAST:event_btnAvance1ActionPerformed

    private void btnAvance2ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnAvance2ActionPerformed
        resaltarBotonAvances(btnAvance2);
        avance = "2";
    }//GEN-LAST:event_btnAvance2ActionPerformed

    private void btnAvance3ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnAvance3ActionPerformed
        resaltarBotonAvances(btnAvance3);
        avance = "3";
    }//GEN-LAST:event_btnAvance3ActionPerformed

    /*private void btnResetActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        graficosController.resetIPF();
        vaciarTablas();
        TablaFaldones.clearSelection();
        TablaCartones.clearSelection();
        inferiorAutoIn = false;
        inferiorAutoSondeoIn = false;
        inferiorMuniIn = false;
        inferiorMuniSondeoIn = false;
        participacionIn = false;
        participacionEspIn = false;
        resultadosIn = false;
        votantesIn = false;
        arcoIn = false;

    }*/

    private void btnConfigActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnConfigActionPerformed


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
                properties.setProperty("direccion1", "10.10.54.140");
                properties.setProperty("direccion3", "0");
                properties.setProperty("puerto", "5123");
                properties.setProperty("ipServerReserva", "127.0.0.1");
                properties.setProperty("direccion2", "0");
                properties.setProperty("direccion4", "0");
                properties.setProperty("ipServer", "127.0.0.1");
                properties.setProperty("BDCartones", "<CARTONES>");
                properties.setProperty("rutaFicheros", "C:\\\\Elecciones2023\\\\DATOS");
                properties.setProperty("nConexiones", "1");
                properties.setProperty("BDFaldones", "<FALDONES>");

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

    private void entreParticipacionEsp() {
        vaciarTablas();
        // rellenarCCAA(5);
        try {
            printDataEsp();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JTable TablaCartones;
    private JTable TablaFaldones;
    private JButton btnActualizar;
    private JButton btnAvance1;
    private JButton btnAvance2;
    private JButton btnAvance3;
    private JButton btnConfig;
    private JButton btnDatosAutonomicas;
    private JButton btnDatosMunicipales;
    private JButton btnEntra;
    private JButton btnPactos;
    private JButton btnReplegar;
    private JButton btnReset;
    private JButton btnSale;
    private JButton btnSondeoAutonomicas;
    private JButton btnSondeoMunicipales;
    private JCheckBox cbRegional;
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
    private JTable tablaMunicipios;
    // End of variables declaration//GEN-END:variables
}
