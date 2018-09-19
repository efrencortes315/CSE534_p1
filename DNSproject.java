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
		
		ArrayList<String> rList = generateListRootServers();
		Message query = null;
		Message response = null;
		SimpleResolver resolver;
		Name reqSite = Name.fromString(args[0], Name.root); //takes first argument given from command line and makes it a name object
		
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
			System.out.println(response.getHeader().toString());
			
		
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
	







	public static String resolve(String host, int addrType) {
    try {
        Lookup lookup = new Lookup(host, addrType);
        SimpleResolver resolver = new SimpleResolver("114.114.114.114");
        resolver.setTimeout(5);
        lookup.setResolver(resolver);
        Record[] result = lookup.run();
        if (result == null) return null;

        List<Record> records = java.util.Arrays.asList(result);
        java.util.Collections.shuffle(records);
        for (Record record : records) {
            if (addrType == Type.A) {
                return ((ARecord) record).getAddress().getHostAddress();
            } else if (addrType == Type.AAAA) {
                return ((AAAARecord) record).getAddress().getHostAddress();
            }
        }

    } catch (Exception ex) {
        return null;
    }

    return null;
}
	}