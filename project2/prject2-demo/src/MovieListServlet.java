import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.*;
import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;



/**
 * Servlet implementation class MovieListServlet
 */
@WebServlet(name = "/MovieListServlet", urlPatterns = "/api/movielist")
public class MovieListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Resource(name = "jdbc/moviedb")
	private DataSource dataSource;   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MovieListServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    
    
    
    
    public static void timelogs(String content) {  
        try {  
            FileWriter writer = new FileWriter("/home/ubuntu/timelogTS.txt", true);  
            writer.write(content+"\n");  
            writer.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }
    
    public static void timelogj(String content) {  
        try {  
            FileWriter writer = new FileWriter("/home/ubuntu/timelogTJ.txt", true);  
            writer.write(content+"\n");  
            writer.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }
    
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * 
	 * process the request for getting different movies
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json"); // Response mime type
		
		// Retrieve parameter id from url request.
		//These three parameters need to be processed in pattern
		String title = request.getParameter("title");
		System.out.println("title1: "+title);
		
		String starname = request.getParameter("starname");
		String genre = request.getParameter("genre");
		System.out.println("starname: "+starname);
		//System.out.println("null: "+starname.equalsIgnoreCase("null"));
		System.out.println("genre: "+genre);
		String director = request.getParameter("director");
		
		//The rest parameters need not to be processed in pattern
		String year = request.getParameter("year");
		/**
		 * Default sort would be sort on title ascend 
		 **/
		String sortontitle = request.getParameter("sortontitle");
		String sortonrating = request.getParameter("sortonrating");
		System.out.println("test genre: "+genre);
		/**
		 * These two parameters need to have default value
		 * offset = 0
		 * range = 20
		**/
		//String offset = request.getParameter("offset");
		//String range = request.getParameter("range");
		
		
		

		// Output stream to STDOUT
		PrintWriter out = response.getWriter();
		try {
			long startTime = System.nanoTime();
			Context initCtx = new InitialContext();

            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            if (envCtx == null)
                out.println("envCtx is NULL");

            // Look up our data source
            DataSource ds = (DataSource) envCtx.lookup("jdbc/TestDB");
            
            if (ds == null)
                out.println("ds is null.");

            Connection dbcon = ds.getConnection();
            if (dbcon == null)
                out.println("dbcon is null.");

            
			// Get a connection from dataSource
			//Connection dbcon = dataSource.getConnection();
			System.out.println("test starname: "+starname);
			// Construct a query with parameter represented by "?"
			String query = "select m.id, m.title, m.year, m.director"
            		+ ", g.name, s.id, s.name, r.rating "
            		+ "from movies as m, genres_in_movies as gim,"
            		+ "genres as g, stars as s, stars_in_movies as sim,"
            		+ "ratings as r where m.id = gim.movieId and "
            		+ "gim.genreId = g.id and m.id = sim.movieId and "
            		+ "sim.starId = s.id and m.id = r.movieId";
			/*
			 * This part is processing the parameters received by using url
			 * Program should consider about the occasion that some parameter might not exist
			 */
			if(title!=null && !title.equalsIgnoreCase("null")) {
				String[] titlelist = title.split(",");
				query = query.concat(" and (match(m.title) against('");
				for (String retval: titlelist){
		           query = query.concat(" +"+retval+"*");
		        }
				query = query.concat("' IN BOOLEAN MODE)");

				//add fuzzy search
				query = query.concat(" or (");
				query = query.concat("edrec('"+titlelist[0]+"',title,"+titlelist[0].length()/3+")");
				for (int i = 1 ; i<titlelist.length;i++){
					query = query.concat(" and ");
					//set the tolerance of user's typo 
					//which depends on how many characters user has typed
					query = query.concat("edrec('"+titlelist[i]+"',title,"+titlelist[i].length()/3+")");
					
			    }
				query = query.concat("))");
				
				
				
			}
			
			if(starname!=null && !starname.equalsIgnoreCase("null")) {
				query = query.concat(" and s.name like \""+starname+"%\"");
			}
			if(genre!=null && !genre.equalsIgnoreCase("null")) {
				query = query.concat(" and g.name = \""+genre+"\"");
			}
			
			if(director!=null && !director.equalsIgnoreCase("null")) {
				query = query.concat(" and m.director like \""+director+"%\"");
			}
			
			if(year!=null && !year.equalsIgnoreCase("null")) {
				query = query.concat(" and m.year = "+year);
			}
			
			if(sortontitle!=null && !sortontitle.equalsIgnoreCase("null")) {
				query = query.concat(" order by m.title "+sortontitle);
			}else if(sortonrating!=null && !sortonrating.equalsIgnoreCase("null")){
				//if user chooses sorting on rating
				query = query.concat(" order by r.rating "+sortonrating+", m.title");
			}else {
				//default is sort on title ascend
				query = query.concat(" order by m.title");
			}
			query = query.concat(";");
			System.out.println("query: "+query);
			// Declare our statement
			//PreparedStatement statement = dbcon.prepareStatement(query);
			Statement statement = dbcon.createStatement();
			// Perform the query
			long TJstartTime = System.nanoTime();
			   
			
			//ResultSet rs = statement.executeQuery();
			ResultSet rs = statement.executeQuery(query);  
			long TJendTime = System.nanoTime();
			long TJelapsedTime = TJendTime - TJstartTime;
			System.out.println("TJ1,"+TJelapsedTime);
			timelogj(TJelapsedTime+"");
			System.out.println("TJ2,"+TJelapsedTime);
			long endTime = System.nanoTime();
			long elapsedTime = endTime - startTime;
			System.out.println("TS,"+elapsedTime);
			timelogs(elapsedTime+"");
			System.out.println("TS,"+elapsedTime);
			
			   
			JsonArray jsonArray = new JsonArray();
			int count = 1;
			String temp_Id = "";
    		String temp_movieTitle = "";
    		String temp_year = "";
    		String temp_rating = "";
    		String temp_director = "";
    		String temp_starId = "";
    		String Genre="";
    		String Star="";
    		try {
    			// Iterate through each row of rs
    			while (rs.next()) {

    				String Id = rs.getString("m.id");
    				String Title = rs.getString("m.title");
    				String Year = rs.getString("m.year");
    				String Director = rs.getString("m.director");
    				String Genresname = rs.getString("g.name");
    				String StarId = rs.getString("s.id");
    				String Starsname = rs.getString("s.name");
    				String Rating = rs.getString("r.rating");
    				//if the movie in this row is the same movie 
    				//in the previous row with different genre or star 
    				if(temp_movieTitle.contains(Title)){
        	        	//if this row has a different genre, add it to the genre
    					if(!Genre.contains(Genresname)) {
        	        		Genre = Genre.concat(", "+Genresname);
        	        	}
    					//if this row has a different genre, add it to the genre
        	        	if(!Star.contains(Starsname)) {
        	        		Star = Star.concat(", "+Starsname);
        	        		temp_starId = temp_starId.concat(", "+StarId);
        	        	}
    				}else {
    					//if this row has a different movie, web should print the total information of the previous page
        	        	if(count==1) {
            	        	Genre = Genresname;
            	        	Star = Starsname;
            	        	temp_starId = StarId;
            	        	temp_Id = Id;
            	        	temp_movieTitle = Title;
            	        	temp_year = Year;
            	        	temp_rating = Rating;
            	        	temp_director = Director;
            	        	count ++;
            	        	continue;
        	        	}
        	        	// Create a JsonObject based on the data we retrieve from rs
        	        	//System.out.println("temp_starId: " + temp_starId);
        				JsonObject jsonObject = new JsonObject();
        				jsonObject.addProperty("movie_id", temp_Id);
        				jsonObject.addProperty("movie_title", temp_movieTitle);
        				jsonObject.addProperty("movie_year", temp_year);
        				jsonObject.addProperty("movie_director", temp_director);
        				jsonObject.addProperty("movie_genre", Genre);
        				jsonObject.addProperty("movie_star", Star);
        				jsonObject.addProperty("movie_rating", temp_rating);
        				jsonObject.addProperty("star_id", temp_starId);
        				Genre = Genresname;
        	        	Star = Starsname;
        	        	temp_starId = StarId;
        	        	temp_Id = Id;
        	        	temp_movieTitle = Title;
        	        	temp_year = Year;
        	        	temp_rating = Rating;
        	        	temp_director = Director;
        	        	count ++;
        				jsonArray.add(jsonObject);
    				
    				}
    			}
    			JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("movie_id", temp_Id);
				jsonObject.addProperty("movie_title", temp_movieTitle);
				jsonObject.addProperty("movie_year", temp_year);
				jsonObject.addProperty("movie_director", temp_director);
				jsonObject.addProperty("movie_genre", Genre);
				jsonObject.addProperty("movie_star", Star);
				jsonObject.addProperty("movie_rating", temp_rating);
				jsonObject.addProperty("star_id", temp_starId);
				jsonArray.add(jsonObject);
    			System.out.println("count: "+count);
    			//System.out.println("jsonArray: "+jsonArray.toString());
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
		} catch (Exception e) {
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
