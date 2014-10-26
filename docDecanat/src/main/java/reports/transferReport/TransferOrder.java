package reports.transferReport;

import java.util.ArrayList;
import java.util.List;

public class TransferOrder {
	private String descriptionTitle="123456789023456789034567890wert";
	private String description;
	private String foundation;
	private List<TransferCourse> listTransferCourse = new ArrayList<TransferCourse>();
	
	public String getDescriptionTitle() {
		return descriptionTitle;
	}
	public void setDescriptionTitle(String descriptionTitle) {
		this.descriptionTitle = descriptionTitle;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<TransferCourse> getListTransferCourse() {
		return listTransferCourse;
	}
	public void setListTransferCourse(List<TransferCourse> listTransferCourse) {
		this.listTransferCourse = listTransferCourse;
	}
	public String getFoundation() {
		return foundation;
	}
	public void setFoundation(String foundation) {
		this.foundation = foundation;
	}
	
	
	
}
