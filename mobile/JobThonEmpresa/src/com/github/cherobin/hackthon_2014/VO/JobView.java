package com.github.cherobin.hackthon_2014.VO;

import java.io.Serializable;

public class JobView implements Serializable {

	public long id;
	public String eid;
	public long vid;
	public String cid;
	public float compatibilidade;
	public String nomeEmpresa;
	public String nomeVaga;
	public String nomeCurriculo;
	
	
	 
	public JobView(long id, String eid, long vid, String cid,
			float compatibilidade, String nomeEmpresa, String nomeVaga,
			String nomeCurriculo) {
		super();
		this.id = id;
		this.eid = eid;
		this.vid = vid;
		this.cid = cid;
		this.compatibilidade = compatibilidade;
		this.nomeEmpresa = nomeEmpresa;
		this.nomeVaga = nomeVaga;
		this.nomeCurriculo = nomeCurriculo;
	}
 
	
	
}
