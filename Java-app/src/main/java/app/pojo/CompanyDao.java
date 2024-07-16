package app.pojo;

import app.DataBase;
import java.sql.SQLException;
import java.util.List;

public class CompanyDao {
    private DataBase db;

    public CompanyDao(DataBase db) {
        this.db = db;
    }
   
    public void create(Company company) throws SQLException {
        String sql = "INSERT INTO COMPANY "
                + "(NAME,AGE,ADDRESS,SALARY) VALUES ("                
                + s(company.name) + ", "
                + company.age + ", "
                + s(company.address) + ", "
                + company.salary + " );"; 
        
        db.update(sql);
    }     
   
    private String s(String s){
        return "'"+s+"'";
    }
    
    public List<Company> findAll() throws SQLException {
        return db.queryAndCollect("SELECT * FROM COMPANY;", rs-> {
            return new Company(
                    rs.getInt("id"), 
                    rs.getString("name"), 
                    rs.getInt("age"), 
                    rs.getString("address"), 
                    rs.getFloat("salary"));
        });
    }   
    
    public Company find(int id) throws SQLException {
        List<Company> matches = db.queryAndCollect("SELECT * FROM COMPANY WHERE ID="+id, rs-> {
            return new Company(
                    rs.getInt("id"), 
                    rs.getString("name"), 
                    rs.getInt("age"), 
                    rs.getString("address"), 
                    rs.getFloat("salary"));
        });
        
        if ( matches.isEmpty() ) {
            return null;
        }
        
        return matches.get(0);
    }     
}
