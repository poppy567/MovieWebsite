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
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ShoppingCartServlet
 */
@WebServlet(name = "/ShoppingCartServlet", urlPatterns = "/api/shoppingcart")
public class ShoppingCartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	@Resource(name = "jdbc/moviedb")
	private DataSource dataSource;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShoppingCartServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json"); // Response mime type
		PrintWriter out = response.getWriter();
		
		/**
		 * There are four situation that the frontend will send a request to the servlet
		 * 1. Customer click the "add to cart" button either on single-movie or movie-page this
		 * 		On this situation, the content js sends would be 							id and title
		 * 2. Customer write the quantity on the shopping cart page
		 * 		On this situation, the content would be 									id, title, qty and showcart
		 * 3. Customer click the delete button on the shopping cart page
		 * 		On this situation, the content would be 									id, delete and showcart
		 * 4. Customer go to the shopping cart page and we need to show the cart content
		 * 		On this situation, the content would be 									showcart 
		 **/
		String movieid = request.getParameter("id");
        String movietitle = request.getParameter("title");
        String movieqty = request.getParameter("qty");
        //This means we need to show the content of user's cart to js file
        String showcart = request.getParameter("showcart");
        //This means we need to delete the item from the cart
        String deletemovie = request.getParameter("delete");
        System.out.println("id: "+movieid);
        System.out.println("title: "+movietitle);
        System.out.println("qty: "+movieqty);
        System.out.println("showcart: "+showcart);
        //get the user information from the session
        User user = (User) request.getSession().getAttribute("user");
        synchronized(user) {
        	if(movieid != null && movieid.length() != 0) {
        		if(movieqty != null && movieqty.length() != 0) {
            		//user want to add the movie according to the quantity he chooses on the cart page
        			
        			user.change_qty(movieid, movieqty);
        			
        		}else {
        			if(deletemovie != null && deletemovie.length() != 0) {
        				//if user wants to delete a movie he chooses
        				user.delete_item(movieid);
        			}else {
        				//user click the cart button
                    	//this means user want to add one more movie into the cart
                    	user.add_item(movieid, movietitle);
        			}
        		}
        	}
        }
        if(showcart != null && showcart.length() != 0) {
        	//if we need to send the content back
        	JsonArray jsonArray = new JsonArray();
        	for(int i = 0; i<user.cart_list_len;i++) {
        		JsonObject jsonObject = new JsonObject();
        		jsonObject.addProperty("movie_id", user.cart.get(i)[0]);
        		jsonObject.addProperty("movie_title", user.cart.get(i)[1]);
        		jsonObject.addProperty("movie_qty", user.cart.get(i)[2]);
            	
            	jsonArray.add(jsonObject);
            }
        	out.write(jsonArray.toString());
        }
        request.getSession().setAttribute("user",user);
        for(int i = 0; i<user.cart_list_len;i++) {
        	System.out.println((i+1)+"th item in user cart: "+user.cart.get(i)[1]+user.cart.get(i)[2]);
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
