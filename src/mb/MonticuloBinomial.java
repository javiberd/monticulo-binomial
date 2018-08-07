package mb;

import java.util.Stack;

/**
 * Implementación de un montículo binomial
 * @author Javier Berdecio Trigueros
 */

public class MonticuloBinomial<T extends Comparable<T>> {
	
	private Nodo<T> cabeza;
	
	private Nodo<T> min;
	
	private static enum TipoDeOperacion {BORRADO, INSERCION};
	
	private Nodo<T> ultimoInsertado;
	
	
	public MonticuloBinomial() {
		cabeza = null;
		min = null;
	}
	
	private MonticuloBinomial(Nodo<T> nodo) {
		cabeza = nodo;
		min = nodo;
	}
	
	private MonticuloBinomial(Nodo<T> cabezaIn, Nodo<T> minIn) {
		cabeza = cabezaIn;
		min = minIn;
	}
	
	/**
	 * Inserta un elemento en el montículo
	 */
	public void insertar(T elem){
		Nodo<T> nuevoNodo = new Nodo<T>(elem);
		MonticuloBinomial<T> mb = new MonticuloBinomial<T>(nuevoNodo);
		ultimoInsertado = nuevoNodo;
		union(mb, TipoDeOperacion.INSERCION);
	}

	/**
	 * Devuelve el mínimo del montículo
	 */
	public T minimo(){
		if (cabeza == null){
			throw new MonticuloVacioException();
		}
		else {
			return min.clave;
		}
	}
	
	/**
	 * Borra el mínimo del montículo
	 * @throws MonticuloVacioException
	 */
	public void borraMinimo() throws MonticuloVacioException{
		if (cabeza == null){
			throw new MonticuloVacioException();
		}
		aislarMinimo();
		union(crearMonticuloConHijosDelMinimo(), TipoDeOperacion.BORRADO);
	}
	
	public void decrecerClave(Nodo<T> x, T k){
		if (x == null || k.compareTo(x.clave) >= 0){
			return;
		}
		x.clave = k;

		Nodo<T> y = x;
		Nodo<T> z = y.padre;
		while (z != null && y.clave.compareTo(z.clave) == -1){
			//Cambio de clave
			T aux = y.clave;
			y.clave = z.clave;
			z.clave = aux;
			
			y = z;
			z = y.padre;
		}
		
		if (k.compareTo(min.clave) == -1){
			min = y;
		}
	}
	
	/**
	 * Buscamos el nodo mínimo y lo aislamos del monticulo, haciendo que el hermano
	 * del nodo anterior al mín apunte al hermano derecho del mínimo.
	 */
	private void aislarMinimo(){
		Nodo<T> anterior = null;
		Nodo<T> actual = cabeza;
		while (actual != min){
			anterior = actual;
			actual = actual.hermano;
		}
		if (anterior == null){
			cabeza = actual.hermano;
		}
		else{
			anterior.hermano = actual.hermano;
		}
	}
	
	/**
	 * Se crea un montículo con todos los hijos del mínimo poniendo a todos los hijos directos
	 * en la lista principal y en orden inverso
	 */
	private MonticuloBinomial<T> crearMonticuloConHijosDelMinimo() {
		Stack<Nodo<T>> s = new Stack<Nodo<T>>();
		Nodo<T> hijo = min.hijo;
		Nodo<T> minimoDeLosHijos = min.hijo;
		while (hijo != null){
			s.push(hijo);
			hijo = hijo.hermano;
		}
		Nodo<T> reversoPrimerElemento = null;
		if (!s.isEmpty()){
			reversoPrimerElemento = s.pop();
			Nodo<T> reverso = reversoPrimerElemento;
			while (!s.empty()){
				reverso.hermano = s.pop();
				reverso.padre = null;
				reverso = reverso.hermano;
			}
			reverso.padre = null;
			reverso.hermano = null;
		}
		return new MonticuloBinomial<T>(reversoPrimerElemento, minimoDeLosHijos);
	}

	
	/**
	 * Union multiconjunto de dos montículos. Esta operación está optimizada para lograr un coste
	 * amortizado O(1) en la inserción. En el caso del borrado, se realizan comparaciones del orden
	 * de la log (n) para ver cual es el nuevo mínimo ya que no sabemos el mínimo del montículo this
	 * al haberle extraído el mínimo anteriormente.
	 */
	private void union(MonticuloBinomial<T> mb2, TipoDeOperacion tipo){
		if (cabeza == null && mb2.cabeza == null){
			return;
		}
		
		combinar(mb2);
		
		Nodo<T> anterior = null;
		Nodo<T> actual = cabeza;
		Nodo<T> siguiente = actual.hermano;
		boolean acarreo = true;
		
		if (tipo != TipoDeOperacion.BORRADO && mb2.min.clave.compareTo(this.min.clave) == -1){
			this.min = mb2.min;
		}
		
		if (tipo == TipoDeOperacion.BORRADO){
			min = cabeza;
		}
		
		while (siguiente != null && (tipo != TipoDeOperacion.INSERCION || acarreo)){
			if (actual.grado != siguiente.grado ||
					siguiente.hermano != null && siguiente.hermano.grado == actual.grado){
				anterior = actual;
				actual = siguiente;
				acarreo = false;
			}
			else if (actual.clave.compareTo(siguiente.clave) <= 0){
				if (min == siguiente) min = actual;
				actual.hermano = actual.hermano.hermano;
				siguiente.enlazar(actual);
			}
			else{ 
				if (anterior == null){
					cabeza = siguiente;
				}
				else{
					anterior.hermano = siguiente;
				}
				actual.enlazar(siguiente);
				actual = siguiente;
			}
			if (tipo == TipoDeOperacion.BORRADO && actual.clave.compareTo(min.clave) == -1){
				min = actual;
			}
			siguiente = actual.hermano;
		}
	}
	
