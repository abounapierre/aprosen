package com.abouna.sante.dao;

import com.abouna.generic.dao.DataAccessException;
import com.abouna.generic.dao.IDao;
import com.abouna.sante.entities.Centre;
import com.abouna.sante.entities.District;
import java.util.List;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public interface ICentreDao extends IDao<Centre, Long>{

    public List<Centre> findByDistrict(District d) throws DataAccessException;

    public Integer getPopulationTotal() throws DataAccessException;
    
}
