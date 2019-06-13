/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abouna.sante.dao;

import com.abouna.generic.dao.DataAccessException;
import com.abouna.generic.dao.IDao;
import com.abouna.sante.entities.AireDeSante;
import com.abouna.sante.entities.Population;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public interface IPopulationDao extends IDao<Population, Long>{
  public Population getPopulationByAireSanteAnnee(AireDeSante aireDeSante,Integer annee) throws DataAccessException;  
}
