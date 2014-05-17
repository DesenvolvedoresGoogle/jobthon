package com.github.cherobin.hackthon_2014.VO;

import java.io.Serializable;
import java.util.ArrayList;

public class JobId implements Serializable {
	public long id;
	public String titulo;
	public String sobre;
	public ArrayList<String> habilidades;
	public String area;
	public String cidade;
	public String estado;
	boolean ativa;
	public ArrayList<String> contratacao;
	public String email;

	public JobId(long id, String titulo, String sobre,
			ArrayList<String> habilidades, String area, String cidade,
			String estado, boolean ativa, ArrayList<String> contratacao,
			String email) {
		this.id = id;
		this.titulo = titulo;
		this.sobre = sobre;
		this.habilidades = habilidades;
		this.area = area;
		this.cidade = cidade;
		this.estado = estado;
		this.ativa = ativa;
		this.contratacao = contratacao;
		this.email = email;
	}

}
