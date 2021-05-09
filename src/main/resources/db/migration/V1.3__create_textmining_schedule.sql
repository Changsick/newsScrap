-- 테이블 dailynews.tb_schedule 구조 내보내기
CREATE TABLE IF NOT EXISTS `tb_schedule` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USERID` int(11) DEFAULT NULL,
  `TYPE` int(11) DEFAULT NULL,
  `CRON` varchar(50) DEFAULT NULL,
  `NEXTTIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK1_schedule` (`USERID`),
  CONSTRAINT `FK1_schedule` FOREIGN KEY (`userid`) REFERENCES `tb_user` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 테이블 dailynews.tb_textmining 구조 내보내기
CREATE TABLE IF NOT EXISTS `tb_textmining` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USERID` int(11) DEFAULT NULL,
  `KEYWORD_ID` int(11) DEFAULT NULL,
  `TOP5` varchar(200) DEFAULT NULL,
  `WORDCLOUD` longtext DEFAULT NULL,
  `NEWS_DATE` date DEFAULT NULL,
  `REGISTER_DATE` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`ID`),
  KEY `FK1_textmining` (`USERID`),
  KEY `FK2_keyowrd` (`KEYWORD_ID`),
  CONSTRAINT `FK1_textmining` FOREIGN KEY (`userid`) REFERENCES `tb_user` (`ID`) ON DELETE CASCADE,
  CONSTRAINT `FK2_keyowrd` FOREIGN KEY (`KEYWORD_ID`) REFERENCES `tb_keywords` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;