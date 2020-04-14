package searchEngine;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.StringTokenizer;
import org.jsoup.*;

public class searchengine {
	
	public static void main (String[] args) throws IOException {
		
		// transfer html to txt 
		String curpath="";
		try {
			 curpath = System.getProperty("user.dir");
		 }catch(Exception e) {}
		String path = curpath + "/src/W3C Web Pages";
		//System.out.println(path);
		File file = new File(path);
		File[] array = file.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return !name.equals(".DS_Store");
			}
		});
		//StdOut.println(array.length);
		for (int i =0;i<array.length;i++) {
			if (array[i].isFile()) {
					//System.out.println(array[i].getName());
					String title = array[i].getName().replaceAll(".htm", "").replaceAll(".html", "");
					org.jsoup.nodes.Document doc = Jsoup.parse(new File(array[i].getPath()),"utf-8","http://www.google.com");
					String text = doc.text();
					//StdOut.println(array[i].getPath());
					PrintWriter out = new PrintWriter(curpath + "/src/Webpagetxt/" + title+".txt");
					out.println(text);
					out.close();
			}
		}
		
		String outpath = curpath + "/src/Webpagetxt";
		//StdOut.println(outpath);
		// input keyword
		StdOut.println("Please input the keyword: ");
		Scanner scanner = new Scanner(System.in);
		String keyword = scanner.nextLine();
		scanner.close();
		
        ArrayList<Integer> a = new ArrayList<Integer>();
        BTree<String, String> btree = new BTree<String, String>();
          
        File outfile =new File(outpath);
		File[] outarray = outfile.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return !name.equals(".DS_Store");
			}
		});
				
		// search keyword in text file 
		for (int i =0;i<outarray.length;i++) {
			In in =new In(outarray[i].getPath());
			StringBuffer sb = new StringBuffer();
			
			while (!in.isEmpty()) {
				sb.append(in.readLine());
			}
			// StringTokenizer
			StringTokenizer str = new StringTokenizer(sb.toString().toLowerCase()," -,.()//:!");
			
			// TST output occur times and url
			TST<Integer> tst = new TST<Integer>();
			
			//int j =0;
	        while (str.hasMoreTokens()) {
	        	tst.put1(str.nextToken(), 0);
	        }
	        
	        String key=keyword.toString();
	        Integer occurtimes = tst.get(key.toLowerCase());
	        
	        if (occurtimes != null) {
	        	// store the key value to array
	        	a.add(occurtimes);
	        	// Store occur time and url in B-tree
	        	//btree.put(outarray[i].getName().replaceAll("txt", "htm"),occurtimes.toString());
	        	StdOut.println(outarray[i].getName().replaceAll("txt", "htm")+", " + occurtimes.toString());
	        	btree.put(occurtimes.toString(),outarray[i].getName().replaceAll("txt", "htm"));	        
	        }
		}
		
		maxHeap maxHeap = new maxHeap(a.size());
        // use maxheap to store the occurtimes and output top 10 url
    	for (int j=0;j<a.size();j++) {
    		maxHeap.insert(a.get(j));
    	}
    	
    	StdOut.println();
    	StdOut.println("Ranking:  ");
    	String old="";
    	String max="";
    	int num=1;
    	if (maxHeap != null) {
    		for (int l=0;l<10;) {
    			max = maxHeap.extractMax();
    			if(!max.equals(old)) 
    			{
    				for (int k =0;k<btree.get(max).size();k++) {
    					StdOut.println("Top " + num +": " + btree.get(max).get(k));
    				}
    				num++;
    				l+=btree.get(max).size();
    			}
    			old=max;
    		}
    	}
	}
	
}
