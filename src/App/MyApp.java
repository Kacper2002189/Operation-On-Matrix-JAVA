package App;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.l2fprod.common.swing.JTipOfTheDay;
import com.l2fprod.common.swing.tips.DefaultTip;
import com.l2fprod.common.swing.tips.DefaultTipModel;

/** 
* Program <code>MyApp</code>
* Klasa <code>MyApp</code> definiujaca glowne okno aplikacji
* @author Kacper Oborzyński 	 	
* @version 1.0	01/06/2024
*/

public class MyApp extends JFrame implements ActionListener {
    
    private final int WIDTH = 850; //szerokosć okna
    private final int HEIGHT = 650; //wysokosć okna
    private JPanel conPane;
    CenterPanel centerPanel;
    StatusBar statusBar;
    HelpWindow helpWindow;
    AboutWindow aboutWindow;
    MyLogger logger;

    /**
	 * Zmienne tworzace menu
	 */
    JMenuBar menuBar;
    JMenu fileMenu, editMenu, viewMenu, calcMenu, helpMenu;

    JMenuItem  fileSaveMenuItem, filePrintMenuItem, fileExitMenuItem,
                editAddMenuItem, editClearMenuItem, editFillMenuItem,
                calcSumMenuItem, calcAvgMenuItem, calcMaxMinMenuItem,
                helpContextMenuItem, helpInfoMenuItem;

    JCheckBoxMenuItem viewStatusBarMenuItem, viewToolBarMenuItem;

    /**
	 * Zmienne tworzace pasek narzedziowy
	 */
    JToolBar JToolBar;

    JButton btSave, btPrint, btExit, btAdd, btClear, btFill, btSum, btAvg, btMin, btMax, btHelp, btInfo;

    Icon iconSave, iconPrint, iconExit, iconAdd, iconClear, iconFill, iconSum, iconAvg, iconMin, iconMax, iconHelp, iconAbout;

    /**
	 *  definicja etykiet tekstowych 
	 */
    String[] labelMenu = {"Plik","Edycja","Widok","Obliczenia","Pomoc"};

	String[] labelFileMenuItem = {"Zapisz", "Drukuj", "Wyjdź"};
    String[] labelEditMenuItem = {"Dodaj", "Wyzeruj", "Wypelnij"};
    String[] labelViewMenuItem = {"Ukryj pasek statusu", "Ukryj pasek narzedziowy"};
    String[] labelCalcMenuItem = {"Oblicz sume elementow w tablicy", "Oblicz srednia elementow w tablicy", "Wyznacz wartosć maksymalna i minimalna"};
    String[] labelHelpMenuItem = {"Kontekst pomocy", "Informacje o programie"};

    /**
	 *  definicja etykiet opisujacych w tzw. dymkach
	 */
    String[] tooltipMenu = {"Zapisanie rezultatu", "Drukowanie", "Zamkniecie programu",
        "Dodanie wartosci do tablicy", "Wyzerowanie tablicy", "Wypelnienienie tablicy", "Suma elementow tablicy", "srednia elementow w tablicy",
        "Wartosć Minimalna", "Wartosć Maksymalna", "Uruchoienie Pomocy", "Informacje o programie"};

