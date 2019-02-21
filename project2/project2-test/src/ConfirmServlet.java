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

import java.util.ArrayList;
import java.util.List;

import java.text.SimpleDateFormat; 
import java.util.Date; 

/**
 * Servlet implementation class ConfirmServlet
 */
@WebServlet(name = "/ConfirmServlet", urlPatterns = "/api/confirm")
public class ConfirmServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@Resource(name = "jdbc/moviedb")
	private DataSource dataSource;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ConfirmServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json"); // Response mime type
		System.out.println("Confirmation Servlet receive the message");
		PrintWriter out = response.getWriter();
		//get the user information from the session
        User user = (User) request.getSession().getAttribute("user");
        SimpleDateFormat bartDateFormat = new SimpleDateFormat
  				("yyyy/MM/dd"); 
        Date date = new Date(); 
        System.out.println(bartDateFormat.format(date));
		try {
			Connection dbcon = dataSource.getConnection();
			String query = "select customers.id from customers where customers.email='"+user.getUsername()+"';";
			System.out.println("query: "+query);
			Statement statement = dbcon.createStatement();
			ResultSet rs = statement.executeQuery(query);
			JsonArray jsonArray = new JsonArray();
			try {
				while(rs.next()) {
					String customerId = rs.getString("customers.id");
					System.out.println("id: "+customerId);
					JsonObject jsonObject = new JsonObject();
					jsonObject.addProperty("customer_id", customerId);
					jsonArray.add(jsonObject);
					out.write(jsonArray.toString());
					System.out.print("json: "+jsonObject.toString());
					System.out.println("id: "+customerId);
					System.out.println("user.cart_list_len: "+user.cart_list_len);
					for(int i=0;i<user.cart_list_len;i++) {
						System.out.println("INSERT INTO sales VALUES(null,"+customerId+",'"+user.cart.get(i)[0]+"', '"+bartDateFormat.format(date)+"');");
						query = "INSERT INTO sales VALUES(null,"+customerId+",'"+user.cart.get(i)[0]+"', '"+bartDateFormat.format(date)+"');";
						PreparedStatement statement1 = dbcon.prepareStatement(query);
						statement1.executeUpdate();
						statement1.close();
					}
					
				}
			}catch (NumberFormatException e) {
    		    e.printStackTrace();
    		}
			
			
			//System.out.println("erroraaaaaaaaaaaaa");
			rs.close();
			statement.close();
			dbcon.close();
		}catch (Exception e) {
			// write error message JSON object to output
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("errorMessage", e.getMessage());
			System.out.println("errormessage: "+e.getMessage());
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