package mgg.code.service;

import mgg.code.model.CP;
import mgg.code.model.Key;
import mgg.code.repository.CPRepository;

import java.util.List;

public class CPService extends CPBaseService<CP, Key, CPRepository> {


    public CPService(CPRepository repository) {
        super(repository);
    }

    public List<CP> getAllCPs() {
        return this.findAll();
    }

    public CP getCPById(Key key) {
        return this.getById(key);
    }

    public CP postCP(CP cp) {
        return this.save(cp);
    }

    public CP updateCP(CP cp) {
        return this.update(cp);
    }

    public CP deleteCP(CP cp) {
        return this.delete(cp.getId());
    }
}
