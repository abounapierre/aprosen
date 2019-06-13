package com.abouna.sante;

import com.abouna.generic.dao.DataAccessException;
import com.abouna.sante.dao.ICategorieIndicateurDao;
import com.abouna.sante.dao.IIndicateurDao;
import com.abouna.sante.dao.ISousCategorieDao;
import com.abouna.sante.dao.ITrimestreDao;
import com.abouna.sante.entities.CategorieIndicateur;
import com.abouna.sante.entities.Indicateur;
import com.abouna.sante.entities.SousCategorie;
import com.abouna.sante.entities.Subvention;
import com.abouna.sante.entities.Utilisateur;
import com.abouna.sante.service.ISuiviService;
import com.abouna.sante.web.ApplicationContextFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * Hello world!
 *
 */
public class App 
{
    private static ISuiviService suiviService;
    private static ICategorieIndicateurDao categorieIndicateurDao;
    private static ISousCategorieDao sousCategorieDao;
    private static ITrimestreDao trimestreDao;
    private static IIndicateurDao indicateurDao;
    
    public static void main( String[] args ) throws IOException, SQLException, DataAccessException
    {
       //App.enregistrerIndicateur();
       // App.enregistrerSubvention();
        suiviService = ApplicationContextFactory.getApplicationContext().getBean(ISuiviService.class);
        Utilisateur u = new Utilisateur();
        u.setEmail("abouna.emmanuel@yahoo.fr");
        u.setNumero(652445015);
        u.setPassword("admin");
        u.setRole("admin");
        u.setUsername("abouna");
        suiviService.saveUtilisateur(u);
        
        
    }
    
    public static void enregistrerIndicateur() throws IOException, DataAccessException{
      suiviService = ApplicationContextFactory.getApplicationContext().getBean(ISuiviService.class);
        categorieIndicateurDao = ApplicationContextFactory.getApplicationContext().getBean(ICategorieIndicateurDao.class);
        sousCategorieDao = ApplicationContextFactory.getApplicationContext().getBean(ISousCategorieDao.class);
        trimestreDao = ApplicationContextFactory.getApplicationContext().getBean(ITrimestreDao.class);
        indicateurDao = ApplicationContextFactory.getApplicationContext().getBean(IIndicateurDao.class);
        POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream("/home/abouna/suivi_des_indicateurs/essai.xls"));
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow row = null;
        HSSFCell cell = null,c1 = null,c2 = null, c3 = null;
        CategorieIndicateur ci = new CategorieIndicateur();
        SousCategorie sca = new SousCategorie();
        ci = categorieIndicateurDao.findById(1L);
        for (Iterator rowIt = sheet.rowIterator(); rowIt.hasNext();){
            row = (HSSFRow) rowIt.next();
            cell = row.getCell(0);
            c1 = row.getCell(3);
            c2 = row.getCell(4);
            c3 = row.getCell(2);
            if(row.getRowNum() <= 35 & row.getRowNum() >= 0){
                if(row.getRowNum() != 18 & row.getRowNum() != 24){
                Indicateur ind = new Indicateur();
                ind.setCode(cell.getStringCellValue());
                ind.setDesignation(c1.getStringCellValue());
                ind.setPoids(c2.getNumericCellValue());      
                    
                ind.setEstFige(true);
                if(row.getRowNum() <= 3 & row.getRowNum() >= 0){
                    ind.setCategorie(categorieIndicateurDao.findById(1L));
                    ind.setSousCategorie(sousCategorieDao.findById(1L));
                }else if(row.getRowNum() <= 12 & row.getRowNum() >= 4){
                  ind.setCategorie(categorieIndicateurDao.findById(1L));
                  ind.setSousCategorie(sousCategorieDao.findById(2L));  
                }else if(row.getRowNum() <= 17 & row.getRowNum() >= 13){
                    ind.setCategorie(categorieIndicateurDao.findById(1L));
                    ind.setSousCategorie(sousCategorieDao.findById(3L));
                }else if(row.getRowNum() <= 21 & row.getRowNum() >= 19){
                    ind.setCategorie(categorieIndicateurDao.findById(3L));
                    ind.setSousCategorie(sousCategorieDao.findById(6L));
                }else if(row.getRowNum() <= 23 & row.getRowNum() >= 22){
                  ind.setCategorie(categorieIndicateurDao.findById(3L));
                  ind.setSousCategorie(sousCategorieDao.findById(7L));
                }else if(row.getRowNum() <= 29 & row.getRowNum() >= 25){
                 ind.setCategorie(categorieIndicateurDao.findById(2L));
                 ind.setSousCategorie(sousCategorieDao.findById(4L));   
                }else if(row.getRowNum() <= 35 & row.getRowNum() >= 30){
                 ind.setCategorie(categorieIndicateurDao.findById(2L));
                 ind.setSousCategorie(sousCategorieDao.findById(5L)); 
                }
                 Subvention subvention = new Subvention();
                    subvention.setAnnee(2014);
                    subvention.setTrimestre(trimestreDao.findById(1L));
                    subvention.setProvision(true);
                    subvention.setValeur((int)c3.getNumericCellValue());
                suiviService.saveOrUpdateIndicateur(ind);
                subvention.setIndicateur(indicateurDao.findById(ind.getId()));
                suiviService.saveOrUpdateSubvention(subvention);
                }
                
            } 
      }  
    }
    
    public static void enregistrerSubvention() throws IOException, DataAccessException{
      suiviService = ApplicationContextFactory.getApplicationContext().getBean(ISuiviService.class);
        trimestreDao = ApplicationContextFactory.getApplicationContext().getBean(ITrimestreDao.class);
        indicateurDao = ApplicationContextFactory.getApplicationContext().getBean(IIndicateurDao.class);
        POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream("/home/abouna/suivi_des_indicateurs/essai.xls"));
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow row = null;
        HSSFCell cell = null,c1 = null,c2 = null;
        for (Iterator rowIt = sheet.rowIterator(); rowIt.hasNext();){
            row = (HSSFRow) rowIt.next();
            cell = row.getCell(2);
           // c1 = row.getCell(2);
            //c2 = row.getCell(4);
            long i = 81;
            if(row.getRowNum() <= 35 & row.getRowNum() >= 0){
                if(row.getRowNum() != 18 & row.getRowNum() != 24){
                    Subvention subvention = new Subvention();
                    subvention.setAnnee(2014);
                    subvention.setTrimestre(trimestreDao.findById(1L));
                    subvention.setProvision(true);
                    subvention.setValeur((int)cell.getNumericCellValue());
                    subvention.setIndicateur(indicateurDao.findById(i));
                    suiviService.saveOrUpdateSubvention(subvention);
                    
                }
               i++; 
            } 
      }    
    }
}
