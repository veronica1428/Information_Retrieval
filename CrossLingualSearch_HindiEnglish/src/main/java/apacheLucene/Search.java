package apacheLucene;
/**
 * This class searches Hindi and English Documents for a given Query
 * 
 */


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.hi.HindiAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query.*;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.search.similarities.LMJelinekMercerSimilarity;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;

public class Search{

	public static void main(String[] args) {

		searchFiles();		
	}
	
	public static void searchFiles(){
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		long startTime , endTime ;
		
		//now search files
		try {
			IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("index")));
			IndexSearcher searcher = new IndexSearcher(reader);
			
			String input = "";		
			while(!input.equalsIgnoreCase("q")){

				System.out.println("Enter the search Query: (q=quit())");
				input = br.readLine();
				if(input.equalsIgnoreCase("q")){
					System.out.println("Thanks for Searching");
					break;
				}
				
				//start time for processing the documents
				startTime = System.currentTimeMillis();
				
				ScoreDoc[] hits = search(reader, searcher, input);				
				System.out.println("Found " + hits.length + " Hits");	
				for (ScoreDoc hit : hits){
					int docNum = hit.doc;
					Document d = searcher.doc(docNum);
					System.out.println(" document path: " + d.get("path") + " score: " + hit.score);
					System.out.println("summary: " + d.get("summary") + "\n\n");
				}
				
				endTime = System.currentTimeMillis();		
				System.out.println("Program Run Time: " + (endTime - startTime)  + "ms");				
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ScoreDoc[] search(IndexReader reader, IndexSearcher searcher, String input){
		
		String translated = "";
		HashMap<String, String> dictionary;
		
		Analyzer hindiAnalyzer = new HindiAnalyzer();
		TopScoreDocCollector collector = TopScoreDocCollector.create(5);
		
		try{
		
			dictionary = readFromDictionary();
			translated = translateQuery(dictionary, input);
			
			Query q = new org.apache.lucene.queryparser.classic.QueryParser("contents", hindiAnalyzer).parse(input);
			
			searcher.setSimilarity(new LMJelinekMercerSimilarity(0.1F));
			searcher.search(q, collector);
			System.out.println("total hits: "+ collector.getTotalHits());
			
			//////Testing
			/*
			ScoreDoc[] hit = (collector.topDocs().scoreDocs);
			Explanation explanation;
			for (ScoreDoc i : hit){
				explanation = searcher.explain(q, i.doc);
				System.out.println("explanation: " + explanation.toString());
			}*/
			
			/////////Testing
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return (collector.topDocs().scoreDocs);
	}
	
	public static String translateQuery(HashMap<String, String> dictionary, String inputQuery){
		
		HindiTextPreprocessingImpl preprocess = new HindiTextPreprocessingImpl();
		String finalQuery = "";
		
		try{
			inputQuery = preprocess.preprocess(inputQuery);
			finalQuery = translate(dictionary , inputQuery);
		}catch(Exception e){
			System.out.println("Search.java :: Error while translating Input Query: " + e.getMessage());
		}finally{
			
		}
		return finalQuery;
	}
	
	public static HashMap<String, String> readFromDictionary() throws IOException{
		BufferedReader br = null;
		HashMap<String, String> dictMap = new HashMap<String, String>();
		String line = "";
		try{
			br = new BufferedReader(new FileReader("Hindi_English_ShabdKosh"));
			
			while((line=br.readLine())!=null){
				String[] values = line.split(",");
				dictMap.put(values[0], values[1]);
			}
		}catch(Exception e){
			System.out.println("Search.java :: Error while reading Dictionary: " + e.getMessage());
		}finally{
			br.close();
		}
		return dictMap;
	}
	
	public static String translate(HashMap<String, String> map , String input_query){
		//ArrayList<String> list = new ArrayList<String>();
		StringBuffer sb  = new StringBuffer();
		String final_query = "";
		
		String[] interm_query = input_query.split(" ");
		for(String str : interm_query){
			if (map.containsKey(str)){
				sb.append(map.get(str) + " " + str + " ");
			}else{
				sb.append(str + " ");
				System.out.println("Word not present in dictionary: " + str);
			}
		}
		
		final_query = sb.toString();
		sb.setLength(0);
		System.out.println("final query: " + final_query);
		return final_query;
	}
}
