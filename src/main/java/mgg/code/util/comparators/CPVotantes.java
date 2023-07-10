package mgg.code.util.comparators;

import mgg.code.model.dto.CpDTO;

import java.util.Comparator;

public class CPVotantes implements Comparator<CpDTO> {
    @Override
    public int compare(CpDTO o1, CpDTO o2) {
        return Integer.compare(o1.getNumVotantes(), o2.getNumVotantes());
    }
}
