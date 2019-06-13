package com.abouna.sante.dao.impl;

import com.abouna.generic.dao.DataAccessException;
import com.abouna.generic.dao.impl.GenericDao;
import com.abouna.sante.dao.IIndicateurCentreDao;
import com.abouna.sante.entities.CategorieIndicateur;
import com.abouna.sante.entities.Centre;
import com.abouna.sante.entities.Centre_;
import com.abouna.sante.entities.IndicateurCentre;
import com.abouna.sante.entities.IndicateurCentre_;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public class IndicateurCentreDao extends GenericDao<IndicateurCentre, Long> implements IIndicateurCentreDao{

    @Override
    public Integer getPopulation(CategorieIndicateur cat, Integer annee) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
        Root<IndicateurCentre> inRoot = cq.from(IndicateurCentre.class);
        Path<Centre> centrePath = inRoot.get(IndicateurCentre_.centre);
        cq.where(cb.and(cb.equal(inRoot.get(IndicateurCentre_.annee), annee),
                cb.equal(inRoot.get(IndicateurCentre_.categorieIndicateur), cat)));
        cq.select(cb.sum(centrePath.get(Centre_.population)));
        return getManager().createQuery(cq).getSingleResult();
    }

    @Override
    public List<IndicateurCentre> findByCategorieAnnee(CategorieIndicateur centre, Integer annee) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<IndicateurCentre> cq = cb.createQuery(IndicateurCentre.class);
        Root<IndicateurCentre> inRoot = cq.from(IndicateurCentre.class);
        cq.where(cb.and(cb.equal(inRoot.get(IndicateurCentre_.annee), annee),
                cb.equal(inRoot.get(IndicateurCentre_.categorieIndicateur), centre)));
        return getManager().createQuery(cq).getResultList();
    }
    
}
