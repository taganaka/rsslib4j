//$Id: Test.java,v 1.13 2004/05/06 16:04:42 taganaka Exp $
package org.gnu.stealthp.rsslib;
import javax.xml.parsers.*;
import java.util.*;
import java.net.*;
import gnu.getopt.*;

/**
 * Main method for lib test.
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


public class Test {

  public static void main(String[] args) {

    if (args.length < 2) usage();

    RSSHandler hand = new RSSHandler();

    Getopt opt = new Getopt("Test", args, "f:u:");

    int c;
    String arg;
    while ((c = opt.getopt()) != -1){
      switch (c) {
          case 'f':
            arg = opt.getOptarg();
            try{
              RSSParser.parseXmlFile(arg, hand, false);
            }catch(RSSException e){
              e.printStackTrace();
              System.exit(1);
            }
            break;
          case 'u':
            arg = opt.getOptarg();
            try{
              URL u = new URL(arg);
              RSSParser.parseXmlFile(u,hand,false);
            }catch(Exception e){
              e.printStackTrace();
              System.exit(1);
            }
            break;
            case '?':
              usage();

      }

    }


    RSSChannel ch = hand.getRSSChannel();
    System.out.println("****************CHANNEL INFO ************");
    System.out.println(ch.toString());

    if (ch.getRSSImage() != null){
      System.out.println("IMAGE INFO: \n" + ch.getRSSImage().toString());
      System.out.println("IMAGE IN HTML: \n" + ch.getRSSImage().toHTML());

    }else
      System.out.println("CHANNEL HAS NO IMAGE");

    if (ch.getRSSTextInput() != null){
      System.out.println("INPUT INFO: \n" + ch.getRSSTextInput().toString());
      System.out.println("HTML INPUT:\n" + ch.getRSSTextInput().toHTML());
    }else
      System.out.println("CHANNEL HAS NO FORM INPUT");

    if (ch.getDoublinCoreElements()!= null){
      System.out.println("DOUBLIN CORE INFO:");
     // Hashtable tbl = ch.getDoublinCoreElements();

      System.out.println(ch.getDoublinCoreElements().toString());
    }else
      System.out.println("CHANNEL HAS NO DOUBLIN CORE TAGS");



    System.out.println("SEQUENCE INFO:");
    if (ch.getItemsSequence() != null)
      System.out.println(hand.getRSSChannel().getItemsSequence().toString());
    else
      System.out.println("CHANNELL HAS NO SEQUENCE MYBE VERSION 0.9 or 2?");

    System.out.println("*****************************************");
    LinkedList lst = hand.getRSSChannel().getItems();
    System.out.println("****************ITEMS INFO *************");
    System.out.println("ITEM SIZE: " + lst.size());
    System.out.println("****************************************");

    for (int i = 0; i < lst.size(); i++){
      RSSItem itm = (RSSItem)lst.get(i);
      System.out.println("****************ITEM DETAILS *************");
      System.out.println(itm.toString());

      if (itm.getDoublinCoreElements()!= null){
        System.out.println("DOUBLIN CORE INFO FOR ITEM:");
        System.out.println(itm.getDoublinCoreElements().toString());
    }else
      System.out.println("ITEM HAS NO DOUBLIN CORE TAGS");

      System.out.println("******************************************");
    }

  }

  private static void usage(){
    String info = "Usage: java org.gnu.stealthp.rsslib.Test -f <local_file_name> | -u <remote_file_name>";
    System.out.println(info);
    System.exit(1);
  }

}
