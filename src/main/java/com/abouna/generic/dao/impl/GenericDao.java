/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abouna.generic.dao.impl;

import com.abouna.generic.dao.DataAccessException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import com.abouna.generic.dao.IDao;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public class GenericDao <T extends Serializable,id> implements IDao<T, id> {
    
    @PersistenceContext
    protected EntityManager em;
    
    private Class<T> type;

    public GenericDao() {
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        type = (Class) pt.getActualTypeArguments()[0];
    }


    @Override
    public T create(T t) throws DataAccessException{
        this.em.persist(t);
       return t; 
    }

    @Override
    public void delete(Object id) throws DataAccessException{
        this.em.remove(id);
    }

    @Override
    public T findById(Object id) throws DataAccessException{
        return this.em.find(type, id);
    }

    @Override
    public T update(T t) throws DataAccessException{
        return this.em.merge(t);
    }

    @Override
    public List<T> findAll() throws DataAccessException{
        return this.em.createQuery( "from " + type.getName() ).getResultList();
    }
   
    public EntityManager getManager(){
        return this.em;
    }
}
