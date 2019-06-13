/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abouna.generic.dao;

import java.util.List;
import java.io.Serializable;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public interface IDao<T extends Serializable,id> {
  T create(T t) throws DataAccessException;

    void delete(Object id) throws DataAccessException;

    T findById(Object id) throws DataAccessException;

    T update(T t) throws DataAccessException;  
    
    List<T> findAll() throws DataAccessException;
}
