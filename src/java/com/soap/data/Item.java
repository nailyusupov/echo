/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soap.data;

/**
 *
 * @author nail yusupov
 */
public class Item {

	private String key, itemClass, itemWeight;
	
	public Item(String k, String iClass, String iWeight){
		key = k;
		itemClass = iClass;
		itemWeight = iWeight;
	}

	public String getItemClass() {
		return itemClass;
	}

	public void setItemClass(String itemClass) {
		this.itemClass = itemClass;
	}

	public String getItemWeight() {
		return itemWeight;
	}

	public void setItemWeight(String itemWeight) {
		this.itemWeight = itemWeight;
	}

	
}
