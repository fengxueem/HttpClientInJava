import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

public class HttpClient {
  // socket connecting to server
  Socket socket;
  // parse URL
  URLStreamHandler handler;

  public HttpClient(String url) throws Exception {
  	handler = new URLStreamHandler(url);
  	socket = new Socket(InetAddress.getByName(handler.getHost()), 80);
  }

  // Send a GET request to server
  public void sendHttpGet() throws Exception {
  	PrintWriter pw = new PrintWriter(socket.getOutputStream());
	pw.println("GET " + handler.getPath() + " HTTP/1.1");
	pw.println("Host: " + handler.getHost());
	pw.println();
	pw.println("");
	pw.flush();
  }

  public void retriveHttpResponse() throws Exception {
  	System.out.println("Retriving HTTP response...");
  	BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	List<String> lines = new ArrayList<String>();
	System.out.println("Start writing to disk...");
	BufferedWriter bufw = new BufferedWriter(
		new FileWriter("HTTPResponse " + handler.getHost() + System.currentTimeMillis() % 100));
	System.out.println("HTTP header lines: ");
	String line;
	// read the Headers and CRLF and store in disk
	while((line = br.readLine()) != null) {
	  bufw.write(line);
	  bufw.newLine();
	  bufw.flush();
	  System.out.println(line);
	  lines.add(line);
	  if (line.equals("")) {
	  	break;
	  }
	}
	// find error in status line
	findError(lines.get(0));
	// read the data part and store in disk
	if (bufw != null) {
	  int c;
	  while ((c = br.read()) != -1) {
	  	bufw.write(c);
	    bufw.flush();
	  }
	  bufw.close();
	} else {
	  System.out.println("file does not exist");
	}
	br.close();
  }

  // find error code: 4XX or 5XX
  private void findError(String statusLine) {
    String regex = "(4\\d{2})|(5\\d{2})";
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(statusLine);
    if (m.find()) System.out.println("Error Code: " + m.group());
  }
    
  public static void main(String[] args){
	try{
	  System.out.println("Please input URL starting with \"http://\":");
	  // read a URL from stdin
	  Scanner scanner = new Scanner(System.in);
	  String url = scanner.nextLine();
	  // URL must start with "http://"
	  while (!url.matches("^http://.+$")){
	  	System.out.println("Please input a valid URL again starting with \"http://\":");
	  	url = scanner.nextLine();
	  }
      System.out.println("Trying to connect: " + url);
	  HttpClient httpClient = new HttpClient(url);
	  httpClient.sendHttpGet();
	  httpClient.retriveHttpResponse();
	  // close the socket
	  httpClient.socket.close();
	} catch (Exception e) {
	  System.out.println(e);
	  e.printStackTrace();
	}
  }
}

