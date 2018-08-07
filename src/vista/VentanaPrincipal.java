package vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import mb.MonticuloBinomial;
import mb.MonticuloBinomial.Nodo;
import mb.MonticuloVacioException;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.ListSelectionModel;

@SuppressWarnings("serial")
public class VentanaPrincipal extends JFrame {

	private MonticuloBinomial<Integer> monticuloB;
	private List<Nodo<Integer>> nodos;
	private JPanel contentPane;
	private JTextField textFieldInsertar;
	private JTextArea textArea;
	private JTextField textFieldNuevoValor;
	JList<Object> list;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaPrincipal frame = new VentanaPrincipal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VentanaPrincipal() {
		monticuloB = new MonticuloBinomial<Integer>();
		nodos = new ArrayList<Nodo<Integer>>();
		
		setResizable(false);
		setTitle("Mont\u00EDculo Binomial (Demo)");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 493, 404);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		
		textFieldInsertar = new JTextField();
		textFieldInsertar.setBounds(25, 11, 86, 20);
		contentPane.add(textFieldInsertar);
		textFieldInsertar.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(25, 42, 216, 310);
		contentPane.add(scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		//Boton para insertar
		JButton btnNewButton = new JButton("Insertar");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					int elem = Integer.parseInt(textFieldInsertar.getText());
					monticuloB.insertar(elem);
					nodos.add(monticuloB.getUltimoInsertado());
					textFieldInsertar.setText("");
					textArea.setText(monticuloB.toString());
					list.setListData(nodos.toArray());
				}
				catch (NumberFormatException e){
					
				}
				catch (NullPointerException e){
					
				}
			}
		});
		btnNewButton.setBounds(121, 10, 87, 23);
		contentPane.add(btnNewButton);
		contentPane.getRootPane().setDefaultButton(btnNewButton);
		
		//Boton para borrar
		JButton btnNewButton_1 = new JButton("Borrar m\u00EDnimo");
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Borrado de la lista de nodos
				Nodo<Integer> min = monticuloB.getMinimo();
				for (int i = 0; i < nodos.size(); i++){
					if (nodos.get(i) == min){
						nodos.remove(i);
						break;
					}
				}
				//Borrado del monticulo
				try {
					monticuloB.borraMinimo();
				}
				catch (MonticuloVacioException e){
					
				}
				//Actualizacion de interfaz
				textArea.setText(monticuloB.toString());
				list.setListData(nodos.toArray());
			}
		});
		btnNewButton_1.setBounds(218, 10, 109, 23);
		contentPane.add(btnNewButton_1);
		
		//Boton de reset
		JButton btnReset = new JButton("Reset");
		btnReset.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				monticuloB = new MonticuloBinomial<Integer>();
				nodos = new ArrayList<Nodo<Integer>>();
				textArea.setText("");
				list.setListData(nodos.toArray());
			}
		});
		btnReset.setBounds(337, 10, 71, 23);
		contentPane.add(btnReset);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(262, 72, 200, 232);
		contentPane.add(scrollPane_1);
		
		list = new JList<Object>();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane_1.setViewportView(list);
		
		JLabel lblSeleccionarNodoA = new JLabel("Seleccionar clave a decrementar");
		lblSeleccionarNodoA.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblSeleccionarNodoA.setBounds(277, 48, 185, 14);
		contentPane.add(lblSeleccionarNodoA);
		
		textFieldNuevoValor = new JTextField();
		textFieldNuevoValor.setBounds(264, 332, 86, 20);
		contentPane.add(textFieldNuevoValor);
		textFieldNuevoValor.setColumns(10);
		
		//Botón para decrementar
		JButton btnNewButton_2 = new JButton("Decrementar");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					@SuppressWarnings("unchecked")
					Nodo<Integer> nodo = (Nodo<Integer>) list.getSelectedValue();
					Integer nuevoValor = Integer.parseInt(textFieldNuevoValor.getText());
					monticuloB.decrecerClave(nodo, nuevoValor);
					textArea.setText(monticuloB.toString());
					list.setListData(nodos.toArray());
				}
				catch (NumberFormatException e){
					
				}
				catch (NullPointerException e){
					
				}
			}
		});
		btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnNewButton_2.setBounds(360, 331, 102, 23);
		contentPane.add(btnNewButton_2);
		
		JLabel lblNewLabel = new JLabel("Nuevo valor");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblNewLabel.setBounds(272, 315, 73, 14);
		contentPane.add(lblNewLabel);
	}
}
