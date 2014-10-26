package dec.docDecanat.data.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Index {
	private int number;
	private String subType;
	private Date date;
	private Long name;
	private String status;
	
	//public static List<Index> listOrder = new ArrayList<Index>(); удалить
	
	public Index(int number, String subType, Date date, Long name,	String status) {
		super();
		this.number = number;
		this.subType = subType;
		this.date = date;
		this.name = name;
		this.status = status;
	}
	
	public int getNumber() {
		return number;
	}
	
	public void setNumber(int number) {
		this.number = number;
	}
	
	public String getSubType() {
		return subType;
	}
	
	public void setSubType(String subType) {
		this.subType = subType;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public Long getName() {
		return name;
	}
	
	public void setName(Long name) {
		this.name = name;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}

}
