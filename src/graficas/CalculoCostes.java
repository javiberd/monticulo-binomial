package graficas;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

import mb_costes.MonticuloBinomial;
import mb_costes.MonticuloBinomial.Nodo;

public class CalculoCostes {
	
	private static HashMap<Integer, ArrayList<Long>> costesBorrado = new HashMap<Integer, ArrayList<Long>>();
	private static HashMap<Integer, ArrayList<Long>> costesInsercion = new HashMap<Integer, ArrayList<Long>>();
	private static HashMap<Integer, ArrayList<Long>> costesDecrecerClave = new HashMap<Integer, ArrayList<Long>>();
	private static TreeMap<Integer, Long> costesFinalesBorrado = new TreeMap<Integer, Long>();
	private static TreeMap<Integer, Long> costesFinalesInsercion = new TreeMap<Integer, Long>();
	private static TreeMap<Integer, Long> costesFinalesDecrecerClave = new TreeMap<Integer, Long>();
	
	/**
	 * Calculo de costes al realizar operaciones de insercion y borrado mezcladas
	 */
	public static void calculoDeCostesMezclados(){
		MonticuloBinomial<Integer> monticuloB = new MonticuloBinomial<Integer>();
		
		Random rnd = new Random();
		int n = 0;
		
		/*	Se realizan 1000000 operaciones de las cuales algunas son de borrado y otras
			de insercion, para cada una de ellas capturamos el tamaño de la estructura 
			junto con el coste de la operación
		*/
		for (int i = 0; i < 1000000; i++){
			Integer aux = rnd.nextInt(100);
			int num = rnd.nextInt(12);
			if (n == 0 || num < 7) {
				monticuloB.insertar(aux);
				if (costesInsercion.containsKey(n)){
					costesInsercion.get(n).add(monticuloB.getCoste());
				}
				else {
					ArrayList<Long> array = new ArrayList<Long>();
					array.add(monticuloB.getCoste());
					costesInsercion.put(n, array);
				}
				n++;
			}
			else {
				monticuloB.borraMinimo();
				if (costesBorrado.containsKey(n)){
					costesBorrado.get(n).add(monticuloB.getCoste());
				}
				else {
					ArrayList<Long> array = new ArrayList<Long>();
					array.add(monticuloB.getCoste());
					costesBorrado.put(n, array);
				}
				n--;
			}
			
		}
	}
	
	/**
	 * Calculo de costes de decrecer clave
	 */
	private static void calculoDeCostesDeDecrecerClave(){
		MonticuloBinomial<Integer> monticuloB = new MonticuloBinomial<Integer>();
		ArrayList<Nodo<Integer>> listaDeNodos = new ArrayList<Nodo<Integer>>();
		Random rnd = new Random();
		int n = 0;
		
		/*
		 * Segun se van insertando elementos en la estructura se van decreciendo sus claves
		 * justo después de que ser insertados. Se captura el tamaño y el coste para cada
		 * operación que se realiza.
		 */
		for (int i = 0; i < 170000; i++){
			Integer aux = rnd.nextInt(100000);
			
			monticuloB.insertar(aux);
			listaDeNodos.add(monticuloB.getUltimoInsertado());
			
			int aleatorio = rnd.nextInt(1000);
			Nodo<Integer> ultimo = monticuloB.getUltimoInsertado();
			if (aleatorio < ultimo.getClave()){
				monticuloB.decrecerClave(ultimo, aleatorio);
				if (costesDecrecerClave.containsKey(n)){
					costesDecrecerClave.get(n).add(monticuloB.getCoste());
				}
				else {
					ArrayList<Long> array = new ArrayList<Long>();
					array.add(monticuloB.getCoste());
					costesDecrecerClave.put(n, array);
				}
			}
			n++;
		}
	}
	
	/**
	 * Calculo de los costes finales sacando un promedio de todos los costes capturados para
	 * cada tamaño de la estructura
	 */
	private static void obtencionDeCostesFinales() {
		Iterator<Entry<Integer, ArrayList<Long>>> it;
		ArrayList<Long> costes;
		Entry<Integer, ArrayList<Long>> par;
		int suma;
		
		it = costesInsercion.entrySet().iterator();
		while (it.hasNext()){
			par = it.next();
			costes = par.getValue();
			suma = 0;
			for (int i = 0; i < costes.size(); i++){
				suma += costes.get(i);
			}
			costesFinalesInsercion.put(par.getKey(), (long) (suma / costes.size()));
		}
		
		it = costesBorrado.entrySet().iterator();
		while (it.hasNext()){
			par = it.next();
			costes = par.getValue();
			suma = 0;
			for (int i = 0; i < costes.size(); i++){
				suma += costes.get(i);
			}
			costesFinalesBorrado.put(par.getKey(), (long) (suma / costes.size()));
		}
		
		it = costesDecrecerClave.entrySet().iterator();
		while (it.hasNext()){
			par = it.next();
			costes = par.getValue();
			suma = 0;
			for (int i = 0; i < costes.size(); i++){
				suma += costes.get(i);
			}
			costesFinalesDecrecerClave.put(par.getKey(), (long) (suma / costes.size()));
		}
	}
	
	/**
	 * Se escribe los datos en tres ficheros: borrado.dat, insercion.dat y decrecer-clave.dat
	 * @throws IOException
	 */
	private static void escrituraEnFichero() throws IOException {
		Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("borradoCostes.dat")));
		Iterator<Entry<Integer, Long>> it = costesFinalesBorrado.entrySet().iterator();
		Entry<Integer, Long> par;
		
		while (it.hasNext()){
			par = it.next();
			writer.write(par.getKey() + " " + par.getValue() + "\n");
		}
		writer.close();
		
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("insercionCostes.dat")));
		it = costesFinalesInsercion.entrySet().iterator();	
		while (it.hasNext()){
			par = it.next();
			writer.write(par.getKey() + " " + par.getValue() + "\n");
		}
		writer.close();
		
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("decrecer-claveCostes.dat")));
		it = costesFinalesDecrecerClave.entrySet().iterator();	
		while (it.hasNext()){
			par = it.next();
			writer.write(par.getKey() + " " + par.getValue() + "\n");
		}
		writer.close();
	}
	
	public static void main(String[] args) throws IOException {
		calculoDeCostesMezclados();
		calculoDeCostesDeDecrecerClave();
		obtencionDeCostesFinales();
		escrituraEnFichero();
	}

}