    /**
	 * Konstruktor bezparametrowy klasy <code>MyApp</code>
	 */
    public MyApp() {
        setTitle("MyApp v.1.0.0");
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeWindow();
            }
        });

        Timer timer = new Timer(1000, e -> showTipOfTheDay());
        timer.setRepeats(false);
        timer.start();

        Dimension frameSize = new Dimension(WIDTH,HEIGHT);			
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		if(frameSize.height > screenSize.height) frameSize.height = screenSize.height;
		if(frameSize.width > screenSize.width) frameSize.width = screenSize.width;
		setSize(frameSize);	
		setLocation((screenSize.width-frameSize.width)/2, //Ustawienie okna na srodku ekranu monitora
					(screenSize.height-frameSize.height)/2);

        conPane = (JPanel)this.getContentPane();
		conPane.setLayout(new BorderLayout());

        logger = new MyLogger();

        try {	
	        javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
	            public void run() {
                    createIcons();
	                createMenu();
                    createGUI();
	            }
	        });	
		}
		catch(Exception e) {
			System.out.println("ERROR - Blad podczas tworzenia GUI aplikacji");
            MyLogger.writeLog("INFO","Blad podczas tworzenia GUI aplikacji");
		}
    }

    
    /**
	 * Metoda tworzaca okno dialogowe z zapytaniem o zamkniecie programu
	 */
    public void closeWindow() {
        int value = JOptionPane.showOptionDialog(this,
            "Czy chcesz zamknać aplikacje?",
              "Uwaga",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                null,
                    new String[] {"Tak", "Nie"}, "Tak");
        if (value == JOptionPane.YES_OPTION) {
            dispose();
            MyLogger.writeLog("INFO","Zamkniecie aplikacji");
            System.exit(0);
        }
    }

    /**
	 * Metoda tworzaca ikony 24 x 24 px dla paska narzedziowego
	 */
    private void createIcons() {

		try {
	  		iconSave = createMyIcon("save.png");
			iconPrint = createMyIcon("print.png");
			iconExit = createMyIcon("exit.png");
			iconAdd = createMyIcon("add.png");
			iconClear = createMyIcon("clear.png");
            iconFill = createMyIcon("fill.png");
            iconSum = createMyIcon("lol.png");
            iconAvg = createMyIcon("avg.png");
            iconHelp = createMyIcon("help.jpg");
            iconAbout = createMyIcon("about.jpg");
            iconMin = createMyIcon("min.png");
            iconMax = createMyIcon("max.png");
		}
		catch(Exception e) { 
			System.out.println("ERROR - Blad tworzenia ikon");
            MyLogger.writeLog("INFO","Blad tworzenia ikon");
		}
	}

    /**
	 * Metoda tworzaca okno zawierajace porady dnia
	 */
    private void showTipOfTheDay() {

        DefaultTipModel model = new DefaultTipModel();
        model.add(new DefaultTip("Porada 1", "Uzywaj skrotow klawiszowych, aby oszczedzić czas."));
        model.add(new DefaultTip("Porada 2", "Aby szybko wypelnić cala tabele, uzyj opcji \"Wypelnij\"."));
        model.add(new DefaultTip("Porada 3", "Pamietaj, aby czesto zapisywać swoja prace."));
        model.add(new DefaultTip("Porada 4", "Jesli czegos nie rozumiesz, zajrzyj do \"Pomocy\"."));
        model.add(new DefaultTip("Porada 5", "Po wiecej informacji, odnosnie wersji oraz autora aplikacji, zajrzyj do \"Informacji o programie\"."));
        
        JTipOfTheDay totd = new JTipOfTheDay(model);
        totd.showDialog(this);
    }

    /**
	 * Metoda tworzaca menu okna glownego
	 */
    private void createMenu(){
    
        menuBar = new JMenuBar();

        fileMenu = createJMenu(labelMenu[0], KeyEvent.VK_P, true);
        editMenu = createJMenu(labelMenu[1], KeyEvent.VK_E, true);
        viewMenu = createJMenu(labelMenu[2], KeyEvent.VK_V, true);
        calcMenu = createJMenu(labelMenu[3], KeyEvent.VK_O, true);
        helpMenu = createJMenu(labelMenu[4], KeyEvent.VK_Z, true);	

        fileSaveMenuItem = createJMenuItem(labelFileMenuItem[0], 
	   	    KeyStroke.getKeyStroke(KeyEvent.VK_L,ActionEvent.ALT_MASK),true);
		filePrintMenuItem = createJMenuItem(labelFileMenuItem[1],
			KeyStroke.getKeyStroke(KeyEvent.VK_W,ActionEvent.ALT_MASK),true);
		fileExitMenuItem = createJMenuItem(labelFileMenuItem[2],
			KeyStroke.getKeyStroke(KeyEvent.VK_X,ActionEvent.ALT_MASK),true);

        editAddMenuItem = createJMenuItem(labelEditMenuItem[0], 
	   	    KeyStroke.getKeyStroke(KeyEvent.VK_I,ActionEvent.ALT_MASK),true);
		editClearMenuItem = createJMenuItem(labelEditMenuItem[1],
			KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.ALT_MASK),true);
		editFillMenuItem = createJMenuItem(labelEditMenuItem[2],
			KeyStroke.getKeyStroke(KeyEvent.VK_F,ActionEvent.ALT_MASK),true);
        
        helpContextMenuItem = createJMenuItem(labelHelpMenuItem[0], KeyStroke.getKeyStroke(KeyEvent.VK_H,ActionEvent.ALT_MASK), true);
        helpInfoMenuItem = createJMenuItem(labelHelpMenuItem[1], KeyStroke.getKeyStroke(KeyEvent.VK_I,ActionEvent.ALT_MASK), true);

        calcSumMenuItem = createJMenuItem(labelCalcMenuItem[0],
			KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.ALT_MASK),true);
		calcAvgMenuItem = createJMenuItem(labelCalcMenuItem[1],
			KeyStroke.getKeyStroke(KeyEvent.VK_A,ActionEvent.ALT_MASK),true);
		calcMaxMinMenuItem = createJMenuItem(labelCalcMenuItem[2],
			KeyStroke.getKeyStroke(KeyEvent.VK_M,ActionEvent.ALT_MASK),true);

        viewStatusBarMenuItem = createJCheckBoxMenuItem(labelViewMenuItem[0], false);
        viewToolBarMenuItem = createJCheckBoxMenuItem(labelViewMenuItem[1], false);
        
        fileMenu.add(fileSaveMenuItem);
        fileMenu.add(filePrintMenuItem);
        fileMenu.add(fileExitMenuItem);
        fileMenu.addSeparator();
        editMenu.add(editAddMenuItem);
        editMenu.add(editClearMenuItem);
        editMenu.add(editFillMenuItem);
        viewMenu.add(viewStatusBarMenuItem);
        viewMenu.add(viewToolBarMenuItem);
        calcMenu.add(calcSumMenuItem);
        calcMenu.add(calcAvgMenuItem);
        calcMenu.add(calcMaxMinMenuItem);
        helpMenu.add(helpContextMenuItem);
        helpMenu.add(helpInfoMenuItem);   
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        menuBar.add(calcMenu);
        menuBar.add(helpMenu);
		this.setJMenuBar(menuBar);
    }

    /**
	 * Metoda tworzaca GUI
	 */
    private void createGUI() {

		// Utworzenie paska narzedziowego
		JToolBar = new JToolBar();
		createJToolBar(JToolBar);
        //Utworzenie panelu centralnego
        centerPanel = new CenterPanel();
        centerPanel.setVisible(true);
        //Utworzenie paska statusu
        statusBar = new StatusBar();
        statusBar.setVisible(true);

		conPane.add(JToolBar, BorderLayout.NORTH);
        conPane.add(centerPanel, BorderLayout.CENTER);
        conPane.add(statusBar, BorderLayout.SOUTH);
	}

    /**
	 * Metoda tworzaca obiekt typu JMenu
     * @param name zmienna okreslajaca nazwe 
	 * @param keyEvent zmienna okreslajaca klawisz skrotu
	 * @param enable zmienna logiczna okreslajaca czy menu jest aktywne
	 * @return zwraca utworzony obiekt typu JMenu
	 */
	public JMenu createJMenu(String name, int keyEvent, boolean enable) {
		JMenu jMenu = new JMenu(name);
		jMenu.setMnemonic(keyEvent); //Ustawienie mozliwosci aktywowania elementow menu za pomoca kombinacji klawiszy
		jMenu.setEnabled(enable);
		return jMenu;
    }

    /**
	 * Metoda tworzaca obiekt typu JMenuItem
     * @param name zmienna okreslajaca nazwe
	 * @param icon zmienna okreslajaca icone wyswietlana wraz z nazwa
	 * @param key zmienna okreslajaca klawisze akceleracji dostepu
	 * @param enable zmienna logiczna okreslajaca czy podmenu jest aktywne
     * @return zwraca utworzony obiekt typu JMenuItem
	 */
    public JMenuItem createJMenuItem(String name, KeyStroke key, 
			boolean enable) {
		JMenuItem jMI;
		jMI = new JMenuItem(name);
		jMI.setAccelerator(key); //Ustawienie kombinacji klawiszy, do uruchomienia akcji
        jMI.addActionListener(this); //Ustawienie obslugi zdarzeń
		jMI.setEnabled(enable);
		return jMI; 
	}
    
    /**
	 * Metoda tworzaca obiekt typu JCheckBoxMenuItem
     * @param name zmienna okreslajaca nazwe
    * @param enable zmienna logiczna okreslajaca czy podmenu jest aktywne
    * @return utworzony obiekt typu JCheckBoxMenuItem
	 */
    public JCheckBoxMenuItem createJCheckBoxMenuItem(String name, 
		boolean enable) {
	JCheckBoxMenuItem jcbmi = new JCheckBoxMenuItem(name,enable);
    jcbmi.addActionListener(this); //Ustawienie obslugi zdarzeń
	jcbmi.setEnabled(true);
	return jcbmi;
	}
    
    /**
	 * Metoda tworzaca pasek narzedziowy
     * @param cjtb zmienna typu JToolBar
	 */
    public void createJToolBar(JToolBar cjtb) {
		cjtb.setFloatable(false); //Zapobieganie przesuwaniu sie paska narzedziowego
		cjtb.add(Box.createHorizontalStrut(12)); //Utworzenie struktury poziomej dla przyciskow w pasku narzedziowym
		
		// Utworzenie przyciskow paska narzedziowego
		btSave = createJButtonToolBar(tooltipMenu[0], iconSave, true);
		btPrint = createJButtonToolBar(tooltipMenu[1], iconPrint, true);
		btExit = createJButtonToolBar(tooltipMenu[2], iconExit, true);
        btAdd = createJButtonToolBar(tooltipMenu[3], iconAdd, true);
        btClear = createJButtonToolBar(tooltipMenu[4], iconClear, true);
        btFill = createJButtonToolBar(tooltipMenu[5], iconFill, true);
        btSum = createJButtonToolBar(tooltipMenu[6], iconSum, true);
        btAvg = createJButtonToolBar(tooltipMenu[7], iconAvg, true);
        btMin = createJButtonToolBar(tooltipMenu[8], iconMin, true);
        btMax = createJButtonToolBar(tooltipMenu[9], iconMax, true);
		btHelp = createJButtonToolBar(tooltipMenu[10], iconHelp, true);
		btInfo = createJButtonToolBar(tooltipMenu[11], iconAbout, true);
		
		// dodanie przyciskow do paska narzedziowego
		cjtb.add(btSave);
		cjtb.add(btPrint);
		cjtb.add(btExit);
		cjtb.addSeparator();
        cjtb.add(btAdd);
        cjtb.add(btClear);
        cjtb.add(btFill);
        cjtb.addSeparator();
        cjtb.add(btSum);
        cjtb.add(btAvg);
        cjtb.add(btMin);
        cjtb.add(btMax);
        cjtb.addSeparator();
		cjtb.add(btHelp);
		cjtb.add(btInfo);
	}

    /**
	 * Metoda tworzaca obiekt typu Icon
     * @param file zmienna okreslajaca nazwe pliku
	 * @return zwraca obiekt typu Icon
	 */
    public Icon createMyIcon(String file) {
		String name = "/grafika/"+file; //Relatywna sciezka do plikow graficznych
		ImageIcon icon = new ImageIcon(getClass().getResource(name)); //Pobranie pliku graficzego ze sciezki okreslonej powyzej
		return icon;
	}

    /**
	 * Metoda tworzaca obiekt typu JButton
     * @param tooltip zmienna okreslajaca podpowiedz dla przycisku
	 * @param icon zmienna okreslajaca obrazek przypisany do przycisku
	 * @param enable zmienna logiczna okreslajaca czy przycisk jest aktywny
	 * @return zwraca utworzony obiekt typu JButton
	 */
    public JButton createJButtonToolBar(String tooltip, Icon icon, boolean enable) {
		JButton jb = new JButton("",icon);
		jb.setToolTipText(tooltip); //Wyswietlenie wskazanego tekstu, po najechaniu na przycisk
		jb.addActionListener(this);
		jb.setEnabled(enable);
		return jb;
    }

    public static void main(String[] args) {
        MyApp f = new MyApp();
        MyLogger.writeLog("INFO","Start Aplikacji");
		f.setVisible(true); //Ustawienie widocznosci okna
    }

     /**
	 * Metoda obslugujaca zdarzenia akcji
	 */
    public void actionPerformed(ActionEvent e) { //Implementacja zdarzeń
        if (e.getSource() == btExit || e.getSource() == fileExitMenuItem){
            closeWindow();
            MyLogger.writeLog("INFO","Zamkniecie aplikacji");
        }
        else if (e.getSource() == btAdd || e.getSource() == editAddMenuItem){
            addValuetoTable();
        }
        else if (e.getSource() == btClear || e.getSource() == editClearMenuItem){
            centerPanel.model.setZeroTable();
        }
        else if (e.getSource() == btFill || e.getSource() == editFillMenuItem){
            centerPanel.model.fillTable(centerPanel.paramTextFieldNum.getText());
        }
        else if (e.getSource() == btSum || e.getSource() == calcSumMenuItem){
            Double sum = centerPanel.model.calculateSum();
			centerPanel.textArea.append("Suma wyrazow tabeli: "+sum+"\n");
        }
        else if (e.getSource() == btAvg || e.getSource() == calcAvgMenuItem){
            Double avg = centerPanel.model.calculateAverage();
            centerPanel.textArea.append("srednia wyrazow tabeli: "+avg+"\n");
        }
        else if (e.getSource() == btMin){
            Double min = centerPanel.model.minValueOfTable();
            centerPanel.textArea.append("Minimalna wartosć komorki w tabeli: " + String.valueOf(min)+ "\n");
        }
        else if (e.getSource() == btMax){
            Double max = centerPanel.model.maxValueOfTable();
            centerPanel.textArea.append("Maksymalna wartosć komorki w tabeli: " + String.valueOf(max)+ "\n");
        }
        else if (e.getSource() == calcMaxMinMenuItem){
            Double min = centerPanel.model.minValueOfTable();
            Double max = centerPanel.model.maxValueOfTable();
            centerPanel.textArea.append("Minimalna wartosć komorki w tabeli: " + String.valueOf(min) + "\nMaksymalna wartosć komorki w tabeli: " + String.valueOf(max) + "\n");
        }
        else if (e.getSource() == btHelp || e.getSource() == helpContextMenuItem){
            if(helpWindow != null) helpWindow.setVisible(true);
		 	else {
		 	 	helpWindow = new HelpWindow();
		 		helpWindow.setVisible(true);
		 	}
        }
        else if (e.getSource() == btInfo || e.getSource() == helpInfoMenuItem){
            if(aboutWindow != null) aboutWindow.setVisible(true);
		 	else {
		 		aboutWindow = new AboutWindow();
		 		aboutWindow.setVisible(true);
		 	}
        }
        else if (e.getSource() == btPrint || e.getSource() == filePrintMenuItem){
            printListForm();
        }
        else if (e.getSource() == btSave || e.getSource() == fileSaveMenuItem){
            saveFileForm();
        }
        else if (e.getSource() == viewStatusBarMenuItem){
            Boolean state = viewStatusBarMenuItem.getState(); //Pobranie statusu komponentu
            if (state)
                statusBar.setVisible(false); //Ustawienie widocznosci komponentu
            else statusBar.setVisible(true);
        }
        else if (e.getSource() == viewToolBarMenuItem){
            Boolean state = viewToolBarMenuItem.getState();
            if (state)
                JToolBar.setVisible(false);
            else JToolBar.setVisible(true);
        }
    }

     /**
	 * Metoda dodajaca zadana wartosc do tabeli
	 */
    private void addValuetoTable(){
        String tfString = centerPanel.paramTextFieldNum.getText();
            Integer col = (Integer) centerPanel.paramSpinnerCol.getValue();
            Integer row = (Integer) centerPanel.paramSpinnerRow.getValue();
            try{
                double textFieldVal = Double.parseDouble(tfString);
                centerPanel.model.setValue(textFieldVal, row-1, col-1);
            }
            catch (NumberFormatException nfe){
                JOptionPane.showMessageDialog(
                    this, "Niewlasciwie wprowadzona liczba. Sproboj ponownie.", "Error", JOptionPane.ERROR_MESSAGE);
            }
    }

     /**
	 * Metoda obslugujaca drukowanie
	 */
    private void printListForm(){ 
		try {
		   PrinterJob job = PrinterJob.getPrinterJob(); //Utworzenie zadania drukowania
		   PageFormat pf = new PageFormat(); //Utworzenie formatu drukowania
		   job.pageDialog(pf); //Wyswietlenie okna dialogowego
	   	if(job.printDialog()) { 
		   	job.print(); //drukowanie strony
			   statusBar.infoField.setText("Wydrukowanie Panelu");
		   }
		}
		catch(Exception exc) {
	   	System.out.println("Blad drukowania...");
        MyLogger.writeLog("INFO","Blad drukowania");
		}
	}

     /**
	 * Metoda wyswietlajaca okno zapisu tabeli do pliku .csv
	 */
    private void saveFileForm(){ //formularz do zapisania tabeli
        JFileChooser fileChooser = new JFileChooser(); //Utworzenie formularzu
            
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Plik formatu CSV", "csv")); //Ograniczenie wyboru rozszerzenia do .csv
        fileChooser.setAcceptAllFileFilterUsed(false); //Ukrycie opcji do zapisania pliku w dowolnym formacie
        
        int result = fileChooser.showSaveDialog(this); //Wyswietlenie okna dialogowego do zapisu pliku
        if (result == JFileChooser.APPROVE_OPTION){
            File selectedFile = new File(fileChooser.getSelectedFile()+".csv"); //Dopisanie do nazwy pliku rozszerzenia .csv
            if (fileChooser.getSelectedFile().getName().endsWith(".csv") == true) //Wykrycie, czy nazwa pliku ma juz w sobie dopisane rozszerzenie
                selectedFile = new File(fileChooser.getSelectedFile().toString()); //Jezeli ma, to nie dopisuj rozszerzenia
            SaveFile(selectedFile);
        }    
    }

     /**
	 * Metoda obslugujaca zapis tabeli do pliku .csv
	 */
    private void SaveFile(File file){ //zapisanie tabeli do pliku
        try {
            FileWriter fileWriter;
             
            fileWriter = new FileWriter(file); //Utworzenie bufora, do ktorego zapisujemy informacje

            for (int i=0;i<centerPanel.dataTable.getColumnCount();i++){ //Zapisanie kolumn w pierwszej linijce i oddzielenie symbolem ","
                if (i<=3) //Jezeli kolumna nie jest ostatnia, to oddziel przecinkiem, w przeciwnym wypadku nie oddzielaj 
                    fileWriter.write(centerPanel.dataTable.getColumnName(i)+",");
                else
                    fileWriter.write(centerPanel.dataTable.getColumnName(i));
            }

            fileWriter.write("\n"); //Nowa linia

            for (int i=0;i<centerPanel.dataTable.getColumnCount();i++){ //Zapisanie poszczegolnych komorek tabeli
                for (int j=0;j<centerPanel.dataTable.getRowCount();j++){
                    if (j<=3) //Jezeli wiersz nie jest ostatni, to oddziel przecinkiem, w przeciwnym wypadku nie oddzielaj
                        fileWriter.write(centerPanel.dataTable.getValueAt(i, j).toString()+",");
                    else
                        fileWriter.write(centerPanel.dataTable.getValueAt(i, j).toString());
                }
                fileWriter.write("\n"); //Oddzielenie wierszy nowa linia
            }
            fileWriter.close(); //Zamkniecie bufora tekstowego
        } catch (IOException ex) {
            MyLogger.writeLog("INFO","Blad zapisu pliku");
            System.out.println("Blad zapisu pliku...");
        }
         
    }
}
