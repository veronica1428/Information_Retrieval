package apacheLucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;

public class HtmlParse {

	public static void main(String[] args) throws IOException {
		System.out.println("Inside htmlParse main method");
		
		//readHtmlFiles(new File("//Users//veronica//Documents//study//information retrieval//project//DataBase//eng-hindi-dict-utf8"));
		make_dictionary();
	}
	
	public static void readHtmlFiles(File folder) throws IOException{
		System.out.println("Inside readHtmlFiles function");
		File[] files = folder.listFiles();
		File outFile = new File("eng-hind-translation.txt");
		
		for(File file : files){		
			if (file.isDirectory()){
				readHtmlFiles(file);
			}else{
				//System.out.println("parsed file name: " + file.getName() + " parsed file path: " + file.getAbsolutePath());
				parseHtmlFile(outFile, file);
			}
		}
	}
	
	public static void parseHtmlFile(File outFile, File htmlFile) throws IOException{
		
		BufferedReader br = null;
		FileWriter fw = null;
		String str = "";
		
		try {
			br = new BufferedReader(new FileReader(htmlFile));
			fw = new FileWriter(outFile.getAbsolutePath(),true);
			
			while((str=br.readLine())!=null){
				if (str.startsWith("\"")){
					str = Jsoup.parse(str).text();
					fw.write(str + "\n");}	}		}
		
		catch (IOException e) {
			e.printStackTrace();}
		
		finally{
			br.close();
			fw.close();}		}
	
	public static void make_dictionary() throws IOException{
		BufferedReader br = null;
		String line = "";
		
		try{
			br = new BufferedReader(new FileReader("eng-hind-translation.txt"));
			while((line=br.readLine())!=null){
				readEnglishHindiContent(line);
			}
			
		}catch(Exception e){
			System.out.println("HtmlParse.java :: Error while forming dictionary: " + e.getMessage());
		}finally{
			br.close();
		}
	}
	
	public static void readEnglishHindiContent(String line) throws IOException{
		String preprocessedLine = "";
		HashMap< String, String > dict = new HashMap<String, String>();
		
		preprocessedLine = removeDigit(line);

		String[] str = preprocessedLine.split(",");
		String englishWord = str[0];
		 
		if (str[2].contains("/")){
			//Containing multiple Hindi Meaning
			String[] multiMeaning = str[2].split("/");
			
			for(String hindiWord : multiMeaning){
				Hindi_eng_dict(dict, hindiWord, englishWord);
			}
		}else{
			Hindi_eng_dict(dict, str[2], englishWord);
		}
		
		write_To_File(dict);
	}
	
	public static String removeDigit(String line){
		StringBuffer outLine = new StringBuffer();
		String outputLine = "";
		try{
			for (char ch : line.toCharArray()){
				if (!(Character.isDigit(ch)) && !(Character.toString(ch).matches("[.\"]"))){
					outLine.append(ch);
				}
			}
			outputLine = outLine.toString();
		}catch(Exception e){
			System.out.println("HtmlParse :: Error while removing Digits: " + e.getMessage());
		}finally{
			outLine.setLength(0);
		}
		return outputLine;
	}
	
	public static void Hindi_eng_dict(HashMap<String, String> dict, String hindi, String english){
		
		if(!dict.containsKey(hindi)){
			dict.put(hindi, english.toLowerCase());
		}
	}
	
	public static void write_To_File(HashMap<String, String> dict) throws IOException{
		String path = "Hindi_English_ShabdKosh";
		
		FileWriter fw = null;
		File f = null;
		
		try{
			f = new File(path);
			fw = new FileWriter(f.getAbsolutePath(), true);
			
			for (Map.Entry<String, String> entry : dict.entrySet()){
				System.out.println("entry: " + entry.getKey() + " : " + entry.getValue());
				fw.write(entry.getKey() + "," + entry.getValue() + "\n");
			}
			
		}catch(Exception e){
			System.out.println("HtmlParse.java :: Error while writing dictionary: " + e.getMessage());
		}finally{
			fw.close();
		}		
	}
}
