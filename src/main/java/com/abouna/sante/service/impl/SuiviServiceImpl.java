package com.abouna.sante.service.impl;

import com.abouna.generic.dao.DataAccessException;
import com.abouna.sante.dao.IAireDeSanteDao;
import com.abouna.sante.dao.IBonusEloignementDao;
import com.abouna.sante.dao.ICategorieIndicateurDao;
import com.abouna.sante.dao.ICentreDao;
import com.abouna.sante.dao.ICibleDao;
import com.abouna.sante.dao.IDistrictDao;
import com.abouna.sante.dao.IFormationSanitaireDao;
import com.abouna.sante.dao.IIndicateurCentreDao;
import com.abouna.sante.dao.IIndicateurDao;
import com.abouna.sante.dao.IMoisDao;
import com.abouna.sante.dao.IPopulationDao;
import com.abouna.sante.dao.IRealisationDao;
import com.abouna.sante.dao.IScoreQualiteDao;
import com.abouna.sante.dao.ISousCategorieDao;
import com.abouna.sante.dao.ISubventionDao;
import com.abouna.sante.dao.ITrimestreDao;
import com.abouna.sante.dao.IUtilisateurDao;
import com.abouna.sante.entities.BonusEloignement;
import com.abouna.sante.entities.CategorieIndicateur;
import com.abouna.sante.entities.Centre;
import com.abouna.sante.entities.Cible;
import com.abouna.sante.entities.District;
import com.abouna.sante.entities.Indicateur;
import com.abouna.sante.entities.IndicateurCentre;
import com.abouna.sante.entities.Mois;
import com.abouna.sante.entities.Realisation;
import com.abouna.sante.entities.ScoreQualite;
import com.abouna.sante.entities.SousCategorie;
import com.abouna.sante.entities.Subvention;
import com.abouna.sante.entities.Trimestre;
import com.abouna.sante.entities.Utilisateur;
import com.abouna.abouna.projection.NombreRealisation;
import com.abouna.abouna.projection.RealisationProjection;
import com.abouna.abouna.projection.RealisationTrimestre;
import com.abouna.abouna.projection.RealisationTrimestrielle;
import com.abouna.abouna.projection.SyntheseActivite;
import com.abouna.sante.service.ISuiviService;
import com.abouna.sante.transfer.DistrictRealisation;
import com.abouna.sante.transfer.GraphiqueElement;
import com.abouna.sante.util.Util;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
@Transactional
public class SuiviServiceImpl implements ISuiviService, Serializable {

    @Autowired
    private ICategorieIndicateurDao categorieIndicateurDao;
    @Autowired
    private ISousCategorieDao sousCategorieDao;
    @Autowired
    private ICentreDao centreDao;
    @Autowired
    private ICibleDao cibleDao;
    @Autowired
    private IDistrictDao districtDao;
    @Autowired
    private IIndicateurDao indicateurDao;
    @Autowired
    private IMoisDao moisDao;
    @Autowired
    private IRealisationDao realisationDao;
    @Autowired
    private ISubventionDao subventionDao;
    @Autowired
    private ITrimestreDao trimestreDao;
    @Autowired
    private IIndicateurCentreDao indicateurCentreDao;
    @Autowired
    private IUtilisateurDao utilisateurDao;
    @Autowired
    private IBonusEloignementDao bonusEloignementDao;
    @Autowired
    private IScoreQualiteDao scoreQualiteDao;
    @Autowired
    private IAireDeSanteDao aireDeSanteDao;
    @Autowired
    private IPopulationDao populationDao;
    @Autowired
    private IFormationSanitaireDao formationSanitaireDao;

    public ICategorieIndicateurDao getCategorieIndicateurDao() {
        return categorieIndicateurDao;
    }

    public void setCategorieIndicateurDao(ICategorieIndicateurDao categorieIndicateurDao) {
        this.categorieIndicateurDao = categorieIndicateurDao;
    }

    public ICentreDao getCentreDao() {
        return centreDao;
    }

    public void setCentreDao(ICentreDao centreDao) {
        this.centreDao = centreDao;
    }

    public ICibleDao getCibleDao() {
        return cibleDao;
    }

    public void setCibleDao(ICibleDao cibleDao) {
        this.cibleDao = cibleDao;
    }

    public IDistrictDao getDistrictDao() {
        return districtDao;
    }

    public void setDistrictDao(IDistrictDao districtDao) {
        this.districtDao = districtDao;
    }

