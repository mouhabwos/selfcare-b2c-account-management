CREATE TABLE if not exists user (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  msisdn varchar(30) NOT NULL,
  nom varchar(20) DEFAULT NULL,
  prenom varchar(20) DEFAULT NULL,
  date datetime NOT NULL,
  user_type varchar(20) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=332 DEFAULT CHARSET=latin1;

