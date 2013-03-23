//$Id: RSSParser.java,v 1.9 2004/03/28 13:07:16 taganaka Exp $
package org.gnu.stealthp.rsslib;

import org.xml.sax.*;
import javax.xml.parsers.*;
import org.xml.sax.helpers.*;
import java.io.*;
import java.net.*;

/**
 * RSS Parser.
 *
 * <blockquote>
 * <em>This module, both source code and documentation, is in the
 * Public Domain, and comes with <strong>NO WARRANTY</strong>.</em>
 * </blockquote>
 *
 * @since RSSLIB4J 0.1
 * @author Francesco aka 'Stealthp' stealthp[@]stealthp.org
 * @version 0.2
 */

public class RSSParser {

  private  SAXParserFactory factory = RSSFactory.getInstance();
  private DefaultHandler hnd;
  private File f;
  private URL u;
  private InputStream in;
  private boolean validate;
  public RSSParser(){
    validate = false;
  }

  /**
   * Set the event handler
   * @param h the DefaultHandler
   *
   */
  public void setHandler(DefaultHandler h){
    hnd = h;
  }

  /**
   * Set rss resource by local file name
   * @param file_name loca file name
   * @throws RSSException
   */
  public void setXmlResource(String file_name) throws RSSException{
    f = new File(file_name);
    try{
      in = new FileInputStream(f);
    }catch(Exception e){
      throw new RSSException("RSSParser::setXmlResource fails: "+e.getMessage());
    }

  }

  /**
   * Set rss resource by URL
   * @param ur the remote url
   * @throws RSSException
   */
  public void setXmlResource(URL ur) throws RSSException{
    u = ur;
    File ft = null;
    try{
      URLConnection con = u.openConnection();
      in = u.openStream();
      if (con.getContentLength() == -1){
        this.fixZeroLength();
      }

    }catch(IOException e){
      throw new RSSException("RSSParser::setXmlResource fails: "+e.getMessage());
    }
  }

  /**
   * set true if parse have to validate the document
   * defoult is false
   * @param b true or false
   */
  public void setValidate(boolean b){
    validate = b;
  }

  /**
   * Parse rss file
   * @param filename local file name
   * @param handler the handler
   * @param validating validate document??
   * @throws RSSException
   */
  public static void parseXmlFile (String filename, DefaultHandler handler, boolean validating) throws RSSException{
    RSSParser p = new RSSParser();
    p.setXmlResource(filename);
    p.setHandler(handler);
    p.setValidate(validating);
    p.parse();
  }

  /**
   * Parse rss file from a url
   * @param remote_url remote rss file
   * @param handler the handler
   * @param validating validate document??
   * @throws RSSException
   */
  public static void parseXmlFile(URL remote_url, DefaultHandler handler, boolean validating) throws RSSException{
    RSSParser p = new RSSParser();
    p.setXmlResource(remote_url);
    p.setHandler(handler);
    p.setValidate(validating);
    p.parse();
  }

  /**
   * Try to fix null length bug
   * @throws IOException
   * @throws RSSException
   */
  private void fixZeroLength() throws IOException, RSSException {

    File ft = File.createTempFile(".rsslib4jbugfix", ".tmp");
    ft.deleteOnExit();
    FileWriter fw = new FileWriter(ft);
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    BufferedWriter out = new BufferedWriter(fw);
    String line = "";
    while ( (line = reader.readLine()) != null) {
      out.write(line + "\n");
    }
    out.flush();
    out.close();
    reader.close();
    fw.close();
    setXmlResource(ft.getAbsolutePath());

  }

  /**
   * Call it at the end of the work to preserve memory
   */
  public  void free(){
    this.factory = null;
    this.f       = null;
    this.in      = null;
    this.hnd     = null;
    System.gc();
  }

  /**
   * Parse the documen
   * @throws RSSException
   */
  public  void parse() throws RSSException{
    try {
      factory.setValidating(validate);
      // Create the builder and parse the file
      factory.newSAXParser().parse(in,hnd);
    }
    catch (SAXException e) {
      throw new RSSException("RSSParser::parse fails: "+e.getMessage());
    }
    catch (ParserConfigurationException e) {
     throw new RSSException("RSSParser::parse fails: "+e.getMessage());
    }
    catch (IOException e) {
     throw new RSSException("RSSParser::parse fails: "+e.getMessage());
    }

  }

}