import java.util.ArrayList;
import java.util.List;

/**
 * This User class only has the username field in this example.
 * <p>
 * However, in the real project, this User class can contain many more things,
 * for example, the user's shopping cart items.
 */
public class User {

    private final String username;
    public List<String[]> cart = new ArrayList<String[]>();
    public int cart_list_len=0;
    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }
    
    public void add_item(String movie_id, String movie_title){
    	
    	for(int i=0;i<cart_list_len;i++) {
    		if(cart.get(i)[0].contains(movie_id))
    		{
    			cart.get(i)[2] = Integer.toString(Integer.parseInt(cart.get(i)[2])+1);
    			return;
    		}
    	}
    	String[] a = new String[3];
    	a[0]=movie_id;
    	a[1]=movie_title;
    
    	a[2]="1";
    
    	cart.add(a);
    
    	cart_list_len++;
    }
    public void delete_item(String movie_id){
    	   
    	for(int i=0;i<cart_list_len;i++) {
    		if(cart.get(i)[0].contains(movie_id))
    		{
    			cart.remove(i);
    			--cart_list_len;
    			break;
    		}
    	}
    	//Iterator<String> sListIterator = list.iterator();  
    	//while(sListIterator.hasNext()){  
    	 //   String e = sListIterator.next();  
    	//    if(e.equals("3")){  
    	 //   sListIterator.remove();  
    	 //   }  
    	//}  
        }
    public void change_qty(String movie_id, String qty) {
    	for(int i=0;i<cart_list_len;i++) {
    		if(cart.get(i)[0].contains(movie_id))
    		{
    			cart.get(i)[2]=qty;
    			break;
    		}
    	}
    }
}
