package myapp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.lang.Double;
import java.util.ArrayList;

import java.io.File;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


@Entity
public class Dataset {
   
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private String name;
    private double min;
    private double max;
    private ArrayList<Double> values;

    public String getName() {
      return name;
    }

    public double getMin() {
      return min;
    }

    public double getMax() {
      return max;
    }

    public ArrayList<Double> getValues() {
      return values;
    }

    public void setName(String name) {
      this.name = name;
    }

    public void setMin(double min) {
      this.min = min;
    }

    public void setMax(double max) {
      this.max = max;
    }

    public void setValues(ArrayList<Double> values) {
      this.values = values;
    }

    public static <T> T json2Java(String fileName, Class<T> classType){

    	T t = null;
    	File file = new File("src/main/resources/static/datasets/"+fileName);
    	
    	 try {
    		 ObjectMapper mapper = new ObjectMapper();
    	    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			t=mapper.readValue(file, classType);
		}catch (Exception e) {
			e.printStackTrace();
		}
    	 
        return t;
    }
}
