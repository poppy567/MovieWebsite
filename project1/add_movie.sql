DELIMITER $$ 

CREATE PROCEDURE add_movie ( IN movie_Id varchar(10),IN movie_title varchar(100), IN movie_year int(11), IN movie_director varchar(100), IN star_Id varchar(10), IN star_name varchar(100), IN star_birthyear int(11), IN genre_name varchar(32),IN rating float(8))
BEGIN


	declare genreId int(11);
	declare movie_ int;
    declare star_ int;
    declare genre_ int;
    
    declare stars_in_movies_ int;
    declare genres_in_movies_ int; 
	SET genreId = (select max(id) from genres);    
	SET genreId = genreId + 1;

	
 	set movie_=1;
 	set star_=1;
 	set genre_=1;
 	set stars_in_movies_=1;
 	set genres_in_movies_=1;


	IF not exists (select  * from movies where title=movie_title and year=movie_year and director=movie_director) Then
		insert into movies(id,title,year,director) values(movie_Id,movie_title,movie_year,movie_director);
		set movie_=0;
	ELSE
		set movie_=1;
	END IF;

	IF (movie_=0) Then
	insert into ratings(movieId,rating,numVotes) values(movie_Id,rating,0);
	END IF;

	IF not exists (select  * from stars where name=star_name) Then
		insert into stars(id,name,birthyear) values(star_Id,star_name,star_birthyear);
    	set star_=0;
	ELSE
		set star_=1;
	END IF;

	IF not exists(select  * from genres where name=genre_name) Then
		insert into genres(id,name) values(genreId,genre_name);
    	set genre_=0;
	ELSE
		set genre_=1;
	END IF;

	set movie_Id= ( select  max(id) from movies where title=movie_title);
	set star_Id=(select  max(id) from stars where name=star_name);
	set genreId=(select  id from genres where name=genre_name);

	IF not exists( select * from stars_in_movies where movieId = movie_Id and starId=star_Id) Then
		insert into stars_in_movies(starId,movieId) values (star_Id,movie_Id);
     	set stars_in_movies_=0;
	END IF;

	IF not exists( select * from genres_in_movies where movieId = movie_Id and genreId=genreId) Then
		insert into genres_in_movies(genreId,movieId) values (genreId,movie_Id);
		set genres_in_movies_=0;
	END IF;
	set @result_status=0;
	CASE
		WHEN (movie_=0 and star_=0 and genre_=0) Then
		set @result_status=1;
		select @result_status;
		WHEN(movie_=0 and star_=0 and genre_=1) Then
		set @result_status=2;
		select @result_status;

		When(movie_=0 and star_=1 and genre_=0) Then
		set @result_status=3;
		select @result_status;

		When(movie_=0 and star_=1 and genre_=1) Then
		set @result_status=4;
		select @result_status;

		When(movie_=1 and star_=0 and genre_=0) Then
		set @result_status=5;
		select @result_status;

		When(movie_=1 and star_=0 and genre_=1) Then
		set @result_status=6;
		select @result_status;

		When(movie_=1 and star_=1 and genre_=0) Then
		set @result_status=7;
		select @result_status;

		When(movie_=1 and star_=1 and genre_=1) Then
		set @result_status=8;
		select @result_status;
	END CASE;
END
$$
DELIMITER ; 