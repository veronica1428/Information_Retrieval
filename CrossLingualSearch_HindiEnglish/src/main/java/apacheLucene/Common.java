package apacheLucene;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

public class Common {

	private String englishDataLoc = "";
	private String hindiDataLoc = "";
	private String indexLoc = "";
	private String dictLocation = "";
	
	//Read from a file
	public static ArrayList<String> readFile(String path) throws IOException{
		BufferedReader br = null;
		ArrayList<String> list = null;
		String line = "";
		
		try{
			br = new BufferedReader(new FileReader(path));
			list = new ArrayList<String>();
			
			while((line = br.readLine())!=null){
				list.add(line.toLowerCase());
			}
			
		}catch(Exception e){
			System.out.println("Common.java :: Error while reading file: " + e.getMessage());
		}finally{
			br.close();
		}
		return list;
	}
	
	public void extractSummary(String fileLocation) throws IOException{
		//Taking the first Two Paragraphs
		BufferedReader br = null;
		StringBuffer result = null;
		String line = "";
		int paragraphs = 1;
		boolean isEmpty = true;
		
		try{
			br = new BufferedReader(new FileReader(fileLocation));
			result = new StringBuffer();
			
			while(line!=null && paragraphs>0){
				line = br.readLine();
			}
			
		}catch(Exception e){
			System.out.println("Error while extracting the summary: " + e.getMessage());
		}finally{
			br.close();
		}
	}
	
	//Property file read function to read tar.gz file from property file
		/*public void readPropertyFile(){
			System.out.println("Inside read Property file");
			Properties prop = new Properties();
			try{
			String propPath = System.getProperty("user.dir") + System.getProperty("file.separator") + "propertyFile.properties";
			InputStream input = new FileInputStream(propPath);
			if (input != null) {
				prop.load(input);
			}
			hiDataLoc = prop.getProperty("hiDataLocation");
			enDataLoc = prop.getProperty("enDataLocation");
			indexPath = prop.getProperty("indexPath");
			dictLocation = prop.getProperty("dictLocation");
			}catch(Exception e){
				e.printStackTrace();
			}
		}*/
}