    public IIndicateurDao getIndicateurDao() {
        return indicateurDao;
    }

    public void setIndicateurDao(IIndicateurDao indicateurDao) {
        this.indicateurDao = indicateurDao;
    }

    public IMoisDao getMoisDao() {
        return moisDao;
    }

    public void setMoisDao(IMoisDao moisDao) {
        this.moisDao = moisDao;
    }

    public IRealisationDao getRealisationDao() {
        return realisationDao;
    }

    public void setRealisationDao(IRealisationDao realisationDao) {
        this.realisationDao = realisationDao;
    }

    public ISubventionDao getSubventionDao() {
        return subventionDao;
    }

    public void setSubventionDao(ISubventionDao subventionDao) {
        this.subventionDao = subventionDao;
    }

    public ITrimestreDao getTrimestreDao() {
        return trimestreDao;
    }

    public void setTrimestreDao(ITrimestreDao trimestreDao) {
        this.trimestreDao = trimestreDao;
    }

    public ISousCategorieDao getSousCategorieDao() {
        return sousCategorieDao;
    }

    public IUtilisateurDao getUtilisateurDao() {
        return utilisateurDao;
    }

    public void setUtilisateurDao(IUtilisateurDao utilisateurDao) {
        this.utilisateurDao = utilisateurDao;
    }

    public void setSousCategorieDao(ISousCategorieDao sousCategorieDao) {
        this.sousCategorieDao = sousCategorieDao;
    }

    public IIndicateurCentreDao getIndicateurCentreDao() {
        return indicateurCentreDao;
    }

    public void setIndicateurCentreDao(IIndicateurCentreDao indicateurCentreDao) {
        this.indicateurCentreDao = indicateurCentreDao;
    }

    public IBonusEloignementDao getBonusEloignementDao() {
        return bonusEloignementDao;
    }

    public void setBonusEloignementDao(IBonusEloignementDao bonusEloignementDao) {
        this.bonusEloignementDao = bonusEloignementDao;
    }

    public IScoreQualiteDao getScoreQualiteDao() {
        return scoreQualiteDao;
    }

    public void setScoreQualiteDao(IScoreQualiteDao scoreQualiteDao) {
        this.scoreQualiteDao = scoreQualiteDao;
    }

    public IAireDeSanteDao getAireDeSanteDao() {
        return aireDeSanteDao;
    }

    public void setAireDeSanteDao(IAireDeSanteDao aireDeSanteDao) {
        this.aireDeSanteDao = aireDeSanteDao;
    }

    public IFormationSanitaireDao getFormationSanitaireDao() {
        return formationSanitaireDao;
    }

    public void setFormationSanitaireDao(IFormationSanitaireDao formationSanitaireDao) {
        this.formationSanitaireDao = formationSanitaireDao;
    }

    public IPopulationDao getPopulationDao() {
        return populationDao;
    }

    public void setPopulationDao(IPopulationDao populationDao) {
        this.populationDao = populationDao;
    }