	/**
	 * Combina los dos montículos poniendo los nodos con mismo grado contiguamente. El resultado
	 * se devuelve en el montículo this
	 */
	private void combinar(MonticuloBinomial<T> mb){
		
		//Si el monticulo this es vacio, mb pasa a ser el monticulo
		if (cabeza == null){
			cabeza = mb.cabeza;
			min = mb.min;
			return;
		}
		
		//nodo1 va a hacer referencia a los nodos del montículo this
		//nodo2 va a hacer referencia a los nodos del montículo a combinar
		Nodo<T> nodo1 = this.cabeza;
		Nodo<T> nodo2 = mb.cabeza;
		
		if (nodo1 != null && nodo2 != null && nodo1.grado > nodo2.grado){
			Nodo<T> aux = cabeza;
			cabeza = nodo2;
			nodo2 = nodo2.hermano;
			cabeza.hermano = aux;
			nodo1 = cabeza;
		}
		
		//Solo si itera si quedan nodos en el montículo this y en el montículo a combinar
		//En cada iteración se hacen una serie de comparaciones para o bien saltar algún nodo
		//o bien insertar el nodo2 en la posición adecuada
		while (nodo1 != null && nodo2 != null) {
			if (nodo1.grado == nodo2.grado){ 
				Nodo<T> aux = nodo2;
				nodo2 = nodo2.hermano;
				nodo1.agregarHermano(aux);
				nodo1 = nodo1.hermano;
			}
			else if (nodo1.grado < nodo2.grado) {
				if (nodo1.hermano == null || nodo1.hermano.grado > nodo2.grado){
					Nodo<T> aux = nodo2;
					nodo2 = nodo2.hermano;
					nodo1.agregarHermano(aux);
					nodo1 = nodo1.hermano;
				}
				else {
					nodo1 = nodo1.hermano;
				}
			}
		}
	}
	
	public Nodo<T> getMinimo(){
		return min;
	}
	
	public Nodo<T> getUltimoInsertado() {
		return ultimoInsertado;
	}

	public String toString(){
        if (cabeza != null) {
            return cabeza.toString(0, min);
        }
        return null;
	}
	
	static public class Nodo<T extends Comparable<T>> {
		private Nodo<T> padre;
		private Nodo<T> hijo;
		private Nodo<T> hermano;
		
		private int grado;
		private T clave;
		
		public Nodo(T claveIn){
			padre = null;
			hijo = null;
			hermano = null;
			grado = 0;
			clave = claveIn;
		}
		
		/**
		 * Cuelga del nodo2 el nodo this
		 */
		public void enlazar(Nodo<T> nodo2) {
			padre = nodo2;
			hermano = nodo2.hijo;
			nodo2.hijo = this;
			nodo2.grado++;
		}

		/**
		 * Pone entre this y el hermano de this al nodo2
		 */
		public void agregarHermano(Nodo<T> nodo2) {
			Nodo<T> aux = hermano;
			hermano = nodo2;
			nodo2.hermano = aux;
		}
		
		public T getClave(){
			return clave;
		}
		
		public String toString(){
			return clave.toString();
		}
		
		public String toString(int nivel, Nodo<T> min) {
			StringBuilder sb = new StringBuilder();
            Nodo<T> actual = this;
            while (actual != null) {
                for (int i = 0; i < nivel; i++) {
                    sb.append("   ");
                }
                sb.append(actual.clave.toString());
                if (actual == min){
                	sb.append('*');
                }
                sb.append('\n');
                if (actual.hijo != null) {
                	sb.append(actual.hijo.toString(nivel + 1, min));
                }
                actual = actual.hermano;
            }
            return sb.toString();
		}
		
	}
	
}
