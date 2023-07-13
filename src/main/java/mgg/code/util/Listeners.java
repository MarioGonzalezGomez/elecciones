
package mgg.code.util;

import mgg.code.controller.BrainStormDTOController;
import mgg.code.controller.CPController;
import mgg.code.controller.CircunscripcionController;
import mgg.code.controller.hibernate.HibernateControllerCongreso;
import mgg.code.controller.hibernate.HibernateControllerSenado;
import mgg.code.model.CP;
import mgg.code.model.Circunscripcion;
import mgg.code.model.dto.BrainStormDTO;
import mgg.code.util.ipf.IPFSender;
import mgg.code.vista.Home;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

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

    public static Listeners getInstance() {
        if (instance == null) {
            instance = new Listeners();
        }
        return instance;
    }

    private Listeners() {
        this.circunscripcionController = CircunscripcionController.getInstance();
        this.cpController = CPController.getInstance();
        ipf = IPFSender.getInstance();
        bscon = BrainStormDTOController.getInstance();
    }

    private boolean orderChanged(List<CP> newPartidos) {
        if (newPartidos.size() == Home.bs.getCpDTO().size()) {
            for (int i = 0; i < newPartidos.size(); i++) {
                if (!newPartidos.get(i).getId().getPartido().equals(Home.bs.getCpDTO().get(i).getCodigoPartido()))
                    return true;
            }
        }
        return false;
    }

    private boolean escanosSondeoHastaChanged(List<CP> changedCP) {
        var cpDto = Home.bs.getCpDTO();
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
        var cpDto = Home.bs.getCpDTO();
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
        var cpDto = Home.bs.getCpDTO();
        for (CP cp : changedCP) {
            var filter = cpDto.stream().filter(x -> Objects.equals(x.getCodigoPartido(), cp.getId().getPartido())).toList();
            if (!filter.isEmpty()) {
                var dtoAux = filter.get(0);
                if (dtoAux != null) {
                    if (dtoAux.getEscanos_desde() != cp.getEscanos_desde()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void listenSenado() {
        if (!isSuscribedSenado.get()) {
            System.out.println("Escuchando senado");
            isSuscribedSenado.set(true);
            ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
            exec.scheduleAtFixedRate(() -> {
               // resetHibernateSenado();

                if (circunscripcionSenado.isEmpty()) {
                    circunscripcionSenado = circunscripcionController.getAllCircunscripcionesSenado();
                } else {
                    HibernateControllerSenado.getInstance().getManager().clear();
                    List<Circunscripcion> circunscripcionesNew = circunscripcionController.getAllCircunscripcionesSenado();
                    if (Home.tipoElecciones == 3 && !circunscripcionesNew.equals(circunscripcionSenado)) {
                        System.out.println("Cambio detectado en senado");
                        var changes = getChanges(circunscripcionSenado, circunscripcionesNew);
                        var cps = cpController.getAllCPsSenado();
                        circunscripcionSenado = circunscripcionesNew;

                        var changesCod = changes.stream().map(Circunscripcion::getCodigo).toList();
                        var cpChanged = cps.stream().filter(
                                cp -> changesCod.contains(cp.getId().getCircunscripcion())).toList();
                        //Si cambiamos esto por los códigos de la lista, valdría para cualquier territorio
                        BrainStormDTO dto = bscon.getBrainStormDTOSenado("9900000", Home.avance);
                        Home.getInstance().showDataTable(dto);
                        bscon.getBrainStormDTOSenadoInCsv(dto);
                        if (orderChanged(cpChanged)) {
                            ipf.senadoActualizaPosiciones();
                        } else if (escanosOficialChanged(cpChanged)) {
                            ipf.senadoActualizaDatos();
                        } else {
                            ipf.senadoActualizaEscrutado();
                        }

                    }
                }
            }, 0, 5, TimeUnit.SECONDS);
        }
    }

    private void resetHibernate() {
        Timer.getInstance().startTimer("[RESET]");
        HibernateControllerSenado.getInstance().close();
        HibernateControllerCongreso.getInstance().close();
        HibernateControllerCongreso.getInstance().open();
        HibernateControllerSenado.getInstance().open();
        Timer.getInstance().calculateTime("[RESET]");
    }

    private void resetHibernateSenado() {
        Timer.getInstance().startTimer("[RESET SENADO]");
        HibernateControllerSenado.getInstance().close();
        HibernateControllerSenado.getInstance().open();
        Timer.getInstance().calculateTime("[RESET SENADO]");
    }


    public void listenCongreso() {
        if (!isSuscribed.get()) {
            System.out.println("Escuchando congreso");
            isSuscribed.set(true);
            ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
            exec.scheduleAtFixedRate(() -> {
               // resetHibernate();
                if (circunscripcionList.isEmpty()) {
                    circunscripcionList = circunscripcionController.getAllCircunscripciones();
                } else {
                    HibernateControllerCongreso.getInstance().getManager().clear();

                    List<Circunscripcion> circunscripcionesNew = circunscripcionController.getAllCircunscripciones();
                    ;
                    if (Home.tipoElecciones != 3 && !circunscripcionesNew.equals(circunscripcionList)) {
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
                            bscon.getBrainStormDTOOficialCongresoInCsv(dto);
                            if (orderChanged(cpChanged)) {
                                ipf.congresoActualizaPosiciones();
                            } else if (escanosOficialChanged(cpChanged)) {
                                ipf.congresoActualizaDatos();
                            } else {
                                ipf.congresoActualizaEscrutado();
                            }
                        } else if (Home.tipoElecciones == 2) {
                            //Si cambiamos esto por los códigos de la lista, valdría para cualquier territorio
                            BrainStormDTO dto = bscon.getBrainStormDTOSondeo("9900000", Home.avance);
                            Home.getInstance().showDataTable(dto);
                            bscon.getBrainStormDTOSondeoEspecialInCsv(dto);
                            if (orderChanged(cpChanged)) {
                                ipf.congresoSondeoActualizaPosiciones();
                            } else if (escanosSondeoDesdeChanged(cpChanged) || escanosSondeoHastaChanged(cpChanged)) {
                                ipf.congresoSondeoActualizaDatos();
                            } else {
                                ipf.congresoSondeoActualizaEscrutado();
                            }
                        }
                    }
                }
            }, 0, 5, TimeUnit.SECONDS);
        }
    }

    private List<Circunscripcion> getChanges(List<Circunscripcion> oldList, List<Circunscripcion> newList) {

        return newList.stream()
                .filter(element -> !oldList.contains(element))
                .toList();
    }

}

