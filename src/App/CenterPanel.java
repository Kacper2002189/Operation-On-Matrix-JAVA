package App;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.BadLocationException;

import org.freixas.jcalendar.DateEvent;
import org.freixas.jcalendar.DateListener;
import org.freixas.jcalendar.JCalendarCombo;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

import com.l2fprod.common.swing.JTaskPane;
import com.l2fprod.common.swing.JTaskPaneGroup;

/** 
* Program <code>MyApp</code>
* Klasa <code>CenterPanel</code> definiujaca centralny panel 
* aplikacji zawierajacy glowna funkcjonalnosc aplikacji
* @author Kacper Oborzynski 	
* @version 1.0	01/06/2024
*/

public class CenterPanel extends JPanel implements ActionListener {

    JPanel northPanel, southPanel, centralPanel, operationPanel;

    StatusBar statusBar = null;
    
    JTextField paramTextFieldNum;
    JTextArea textArea;
    JTable dataTable;
    JList<String> opList;
    JButton buttonAdd, buttonClear, buttonFill, buttonSave, buttonCalc, buttonExport;
    JSpinner paramSpinnerRow, paramSpinnerCol;
    JCalendarCombo calendarCombo;
    JFreeChart freeChart;
    MyTableModel model;
    MyListModel listModel;
    private JLabel paramLabelNum, paramLabelRow, paramLabelCol, paramOpList;
    private TitledBorder titledBorder;
    private Border blackBorder;

    private Icon mIconAdd, mIconClear, mIconFill, mIconSave, mIconCalc, mIconPrint, mIconChart;

    String[] colStrings = {"1","2","3","4","5"};
    String[] listStrings = {"Suma elementow", "srednia elementow", "Wartosć max i min"};

    Boolean flag = false;

    /**
	 * Konstruktor bezparametrowy klasy <CODE>CentralPanel<CODE>
	 */
    public CenterPanel(){
        createIcons();
        createGUI();
    }

    /**
	 * Metoda tworzacaca GUI 
	 */
    public void createGUI(){
        this.setLayout(new GridBagLayout()); 
        GridBagConstraints c = new GridBagConstraints();

        northPanel = createNorthPanel();
        centralPanel = createCentralPanel();
        operationPanel = createOperationPanel();
        southPanel = createSouthPanel();

        c.fill = GridBagConstraints.BOTH; //Skalowanie komponentu w poziomie oraz w pionie
        c.anchor = GridBagConstraints.PAGE_START; //Zakotwiczenie komponentu na gorze strony
        c.insets = new Insets(5,5,5,5); //Ustawienie marginesow
        c.weightx = 1; //Dystrybucja dodatkowej poziomej przestrzeni dla komponentu
        c.weighty = 0; //Dystrybucja dodatkowej pionowej przestrzeni dla komponentu
        c.gridx = 0; //Ustawienie w jakiej komorce poziomej znajduje się komponent
        c.gridy = 0; //Ustawienie w jakiej komorce pionowej znajduje się komponent
        this.add(northPanel,c); //Dodanie komponentu z powyższymi modyfikacjami layoutu

        c.anchor = GridBagConstraints.LINE_START; //Zakotwiczenie komponentu po lewej stronie
        c.weightx = 0.9;
        c.weighty = 0.05;
        c.gridx = 0;
        c.gridy = 1;
        this.add(centralPanel,c);
        
        c.gridx = 0;
        c.gridy = 2;
        c.weighty = 0.4;
        c.weightx = 1;
        this.add(operationPanel,c);

        c.anchor = GridBagConstraints.PAGE_END; //Zakotwiczenie komponentu na dole strony
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 3;
        this.add(southPanel,c);
    }

