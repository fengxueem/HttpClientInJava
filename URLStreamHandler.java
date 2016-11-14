import java.util.regex.*;

public class URLStreamHandler {

  private String spec;
  private String host;
  private String path;
  private String file;

  public URLStreamHandler(String spec) {
	  this.spec = spec;
    analyseURL();
  }

  private void analyseURL() {
    int hostStart = findPattern(spec, "//") + 2;
    int hostEnd = findPattern(spec, "\\w/") + 1;
    host = spec.substring(hostStart, hostEnd);
    path = spec.substring(hostEnd);
    int fileStart = findPattern(path, "/[^/]*$") + 1;
    if (fileStart != 0) {
      file = path.substring(fileStart);
    } 
  }
	
  private int findPattern(String str,String regex) {
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(str);
    if (m.find()) {
      return m.start();
    } else {
      return -1;
    }
  }

  public String getSpec() {return spec;}

  public String getHost() {return host;}

  public String getPath() {return path;}

  public String getFile() {return file;}

}