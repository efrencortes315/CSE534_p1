import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.io.*;

class HelloWorldApp { 
    public static void main(String[] args)  throws IOException {
	
                // Make a URL to the web page
        URL url = new URL("https://www.iana.org/domains/root/servers");

        // Get the input stream through URL Connection
        URLConnection con = url.openConnection();
        InputStream is =con.getInputStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line = null;

        // read each line and write to System.out
		boolean found = false;
        while ((line = br.readLine()) != null) {
			 String strWithoutSpace = line.replaceAll("\\s", "");
			if(strWithoutSpace.matches("<td>(.*).root-servers.net</td>")){
				//found=true;
				System.out.println(strWithoutSpace);
			}
			/*if(strWithoutSpace.equals("</tbody>")){
				found = false;
			}
			if(found){
				System.out.println(strWithoutSpace);
			}*/
		}
    }
	
} 