    /**
	 * Metoda tworzaca panel z parametrami
	 */
    public JPanel createNorthPanel(){
        GridLayout lGridLayout = new GridLayout(1,1);
        FlowLayout layout = new FlowLayout();
        JPanel jf = new JPanel();
        JPanel jp1 = new JPanel();
        JPanel jp2 = new JPanel();
        JPanel jp3 = new JPanel();
        SpinnerNumberModel spinnerNumberModelrow = new SpinnerNumberModel(1,1,5,1); //Utworzenie modelu liczbowego dla spinnera i ustawienie wartosci poczatkowej, minimalnej, maksymalnej oraz o ile zwiększy, lub zmniejszy się wartosć wskazana na spinnerze po kliknięciu w strzalki obok pola tekstowego 
        SpinnerNumberModel spinnerNumberModelcol = new SpinnerNumberModel(1,1,5,1);

        paramTextFieldNum = new JTextField(8); //Utworzenie pola tekstowego o szerokosci 8
        paramTextFieldNum.setText("0"); //Ustawienie domyslnego tekstu

        paramSpinnerRow = new JSpinner(spinnerNumberModelrow); //Utworzenie spinnera, w oparciu o utworzony wczesniej model
        paramSpinnerCol = new JSpinner(spinnerNumberModelcol);

        Component mySpinnerEditorCol = paramSpinnerCol.getEditor();
        Component mySpinnerEditorRow = paramSpinnerRow.getEditor();
        JFormattedTextField jftfCol = ((JSpinner.DefaultEditor) mySpinnerEditorCol).getTextField();
        JFormattedTextField jftfRow = ((JSpinner.DefaultEditor) mySpinnerEditorRow).getTextField();
        jftfCol.setColumns(6); //Ustawienie szerokosci pola tekstowego spinnera
        jftfRow.setColumns(6);

        paramLabelNum = new JLabel("Wprowadź liczbę");
        paramLabelRow = new JLabel("Numer wiersza");
        paramLabelCol = new JLabel("Numer kolumny");

        layout.setHgap(5); //Ustawienie poziomego odstępu między komponentami

        jf.setLayout(lGridLayout);

        jp1.setLayout(layout);
        jp2.setLayout(layout);
        jp3.setLayout(layout);

        jp1.add(paramLabelNum);
        jp1.add(paramTextFieldNum);
        jp2.add(paramLabelRow);
        jp2.add(paramSpinnerRow);
        jp3.add(paramLabelCol);
        jp3.add(paramSpinnerCol);

        jf.add(jp1);
        jf.add(jp2);
        jf.add(jp3);

        return jf;
    }

    /**
	 * Metoda tworzaca panel z tabela
	 */
    public JPanel createCentralPanel(){
        JPanel jp = new JPanel();

        GridBagLayout Glayout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;

        jp.setLayout(Glayout);

        buttonAdd = createJButton("Dodaj", mIconAdd);
        buttonClear = createJButton("Wyczysć", mIconClear);
        buttonFill = createJButton("Wypelnij", mIconFill);
        buttonSave = createJButton("Zapisz", mIconSave);

        paramOpList = new JLabel("Obliczenia");
        model = new MyTableModel();
        dataTable = new JTable(model); // Przypisz model do JTable
        JScrollPane sp = new JScrollPane(dataTable); //Umieszczenie tabeli w scroll panelu

        dataTable.setPreferredScrollableViewportSize(dataTable.getPreferredSize()); //Ustawienie preferowanej wielkosci Viewportu jako preferowana wielkosć tabeli
        dataTable.setFillsViewportHeight(true); //Skalowanie tabeli wraz z wysokoscia Viewportu
        dataTable.getTableHeader().setReorderingAllowed(false); //uniemożliwienie przesuwania kolumn

        for (int i=0;i<dataTable.getColumnCount();i++){ //Wyrownanie tekstu w komorkach tabeli do prawej strony 
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            dataTable.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
        }
        
        c.gridheight = 4; //Ustawienie ilosci komorek w kolumnie jaka ma zajmować komponent
        c.gridwidth = 4; //Ustawienie ilosci komorek w wierszu jaka ma zajmować komponent
        c.weightx = 1;
        c.weighty = 0;
        c.insets = new Insets(5,5,5,5);
        jp.add(sp,c);

        c.gridheight = 1;
        c.gridx = 5;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 1;
        jp.add(buttonAdd,c);

        c.gridx = 5;
        c.gridy = 1;
        jp.add(buttonClear,c);

        c.gridx = 5;
        c.gridy = 2;
        jp.add(buttonFill,c);

        c.gridx = 5;
        c.gridy = 3;
        jp.add(buttonSave,c);

        return jp;
    }

