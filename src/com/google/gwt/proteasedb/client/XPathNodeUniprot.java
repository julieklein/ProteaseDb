package com.google.gwt.proteasedb.client;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.google.gwt.user.client.rpc.IsSerializable;


public class XPathNodeUniprot implements IsSerializable {
	  public NodeList getNodeListByXPath(String xpathQueryNode, Node xmlNode){
          NodeList r = null;
        try {
           
            r = (NodeList) XPathFactory.newInstance().newXPath().evaluate(xpathQueryNode, xmlNode, XPathConstants.NODESET);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(XPathUniprotPep.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;
    
}

}