    @Override
    public List<District> getAllDistrict() {
        try {
            return districtDao.findAll();
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public District saveOrUpdateDistrict(District district) {
        try {
            if (district.getId() == null) {
                return districtDao.create(district);
            } else {
                return districtDao.update(district);
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public void deleteDistrict(Long id) {
        if (id != null) {
            try {
                District district = districtDao.findById(id);
                districtDao.delete(district);
            } catch (DataAccessException ex) {
                Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void deleteCentre(Long id) {
        try {
            Centre centre = centreDao.findById(id);
            if (centre != null) {
                centreDao.delete(centre);
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Centre saveOrUpdateCentre(Centre centre) {
        try {
            if (centre.getId() == null) {
                return centreDao.create(centre);
            } else {
                return centreDao.update(centre);
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    @Override
    public List<Centre> getAllCentre() {
        try {
            return centreDao.findAll();
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public void deleteCategorieIndicateur(Long id) {
        try {
            CategorieIndicateur cat = categorieIndicateurDao.findById(id);
            if (cat != null) {
                categorieIndicateurDao.delete(cat);
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public CategorieIndicateur saveOrUpdateCategorieIndicateur(CategorieIndicateur categorie) {
        try {
            if (categorie.getId() == null) {
                return categorieIndicateurDao.create(categorie);
            } else {
                return categorieIndicateurDao.update(categorie);
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<CategorieIndicateur> getAllCategorieIndicateur() {
        try {
            return categorieIndicateurDao.findAll();
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public void deleteIndicateur(Long id) {
        try {
            Indicateur indicateur = indicateurDao.findById(id);
            if (indicateur == null) {
                indicateurDao.delete(indicateur);
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Indicateur saveOrUpdateIndicateur(Indicateur indicateur) {
        try {
            if (indicateur.getId() == null) {
                return indicateurDao.create(indicateur);
            } else {
                return indicateurDao.update(indicateur);
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<Indicateur> getAllIndicateur() {
        try {
            return indicateurDao.findAll();
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public void deleteTrimestre(Long id) {
        try {
            Trimestre trimestre = trimestreDao.findById(id);
            if (trimestre != null) {
                trimestreDao.delete(trimestre);
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Trimestre saveOrUpdateTrimestre(Trimestre trimestre) {
        try {
            if (trimestre.getId() == null) {
                return trimestreDao.create(trimestre);
            } else {
                return trimestreDao.update(trimestre);
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<Trimestre> getAllTrimestre() {
        try {
            return trimestreDao.findAll();
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public void deleteMois(Long id) {
        try {
            Mois mois = moisDao.findById(id);
            if (mois != null) {
                moisDao.delete(mois);
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Mois saveOrUpdateMois(Mois mois) {
        try {
            if (mois.getId() == null) {
                return moisDao.create(mois);
            } else {
                return moisDao.update(mois);
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<Mois> getAllMois() {
        try {
            return moisDao.findAll();
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public void deleteCible(Long id) {
        try {
            Cible cible = cibleDao.findById(id);
            if (cible != null) {
                cibleDao.delete(cible);
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Cible saveOrUpdateCible(Cible cible) {
        try {
            if (cible.getId() == null) {
                return cibleDao.create(cible);
            } else {
                return cibleDao.update(cible);
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<Cible> getAllCible() {
        try {
            return cibleDao.findAll();
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public void deleteRealisation(Long id) {
        try {
            Realisation realisation = realisationDao.findById(id);
            if (realisation != null) {
                realisationDao.delete(realisation);
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Realisation saveOrUpdateRealisation(Realisation realisation) {
        try {
            if (realisation.getId() == null) {
                return realisationDao.create(realisation);
            } else {
                return realisationDao.update(realisation);
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<Realisation> getAllRealisation() {
        try {
            return realisationDao.findAll();
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public void deleteSubvention(Long id) {
        try {
            Subvention subvention = subventionDao.findById(id);
            if (subvention != null) {
                subventionDao.delete(subvention);
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Subvention saveOrUpdateSubvention(Subvention subvention) {
        try {
            if (subvention.getId() == null) {
                return subventionDao.create(subvention);
            } else {
                return subventionDao.update(subvention);
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<Subvention> getAllSubvention() {
        try {
            return subventionDao.findAll();
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<Centre> findCentreFromDistrict(District d) {
        try {
            return centreDao.findByDistrict(d);
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public void deleteSousCategorie(Long id) {
        try {
            SousCategorie sc = sousCategorieDao.findById(id);
            if (sc != null) {
                sousCategorieDao.delete(sc);
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public SousCategorie saveOrUpdateSousCategorie(SousCategorie sousCategorie) {
        try {
            if (sousCategorie.getId() == null) {
                return sousCategorieDao.create(sousCategorie);
            } else {
                return sousCategorieDao.update(sousCategorie);
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<SousCategorie> getAllSousCategorie() {
        try {
            return sousCategorieDao.findAll();
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<SousCategorie> findSousFromCategorie(CategorieIndicateur c) {
        try {
            return sousCategorieDao.findByCategorie(c);
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<Indicateur> findIndicateurFromSousCategorie(SousCategorie sc) {
        try {
            return indicateurDao.findBySousCategorie(sc);
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public RealisationProjection getRealisation(Indicateur indicateur, Trimestre trimestre, Integer annee, District district, Centre centre) {
        try {
            RealisationProjection rp = realisationDao.getRealisationProjection(indicateur, trimestre, annee);
            List<NombreRealisation> rs;
            try {
                if (centre != null) {
                    rs = realisationDao.getByIndicateurTrimestreAnneeCentre(indicateur, trimestre, annee, centre);
                    rp.setTotal(realisationDao.getValidRealisation(indicateur, trimestre, annee, centre));
                } else {
                    rs = realisationDao.getByIndicateurTrimestreAnneeDistrict(indicateur, trimestre, annee, district);
                    rp.setTotal(realisationDao.getValidRealisation(indicateur, trimestre, annee));
                }
                for (NombreRealisation realisation : rs) {
                    rp.getRealisations().put(realisation.getMois(), realisation.getValeur());
                }
                if (rp.getTotal() == null) {
                    rp.setTotal(0L);
                }

                return rp;
            } catch (DataAccessException ex) {
                Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        } catch (NoResultException e) {
            return null;
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    @Override
    public List<Mois> getMoisFromTrimestre(Trimestre trimestre) {
        try {
            return moisDao.findByTrimestre(trimestre);
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return Collections.EMPTY_LIST;
        }
    }

    /**
     *
     * @param categorieIndicateur the value of categorieIndicateur
     * @param annee
     * @return
     */
    @Override
    public Integer getPopulation(CategorieIndicateur categorieIndicateur, Integer annee) {
        try {
            return indicateurCentreDao.getPopulation(categorieIndicateur, annee);
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public SyntheseActivite getSynthese(Indicateur indicateur, Trimestre trimestre, Integer annee) {
        SyntheseActivite ac = new SyntheseActivite();
        List<DistrictRealisation> res = new ArrayList<DistrictRealisation>();
        try {

            RealisationProjection rp = realisationDao.getRealisationProjection(indicateur, trimestre, annee);
            ac.setSubvention(rp.getSubvention());
            ac.setEstProvisionne(rp.isEstProvisionne());
            res = realisationDao.getRealisationDistrict(indicateur, trimestre, annee);
            for (DistrictRealisation districtRealisation : res) {
                ac.getValues().put(districtRealisation.getDistrict(), districtRealisation.getValeur());
            }
            return ac;
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (NoResultException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            ac.setIndicateur(indicateur.getCode());
            ac.setEstProvisionne(false);
            ac.setSubvention(0);
            for (DistrictRealisation districtRealisation : res) {
                ac.getValues().put(districtRealisation.getDistrict(), 0L);
            }
            return ac;
        }
    }

    @Override
    public List<GraphiqueElement> getGraphiqueElement(Indicateur indicateur, Integer annee, Trimestre trimestre, District district) {
        try {
            return realisationDao.getGraphiqueElement(indicateur, annee, trimestre, district);
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<GraphiqueElement> getGraphiqueElement(Indicateur indicateur, Integer annee, Trimestre trimestre) {
        try {
            return realisationDao.getGraphiqueElement(indicateur, annee, trimestre);
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<Integer> getDistictYears() {
        try {
            return realisationDao.getDistinctYears();
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<Trimestre> getAllTrimestre(Integer annee) {
        try {
            return trimestreDao.getByAnnee(annee);
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public RealisationTrimestrielle getRealisationTrimestrielle(Indicateur indicateur, Integer annee, Trimestre trimestre) {
        try {
            return realisationDao.getRealisationTrimestrielle(indicateur, annee, trimestre);
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Map<String, Long> getRealisationTrimestrielle(Indicateur indicateur, Centre centre, Integer annee) {
        try {
            Map<String, Long> result = new HashMap<String, Long>();
            List<RealisationTrimestre> realisations = realisationDao.getByIndicateurAnneeCentre(indicateur, annee, centre);
            for (RealisationTrimestre realisationTrimestre : realisations) {
                result.put(realisationTrimestre.getTrimestre(), realisationTrimestre.getRealisation());
            }
            return result;
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public void deleteIndicateurCentre(Long id) {
        try {
            IndicateurCentre indi = indicateurCentreDao.findById(id);
            if (indi != null) {
                indicateurCentreDao.create(indi);
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<IndicateurCentre> getCategorieFromCategorieAnnee(CategorieIndicateur centre, Integer annee) {
        try {
            return indicateurCentreDao.findByCategorieAnnee(centre, annee);
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public IndicateurCentre saveOrUpdate(IndicateurCentre indicateur) {
        try {
            if (indicateur.getId() == null) {
                return indicateurCentreDao.create(indicateur);
            } else {
                return indicateurCentreDao.update(indicateur);
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public long getValidRealisation(Indicateur indicateur, Trimestre trimestre, Integer annee, Centre centre) {
        try {
            Long val = realisationDao.getValidRealisation(indicateur, trimestre, annee, centre);
            if (val == null) {
                return 0L;
            }
            return val;
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return 0L;
        }
    }

    @Override
    public long getValidRealisation(Indicateur indicateur, Trimestre trimestre, Integer annee) {
        try {
            Long val = realisationDao.getValidRealisation(indicateur, trimestre, annee);
            if (val == null) {
                return 0L;
            }
            return val;
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return 0L;
        }
    }

    @Override
    public Utilisateur saveUtilisateur(Utilisateur u) {
        try {
            String pwd = Util.hacherMotDepasse("SHA1",u.getPassword());
            u.setPassword(pwd);
            return utilisateurDao.create(u);

        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Utilisateur updateUtilisateur(Utilisateur u) {
        try {
            return utilisateurDao.update(u);
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Utilisateur getUtilisateurByUsername(String username) {
        try {
            return utilisateurDao.findById(username);
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void deleteUtilisateur(String username) {
        try {
            utilisateurDao.delete(utilisateurDao.findById(username));
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<Utilisateur> getAllUtilisateur() {
        try {
            return utilisateurDao.findAll();
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public ScoreQualite saveOrUpdateScoreQualite(ScoreQualite sc) {
        if (sc.getId() == null) {
            try {
                return scoreQualiteDao.create(sc);
            } catch (DataAccessException ex) {
                Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                return scoreQualiteDao.update(sc);
            } catch (DataAccessException ex) {
                Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    @Override
    public ScoreQualite getScoreQualiteById(Long id) {
        try {
            return scoreQualiteDao.findById(id);
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<ScoreQualite> getAllScoreQualite() {
        try {
            return scoreQualiteDao.findAll();
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public void deleteScoreQualite(Long id) {
        try {
            scoreQualiteDao.delete(scoreQualiteDao.findById(id));
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public BonusEloignement saveOrUpdateBonusEloignement(BonusEloignement bonus) {

        try {
            if (bonus.getId() == null) {

                return bonusEloignementDao.create(bonus);

            } else {

                return bonusEloignementDao.update(bonus);

            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public BonusEloignement getBonusEloignementById(Long id) {
        try {
            return bonusEloignementDao.findById(id);
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<BonusEloignement> getAllBonusEloignement() {
        try {
            return bonusEloignementDao.findAll();
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public void deleteBonusEloignement(Long id) {
        try {
            bonusEloignementDao.delete(bonusEloignementDao.findById(id));
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public RealisationTrimestrielle getRealisationByTrimestre(Indicateur indicateur, Trimestre trimestre, Integer annee) {

        long val = 0L;
        try {
            List<Realisation> realisations = new ArrayList<Realisation>();
            for (Mois mois : moisDao.findByTrimestre(trimestre)) {
                realisations.addAll(realisationDao.getRealisationByTrimestre(indicateur, mois, annee));
            }
            for (Realisation realisation : realisations) {
                val += realisation.getValeur();
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            return new RealisationTrimestrielle(cibleDao.getByIndicateurTrimestreAnnee(indicateur, trimestre, annee).getValeur(), subventionDao.getByIndicateurTrimestreAnnee(indicateur, trimestre, annee).getValeur(), val, true);
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoResultException ex) {
            return new RealisationTrimestrielle(0, 0, 0L, false);
        }
        return null;
    }
   /*
    @Override
    public ScoreQualite getScoreByCentreTrimestreAnnee(Centre centre, Trimestre trimestre, Integer annee) {
        try {
            return scoreQualiteDao.getByCentreTrimestre(centre, trimestre, annee);
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public BonusEloignement getBonusByCentreTrimestreAnnee(Centre centre, Trimestre trimestre, Integer annee) {
        try {
            return bonusEloignementDao.getByCentreTrimestre(centre, trimestre, annee);
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public long getScoreParCentreTrimestreAnnee(Centre centre, Trimestre trimestre, Integer annee) {
        List<Realisation> realisations = new ArrayList<Realisation>();
        long val = 0;
        try {
            for (Mois mois : moisDao.findByTrimestre(trimestre)) {
                realisations.addAll(realisationDao.getRealisationByCentreMoisAnnee(centre, mois, annee));
            }
            for (Realisation realisation : realisations) {
                val += realisation.getValeur();
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            return (long) (val * scoreQualiteDao.getByCentreTrimestre(centre, trimestre, annee).getValeur() / 100);
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoResultException ex) {
            return 0;
        }
        return 0;
    }

    @Override
    public long getBonusParCentreParTrimestreAnnee(Centre centre, Trimestre trimestre, Integer annee) {
        List<Realisation> realisations = new ArrayList<Realisation>();
        long val = 0;
        try {
            for (Mois mois : moisDao.findByTrimestre(trimestre)) {
                realisations.addAll(realisationDao.getRealisationByCentreMoisAnnee(centre, mois, annee));
            }
            for (Realisation realisation : realisations) {
                val += realisation.getValeur();
            }
            return (long) (val * bonusEloignementDao.getByCentreTrimestre(centre, trimestre, annee).getValeur() / 100);
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoResultException ex) {
            return 0;
        }
        return 0;
    }

    @Override
    public Double getTotalScore(Trimestre trimestre, Integer annee) {
        Double val = 0.0;
        try {
            for (Centre centre : centreDao.findAll()) {
                val += getScoreByCentreTrimestreAnnee(centre, trimestre, annee).getValeur();

            }
            return val / centreDao.findAll().size();
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0.0;
    }

    @Override
    public Double getTotalBonus(Trimestre trimestre, Integer annee) {
        Double val = 0.0;
        try {
            for (Centre centre : centreDao.findAll()) {
                val += getScoreByCentreTrimestreAnnee(centre, trimestre, annee).getValeur();

            }
            return val / centreDao.findAll().size();
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0.0;
    }

    @Override
    public Double getTotalSocore(Trimestre trimestre, Integer annee, District district) {
        Double val = 0.0;
        try {
            for (Centre c : centreDao.findByDistrict(district)) {
                val += getScoreByCentreTrimestreAnnee(c, trimestre, annee).getValeur();
            }
            return val / centreDao.findByDistrict(district).size();
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0.0;
    }

    @Override
    public Double getTotalBonus(Trimestre trimestre, Integer annee, District district) {
        Double val = 0.0;
        try {
            for (Centre c : centreDao.findByDistrict(district)) {
                val += getBonusByCentreTrimestreAnnee(c, trimestre, annee).getValeur();
            }
            return val / centreDao.findByDistrict(district).size();
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0.0;
    }

    @Override
    public Double getTotalScore(Trimestre trimestre, Integer annee, Centre centre, District district) {
        Double val = 0.0;
        if (district != null) {
            List<Centre> centres = new ArrayList<Centre>();
            try {
                centres = centreDao.findByDistrict(district);
            } catch (DataAccessException ex) {
                Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            for (Centre c : centres) {
                val += getScoreByCentreTrimestreAnnee(c, trimestre, annee).getValeur();
            }
            val /= centres.size();
        } else if (centre != null) {
            val = getScoreByCentreTrimestreAnnee(centre, trimestre, annee).getValeur();
        } else {
            List<Centre> centres = new ArrayList<Centre>();
            try {
                centres = centreDao.findAll();
            } catch (DataAccessException ex) {
                Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            for (Centre c : centres) {
                val += getScoreByCentreTrimestreAnnee(c, trimestre, annee).getValeur();
            }
            val /= centres.size();
        }
        return val;
    }

    @Override
    public Double getTotalBonus(Trimestre trimestre, Integer annee, Centre centre, District district) {
        Double val = 0.0;
        if (district != null) {
            List<Centre> centres = new ArrayList<Centre>();
            try {
                centres = centreDao.findByDistrict(district);
            } catch (DataAccessException ex) {
                Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            for (Centre c : centres) {
                val += getBonusByCentreTrimestreAnnee(c, trimestre, annee).getValeur();
            }
            val /= centres.size();
        } else if (centre != null) {
            val = getBonusByCentreTrimestreAnnee(centre, trimestre, annee).getValeur();
        } else {
            List<Centre> centres = new ArrayList<Centre>();
            try {
                centres = centreDao.findAll();
            } catch (DataAccessException ex) {
                Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            for (Centre c : centres) {
                val += getBonusByCentreTrimestreAnnee(c, trimestre, annee).getValeur();
            }
            val /= centres.size();
        }
        return val;
    }*/

    @Override
    public double countBonusEloignement(District district, Centre centre, CategorieIndicateur cate, Integer annee, Trimestre trimestre) {
        try {
            return realisationDao.countBonusEloignement(district, centre, cate, annee, trimestre);
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return 0L;
        }
    }

    @Override
    public ScoreQualite getScoreQualite(Trimestre trimestre, Integer annee) {
        try{
            return scoreQualiteDao.getByCentreTrimestre(trimestre, annee);
        }catch(NoResultException ex){
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<Realisation> getRealisation(Centre c, Indicateur indi, Integer an, Mois m) {
        try {
            return realisationDao.getRealisation(c, indi, an, m);
        } catch (DataAccessException ex) {
            Logger.getLogger(SuiviServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    
}