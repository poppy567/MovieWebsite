
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Collection;

import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;



public class ParseXML {
	
	@Resource(name = "jdbc/moviedb")
	private DataSource dataSource;
	
	HashMap<String, Movie> moviesmap;
	HashMap<String, Star> starsmap;
	HashMap<String, ArrayList<String>> stars_in_movies;
	HashMap<String, ArrayList<String>> genres_in_movies;
	ArrayList<String> stars;
	ArrayList<String> genres;
	
	//List<Genre> genres;
    Document dom1;
    Document dom2;
    Document dom3;
    
    public ParseXML() {
    	moviesmap = new HashMap<String, Movie>();
    	starsmap = new HashMap<String, Star>();
    	stars_in_movies = new HashMap<String, ArrayList<String>>();
    	genres_in_movies = new HashMap<String, ArrayList<String>>();
    	//genres = new ArrayList<>();
    }
    
    public void runExample() {

        //parse the xml file and get the dom object
        parseXmlFile();

        //get each employee element and create a Employee object
        parseDocument();
        
        /*System.out.println("movie size:"+moviesmap.size());
        System.out.println("stars size:"+starsmap.size());
        System.out.println("stars_in_movies size:"+stars_in_movies.size());
        System.out.println("genres_in_movies size:"+genres_in_movies.size());
        System.out.println("Diahnne Abbott's birthyear: "+starsmap.get("Diahnne Abbott").getBirthyear());*/

    }
    
    private void parseXmlFile() {
        //get the factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {

            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            //parse using builder to get DOM representation of the XML file
            dom1 = db.parse("/Users/apple/eclipse-workspace/test_project/stanford-movies/mains243.xml");
            dom2 = db.parse("/Users/apple/eclipse-workspace/test_project/stanford-movies/casts124.xml");
            dom3 = db.parse("/Users/apple/eclipse-workspace/test_project/stanford-movies/actors63.xml");
            System.out.println("parse XML success");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        
        
    }
    
    private void parseDocument() {
        //get the root elememt
        Element moviesEle = dom1.getDocumentElement();
        Element starsEle = dom3.getDocumentElement();
        Element sinmEle = dom2.getDocumentElement();

        //get a nodelist of <directorfilms> elements
        NodeList nl = moviesEle.getElementsByTagName("directorfilms");
        if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {

                //get the <directorfilms> element
                Element el = (Element) nl.item(i);

                //convert all the film elements into Movie and add them to the moviesmap for each films element
                getDirectorFilms(el);

            }
        }
        NodeList nl3 = starsEle.getElementsByTagName("actor");
        if (nl3 != null && nl3.getLength() > 0) {
            for (int i = 0; i < nl3.getLength(); i++) {

                //get the actor element
                Element starel = (Element) nl3.item(i);
                String name = getTextValue(starel,"stagename");
                //System.out.println("starname: "+name);
                String birthyear = getTextValue(starel,"dob");
                Star s = new Star();
                if(name != null && !name.equalsIgnoreCase("null")) {
                	s.setName(name);
                }
                if(birthyear != null && !birthyear.equalsIgnoreCase("null")) {
                	try {
                		s.setBirthyear(Integer.parseInt(birthyear));
                	}catch(NumberFormatException e) {
                		System.out.println("Error: Star \""+name+"\"'s birthyear has a wrong format");
                	}
                }
                //name is the key
                starsmap.put(name, s);
            }
        }
        
      //get a nodelist of <filmc> elements, each filmc element stands for a movie
        NodeList nl2 = sinmEle.getElementsByTagName("filmc");
        
