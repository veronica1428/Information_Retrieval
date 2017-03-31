package apacheLucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;

import org.apache.lucene.util.ArrayUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class RandomProcessing {

	public static void main(String[] args) throws IOException {
		
		String path = "//Users//veronica//Documents//study//information retrieval//project//DataBase//stopwords_hi.txt";
		formStopWordRemovalFile(path);}
	
	public static void formStopWordRemovalFile(String path) throws IOException{
		BufferedReader br = null;
		ArrayList<String> list = null;
		FileWriter fw = new FileWriter(new File("stopword_hi.txt"), true);
		String line = "";
		
		try{
			br = new BufferedReader(new FileReader(path));
			list = new ArrayList<String>();
			
			while((line=br.readLine())!=null){
				if(!list.contains(line)){
					list.add(line.toLowerCase());}
			}
			
			for (String str : list){
				fw.write(str + "\n");}}
		
		catch(Exception e){
			System.out.println("RandomProcessing.java :: Error while forming stop word file: " + e.getMessage());}
		finally{
			list.clear();
			fw.close();
			br.close();}}
	
	public static String[] extractContents_Summary(FileReader f) throws IOException{
		BufferedReader br = null;
		StringBuffer sb = null;
		String[] outputString = new String[2];
		String line = "", summary = "";
		StringBuffer content = new StringBuffer();
		HindiTextPreprocessingImpl preprocess = new HindiTextPreprocessingImpl();
		
		try{
			br = new BufferedReader(f);
			sb = new StringBuffer();
	
			while((line=br.readLine())!=null){
				//line = preprocess.preprocess(line);
				//line = preprocess.
				sb.append(line);
			}

			String HTMLSTring = sb.toString();
			Document html = Jsoup.parse(HTMLSTring);
			String title = html.title();
			String h1 = html.body().getElementsByTag("text").text(); 
			String h3 = html.body().getElementsByTag("h3").text();
			String headingSpan = html.body().getElementsByTag("span").text();
			
			Elements titles = html.select(".ns-0");
			for(Element e: titles){
				content.append(e.text());
				//System.out.println("text: " +e.text());
				//System.out.println("html: "+ e.html());
			}
			
			/*System.out.println("Input HTML String to JSoup :" + HTMLSTring); 
			System.out.println("After parsing, Title : " + title); 
			System.out.println("After parsing, Heading h1: " + h1);
			System.out.println("After parsing, Heading h3 : " + html.body().getElementsByTag("h3").text());
			System.out.println("After parsing, Heading span : " + html.body().getElementsByTag("span").text());*/
			
			summary = title + " : " + headingSpan;
			if(content.length()==0){
				content.append(title + "  " + h3 + "  " + headingSpan);
			}
			
			outputString[0] = (summary); outputString[1] = (content.toString());
			
			/*if (fieldName.equalsIgnoreCase("summary")){
				outputString = title + " : " + headingSpan;
				System.out.println("outpput  stirnf: summary: " + outputString);
			}else if (fieldName.equalsIgnoreCase("contents")) {
				
				if (content.length()==0){
					System.out.println("content length 0 : ");
					content.append(title + "  " + h3 + "  " + headingSpan);
				}
				outputString = content.toString();
				System.out.println("outpput  stirnf: content: " + outputString);
			}*/
		}catch(Exception e){}
		
		/*finally{
			//br.close();
			//sb.setLength(0);
		}*/
		
		//System.out.println("outputStirng: outside " + outputString);
		return outputString;
	}
	
	public static void extract(File path){
		Document doc = null;
		
		try {
            doc = Jsoup.connect(path.getAbsolutePath()).get();
            System.out.println("content: " + doc);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
	}
}
