package com.abouna.sante.dao.impl;

import com.abouna.generic.dao.DataAccessException;
import com.abouna.generic.dao.impl.GenericDao;
import com.abouna.sante.dao.ITrimestreDao;
import com.abouna.sante.entities.Mois;
import com.abouna.sante.entities.Mois_;
import com.abouna.sante.entities.Realisation;
import com.abouna.sante.entities.Realisation_;
import com.abouna.sante.entities.Trimestre;
import com.abouna.sante.entities.Trimestre_;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public class TrimestreDaoImpl extends GenericDao<Trimestre, Long> implements ITrimestreDao{

    @Override
    public List<Trimestre> getByAnnee(Integer annee) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<Trimestre> cq = cb.createQuery(Trimestre.class);
        Root<Realisation> realisationRoot = cq.from(Realisation.class);
        Path<Mois> moisPath = realisationRoot.get(Realisation_.mois);
        cq.where(cb.equal(realisationRoot.get(Realisation_.annee), annee));
        cq.select(moisPath.get(Mois_.trimestre));
        cq.orderBy(cb.asc(moisPath.get(Mois_.trimestre).get(Trimestre_.id)));
        cq.distinct(true);
        return getManager().createQuery(cq).getResultList();
    }   
}