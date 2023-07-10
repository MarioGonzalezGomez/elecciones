package mgg.code.util.ipf;

import mgg.code.config.Config;

public class IPFFaldonesMessageBuilder {

    private static IPFFaldonesMessageBuilder instance = null;

    private final String bd;

    private IPFFaldonesMessageBuilder() {
        this.bd = Config.getConfiguracion().getBdFaldones();
    }

    public static IPFFaldonesMessageBuilder getInstance() {
        if (instance == null)
            instance = new IPFFaldonesMessageBuilder();
        return instance;
    }

    //SENADO
    public String senadoEntra() {
        return eventRunBuild("SENADO/ENTRA");
    }
    public String senadoActualizaEscrutado() {
        return eventRunBuild("SENADO/ACTUALIZO_ESCRUTADO");
    }
    public String senadoActualizaDatos() {return eventRunBuild("SENADO/ACTUALIZO_DATOS");}
    public String senadoActualizaPosiciones() {return eventRunBuild("SENADO/ACTUALIZO_POSICIONES");}
    public String senadoSale() {return eventRunBuild("SENADO/SALE");}

    //CONGRESO
    public String congresoEntra() {return eventRunBuild("CONGRESO/ENTRA");}
    public String congresoActualizaEscrutado() {return eventRunBuild("CONGRESO/ACTUALIZO_ESCRUTADO");}
    public String congresoActualizaDatos() {return eventRunBuild("CONGRESO/ACTUALIZO_DATOS");}
    public String congresoActualizaPosiciones() {return eventRunBuild("CONGRESO/ACTUALIZO_POSICIONES");}
    public String congresoSale() {return eventRunBuild("CONGRESO/SALE");}

    //CONGRESO_SONDEO
    public String congresoSondeoEntra() {return eventRunBuild("CONGRESO_SONDEO/ENTRA");}
    public String congresoSondeoActualizaEscrutado() {return eventRunBuild("CONGRESO_SONDEO/ACTUALIZO_ESCRUTADO");}
    public String congresoSondeoActualizaDatos() {return eventRunBuild("CONGRESO_SONDEO/ACTUALIZO_DATOS");}
    public String congresoSondeoActualizaPosiciones() {return eventRunBuild("CONGRESO_SONDEO/ACTUALIZO_POSICIONES");}
    public String congresoSondeoSale() {return eventRunBuild("CONGRESO_SONDEO/SALE");}

    //GIROS
    public String deCongresoASenado() {
        return eventRunBuild("CONGRESOaSENADO");
    }
    public String deSenadoACongreso() {
        return eventRunBuild("SENADOaCONGRESO");
    }
    public String deSondeoACongreso() {
        return eventRunBuild("SONDEOaCONGRESO");
    }

    //RESET
    public String resetIPF() {
        return eventRunBuild("RESET");
    }

    private String eventRunBuild(String eventName) {
        String message = "";
        String itemSet = "itemset('";
        String eventRun = "EVENT_RUN";
        return message + itemSet +
                bd +
                eventName + "','" + eventRun + "');";
    }

    private String eventBuild(String objecto, String propiedad, String values, int tipoItem) {
        String message = "";
        String itemSet = "";
        if (tipoItem == 1) {
            itemSet = "itemset('";
        }
        if (tipoItem == 2) {
            itemSet = "itemgo('";
        }
        return message + itemSet +
                bd +
                objecto + "','" + propiedad + "'," + values + ");";
    }
}
