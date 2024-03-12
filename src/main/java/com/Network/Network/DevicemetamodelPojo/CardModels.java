package com.Network.Network.DevicemetamodelPojo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class CardModels {
	@Id
	private String cardmodels;
	

	public CardModels() {
		super();
	}

	public CardModels(String cardmodels) {
		super();
		this.cardmodels = cardmodels;
	}

	public String getCardmodels() {
		return cardmodels;
	}

	public void setCardmodels(String cardmodels) {
		this.cardmodels = cardmodels;
	}
	

}
