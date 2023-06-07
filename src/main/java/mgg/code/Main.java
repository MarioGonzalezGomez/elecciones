package mgg.code;

import mgg.code.config.Config;
import mgg.code.controller.PrimeDTOController;


public class Main {
    public static void main(String[] args) {
        Config.getConfiguracion();
        PrimeDTOController controller = PrimeDTOController.getInstance();
        controller.findAll().forEach(System.out::println);
    }
}