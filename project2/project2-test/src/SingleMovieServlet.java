import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.annotation.Resource;
import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class SingleMovieServlet
 */
@WebServlet(name="/SingleMovieServlet", urlPatterns = "/api/singlemovie")
public class SingleMovieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@Resource(name = "jdbc/moviedb")
	private DataSource dataSource; 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SingleMovieServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json"); // Response mime type
		String movieid = request.getParameter("id");
		System.out.println("id: "+movieid);
		
		
		// Output stream to STDOUT
		PrintWriter out = response.getWriter();
		
		try {
			// Get a connection from dataSource
			Connection dbcon = dataSource.getConnection();
			System.out.println("connection ");
			
			String query = "Select m.id, m.title, m.year, m.director, g.name, s.name, s.id\n" + 
					"From movies as m, genres_in_movies as gim, genres as g, stars_in_movies as sim, stars as s\n" + 
					"Where m.id = gim.movieId and gim.genreId = g.id and m.id = sim.movieId and sim.starId = s.id";
			if(!movieid.equalsIgnoreCase("null")) {
				query = query.concat(" and m.id = '"+ movieid +"'");
				
			}
			
			//System.out.println(query);
			// Declare our statement
			PreparedStatement statement = dbcon.prepareStatement(query);
			// Perform the query
			ResultSet rs = statement.executeQuery();
			//ResultSet rs = statement.executeQuery();
			System.out.println("execute");
			JsonArray jsonArray = new JsonArray();
			int count = 1;
			String temp_Id = "";
    		String temp_movieTitle = "";
    		String temp_year = "";
    		String temp_director = "";
    		String temp_starId = "";
    		String Genre="";
    		String Star="";
			try {
				while(rs.next()) {
					String Id = rs.getString("m.id");
    				String Title = rs.getString("m.title");
    				String Year = rs.getString("m.year");
    				String Director = rs.getString("m.director");
    				String Genresname = rs.getString("g.name");
    				String Starsname = rs.getString("s.name");
    				String StarId = rs.getString("s.id");
    				//if the movie in this row is the same movie 
    				//in the previous row with different genre or star 
    				if(temp_movieTitle.contains(Title)){
        	        	//if this row has a different genre, add it to the genre
    					if(!Genre.contains(Genresname)) {
    						//System.out.println("if this row has a different genre, add it to the genre");
        	        		Genre = Genre.concat(", "+Genresname);
        	        	}
    					
    					//if this row has a different genre, add it to the genre
        	        	if(!Star.contains(Starsname)) {
        	        		//System.out.println("if this row has a different star, add it to the star");
        	        		Star = Star.concat(", "+Starsname);
        	        		temp_starId = temp_starId.concat(", "+StarId);
        	        	}
    				}else {
    					
    					//if this row has a different movie, web should print the total information of the previous page
        	        	if(count==1) {
        	        		//System.out.println("count=1");
            	        	Genre = Genresname;
            	        	Star = Starsname;
            	        	temp_starId = StarId;
            	        	temp_Id = Id;
            	        	temp_movieTitle = Title;
            	        	temp_year = Year;
            	        	temp_director = Director;
            	        	count ++;
            	        	continue;
        	        	}
        	        	// Create a JsonObject based on the data we retrieve from rs

        				JsonObject jsonObject = new JsonObject();
        				jsonObject.addProperty("movie_id", temp_Id);
        				jsonObject.addProperty("movie_title", temp_movieTitle);
        				jsonObject.addProperty("movie_year", temp_year);
        				jsonObject.addProperty("movie_director", temp_director);
        				jsonObject.addProperty("movie_genre", Genre);
        				jsonObject.addProperty("movie_star", Star);
        				jsonObject.addProperty("star_id", temp_starId);
        				Genre = Genresname;
        	        	Star = Starsname;
        	        	temp_Id = Id;
        	        	temp_movieTitle = Title;
        	        	temp_starId = StarId;
        	        	temp_year = Year;
        	        	temp_director = Director;
        	        	count ++;
        				jsonArray.add(jsonObject);
        				System.out.println("count: "+count);
            			System.out.println("jsonArray: "+jsonArray.toString());
    				}
    				
				}
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("movie_id", temp_Id);
				jsonObject.addProperty("movie_title", temp_movieTitle);
				jsonObject.addProperty("movie_year", temp_year);
				jsonObject.addProperty("movie_director", temp_director);
				jsonObject.addProperty("movie_genre", Genre);
				jsonObject.addProperty("movie_star", Star);
				jsonObject.addProperty("star_id", temp_starId);
				jsonArray.add(jsonObject);
    			System.out.println("count: "+count);
    			System.out.println("jsonArray: "+jsonArray.toString());
                // write JSON string to output
                out.write(jsonArray.toString());
                // set response status to 200 (OK)
                response.setStatus(200);
			}catch (NumberFormatException e) {
    		    e.printStackTrace();
			}
			rs.close();
			statement.close();
			dbcon.close();
		}catch (Exception e) {
			// write error message JSON object to output
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("errorMessage", e.getMessage());
			out.write(jsonObject.toString());

			// set response status to 500 (Internal Server Error)
			response.setStatus(500);
		}
		
		out.close();
	}

	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
