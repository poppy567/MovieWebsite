

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
 * Servlet implementation class AddMovie
 */
@WebServlet(name="/AddMovie",urlPatterns="/api/add_movie")
public class AddMovie extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	@Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddMovie() {
        super();
        // TODO Auto-generated constructor stub
    }
public String getID(String str) {
	String[] strs = str.split("[^0-9]");//根据不是数字的字符拆分字符串
    String numStr = strs[strs.length-1];//取出最后一组数字
    String newid;
    
    if(numStr != null && numStr.length()>0){//如果最后一组没有数字(也就是不以数字结尾)，抛NumberFormatException异常
        int n = numStr.length();//取出字符串的长度
        int num = Integer.parseInt(numStr)+1;//将该数字加一
        String added = String.valueOf(num);
        n = Math.min(n, added.length());
        //拼接字符串
        newid=str.subSequence(0, str.length()-n)+added;
        
        System.out.println(newid);
        return newid;
    }else{
        throw new NumberFormatException();
    }
	
}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		//movie_Id  movie_title  movie_year  movie_director  movie_Id  star_name  star_birthyear  genre_name 
		response.setContentType("application/json"); // Response mime type
		System.out.println("Add movie servlet receive the message");
		PrintWriter out = response.getWriter();
		
		String movie_title=request.getParameter("movie_title");
		String movie_year=request.getParameter("movie_year");
		String movie_director=request.getParameter("director");
		String star_name=request.getParameter("star_name");
		String genre_name=request.getParameter("genre");
		String star_Id=null;
		String movie_Id=null;
		String birthyear=null;
		//double rating=Math.random()*10;
		//String rating1=String.format("{0:N2}", rating);
		String rating="5.0";
		
		try {
			System.out.println("Add movie servlet receive the message2");
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
			System.out.println("Add movie servlet receive the messag3");
			//get movie id
			String query1="select id from movies where title=? and year=? and director=?";
			PreparedStatement statement1 = dbcon.prepareStatement(query1);
			statement1.setString(1, movie_title);
			statement1.setString(2, movie_year);
			statement1.setString(3, movie_director);
			ResultSet rs1=statement1.executeQuery();
			System.out.println(query1);
			if(rs1.next())
			{
				movie_Id=rs1.getString("id");
			}
			else {
				String query4="select max(id) as max_movie_id from movies;";
				PreparedStatement statement4 = dbcon.prepareStatement(query4);
				ResultSet rs4=statement4.executeQuery();
				rs4.next();
				String max_movie_Id=rs4.getString("max_movie_id");
				movie_Id=getID(max_movie_Id);
				rs4.close();
				statement4.close();
			}
			rs1.close();
			statement1.close();
			
			
			//get star id
			String query2="select id from stars where name=?";
			
			PreparedStatement statement2 = dbcon.prepareStatement(query2);
			statement2.setString(1, star_name);
			ResultSet rs2=statement2.executeQuery();
			if(rs2.next())
			{
				star_Id=rs2.getString("id");
			}
			else {
				String query3="select max(id) as max_star_id from stars;";
				PreparedStatement statement3 = dbcon.prepareStatement(query3);
				ResultSet rs3=statement3.executeQuery();
				rs3.next();
				String max_star_Id=rs3.getString("max_star_id");
				star_Id=getID(max_star_Id);
				rs3.close();
				statement3.close();
			}
			rs2.close();
			statement2.close();
			//String query6="select * from ";
			if(star_name.equals(""))
			{
				star_name="nostar";
			}
			else {
				try {
					//Connection dbcon = dataSource.getConnection();
					String query_nostar="select * from stars_in_movies as sim,stars where stars.name=? and sim.starId = stars.id and movieId=?;";
					PreparedStatement statement_nostar = dbcon.prepareStatement(query_nostar);
					statement_nostar.setString(1, "nostar");
					statement_nostar.setString(2, movie_Id);
					ResultSet rs_ns=statement_nostar.executeQuery();
					
					if(rs_ns.next())
					{
						
						String starId=rs_ns.getString("starId");
						String query_dns="delete from stars_in_movies where starId = ?";
						PreparedStatement statement_dns = dbcon.prepareStatement(query_dns);
						statement_dns.setString(1, starId);
						statement_dns.executeUpdate();
						statement_dns.close();
						//dbcon.close();
					}
				}catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(genre_name.equals(""))
			{
				genre_name="nogenre";
			}
			else {
				try {
					//Connection dbcon = dataSource.getConnection();
					String query_nog="select * from genres_in_movies as gim,genres where genres.name=? and gim.genreId = genres.id and movieId=?;";
					PreparedStatement statement_nog = dbcon.prepareStatement(query_nog);
					statement_nog.setString(1, "nogenre");
					statement_nog.setString(2, movie_Id);
					ResultSet rs_ng=statement_nog.executeQuery();
					if(rs_ng.next())
					{
						//String starId=rs_ng.getString("genreId");
						String query_dng="delete from genres_in_movies where movieId = ?";
						PreparedStatement statement_dng = dbcon.prepareStatement(query_dng);
						statement_dng.setString(1, movie_Id);
						statement_dng.executeUpdate();
						statement_dng.close();
						//dbcon.close();
					}
				}catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			String query5="CALL add_movie(?,?,?,?,?,?,?,?,?)";
			PreparedStatement statement5 = dbcon.prepareStatement(query5);
			statement5.setString(1, movie_Id);
			statement5.setString(2, movie_title);
			statement5.setString(3, movie_year);
			statement5.setString(4, movie_director);
			statement5.setString(5, star_Id);
			statement5.setString(6, star_name);
			statement5.setString(7, birthyear);
			statement5.setString(8, genre_name);
			statement5.setString(9, rating);
			System.out.println(statement5.toString());
			ResultSet rs5=statement5.executeQuery();
			System.out.println(rs5.toString());
			//System.out.println(rs5.next());
			if(rs5.next()) {
				
				String status=rs5.getString("@result_status");
				System.out.println(status);
				JsonArray jsonArray=new JsonArray();
				JsonObject responseJsonObject = new JsonObject();
				responseJsonObject.addProperty("status", status);
				jsonArray.add(responseJsonObject);
				out.write(jsonArray.toString());
				}
			rs5.close();
			statement5.close();
			dbcon.close();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
