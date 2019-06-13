/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abouna.sante.dao.impl;

import com.abouna.generic.dao.DataAccessException;
import com.abouna.generic.dao.impl.GenericDao;
import com.abouna.sante.dao.IFormationSanitaireDao;
import com.abouna.sante.entities.AireDeSante;
import com.abouna.sante.entities.FormationSanitaire;
import com.abouna.sante.entities.FormationSanitaire_;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public class FormationSanitaireDaoImpl extends GenericDao<FormationSanitaire, Long> implements IFormationSanitaireDao{

    @Override
    public List<FormationSanitaire> findFormationSanitaireFromAireDeSante(AireDeSante aireDeSante) throws DataAccessException {
        CriteriaBuilder builder =  getManager().getCriteriaBuilder();
        CriteriaQuery<FormationSanitaire> cq = builder.createQuery(FormationSanitaire.class);
        Root<FormationSanitaire> formationSanitaireRoot = cq.from(FormationSanitaire.class);
        cq.where(builder.equal(formationSanitaireRoot.get(FormationSanitaire_.aireDeSante), aireDeSante));
        cq.select(formationSanitaireRoot);
        return getManager().createQuery(cq).getResultList();
    }
    
}
