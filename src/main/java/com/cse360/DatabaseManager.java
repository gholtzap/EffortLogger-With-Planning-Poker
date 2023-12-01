package com.cse360;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DatabaseManager {

    static final String URL = "jdbc:mysql://localhost:3306/PLANNINGPOKER";
    static final String USERNAME = "root";
    static final String PASSWORD = "hello@123";

    private static Connection connection;

    private static boolean connectToDatabase(){

        try 
        {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("<------Connected to Database successfully------>");
            
            return true;
        } catch (SQLException e){
            
            System.out.println(e);
        }

        return false;
    }

    private static boolean emptyTables(){

        String query1 = "CREATE TABLE RATINGS1 (user varchar(255), rating varchar(20))";
        String query2 = "CREATE TABLE RATINGS2 (user varchar(255), rating varchar(20))";


        try (PreparedStatement statement1 = connection.prepareStatement(query1); PreparedStatement statement2 = connection.prepareStatement(query2) ){
            statement1.executeUpdate();
            statement2.executeUpdate();

            return true;

        } catch ( SQLException e ){
            System.out.println(e);
        }

        return false;
    }

    private static void runCommand(){

        String query = "CREATE TABLE RATINGS (user varchar(100), agenda varchar(999), rating varchar(10))";

        try (PreparedStatement statement = connection.prepareStatement(query)){
            statement.executeUpdate();

        } catch ( SQLException e ){
            System.out.println(e);
        }
    }

    public static void main(String[] args) throws Exception {

        connectToDatabase();

        emptyTables();

       
    }
    
}
