import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.io.*;
import java.util.ArrayList;
import org.xbill.DNS.*;
import java.util.*;
import java.net.*; 
class DNSproject { 
		
		static int type = Type.A, dclass = DClass.IN;
    public static void main(String[] args)  throws IOException {
		//begin initiation//
		ArrayList<String> rList = generateListRootServers();
		Message query = null;
		Message response = null;
		SimpleResolver resolver;
		Name reqSite= null;
		try{
			reqSite = Name.fromString(args[0], Name.root); //takes first argument given from command line and makes it a name object
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Error, please make sure you enter a target website to resolve");
		}
		
		//end initiation//
		
		//begin trying the first root server and loop until one of them responds//
		boolean found = false;
		int ptr = 0;
		
		while(found==false){
			
			resolver = new SimpleResolver(rList.get(ptr));//
			Record record = Record.newRecord(reqSite,Type.A, DClass.IN);//requested site, record type, dns internet class
			query = Message.newQuery(record);
			response = resolver.send(query);
			
			if(response!=null){
				found = true;
			}else{
				ptr++;
			}
			if(ptr>= rList.size()){
				System.out.println("Error");
			}
		}
		//System.out.println(response.toString());
		String inf = response.toString(); //gets full result of the query to the root serve

		//response was found and stored in a string object//
		Record[] answerSection = response.getSectionArray(Section.ANSWER);
		Record[] authoritySection = response.getSectionArray(Section.AUTHORITY);
		System.out.println(answerSection[0]);
		}
	public static ArrayList<String> generateListRootServers() throws IOException{
		
		ArrayList<String> list = new ArrayList<String>();
                // Make a URL to the web page
        URL url = new URL("https://www.iana.org/domains/root/servers");

        // Get the input stream through URL Connection
        URLConnection con = url.openConnection();
        InputStream is =con.getInputStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line = null;

        // read each line and write to System.out
		int m=0;
        while ((line = br.readLine()) != null) {
			 String strWithoutSpace = line.replaceAll("\\s", "");
			if(strWithoutSpace.matches("<td>(.[0-9]).(.[0-9]).(.[0-9]).(.*)</td>")){
				//System.out.println(strWithoutSpace + " " + m);
				strWithoutSpace = strWithoutSpace.replaceAll("<td>","");
				strWithoutSpace = strWithoutSpace.replaceAll("</td>","");
				String[] spl = strWithoutSpace.split(",");
				strWithoutSpace = spl[0];
				list.add(strWithoutSpace);
			}
		}
		//System.out.println(list.toString());
		return list;
	}
	
	
	}