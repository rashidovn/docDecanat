package dec.docDecanat.ItemRender;

import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import dec.docDecanat.data.entity.Curriculum;
import dec.docDecanat.data.entity.Group;

public class CurriculumListboxRender implements ListitemRenderer<Curriculum>{


	public void render(Listitem li, Curriculum curri, int index)
			throws Exception {
		String groups = "";
		for (int i = 0; i < curri.getGroups().size(); i++) {
			groups +=((Group) curri.getGroups().toArray()[i]).getName();
		}
		if(curri instanceof Curriculum){
			li.setLabel(curri.getSpecialitytitle() + "("+groups+")");

		}
	}
}