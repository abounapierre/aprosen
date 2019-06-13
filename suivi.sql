-- MySQL dump 10.13  Distrib 5.5.37, for debian-linux-gnu (i686)
--
-- Host: localhost    Database: suivi
-- ------------------------------------------------------
-- Server version	5.5.37-0ubuntu0.12.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `suivi`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `suivi` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `suivi`;

--
-- Table structure for table `Mois`
--

DROP TABLE IF EXISTS `Mois`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Mois` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nom` varchar(255) NOT NULL,
  `trimestre_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_1gy688tcfa79qetwxigab3lra` (`nom`),
  KEY `FK_nd7kuvga4u09k4akhoc66difi` (`trimestre_id`),
  CONSTRAINT `FK_nd7kuvga4u09k4akhoc66difi` FOREIGN KEY (`trimestre_id`) REFERENCES `Trimestre` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Mois`
--

LOCK TABLES `Mois` WRITE;
/*!40000 ALTER TABLE `Mois` DISABLE KEYS */;
INSERT INTO `Mois` VALUES (1,'Janvier',1),(2,'Fevrier',1),(3,'Mars',1),(4,'Avril',2),(5,'Mai',2),(6,'Juin',2),(7,'Juillet',3),(8,'Aout',3),(9,'Septembre',3),(10,'Octobre',4),(11,'Novembre',4),(12,'Decembre',4);
/*!40000 ALTER TABLE `Mois` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Trimestre`
--

DROP TABLE IF EXISTS `Trimestre`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Trimestre` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `numero` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_nowq8dktfcdg6f2duxbfia525` (`code`),
  UNIQUE KEY `UK_s6f6wpfwcwxbjdu51kx3mt03k` (`numero`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Trimestre`
--

LOCK TABLES `Trimestre` WRITE;
/*!40000 ALTER TABLE `Trimestre` DISABLE KEYS */;
INSERT INTO `Trimestre` VALUES (1,'Premier Trimestre',1),(2,'Deuxième Trimestre',2),(3,'Troisième Trimestre',3),(4,'Quatrième Trimestre',4);
/*!40000 ALTER TABLE `Trimestre` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-08-17 17:31:33
