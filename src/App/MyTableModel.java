package App;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/** 
* Program <code>MyApp</code>
* Klasa <code>MyTableModel</code> definiujaca abstrakcyjny model tabeli 
* @author Kacper Oborzynski 	
* @version 1.0	01/06/2024
*/

public class MyTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;
    private final int countRowTable = 5;
    private final int countColumnTable = 5;
    private final JFrame frame = new JFrame();
    private Double[][] data = new Double[countRowTable][countColumnTable];
    private String[] colName = {"1", "2", "3", "4", "5"};

    public MyTableModel() {
        super();
        setZeroTable();
    }

    @Override
    public int getColumnCount() {
        return countColumnTable;
    }

    @Override
    public int getRowCount() {
        return countRowTable;
    }

    @Override
    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    public Double[][] getValuesTable() {
        return data;
    }

    public String getStringValuesTable() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < countRowTable; i++) {
            for (int j = 0; j < countColumnTable; j++) {
                str.append(data[i][j]).append(" ");
            }
        }
        return str.toString();
    }

    @Override
    public String getColumnName(int col) {
        return colName[col];
    }

    public String[] getColumnNames() {
        return colName;
    }

    public void setValue(Double value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }

    public void setZeroTable() {
        for (int i = 0; i < countRowTable; i++) {
            for (int j = 0; j < countColumnTable; j++) {
                data[i][j] = 0.0;
            }
        }
        fireTableDataChanged();
    }

    public Double calculateSum() {
        Double sum = 0.0;
        for (int i = 0; i < countRowTable; i++) {
            for (int j = 0; j < countColumnTable; j++) {
                sum += data[i][j];
            }
        }
        return sum;
    }

    public Double calculateAverage() {
        Double sum = calculateSum();
        if (sum > 0) {
            return sum / (countRowTable * countColumnTable);
        }
        return 0.0;
    }

    public Double minValueOfTable(){
        Double min = 0.0;
        Double current_min = (Double)getValueAt(0, 0);
        Double valueAt;

        for (int i=0;i<getColumnCount();i++){
            for (int j=0;j<getRowCount();j++){
                valueAt = (Double)getValueAt(j, i);
                if (current_min < valueAt)
                    current_min = valueAt;
                else
                    min = current_min;
            }
        }
        return min;
    }

    public Double maxValueOfTable(){
        Double max = 0.0;
        Double current_max = (Double)getValueAt(0, 0);
        Double valueAt;

        for (int i=0;i<getColumnCount();i++){
            for (int j=0;j<getRowCount();j++){
                valueAt = (Double)getValueAt(j, i);
                if (current_max < valueAt)
                    current_max = valueAt;
                else
                    max = current_max;
            }
        }
        return max;
    }

    public void fillTable(String tfString) {
        Double textFieldVal = Double.parseDouble(tfString);
        for (int i = 0; i < countRowTable; i++) {
            for (int j = 0; j < countColumnTable; j++) {
                setValue(textFieldVal, i, j);
            }
        }
        fireTableDataChanged();
    }

    public void exportToPDF() {
		JFileChooser fc = new JFileChooser();
		// konfiguracja obiektu FileChooser
	    fc.setCurrentDirectory(new File("."));
	    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    fc.setMultiSelectionEnabled(false);
	    // Utworzenie filtra do odczytu tylko plikow pdf
	    FileNameExtensionFilter pdfFilter = new FileNameExtensionFilter(
	    		"pdf document (*.pdf)", "pdf");
	    fc.setFileFilter(pdfFilter);  
		fc.setDialogTitle("Zapisz");
		fc.setSelectedFile(new File("dane.pdf"));
		// otwarcie okna do zapisu w pdf
		int returnVal = fc.showSaveDialog(frame); 
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
	    	  // sprawdzamy rozszerzenie pliku
	    	 String filePath = fc.getSelectedFile().getAbsolutePath();
	    	 if(!filePath.endsWith(".pdf")) {
	    	     filePath += ".pdf";
	    	 }
	    
	    	boolean isSave = saveToPDF(fc.getSelectedFile().getName());
	    	if(isSave) 
	    		JOptionPane.showMessageDialog(
	              		frame,
	              		"Dane tabeli poprawnie zapisano do pliku \n" + filePath,
	                    "Export Result",
	                    JOptionPane.INFORMATION_MESSAGE);
	    	else JOptionPane.showMessageDialog(
              		frame,
              		"Nieudana próba zapisania danych do pliku ",
                    "Export Result",
                    JOptionPane.INFORMATION_MESSAGE);
	    	} // end returnVal
	}

    public boolean saveToPDF(String fileName) {
		Document document = new Document();
		document.setPageSize(PageSize.A4.rotate());
		PdfWriter writer = null;
	    try {
	    	writer = PdfWriter.getInstance(document, 
	    			new FileOutputStream(fileName));
	    	document.open();
	    	// opis tabeli
	    	String tabName = "Tabela z danymi programu MVC Test";
	    	document.add(new Paragraph(tabName)); // opis tabeli
	    	document.add(new Paragraph(" "));	  
	    	
	        // tworzymy obiekt tabeli i justujemy do lewej
	        PdfPTable table = new PdfPTable(getColumnCount());
	        table.setHorizontalAlignment(Element.ALIGN_LEFT);
	        
	        // dodajemy naglowki do tabeli
	        for(int i = 0; i < getColumnCount(); i++) {
	        	PdfPCell cell = new PdfPCell(new Paragraph(
	        			getColumnName(i)));
	        	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        	cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        	cell.setBackgroundColor(new BaseColor(199,218,232));
	        	table.addCell(cell);
	        }
	        
	        // dodajemy dane do tabeli
	        for(int i = 0; i < getRowCount(); i++) {
	        	for(int j = 0; j < getColumnCount(); j++) {
	        		PdfPCell cell = new PdfPCell(new Paragraph(
		        			(String) getValueAt(i,j).toString()));
		        	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        	cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        	table.addCell(cell);
	        	}
	        }
	        
	        document.add(table);
	        return true;
	    } 
	    catch (DocumentException e) {   } 
	    catch (FileNotFoundException e) { }
	    finally {
	    	if(document != null) document.close();
	        if(writer != null) writer.close();
	    }
	    return false;
	}
}
