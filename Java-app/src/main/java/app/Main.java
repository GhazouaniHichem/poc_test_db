package app;

import app.pojo.Company;
import app.pojo.CompanyDao;

public class Main {

    public static void main(String args[]) throws Exception {
        DataBase db = new DataBase("test2.db");

            //db.createTable();
            //db.createData();
        

        CompanyDao companyDao = new CompanyDao(db);
        //companyDao.findAll().forEach(s->System.out.println(s));
             
        
        Company c = new Company("Pekka", 37, "Haaga", 3560.0f);
        companyDao.create(c);
        
        companyDao.findAll().forEach(s->System.out.println(s));

    }


}
