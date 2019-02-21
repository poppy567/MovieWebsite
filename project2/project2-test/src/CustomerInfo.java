import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class CustomerInfo
 */
@WebServlet(name="/CustomerInfo",urlPatterns="/api/customerinfor")
public class CustomerInfo extends HttpServlet {
	
	@Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CustomerInfo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		response.setContentType("application/json"); // Response mime type
    	PrintWriter out = response.getWriter();
    	
    	String id=request.getParameter("id");
    	String first_name=request.getParameter("first_name");
    	String last_name=request.getParameter("last_name");
    	String expiration=request.getParameter("expiration");
    	try {
    		// Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();
            
            String query="select firstName, lastName, id, expiration from creditcards where id=? and firstName=? and lastName=? and expiration=?";
            PreparedStatement statement = dbcon.prepareStatement(query);
            // Perform the query
            //ResultSet rs = statement.executeQuery(query,param);
         	
         	statement.setString(2,first_name);
         	statement.setString(3,last_name);
         	statement.setString(1,id);
         	statement.setString(4,expiration);
         	
         	System.out.println("Debugging message" + statement.toString());
         	ResultSet rs=statement.executeQuery();
         	//System.out.println("Debugging message " + rs.next());
         	JsonArray jsonArray = new JsonArray();
         	try {
         		if(rs.next())
    			{
    				//request.getSession().setAttribute("user", new User(email));
    				
    				JsonObject responseJsonObject = new JsonObject(); 
    				//responseJsonObject.addProperty("test", "test "+ email);
    				responseJsonObject.addProperty("status", "success"); 
    				responseJsonObject.addProperty("message", "success");
    				jsonArray.add(responseJsonObject);
    				out.write(jsonArray.toString());
    				System.out.println("response: " + responseJsonObject.toString());
    			}
    		else{
    				JsonObject responseJsonObject = new JsonObject();    				
    				responseJsonObject.addProperty("status", "fail");
    				jsonArray.add(responseJsonObject);
    				out.write(jsonArray.toString());
    				System.out.println("response: " + jsonArray.toString());
    		}
         	}catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
         	response.setStatus(200);

			rs.close();
			statement.close();
			dbcon.close();
    	}catch (Exception e) {
    	
        	
			// write error message JSON object to output
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("errorMessage", e.getMessage());
			out.write(jsonObject.toString());

			// set reponse status to 500 (Internal Server Error)
			response.setStatus(500);
    	}
	}

}
