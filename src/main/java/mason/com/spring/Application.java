package mason.com.spring;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


@SpringBootApplication
@RestController
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @GetMapping("/area_type")
    public String area_type(@RequestParam(value = "area") String area) {
        boolean onlyType = true;
        String SQL = String.format("select type from shop where Area = '%s' group by type",area);
        return json(SQL, onlyType);
    }

    @GetMapping("/shop")
    public String shop(@RequestParam(value = "area") String area,@RequestParam(value = "type") String type) {
        boolean onlyType = false;
        String SQL = String.format("select * from shop where Area = '%s' and type = '%s'",area,type);
        return json(SQL, onlyType);
    }

    public String json(String SQL, boolean onlyType) {
        //jason sting init
        String jsonString = "[";
        //db connection
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:shop.db");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
        //

        //select sql execution & insert into string json
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            if (onlyType) {
                while (rs.next()) {
                    String Type = rs.getString("Type");
                    jsonString += String.format(Type+",");
                }
            } else {
                while (rs.next()) {
                    String Name = rs.getString("Name");
                    String Area = rs.getString("Area");
                    String Address = rs.getString("Address");
                    String PhoneNum = rs.getString("PhoneNum");
                    String ID = rs.getString("ID");
                    String Type = rs.getString("Type");

                    jsonString += String.format("{\"name\": \"%s\", \"area\": \"%s\" , \"address\":\"%s\" , \"Tel_num\":\"%s\" , \"ID\":\"%s\" ,\"type\":\"%s\"},", Name, Area, Address, PhoneNum, ID, Type);

                }
            }
            c.close();
            stmt.close();
            jsonString = jsonString.substring(0, jsonString.length() - 1);
            jsonString += "]";
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }


        if (onlyType) {
            Gson jsongson = new Gson();
            jsongson.toJson(jsonString);
        }
        else{
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            Gson gson = builder.create();
            YellowShop[] shop = gson.fromJson(jsonString, YellowShop[].class);
            jsonString = gson.toJson(shop);
        }

        return jsonString;
    }

}


class YellowShop {
    String name;
    String area;
    String address;
    String Tel_num;
    String ID;
    String type;
}

