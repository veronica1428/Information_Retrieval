package apacheLucene;

import java.util.ArrayList;

public class HindiTextPreprocessingImpl extends Common implements HindiTextPreprocessingInterface{

	public String stopWordRemoval(String query){
		ArrayList<String> list = new ArrayList<String>();
		StringBuffer sb = null;
		String outputQuery = "";
		
		try{
			sb = new StringBuffer();
			String[] queryPartition = query.split(" ");
			list = Common.readFile("stopword_hi.txt");
			
			for (String str : queryPartition){
				if (!list.contains(str)){
					sb.append(str + " ");
				}
			}		
			outputQuery = sb.toString();
			
		}catch(Exception e){
			System.out.println("HindiTextPreprocessing.java :: Error in stopWordRemoval function: " + e.getMessage());
		}finally{
			sb.setLength(0);
		}
		
		return outputQuery;
	}
	
	public String removePunctuation(String query){
		StringBuffer sb = null;
		try{
			sb = new StringBuffer();
			for (char ch : query.toCharArray()){
				if(!(Character.toString(ch).matches("[*,.;:!?'\"\\[\\]()=]"))){
					sb.append(ch);
				}
			}
			query = sb.toString();
		}catch(Exception e){
			System.out.println("HindiTextPreprocessing.java :: Error while removing punctuations: " + e.getMessage());
		}finally{
			sb.setLength(0);
		}
		return query;
	}
	
	public String preprocess(String inputQuery){
		
		//Preprocessing : Punctuation Removal
		inputQuery = this.removePunctuation(inputQuery);
		
		//Preprocessing : StopWord Removal
		inputQuery = this.stopWordRemoval(inputQuery);
		
		return inputQuery;
	}
}
