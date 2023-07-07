package mgg.code.util.comparators;

import mgg.code.model.CP;

import java.util.Comparator;

public class CPVotantes implements Comparator<CP> {
    @Override
    public int compare(CP o1, CP o2) {
        return Integer.compare(o1.getNumVotantes(), o2.getNumVotantes());
    }
}
