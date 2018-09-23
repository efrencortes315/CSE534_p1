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
		public int a = 9;
	public ArrayList<String> findNextList(ArrayList<String> lastList, Message query){
		ArrayList<String> prevList = lastList;
		boolean found = false;
		int ptr = 0;
		while(found==false){
			
			
			
		}
		
		return prevList;
	}
    public static void main(String[] args)  throws IOException, SocketException 
	{
		//begin initiation
		//ArrayList<String> rList = generateListRootServers();
		ArrayList<String> rList = generateRoots();
		Message query = null;
		Message response = null;
		SimpleResolver resolver=null;
		Name reqSite= null;
		try{
			reqSite = Name.fromString(args[0], Name.root); //takes first argument given from command line and makes it a name object
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Error, please make sure you enter a target website to resolve");
		}
		Record record = Record.newRecord(reqSite,Type.A, DClass.IN);//requested site, record type, dns internet class
		query = Message.newQuery(record);
		//end initiation//
		
		//begin trying the first root server and loop until one of them responds//
		boolean found = false;
		int ptr = 0;
		
		while(found==false){
			
			resolver = new SimpleResolver(rList.get(ptr));//
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

		//response was found and stored in a string object, most likely, we will need to now request to a TLD server//
		Record[] answerSection = response.getSectionArray(Section.ANSWER); //check if we got answer already
		Record[] authoritySection = response.getSectionArray(Section.AUTHORITY);//but we probably didn't
		Record[] additionalSection = response.getSectionArray(Section.ADDITIONAL);//additionalSection
		System.out.println(inf);
		
		
		
		ArrayList<String> nextLevel = new ArrayList<String>();
		for(int i=0;i<additionalSection.length;i++){
			if(additionalSection[i].getType()==1){
				nextLevel.add(additionalSection[i].rdataToString());
			}
			
		}
		ptr=0;
		found=false;
		while(found==false){
			response=null;
			resolver= new SimpleResolver(nextLevel.get(ptr));
			response = resolver.send(query);
			if(response!=null){
				found=true;
			}else{
				ptr++;
			}
			if(ptr>= nextLevel.size()){
				System.out.println("Error");
			}
		}
		String nextInf = response.toString();
		System.out.println(nextInf);
		
		Record[] answerSection2 = response.getSectionArray(Section.ANSWER);
		Record[] authoritySection2 = response.getSectionArray(Section.AUTHORITY);
		Record[] additionalSection2 = response.getSectionArray(Section.ADDITIONAL);
		ArrayList<String> nextLevel2 = new ArrayList<String>();
		for(int i=0;i<additionalSection2.length;i++){
			if(additionalSection[i].getType()==1){
				nextLevel2.add(additionalSection2[i].rdataToString());
			}
		}
		ptr=0;
		found=false;
		while(found==false){
			response=null;
			resolver = new SimpleResolver(nextLevel2.get(ptr));
			
			try{
				response = resolver.send(query);
			}catch(SocketException s){
				ptr++;
				continue;
			}
			
			if(response!=null){
				found=true;
			}else{
				ptr++;
			}if(ptr>=nextLevel2.size()){
					System.out.println("Error");
			}
		}
		String nextInf2 = response.toString();
		System.out.println(nextInf2);
		}
	/*public static ArrayList<String> generateListRootServers() throws IOException{
		
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
	*/
	public static ArrayList<String> generateRoots(){
		ArrayList<String> rList = new ArrayList<String>();
		rList.add("198.41.0.4");
		rList.add("192.228.79.201");
		rList.add("192.33.4.12");
		rList.add("199.7.91.13");
		rList.add("192.203.230.10");
		rList.add("192.5.5.241");
		rList.add("192.112.36.4");
		rList.add("198.97.190.53");
		rList.add("192.36.148.17");
		rList.add("192.58.128.30");
		rList.add("193.0.14.129");
		rList.add("199.7.83.42");
		rList.add("202.12.27.33");
		
		return rList;
	}
	}