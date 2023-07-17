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
        return eventRunBuild("TICKER_SENADO/ENTRA");
    }
    public String senadoActualizaEscrutado() {
        return eventRunBuild("TICKER_SENADO/CambiaEscrutado");
    }
    public String senadoActualizaDatos() {return eventRunBuild("TICKER_SENADO/CambiaResultado");}
    public String senadoActualizaPosiciones() {return eventRunBuild("TICKER_SENADO/CambiaOrden");}
    public String senadoSale() {return eventRunBuild("TICKER_SENADO/SALE");}

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

    public String congresoVotosEntra() {return eventRunBuild("TICKER/VOTOS/ENTRA");}
    public String congresoVotosSale() {return eventRunBuild("TICKER/VOTOS/SALE");}
    public String congresoHistoricosEntra() {return eventRunBuild("TICKER/HISTORICOS/ENTRA");}
    public String congresoHistoricosSale() {return eventRunBuild("TICKER/HISTORICOS/SALE");}

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

    //DESPLIEGO_4_SONDEO
    public String cuatroPrimerosSondeo() {return eventRunBuild("TICKER_SONDEO/DESPLIEGO_4");}
    public String despliegoSondeo(String codPartido) {return eventRunBuild("TICKER_SONDEO/VIDEO_"+ codPartido +"/ENTRA");}
    public String repliegoSondeo(String codPartido) {return eventRunBuild("TICKER_SONDEO/VIDEO_"+ codPartido +"/SALE");}
    public String recuperoTodosSondeo() {return eventRunBuild("TICKER_SONDEO/RECUPERO_TODOS");}

    //SONDEO
    public String congresoSondeoEntra() {return eventRunBuild("TICKER_SONDEO/ENTRA");}
    public String congresoSondeoSale() {return eventRunBuild("TICKER_SONDEO/SALE");}

    public String congresoSondeoVotosEntra() {return eventRunBuild("TICKER_SONDEO/VOTOS/ENTRA");}
    public String congresoSondeoVotosSale() {return eventRunBuild("TICKER_SONDEO/VOTOS/SALE");}
    public String congresoSondeoHistoricosEntra() {return eventRunBuild("TICKER_SONDEO/HISTORICOS/ENTRA");}
    public String congresoSondeoHistoricosSale() {return eventRunBuild("TICKER_SONDEO/HISTORICOS/SALE");}

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
