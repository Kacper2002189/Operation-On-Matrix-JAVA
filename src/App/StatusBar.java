package App;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/** 
* Program <code>MyApp</code>
* Klasa <code>StatusBar</code> definiujaca pasek statusu 
* @author Kacper Oborzynski 	
* @version 1.0	01/06/2024
*/

public class StatusBar extends JPanel implements ActionListener {
    
    JTextField infoField, statusField;
    private JLabel infoLabel, statusLabel;

    /**
	 * Konstruktor bezparametrowy
	 */
    public StatusBar(){
        createGUI();
    }

    /**
	 * Metoda tworzaca GUI
	 */
    public void createGUI(){
        this.setBackground(new Color(210,210,210)); //Ustawienie koloru RGB
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS)); //Ustawienie komponentów wzdłuż osi X

        infoField = new JTextField("Start aplikacji");
        statusField = new JTextField("Status");

        infoLabel = new JLabel("Info");
        statusLabel = new JLabel("Status");

        infoField.setEditable(false); //uniemożliwienie edycji pól tekstowych
        statusField.setEditable(false);

        this.add(Box.createHorizontalStrut(10));
        this.add(infoLabel);
        this.add(Box.createHorizontalStrut(10));
        this.add(infoField);
        this.add(Box.createHorizontalStrut(10));
        this.add(statusLabel);
        this.add(Box.createHorizontalStrut(10));
        this.add(statusField);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
    }

}