        //for each filmc element, if there is no available star, the hashmap won't add it
        if (nl2 != null && nl2.getLength() > 0) {
        	for (int i = 0; i < nl2.getLength(); i++) {
        		//for each <filmc> element, create a movieid to store the movie's id
        		//and a stars to store the list of stars in this movie
        		String movieid = null;
            	stars = new ArrayList<String>();
            	
            	//get <filmc> element
            	Element fimcEl = (Element) nl2.item(i);
            	movieid = getTextValue(fimcEl,"f");
            	//get a nodelist of <m> elements
            	NodeList nlm = fimcEl.getElementsByTagName("m");
            	if(nlm != null && nlm.getLength() > 0) {
            		for (int j = 0; j < nlm.getLength(); j++) {
            			//get <m> element
            			Element mEle = (Element) nlm.item(j);
                    	String starname = getTextValue(mEle,"a");
                    	//System.out.println("starname: "+starname);
                    	//if there is a starname in <m> tag
                    	if(starname != null && !starname.equalsIgnoreCase("null")) {
                    		stars.add(starname);
                    	}
            		}
            	}
            	if(stars.isEmpty()) {
                	System.out.println("Error: movie with id \""+movieid+"\" has no star. Thus the program will add a nostar for this movie");
                	stars.add("nostar");
                }
            	stars_in_movies.put(movieid, stars);
            }
            
            
        }
        
    }
    
    private void getDirectorFilms(Element el) {
    	String director = getTextValue(el, "dirname");
    	// get a nodelist of <film> element
    	NodeList nl = el.getElementsByTagName("film");
    	
    	if (nl != null && nl.getLength() > 0) {
    		for (int i = 0; i < nl.getLength(); i++) {
    			//get the <film> element
                Element movieEle = (Element) nl.item(i);
                String id = getTextValue(movieEle,"fid");
                String title = getTextValue(movieEle,"t");
                if(id==null||id.equalsIgnoreCase("null")) {
                	System.out.println("Error: Movie \""+title+"\" doesn't have a id");
                	continue;
                }
                String year = getTextValue(movieEle,"year");
                Movie m = new Movie();
                if(title==null) {
                	title = "notitle";
                }
                if(director==null) {
                	director = "nodirector";
                }
                m.setDirector(director);
                m.setTitle(title);
                if(year==null ||  year.equalsIgnoreCase("null")) {
                }
                else {
                	try {
                		m.setYear(Integer.parseInt(year));
                	}catch(NumberFormatException e) {
                		System.out.println("Movie \""+title+ "\"'s year has a wrong format");
                	}
                }

                // get a nodelist of <cat> element
                NodeList genrelist = movieEle.getElementsByTagName("cat");
                genres = new ArrayList<String>();
                if(genrelist != null && genrelist.getLength() > 0) {
                	for (int j = 0; j < genrelist.getLength(); j++) {
                		//System.out.println("title:"+title);
                		//get the <cat> element
                		Element genreEl = (Element) genrelist.item(j);
                		//System.out.println("genreEl: "+genreEl.toString());
                		
                		String genrename = null;
                		if(genreEl.hasChildNodes()) {
                            genrename = el.getFirstChild().getNodeValue();
                        }
                        else {
                        	System.out.println("Error: no content in tag <cat>");
                        }
                		//System.out.println("genre:"+genrename);
                		//if the genrename is not null and "", add it to the genres list
                		if(genrename!=null && !genrename.equalsIgnoreCase("null")) {
                			genres.add(genrename);
                		}
                	}
                }
                if(genres.isEmpty()) {
                	System.out.println("Error: movie \""+title+"\" doesn't have a genre. Thus the program will add a nogenre for this movie");
                	genres.add("nogenre");
                }
                //System.out.println("id: "+id+"\ngenres:"+genres.toString());
                genres_in_movies.put(id, genres);
                //add the movie to the moviesmap
                moviesmap.put(id, m);
    		}
    	}
    }
    
    private String getTextValue(Element ele, String tagName) {
        String textVal = null;
        NodeList nl = ele.getElementsByTagName(tagName);
        if (nl != null && nl.getLength() > 0) {
            Element el = (Element) nl.item(0);
            if(el.hasChildNodes()) {
                textVal = el.getFirstChild().getNodeValue();
            }
        }
        else {
        	System.out.println("no content in "+tagName);
        }

        return textVal;
    }
    
    
    
    /**
     * This function is used to insert new movies, stars and genres in the database.
     * Meanwhile, it should also update the stars_in_movies and genres_in_movies table while inserting
     * 
     * For inserting the new movie, the program should first consider whether the movie already exists in the database
     * if the title, year and director is all the same with an old movie in the database, the program should not insert this movie into database
     * but the program should consider what if there are new star records related to this old movie, if this happens, the program should insert new star records
     * and update the stars_in_movies. Same for the genre records.
     * 
     * If this is a new movie record, then the program will generate a random rating for this movie. Judge whether the star and genre is new in database and do the same insert
     * procedure.
     */
    public void InsertData() throws InstantiationException, IllegalAccessException, ClassNotFoundException {

        
        
        //go through the whole movies list
        Iterator<Entry<String, Movie>> iter = moviesmap.entrySet().iterator();
        try {
        	Connection conn = null;
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String jdbcURL="jdbc:mysql://localhost:3306/moviedb";
            conn = DriverManager.getConnection(jdbcURL,"mytestuser", "mypassword");
            
        	while(iter.hasNext()) {
        		String movie_Id = null;
            	Map.Entry<String, Movie> entry = (Map.Entry<String, Movie>)iter.next();
            	//Judge if this movie already exists in database
            	String query1="select id from movies where title=? and year=? and director=?";
    			PreparedStatement statement1 = conn.prepareStatement(query1);
    			statement1.setString(1, entry.getValue().getTitle());
    			statement1.setString(2, String.valueOf(entry.getValue().getYear()));
    			statement1.setString(3, entry.getValue().getDirector());
    			ResultSet rs1=statement1.executeQuery();
    			
    			if(rs1.next())
    			{
    				//if this movie already exist in database
    				//get its id and print the report information
    				movie_Id=rs1.getString("id");
    				System.out.println("movie \""+movie_Id+"\" already exists");
    			}
    			else {
    				//if this movie doesn't exist
    				//get the max id in the database and plus one to be the id for this new movie
    				String query4="select max(id) as max_movie_id from movies;";
    				PreparedStatement statement4 = conn.prepareStatement(query4);
    				ResultSet rs4=statement4.executeQuery();
    				rs4.next();
    				String max_movie_Id=rs4.getString("max_movie_id");
    				movie_Id=getID(max_movie_Id);
    				rs4.close();
    				statement4.close();
    			}
    			//System.out.println("movie \""+movie_Id+"\"");
    			//System.out.println("movie title"+entry.getValue().getTitle());
    			
            	//get all the star this movie has
            	stars = stars_in_movies.get(entry.getKey());
            	if(stars == null) {
            		System.out.println("This movie does not appear in cast.xml");
            		continue;
            	}
            	//go through the star list
            	String query2="select id from stars where name=?";
            	PreparedStatement statement2 = conn.prepareStatement(query2);
            	ArrayList<String> Star_Ids = new ArrayList<String>();
            	ArrayList<String> Star_years = new ArrayList<String>();
            	int count = 1;
            	//System.out.println("star: "+stars);
            	for(int i = 0; i<stars.size(); i++) {
            		//Judge whether this star is a new star or not
            		String star_Id = null;
            		statement2.setString(1, stars.get(i));
        			ResultSet rs2=statement2.executeQuery();
        			if(rs2.next())
        			{
        				//if this star already exist in database
        				star_Id=rs2.getString("id");
        			}
        			else {
        				
        				//set a new Id for the new Star
        				String query3="select max(id) as max_star_id from stars;";
        				PreparedStatement statement3 = conn.prepareStatement(query3);
        				ResultSet rs3=statement3.executeQuery();
        				rs3.next();
        				String max_star_Id=rs3.getString("max_star_id");
        				for(int j = 0; j<count; j++) {
        					star_Id=getID(max_star_Id);
        					max_star_Id=star_Id;
        				}
        				count++;
        				rs3.close();
        				statement3.close();
        			}
        			String birthyear = null;
        			//System.out.println("starname: "+stars.get(i));
        			if(starsmap.get(stars.get(i))==null) {
        				//means there is no such star in starsmap
        				birthyear=null;
        				System.out.println("There is no star in casts.xml for star "+ stars.get(i));
        			}
        			else{
        				//System.out.println("birthyear: "+starsmap.get(stars.get(i)).getBirthyear());
        				if(starsmap.get(stars.get(i)).getBirthyear()!=0) {
            				//means this star has a birthyear
            				birthyear = String.valueOf(starsmap.get(stars.get(i)).getBirthyear());
            			}
        			}
        			
        			Star_Ids.add(star_Id);
        			Star_years.add(birthyear);
            	}
            	statement2.close();
            	
            	//get all the genres this movies has
            	//genre's id  and genres_in_movies table's update will be processed in the stored procedure
            	genres = genres_in_movies.get(entry.getKey());
            	
            	/**
            	 * Now, the program has the movie id, star list which has star Id for each star 
            	 * and genre list for this movie
            	 * For using the procedure, the program needs to get movie's id, title, year, director
            	 * star's id and name, genre's name, and generating a random rating
            	 */
            	conn.setAutoCommit(false);
            	String query5="CALL add_movie(?,?,?,?,?,?,?,?,?)";
    			PreparedStatement statement5 = conn.prepareStatement(query5);
    			int[] iNoRows=null;
    			
    			for(int i = 0; i<stars.size(); i++) {
    				//System.out.println("star id: "+Star_Ids.get(i));
    				//System.out.println("star name: "+stars.get(i));
    				statement5.setString(1, movie_Id);
        			statement5.setString(2, entry.getValue().getTitle());
        			statement5.setString(3, String.valueOf(entry.getValue().getYear()));
        			statement5.setString(4, entry.getValue().getDirector());
        			statement5.setString(5, Star_Ids.get(i));
        			statement5.setString(6, stars.get(i));
        			statement5.setString(7, Star_years.get(i));
        			statement5.setString(8, genres.get(0));
        			statement5.setString(9, "5.0");
        			statement5.addBatch();
    			}
    			for(int i = 0; i<genres.size(); i++) {
    				statement5.setString(1, movie_Id);
        			statement5.setString(2, entry.getValue().getTitle());
        			statement5.setString(3, String.valueOf(entry.getValue().getYear()));
        			statement5.setString(4, entry.getValue().getDirector());
        			statement5.setString(5, Star_Ids.get(0));
        			statement5.setString(6, stars.get(0));
        			statement5.setString(7, Star_years.get(0));
        			statement5.setString(8, genres.get(i));
        			statement5.setString(9, "5.0");
        			statement5.addBatch();
    			}
    			iNoRows = statement5.executeBatch();
    			conn.commit();
    			statement5.close();
    			
            }
        	conn.close();
        }catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        
    }
    
    public String getID(String str) {
    	String[] strs = str.split("[^0-9]");//根据不是数字的字符拆分字符串
        String numStr = strs[strs.length-1];//取出最后一组数字
        String starId;
        
        if(numStr != null && numStr.length()>0){//如果最后一组没有数字(也就是不以数字结尾)，抛NumberFormatException异常
            int n = numStr.length();//取出字符串的长度
            int num = Integer.parseInt(numStr)+1;//将该数字加一
            String added = String.valueOf(num);
            n = Math.min(n, added.length());
            //拼接字符串
            starId=str.subSequence(0, str.length()-n)+added;
           
            return starId;
        }else{
            throw new NumberFormatException();
        }
    	
    }
    public static void main(String[] args) {
        //create an instance
    	ParseXML dpe = new ParseXML();

        //call run example
        dpe.runExample();
        
        try{
        	dpe.InsertData();
        }catch (InstantiationException e1) {
        	e1.printStackTrace();
        }catch(IllegalAccessException e2) {
        	e2.printStackTrace();
        }catch(ClassNotFoundException e3) {
        	e3.printStackTrace();
        }
    }
}
