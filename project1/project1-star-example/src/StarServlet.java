

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// this annotation maps this Java Servlet Class to a URL
@WebServlet("/movies")
public class StarServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public StarServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // change this to your own mysql username and password
        String loginUser = "mytestuser";
        String loginPasswd = "mypassword";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
		
        // set response mime type
        response.setContentType("text/html"); 

        // get the printwriter for writing response
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"mystyle.css\" />");
        out.println("<head><title>Fabflix</title></head>");
        
        
        try {
        		Class.forName("com.mysql.jdbc.Driver").newInstance();
        		// create database connection
        		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
        		// declare statement
        		Statement statement = connection.createStatement();
        		// prepare query
        		String query = "select movies.title, movies.year, movies.director, genres.name, stars.name, ratings.rating from ((((movies join stars_in_movies on movies.id = stars_in_movies.movieId) join stars on stars_in_movies.starId = stars.id) join genres_in_movies on movies.id = genres_in_movies.movieId) join genres on genres.id = genres_in_movies.genreId) join ratings on movies.id = ratings.movieId order by ratings.rating desc, movies.title;";
        		// execute query
        		ResultSet resultSet = statement.executeQuery(query);

        		out.println("<body>");
        		out.println("<h1>MovieDB Movies</h1>");
        		
        		out.println("<table border>");
        		
        		// add table header row
        		out.println("<tr>");
        		out.println("<th>count</th>");
        		out.println("<th>title</th>");
        		out.println("<th>year</th>");
        		out.println("<th>director</th>");
        		out.println("<th>genres</th>");
        		out.println("<th>stars</th>");
        		out.println("<th>rating</th>");
        		out.println("</tr>");
        		int count = 0;
        		String temp_movieTitle = "";
        		String temp_year = "";
        		String temp_rating = "";
        		String temp_director = "";
        		String genre="";
        		String star="";
        		//System.out.println(""+temp_movieTitle);
        		// add a row for every star result
        		while (resultSet.next() && count<=20) {
        			// get a star from result set
        			String title = resultSet.getString("movies.title");
        			String year = resultSet.getString("movies.year");
        	        String rating = resultSet.getString("ratings.rating");
        	        String director = resultSet.getString("movies.director");
        	        String genresname = resultSet.getString("genres.name");
        	        String starname = resultSet.getString("stars.name");
        	        if(temp_movieTitle.contains(title)){
        	        	if(!genre.contains(genresname)) {
        	        		genre = genre.concat(", "+genresname);
        	        	}
        	        	if(!star.contains(starname)) {
        	        		star = star.concat(", "+starname);
        	        	}
        	        }
        	        else {
        	        	if(count==0) {
            	        	genre = genresname;
            	        	star = starname;
            	        	temp_movieTitle = title;
            	        	temp_year = year;
            	        	temp_rating = rating;
            	        	temp_director = director;
            	        	count ++;
            	        	continue;
        	        	}
        	        	out.println("<tr>");
        	        	out.println("<td>" + count + "</td>");
            			out.println("<td>" + temp_movieTitle + "</td>");
            			out.println("<td>" + temp_year + "</td>");
            			out.println("<td>" + temp_director + "</td>");
            			out.println("<td>" + genre + "</td>");
            			out.println("<td>" + star + "</td>");
            			out.println("<td>" + temp_rating + "</td>");
            			out.println("</tr>");
            			temp_movieTitle = title;
        	        	temp_year = year;
        	        	temp_rating = rating;
        	        	temp_director = director;
        	        	genre = genresname;
        	        	star = starname;
        	        	count++;
        	        }
        			
        		}
        		
        		out.println("</table>");
        		
        		out.println("</body>");
        		
        		resultSet.close();
        		statement.close();
        		connection.close();
        		
        } catch (Exception e) {
        		/*
        		 * After you deploy the WAR file through tomcat manager webpage,
        		 *   there's no console to see the print messages.
        		 * Tomcat append all the print messages to the file: tomcat_directory/logs/catalina.out
        		 * 
        		 * To view the last n lines (for example, 100 lines) of messages you can use:
        		 *   tail -100 catalina.out
        		 * This can help you debug your program after deploying it on AWS.
        		 */
        		e.printStackTrace();
        		
        		out.println("<body>");
        		out.println("<p>");
        		out.println("Exception in doGet: " + e.getMessage());
        		out.println("</p>");
        		out.print("</body>");
        }
        
        out.println("</html>");
        out.close();
        
	}


}
