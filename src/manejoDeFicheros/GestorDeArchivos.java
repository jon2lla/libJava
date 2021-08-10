package manejoDeFicheros;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.*;


import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import controlador.MetodosAdicionales;
import modelo.Menu;

public class GestorDeArchivos extends Thread{
	
	private static final String LIB = "libros";
	private static final String TXT = ".txt";
	private static final String DAT = ".dat";
	private static final String XML = ".xml";
	private static final String CSV = ".csv";
	private static final String SUB_FIC = "submenuFicheros";
	private static final String RUTA_LINUX = File.separator + "home" + File.separator + System.getProperty("user.name");
	private static final String RUTA_WINDOWS = "C:" + File.separator + "users" + File.separator + System.getProperty("user.name");
	//private static final String RUTA_WINDOWS ="ficherosDeSalida";
	private static final String FDS ="ficherosDeSalida";
	private static String NOMBRE_FIC;
	private static File FICHERO_SALIDA;
			
	private String rutaFichero;
	private JFileChooser fc;
	private FileNameExtensionFilter filter;
	private int seleccionUsuario;
	private int tipo;
	private boolean defaultFiles;
	private String extension;

	public GestorDeArchivos(int pTipo, String pExtension) {
		
		this.tipo = pTipo;
		this.extension = pExtension;
		
		directorioDeInicio();
		
		if(tipo == 1) {
		
			switch (extension) {
			case ".txt":
				this.filter = new FileNameExtensionFilter(".txt Files", "txt");
				break;
			case ".dat":
				this.filter = new FileNameExtensionFilter(".dat Files", "dat");
				break;
			case ".xml":
				this.filter = new FileNameExtensionFilter(".xml Files", "xml");
				break;
			case ".csv":
				this.filter = new FileNameExtensionFilter(".csv Files", "csv");
				break;
			}
		    this.fc.setFileFilter(this.filter);

		}
		else if(tipo == 2 || tipo == 4) {
			this.fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}
		else if(tipo == 3) {
			this.fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		}
		fc.setPreferredSize(new Dimension(800,500));
	}
	
	@Override
	public void run() {
		int opcionD = 0;
		if(tipo == 1 || tipo == 2) {
			System.out.println(Menu.mostrarSubmenuFicheros());
			opcionD = MetodosAdicionales.solicitarOpcion(2, 1, SUB_FIC);
		}
		else {
			opcionD = 2;
		}
		
	    switch (opcionD) {
	    
	    case 1:
	    	this.defaultFiles = true;
	    	this.rutaFichero = FDS;
	    	break;
	    case 2:
	    	this.defaultFiles = false;
	    	if(fc != null) {
			    if (tipo == 1 || tipo == 3) {
				
				    this.seleccionUsuario = fc.showOpenDialog(null);
			    }
			    else if(tipo == 2 || tipo == 4){
				    this.seleccionUsuario = fc.showSaveDialog(null);
				
				}
			    if(this.seleccionUsuario == JFileChooser.APPROVE_OPTION) {
			      this.rutaFichero = fc.getSelectedFile().getAbsolutePath();
			    }
			    
			    if (this.seleccionUsuario == JFileChooser.CANCEL_OPTION) {
			        System.out.println("\nSE HA CANCELADO LA SELECCION DE ARCHIVOS\n");
			    }	
	    	}
	    }
	}

	public String iniciarHilo(){
		
		this.start();
		return this.rutaFichero;
	}
	
	
	private boolean directorioDeInicio() {
		if(MetodosAdicionales.comprobarOS().equals("Linux")) {
			this.fc = new JFileChooser(RUTA_LINUX);
		}
		else if(MetodosAdicionales.comprobarOS().contains("Windows")){
			this.fc = new JFileChooser(RUTA_WINDOWS);
		}
		return true;
	}
	
	public File generarFichero() {
		if(defaultFiles) {	
				
			switch(tipo) {
			case 1:	
				NOMBRE_FIC = FDS + File.separator + LIB;
				
				switch(extension) {
				case TXT: 
					FICHERO_SALIDA = new File(NOMBRE_FIC + TXT);
					break;
					
				case DAT:
					FICHERO_SALIDA = new File(NOMBRE_FIC + DAT);
					break;
					
				case XML:
					FICHERO_SALIDA = new File(NOMBRE_FIC + XML);
					break;
					
				case CSV:
					FICHERO_SALIDA = new File(NOMBRE_FIC + CSV);
					break;
				}
				break;
			case 2:
				NOMBRE_FIC = FDS + File.separator + EscritorFichero.solicitarNombreFichero();

				switch(extension) {
				case TXT: 
					FICHERO_SALIDA = new File(NOMBRE_FIC + TXT);
					break;
					
				case DAT:
					FICHERO_SALIDA = new File(NOMBRE_FIC + DAT);
					break;
					
				case XML:
					FICHERO_SALIDA = new File(NOMBRE_FIC + XML);
					break;
					
				case CSV:
					FICHERO_SALIDA = new File(NOMBRE_FIC + CSV);
					break;
				}
				break;
			}
		}
		else {
			switch(this.tipo) {
			case 1:
				FICHERO_SALIDA = new File(rutaFichero);
			
				break;
			case 2:
				NOMBRE_FIC = rutaFichero + File.separator + EscritorFichero.solicitarNombreFichero();
				
				switch(extension) {
				case TXT: 
					FICHERO_SALIDA = new File(NOMBRE_FIC + TXT);
					break;
					
				case DAT:
					FICHERO_SALIDA = new File(NOMBRE_FIC + DAT);
					break;
					
				case XML:
					FICHERO_SALIDA = new File(NOMBRE_FIC + XML);
					break;
					
				case CSV:
					FICHERO_SALIDA = new File(NOMBRE_FIC + CSV);
					break;
				}
				break;
			}
		}
		return FICHERO_SALIDA;
	}
	//GETTERS & SETTERS

	public String getRutaFichero() {
		return rutaFichero;
	}
	
	
	public boolean modificarUbicacionFichero(String rutaOrigen, String rutaDestino, File fichero) {
		
		Path pathOrigen = Paths.get(rutaOrigen);
		Path pathDestino = Paths.get(rutaDestino);
		
		try {
			Files.move(pathOrigen, pathDestino, REPLACE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
}
