package dec.docDecanat.pageControllers;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

public class CtrSetting  extends SelectorComposer<Component>{

	@Wire
	Button settingRule;
	@Wire
	Button settingDate;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	}

	@Listen("onClick = #settingRule")
	public void rulesMenu(){
		Window window = (Window)Executions.createComponents("rulesMenu.zul", null, null);
		window.doModal();
	}
	@Listen("onClick = #settingDate")
	public void date(){
		Window window = (Window)Executions.createComponents("setDate.zul", null, null);
		window.doModal();
	}
}
