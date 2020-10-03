package mason.com.spring;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;


public class ReadCSVintoSQLite {
    public static void main(String[] args) throws SQLException {

        //database connection
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:shop.db");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }        System.out.println("Opened database successfully");

        //Create table
        stmt = c.createStatement();
        String sql = "CREATE TABLE Shop " +
                "(Name        CHAR(500)," +
                " Area        CHAR(500), " +
                " Address        CHAR(500), " +
                " PhoneNum        CHAR(500), " +
                " ID INT PRIMARY KEY     NOT NULL," +
                " Type        CHAR(500))";
        stmt.executeUpdate(sql);


        //CSV code
        String csvFile = "shop.csv";
        String line = "";
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {

                String[] shop = line.split(cvsSplitBy);

                PreparedStatement insert_sql = c.prepareStatement("INSERT INTO Shop (Name, Area, Address, PhoneNum, ID, Type) values (?,?,?,?,?,? );");

                insert_sql.setString(1,shop[0]);
                insert_sql.setString(2,shop[1]);
                insert_sql.setString(3,shop[2]);
                insert_sql.setString(4,shop[3]);
                insert_sql.setString(5,shop[4]);
                insert_sql.setString(6,shop[5]);

                insert_sql.executeUpdate();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        ResultSet rs = stmt.executeQuery( "SELECT * FROM Shop;" );
        while ( rs.next() ) {
            String  Name = rs.getString("Name");
            String  Area = rs.getString("Area");
            String  Address = rs.getString("Address");
            String  PhoneNum = rs.getString("PhoneNum");
            String  ID = rs.getString("ID");
            String  Type = rs.getString("Type");
            System.out.println( "Name = " + Name );
            System.out.println( "Area = " + Area );
            System.out.println( "Address = " + Address );
            System.out.println( "PhoneNum = " + PhoneNum );
            System.out.println( "ID = " + ID );
            System.out.println( "Type = " + Type );
            System.out.println();
        }
    }
}
