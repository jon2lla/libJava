package modeloTestJUnit5;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import modelo.Menu;

class MenuTest {

	@Test
	void test() {
		@SuppressWarnings("unused")
		Menu menu = new Menu();
		assertEquals("\n  --MENU PRINCIPAL--\n\n "
				+ "1) Leer ficheros\n "
				+ "2) Escribir ficheros\n "
				+ "3) Insertar libro\n "
				+ "4) Mostrar libros en memoria\n "
				+ "5) Borrar memoria\n "
				+ "6) Buscar libro\n "
				+ "7) Mover ficheros\n "
				+ "8) Modificar permisos\n\n "
				+ "0) Salir", Menu.mostrarMenu());
	}
	
	@Test
	void test2() {
		String resultado = Menu.mostrarSubmenu("leer");
		assertEquals(resultado, Menu.mostrarSubmenu("leer"));
	}

	@Test
	void test3() {
		String resultado = Menu.mostrarSubmenu("escribir");
		assertEquals(resultado, Menu.mostrarSubmenu("escribir"));
	}
	
	
	@Test
	void test4() {
		String resultado = Menu.mostrarSubmenuSobreescribir();
		assertEquals(resultado, Menu.mostrarSubmenuSobreescribir());
	}
}
