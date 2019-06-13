package com.abouna.sante.dao;

import com.abouna.generic.dao.DataAccessException;
import com.abouna.generic.dao.IDao;
import com.abouna.sante.entities.ScoreQualite;
import com.abouna.sante.entities.Trimestre;
/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public interface IScoreQualiteDao extends IDao<ScoreQualite, Long>{
    
    public ScoreQualite getByCentreTrimestre(Trimestre trimestre, Integer annee) throws DataAccessException;
    
}
