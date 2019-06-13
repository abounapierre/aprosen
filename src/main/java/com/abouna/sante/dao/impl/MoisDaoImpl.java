package com.abouna.sante.dao.impl;

import com.abouna.generic.dao.DataAccessException;
import com.abouna.generic.dao.impl.GenericDao;
import com.abouna.sante.dao.IMoisDao;
import com.abouna.sante.entities.Indicateur;
import com.abouna.sante.entities.Indicateur_;
import com.abouna.sante.entities.Mois;
import com.abouna.sante.entities.Mois_;
import com.abouna.sante.entities.Trimestre;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public class MoisDaoImpl extends GenericDao<Mois, Long> implements IMoisDao{

    @Override
    public List<Mois> findByTrimestre(Trimestre trimestre) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<Mois> cq = cb.createQuery(Mois.class);
        Root<Mois> root = cq.from(Mois.class);
        cq.where(cb.equal(root.get(Mois_.trimestre), trimestre));
        return getManager().createQuery(cq).getResultList();
    }
    
}
