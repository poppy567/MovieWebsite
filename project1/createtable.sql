create table movies(
	id varchar(10) not null, 
	title varchar(100) not null,
	year integer not null,
	director varchar(100) not null,
	PRIMARY KEY ( id ),
	FULLTEXT (title)
 )DEFAULT CHARSET=utf8;

create table stars(
	id varchar(10) not null,
	name varchar(100) not null,
	birthyear integer,
	PRIMARY KEY ( id )
)DEFAULT CHARSET=utf8;

create table stars_in_movies(
	starId varchar(10) not null,
	movieId varchar(10) not null,
	FOREIGN KEY(starId) REFERENCES stars(id)
		on delete cascade on update cascade,
	FOREIGN KEY(movieId) REFERENCES movies(id)
		on delete cascade on update cascade
)DEFAULT CHARSET=utf8;

create table genres(
	id integer AUTO_INCREMENT not null,
	name varchar(32) not null,
	PRIMARY KEY(id)
)DEFAULT CHARSET=utf8;

create table genres_in_movies(
	genreId integer not null,
	movieId varchar(10) not null,
	FOREIGN KEY(genreId) REFERENCES genres(id)
		on delete cascade on update cascade,
	FOREIGN KEY(movieId) REFERENCES movies(id)
		on delete cascade on update cascade
)DEFAULT CHARSET=utf8;

create table creditcards(
	id varchar(20) not null, 
	firstName varchar(50) not null,
	lastName varchar(50) not null,
	expiration date not null,
	PRIMARY KEY(id)
)DEFAULT CHARSET=utf8;

create table customers(
	id integer not null AUTO_INCREMENT,
	firstName varchar(50) not null,
	lastName varchar(50) not null,
	ccId varchar(20) not null,
	address varchar(200) not null,
	email varchar(50) not null,
	password varchar(20) not null,
	PRIMARY KEY(id),
	FOREIGN KEY(ccId) REFERENCES creditcards(id)
		on delete cascade on update cascade
)DEFAULT CHARSET=utf8;

create table sales(
	id integer not null AUTO_INCREMENT,
	customerId integer not null, 
	movieId varchar(10)not null, 
	saleDate date not null,
	PRIMARY KEY(id),
	FOREIGN KEY(customerId) REFERENCES customers(id)
		on delete cascade on update cascade,
	FOREIGN KEY(movieId) REFERENCES movies(id)
		on delete cascade on update cascade
)DEFAULT CHARSET=utf8;

create table ratings(
	movieId varchar(10) not null, 
	rating FLOAT not null,
	numVotes integer not null,
	FOREIGN KEY(movieId) REFERENCES movies(id)
		on delete cascade on update cascade
)DEFAULT CHARSET=utf8;

create table employees(
    email varchar(50) not null, 
    password varchar(20) not null,
    fullname varchar(100),
    PRIMARY KEY ( email )
    )DEFAULT CHARSET=utf8;