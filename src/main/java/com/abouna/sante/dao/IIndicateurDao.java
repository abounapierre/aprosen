package com.abouna.sante.dao;

import com.abouna.generic.dao.DataAccessException;
import com.abouna.generic.dao.IDao;
import com.abouna.sante.entities.Indicateur;
import com.abouna.sante.entities.SousCategorie;
import java.util.List;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public interface IIndicateurDao extends IDao<Indicateur, Long>{

    public List<Indicateur> findBySousCategorie(SousCategorie sc) throws DataAccessException;
    
}