    /**
	 * Metoda tworzaca panel z lista operacji i przyciskami funkcjonanymi
	 */
    public JPanel createOperationPanel(){ //Utworzenie panelu zawierajacego listę operacji i przycisk "Oblicz"
        JPanel jp = new JPanel();
        JTaskPane taskPane = new JTaskPane();
        JTaskPaneGroup naviPanel = new JTaskPaneGroup();
        FlowLayout Llayout = new FlowLayout(FlowLayout.LEFT); //Umieszczenie komponentow od lewej strony panelu

        blackBorder = BorderFactory.createLineBorder(Color.lightGray); //Utworzenie obramowania w kolorze jasno-szarym
        titledBorder = BorderFactory.createTitledBorder(blackBorder,"Wybierz operację"); //Utworzenie obramowania z tytulem
        titledBorder.setTitleJustification(TitledBorder.CENTER); //Wyjustowanie tytulu obramowania na srodek

        jp.setLayout(Llayout);

        buttonCalc = createJButton("Oblicz", mIconCalc);
        buttonExport = createJButton("Eksportuj do PDF", null);


        paramOpList = new JLabel("Obliczenia");

        listModel = new MyListModel(listStrings);
		opList = new JList(listModel);
        opList.setBorder(titledBorder); //Ustawienie obramowania
        opList.setBackground(jp.getBackground()); //Ustawienie tla
		opList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		opList.setLayoutOrientation(JList.VERTICAL);

        opList.addListSelectionListener(new ListSelectionListener() { //Wykrywanie wybranej opcji
            public void valueChanged(ListSelectionEvent e) {
            if (opList.getSelectedValue() != null) {
                opList.getSelectedValue();
            }
        }
    });

        naviPanel.setTitle("Panel Nawigacyjny");
        naviPanel.setExpanded(false);
        taskPane.add(naviPanel);
        taskPane.setBackground(jp.getBackground());
        
        naviPanel.add(makeAction("Dodaj", "",
        mIconAdd));
        naviPanel.add(makeAction("Wypelnij", "",
        mIconFill));
        naviPanel.add(makeAction("Wyczysć", "",
        mIconClear));
        naviPanel.add(makeAction("Zapisz", "",
        mIconSave));
        naviPanel.add(makeAction("Drukuj", "",
        mIconPrint));
        naviPanel.add(makeAction("Wykres", "",
        mIconChart));

        calendarCombo = new JCalendarCombo();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        calendarCombo.setDateFormat(dateFormat);

        calendarCombo.addDateListener(new DateListener() {
            @Override
            public void dateChanged(DateEvent event) {
                Date selectedDate = calendarCombo.getDate();
                String formattedDate = dateFormat.format(selectedDate);
                textArea.append("Wybrana data: " + formattedDate + "\n");
                
                if (!flag) {
                    try {
                        int lastLineStart = textArea.getLineStartOffset(textArea.getLineCount() - 2);
                        int lastLineEnd = textArea.getLineEndOffset(textArea.getLineCount() - 2);
                        textArea.replaceRange("", lastLineStart, lastLineEnd);
                    } catch (BadLocationException e) {
                        e.printStackTrace();
                    }
                    flag = true;
                }
            }
            
        });

        Llayout.setHgap(10);

        jp.add(paramOpList);
        jp.add(opList);
        jp.add(buttonCalc);
        jp.add(taskPane);
        jp.add(calendarCombo);
        jp.add(buttonExport);

        return jp;
    }

