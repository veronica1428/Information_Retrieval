package apacheLucene;
/**
 * @author veronica
 * This Class only indexes the database(English and Hindi).
 * 
 */


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.hi.HindiAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexDeletionPolicy;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.KeepOnlyLastCommitDeletionPolicy;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class IndexFiles {

	private String hiDataLoc = "";
	private String enDataLoc = "";
	private String indexPath = "";
	private String dictLocation = "";
	
	private ArrayList<File> queue = new ArrayList<File>();
	
	/**
	 * @author veronica
	 * Used to read Property file to fetch values
	 * 
	 * @param path
	 */
	//Property file read function to read tar.gz file from property file
	public void readPropertyFile(){
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
	}
	
	/**
	 * @author veronica
	 * Recursively iterate through each folder
	 * If finds HTML file, index it otherwise continue.
	 * @param location
	 * @throws IOException
	 */
	static public void readHtmlFiles(IndexWriter writer, File[] files) throws IOException{
		
		IndexFiles indFile = new IndexFiles();
		for(File file : files){		
			
			if (file.isDirectory()){
				readHtmlFiles(writer, file.listFiles());
			}else{
				//===================================================
			    // Only index text files
			    //===================================================
				if (file.getName().toLowerCase().endsWith(".html")||file.getName().toLowerCase().endsWith("htm")){
					//indexDoc(writer, file.getAbsolutePath().toString());
					indFile.queue.add(file);					
				}else{
					System.out.println("skipped: " + file.getName());
				}
				
				indexDoc(writer, indFile.queue);
			}
		}
	}
	
	/**
	 * @author veronica
	 * Used to index English database
	 * 
	 * @param path
	 * @param indexPath
	 * @throws IOException
	 */
	public static void indexEngDocs(Path path, String indexPath) throws IOException{
		
		//Create instance of Directory where index files will be stored
		Directory fsDirectory = FSDirectory.open(Paths.get(indexPath));
		
		//Create an instance of analyzer to tokenize the input text(For English here used Standard Analyzer)
		Analyzer analyzer = new StandardAnalyzer();

		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		//iwc.setSimilarity(new CustomSimilarity((float) 0.5));	//used custom Similarity
		IndexWriter writer = new IndexWriter(fsDirectory, iwc);
		
		File folder  = new File(path.toString());
		File[] files = folder.listFiles();
		
		readHtmlFiles(writer, files);
		
		writer.close();
		analyzer.close();
		fsDirectory.close();
	}
	
	/**
	 * @author veronica
	 * Used to index Hindi database
	 * 
	 * @param path
	 * @param indexPath
	 * @throws IOException
	 */
	public static void indexHindiDocs(Path path, String indexPath) throws IOException{
		Directory dir = FSDirectory.open(Paths.get(indexPath));
		
		//Create an instance of analyzer to tokenize the input text(For Hindi here used Hindi Analyzer)
		Analyzer analyzer = new HindiAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		//iwc.setSimilarity(new CustomSimilarity(0.5F));  //used Custom similarity
		IndexWriter writer = new IndexWriter(dir, iwc);
		
		File folder  = new File(path.toString());
		File[] files = folder.listFiles();
		
		readHtmlFiles(writer, files);
		
		writer.close();
		analyzer.close();
		dir.close();
	}
	
	/**
	 * @author veronica
	 * Used to index all the documents present in Arraylist
	 * 
	 * @param path
	 * @param indexPath
	 * @throws IOException
	 */
	static public void indexDoc(IndexWriter writer, ArrayList<File> queue) throws IOException{
		
		int originalDocuments = writer.numDocs();
		int newDocuments = 0;
		String[] data = new String[2];
		
		if (queue == null){
			System.out.println("Input valid files");
			return ;
		}
		
		//traverse Queue arraylist containing html/htm files to be indexed   
		for (File file : queue){
			FileReader fr = null;			
		
			try{
			   Document doc = new Document();
					
			   data = RandomProcessing.extractContents_Summary(new FileReader(file));
			   String summary = data[0];
			   String content = data[1];
			   
			   doc.add(new TextField("contents", content, Field.Store.YES));
			   doc.add(new TextField("summary", summary , Field.Store.YES));
			   doc.add(new StringField("path", file.getAbsolutePath(), Field.Store.YES));
			   doc.add(new StringField("filename", file.getName(), Field.Store.YES));
			   
			   writer.addDocument(doc);
			   System.out.println("Added: " + file);
				
			}catch(Exception e){
				System.out.println("Could not add :" + file);
			}
		}
		
		newDocuments = writer.numDocs();
		System.out.println("Original Documents: " + originalDocuments);
		System.out.println("New Documents: " + newDocuments);
		System.out.println("Documents added: " + (newDocuments - originalDocuments));
		queue.clear();
	}

	public static void main(String[] args) throws IOException {
		System.out.println("Inside main function of IndexFiles class");
		
		IndexFiles index = new IndexFiles();
		long startTime, endTime;
		
		//read database location using property files
		index.readPropertyFile();
		System.out.println("Indexing to Directory............");
		
		Path p1 = Paths.get(index.hiDataLoc);
		Path p2 = Paths.get(index.enDataLoc);
		
		startTime = System.currentTimeMillis();
		
		indexHindiDocs(p1, index.indexPath);
		indexEngDocs(p2, index.indexPath);	
		
		endTime = System.currentTimeMillis();
		System.out.println("All documents processed within time: " + (endTime-startTime)/1000 + "s");
	}
}
