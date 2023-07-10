package mgg.code.util.comparators;

import mgg.code.model.dto.CpDTO;

import java.util.Comparator;

public class CPOrden implements Comparator<CpDTO> {
    @Override
    public int compare(CpDTO o1, CpDTO o2) {
        return o1.getCodigoPartido().compareTo(o2.getCodigoPartido());
    }
}
