package com.abouna.sante.dao.impl;

import com.abouna.generic.dao.DataAccessException;
import com.abouna.generic.dao.impl.GenericDao;
import com.abouna.sante.dao.ICibleDao;
import com.abouna.sante.entities.Cible;
import com.abouna.sante.entities.Indicateur;
import com.abouna.sante.entities.Trimestre;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public class CibleDaoImpl extends GenericDao<Cible, Long> implements ICibleDao{

    @Override
    public Cible getByIndicateurTrimestreAnnee(Indicateur indicateur, Trimestre trimestre, Integer annee) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<Cible> cq = cb.createQuery(Cible.class);
        Root<Cible> buRoot = cq.from(Cible.class);
        Root<Cible> pet = cq.from(Cible.class);
        cq.select(buRoot).
        where(cb.and(cb.equal(buRoot.get("indicateur"), indicateur),cb.equal(buRoot.get("trimestre"),
                trimestre) ),cb.equal(buRoot.get("annee"), annee));
        TypedQuery<Cible> tq = getManager().createQuery(cq);
        return tq.getSingleResult();
    }
    
}
