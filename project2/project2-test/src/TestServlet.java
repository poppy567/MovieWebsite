import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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
 * Servlet implementation class TestServlet
 */
@WebServlet(name = "TestServlet", urlPatterns = "/api/main")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Resource(name = "jdbc/moviedb")
	private DataSource dataSource;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    //process general search and advanced search request 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String title = request.getParameter("title");
        String year = request.getParameter("year");
        String director = request.getParameter("director");
        String star = request.getParameter("star");

        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        if(title != null && title.length() != 0) {
        	 title=title.concat("%");
        	 jsonObject.addProperty("movie_title", title);
        }
        if(year != null && year.length() != 0) {
        	jsonObject.addProperty("movie_year", year);
        }
        if(director != null && director.length() != 0) {
        	director=director.concat("%");
        	jsonObject.addProperty("movie_director", director);
        }
        if(star != null && star.length() != 0) {
        	star=star.concat("%");
        	jsonObject.addProperty("movie_star", star);
        }
        jsonArray.add(jsonObject);
     // write JSON string to output
        out.write(jsonArray.toString());
        // set response status to 200 (OK)
        response.setStatus(200);
        System.out.println("Title: "+title);
        out.close();
    }
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
