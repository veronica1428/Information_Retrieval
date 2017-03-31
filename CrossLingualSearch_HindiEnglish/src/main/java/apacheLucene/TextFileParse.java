package apacheLucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TextFileParse {

	public static void main(String[] args) {
		System.out.println("Inside TextFileParse main method");
		
		try{
			String path = "//Users//veronica//Documents//study//information retrieval//project//DataBase//shabdanjali.utf8";
			//parseTextFile(path);
			make_dict("eng-hind-translation.txt");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void parseTextFile(String filePath) throws IOException{
		
		BufferedReader br = null;
		String line = "";
		File file = null;
		try{
			br = new BufferedReader(new FileReader(filePath));
			file = new File("eng-hind-translation.txt");
			FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
			
			while((line = br.readLine())!=null){
				write_to_file(fileWriter, line);
			}
		}catch(IOException e){
			System.out.println("Exception during database parsing" + e.getMessage());
		}finally{
			br.close();
		}
	}
	
	public static void write_to_file(FileWriter fileWriter ,String line) throws IOException{
		
		try{
			if (line.startsWith("\"")){
				System.out.println("line :" + line);
				fileWriter.append(line + "\n");
			}
		}catch(Exception e){
			System.out.println("Error while writing to file: " + e.getMessage());
		}finally{
			fileWriter.close();
		}
	}
	
	public static void make_dict(String fileLocation) throws IOException{
		HashMap<String, ArrayList<String>> hind_eng_dict = new HashMap<String, ArrayList<String>>();
		BufferedReader br = null;
		ArrayList<String> value = new ArrayList<String>();
		String line = "";
		 
		try{
			br = new BufferedReader(new FileReader(fileLocation));
			while((line=br.readLine())!=null){
				String[] line_entry = line.split(",");
				
				/*if (hind_eng_dict.containsKey(line_entry[2])){
					  
				}else{
					value.add(line_entry[0]);
					hind_eng_dict.put(line_entry[2], value.add(line_entry[0]));
				}*/
			    line_entry[2] = removeNumeric(line_entry[2]);
				System.out.println("line entry[2]: " + line_entry[2]);
			}
			
		}catch(IOException e){
			System.out.println("Error while making hindi_english_dictionary" + e.getMessage());
		}finally{
			br.close();
		}
	}
	
	public static String removeNumeric(String value){
		
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<value.length();i++){
			if ((i==0)||(i==1) || (i==2)){
				System.out.println("value at i: " + i + " value: " + value.charAt(i));
				continue;
			}else{
				sb.append(value.charAt(i));
			}
		}
		return sb.toString();
	}
}