package com.abouna.sante.dao.impl;

import com.abouna.generic.dao.DataAccessException;
import com.abouna.generic.dao.impl.GenericDao;
import com.abouna.sante.dao.IRealisationDao;
import com.abouna.sante.entities.BonusEloignement;
import com.abouna.sante.entities.BonusEloignement_;
import com.abouna.sante.entities.CategorieIndicateur;
import com.abouna.sante.entities.Centre;
import com.abouna.sante.entities.Centre_;
import com.abouna.sante.entities.Cible;
import com.abouna.sante.entities.Cible_;
import com.abouna.sante.entities.District;
import com.abouna.sante.entities.District_;
import com.abouna.sante.entities.Indicateur;
import com.abouna.sante.entities.Indicateur_;
import com.abouna.sante.entities.Mois;
import com.abouna.sante.entities.Mois_;
import com.abouna.sante.entities.Realisation;
import com.abouna.sante.entities.Realisation_;
import com.abouna.sante.entities.Subvention;
import com.abouna.sante.entities.Subvention_;
import com.abouna.sante.entities.Trimestre;
import com.abouna.sante.entities.Trimestre_;
import com.abouna.abouna.projection.NombreRealisation;
import com.abouna.abouna.projection.RealisationProjection;
import com.abouna.abouna.projection.RealisationTrimestre;
import com.abouna.abouna.projection.RealisationTrimestrielle;
import com.abouna.sante.transfer.DistrictRealisation;
import com.abouna.sante.transfer.GraphiqueElement;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public class RealisationDaoImpl extends GenericDao<Realisation, Long> implements IRealisationDao {

    @Override
    public List<NombreRealisation> getByIndicateurTrimestreAnneeCentre(Indicateur indicateur, Trimestre trimestre, Integer annee, Centre centre) throws DataAccessException {
        List<NombreRealisation> result = new ArrayList<NombreRealisation>();
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<Realisation> realRoot = cq.from(Realisation.class);
        Path<Mois> moisPath = realRoot.get(Realisation_.mois);
        cq.where(cb.and(cb.equal(realRoot.get(Realisation_.centre), centre),
                cb.equal(realRoot.get(Realisation_.indicateur), indicateur),
                cb.equal(realRoot.get(Realisation_.annee), annee),
                cb.equal(moisPath.get(Mois_.trimestre), trimestre)));
        cq.multiselect(realRoot.get(Realisation_.mois).get(Mois_.nom).alias("mois"), realRoot.get(Realisation_.valeur).alias("valeur"));
        List<Tuple> toto = getManager().createQuery(cq).getResultList();
        for (Tuple tuple : toto) {
            result.add(new NombreRealisation(tuple.get("mois", String.class), 
                    tuple.get("valeur", Integer.class).longValue()));
            //System.out.println("dd s "+ tuple.get("valeur", Integer.class).longValue());
        }
        return result;
    }

    @Override
    public RealisationProjection getRealisationProjection(Indicateur indicateur, Trimestre trimestre, Integer annee) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<RealisationProjection> cq = cb.createQuery(RealisationProjection.class);
        Root<Subvention> subRoot = cq.from(Subvention.class);
        Root<Cible> cibleRoot = cq.from(Cible.class);
        cq.where(cb.and(cb.equal(subRoot.get(Subvention_.annee), annee),
                cb.equal(subRoot.get(Subvention_.indicateur), indicateur),
                cb.equal(subRoot.get(Subvention_.trimestre), trimestre),
                cb.equal(cibleRoot.get(Cible_.annee), annee),
                cb.equal(cibleRoot.get(Cible_.indicateur), indicateur),
                cb.equal(cibleRoot.get(Cible_.trimestre), trimestre)));
        cq.multiselect(subRoot.get(Subvention_.indicateur).get(Indicateur_.code),
                subRoot.get(Subvention_.valeur),
                cibleRoot.get(Cible_.valeur),
                subRoot.get(Subvention_.provision));
        return getManager().createQuery(cq).getSingleResult();
    }

    @Override
    public List<NombreRealisation> getByIndicateurTrimestreAnneeDistrict(Indicateur indicateur, Trimestre trimestre, Integer annee, District district) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<NombreRealisation> cq = cb.createQuery(NombreRealisation.class);
        Root<Realisation> realRoot = cq.from(Realisation.class);
        Path<Mois> moisPath = realRoot.get(Realisation_.mois);
        Path<Centre> centrePath = realRoot.get(Realisation_.centre);
        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(cb.equal(realRoot.get(Realisation_.indicateur), indicateur));
        predicates.add(cb.equal(realRoot.get(Realisation_.annee), annee));
        predicates.add(cb.equal(moisPath.get(Mois_.trimestre), trimestre));
        if (district != null) {
            predicates.add(cb.equal(centrePath.get(Centre_.district), district));
        }
        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        cq.groupBy(realRoot.get(Realisation_.mois));
        cq.multiselect(realRoot.get(Realisation_.mois).get(Mois_.nom),
                cb.sum(realRoot.get(Realisation_.valeur)));
        return getManager().createQuery(cq).getResultList();
    }

    @Override
    public List<DistrictRealisation> getRealisationDistrict(Indicateur indicateur, Trimestre trimestre, Integer annee) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<DistrictRealisation> cq = cb.createQuery(DistrictRealisation.class);
        Root<Realisation> realRoot = cq.from(Realisation.class);
        Path<Mois> moisPath = realRoot.get(Realisation_.mois);
        Path<Centre> centrePath = realRoot.get(Realisation_.centre);
        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(cb.equal(realRoot.get(Realisation_.indicateur), indicateur));
        predicates.add(cb.equal(realRoot.get(Realisation_.annee), annee));
        predicates.add(cb.equal(moisPath.get(Mois_.trimestre), trimestre));
        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        cq.select(cb.construct(DistrictRealisation.class, centrePath.get(Centre_.district).get(District_.code),
                cb.sum(realRoot.get(Realisation_.valeur))));
        cq.groupBy(centrePath.get(Centre_.district));
        return getManager().createQuery(cq).getResultList();
    }

    @Override
    public List<GraphiqueElement> getGraphiqueElement(Indicateur indicateur, Integer annee, Trimestre trimestre, District district) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<GraphiqueElement> cq = cb.createQuery(GraphiqueElement.class);
        Root<Realisation> realisationRoot = cq.from(Realisation.class);
        
        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(cb.equal(realisationRoot.get(Realisation_.indicateur), indicateur));
        predicates.add(cb.equal(realisationRoot.get(Realisation_.centre).get(Centre_.district), district));
        if (annee != null) {
            predicates.add(cb.equal(realisationRoot.get(Realisation_.annee), annee));
        }
        if (trimestre != null) {
            predicates.add(cb.equal(realisationRoot.get(Realisation_.mois).get(Mois_.trimestre), trimestre));
        }
        cq.where((predicates.size() == 1) ? predicates.get(0) : cb
                .and(predicates.toArray(new Predicate[0])));
        cq.multiselect(realisationRoot.get(Realisation_.centre).get(Centre_.nom), cb.quot(cb.prod(100.0,cb.sum(realisationRoot.get(Realisation_.valeur))), cb.sum(realisationRoot.get(Realisation_.centre).get(Centre_.population))));
        cq.distinct(true);
        cq.groupBy(realisationRoot.get(Realisation_.centre));
        return getManager().createQuery(cq).getResultList();
    }

    @Override
    public List<GraphiqueElement> getGraphiqueElement(Indicateur indicateur, Integer annee, Trimestre trimestre) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<GraphiqueElement> cq = cb.createQuery(GraphiqueElement.class);
        Root<Realisation> realisationRoot = cq.from(Realisation.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(cb.equal(realisationRoot.get(Realisation_.indicateur), indicateur));
        if (annee != null) {
            predicates.add(cb.equal(realisationRoot.get(Realisation_.annee), annee));
        }
        if (trimestre != null) {
            predicates.add(cb.equal(realisationRoot.get(Realisation_.mois).get(Mois_.trimestre), trimestre));
        }
        cq.where((predicates.size() == 1) ? predicates.get(0) : cb
                .and(predicates.toArray(new Predicate[0])));
        cq.multiselect(realisationRoot.get(Realisation_.centre).get(Centre_.district).get(District_.code), cb.quot(cb.prod(100.0,cb.sum(realisationRoot.get(Realisation_.valeur))), cb.sum(realisationRoot.get(Realisation_.centre).get(Centre_.population))));
        cq.groupBy(realisationRoot.get(Realisation_.centre).get(Centre_.district));
        return getManager().createQuery(cq).getResultList();
    }

    @Override
    public List<Integer> getDistinctYears() throws DataAccessException {
        List<Integer> result = new ArrayList<Integer>();
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<Realisation> realisationRoot = cq.from(Realisation.class);
        cq.multiselect(realisationRoot.get(Realisation_.annee).alias("annee"));
        cq.orderBy(cb.desc(realisationRoot.get(Realisation_.annee)));
        cq.distinct(true);
        TypedQuery<Tuple> vals = getManager().createQuery(cq);
        for (Tuple tt : vals.getResultList()) {
            result.add(tt.get("annee", Integer.class));
        }
        return result;
    }

    @Override
    public RealisationTrimestrielle getRealisationTrimestrielle(Indicateur indicateur, Integer annee, Trimestre trimestre) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<RealisationTrimestrielle> cq = cb.createQuery(RealisationTrimestrielle.class);
        Root<Realisation> realisationRoot = cq.from(Realisation.class);
        Root<Cible> cibleRoot = cq.from(Cible.class);
        Root<Subvention> subventionRoot = cq.from(Subvention.class);
        cq.where(cb.and(cb.equal(realisationRoot.get(Realisation_.annee), annee),
                cb.equal(realisationRoot.get(Realisation_.indicateur), indicateur),
                cb.equal(realisationRoot.get(Realisation_.mois).get(Mois_.trimestre), trimestre),
                cb.equal(cibleRoot.get(Cible_.indicateur), indicateur),
                cb.equal(cibleRoot.get(Cible_.annee), annee),
                cb.equal(cibleRoot.get(Cible_.trimestre), trimestre),
                cb.equal(subventionRoot.get(Subvention_.indicateur), indicateur),
                cb.equal(subventionRoot.get(Subvention_.annee), annee),
                cb.equal(subventionRoot.get(Subvention_.trimestre), trimestre)));
                 cq.multiselect(cibleRoot.get(Cible_.valeur), 
                subventionRoot.get(Subvention_.valeur), 
                cb.sum(realisationRoot.get(Realisation_.valeur)),
                subventionRoot.get(Subvention_.provision));
        return getManager().createQuery(cq).getSingleResult();
    }
    
    

    @Override
    public List<RealisationTrimestre> getByIndicateurAnneeCentre(Indicateur indicateur, Integer annee, Centre centre) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<RealisationTrimestre> cq = cb.createQuery(RealisationTrimestre.class);
        Root<Realisation> realisationRoot = cq.from(Realisation.class);
        cq.where(cb.and(cb.equal(realisationRoot.get(Realisation_.annee), annee),
                cb.equal(realisationRoot.get(Realisation_.centre), centre),
                cb.equal(realisationRoot.get(Realisation_.indicateur), indicateur)));
        cq.groupBy(realisationRoot.get(Realisation_.mois).get(Mois_.trimestre));
        cq.multiselect(realisationRoot.get(Realisation_.mois).get(Mois_.trimestre).get(Trimestre_.code),
                cb.sum(realisationRoot.get(Realisation_.valeur)));
        return getManager().createQuery(cq).getResultList();
    }

    @Override
    public Long getValidRealisation(Indicateur indicateur, Trimestre trimestre, Integer annee, Centre centre) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<Realisation> realisationRoot = cq.from(Realisation.class);
        cq.where(cb.and(cb.equal(realisationRoot.get(Realisation_.annee), annee),
                cb.equal(realisationRoot.get(Realisation_.indicateur), indicateur),
                cb.equal(realisationRoot.get(Realisation_.mois).get(Mois_.trimestre), trimestre),
                cb.equal(realisationRoot.get(Realisation_.centre), centre)));
        cq.multiselect(cb.sum(realisationRoot.get(Realisation_.valeur)).alias("donnees"));
        Tuple val = getManager().createQuery(cq).getSingleResult();
        return val.get("donnees", Long.class);
    }

    @Override
    public Long getValidRealisation(Indicateur indicateur, Trimestre trimestre, Integer annee) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<Realisation> realisationRoot = cq.from(Realisation.class);
        cq.where(cb.and(cb.equal(realisationRoot.get(Realisation_.annee), annee),
                cb.equal(realisationRoot.get(Realisation_.indicateur), indicateur),
                cb.equal(realisationRoot.get(Realisation_.mois).get(Mois_.trimestre), trimestre),
                cb.equal(realisationRoot.get(Realisation_.donneesCorrecte), Boolean.FALSE)));
        cq.multiselect(cb.sum(realisationRoot.get(Realisation_.valeur)).alias("donnees"));
        Tuple val = getManager().createQuery(cq).getSingleResult();
        return val.get("donnees", Long.class);
    }

    @Override
    public RealisationProjection getRealisationProjection1(Indicateur indicateur, Trimestre trimestre, Integer annee) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<Subvention> cq = cb.createQuery(Subvention.class);
        Root<Subvention> buRoot = cq.from(Subvention.class);
        Root<Subvention> pet = cq.from(Subvention.class);
        cq.select(buRoot).
        where(cb.and(cb.equal(buRoot.get("indicateur"), indicateur),cb.equal(buRoot.get("trimestre"),
                trimestre) ),cb.equal(buRoot.get("annee"), annee));
        TypedQuery<Subvention> tq = getManager().createQuery(cq);
        
        return null;
    }

    @Override
    public List<Realisation> getRealisationByTrimestre(Indicateur indicateur, Mois mois, Integer annee) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<Realisation> cqRealisation = cb.createQuery(Realisation.class);
        Root<Realisation> realisationRoot = cqRealisation.from(Realisation.class);
        cqRealisation.select(realisationRoot).
        where(cb.and(cb.equal(realisationRoot.get("indicateur"), indicateur),cb.equal(realisationRoot.get("mois"),
                mois) ),cb.equal(realisationRoot.get("annee"), annee));
        TypedQuery<Realisation> tqRealisation = getManager().createQuery(cqRealisation);
        List<Realisation> realisations = tqRealisation.getResultList();
        return realisations;
    }   

    @Override
    public List<Realisation> getRealisationByCentreMoisAnnee(Centre centre, Mois mois, Integer annee) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<Realisation> cqRealisation = cb.createQuery(Realisation.class);
        Root<Realisation> realisationRoot = cqRealisation.from(Realisation.class);
        cqRealisation.select(realisationRoot).
        where(cb.and(cb.equal(realisationRoot.get("centre"), centre),cb.equal(realisationRoot.get("mois"),
                mois) ),cb.equal(realisationRoot.get("annee"), annee));
        TypedQuery<Realisation> tqRealisation = getManager().createQuery(cqRealisation);
        List<Realisation> realisations = tqRealisation.getResultList();
        return realisations;
    }

    @Override
    public double countBonusEloignement(District district, Centre centre, CategorieIndicateur cate, Integer annee, Trimestre trimestre) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<Realisation> reaRoot = cq.from(Realisation.class);
        Root<BonusEloignement> bonusRoot = cq.from(BonusEloignement.class);
        Root<Subvention> subRoot = cq.from(Subvention.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(cb.equal(reaRoot.get(Realisation_.annee), annee));
        predicates.add(cb.equal(reaRoot.get(Realisation_.mois).get(Mois_.trimestre), trimestre));
        predicates.add(cb.equal(subRoot.get(Subvention_.annee), annee));
        predicates.add(cb.equal(subRoot.get(Subvention_.trimestre), trimestre));
        predicates.add(cb.equal(bonusRoot.get(BonusEloignement_.annee), annee));
        predicates.add(cb.equal(bonusRoot.get(BonusEloignement_.trimestre), trimestre));
        if(centre != null){
            predicates.add(cb.equal(reaRoot.get(Realisation_.centre), centre));
            predicates.add(cb.equal(bonusRoot.get(BonusEloignement_.centre), centre));
        }
        if(district != null){
            predicates.add(cb.equal(reaRoot.get(Realisation_.centre).get(Centre_.district), district));
            predicates.add(cb.equal(bonusRoot.get(BonusEloignement_.centre).get(Centre_.district), district));
        }
        if(cate != null){
            predicates.add(cb.equal(reaRoot.get(Realisation_.indicateur).get(Indicateur_.categorie), cate));
        }
        // Je n'en suis pas sur
        // predicates.add(cb.equal(reaRoot.get(Realisation_.annee), subRoot.get(Subvention_.annee)));
        predicates.add(cb.equal(reaRoot.get(Realisation_.indicateur), subRoot.get(Subvention_.indicateur)));
        predicates.add(cb.equal(reaRoot.get(Realisation_.centre), bonusRoot.get(BonusEloignement_.centre)));
        cq.where((predicates.size() == 1) ? predicates.get(0) : cb
                .and(predicates.toArray(new Predicate[0])));
        cq.multiselect(cb.sum(cb.prod(cb.prod(reaRoot.get(Realisation_.valeur), subRoot.get(Subvention_.valeur)), bonusRoot.get(BonusEloignement_.valeur))).alias("annee"));
        Tuple val = getManager().createQuery(cq).getSingleResult();
        //System.out.println("Douwe "+val + " et donc "+(val == null));
        if(val.get("annee") == null)
            return 0.0;        
        return val.get("annee", Double.class);        
    }

    @Override
    public List<Realisation> getRealisation(Centre c, Indicateur indi, Integer an, Mois m) throws DataAccessException{
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<Realisation> cq = cb.createQuery(Realisation.class);
        Root<Realisation> realisationRoot = cq.from(Realisation.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        if(c != null){
            predicates.add(cb.equal(realisationRoot.get(Realisation_.centre), c));
        }
        if(indi != null){
            predicates.add(cb.equal(realisationRoot.get(Realisation_.indicateur), indi));
        }
        if(an != null){
            predicates.add(cb.equal(realisationRoot.get(Realisation_.annee), an));
        }
        if(m != null){
            predicates.add(cb.equal(realisationRoot.get(Realisation_.mois), m));
        }
        cq.where((predicates.size() == 1) ? predicates.get(0) : cb
                .and(predicates.toArray(new Predicate[0]))); 
        return getManager().createQuery(cq).getResultList();
    }
}
