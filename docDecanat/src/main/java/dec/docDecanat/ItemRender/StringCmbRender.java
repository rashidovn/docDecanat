package dec.docDecanat.ItemRender;

import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;



public class StringCmbRender implements ComboitemRenderer<String>{

	public void render(Comboitem cmb, String str, int sch) throws Exception {
		if(str instanceof String){
			cmb.setValue(str);
			cmb.setLabel(str);
			
		}
		
	}

}
