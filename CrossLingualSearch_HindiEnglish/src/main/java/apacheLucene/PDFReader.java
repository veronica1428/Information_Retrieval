package apacheLucene;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

public class PDFReader {
	
	
	/*
	 * @Author : veronica
	 * Function : function reads the pdf file and extract contents from it
	 * @parameter : none
	 * 
	 */
	public static void main(String[] args) {

		IndexFiles_backup indFile = new IndexFiles_backup();
		PdfReader reader = null;
		
		try{
			reader = new PdfReader("/Users/veronica/Desktop/webcrawler assignment.pdf");
			System.out.println("reader file length:  " + reader.getFileLength() + "getnumber of pages: " + reader.getNumberOfPages());
			 
			String page = PdfTextExtractor.getTextFromPage(reader, 1);
			System.out.println("page: " + page);
			System.out.println("reader tempered: " + reader.isTampered());
			System.out.println("reader encrypted: " + reader.isEncrypted());
		}catch(Exception e){
			System.out.println("Error in Reading PDF file"+ e.getMessage());
		}finally{
			reader.close();
		}
	}	
}