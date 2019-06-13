package com.abouna.sante.dao.impl;

import com.abouna.generic.dao.DataAccessException;
import com.abouna.generic.dao.impl.GenericDao;
import com.abouna.sante.dao.IBonusEloignementDao;
import com.abouna.sante.entities.BonusEloignement;
import com.abouna.sante.entities.BonusEloignement_;
import com.abouna.sante.entities.Centre;
import com.abouna.sante.entities.Trimestre;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author abouna
 */
public class BonusEloignementDaoImpl extends GenericDao<BonusEloignement, Long> implements IBonusEloignementDao{

    @Override
    public BonusEloignement getByCentreTrimestre(Centre centre, Trimestre trimestre,Integer annee) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<BonusEloignement> cq = cb.createQuery(BonusEloignement.class);
        Root<BonusEloignement> buRoot = cq.from(BonusEloignement.class);
        cq.select(buRoot).
        where(cb.and(cb.equal(buRoot.get(BonusEloignement_.centre), centre),cb.equal(buRoot.get(BonusEloignement_.trimestre),
                trimestre) ),cb.equal(buRoot.get(BonusEloignement_.annee), annee));
        TypedQuery<BonusEloignement> tq = getManager().createQuery(cq);
        return tq.getSingleResult();
    }
    
}
