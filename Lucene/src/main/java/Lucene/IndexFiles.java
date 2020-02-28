package Lucene;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.*;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.search.similarities.*;

public class IndexFiles {

 	// Directory where the search index will be saved
	private static String INDEX_DIRECTORY = "src/indexes";

	public static void main(String[] args) throws IOException {
		//CharArraySet stopwords = CharArraySet.copy(StopAnalyzer.ENGLISH_STOP_WORDS_SET);

		Analyzer analyzer = new EnglishAnalyzer();

		Directory directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));

		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
		// Change the "ClassicSimilarity" to change similarity scoring
	  indexWriterConfig = indexWriterConfig.setSimilarity(new BM25Similarity());

		indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

		IndexWriter iwriter = new IndexWriter(directory, indexWriterConfig);

		 String docPath = "src/cran/cran.all.1400";
	      FileReader fileReader = new FileReader(docPath);
	      BufferedReader bufferedReader = new BufferedReader(fileReader);
	      String line = "";
	      int index =1;
	      line = bufferedReader.readLine();
	      String id = "", title = "", author = "", bib = "", content = "", state = "";
	      Boolean first = true;
	      ArrayList<Map<String, String>> ankitaList = new ArrayList<Map<String,String>>();
	      String tk = "";
	      
	      while ((line = bufferedReader.readLine()) != null){
	          switch(line.substring(0,2)){
	            case ".I":
	              if(!first){
//	                Document d = createDocument(id,title,author,bib,content);
//	                writer.addDocument(d);
		     			 Map< String,String> hm =  new HashMap< String,String>();
		     			 hm.put("ID", id);
		     			hm.put("TITLE", title);
		     			hm.put("AUTHOR", author);
		     			hm.put("PUB", bib);
		     			hm.put("WORDS", content);
		     			ankitaList.add(hm);
	              }
	              else{ first=false; }
	              title = ""; author = ""; bib = ""; content = "";
	              id = line.substring(3,line.length()); break;
	            case ".T":
	            	tk = ".T";
	            case ".A":
	            	tk = ".A";
	            case ".B":
	            	tk = ".B";
	            case ".W":
	            	tk = ".W";
//	              state = tk; 
	              break;
	            default:
	              switch(tk){
	                case ".T": title += line + " "; break;
	                case ".A": author += line + " "; break;
	                case ".B": bib += line + " "; break;
	                case ".W": content += line + " "; break;
	              }
	          }
	        }
	      
			 Map< String,String> hm =  new HashMap< String,String>();
			 hm.put("ID", id);
			hm.put("TITLE", title);
			hm.put("AUTHOR", author);
			hm.put("PUB", bib);
			hm.put("WORDS", content);
			ankitaList.add(hm);
	      
	      
		    bufferedReader.close();
		    System.out.println(ankitaList.size());
		    for ( Map<String, String> i : ankitaList) {
		    	
		    	System.out.println(i.get("ID"));
		    	Document doc = new Document();
				doc.add(new TextField("index", i.get("ID")+"", Field.Store.YES));
			    doc.add(new TextField("title", i.get("TITLE"), Field.Store.YES));
			    doc.add(new TextField("author", i.get("AUTHOR"), Field.Store.YES));
			    doc.add(new TextField("published", i.get("PUB"), Field.Store.YES));
			    doc.add(new TextField("content", i.get("WORDS"), Field.Store.YES));
			    iwriter.addDocument(doc);
			}
		    
		    
		iwriter.close();
		directory.close();

	}

			


}