import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Test {

 public static void readTJ() {  
        File file = new File("/Users/apple/Desktop/timelogTJ.txt");  
        BufferedReader reader = null;  
        try {  
            reader = new BufferedReader(new FileReader(file));  
            String tempString = null;  
            int line = 0;  
            long sum=0;
            while ((tempString = reader.readLine()) != null) {  
                sum+=Integer.parseInt(tempString);
                line++;  
            } 
            sum=sum/line;
            System.out.println("TJ Average is "+sum);
            reader.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (reader != null) {  
                try {  
                    reader.close();  
                } catch (IOException e1) {  
                }  
            }  
        }  
    }  
 public static void readTS() {  
        File file = new File("/Users/apple/Desktop/timelogTS.txt");  
        BufferedReader reader = null;  
        try {  
            reader = new BufferedReader(new FileReader(file));  
            String tempString = null;  
            int line = 0; 
            long sum=0;
            while ((tempString = reader.readLine()) != null) {  
                sum+=Integer.parseInt(tempString);
                line++;  
            } 
            sum=sum/line;
            System.out.println("TS Average is "+sum);
            reader.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (reader != null) {  
                try {  
                    reader.close();  
                } catch (IOException e1) {  
                }  
            }  
        }  
    }
 
 public static void main(String[] args) {
  // TODO Auto-generated method stub
  Test t=new Test();
  t.readTJ();
  t.readTS();
 }

}