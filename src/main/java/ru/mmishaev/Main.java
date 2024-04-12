package ru.mmishaev;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        try(CSVWriter csvWrtr = new CSVWriter(new FileWriter("data.csv"),
                CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END)){
            csvWrtr.writeNext(new String[]{"1", "John", "Doe", "USA", "25"});
            csvWrtr.writeNext(new String[]{"2", "Ivan", "Petroff", "Russia", "35"});
        } catch(IOException exIO) {
            exIO.printStackTrace();
        }

        try(CSVReader csvRdr = new CSVReader(new FileReader("data.csv"))) {
            String[] list;
            while((list = csvRdr.readNext()) != null) {
                System.out.println("id: " + list[0]);
                System.out.println("name: " + list[1]);
                System.out.println("surname: " + list[2]);
                System.out.println("country: " + list[3]);
                System.out.println("age: " + list[4]);
            }
        } catch(Exception exIO) {
            exIO.printStackTrace();
        }
        List<Employee> lstOfEmpl = new java.util.ArrayList<Employee>();
        try(CSVReader csvReader = new CSVReader(new FileReader("data.csv"))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping("id", "firstName", "lastName", "country", "age");
            CsvToBean<Employee> ctb = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            lstOfEmpl = ctb.parse();
            lstOfEmpl.forEach(System.out::println);
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        for (Employee empl : lstOfEmpl) {
            JSONObject jo = new JSONObject();
            jo.put("id", empl.id);
            jo.put("firstName", empl.firstName);
            jo.put("lastName", empl.lastName);
            jo.put("age", empl.age);
            try(PrintWriter pw = new PrintWriter(new java.io.FileOutputStream("data.json", true))) {
                pw.write(jo.toJSONString());
                pw.flush();
            } catch(java.io.FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }

    }
}