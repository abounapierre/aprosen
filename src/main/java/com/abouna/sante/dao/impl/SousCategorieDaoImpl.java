package com.abouna.sante.dao.impl;

import com.abouna.generic.dao.DataAccessException;
import com.abouna.generic.dao.impl.GenericDao;
import com.abouna.sante.dao.ISousCategorieDao;
import com.abouna.sante.entities.CategorieIndicateur;
import com.abouna.sante.entities.SousCategorie;
import com.abouna.sante.entities.SousCategorie_;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public class SousCategorieDaoImpl extends GenericDao<SousCategorie, Long> implements ISousCategorieDao{

    @Override
    public List<SousCategorie> findByCategorie(CategorieIndicateur c) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<SousCategorie> cq = cb.createQuery(SousCategorie.class);
        Root<SousCategorie> root = cq.from(SousCategorie.class);
        cq.where(cb.equal(root.get(SousCategorie_.categorie), c));
        return getManager().createQuery(cq).getResultList();
    }
    
}
