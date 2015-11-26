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
import java.text.DecimalFormat;
import java.util.ArrayList;
import Enrichissement.GraphViz;
import org.jdom2.*;
import java.util.List;
import java.util.Iterator;
import org.jdom2.input.SAXBuilder;

public class Jaccard {

	/**
	 * @param args
	 *            the command line arguments
	 */

	static org.jdom2.Document document;
	static org.jdom2.Element racine;
	static double[][] mat;
	static double[][] carUnion;
	static int nbRDF;
	static List<String> urls = new ArrayList<String>();
	static GraphViz gv;

	public static void main(String[] args) {
		// TODO code application logic here
		ouvrirDoc();

		List listRDF = racine.getChildren("rdf");

		nbRDF = listRDF.size();

		initMatrices();

		int i = 0;
		int j;
		for (Iterator iRDF = listRDF.iterator(); iRDF.hasNext(); i++) {
			Element curRDF1 = (Element) iRDF.next();
			urls.add(curRDF1.getAttributeValue("url"));

			j = 0;

			for (Iterator jRDF = listRDF.iterator(); jRDF.hasNext(); j++) {
				Element curRDF2 = (Element) jRDF.next();

				if (i != j) {
					comparerRDF(curRDF1, i, curRDF2, j);
				}
			}

		}
		diviserMatrices();
		afficherMatrice(mat, nbRDF);

		genererDotSource();
		EcritureGrapheDansTxt.ecrireTxtFile(nbRDF, urls, mat);

	}

	private static void ouvrirDoc() {
		SAXBuilder sxb = new SAXBuilder();
		try {
			// On crée un nouveau document JDOM avec en argument le fichier XML
			// Le parsing est terminé ;)
			document = sxb.build(new File("liste_rdf.xml"));
			System.out.println("Fichier ouvert");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// On initialise un nouvel élément racine avec l'élément racine du
		// document.
		racine = document.getRootElement();
	}

	private static void initMatrices() {
		carUnion = new double[nbRDF][nbRDF];
		mat = new double[nbRDF][nbRDF];
		for (int i = 0; i < nbRDF; i++) {
			mat[i][i] = 1;
			carUnion[i][i] = 1;
		}

	}

	private static void afficherMatrice(double mat[][], int dim) {
		System.out.println("Matrice de similarité");
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				System.out.print("|" + mat[i][j]);
			}
			System.out.println("|");
		}
	}

	private static void diviserMatrices() {
		for (int i = 0; i < nbRDF; i++) {
			for (int j = 0; j < nbRDF; j++) {
				mat[i][j] = mat[i][j] / carUnion[i][j];
			}
		}
	}

	private static void comparerRDF(Element rdf1, int i, Element rdf2, int j) {
		List listTriplet1 = rdf1.getChildren("triplet");
		List listTriplet2 = rdf2.getChildren("triplet");

		carUnion[i][j] = listTriplet1.size() + listTriplet2.size();

		for (Iterator iTriplet = listTriplet1.iterator(); iTriplet.hasNext();) {

			Element curTriplet1 = (Element) iTriplet.next();

			for (Iterator jTriplet = listTriplet2.iterator(); jTriplet.hasNext();) {
				Element curTriplet2 = (Element) jTriplet.next();

				if (comparerTriplet(curTriplet1, curTriplet2)) {
					mat[i][j] += 1.0;
					carUnion[i][j] -= 1;
				}

			}
		}
	}

	private static boolean comparerTriplet(Element triplet1, Element triplet2) {
		String s1 = triplet1.getChild("s").getChildText("uri");
		String s2 = triplet2.getChild("s").getChildText("uri");
		String p1 = triplet1.getChild("p").getChildText("uri");
		String p2 = triplet2.getChild("p").getChildText("uri");
		Element o1 = triplet1.getChild("o");
		Element o2 = triplet2.getChild("o");
		if (!s1.equals(s2)) {
			return false;
		} else if (!p1.equals(p2)) {
			return false;
		} else if (!comparerObjet(o1, o2)) {
			return false;
		}
		return true;
	}

	private static boolean comparerTripletObjet(Element triplet1, Element triplet2) {
		Element o1 = triplet1.getChild("o");
		Element o2 = triplet2.getChild("o");
		if (!comparerObjet(o1, o2)) {
			return false;
		}
		return true;
	}

	private static boolean comparerObjet(Element triplet1, Element triplet2) {
		Element uri1 = triplet1.getChild("uri");
		Element uri2 = triplet2.getChild("uri");
		Element literal1 = triplet1.getChild("literal");
		Element literal2 = triplet1.getChild("literal");
		if ((uri1 == null && uri2 != null) || (uri2 == null && uri1 != null)) {
			return false;
		} else if (uri1 != null && uri2 != null) {
			String uri1text = uri1.getText();
			String uri2text = uri2.getText();
			if (!(uri1text.equals(uri2text))) {
				return false;
			}
		} else if (literal1 != null && literal2 != null) {
			String lang1 = literal1.getAttributeValue("lang", Namespace.XML_NAMESPACE);
			String lang2 = literal2.getAttributeValue("lang", Namespace.XML_NAMESPACE);
			String datatype1 = literal1.getAttributeValue("datatype");
			String datatype2 = literal2.getAttributeValue("datatype");
			String literal1Text = literal1.getText();
			String literal2Text = literal2.getText();

			if (lang1 != null && lang2 != null) {
				
				if (!lang1.equals(lang2)) {
					return false;
				} else if (!literal1Text.equals(literal2Text)) {

					return false;
				}
			}
			else if(datatype1 != null && datatype2 != null){
				if (!datatype1.equals(datatype2)) {
					return false;
				} else if (!literal1Text.equals(literal2Text)) {
					return false;
				}
			}

		}
		return true;
	}

	private static void genererDotSource() {
		gv = new GraphViz();
		gv.addln(gv.start_graph());
		for (int i = 0; i < nbRDF; i++) {
			for (int j = i; j < nbRDF; j++) {
				if (mat[i][j] > 0 && mat[i][j] < 1)
					gv.addln(urls.get(i) + "--" + urls.get(j) + "[label=\""
							+ (new DecimalFormat("#.##").format(mat[i][j])) + "\",weight=\""
							+ (new DecimalFormat("#.##").format(mat[i][j])) + "\"];");
			}

		}
		gv.addln(gv.end_graph());
		System.out.println(gv.getDotSource());
		gv.increaseDpi(); // 106 dpi
		String type = "gif";
		String repesentationType = "dot";
		File out = new File("out." + type);
		gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type, repesentationType), out);
	}

}
