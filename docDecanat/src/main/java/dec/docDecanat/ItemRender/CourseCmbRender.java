package dec.docDecanat.ItemRender;

import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;

public class CourseCmbRender implements ComboitemRenderer<Integer>{

	public void render(Comboitem cmb, Integer course, int index)
			throws Exception {
		cmb.setValue(course);
		cmb.setLabel(course+"");
	}

}
