
package mgg.code.util;

//TODO(comprobar cambios de escaños h/d, comprobar escrutado, comprobar orden de los partidos)
//TODO(en senado no hay sondeo, en lo otro si)
//TODO(TipoElecciones:
//       1 = congreso oficiales
//       2 = sondeo
//       3 = Senado)

import mgg.code.controller.CPController;
import mgg.code.controller.CircunscripcionController;
import mgg.code.model.CP;
import mgg.code.model.Circunscripcion;
import mgg.code.model.dto.CpDTO;
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


    public static Listeners getInstance() {
        if (instance == null) {
            instance = new Listeners();
        }
        return instance;
    }

    private Listeners() {
        this.circunscripcionController = CircunscripcionController.getInstance();
        this.cpController = CPController.getInstance();
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
        boolean result = false;
        var cpDto = Home.bs.getCpDTO();
        changedCP.get(1).getId().getPartido();
        for (CP cp : changedCP
        ) {
            var dtoAux = cpDto.stream().filter(x -> Objects.equals(x.getCodigoPartido(), cp.getId().getPartido())).toList().get(0);
            System.out.println(dtoAux);

            if (dtoAux != null) {
                if (dtoAux.getEscanos_hasta_sondeo() != cp.getEscanos_hasta_sondeo()) return true;
            }
        }
        return false;
    }


    private boolean escanosSondeoDesdeChanged(List<CP> changedCP) {
        boolean result = false;
        var cpDto = Home.bs.getCpDTO();
        changedCP.get(1).getId().getPartido();
        for (CP cp : changedCP
        ) {
            var filter = cpDto.stream().filter(x -> Objects.equals(x.getCodigoPartido(), cp.getId().getPartido())).toList();
            if (!filter.isEmpty()) {
                var dtoAux = filter.get(0);
                System.out.println(dtoAux);

                if (dtoAux != null) {
                    if (dtoAux.getEscanos_desde_sondeo() != cp.getEscanos_desde_sondeo()) {
                        System.out.println(dtoAux.getEscanos_desde_sondeo());
                        System.out.println("-----------");
                        System.out.println(cp.getEscanos_desde_sondeo());
                        System.out.println("oooooooooooooooo");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean escanosOficialChanged(List<CP> changedCP) {
        boolean result = false;
        var cpDto = Home.bs.getCpDTO();
        for (CP cp : changedCP) {
            var filter = cpDto.stream().filter(x -> Objects.equals(x.getCodigoPartido(), cp.getId().getPartido())).toList();
            if (!filter.isEmpty()) {
                var dtoAux = filter.get(0);
                System.out.println(dtoAux);
                if (dtoAux != null) {
                    if (dtoAux.getEscanos_desde() != cp.getEscanos_desde()) {
                        System.out.println(dtoAux.getEscanos_desde());
                        System.out.println("-----------");
                        System.out.println(cp.getEscanos_desde());
                        System.out.println("oooooooooooooooo");
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
                if (circunscripcionSenado.isEmpty()) {
                    circunscripcionSenado = circunscripcionController.getAllCircunscripcionesSenado();
                } else {
                    List<Circunscripcion> circunscripcionesNew;
                    circunscripcionesNew = circunscripcionController.getAllCircunscripcionesSenado();
                    if (!circunscripcionesNew.equals(circunscripcionSenado)) {
                        System.out.println("Cambio detectado en senado");
                        var changes = getChanges(circunscripcionSenado, circunscripcionesNew);
                        var cps = cpController.getAllCPsSenado();
                        System.out.println(orderChanged(cps));
                        circunscripcionSenado = circunscripcionesNew;

                        var changesCod = changes.stream().map(Circunscripcion::getCodigo).toList();
                        var cpChanged = cps.stream().filter(
                                cp -> changesCod.contains(cp.getId().getCircunscripcion())).toList();

                        boolean order = false;
                        boolean escanos = false;
                        if (Home.tipoElecciones == 3) {
                            order = orderChanged(cpChanged);
                            escanos = escanosOficialChanged(cpChanged);

                        }
                    }
                }
            }, 0, 5, TimeUnit.SECONDS);
        }
    }

    public void listenCongreso() {
        if (!isSuscribed.get()) {
            System.out.println("Escuchando congreso");
            //TODO( LIST PARTIDOS Y COMPROBAR EL ORDEN )
            isSuscribed.set(true);
            ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
            exec.scheduleAtFixedRate(() -> {
                if (circunscripcionList.isEmpty()) {
                    circunscripcionList = circunscripcionController.getAllCircunscripciones();
                } else {
                    List<Circunscripcion> circunscripcionesNew;
                    circunscripcionesNew = circunscripcionController.getAllCircunscripciones();
                    if (!circunscripcionesNew.equals(circunscripcionList)) {
                        System.out.println("Cambio detectado en senado");
                        var changes = getChanges(circunscripcionSenado, circunscripcionesNew);
                        var cps = cpController.getAllCPs();
                        //System.out.println(orderChanged(cps));
                        System.out.println("Cambio detectado en congreso");
                        getChanges(circunscripcionList, circunscripcionesNew);
                        circunscripcionList = circunscripcionesNew;


                        var changesCod = changes.stream().map(Circunscripcion::getCodigo).toList();
                        var cpChanged = cps.stream().filter(
                                cp -> changesCod.contains(cp.getId().getCircunscripcion())).toList();

                        boolean order = false;
                        boolean escanos = false;
                        boolean escanosOficial = false;
                        if (Home.tipoElecciones == 1) {
                            order = orderChanged(cpChanged);
                            escanosOficial = escanosOficialChanged(cpChanged);
                            if (order) {
                                System.out.println("EL ORDEN HA CAMBIADO");
                                order = false;
                            }
                            if (escanosOficial) {
                                System.out.println("ESCANOS OFICIAL CAMBIADO");
                                escanosOficial = false;
                            }
                        } else if (Home.tipoElecciones == 2) {
                            escanos = escanosSondeoDesdeChanged(cpChanged) || escanosSondeoHastaChanged(cpChanged);
                            if (escanos) {
                                System.out.println("ESCAÑOS HAN CAMBIADO ");
                                escanos = false;
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

