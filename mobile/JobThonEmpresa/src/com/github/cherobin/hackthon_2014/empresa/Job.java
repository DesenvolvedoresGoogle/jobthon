package com.github.cherobin.hackthon_2014.empresa;

import java.io.Serializable;
import java.util.ArrayList;

public class Job implements Serializable {
    
	  String titulo;
	  String sobre;
	  ArrayList<String> habilidades;
	  String area;
	  String cidade;
	  String estado;
	  boolean ativa;
	  ArrayList<String> contratacao;
	  String email;	
	public Job(String titulo, String sobre, ArrayList<String> habilidades,
			String area, String cidade, String estado, boolean ativa,
			ArrayList<String> contratacao, String email) {
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
