

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
/**
 * Servlet implementation class metadata
 */
@WebServlet(name="/metadata",urlPatterns="/api/metadata")
public class MetadataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	@Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
    public MetadataServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		response.setContentType("application/json"); // Response mime type
		System.out.println("metadata servlet receive the message");
		PrintWriter out = response.getWriter();
		//ArrayList<Metadata> metadatas=new ArrayList<Metadata>();
		try {
			Connection dbcon=dataSource.getConnection();
			System.out.println("success connection");
			//String db="moviedb";
			String query1="select table_name from information_schema.tables where table_schema like ?;";
			PreparedStatement statement1 = dbcon.prepareStatement(query1);
			statement1.setString(1, "moviedb");
			ResultSet rs1=statement1.executeQuery();
			System.out.println("Debugging message" + statement1.toString());
			JsonArray jsonArray=new JsonArray();
			while(rs1.next())
			{
				//System.out.println("rs1 bukong");
				String table_name=rs1.getString("table_name");
				System.out.println(table_name);
				JsonObject responseJsonObject = new JsonObject();
				responseJsonObject.addProperty("table_name", table_name);
				String query2 = "select COLUMN_NAME,DATA_TYPE,CHARACTER_MAXIMUM_LENGTH,IS_NULLABLE from information_schema.COLUMNS where table_schema like 'moviedb' and table_name like ?;";
				PreparedStatement statement2 = dbcon.prepareStatement(query2);
				statement2.setString(1, table_name);
				ResultSet rs2=statement2.executeQuery();
				//System.out.println("Debugging message" + statement2.toString());
				String data="";
				while(rs2.next()) {
					
					data+="<tr><td>"+rs2.getString("COLUMN_NAME")+"</td>"+"<td>"+rs2.getString("DATA_TYPE")
					+"</td>"+"<td>"+rs2.getString("CHARACTER_MAXIMUM_LENGTH")+"</td>"+"<td>"+rs2.getString("IS_NULLABLE")+"</td></tr>";
					//System.out.println(data);
					
				}
				responseJsonObject.addProperty("data", data);
				jsonArray.add(responseJsonObject);
				rs2.close();
				statement2.close();
			}
			System.out.println(jsonArray.toString());
			out.write(jsonArray.toString());
			rs1.close();		
			statement1.close();	
			dbcon.close();
			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
