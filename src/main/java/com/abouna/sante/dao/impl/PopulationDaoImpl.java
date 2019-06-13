/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abouna.sante.dao.impl;

import com.abouna.generic.dao.DataAccessException;
import com.abouna.generic.dao.impl.GenericDao;
import com.abouna.sante.dao.IPopulationDao;
import com.abouna.sante.entities.AireDeSante;
import com.abouna.sante.entities.Population;
import com.abouna.sante.entities.Population_;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public class PopulationDaoImpl extends GenericDao<Population, Long> implements IPopulationDao{

    @Override
    public Population getPopulationByAireSanteAnnee(AireDeSante aireDeSante, Integer annee) throws DataAccessException {
        CriteriaBuilder builder =  getManager().getCriteriaBuilder();
        CriteriaQuery<Population> cq = builder.createQuery(Population.class);
        Root<Population> populationRoot = cq.from(Population.class);
        cq.where(builder.and(builder.equal(populationRoot.get(Population_.aireDeSante), aireDeSante)
                ,builder.equal(populationRoot.get(Population_.annee), annee)));
        cq.select(populationRoot);
        return getManager().createQuery(cq).getSingleResult();
    }
    
}
