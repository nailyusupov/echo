/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soap.data;

import java.util.ArrayList;

/**
 *
 * @author nail yusupov
 */
public class QuoteData {
    
    	private String saveQuote, userId, password, originZip, destinationZip, pickupDate, returnMultipleCarriers, numberOfPallets;
	private ArrayList<Item> items;
        private boolean notifyPriorDelivery;

	public QuoteData(){
		setItems(new ArrayList<Item>());
	}
	
	public String getSaveQuote() {
		return saveQuote;
	}

	public void setSaveQuote(String saveQuote) {
		this.saveQuote = saveQuote;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOriginZip() {
		return originZip;
	}

	public void setOriginZip(String originZip) {
		this.originZip = originZip;
	}

	public String getDestinationZip() {
		return destinationZip;
	}

	public void setDestinationZip(String destinationZi) {
		this.destinationZip = destinationZi;
	}

	public String getPickupDate() {
		return pickupDate;
	}

	public void setPickupDate(String pickupDate) {
		this.pickupDate = pickupDate;
	}

	public ArrayList<Item> getItems() {
		return items;
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}

	public String getReturnMultipleCarriers() {
		return returnMultipleCarriers;
	}
	
	public void setReturnMultipleCarriers(String rmc){
		this.returnMultipleCarriers = rmc;
	}

    public void setNotifyPriorDelivery(boolean b) {
        this.notifyPriorDelivery = b;
    }
    
    public boolean getNotifyPriorDelivery(){
        return notifyPriorDelivery;
    }

    /**
     * @return the numberOfPallets
     */
    public String getNumberOfPallets() {
        return numberOfPallets;
    }

    /**
     * @param numberOfPallets the numberOfPallets to set
     */
    public void setNumberOfPallets(String numberOfPallets) {
        this.numberOfPallets = numberOfPallets;
    }
    
}
