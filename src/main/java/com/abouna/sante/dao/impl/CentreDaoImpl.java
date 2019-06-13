package com.abouna.sante.dao.impl;

import com.abouna.generic.dao.DataAccessException;
import com.abouna.generic.dao.impl.GenericDao;
import com.abouna.sante.dao.ICentreDao;
import com.abouna.sante.entities.Centre;
import com.abouna.sante.entities.Centre_;
import com.abouna.sante.entities.District;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public class CentreDaoImpl extends GenericDao<Centre, Long> implements ICentreDao{

    @Override
    public List<Centre> findByDistrict(District d) throws DataAccessException {
        CriteriaBuilder builder =  getManager().getCriteriaBuilder();
        CriteriaQuery<Centre> cq = builder.createQuery(Centre.class);
        Root<Centre> centreRoot = cq.from(Centre.class);
        cq.where(builder.equal(centreRoot.get(Centre_.district), d));
        cq.select(centreRoot);
        return getManager().createQuery(cq).getResultList();
    }

    @Override
    public Integer getPopulationTotal() throws DataAccessException {
        CriteriaBuilder builder =  getManager().getCriteriaBuilder();
        CriteriaQuery<Integer> cq = builder.createQuery(Integer.class);
        Root<Centre> centreRoot = cq.from(Centre.class);
        cq.multiselect(builder.sum(centreRoot.get(Centre_.population)));
        return getManager().createQuery(cq).getSingleResult();
    }
    
}
