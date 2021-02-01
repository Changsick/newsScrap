-- --------------------------------------------------------
-- 호스트:                          127.0.0.1
-- 서버 버전:                        10.2.17-MariaDB - mariadb.org binary distribution
-- 서버 OS:                        Win64
-- HeidiSQL 버전:                  9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- dailynews 데이터베이스 구조 내보내기
CREATE DATABASE IF NOT EXISTS `dailynews` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `dailynews`;

-- 테이블 dailynews.tb_keywords 구조 내보내기
CREATE TABLE IF NOT EXISTS `tb_keywords` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ENTERPRISE` varchar(50) NOT NULL COMMENT '회사',
  `KEYWORDS` varchar(200) DEFAULT NULL COMMENT '키워드',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3605 DEFAULT CHARSET=utf8 COMMENT='키워드 지정 테이블';

-- 내보낼 데이터가 선택되어 있지 않습니다.
-- 테이블 dailynews.tb_monitoring_news 구조 내보내기
CREATE TABLE IF NOT EXISTS `tb_monitoring_news` (
  `MONITORING_ID` int(11) NOT NULL AUTO_INCREMENT,
  `KEYWORD_ID` int(11) NOT NULL,
  `TITLE` varchar(200) NOT NULL,
  `CONTENT` text DEFAULT '',
  `LINK` varchar(500) DEFAULT NULL,
  `REGITDATE` datetime NOT NULL DEFAULT current_timestamp(),
  `NEWSDATE` date NOT NULL,
  `PRESS` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`MONITORING_ID`),
  KEY `FK1_KEYWORD_ID` (`KEYWORD_ID`),
  CONSTRAINT `FK1_KEYWORD_ID` FOREIGN KEY (`KEYWORD_ID`) REFERENCES `tb_keywords` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=165 DEFAULT CHARSET=utf8 COMMENT='뉴스 데일리 모니터링 테이블';

-- 내보낼 데이터가 선택되어 있지 않습니다.
-- 테이블 dailynews.tb_news 구조 내보내기
CREATE TABLE IF NOT EXISTS `tb_news` (
  `NEWS_ID` int(11) NOT NULL AUTO_INCREMENT,
  `KEYWORD_ID` int(11) NOT NULL,
  `TITLE` varchar(200) NOT NULL,
  `CONTENT` text DEFAULT '',
  `LINK` varchar(500) DEFAULT NULL,
  `REGITDATE` datetime NOT NULL DEFAULT current_timestamp(),
  `NEWSDATE` date NOT NULL,
  `PRESS` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`NEWS_ID`),
  KEY `FK1_KEYWORDS` (`KEYWORD_ID`),
  CONSTRAINT `FK1_KEYWORDS` FOREIGN KEY (`KEYWORD_ID`) REFERENCES `tb_keywords` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6787 DEFAULT CHARSET=utf8 COMMENT='뉴스 스크랩 데이터 테이블';

-- 내보낼 데이터가 선택되어 있지 않습니다.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
