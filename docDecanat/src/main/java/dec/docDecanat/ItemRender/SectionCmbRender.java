package dec.docDecanat.ItemRender;

import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import dec.docDecanat.data.entity.OrderRule;
import dec.docDecanat.data.entity.Section;

public class SectionCmbRender  implements ComboitemRenderer<OrderRule>{
	public void render(Comboitem cmb, OrderRule or, int id) throws Exception {
		if(or instanceof OrderRule){
			cmb.setLabel(or.getSection().getName());
			cmb.setValue(or);
			System.out.println("sectioncmbrender str 19 ID ORRU"+or.getId());
			}
	}

}
