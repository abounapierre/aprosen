package com.abouna.sante.dao;

import com.abouna.generic.dao.DataAccessException;
import com.abouna.generic.dao.IDao;
import com.abouna.sante.entities.CategorieIndicateur;
import com.abouna.sante.entities.IndicateurCentre;
import java.util.List;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public interface IIndicateurCentreDao extends IDao<IndicateurCentre, Long>{
    
    public Integer getPopulation(CategorieIndicateur cat, Integer annee) throws DataAccessException;

    public List<IndicateurCentre> findByCategorieAnnee(CategorieIndicateur centre, Integer annee) throws DataAccessException ;
    
}
