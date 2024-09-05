package App;

import java.util.Vector;
import javax.swing.*;

/** 
* Program <code>MyApp</code>
* Klasa <code>MyListModel</code> definiujaca abstrakcyjny model listy 
* @author Kacper Oborzynski 	
* @version 1.0	01/06/2024
*/

public class MyListModel extends AbstractListModel<Object> {

	private static final long serialVersionUID = 1L;
	Vector<Object> v = new Vector<Object>();
	
	public MyListModel() { }
	public MyListModel(Object[] objects) {
		for(int i = 0; i < objects.length; i++) {
			v.addElement(objects[i]);
		}
	}
	
	public Object getElementAt(int index) { return v.elementAt(index); }
	public int getSize() { return v.size(); }
	public void add(int index, Object object) {
		v.insertElementAt(object, index);
		fireIntervalAdded(this,index,index);
	}
}
