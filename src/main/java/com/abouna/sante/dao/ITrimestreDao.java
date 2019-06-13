package com.abouna.sante.dao;

import com.abouna.generic.dao.DataAccessException;
import com.abouna.generic.dao.IDao;
import com.abouna.sante.entities.Trimestre;
import java.util.List;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public interface ITrimestreDao extends IDao<Trimestre, Long>{

    public List<Trimestre> getByAnnee(Integer annee) throws DataAccessException;
    
}
