package main;
import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;


public class MainUI extends JFrame implements onActualizaUI{

	private JPanel contentPane;
	private JScrollPane scrollPaneArchivoCargado;
	private JPanel panelArchivoCargado;
	private JPanel panelAlgoritmoGoloso;
	private JScrollPane scrollPaneAlgoritmoGoloso;
	private final String EXTENCION = "json";
	private final String NOMBRE_ARCHIVO_JSON = "fechas.json";
	private int datosCargadosY = 10;
	private int datosGeneradosGolosoY = 10;
	
	private AlgoritmoGolosoMain golosoMain;
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
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\reiva\\Desktop\\Programacion III\\TP3\\icon.png"));
		setTitle("Generador de arbitros por equipos");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
	  
		
		JButton btnGenerarAlgoritmo = new JButton("Generar");
		btnGenerarAlgoritmo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				HashMap<Integer, ArrayList<Partido>> fechas = golosoMain.getFechas();
				resetUIAlgoritmoGoloso();
				golosoMain.generarAlgoritmoGoloso(fechas);
				golosoMain.colocarDatosDeAlgoritmoGolosoUI(fechas);
				
			}
		});
		btnGenerarAlgoritmo.setBounds(311, 676, 89, 23);
		contentPane.add(btnGenerarAlgoritmo);
		
		JButton btnNewButton_1 = new JButton("Abrir Archivo");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirArchivo();
			}

		});
		btnNewButton_1.setBounds(24, 0, 190, 23);
		contentPane.add(btnNewButton_1);
		
		scrollPaneArchivoCargado = new JScrollPane();
		scrollPaneArchivoCargado.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneArchivoCargado.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPaneArchivoCargado.setBounds(24, 105, 263, 546);
		scrollPaneArchivoCargado.setWheelScrollingEnabled(true);
		contentPane.add(scrollPaneArchivoCargado);
		
	    scrollPaneAlgoritmoGoloso = new JScrollPane();
		scrollPaneAlgoritmoGoloso.setWheelScrollingEnabled(true);
		scrollPaneAlgoritmoGoloso.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneAlgoritmoGoloso.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPaneAlgoritmoGoloso.setBounds(311, 105, 448, 546);
		contentPane.add(scrollPaneAlgoritmoGoloso);
		
		JLabel lblNewLabel = new JLabel("Equipos sin arbitro");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setBounds(66, 71, 221, 23);
		contentPane.add(lblNewLabel);
		
		JLabel lblEquiposConArbitros = new JLabel("Equipos con arbitros");
		lblEquiposConArbitros.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblEquiposConArbitros.setBounds(465, 71, 221, 23);
		contentPane.add(lblEquiposConArbitros);
		
		JButton btnGuardarArchivo = new JButton("Guardar Archivo");
		btnGuardarArchivo.setBounds(224, 0, 190, 23);
		contentPane.add(btnGuardarArchivo);
		
		panelArchivoCargado = new JPanel();
		panelArchivoCargado.setLayout(null);
		
		panelAlgoritmoGoloso = new JPanel();
		panelAlgoritmoGoloso.setLayout(null);
		
		inicializarLogicaTP();
		
	}

	 private void inicializarLogicaTP() {
		 Fixture fix = new Fixture();
		// Creamos archivo y fechas por defecto
		 this.golosoMain = new AlgoritmoGolosoMain(this);
		 cargarDatosGuardados(fix);
	}

	private void cargarDatosGuardados(Fixture fix) {
		  new ArchivoJSON(fix.obtenerFechas(), NOMBRE_ARCHIVO_JSON);
		  File file = new File("./"+NOMBRE_ARCHIVO_JSON);
		  this.golosoMain.setFechas(ArchivoJSON.leerArchivo(file.getName()).getFechas());
		  this.golosoMain.colocarDatosDeArchivoUI(golosoMain.getFechas());
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
            golosoMain.colocarDatosDeArchivoUI(golosoMain.getFechas());
        } 
	}

	@Override
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
	
	
	@Override
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
				
					JLabel partidoLabel = new JLabel(
							"<html>" 
							+partidos.get(partido).getLocal() 
							+"<font color=\"green\"> VS </font>"	
							+partidos.get(partido).getVisitante()
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
					panelAlgoritmoGoloso.add(resaltarEquipo);
				}
				datosGeneradosGolosoY = espacioLabelPartidos;
				
				panelAlgoritmoGoloso.setPreferredSize(new Dimension(418, datosGeneradosGolosoY));
				scrollPaneAlgoritmoGoloso.setViewportView(panelAlgoritmoGoloso);
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
}