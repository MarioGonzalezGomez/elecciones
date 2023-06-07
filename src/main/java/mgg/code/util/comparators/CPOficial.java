package mgg.code.util.comparators;

import mgg.code.model.CP;

import java.util.Comparator;

public class CPOficial implements Comparator<CP> {
    @Override
    public int compare(CP o1, CP o2) {
        if (o1.getId().getPartido().equals("99999")) return -1;
        var comp = Integer.compare(o1.getEscanos_hasta(), o2.getEscanos_hasta());
        if (comp == 0) {
            comp = Double.compare(o1.getPorcentajeVoto(), o2.getPorcentajeVoto());
            if (comp == 0) {
                comp = Integer.compare(o1.getNumVotantes(), o2.getNumVotantes());
            }
        }
        return comp;
    }
}
