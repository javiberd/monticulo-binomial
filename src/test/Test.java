package test;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;

import mb.MonticuloBinomial;
import mb.MonticuloBinomial.Nodo;

public class Test {
	
	public static void main(String[] args) {
		test1(); //Test de inserción y borrado
		test2(); //Test de inserción y borrado
		test3(); //Test de decrecer clave
	}
	
	/**
	 * Test para probar la inserción y el borrado. Se insertan primer un millón de elementos de manera seguida,
	 * y luego se borran todos
	 */
	public static void test1(){
		MonticuloBinomial<Integer> monticuloB = new MonticuloBinomial<Integer>();
		Random rnd = new Random();
		PriorityQueue<Integer> colaDePrioridad = new PriorityQueue<Integer>();
		Boolean fallo = false;
		
		//Inserción de 1.000.000 elementos en el montículo binomial y en la cola de prioridad
		for (int i = 0; i < 1000000; i++){
			Integer aux = rnd.nextInt(10000000);
			monticuloB.insertar(aux);
			colaDePrioridad.add(aux);
		}
		
		//Para cada iteración se comprueba que el mínimo del montículo binomial sea
		//igual al mínimo de la cola de prioridad, luego se borra el mínimo
		for (int i = 0; i < 1000000; i++){
			if (!monticuloB.minimo().equals(colaDePrioridad.element())){
				fallo = true;
				System.out.println("Error: test1->segundo bucle->primer if");
			}
			monticuloB.borraMinimo();
			colaDePrioridad.remove();
		}
		
		if (!fallo){
			System.out.println("test1 sin fallos");
		}

	}
	
	/**
	 * Test para probar la inserción y el borrado. Se realizan ambas operaciones de manera entremezclada.
	 * Luego se eliminan los elementos que quedan de la etapa anterior.
	 */
	public static void test2(){
		MonticuloBinomial<Integer> monticuloB = new MonticuloBinomial<Integer>();
		Random rnd = new Random();
		PriorityQueue<Integer> colaDePrioridad = new PriorityQueue<Integer>();
		Boolean fallo = false;
		int n = 0;
		
		//Insercion y borrado entremezclados en el montículo binomial y en la cola
		//de prioridad. Si se va a borrar, se comprueba que el mínimo del montículo
		//binomial es igual al de la cola de prioridad
		for (int i = 0; i < 1000000; i++){
			Integer aux = rnd.nextInt(100);
			if (n == 0 || rnd.nextInt(10) > 3) {
				monticuloB.insertar(aux);
				colaDePrioridad.add(aux);
				n++;
			}
			else {
				if (!monticuloB.minimo().equals(colaDePrioridad.element())){
					fallo = true;
					System.out.println("Error: test2->primer bucle->segundo if");
				}
				monticuloB.borraMinimo();
				colaDePrioridad.remove();
				n--;
			}
			
		}
		
		//Se eliminan los elementos restantes y se va comprobando si el mínimo
		//del montículo binomial es igual al de la cola de prioridad
		for (int i = 0; i < n; i++){
			if (!monticuloB.minimo().equals(colaDePrioridad.element())){
				fallo = true;
				System.out.println("Error: test3->segundo bucle->primer if");
			}
			monticuloB.borraMinimo();
			colaDePrioridad.remove();
		}
		
		if (!fallo){
			System.out.println("test3 sin fallos");
		}
		
	}
	
	/**
	 * Testeo de decrecer clave
	 */
	public static void test3(){
		MonticuloBinomial<Integer> monticuloB = new MonticuloBinomial<Integer>();
		PriorityQueue<Integer> colaDePrioridad = new PriorityQueue<Integer>();
		ArrayList<Nodo<Integer>> listaDeNodos = new ArrayList<Nodo<Integer>>();
		Random rnd = new Random();
		Boolean fallo = false;
		
		//Se insertan 1000 elementos en el montículo binomial y la cola de prioridad. Se almacenan
		//los nodos que contienen los elementos insertados en una lista.
		for (int i = 0; i < 10000; i++){
			Integer aux = rnd.nextInt(10000000);
			monticuloB.insertar(aux);
			colaDePrioridad.add(aux);
			listaDeNodos.add(monticuloB.getUltimoInsertado());
		}
		
		//Para cada nodo de la lista se prueba a decrecer su clave. Se hace lo mismo para el montículo
		//binomial y para la cola de prioridad aunque en el último caso solo se emula el comportamiento
		//de la operación ya que no se cuenta con decrecer clave en su abanico de operaciones disponibles
		for (int i = 0; i < 10000; i++){
			int aleatorio = rnd.nextInt(1000);
			if (aleatorio < listaDeNodos.get(i).getClave()){
				colaDePrioridad.remove(listaDeNodos.get(i).getClave());
				colaDePrioridad.add(aleatorio);
			}
			monticuloB.decrecerClave(listaDeNodos.get(i), aleatorio);
		}
		
		//Se van eliminando los elementos del montículo binomial y la cola de prioridad, comprobando
		//previamente que el mínimo del montículo binomial es igual al mínimo de la cola de prioridad
		for (int i = 0; i < 10000; i++){
			if (!monticuloB.minimo().equals(colaDePrioridad.element())){
				fallo = true;
				System.out.println("Error: test4->segundo bucle->primer if");
			}
			monticuloB.borraMinimo();
			colaDePrioridad.remove();
		}
		
		if (!fallo){
			System.out.println("test4 sin fallos");
		}
	}

}
