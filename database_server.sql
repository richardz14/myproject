-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.6.21 - MySQL Community Server (GPL)
-- Server OS:                    Win32
-- HeidiSQL Version:             8.3.0.4694
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Dumping database structure for bloodlife_db
CREATE DATABASE IF NOT EXISTS `bloodlife_db` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `bloodlife_db`;


-- Dumping structure for table bloodlife_db.person_info
CREATE TABLE IF NOT EXISTS `person_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `full_name` varchar(50) DEFAULT NULL,
  `blood_type` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `phone_number1` varchar(50) DEFAULT NULL,
  `phone_number2` varchar(50) DEFAULT NULL,
  `location_brgy` varchar(50) DEFAULT NULL,
  `location_street` varchar(50) DEFAULT NULL,
  `location_purok` varchar(50) DEFAULT NULL,
  `bday_month` varchar(50) DEFAULT NULL,
  `bday_day` varchar(50) DEFAULT NULL,
  `bday_year` varchar(50) DEFAULT NULL,
  `nick_name` varchar(50) DEFAULT NULL,
  `donate_boolean` varchar(50) DEFAULT NULL,
  `lat` varchar(50) NOT NULL,
  `long` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

-- Dumping data for table bloodlife_db.person_info: ~7 rows (approximately)
DELETE FROM `person_info`;
/*!40000 ALTER TABLE `person_info` DISABLE KEYS */;
INSERT INTO `person_info` (`id`, `full_name`, `blood_type`, `email`, `password`, `phone_number1`, `phone_number2`, `location_brgy`, `location_street`, `location_purok`, `bday_month`, `bday_day`, `bday_year`, `nick_name`, `donate_boolean`, `lat`, `long`) VALUES
	(1, 'rodolfo javier', 'B', 'javier@gmail.com', 'Äiqo', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '10.1799469', '122.9068577'),
	(2, 'zaijan javier', 'B', 'z@gmail.com', 'œãXÂ', '4441', '11555', 'Brgy. 1', 'Gonzaga', 'Gwapo', 'January', '1', '2017', 'bhfh', 'YES', '10.165214', '122.865433'),
	(3, 'gracel geroy', 'A', 'gracel_geroy23@yahoo.com', 'q†½Ù=|=A', '09856754233', '08556547855', 'Brgy. 1', 'Gonzaga', 'Gwapo', 'January', '1', '2017', 'ngfjgkkg', 'YES', '10.148233', '122.869741'),
	(4, 'hghffh', 'A', 'ggracel_geroy22523@yahoo.com', 'q†½Ù=|=A', '88989', '998', 'Brgy. 1', 'Gonzaga', 'Gwapo', 'January', '1', '2017', 'hhfjf', 'YES', '10.121855', '122.872266'),
	(5, 'mam goroy', 'A', 'ggracel_geroy233@yahoo.com', 'q†½Ù=|=A', '88989', '998', 'Brgy. 1', 'Gonzaga', 'Gwapo', 'January', '1', '2017', 'hhfjf', 'YES', '10.116699', '122.871783'),
	(6, 'tumabing jr', 'A', 'tumabing@yahoo.com', 'q†½Ù=|=A', '88989', '998', 'Brgy. 1', 'Gonzaga', 'Gwapo', 'January', '1', '2017', 'hhfjf', 'YES', '10.155844', '122.861129'),
	(7, 'tumabing sr', 'A', 'tumabing_sr@yahoo.com', 'q†½Ù=|=A', '88989', '998', 'Brgy. 1', 'Gonzaga', 'Gwapo', 'January', '1', '2017', 'hhfjf', 'YES', '10.155844', '122.870075');
/*!40000 ALTER TABLE `person_info` ENABLE KEYS */;


-- Dumping structure for table bloodlife_db.practice
CREATE TABLE IF NOT EXISTS `practice` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fn` varchar(50) DEFAULT NULL,
  `ln` varchar(50) DEFAULT NULL,
  `gen` char(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

-- Dumping data for table bloodlife_db.practice: ~6 rows (approximately)
DELETE FROM `practice`;
/*!40000 ALTER TABLE `practice` DISABLE KEYS */;
INSERT INTO `practice` (`id`, `fn`, `ln`, `gen`) VALUES
	(1, 'rr', 'fgga', '\nyó!¾âQÏkêwv¥â&)'),
	(2, 'ttttt', 'tettet', '»{¼£º¥š1daô)¬ ÌÝ('),
	(3, 'ryftg', 'oooopp', '»{¼£º¥š1daô)¬ ÌÝ('),
	(4, 'hg', 'jhgggt', NULL),
	(5, 'rodolfo', 'javier', NULL),
	(6, 'rodolfo', 'javier', 'dfewf');
/*!40000 ALTER TABLE `practice` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
