package dec.docDecanat.data.entity;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;

public class SectionParameters extends SelectorComposer<Component>{

	int name;					//название оценки
	String simbol;		//знак больше/меньше
	int amount;				//количество оценок
	String measure;			//мера количество (%/единицы)


	String simbolMin;		//знак больше/меньше
	String simbolMax;		//знак больше/меньше
	int amountMinSubject;				//Сколько разрешено долгов
	int amountMaxSubject;				//Сколько разрешено долгов
	int amountSummerSemester;				//За какой период долги
	boolean allsemOrOnlysem;				//определяет как считать количество долгов
	boolean payOrFree;				//если true значит студент бюджетник
	boolean prol; //если true значит пункт продления

	public boolean isPayOrFree() {
		return payOrFree;
	}
	public void setPayOrFree(boolean payOrFree) {
		this.payOrFree = payOrFree;
	}
	
	public boolean isAllsemOrOnlysem() {
		return allsemOrOnlysem;
	}
	public void setAllsemOrOnlysem(boolean allsemOrOnlysem) {
		this.allsemOrOnlysem = allsemOrOnlysem;
	}
	
	public int getName() {
		return name;
	}
	public void setName(int name) {
		this.name = name;
	}
	
	public String getSimbol() {
		return simbol;
	}
	public void setSimbol(String simbol) {
		this.simbol = simbol;
	}
	
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public String getMeasure() {
		return measure;
	}
	public void setMeasure(String measure) {
		this.measure = measure;
	}

	public String getSimbolMin() {
		return simbolMin;
	}
	public void setSimbolMin(String simbolMin) {
		this.simbolMin = simbolMin;
	}
	
	public String getSimbolMax() {
		return simbolMax;
	}
	public void setSimbolMax(String simbolMax) {
		this.simbolMax = simbolMax;
	}
	
	public int getAmountMinSubject() {
		return amountMinSubject;
	}
	public void setAmountMinSubject(int amountMinSubject) {
		this.amountMinSubject = amountMinSubject;
	}
	
	public int getAmountMaxSubject() {
		return amountMaxSubject;
	}
	public void setAmountMaxSubject(int amountMaxSubject) {
		this.amountMaxSubject = amountMaxSubject;
	}
	
	public int getAmountSummerSemester() {
		return amountSummerSemester;
	}
	public void setAmountSummerSemester(int amountSummerSemester) {
		this.amountSummerSemester = amountSummerSemester;
	}

	public boolean isProl() {
		return prol;
	}
	public void setProl(boolean prol) {
		this.prol = prol;
	}
}
