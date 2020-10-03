package mason.com.spring;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


//("C:\\Users\\MasonHSLAI\\Desktop\\java_project\\mason.com.spring\\shoplist.xlsx")
//"..\\resources\\shop.csv"
public class ReadCSV {
    public static void main(String[] args) {

        String csvFile = "shop.csv";
        String line = "";
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {

                String[] shop = line.split(cvsSplitBy);

                System.out.println("Country [name= " + shop[0] + " , area=" + shop[1] + " , address=" + shop[2] + " , Tel.=" + shop[3] + " , Type=" + shop[4] + "]");

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
