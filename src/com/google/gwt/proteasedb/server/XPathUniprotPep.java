package com.google.gwt.proteasedb.server;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class XPathUniprotPep {
    public NodeList getNodeListByXPath (String xpathQuery, Document xmlDoc){
        NodeList r = null;
      try {
         
          r = (NodeList) XPathFactory.newInstance().newXPath().evaluate(xpathQuery, xmlDoc, XPathConstants.NODESET);
      } catch (XPathExpressionException ex) {
          Logger.getLogger(XPathUniprotPep.class.getName()).log(Level.SEVERE, null, ex);
      }
      return r;
    }
       
}
