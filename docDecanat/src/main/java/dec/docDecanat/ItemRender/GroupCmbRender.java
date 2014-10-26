package dec.docDecanat.ItemRender;

import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;

import dec.docDecanat.data.entity.GroupSemester;
import dec.docDecanat.data.entity.OrderRule;

public class GroupCmbRender implements ComboitemRenderer<GroupSemester>{

	public void render(Comboitem cmb, GroupSemester gs, int index) throws Exception {
		
		if(gs instanceof GroupSemester){
			cmb.setValue(gs);
			cmb.setLabel(gs.getGroup().getName());
			
		}
	}

}
