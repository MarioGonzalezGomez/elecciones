package mgg.code.util.comparators;


import mgg.code.model.CP;

import java.util.Comparator;

public class CPSondeo implements Comparator<CP> {

    @Override
    public int compare(CP o1, CP o2) {
        if (o1.getId().getPartido().equals("99999")) return -1;
        int comp = Integer.compare(o1.getEscanos_hasta_sondeo(), o2.getEscanos_hasta_sondeo());
        if (comp == 0) {
            comp = Integer.compare(o1.getEscanos_desde_sondeo(), o2.getEscanos_desde_sondeo());
            if (comp == 0) {
                comp = Double.compare(o1.getPorcentajeVotoSondeo(), o2.getPorcentajeVotoSondeo());
            }
        }
        return comp;
    }
}
