
package mgg.code.util;

//TODO(Cambios en dos bases de dato congreso y senado)

import lombok.val;
import mgg.code.controller.CircunscripcionController;
import mgg.code.model.Circunscripcion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Listeners {

    private static Listeners instance = null;
    private CircunscripcionController circunscripcionController;
    private List<Circunscripcion> circunscripcionList = new ArrayList<>();
    private static AtomicBoolean isSuscribed = new AtomicBoolean(false);


    public static Listeners getInstance() {
        if (instance == null) {
            instance = new Listeners();
        }
        return instance;
    }

    private Listeners() {
        this.circunscripcionController = CircunscripcionController.getInstance();
    }

    public void listenSenado() {
        if (!isSuscribed.get()) {
            isSuscribed.set(true);
            ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
            exec.scheduleAtFixedRate(() -> {
                if (circunscripcionList.isEmpty()) {
                    circunscripcionList = circunscripcionController.getAllCircunscripciones();
                } else {
                    List<Circunscripcion> circunscripcionesNew = null;
                    circunscripcionesNew = circunscripcionController.getAllCircunscripciones();
                    if (!circunscripcionesNew.equals(circunscripcionList)) {
                        getChanges(circunscripcionList, circunscripcionesNew);
                        circunscripcionList = circunscripcionesNew;
                    }
                }
            }, 0, 5, TimeUnit.SECONDS);
        }
    }

    public void listenCongreso() {
        if (!isSuscribed.get()) {
            System.out.println("Escuchando congreso");

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

