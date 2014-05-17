package com.github.cherobin.hackthon_2014.empresa;

import java.io.Serializable;
import java.util.ArrayList;

public class Job_com_id implements Serializable {
       long id;
	  String titulo;
	  String sobre;
	  ArrayList<String> habilidades;
	  String area;
	  String cidade;
	  String estado;
	  boolean ativa;
	  ArrayList<String> contratacao;
	  String email;	
 

	public Job_com_id(long id, String titulo, String sobre, ArrayList<String> habilidades,
			String area, String cidade, String estado, boolean ativa,
			ArrayList<String> contratacao, String email) {
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
