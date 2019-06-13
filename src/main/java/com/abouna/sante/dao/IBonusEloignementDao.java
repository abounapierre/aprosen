package com.abouna.sante.dao;

import com.abouna.generic.dao.DataAccessException;
import com.abouna.generic.dao.IDao;
import com.abouna.sante.entities.BonusEloignement;
import com.abouna.sante.entities.Centre;
import com.abouna.sante.entities.Trimestre;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public interface IBonusEloignementDao extends IDao<BonusEloignement, Long>{
    public BonusEloignement getByCentreTrimestre(Centre centre, Trimestre trimestre,Integer annee) throws DataAccessException;
}
