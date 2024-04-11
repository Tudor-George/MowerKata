package com.mowitnow.tondeuse.dto.helpers;

public class MowerDataInputWarper {
	private String fileLine;


	public MowerDataInputWarper(String fileLine) {
	
		this.fileLine = fileLine;
	}
	public MowerDataInputWarper() {
		
		this.fileLine = "";
	}

	public String getFileLine() {
		return fileLine;
	}

	public void setFileLine(String fileLine) {
		this.fileLine = fileLine;
	}

}
