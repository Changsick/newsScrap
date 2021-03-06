-- admin id가 존재하지 않으면 insert 
INSERT INTO `tb_user` 
	(`EMAIL`, `PASSWORD`, `NAME`, `PHONE`, `ACTIVE`, `ROLE`, `PASSWORD_FAIL_COUNT`, `REGISTER_DATE`, `PASSWORD_CHANGE`)
	select 'admin@gridone.co.kr', '{bcrypt}$2a$10$Hd.n/CwS8uOm.3B8EgZLyeO54y26upBg/8CJ6mi..zwyUplehk8va', 'admin', NULL, 1, 0, 0, now(), now()
	from dual
WHERE NOT EXISTS (	select * from tb_user where EMAIL = 'admin@gridone.co.kr' );