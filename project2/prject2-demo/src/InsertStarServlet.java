

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class InsertStarServlet
 */
@WebServlet(name = "/InsertStarServlet", urlPatterns = "/api/insertstar")
public class InsertStarServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@Resource(name = "jdbc/moviedb")
	private DataSource dataSource;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InsertStarServlet() {
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
		System.out.println("insert star Servlet receive the message");
		PrintWriter out = response.getWriter();
		String name=request.getParameter("starname");
		String birthyear;
		if(request.getParameter("birthday").equals("")) {
			birthyear=null;
		}
		else {
			birthyear=request.getParameter("birthday");
		}
		//get the user information from the session
        //User user = (User) request.getSession().getAttribute("user");
		try {
			Context initCtx = new InitialContext();

            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            if (envCtx == null)
                out.println("envCtx is NULL");

            // Look up our data source
            DataSource ds = (DataSource) envCtx.lookup("jdbc/WriteDB");
            
            if (ds == null)
                out.println("ds is null.");

            Connection dbcon = ds.getConnection();
            if (dbcon == null)
                out.println("dbcon is null.");
			System.out.println("success connection");
			System.out.println("success connection");
			String query1="select max(id) as max_id from stars;";
			Statement statement = dbcon.createStatement();
			ResultSet rs = statement.executeQuery(query1);
			System.out.println(rs);
			rs.next();
			String maxId=rs.getString("max_id");
			System.out.println(maxId);
			String[] strs = maxId.split("[^0-9]");//根据不是数字的字符拆分字符串
		    String numStr = strs[strs.length-1];//取出最后一组数字
		    String starId;
		    if(numStr != null && numStr.length()>0){//如果最后一组没有数字(也就是不以数字结尾)，抛NumberFormatException异常
		        int n = numStr.length();//取出字符串的长度
		        int num = Integer.parseInt(numStr)+1;//将该数字加一
		        String added = String.valueOf(num);
		        n = Math.min(n, added.length());
		        //拼接字符串
		        starId=maxId.subSequence(0, maxId.length()-n)+added;
		        System.out.println(starId);
		    }else{
		        throw new NumberFormatException();
		    }
			JsonArray jsonArray = new JsonArray();
			String query2="insert into stars values(?,?,?);";
			PreparedStatement statement2 = dbcon.prepareStatement(query2);
			//PreparedStatement statement = dbcon.prepareStatement(query);
			
			statement2.setString(1,starId);
			statement2.setString(2,name);
			statement2.setString(3,birthyear);
			
			System.out.println("Debugging message" + statement2.toString());
			statement2.executeUpdate();
			//System.out.println("Debugging message " + rs2.next());
			
			
			String query3="select * from stars where id=?;";
			PreparedStatement statement3 = dbcon.prepareStatement(query3);
			statement3.setString(1,starId);
			System.out.println("Debugging message" + statement3.toString());
			ResultSet rs3 = statement3.executeQuery();
			if(rs3.next()) {
				JsonObject responseJsonObject = new JsonObject(); 
				responseJsonObject.addProperty("status", "success"); 
				responseJsonObject.addProperty("message", "success");
				responseJsonObject.addProperty("starname", name); 
				responseJsonObject.addProperty("birthday", birthyear);
				jsonArray.add(responseJsonObject);
				out.write(jsonArray.toString());
			}
			else {
				JsonObject responseJsonObject = new JsonObject(); 
				responseJsonObject.addProperty("status", "fail"); 
				responseJsonObject.addProperty("message", "fail to insert");
				jsonArray.add(responseJsonObject);
				out.write(jsonArray.toString());
			}
			response.setStatus(200);

			rs.close();
			//rs2.close();
			rs3.close();
			statement.close();
			statement2.close();
			statement3.close();
			dbcon.close();
       
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
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
