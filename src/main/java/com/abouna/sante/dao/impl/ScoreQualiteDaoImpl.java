package com.abouna.sante.dao.impl;

import com.abouna.generic.dao.DataAccessException;
import com.abouna.generic.dao.impl.GenericDao;
import com.abouna.sante.dao.IScoreQualiteDao;
import com.abouna.sante.entities.ScoreQualite;
import com.abouna.sante.entities.ScoreQualite_;
import com.abouna.sante.entities.Trimestre;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author abouna
 */
public class ScoreQualiteDaoImpl extends GenericDao<ScoreQualite, Long> implements IScoreQualiteDao{

    @Override
    public ScoreQualite getByCentreTrimestre(Trimestre trimestre, Integer annee) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<ScoreQualite> cq = cb.createQuery(ScoreQualite.class);
        Root<ScoreQualite> buRoot = cq.from(ScoreQualite.class);
        cq.select(buRoot).
        where(cb.and(cb.equal(buRoot.get(ScoreQualite_.trimestre),
                trimestre) ),cb.equal(buRoot.get(ScoreQualite_.annee), annee));
        TypedQuery<ScoreQualite> tq = getManager().createQuery(cq);
        return tq.getSingleResult();
    }
    
    
}
