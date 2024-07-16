package app;

import app.pojo.Company;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.*;

public class DataBase {

    private Connection connection;

    public DataBase(String name) {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + name);
            System.out.println("Opened database successfully");
        } catch (Exception e) {
        }
    }

    public void createTable() throws SQLException {
        Statement stmt = connection.createStatement();
        String sql = "CREATE TABLE COMPANY "
                + "(ID INTEGER PRIMARY KEY     NOT NULL,"
                + " NAME           TEXT    NOT NULL, "
                + " AGE            INT     NOT NULL, "
                + " ADDRESS        CHAR(50), "
                + " SALARY         REAL)";
        stmt.executeUpdate(sql);
        stmt.close();
        System.out.println("Table created successfully");
    }

    public void createData() throws SQLException {
        connection.setAutoCommit(false);
        Statement stmt = connection.createStatement();
        String sql = "INSERT INTO COMPANY (NAME,AGE,ADDRESS,SALARY) "
                + "VALUES ('Paul', 32, 'California', 20000.00 );";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO COMPANY (NAME,AGE,ADDRESS,SALARY) "
                + "VALUES ('Allen', 25, 'Texas', 15000.00 );";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO COMPANY (NAME,AGE,ADDRESS,SALARY) "
                + "VALUES ('Teddy', 23, 'Norway', 20000.00 );";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO COMPANY (NAME,AGE,ADDRESS,SALARY) "
                + "VALUES ('Mark', 25, 'Rich-Mond ', 65000.00 );";
        stmt.executeUpdate(sql);

        stmt.close();
        connection.commit();
    }    
    
    public void createData2() throws SQLException {
        connection.setAutoCommit(false);
        Statement stmt = connection.createStatement();
        String sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) "
                + "VALUES (1, 'Paul', 32, 'California', 20000.00 );";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) "
                + "VALUES (2, 'Allen', 25, 'Texas', 15000.00 );";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) "
                + "VALUES (3, 'Teddy', 23, 'Norway', 20000.00 );";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) "
                + "VALUES (4, 'Mark', 25, 'Rich-Mond ', 65000.00 );";
        stmt.executeUpdate(sql);

        stmt.close();
        connection.commit();
    }

    public int nextId(String table) throws SQLException {
        List<Integer> ids = queryAndCollect("SELECT * FROM "+table+";", rs-> {
            return new Integer(rs.getInt("id"));
        });     
 
        if (ids.isEmpty()) {
            return 1;   
        }
               
        return 1+ids.stream().sorted().mapToInt(i->i).max().getAsInt(); 
    }
    
    public void update(String sql) throws SQLException {
        connection.setAutoCommit(false);
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(sql); 
        stmt.close();
        connection.commit();
    }
    
    public void query(String query, Command com) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            com.act(rs);
        }
        rs.close();
        stmt.close();        
    }
    
    public <T> List<T> queryAndCollect(String query, Collector col) throws SQLException {
        List<T> rows = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            rows.add((T)col.collect(rs));
        }
        rs.close();
        stmt.close();        
        return rows;
    }    
    
    public List<Company> getData(String sql) throws SQLException {
        return queryAndCollect(sql, rs-> {
            return new Company(
                    rs.getInt("id"), 
                    rs.getString("name"), 
                    rs.getInt("age"), 
                    rs.getString("address"), 
                    rs.getFloat("salary"));
        });
    }   
    
    public List<Company> getData() throws SQLException {
        return queryAndCollect("SELECT * FROM COMPANY;", rs-> {
            return new Company(
                    rs.getInt("id"), 
                    rs.getString("name"), 
                    rs.getInt("age"), 
                    rs.getString("address"), 
                    rs.getFloat("salary"));
        });
    }    
    
    public void showData2() throws SQLException {
        query("SELECT * FROM COMPANY;", rs-> {
            for (int i=1; i<=rs.getMetaData().getColumnCount(); i++){
                System.out.println(rs.getMetaData().getColumnClassName(i));
                System.out.println(rs.getMetaData().getColumnTypeName(i));
                System.out.println(rs.getMetaData().getColumnName(i));
                System.out.println("--");
            }
            System.out.println("ID = " + rs.getInt("id"));
            System.out.println("NAME = " + rs.getString("name"));
            System.out.println("AGE = " + rs.getInt("age"));
            System.out.println("ADDRESS = " + rs.getString("address"));
            System.out.println("SALARY = " + rs.getFloat("salary"));
            System.out.println();            
        });
    }
}
