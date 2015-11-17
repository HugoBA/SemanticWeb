package Enrichissement;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author zlahjouji
 */
import java.io.*;

import org.jdom.input.SAXBuilder;
import org.jdom2.*;
import org.jdom2.input.*;
import org.jdom2.filter.*;
import java.util.List;
import java.util.Iterator;


public class Jaccard {
    /**
     * @param args the command line arguments
     */
    
     static org.jdom.Document document;
     static org.jdom.Element racine;
    
    //static boolean matches
    public static void main(String[] args) {
         SAXBuilder sxb = new SAXBuilder();
            try
            {
               //On crée un nouveau document JDOM avec en argument le fichier XML
               //Le parsing est terminé ;)
               document = sxb.build(new File("rdf.xml"));
            }
            catch(Exception e){}

            //On initialise un nouvel élément racine avec l'élément racine du document.
            racine = document.getRootElement();
            List listRDF = racine.getChildren("rdf");
          
            int tailleRDF=listRDF.size();
            
          //matrice indice de jaccard
            double[][] mat= new double[tailleRDF][tailleRDF];
            Iterator i = listRDF.iterator();
            Iterator j = listRDF.iterator(); 
                
            while(i.hasNext())
            {
                
            }
    
}
}
