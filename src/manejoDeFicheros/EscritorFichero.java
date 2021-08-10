package manejoDeFicheros;

import java.io.*;
import java.util.ArrayList;
import javax.xml.parsers.*;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;


import org.w3c.dom.*;

import controlador.MetodosAdicionales;
import excepciones.ExcepcionNombreArchivo;
import main.Main;
import modelo.Libro;
import modelo.Menu;

public class EscritorFichero {
	
	ArrayList<Libro> libros;
	boolean cargado = true;
	int numLibros = 0;
	
	/**
	 * se crea el objeto con los libros que hay en memoria
	 * @param pLibros
	 */
	public EscritorFichero(ArrayList<Libro> pLibros) {
		this.libros = pLibros;
	}
	
	/**
	 * se escribe en el fichero TXT que se nos pasa lo que se quiere escribir o sobreescribir 
	 * @param fichero TXT a escribir
	 * @param boolean si se sobreescribe o no
	 * @return devuelve el fichero cargado
	 */
	public boolean escribirFicheroTxt(File pFichero, boolean sobreeescribir){

		numLibros = 0;
		cargado = true;
		try{
		
			BufferedWriter bwFichero = new BufferedWriter(new FileWriter(pFichero, sobreeescribir));
			
			for (int i = 0; i < libros.size(); i++){
				Libro libro = libros.get(i);
				bwFichero.write(libro.toString()); 
				bwFichero.newLine(); 
				numLibros++;
			}
			bwFichero.close();
			
			System.out.println("\nSe ha(n) guardado " + numLibros + " libro(s) en el fichero " + pFichero.getName());
		
		}catch (FileNotFoundException fn ){
			 System.out.println("\nNo se encuentra el fichero");
			 cargado = false;
		}catch (IOException io) {
			 System.out.println("\nError de E/S ");
		}
		return cargado;
	}
		
	/**
	 * se escribe en el fichero DAT que se nos pasa lo que se quiere escribir o sobreescribir 
	 * @param fichero DAT a escribir
	 * @param boolean si se sobreescribe o no
	 * @return devuelve el fichero cargado
	 */

	public boolean escribirFicheroDat(File pFichero, boolean pAniadir){
		
		numLibros = 0;
		
		ObjectOutputStream dataOS = null;
		
		if(!pAniadir) {
			try {
				dataOS = new ObjectOutputStream(new FileOutputStream(pFichero));
			} catch (FileNotFoundException e1) {
				System.out.println("\nNo se encuentra el fichero");
			} catch (IOException e1) {
				System.out.println("\nError de E/S ");
			}
		}
		else{
			try {
				dataOS = new ObjectOutputStream2(new FileOutputStream(pFichero, pAniadir));
			} catch (FileNotFoundException e1) {
				System.out.println("\nNo se encuentra el fichero");
			} catch (IOException e1) {
				System.out.println("\nError de E/S ");
			}
		}
		
		
		for (Libro libro : this.libros){
			
		 	try {
				dataOS.writeObject(libro);
			} catch (IOException e) {
				 System.out.println("\nError de E/S ");
			} 
		 	numLibros++;
		}
		try {
			dataOS.close();
		} catch (IOException e) {
			 System.out.println("\nError de E/S ");
		} 
		 
		System.out.println("\nSe ha(n) guardado " + numLibros + " libro(s) en el fichero " + pFichero.getName());
		return cargado;
	}
	 
