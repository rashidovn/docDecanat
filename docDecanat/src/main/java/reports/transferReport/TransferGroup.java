package reports.transferReport;

import java.util.ArrayList;
import java.util.List;

public class TransferGroup {
	private String groupName;
	private List<TransferStudent> listTransferStudent = new ArrayList<TransferStudent>();
	
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public List<TransferStudent> getListTransferStudent() {
		return listTransferStudent;
	}
	public void setListTransferStudent(List<TransferStudent> listTransferStudent) {
		this.listTransferStudent = listTransferStudent;
	}
	
	
}
