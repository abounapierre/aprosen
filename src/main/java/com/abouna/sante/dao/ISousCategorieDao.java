package com.abouna.sante.dao;

import com.abouna.generic.dao.DataAccessException;
import com.abouna.generic.dao.IDao;
import com.abouna.sante.entities.CategorieIndicateur;
import com.abouna.sante.entities.SousCategorie;
import java.util.List;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public interface ISousCategorieDao extends IDao<SousCategorie, Long>{

    public List<SousCategorie> findByCategorie(CategorieIndicateur c) throws DataAccessException;
    
}
