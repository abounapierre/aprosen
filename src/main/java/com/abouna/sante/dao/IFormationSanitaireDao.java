/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abouna.sante.dao;

import com.abouna.generic.dao.DataAccessException;
import com.abouna.generic.dao.IDao;
import com.abouna.sante.entities.AireDeSante;
import com.abouna.sante.entities.FormationSanitaire;
import java.util.List;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public interface IFormationSanitaireDao extends IDao<FormationSanitaire, Long> {
    public List<FormationSanitaire> findFormationSanitaireFromAireDeSante(AireDeSante aireDeSante) throws DataAccessException;
}
