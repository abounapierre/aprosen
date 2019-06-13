package com.abouna.sante.dao.impl;

import com.abouna.generic.dao.DataAccessException;
import com.abouna.generic.dao.impl.GenericDao;
import com.abouna.sante.dao.ISubventionDao;
import com.abouna.sante.entities.Indicateur;
import com.abouna.sante.entities.Subvention;
import com.abouna.sante.entities.Trimestre;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public class SubventionDaoImpl extends GenericDao<Subvention, Long> implements ISubventionDao{

    @Override
    public Subvention getByIndicateurTrimestreAnnee(Indicateur indicateur, Trimestre trimestre, Integer annee) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<Subvention> cq = cb.createQuery(Subvention.class);
        Root<Subvention> buRoot = cq.from(Subvention.class);
        cq.select(buRoot).
        where(cb.and(cb.equal(buRoot.get("indicateur"), indicateur),cb.equal(buRoot.get("trimestre"),
                trimestre) ),cb.equal(buRoot.get("annee"), annee));
        TypedQuery<Subvention> tq = getManager().createQuery(cq);
        return tq.getSingleResult();
    }
    
}
