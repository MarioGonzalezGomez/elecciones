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

    //TICKER
    public String congresoEntra() {return eventRunBuild("TICKER/ENTRA");}
    public String congresoActualizaEscrutado() {return eventRunBuild("TICKER/ACTUALIZO_ESCRUTADO");}
    public String congresoActualizaDatos() {return eventRunBuild("TICKER/ACTUALIZO_DATOS");}
    public String congresoActualizaPosiciones() {return eventRunBuild("TICKER/ACTUALIZO_POSICIONES");}
    public String congresoSale() {return eventRunBuild("TICKER/SALE");}

    //DESPLIEGO_4
    public String cuatroPrimeros() {return eventRunBuild("TICKER/DESPLIEGO_4");}
    public String despliego(int posicion) {return eventRunBuild("TICKER/VIDEO_"+ posicion +"/ENTRA");}
    public String repliego(int posicion) {return eventRunBuild("TICKER/VIDEO_"+ posicion +"/SALE");}
    public String recuperoTodos() {return eventRunBuild("TICKER/RECUPERO_TODOS");}

    //SONDEO
    public String congresoSondeoEntra() {return eventRunBuild("SONDEO/ENTRA");}
    public String congresoSondeoActualizaEscrutado() {return eventRunBuild("SONDEO/ACTUALIZO_ESCRUTADO");}
    public String congresoSondeoActualizaDatos() {return eventRunBuild("SONDEO/ACTUALIZO_DATOS");}
    public String congresoSondeoActualizaPosiciones() {return eventRunBuild("SONDEO/ACTUALIZO_POSICIONES");}
    public String congresoSondeoSale() {return eventRunBuild("SONDEO/SALE");}

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
