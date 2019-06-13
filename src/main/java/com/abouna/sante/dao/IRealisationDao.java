package com.abouna.sante.dao;

import com.abouna.generic.dao.DataAccessException;
import com.abouna.generic.dao.IDao;
import com.abouna.sante.entities.CategorieIndicateur;
import com.abouna.sante.entities.Centre;
import com.abouna.sante.entities.District;
import com.abouna.sante.entities.Indicateur;
import com.abouna.sante.entities.Mois;
import com.abouna.sante.entities.Realisation;
import com.abouna.sante.entities.Trimestre;
import com.abouna.abouna.projection.NombreRealisation;
import com.abouna.abouna.projection.RealisationProjection;
import com.abouna.abouna.projection.RealisationTrimestre;
import com.abouna.abouna.projection.RealisationTrimestrielle;
import com.abouna.sante.transfer.DistrictRealisation;
import com.abouna.sante.transfer.GraphiqueElement;
import java.util.List;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public interface IRealisationDao extends IDao<Realisation, Long>{
    
    public List<NombreRealisation> getByIndicateurTrimestreAnneeCentre(Indicateur indicateur, Trimestre trimestre, Integer annee, Centre centre) throws DataAccessException;
    
    public RealisationProjection getRealisationProjection(Indicateur indicateur, Trimestre trimestre, Integer annee) throws DataAccessException;

    public List<NombreRealisation> getByIndicateurTrimestreAnneeDistrict(Indicateur indicateur, Trimestre trimestre, Integer annee, District district) throws DataAccessException;
    
    public List<DistrictRealisation> getRealisationDistrict(Indicateur indicateur, Trimestre trimestre, Integer annee) throws DataAccessException;

    public List<GraphiqueElement> getGraphiqueElement(Indicateur indicateur, Integer annee, Trimestre trimestre, District district) throws DataAccessException;

    public List<GraphiqueElement> getGraphiqueElement(Indicateur indicateur, Integer annee, Trimestre trimestre) throws DataAccessException;

    public List<Integer> getDistinctYears() throws DataAccessException;

    public RealisationTrimestrielle getRealisationTrimestrielle(Indicateur indicateur, Integer annee, Trimestre trimestre) throws DataAccessException;

    public List<RealisationTrimestre> getByIndicateurAnneeCentre(Indicateur indicateur, Integer annee, Centre centre) throws DataAccessException;

    public Long getValidRealisation(Indicateur indicateur, Trimestre trimestre, Integer annee, Centre centre)throws DataAccessException;

    public Long getValidRealisation(Indicateur indicateur, Trimestre trimestre, Integer annee)throws DataAccessException;
    
    public RealisationProjection getRealisationProjection1(Indicateur indicateur, Trimestre trimestre, Integer annee) throws DataAccessException;
    
    public List<Realisation> getRealisationByTrimestre(Indicateur indicateur,Mois mois,Integer annee) throws DataAccessException;
    
    public List<Realisation> getRealisationByCentreMoisAnnee(Centre centre,Mois mois,Integer annee) throws DataAccessException;

    public double countBonusEloignement(District district, Centre centre, CategorieIndicateur cate, Integer annee, Trimestre trimestre) throws DataAccessException;

    public List<Realisation> getRealisation(Centre c, Indicateur indi, Integer an, Mois m)  throws DataAccessException;
}
