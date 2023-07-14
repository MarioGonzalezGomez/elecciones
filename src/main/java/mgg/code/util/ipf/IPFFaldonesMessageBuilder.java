package mgg.code.util.ipf;

import mgg.code.config.Config;
import mgg.code.model.CP;

import java.util.List;

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
    public String congresoActualiza() {return eventRunBuild("TICKER/ACTUALIZO");}
    public String congresoActualizaEscrutado() {return eventBuild("TICKER/CambiaEscrutado","MAP_INT_PAR","1",1);}
    public String congresoActualizaDatos() {return eventBuild("TICKER/CambiaResultado","MAP_INT_PAR","1",1);}
    public String congresoActualizaDatosIndividualizado(List<CP> partidos){
        StringBuilder signal = new StringBuilder();
        partidos.forEach(par-> signal.append(eventBuild("TICKER/"+par.getId().getPartido()+"/HaCambiado","MAP_INT_PAR","1",1)));
        return signal.toString();
    }
    public String congresoActualizaPosiciones() {return eventBuild("TICKER/CambiaOrden","MAP_INT_PAR","1",1);}
    public String congresoActualizaNumPartidos() {return eventBuild("TICKER/CambiaNPartidos","MAP_INT_PAR","1",1);}
    public String congresoSale() {return eventRunBuild("TICKER/SALE");}

    //DESPLIEGO_4
    public String cuatroPrimeros() {return eventRunBuild("TICKER/DESPLIEGO_4");}
    public String despliego(String codPartido) {return eventRunBuild("TICKER/VIDEO_"+ codPartido +"/ENTRA");}
    public String repliego(String codPartido) {return eventRunBuild("TICKER/VIDEO_"+ codPartido +"/SALE");}
    public String recuperoTodos() {return eventRunBuild("TICKER/RECUPERO_TODOS");}

    public String esDirecto(boolean directo, int tipoElecciones){
        String valor;
        String objeto="TICKER";
        if(directo){
        valor="1";
        } else{
            valor="0";
        }
        if(tipoElecciones==2){
            objeto = objeto+"_SONDEO";
        }
        return eventBuild(objeto+"/P1/VideoVivo","MAP_INT_PAR",valor,1);
    }

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
