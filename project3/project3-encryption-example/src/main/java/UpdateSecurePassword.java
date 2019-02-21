import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;

public class UpdateSecurePassword {

    /*
     * 
     * This program updates your existing moviedb customers table to change the
     * plain text passwords to encrypted passwords.
     * 
     * You should only run this program **once**, because this program uses the
     * existing passwords as real passwords, then replace them. If you run it more
     * than once, it will treat the encrypted passwords as real passwords and
     * generate wrong values.
     * 
     */
    public static void main(String[] args) throws Exception {

        String loginUser = "mytestuser";
        String loginPasswd = "mypassword";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
        Statement statement1 = connection.createStatement();
        Statement statement2 = connection.createStatement();

        // change the customers table password column from VARCHAR(20) to VARCHAR(128)
        String alterQuery = "ALTER TABLE customers MODIFY COLUMN password VARCHAR(128)";
        int alterResult = statement1.executeUpdate(alterQuery);
        System.out.println("altering customers table schema completed, " + alterResult + " rows affected");
        
        alterQuery = "ALTER TABLE employees MODIFY COLUMN password VARCHAR(128)";
        alterResult = statement2.executeUpdate(alterQuery);
        System.out.println("altering employees table schema completed, " + alterResult + " rows affected");

        // get the ID and password for each customer
        String query = "SELECT id, password from customers";

        ResultSet rs1 = statement1.executeQuery(query);
        
        query = "SELECT email, password from employees";
        ResultSet rs2 = statement2.executeQuery(query);

        // we use the StrongPasswordEncryptor from jasypt library (Java Simplified Encryption) 
        //  it internally use SHA-256 algorithm and 10,000 iterations to calculate the encrypted password
        PasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();

        ArrayList<String> updateQueryList = new ArrayList<>();

        System.out.println("encrypting password (this might take a while)");
        while (rs1.next()) {
            // get the ID and plain text password from current table
            String id = rs1.getString("id");
            String password = rs1.getString("password");
            
            
            // encrypt the password using StrongPasswordEncryptor
            String encryptedPassword = passwordEncryptor.encryptPassword(password);

            // generate the update query
            String updateQuery = String.format("UPDATE customers SET password='%s' WHERE id='%s';", encryptedPassword,
                    id);
            updateQueryList.add(updateQuery);
        }
        rs1.close();

        // execute the update queries to update the password
        System.out.println("updating password for customers");
        int count = 0;
        for (String updateQuery : updateQueryList) {
            int updateResult = statement1.executeUpdate(updateQuery);
            count += updateResult;
        }
        System.out.println("updating password completed, " + count + " rows affected");
        while (rs2.next()) {
            // get the ID and plain text password from current table
            String id = rs2.getString("email");
            String password = rs2.getString("password");
            System.out.println("email: "+id);
            System.out.println("password: "+password);
            
            // encrypt the password using StrongPasswordEncryptor
            String encryptedPassword = passwordEncryptor.encryptPassword(password);

            // generate the update query
            String updateQuery = String.format("UPDATE employees SET password='%s' WHERE email='%s';", encryptedPassword,
                    id);
            updateQueryList.add(updateQuery);
        }
        rs2.close();

        // execute the update queries to update the password
        System.out.println("updating password for employees");
        count = 0;
        for (String updateQuery : updateQueryList) {
            int updateResult = statement2.executeUpdate(updateQuery);
            count += updateResult;
        }
        System.out.println("updating password completed, " + count + " rows affected");
        statement1.close();
        statement2.close();
        connection.close();

        System.out.println("finished");

    }

}
