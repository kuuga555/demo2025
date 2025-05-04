package com.example.demo.entity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class CurrentPrice{
	private static final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column String shortName; //幣別
    @Column String longName; //幣別中文名稱
    @Column double exchangeRate; //匯率
    @Column Date updDate; //更新時間

    public Integer getId(){ return id; }
    public void setId(Integer id){ this.id = id; }

    public String getShortName(){ return shortName; }
    public void setShortName(String shortName){ this.shortName = shortName; }

    public String getLongName(){ return longName; }
    public void setLongName(String longName){ this.longName = longName; }

	public double getExchangeRate(){ return exchangeRate; }
	public void setExchangeRate(double exchangeRate){ this.exchangeRate = exchangeRate; }
	
	public Date getUpdDate(){ return updDate; }
	public void setUpdDate(Date updDate){ this.updDate = updDate; }


	@Override
    public String toString(){
        return "CurrentPrice{id=[" + id + "], shortName=[" + shortName + "], longName=[" + longName
        	+ "], exchangeRate=[" + exchangeRate + "], updDate=[" + df.format(updDate) + "}";
    }
}
