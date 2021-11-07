package main;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

import org.eclipse.swt.graphics.RGB;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JTextField;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class MainUI extends JFrame implements KeyListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JScrollPane scrollPaneArchivoCargado;
	private JPanel panelArchivoCargado;
	private JPanel panelAlgoritmoGoloso;
	private JPanel panelEstadisticas;
	private JScrollPane scrollPaneAlgoritmoGoloso;
	private JScrollPane scrollPanelEstadisticas;
	private JButton btnGenerarAlgoritmo;
	private  Pattern patternCantArbitros;
	private final String EXTENCION = "json";
	private final String NOMBRE_ARCHIVO_JSON = "fechas.json";
	private int datosCargadosY = 10;
	private int datosGeneradosGolosoY = 10;
	private int cantidadArbitrosPorDefecto = 13;
	private int datosEstadisticasX = 10;
	private int datosEstadisticasY = 10;

	private AlgoritmoGolosoMain golosoMain;
	private JTextField cantidadArbitrostField;
	private HashMap<Integer, ArrayList<Partido>> fechasUI;
	
	private ArrayList<Integer> partidoLabelIndex;
	private int indexCountPartidoLabel;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainUI frame = new MainUI();
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
	public MainUI() {
		
		this.patternCantArbitros = Pattern.compile("^[0]|\\D"); // true si es cero el primer caracter  y si no es un digito
		 
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\reiva\\Desktop\\Programacion III\\TP3\\icon.png"));
		setTitle("Generador de arbitros por equipos");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
	  
		
	    btnGenerarAlgoritmo = new JButton("Generar");
		btnGenerarAlgoritmo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				 resetUIEstadisticas();
				 mostrarResultadosAlgoritmoGoloso();
			}
		});
		btnGenerarAlgoritmo.setBounds(428, 0, 89, 23);
		contentPane.add(btnGenerarAlgoritmo);
		
		JButton btnNewButton_1 = new JButton("Abrir Archivo");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirArchivo();
			}

		});
		btnNewButton_1.setBounds(0, 0, 190, 23);
		contentPane.add(btnNewButton_1);
		
		scrollPaneArchivoCargado = new JScrollPane();
		scrollPaneArchivoCargado.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneArchivoCargado.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPaneArchivoCargado.setBounds(24, 105, 263, 508);
		scrollPaneArchivoCargado.setWheelScrollingEnabled(true);
		contentPane.add(scrollPaneArchivoCargado);
		
	    scrollPaneAlgoritmoGoloso = new JScrollPane();
		scrollPaneAlgoritmoGoloso.setWheelScrollingEnabled(true);
		scrollPaneAlgoritmoGoloso.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneAlgoritmoGoloso.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPaneAlgoritmoGoloso.setBounds(311, 105, 448, 508);
		contentPane.add(scrollPaneAlgoritmoGoloso);
		
		scrollPanelEstadisticas = new JScrollPane();
		scrollPanelEstadisticas.setWheelScrollingEnabled(true);
		scrollPanelEstadisticas.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPanelEstadisticas.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPanelEstadisticas.setBounds(24, 624, 735, 126);
		contentPane.add(scrollPanelEstadisticas);
		
		JLabel lblNewLabel = new JLabel("Equipos sin arbitro");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setBounds(66, 71, 221, 23);
		contentPane.add(lblNewLabel);
		
		JLabel lblEquiposConArbitros = new JLabel("Equipos con arbitros");
		lblEquiposConArbitros.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblEquiposConArbitros.setBounds(465, 71, 221, 23);
		contentPane.add(lblEquiposConArbitros);
		
		cantidadArbitrostField = new JTextField();
		cantidadArbitrostField.setColumns(1);
		cantidadArbitrostField.addKeyListener(this);
	
		cantidadArbitrostField.setToolTipText("Cantidad de arbitros");
		cantidadArbitrostField.setBounds(311, 1, 86, 20);
		contentPane.add(cantidadArbitrostField);
		cantidadArbitrostField.setText(Integer.toString(cantidadArbitrosPorDefecto));
		
		JLabel lblNewLabelCantidadArbitros = new JLabel("Cantidad Arbitros");
		lblNewLabelCantidadArbitros.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabelCantidadArbitros.setForeground(Color.BLACK);
		lblNewLabelCantidadArbitros.setBounds(200, 4, 104, 14);
		contentPane.add(lblNewLabelCantidadArbitros);
		
	
		
		panelArchivoCargado = new JPanel();
		panelArchivoCargado.setLayout(null);
		
		panelAlgoritmoGoloso = new JPanel();
		panelAlgoritmoGoloso.setLayout(null);
		
		panelEstadisticas = new JPanel();
		panelEstadisticas.setLayout(null);
		
		partidoLabelIndex = new ArrayList<Integer>();
		
		inicializarLogicaTP();
		
	}

	 private void inicializarLogicaTP() {
		 FixtureGenerador fix = new FixtureGenerador();
		// Creamos archivo y fechas por defecto
		 this.golosoMain = new AlgoritmoGolosoMain();
		 cargarDatosGuardados(fix);
	}

	private void cargarDatosGuardados(FixtureGenerador fix) {
		  new ArchivoJSON(fix.obtenerFechas(), NOMBRE_ARCHIVO_JSON);
		  File file = new File("./"+NOMBRE_ARCHIVO_JSON);
		  this.golosoMain.setFechas(ArchivoJSON.leerArchivo(file.getName()).getFechas());
		  colocarDatosDeArchivoUI(golosoMain.getFechas());
	}
	
	private void mostrarResultadosAlgoritmoGoloso() {
		
		new Thread() {
			    public void run() {
			    	partidoLabelIndex.clear();
					indexCountPartidoLabel = 0;
					
					resetUIAlgoritmoGoloso();  
					golosoMain.resetArbitros();
					
					fechasUI = golosoMain.generarAlgoritmoGoloso(
							   golosoMain.getFechas(),
							   cantidadArbitrosPorDefecto);
							
					colocarDatosDeAlgoritmoGolosoUI(fechasUI);
			    };
		 }.start();
		 
	}

	private void abrirArchivo() {
		//Create a file chooser
		final JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File("./"));
		fc.setAcceptAllFileFilterUsed(false);
		
		fc.addChoosableFileFilter(new FileFilter() {
			
			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return "Solo Archivos *.json";
			}
			
			@Override
			public boolean accept(File f) {
				 if (f.isDirectory()) {
				        return true;
				    }

				    String extension = Utils.obtenerExtensionArchivo(f);
				    if (extension != null) {
				        if (extension.equals(EXTENCION)) {
				             return true;
				        } else {
				            return false;
				        }
				    }

				    return false;
			}
		});
		int returnVal = fc.showOpenDialog(contentPane);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			resetUIFechasCargadas();
			resetUIAlgoritmoGoloso();
            File file = fc.getSelectedFile();
            golosoMain.setFechas(ArchivoJSON.leerArchivo(file.getName()).getFechas());
            colocarDatosDeArchivoUI(golosoMain.getFechas());
        } 
	}

	public void colocarfechasDeArchivoUI(int fecha, ArrayList<Partido> partidos) {
		// fecha
		JLabel jlabel = new JLabel(
				"<html>" 
				+"<font color=\"red\">fecha "+ Integer.toString(fecha +1)+"</font>"	
				+"</html>" 
				);
		jlabel.setBounds(10, datosCargadosY, 100, 14);
		panelArchivoCargado.add(jlabel);

		// equipo
		int espacioLabelPartidos = datosCargadosY + 17;
		for(int partido = 0; partido < partidos.size(); partido ++) {
		
			JLabel partidoLabel = new JLabel(
					"<html>" 
					+partidos.get(partido).getLocal() 
					+"<font color=\"green\"> VS </font>"	
					+partidos.get(partido).getVisitante()
					+"</html>"
					);
			partidoLabel.setBounds(20, espacioLabelPartidos, 300, 14);
			espacioLabelPartidos += 17;
			panelArchivoCargado.add(partidoLabel);
			
		}
		datosCargadosY = espacioLabelPartidos;
		
		panelArchivoCargado.setPreferredSize(new Dimension(418, datosCargadosY));
		scrollPaneArchivoCargado.setViewportView(panelArchivoCargado);
		contentPane.updateUI();
		
	}
	
	
	
	public void colocarfechasAlgoritmoGoloso(int fecha, ArrayList<Partido> partidos) {
		
				
				// fechas
				JLabel jlabel = new JLabel(
						"<html>" 
						+"<font color=\"red\">fecha "+ Integer.toString(fecha +1)+"</font>"	
						+"</html>" 
						);
				jlabel.setBounds(10, datosGeneradosGolosoY, 100, 14);
				panelAlgoritmoGoloso.add(jlabel);

				// equipos
				int espacioLabelPartidos = datosGeneradosGolosoY + 17;
				for(int partido = 0; partido < partidos.size(); partido ++) {
					
					String local = partidos.get(partido).getLocal();
					String visitante = partidos.get(partido).getVisitante();
					
					JLabel partidoLabel = new JLabel(
							"<html>" 
							+local
							+"<font color=\"green\"> VS </font>"	
							+visitante
							+"</html>"
							);
					partidoLabel.setBounds(20, espacioLabelPartidos, 250, 14);
					
					JLabel arbitro = new JLabel("<html><font color=\"blue\"> Arbitro: </font>"
							+partidos.get(partido).getArbitro()
							+"</html>");
					arbitro.setBounds(partidoLabel.getBounds().x+partidoLabel.getBounds().width, partidoLabel.getBounds().y, 80, partidoLabel.getBounds().height);
					panelAlgoritmoGoloso.add(partidoLabel);
					panelAlgoritmoGoloso.add(arbitro);
					espacioLabelPartidos += 17;
					
					// ver boton
					JButton resaltarEquipo = new JButton("ver");
					resaltarEquipo.setBounds(arbitro.getBounds().x+arbitro.getBounds().width, arbitro.getBounds().y, 60, 14);
					
					resaltarEquipo.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							resaltarEquipo(local);
							resetUIEstadisticas();
							mostrarEstadisticas(local);
						}
					});
					
					panelAlgoritmoGoloso.add(resaltarEquipo);
					
					partidoLabelIndex.add(indexCountPartidoLabel);
					indexCountPartidoLabel ++;
				}
				datosGeneradosGolosoY = espacioLabelPartidos;
				
				// datosGeneradosGoloso Y es la posicion, del ultimo item.
				panelAlgoritmoGoloso.setPreferredSize(new Dimension(scrollPaneAlgoritmoGoloso.getWidth(), datosGeneradosGolosoY));
				scrollPaneAlgoritmoGoloso.setViewportView(panelAlgoritmoGoloso);
				contentPane.updateUI();
		
	}
	
	protected void mostrarEstadisticas(String equipo) {
		
		HashMap<Integer, Integer> arbitros = new HashMap<Integer, Integer>();
		ArrayList<JLabel> labelsArbitros = new ArrayList<JLabel>();
		
		JLabel pEquipo = new JLabel("Equipo: " +equipo);
		
		pEquipo.setBounds(panelEstadisticas.getX(), panelEstadisticas.getY(), 500, 12);
		JLabel arbitrosTitulo = new JLabel("Numero Arbitros:");
		arbitrosTitulo.setBounds(pEquipo.getX(), pEquipo.getY() + 22, 500, 12);
		pEquipo.setBounds(panelEstadisticas.getX(), panelEstadisticas.getY(), 100, 12);
		
		panelEstadisticas.add(pEquipo);
		panelEstadisticas.add(arbitrosTitulo);
		// inicializar
		int espaciolabelarbitro = pEquipo.getX();
		for(int arbitro = 1; arbitro <= cantidadArbitrosPorDefecto; arbitro++) {
			arbitros.put(arbitro, 0);
			JLabel arbitroLabel = new JLabel(String.valueOf(arbitro));
			arbitroLabel.setBounds(espaciolabelarbitro, arbitrosTitulo.getY() + 20, 50, 12);
			panelEstadisticas.add(arbitroLabel);
			espaciolabelarbitro += arbitroLabel.getWidth();
			labelsArbitros.add(arbitroLabel);
		}
		
		for(Integer fecha: fechasUI.keySet()) {
			for(Partido e: fechasUI.get(fecha)) {
				if(e.getLocal().equals(equipo) || e.getVisitante().equals(equipo)) {
					if(e.getArbitro() != 0)
						arbitros.put(e.getArbitro(), arbitros.get(e.getArbitro())+1);
				}
			}
		}
	
		//System.out.println(equipo);
		 for(int arbitro: arbitros.keySet()) {
			
			 for(JLabel label: labelsArbitros) {
				 
				 if(label.getText().equals(Integer.toString(arbitro))) {
					 JLabel cantVeces = new JLabel(String.valueOf(arbitros.get(arbitro)));
					 cantVeces.setForeground(Color.BLUE);
					 cantVeces.setFont(new Font("Tahoma", Font.BOLD, 14));
					 cantVeces.setBounds(label.getX(), label.getY()+16, label.getWidth(), label.getHeight());
					 panelEstadisticas.add(cantVeces);
	 
				 }
				
				 
			 }
		} 
		
		
		panelEstadisticas.setPreferredSize(new Dimension(espaciolabelarbitro, scrollPanelEstadisticas.getHeight()));
		scrollPanelEstadisticas.setViewportView(panelEstadisticas);
		contentPane.updateUI();
	}

	protected void resaltarEquipo(String local) {
		
		// reset seleccion
		for(Component c: panelAlgoritmoGoloso.getComponents()) {				
			if(c instanceof JLabel) {
				JLabel partido = (JLabel) c; 
				partido.setForeground(Color.DARK_GRAY);
			}
		}
		
		ArrayList<JLabel> jlabels = new ArrayList<JLabel>();
		
		for(Component c: panelAlgoritmoGoloso.getComponents()) {				
				if(c instanceof JLabel) {
					JLabel partido = (JLabel) c; 
					if(partido.getText().contains(local)) {
						jlabels.add(partido);
					}
				}		
		}
		
		for(JLabel label : jlabels) {
			label.setForeground(Color.RED);
		}
	
		contentPane.updateUI();
	}

	private void resetUIAlgoritmoGoloso() {
		panelAlgoritmoGoloso.removeAll();
		scrollPaneAlgoritmoGoloso.setViewportView(null);
		this.datosGeneradosGolosoY = 10;
	}
	private void resetUIFechasCargadas() {
		panelArchivoCargado.removeAll();
		scrollPaneArchivoCargado.setViewportView(null);
		this.datosCargadosY = 10;
	}
	private void resetUIEstadisticas() {
		panelEstadisticas.removeAll();
		scrollPanelEstadisticas.setViewportView(null);
		this.datosEstadisticasX = 10;
	}
	
	public void colocarDatosDeArchivoUI(HashMap<Integer, ArrayList<Partido>> fechas) {
		if(fechas != null && !fechas.isEmpty()) {
			for(int fecha = 0; fecha < fechas.size(); fecha ++) {
			  colocarfechasDeArchivoUI(fecha, fechas.get(fecha));
			}
		} else {
			throw new RuntimeException("Fechas incorrectas");
		}
	}
	
	public void colocarDatosDeAlgoritmoGolosoUI(HashMap<Integer, ArrayList<Partido>> fechas) {
		if(fechas != null && !fechas.isEmpty()) {
			for(int fecha = 0; fecha < fechas.size(); fecha ++) {
			  colocarfechasAlgoritmoGoloso(fecha, fechas.get(fecha));
			}
		} else {
			throw new RuntimeException("Fechas incorrectas");
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	   // TODO no implemented
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO no implemented
	}

	@Override
	public void keyReleased(KeyEvent e) {
		 Matcher disableGenerador = patternCantArbitros.matcher(cantidadArbitrostField.getText());
		 boolean off = disableGenerador.find();
		 if(off || cantidadArbitrostField.getText().isEmpty()) {
			 btnGenerarAlgoritmo.setEnabled(false);
		 } else {
			 btnGenerarAlgoritmo.setEnabled(true);
			 this.cantidadArbitrosPorDefecto = Integer.valueOf(cantidadArbitrostField.getText());
		 }
		
	}
}
