package com.abouna.sante.dao.impl;

import com.abouna.generic.dao.DataAccessException;
import com.abouna.generic.dao.impl.GenericDao;
import com.abouna.sante.dao.IIndicateurDao;
import com.abouna.sante.entities.Indicateur;
import com.abouna.sante.entities.Indicateur_;
import com.abouna.sante.entities.SousCategorie;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public class IndicateurDaoImpl extends GenericDao<Indicateur, Long> implements IIndicateurDao{

    @Override
    public List<Indicateur> findBySousCategorie(SousCategorie sc) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<Indicateur> cq = cb.createQuery(Indicateur.class);
        Root<Indicateur> root = cq.from(Indicateur.class);
        cq.where(cb.equal(root.get(Indicateur_.sousCategorie), sc));
        return getManager().createQuery(cq).getResultList();
    }
    
}
