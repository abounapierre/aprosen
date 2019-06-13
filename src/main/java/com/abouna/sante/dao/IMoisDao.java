package com.abouna.sante.dao;

import com.abouna.generic.dao.DataAccessException;
import com.abouna.generic.dao.IDao;
import com.abouna.sante.entities.Mois;
import com.abouna.sante.entities.Trimestre;
import java.util.List;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public interface IMoisDao extends IDao<Mois, Long>{

    public List<Mois> findByTrimestre(Trimestre trimestre) throws DataAccessException;
    
}
