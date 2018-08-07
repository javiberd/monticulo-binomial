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

import mb.MonticuloBinomial;
import mb.MonticuloBinomial.Nodo;

public class CalculoTiempos {
	
	private static HashMap<Integer, ArrayList<Long>> tiemposBorrado = new HashMap<Integer, ArrayList<Long>>();
	private static HashMap<Integer, ArrayList<Long>> tiemposInsercion = new HashMap<Integer, ArrayList<Long>>();
	private static HashMap<Integer, ArrayList<Long>> tiemposDecrecerClave = new HashMap<Integer, ArrayList<Long>>();
	private static TreeMap<Integer, Long> tiemposFinalesBorrado = new TreeMap<Integer, Long>();
	private static TreeMap<Integer, Long> tiemposFinalesInsercion = new TreeMap<Integer, Long>();
	private static TreeMap<Integer, Long> tiemposFinalesDecrecerClave = new TreeMap<Integer, Long>();
	
	/**
	 * Calculo de tiempos al realizar operaciones de insercion y borrado mezcladas
	 */
	public static void calculoDeTiemposMezclados(){
		MonticuloBinomial<Integer> monticuloB = new MonticuloBinomial<Integer>();
		
		Random rnd = new Random();
		int n = 0;
		
		long startTime, stopTime;
		
		
		/*	Se realizan 1000000 operaciones de las cuales algunas son de borrado y otras
			de insercion, para cada una de ellas capturamos el tamaño de la estructura 
			junto con el tiempo de la operación
		*/
		for (int i = 0; i < 1000000; i++){
			Integer aux = rnd.nextInt(100);
			int num = rnd.nextInt(12);
			if (n == 0 || num < 7) {
				startTime = System.nanoTime();
				monticuloB.insertar(aux);
				stopTime = System.nanoTime();
				if (tiemposInsercion.containsKey(n)){
					tiemposInsercion.get(n).add(stopTime - startTime);
				}
				else {
					ArrayList<Long> array = new ArrayList<Long>();
					array.add(stopTime - startTime);
					tiemposInsercion.put(n, array);
				}
				n++;
			}
			else {
				startTime = System.nanoTime();
				monticuloB.borraMinimo();
				stopTime = System.nanoTime();
				if (tiemposBorrado.containsKey(n)){
					tiemposBorrado.get(n).add(stopTime - startTime);
				}
				else {
					ArrayList<Long> array = new ArrayList<Long>();
					array.add(stopTime - startTime);
					tiemposBorrado.put(n, array);
				}
				n--;
			}
			
		}
	}
	
	/**
	 * Calculo de tiempos de decrecer clave
	 */
	private static void calculoDeTiemposDeDecrecerClave(){
		MonticuloBinomial<Integer> monticuloB = new MonticuloBinomial<Integer>();
		ArrayList<Nodo<Integer>> listaDeNodos = new ArrayList<Nodo<Integer>>();
		Random rnd = new Random();
		int n = 0;
		
		long startTime, stopTime;
		
		/*
		 * Segun se van insertando elementos en la estructura se van decreciendo sus claves
		 * justo después de que ser insertados. Se captura el tamaño y el tiempo para cada
		 * operación que se realiza.
		 */
		for (int i = 0; i < 170000; i++){
			Integer aux = rnd.nextInt(100000);
			
			monticuloB.insertar(aux);
			listaDeNodos.add(monticuloB.getUltimoInsertado());
			
			int aleatorio = rnd.nextInt(1000);
			Nodo<Integer> ultimo = monticuloB.getUltimoInsertado();
			if (aleatorio < ultimo.getClave()){
				startTime = System.nanoTime();
				monticuloB.decrecerClave(ultimo, aleatorio);
				stopTime = System.nanoTime();
				if (tiemposDecrecerClave.containsKey(n)){
					tiemposDecrecerClave.get(n).add(stopTime - startTime);
				}
				else {
					ArrayList<Long> array = new ArrayList<Long>();
					array.add(stopTime - startTime);
					tiemposDecrecerClave.put(n, array);
				}
			}
			n++;
		}
	}
	
	/**
	 * Calculo de los tiempos finales sacando un promedio de todos los tiempos capturados para
	 * cada tamaño de la estructura
	 */
	private static void obtencionDeTiemposFinales() {
		Iterator<Entry<Integer, ArrayList<Long>>> it;
		ArrayList<Long> tiempos;
		Entry<Integer, ArrayList<Long>> par;
		int suma;
		
		it = tiemposInsercion.entrySet().iterator();
		while (it.hasNext()){
			par = it.next();
			tiempos = par.getValue();
			suma = 0;
			for (int i = 0; i < tiempos.size(); i++){
				suma += tiempos.get(i);
			}
			tiemposFinalesInsercion.put(par.getKey(), (long) (suma / tiempos.size()));
		}
		
		it = tiemposBorrado.entrySet().iterator();
		while (it.hasNext()){
			par = it.next();
			tiempos = par.getValue();
			suma = 0;
			for (int i = 0; i < tiempos.size(); i++){
				suma += tiempos.get(i);
			}
			tiemposFinalesBorrado.put(par.getKey(), (long) (suma / tiempos.size()));
		}
		
		it = tiemposDecrecerClave.entrySet().iterator();
		while (it.hasNext()){
			par = it.next();
			tiempos = par.getValue();
			suma = 0;
			for (int i = 0; i < tiempos.size(); i++){
				suma += tiempos.get(i);
			}
			tiemposFinalesDecrecerClave.put(par.getKey(), (long) (suma / tiempos.size()));
		}
	}
	
	/**
	 * Se escribe los datos en tres ficheros: borrado.dat, insercion.dat y decrecer-clave.dat
	 * Se ignoran los primeros 10000 valores de cada mapa debido a que la medición de tiempo
	 * tarda un tiempo en calibrarse.
	 * @throws IOException
	 */
	private static void escrituraEnFichero() throws IOException {
		Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("borradoTiempos.dat")));
		Iterator<Entry<Integer, Long>> it = tiemposFinalesBorrado.entrySet().iterator();
		Entry<Integer, Long> par;
		int i;
		
		i = 100000;
		while (it.hasNext()){
			par = it.next();
			if (i < 0){
				writer.write(par.getKey() + " " + par.getValue() + "\n");
			}
			else {
				i--;
			}
		}
		writer.close();
		
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("insercionTiempos.dat")));
		it = tiemposFinalesInsercion.entrySet().iterator();	
		i = 100000;
		while (it.hasNext()){
			par = it.next();
			if (i < 0){
				writer.write(par.getKey() + " " + par.getValue() + "\n");
			}
			else {
				i--;
			}
		}
		writer.close();
		
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("decrecer-claveTiempos.dat")));
		it = tiemposFinalesDecrecerClave.entrySet().iterator();	
		i = 10000;
		while (it.hasNext()){
			par = it.next();
			if (i < 0){
				writer.write(par.getKey() + " " + par.getValue() + "\n");
			}
			else {
				i--;
			}
		}
		writer.close();
	}
	
	public static void main(String[] args) throws IOException {
		calculoDeTiemposMezclados();
		calculoDeTiemposDeDecrecerClave();
		obtencionDeTiemposFinales();
		escrituraEnFichero();
	}

}
