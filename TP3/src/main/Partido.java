package main;
import java.util.Comparator;

public class  Partido implements Comparator<Partido> {

	private String local;
	private String visitante;
	private int arbitro;

	public Partido(String local, String visitante, int arbitro) {
		this.local = local;
		this.visitante = visitante;
		this.arbitro = arbitro;
	}

	
	public String getLocal() {
		return local;
	}

	public String getVisitante() {
		return visitante;
	}


	public int getArbitro() {
		return arbitro;
	}


	public void setArbitro(int arbitro) {
		this.arbitro = arbitro;
	}


	@Override
	public int compare(Partido partidoUno, Partido partidoDos) {
		if(partidoUno.getArbitro() == partidoDos.getArbitro()) {
			return -1;
		} 
		return 0;
	}

}