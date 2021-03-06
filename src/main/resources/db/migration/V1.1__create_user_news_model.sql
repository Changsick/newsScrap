CREATE TABLE IF NOT EXISTS `tb_user` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `EMAIL` varchar(50) NOT NULL COMMENT '유저 id(email)',
  `PASSWORD` varchar(200) NOT NULL COMMENT '비밀번호',
  `NAME` varchar(50) NOT NULL COMMENT '유저 이름',
  `PHONE` varchar(50) DEFAULT NULL COMMENT '휴대폰번호',
  `ACTIVE` tinyint(4) NOT NULL COMMENT '활성화플래그',
  `ROLE` smallint(6) NOT NULL COMMENT '권한Role',
  `PASSWORD_FAIL_COUNT` int(10) NOT NULL COMMENT '비밀번호실패횟수',
  `REGISTER_DATE` datetime DEFAULT current_timestamp() COMMENT '등록일',
  `PASSWORD_CHANGE` datetime DEFAULT NULL COMMENT '비밀번호 바꾼 날짜',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `tb_keywords` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USERID` int(11) DEFAULT NULL,
  `ENTERPRISE` varchar(50) NOT NULL COMMENT '회사',
  `KEYWORDS` varchar(200) DEFAULT NULL COMMENT '키워드',
  PRIMARY KEY (`ID`),
  KEY `FK_userid` (`USERID`),
  CONSTRAINT `FK_userid` FOREIGN KEY (`USERID`) REFERENCES `tb_user` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='키워드 지정 테이블';

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
  KEY `FK_keywords_1` (`KEYWORD_ID`),
  CONSTRAINT `FK_keywords_1` FOREIGN KEY (`KEYWORD_ID`) REFERENCES `tb_keywords` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='뉴스 스크랩 데이터 테이블';

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
  KEY `FK_keywords_2` (`KEYWORD_ID`),
  CONSTRAINT `FK_keywords_2` FOREIGN KEY (`KEYWORD_ID`) REFERENCES `tb_keywords` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='뉴스 데일리 모니터링 테이블';