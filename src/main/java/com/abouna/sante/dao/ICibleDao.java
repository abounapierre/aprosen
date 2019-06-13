package com.abouna.sante.dao;

import com.abouna.generic.dao.DataAccessException;
import com.abouna.generic.dao.IDao;
import com.abouna.sante.entities.Cible;
import com.abouna.sante.entities.Indicateur;
import com.abouna.sante.entities.Trimestre;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public interface ICibleDao extends IDao<Cible, Long>{
   public Cible getByIndicateurTrimestreAnnee(Indicateur indicateur,Trimestre trimestre,Integer annee) throws DataAccessException;
}
