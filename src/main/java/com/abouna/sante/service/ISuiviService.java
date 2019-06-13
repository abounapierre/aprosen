package com.abouna.sante.service;

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
import com.abouna.abouna.projection.RealisationProjection;
import com.abouna.abouna.projection.RealisationTrimestrielle;
import com.abouna.abouna.projection.SyntheseActivite;
import com.abouna.sante.transfer.GraphiqueElement;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public interface ISuiviService {
    /* Les district de sante */
    public List<District> getAllDistrict();
    
    public District saveOrUpdateDistrict(District district);
    
    public void deleteDistrict(Long id);

    /* les centres de sante */
    public void deleteCentre(Long id);

    public Centre saveOrUpdateCentre(Centre centre);

    public List<Centre> getAllCentre();

    /* Les categories d'indicateurs */
    public void deleteCategorieIndicateur(Long id);

    public CategorieIndicateur saveOrUpdateCategorieIndicateur(CategorieIndicateur categorie);

    public List<CategorieIndicateur> getAllCategorieIndicateur();
    
    /* Les indicateurs */

    public void deleteIndicateur(Long id);

    public Indicateur saveOrUpdateIndicateur(Indicateur indicateur);

    public List<Indicateur> getAllIndicateur();
    
    /* Les trimestres */

    public void deleteTrimestre(Long id);

    public Trimestre saveOrUpdateTrimestre(Trimestre trimestre);

    public List<Trimestre> getAllTrimestre();
    
    /* Les mois */

    public void deleteMois(Long id);

    public Mois saveOrUpdateMois(Mois mois);

    public List<Mois> getAllMois();
    
    /* Les cibles */

    public void deleteCible(Long id);

    public Cible saveOrUpdateCible(Cible cible);

    public List<Cible> getAllCible();
    
    /* Les realisations */

    public void deleteRealisation(Long id);

    public Realisation saveOrUpdateRealisation(Realisation realisation);

    public List<Realisation> getAllRealisation();
    
    /* Les subventions */

    public void deleteSubvention(Long id);

    public Subvention saveOrUpdateSubvention(Subvention subvention);

    public List<Subvention> getAllSubvention();

    public List<Centre> findCentreFromDistrict(District d);
    
    /* Les sous categories */

    public void deleteSousCategorie(Long id);

    public SousCategorie saveOrUpdateSousCategorie(SousCategorie sousCategorie);

    public List<SousCategorie> getAllSousCategorie();

    public List<SousCategorie> findSousFromCategorie(CategorieIndicateur c);

    public List<Indicateur> findIndicateurFromSousCategorie(SousCategorie sc);
    
    public RealisationProjection getRealisation(Indicateur indicateur, Trimestre trimestre, Integer annee, District district, Centre centre);

    public List<Mois> getMoisFromTrimestre(Trimestre trimestre);

    /**
     *
     * @param categorieIndicateur the value of categorieIndicateur
     * @param par1 the value of par1
     */
    public Integer getPopulation(CategorieIndicateur categorieIndicateur, Integer par1);

    public SyntheseActivite getSynthese(Indicateur indicateur, Trimestre trimestre, Integer annee);
    
    /* Les graphiques */
    
    public List<GraphiqueElement> getGraphiqueElement(Indicateur indicateur, Integer annee, Trimestre trimestre, District district);

    public List<GraphiqueElement> getGraphiqueElement(Indicateur indicateur, Integer annee, Trimestre trimestre);
    
    public List<Integer> getDistictYears();
    
    public List<Trimestre> getAllTrimestre(Integer annee);
    
    public RealisationTrimestrielle getRealisationTrimestrielle(Indicateur indicateur,  Integer annee, Trimestre trimestre);
    
    public Map<String, Long> getRealisationTrimestrielle(Indicateur indicateur, Centre centre, Integer annee);

    public void deleteIndicateurCentre(Long id);

    public List<IndicateurCentre> getCategorieFromCategorieAnnee(CategorieIndicateur centre, Integer annee);
    
    public IndicateurCentre saveOrUpdate(IndicateurCentre indicateur);
    
    public long getValidRealisation(Indicateur indicateur, Trimestre trimestre,Integer annee, Centre centre);
    public long getValidRealisation(Indicateur indicateur, Trimestre trimestre,Integer annee);

    public Utilisateur saveUtilisateur(Utilisateur u);
    public Utilisateur updateUtilisateur(Utilisateur u);
    public Utilisateur getUtilisateurByUsername(String username);
    public void deleteUtilisateur(String username);
    public List<Utilisateur> getAllUtilisateur();
    
    //les parametres
    
    public ScoreQualite saveOrUpdateScoreQualite(ScoreQualite sc);
    public ScoreQualite getScoreQualiteById(Long id);
    public List<ScoreQualite> getAllScoreQualite();
    public void deleteScoreQualite(Long id);
    
    public BonusEloignement saveOrUpdateBonusEloignement(BonusEloignement bonus);
    public BonusEloignement getBonusEloignementById(Long id);
    public List<BonusEloignement> getAllBonusEloignement();
    public void deleteBonusEloignement(Long id);
    
    public RealisationTrimestrielle getRealisationByTrimestre(Indicateur indicateur,Trimestre trimestre,Integer annee);
    
    public double countBonusEloignement(District district, Centre centre, CategorieIndicateur cate, Integer annee, Trimestre trimestre);
    /*
    public ScoreQualite getScoreByCentreTrimestreAnnee(Centre centre,Trimestre trimestre,Integer annee);
    public BonusEloignement getBonusByCentreTrimestreAnnee(Centre centre,Trimestre trimestre,Integer annee);

    public long getScoreParCentreTrimestreAnnee(Centre centre,Trimestre trimestre,Integer annee);
    public long getBonusParCentreParTrimestreAnnee(Centre centre,Trimestre trimestre,Integer annee);
    
    public Double getTotalScore(Trimestre trimestre,Integer annee);
    public Double getTotalBonus(Trimestre trimestre,Integer annee);
    public Double getTotalSocore(Trimestre trimestre,Integer annee,District district);
    public Double getTotalBonus(Trimestre trimestre,Integer annee,District district);
    public Double getTotalScore(Trimestre trimestre,Integer annee,Centre centre,District district);
    public Double getTotalBonus(Trimestre trimestre,Integer annee,Centre centre,District district); */

    public ScoreQualite getScoreQualite(Trimestre trimestre, Integer annee);

    public List<Realisation> getRealisation(Centre c, Indicateur indi, Integer an, Mois m);
    
    
}
