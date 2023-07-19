
package mgg.code.util;

import mgg.code.controller.BrainStormDTOController;
import mgg.code.controller.CPController;
import mgg.code.controller.CircunscripcionController;
import mgg.code.controller.PrimeDTOController;
import mgg.code.controller.hibernate.HibernateControllerCongreso;
import mgg.code.controller.hibernate.HibernateControllerSenado;
import mgg.code.model.CP;
import mgg.code.model.Circunscripcion;
import mgg.code.model.Partido;
import mgg.code.model.dto.BrainStormDTO;
import mgg.code.model.dto.CpDTO;
import mgg.code.util.comparators.CPOficial;
import mgg.code.util.comparators.CPSondeo;
import mgg.code.util.ipf.ConexionIPF;
import mgg.code.util.ipf.IPFSender;
import mgg.code.vista.ConfigView;
import mgg.code.vista.Home;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

//TODO(YaNoEsta lista)
// Seimpre tras una de las señales se manda una señal que se llama actualiza (CongresoActualiza))

public class Listeners {

    private static Listeners instance = null;
    private CircunscripcionController circunscripcionController;
    private CPController cpController;
    private List<Circunscripcion> circunscripcionList = new ArrayList<>();
    private List<Circunscripcion> circunscripcionSenado = new ArrayList<>();

    private static AtomicBoolean isSuscribed = new AtomicBoolean(false);

    private static AtomicBoolean isSuscribedSenado = new AtomicBoolean(false);

    private IPFSender ipf;
    private BrainStormDTOController bscon;
    private PrimeDTOController primecon;

    public static Listeners getInstance() {
        if (instance == null) {
            instance = new Listeners();
        }
        return instance;
    }

    private Listeners() {
        this.circunscripcionController = CircunscripcionController.getInstance();
        this.cpController = CPController.getInstance();
        ConexionIPF.getConexion();
        ipf = IPFSender.getInstance();
        bscon = BrainStormDTOController.getInstance();
        primecon = PrimeDTOController.getInstance();
    }

    public List<CP> partidosChanged(List<CP> newPartidos) {
        if (Home.bs == null) {
            System.out.println("BS ES NULO");
        }

        List<CP> partidosRes = new ArrayList<>();
        var res = newPartidos.stream().filter(x -> x.getId().getCircunscripcion().equals("9900000")).toList();
        if (res.isEmpty()) {
            System.out.println("NO ESPAÑA");
            return null;
        }

        var filteredRes = res.stream().filter(x -> x.getEscanos_hasta() > 0).toList();
        for (CP cp : filteredRes) {
            for (CpDTO dto : oldData.getCpDTO()) {
                if (dto.getCodigoPartido().equals(cp.getId().getPartido())) {
                    if ((dto.getEscanos_desde() != cp.getEscanos_desde()) ||
                            (dto.getEscanos_hasta() != cp.getEscanos_hasta()) ||
                            (dto.getEscanos_hasta_sondeo() != cp.getEscanos_hasta_sondeo()) ||
                            (dto.getEscanos_desde_sondeo() != cp.getEscanos_desde_sondeo())) {
                        partidosRes.add(cp);
                    }
                }
            }
        }
        return partidosRes;
    }


    public boolean partidosDistintos(List<CP> newPartidos) {
        if (Home.bs == null) {
            System.out.println("BS ES NULO");
        }
        var codNew = newPartidos.stream().filter(x -> x.getId().getCircunscripcion().equals("9900000")).map(x -> x.getId().getPartido()).toList();
        var codOld = oldData.getCpDTO().stream().map(CpDTO::getCodigoPartido).toList();

        var distint = new HashSet<>(codNew).containsAll(codOld);
        //System.out.println(distint);
        return !distint;

    }


    private boolean orderChanged(List<CP> newPartidos) {
        if (Home.bs == null) {
            System.out.println("BS ES NULO");
        }


        var filtered = new ArrayList<>(newPartidos.stream().filter(x -> x.getId().getCircunscripcion().equals("9900000")).toList());

        if (Home.tipoElecciones == 1) {
            filtered.sort(new CPOficial().reversed());
        } else if (Home.tipoElecciones == 2) {
            filtered.sort(new CPSondeo().reversed());
        }

        if (filtered.size() == oldData.getCpDTO().size()) {
            for (int i = 0; i < filtered.size(); i++) {
                if (!filtered.get(i).getId().getPartido().equals(oldData.getCpDTO().get(i).getCodigoPartido()))
                    return true;
            }
        }
        return false;
    }

    private boolean numeroPartidosChange(List<CP> newPartidos) {
        if (Home.bs == null) {
            System.out.println("BS ES NULO");
        }
        var filtered = newPartidos.stream().filter(x -> x.getId().getCircunscripcion().equals("9900000")).toList();
        //boolean res = filtered.size() == Home.bs.getNumPartidos();
        return filtered.size() != oldData.getNumPartidos();
    }


    private boolean escanosSondeoHastaChanged(List<CP> changedCP) {
        if (Home.bs == null) {
            System.out.println("BS ES NULO");
        }
        var cpDto = oldData.getCpDTO();
        for (CP cp : changedCP
        ) {
            var dtoAux = cpDto.stream().filter(x -> Objects.equals(x.getCodigoPartido(), cp.getId().getPartido())).toList().get(0);

            if (dtoAux != null) {
                if (dtoAux.getEscanos_hasta_sondeo() != cp.getEscanos_hasta_sondeo()) return true;
            }
        }
        return false;
    }


