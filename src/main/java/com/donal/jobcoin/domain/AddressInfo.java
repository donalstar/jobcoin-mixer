package com.donal.jobcoin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressInfo {

	private String balance;
	private Transaction[] transactions;

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public Transaction[] getTransactions() {
		return transactions;
	}

	public void setTransactions(Transaction[] transactions) {
		this.transactions = transactions;
	}
}