	/**
	 * se escribe en el fichero XML que se nos pasa lo que se quiere escribir o sobreescribir 
	 * @param fichero XML a escribir
	 * @param boolean si se sobreescribe o no
	 * @return devuelve el fichero cargado
	 */
	public boolean escribirFicheroXml(File pFichero){
						
		DocumentBuilderFactory  dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dbBuilder = null;
		numLibros = 0;
		
		try {
			dbBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			System.out.println("\nError en la configuracion para la creacion del DocumentBuilder");
		}
		
		Document doc = dbBuilder.newDocument();
		
		Element elementoRaiz = doc.createElement("CATALOGO");
		doc.appendChild(elementoRaiz);
		
		for (Libro libro : this.libros) {
			
			Element eLibro = doc.createElement("LIBRO");
			elementoRaiz.appendChild(eLibro);
			
			Attr attrTitulo = doc.createAttribute("TITULO");
			attrTitulo.setValue(libro.getTitulo());
			eLibro.setAttributeNode(attrTitulo);
			
			Attr attrEditorial = doc.createAttribute("EDITORIAL");
			attrEditorial.setValue(libro.getEditorial());
			eLibro.setAttributeNode(attrEditorial);
			
			Attr attrPaginas = doc.createAttribute("PAGINAS");
			attrPaginas.setValue(Integer.toString(libro.getPaginas()));
			eLibro.setAttributeNode(attrPaginas);
			
			Attr attrAltura = doc.createAttribute("ALTURA");
			attrAltura.setValue(Double.toString(libro.getAltura()));
			eLibro.setAttributeNode(attrAltura);
			
			Attr attrNotas = doc.createAttribute("NOTAS");
			attrNotas.setValue(libro.getNotas());
			eLibro.setAttributeNode(attrNotas);
			
			Attr attrIsbn = doc.createAttribute("ISBN");
			attrIsbn.setValue(libro.getIsbn());
			eLibro.setAttributeNode(attrIsbn);
			
			Attr attrMaterias = doc.createAttribute("MATERIAS");
			attrMaterias.setValue(libro.getMaterias());
			eLibro.setAttributeNode(attrMaterias);	
			
			numLibros++;
		}
		
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = null;
		try {
			transformer = tf.newTransformer();
		} catch (TransformerConfigurationException e) {
			System.out.println("\nError en la configuracion para la transformacion del archivo XML");
		}		
		
		DOMSource source = new DOMSource(doc);
		
		StreamResult result = new StreamResult(pFichero);
		
		try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			System.out.println("\nError en la transformacion del archivo XML");
		}
		
		System.out.println("\nSe ha(n) guardado " + numLibros + " libro(s) en el fichero " + pFichero.getName());
		
		return cargado;
	}
	/**
	 * se escribe en el fichero CSV que se nos pasa lo que se quiere escribir o sobreescribir 
	 * @param fichero CSV a escribir
	 * @param boolean si se sobreescribe o no
	 * @return devuelve el fichero cargado
	 */
	public boolean escribirFicheroCsv(File pFichero, boolean pSobreescribir){

		int numLibros = 0;

		try{
		
			BufferedWriter bwFichero = new BufferedWriter(new FileWriter(pFichero, pSobreescribir));
			
			for (int i = 0; i < this.libros.size(); i++){
				Libro libro = this.libros.get(i);
				bwFichero.write(libro.toString()); 
				bwFichero.newLine(); 
				numLibros++;
			}
			bwFichero.close();
			
			System.out.println("\nSe ha(n) guardado " + numLibros + " libro(s) en el fichero " + pFichero.getName());
		}
		catch (FileNotFoundException fn ){
			 System.out.println("\nNo se encuentra el fichero");
		}
		catch (IOException io) {
			 System.out.println("\nError de E/S ");
		}
		return cargado;
	}
	
	/**
	 * pregunta si se va a querer sobrescribir o no
	 * @param fichero al que añadir los datos
	 * @return true si se quiere añadido false si se quiere sobreescribir
	 */
	public static boolean aniadirDatosFicheroExistente(File pFichero) {

		boolean aniadir = true;
		
		if (pFichero.exists()) {
			
			System.out.println(Menu.mostrarSubmenuSobreescribir());
			switch(MetodosAdicionales.solicitarOpcion(2, 1, "submenuSobreescribir")) {
			
			case 1:
				aniadir = false;
				break;
			case 2: 
				aniadir = true;
				break;
			}
			
		}
		else {
			System.out.println("\nSe ha creado el archivo " + pFichero.getName());
		}
		return aniadir;
	}
	
	/**
	 * pide el nombre del fichero con el que vamos a trabajar
	 * @return el nombre del fichero
	 */
	public static String solicitarNombreFichero(){

		String nombreFichero = "";
		boolean error = true;
		
		do {
			try{
				System.out.print("\nIntroduce el nombre del archivo: ");
				nombreFichero =  Main.sc.next();
				ExcepcionNombreArchivo.comprobarNombreFichero(nombreFichero);
				Main.sc.nextLine();
				error = false;
			}catch(ExcepcionNombreArchivo exNA) {
				
				System.out.println(exNA.getMessage());
			}
			
		}while(error);
		
		return nombreFichero;
	}
}
