import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Entity.GenreInMovie;
import Entity.Movie;
import Entity.Star;
import Entity.StarInMovie;
import parse.ParseActors63;
import parse.ParseCasts124;
import parse.ParseMain243;

public class Main {

       public static void main(String[] args) {
    	   try {
    			Statement select;
    			String query;
    			ResultSet result;
    			Class.forName("com.mysql.jdbc.Driver").newInstance();
    			// Connect to the test database
    			Connection connection = DriverManager.getConnection("jdbc:mysql:///moviedb?autoReconnect=true&useSSL=false",
    					"mytestuser", "mypassword");
    			select = connection.createStatement();

    			// Create an instance
    			ParseMain243 main243Parser = new ParseMain243();
    			System.out.println("Size of movies: " + main243Parser.getMovies().size());
    			
    			
    			for (int i = 0; i < main243Parser.getMovies().size(); i++) {
    				Movie movie = (Movie) main243Parser.getMovies().get(i);
    				query = "select * from movies where id=\""+movie.getId()+"\"";
    				result = select.executeQuery(query);
    				if (!result.next()) {
    					query = "insert into movies(id,title,year,director) values(\"" + movie.getId() + "\",\"" + movie.getTitle()
    							+ "\"," + movie.getYear() + ",\"" + movie.getDirector() + "\")";
    					 select.executeUpdate(query);
    				}
    			}
    			ParseActors63 actors63Parser = new ParseActors63();
    			for (int i = 0; i < actors63Parser.getStars().size(); i++) {
    				Star star = (Star) actors63Parser.getStars().get(i);
    				query = "select * from stars where id=\"" + star.getId() + "\"" ;
    				 result=select.executeQuery(query);
    				if (!result.next()) {
    					query = "insert into stars(id,name,birthYear) values(\"" + star.getId() + "\",\"" + star.getName() + "\","
    							+ star.getBirthYear() + ")";
    					select.executeUpdate(query);
    				}
    			}

    			// Create an instance
    			ParseCasts124 casts124Parser = new ParseCasts124();
    			
    			query="SET FOREIGN_KEY_CHECKS=0";
    			result = select.executeQuery(query);
    			
    			for (int i = 0; i < casts124Parser.getStarInMovies().size(); i++) {
    				StarInMovie starinmovie = (StarInMovie) casts124Parser.getStarInMovies().get(i);
    				query = "select * from stars_in_movies where starId=\"" + starinmovie.getStarId() + "\" and movieId=\""
    						+ starinmovie.getMovieId()+"\"";
    				result = select.executeQuery(query);
    				if (!result.next()) {
    					query = "insert into stars_in_movies(starId,movieId) values(\"" + starinmovie.getStarId() + "\",\""
    							+ starinmovie.getMovieId() + "\")";
    					select.executeUpdate(query);
    				}
    			}
    			
    			query="SET FOREIGN_KEY_CHECKS=1";
    			result = select.executeQuery(query);
    			System.out.println("success");   
    	   }catch(Exception e) {
    		   e.printStackTrace();
    	   }
	}
}