    /**
	 * Metoda obslugujaca zdarzenia obiektu JTaskPane
	 */
    Action makeAction(String title, String tooltiptext, Icon iconPath) {
        Action action = new AbstractAction(title) {
          public void actionPerformed(ActionEvent e) {
            String s = e.getActionCommand();
            if (s.equals("Dodaj")){
                addValuetoTable();
            }
            else if (s.equals("Wypelnij")){
                model.fillTable(paramTextFieldNum.getText());
            }
            else if (s.equals("Wyczysć")){
                model.setZeroTable();
            }
            else if (s.equals("Zapisz")){
                saveFileForm();
            }
            else if (s.equals("Drukuj")){
                printListForm();
            }
            else if (s.equals("Wykres")){
               createHistogram();
            }
          }
        };
        action.putValue(Action.SMALL_ICON, iconPath);
        action.putValue(Action.SHORT_DESCRIPTION, tooltiptext);
        return action;
      }

    /**
	 * Metoda tworzaca panel z wynikami
	 */
      
    public JPanel createSouthPanel(){ //Utworzenie panelu zawierajacego Pole tekstowe z rezultatem obliczeń
        JPanel jp = new JPanel();
        blackBorder = BorderFactory.createLineBorder(Color.lightGray);
        titledBorder = BorderFactory.createTitledBorder(blackBorder,"Uzyskany rezultat");
        titledBorder.setTitleJustification(TitledBorder.CENTER);

        jp.setBorder(titledBorder); //Ustawienie wczesniej stworzonego obramowania

        textArea = new JTextArea();
        textArea.setLineWrap(true); //Zawijanie tekstu

        jp.setLayout(new BorderLayout());

		jp.add(new JScrollPane(textArea),BorderLayout.CENTER);

        return jp;
    }