    private boolean escanosSondeoDesdeChanged(List<CP> changedCP) {
        if (Home.bs == null) {
            System.out.println("BS ES NULO");
        }
        var cpDto = oldData.getCpDTO();
        for (CP cp : changedCP
        ) {
            var filter = cpDto.stream().filter(x -> Objects.equals(x.getCodigoPartido(), cp.getId().getPartido())).toList();
            if (!filter.isEmpty()) {
                var dtoAux = filter.get(0);

                if (dtoAux != null) {
                    if (dtoAux.getEscanos_desde_sondeo() != cp.getEscanos_desde_sondeo()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean escanosOficialChanged(List<CP> changedCP) {
        if (Home.bs == null) {
            System.out.println("BS ES NULO");
        }
        var cpDto = oldData.getCpDTO();
        var filtered = changedCP.stream().filter(x -> x.getId().getCircunscripcion().equals("9900000")).toList();
        for (CP cp : filtered) {
            var filter = cpDto.stream().filter(x -> Objects.equals(x.getCodigoPartido(), cp.getId().getPartido())).toList();
            if (!filter.isEmpty()) {
                var dtoAux = filter.get(0);
                if (dtoAux != null) {
                    if (dtoAux.getEscanos_hasta() != cp.getEscanos_hasta()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private BrainStormDTO oldData;

    public void listenCongreso() {
        if (!isSuscribed.get()) {
            System.out.println("Escuchando congreso");
            isSuscribed.set(true);
            ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
            exec.scheduleAtFixedRate(() -> {
                try {
                    System.out.println("BUscando cambios en congreso...");
                    if (Home.bs != null)
                        oldData = Home.bs;

                    if (!ConfigView.cambioBD) {
                        if (circunscripcionList.isEmpty()) {
                            circunscripcionList = circunscripcionController.getAllCircunscripciones();
                        } else {
                            HibernateControllerCongreso.getInstance().getManager().clear();

                            List<Circunscripcion> circunscripcionesNew = circunscripcionController.getAllCircunscripciones();
                            ;
                            if (Home.tipoElecciones != 3 && !circunscripcionesNew.equals(circunscripcionList)) {
                                if (oldData != null) {
                                    var changes = getChanges(circunscripcionSenado, circunscripcionesNew);
                                    var cps = cpController.getAllCPs();
                                    System.out.println("Cambio detectado en congreso");
                                    getChanges(circunscripcionList, circunscripcionesNew);
                                    circunscripcionList = circunscripcionesNew;
                                    var changesCod = changes.stream().map(Circunscripcion::getCodigo).toList();
                                    var cpChanged = cps.stream().filter(
                                            cp -> changesCod.contains(cp.getId().getCircunscripcion())).toList();
                                    if (Home.tipoElecciones == 1) {
                                        //Si cambiamos esto por los códigos de la lista, valdría para cualquier territorio
                                        BrainStormDTO dto = bscon.getBrainStormDTOOficial("9900000", Home.avance);
                                        Home.getInstance().showDataTable(dto);
                                        Home.bs = dto;
                                        bscon.getBrainStormDTOOficialCongresoInCsv(dto);
                                        //primecon.findAllInExcel(primecon.findAll());
                                        ipf.congresoActualizaEscrutado();
                                        if (numeroPartidosChange(cpChanged) || partidosDistintos(cpChanged)) {
                                            ipf.congresoActualizaNumPartidos();
                                            ipf.congresoYaNoEstaDatosIndividualizado(CPNoEsta(cpChanged));
                                        } else if (orderChanged(cpChanged)) {
                                            ipf.congresoActualizaPosiciones();
                                        } else if (escanosOficialChanged(cpChanged)) {
                                            var partidosChanged = partidosChanged(cpChanged);
                                            ipf.congresoActualizaDatosIndividualizado(partidosChanged);
                                            //ipf.congresoYaNoEstaDatosIndividualizado()
                                            ipf.congresoActualizaDatos();

                                        }
                                        ipf.congresoActualiza();
                                    }

                                    //oldData = Home.bs;
                                } else {
                                    System.out.println("BS ES NULO");
                                }
                            }
                        }
                    } else {
                        System.out.println("Esperando cambio de base de datos");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 0, 7, TimeUnit.SECONDS);
        }
    }

    private List<String> CPNoEsta(List<CP> newPartidos) {
        var filtered = newPartidos.stream().filter(x -> x.getId().getCircunscripcion().equals("9900000")).toList();
        List<String> result = new ArrayList<>();
        List<String> oldCode = oldData.getCpDTO().stream().map(CpDTO::getCodigoPartido).toList();
        List<String> newCode = filtered.stream().map(x -> x.getId().getPartido()).toList();
        for (String code : oldCode) {
            if (!newCode.contains(code)) {
                result.add(code);
            }
        }
        return result;
    }

    private List<Circunscripcion> getChanges(List<Circunscripcion> oldList, List<Circunscripcion> newList) {

        return newList.stream()
                .filter(element -> !oldList.contains(element))
                .toList();
    }


}

