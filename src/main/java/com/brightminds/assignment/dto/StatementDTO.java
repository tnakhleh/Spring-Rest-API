package com.brightminds.assignment.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class StatementDTO {
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date dateFrom;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date dateTo;
	private BigDecimal amountFrom;
	private BigDecimal amountTo;
	
	public StatementDTO(Date dateFrom, Date dateTo, BigDecimal amountFrom, BigDecimal amountTo) {
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.amountFrom = amountFrom;
		this.amountTo = amountTo;
	}
	
	public StatementDTO() {
	}

	public Date getDateFrom() {
		return dateFrom;
	}
	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}
	public Date getDateTo() {
		return dateTo;
	}
	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}
	public BigDecimal getAmountFrom() {
		return amountFrom;
	}
	public void setAmountFrom(BigDecimal amountFrom) {
		this.amountFrom = amountFrom;
	}
	public BigDecimal getAmountTo() {
		return amountTo;
	}
	public void setAmountTo(BigDecimal amountTo) {
		this.amountTo = amountTo;
	}
}