    /**
	 * Metoda tworzaca histogram
	 */
    private void createHistogram() {
        double[] data = new double[model.getColumnCount() * model.getRowCount()];

        int index = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                data[index] = (Double) model.getValueAt(i, j);
                index++;
            }
        }

        HistogramDataset dataset = new HistogramDataset();
        dataset.setType(HistogramType.FREQUENCY);
        dataset.addSeries("Histogram", data, 100);

        String plotTitle = "Histogram";
        String xAxisLabel = "Wartosć";
        String yAxisLabel = "Częstosć";

        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = false;
        boolean urls = false;

        JFreeChart chart = ChartFactory.createHistogram(
                plotTitle,
                xAxisLabel,
                yAxisLabel,
                dataset,
                orientation,
                show,
                toolTips,
                urls
        );

        XYPlot plot = (XYPlot) chart.getPlot();
        XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();
        renderer.setBarPainter(new StandardXYBarPainter());

        ChartPanel chartPanel = new ChartPanel(chart);
        JFrame frame = new JFrame("Histogram");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(chartPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
	 * Metoda tworzaca ikony 16 x 16 px
	 */
    private void createIcons() {

		try {
            mIconAdd = createMyIcon("min_add.png");
            mIconClear = createMyIcon("min_clear.png");
            mIconFill = createMyIcon("min_fill.png");
            mIconSave = createMyIcon("min_save.png");
            mIconCalc = createMyIcon("min_avg.png");
            mIconPrint = createMyIcon("min_print.png");
            mIconChart = createMyIcon("min_chart.png");
		}
		catch(Exception e) { 
			System.out.println("ERROR - Blad tworzenia ikon");
            MyLogger.writeLog("INFO","Blad tworzenia ikon");
		}
	}

    /**
	 * Metoda tworzaca obiekt typu Icon
     * @param file zmienna okreslajaca nazwe pliku
	 * @return zwraca obiekt typu Icon
	 */
    public Icon createMyIcon(String file) {
		String name = "/grafika/"+file;
		ImageIcon icon = new ImageIcon(getClass().getResource(name));
		return icon;
	}

    /**
	 * Metoda tworzaca obiekt typu Icon
	 * @param text zmienna okreslajaca tekst przycisku
	 * @param icon zmienna okreslajaca obrazek przypisany do przycisku
	 * @return zwraca utworzony obiekt typu JButton
	 */
    public JButton createJButton(String text, Icon icon) {
		JButton jb = new JButton(text,icon);
		jb.addActionListener(this);
		return jb;
	}

    /**
	 * Metoda obslugujaca zdarzenia akcji
	 */
    public void actionPerformed(ActionEvent e) {
    
        if (e.getSource() == buttonAdd){
            addValuetoTable();
        }
        else if (e.getSource() == buttonClear){
            model.setZeroTable();
        }
        else if (e.getSource() == buttonFill){
            model.fillTable(paramTextFieldNum.getText());
        }
        else if (e.getSource() == buttonSave){
            saveFileForm();
        }
        else if (e.getSource() == buttonExport){
            model.exportToPDF();
        }
        else if (e.getSource() == buttonCalc){
            String selected = opList.getSelectedValue().toString();

            if (selected.indexOf("Suma") != -1){
                Double sum = model.calculateSum();
			    textArea.append("Suma wyrazow tabeli: "+sum+"\n");
            }
            else if (selected.indexOf("srednia") != -1){
                Double avg = model.calculateAverage();
                textArea.append("srednia wyrazow tabeli: "+avg+"\n");
            }
            else if (selected.indexOf("Wartosć") != -1){
                Double min = model.minValueOfTable();
                Double max = model.maxValueOfTable();
                textArea.append("Minimalna wartosć komorki w tabeli: " + String.valueOf(min) + "\nMaksymalna wartosć komorki w tabeli: " + String.valueOf(max) + "\n");
            }
        }

        throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
    }

    /**
	 * Metoda dodajaca zadana wartosc do tabeli
	 */
    private void addValuetoTable(){
        String tfString = paramTextFieldNum.getText();
            Integer col = (Integer) paramSpinnerCol.getValue();
            Integer row = (Integer) paramSpinnerRow.getValue();
            try{
                double textFieldVal = Double.parseDouble(tfString);
                model.setValue(textFieldVal, row-1, col-1);
            }
            catch (NumberFormatException nfe){
                JOptionPane.showMessageDialog(
                    this, "Niewlasciwie wprowadzona liczba. Sproboj ponownie.", "Error", JOptionPane.ERROR_MESSAGE);
            }
    }

    /**
	 * Metoda wyswietlajaca okno zapisu tabeli do pliku .csv
	 */
    private void saveFileForm(){
        JFileChooser fileChooser = new JFileChooser();
            
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Plik formatu CSV", "csv"));
        fileChooser.setAcceptAllFileFilterUsed(false);
        
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION){
            File selectedFile = new File(fileChooser.getSelectedFile()+".csv");
            if (fileChooser.getSelectedFile().getName().endsWith(".csv") == true)
                selectedFile = new File(fileChooser.getSelectedFile().toString());
            SaveFile(selectedFile);
        }    
    }

     /**
	 * Metoda obslugujaca zapis tabeli do pliku .csv
	 */
    private void SaveFile(File file){
        try {
            FileWriter fileWriter;
             
            fileWriter = new FileWriter(file);

            for (int i=0;i<model.getColumnCount();i++){
                if (i<=3)
                    fileWriter.write(model.getColumnName(i)+",");
                else
                    fileWriter.write(model.getColumnName(i));
            }

            fileWriter.write("\n");

            for (int i=0;i<model.getColumnCount();i++){
                for (int j=0;j<model.getRowCount();j++){
                    if (j<=3)
                        fileWriter.write(model.getValueAt(i, j).toString()+",");
                    else
                        fileWriter.write(model.getValueAt(i, j).toString());
                }
                fileWriter.write("\n");
            }
            fileWriter.close();
        } catch (IOException ex) {
            MyLogger.writeLog("INFO","Blad zapisu pliku");
            System.out.println("Blad zapisu pliku...");
        }
         
    }

     /**
	 * Metoda obslugujaca drukowanie
	 */
    private void printListForm(){ //Drukowanie
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
            MyLogger.writeLog("INFO","Blad drukowania");
	   	    System.out.println("Blad drukowania...");
		}
	}

}
