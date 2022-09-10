package com.unlimint.java.test;

public class Order {

	private Integer id;
	private Integer orderId;
	private Double amount;
	private String currency;
	private String comment;

	private String filename;
	private String result;
	private Integer line;

	public Order(Integer orderId, Double amount, String currency, String comment, String filename, String result,
			Integer line) {
		super();
		this.orderId = orderId;
		this.amount = amount;
		this.currency = currency;
		this.comment = comment;
		this.filename = filename;
		this.result = result;
		this.line = line;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Integer getLine() {
		return line;
	}

	public void setLine(Integer line) {
		this.line = line;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", orderId=" + orderId + ", amount=" + amount + ", currency=" + currency
				+ ", comment=" + comment + ", filename=" + filename + ", result=" + result + ", line=" + line + "]";
	}

}
