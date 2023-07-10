
package mgg.code.util;

//TODO(Cambios en dos bases de dato congreso y senado)

import mgg.code.controller.CPController;
import mgg.code.controller.CircunscripcionController;
import mgg.code.model.CP;
import mgg.code.model.Circunscripcion;
import mgg.code.model.Partido;
import mgg.code.vista.Home;

import java.util.ArrayList;
import java.util.List;
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
        boolean result = false;
        if (newPartidos.size() == Home.bs.getCpDTO().size()) {

            for (int i = 0; i < newPartidos.size(); i++) {
                if (!newPartidos.get(i).getId().getPartido().equals(Home.bs.getCpDTO().get(i).getCodigoPartido()))
                    return true;
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
                    List<Circunscripcion> circunscripcionesNew = null;
                    circunscripcionesNew = circunscripcionController.getAllCircunscripcionesSenado();
                    if (!circunscripcionesNew.equals(circunscripcionSenado)) {
                        System.out.println("Cambio detectado en senado");
                        //var changes = getChanges(circunscripcionSenado, circunscripcionesNew);
                        var cps = cpController.getAllCPs();
                        System.out.println(orderChanged(cps));
                        circunscripcionSenado = circunscripcionesNew;
                    }
                }
            }, 0, 5, TimeUnit.SECONDS);
        }
    }

    public void listenCongreso() {
        if (!isSuscribed.get()) {
            System.out.println("Escuchando congreso");
            //TODO( LISTA PARTIDOS Y COMPROBAR EL ORDEN )
            isSuscribed.set(true);
            ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
            exec.scheduleAtFixedRate(() -> {
                System.out.println("Actualizando congreso...");
                if (circunscripcionList.isEmpty()) {
                    circunscripcionList = circunscripcionController.getAllCircunscripciones();
                } else {
                    List<Circunscripcion> circunscripcionesNew = null;
                    circunscripcionesNew = circunscripcionController.getAllCircunscripciones();
                    if (!circunscripcionesNew.equals(circunscripcionList)) {
                        System.out.println("Cambio detectado en senado");
                        //var changes = getChanges(circunscripcionSenado, circunscripcionesNew);
                        var cps = cpController.getAllCPs();
                        System.out.println(orderChanged(cps));
                        System.out.println("Cambio detectado en congreso");
                        getChanges(circunscripcionList, circunscripcionesNew);
                        circunscripcionList = circunscripcionesNew;
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

