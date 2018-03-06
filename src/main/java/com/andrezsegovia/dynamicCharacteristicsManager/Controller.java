package com.andrezsegovia.dynamicCharacteristicsManager;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.*;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

public class Controller  implements Initializable{

    private static final String QUERY_DELETE_BASE = "DELETE CARACTERISTICAS_DINAMICAS_O WHERE IDCARACTERISTICA = {0} AND IDTIPOINMUEBLE = {X} AND IDTIPONEGOCIO = {1} AND IDSECCION = {2};";
    private static final String QUERY_INSERT_BASE = "INSERT INTO CARACTERISTICAS_DINAMICAS_O(IDCARACTERISTICA,IDTIPONEGOCIO,IDSECCION,ORDEN,CLASEVALIDACION,DATAMIN,DATAMAX,PLACEHOLDER,OBLIGATORIA,VALORXDEFECTO,PROCESO,IDTIPOINMUEBLE) VALUES({0},{1},{2},{3},{4},{5},{6},{7},{8},{9},{10},{X});";
    		

    private enum IDTIPOINMUEBLE {
    	APARTAMENTO("1"),
    	CASA("2"),
    	OFICINA("3"),
    	LOTEOCASALOTE("4"),
    	CONSULTORIO("5"),
    	LOCALCOMERCIAL("6"),
    	FINCA("7"),
    	BODEGA("8");
    	
    	private String value;
    	
    	IDTIPOINMUEBLE(String value) {
			this.value = value;
		} 
    }
    
    private final String[] FILENAMES = {
    		"INSERTS-TABLE-CARACTERISTICAS-DINAMICAS-O-VENTA.sql",
    		"INSERTS-TABLE-CARACTERISTICAS-DINAMICAS-O-ARRINEDO.sql",
    		"INSERTS-TABLE-CARACTERISTICAS-DINAMICAS-O-VENTA-ARRIENDO.sql"};
    
    private final String PATH_LOCATION_FILES_RESULTS = "/home/asc-dev/Documents/"; 
	
	@Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void importFile(ActionEvent event){
    	
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XLS Files","*.xls","*.xlsx")
        );

        File file = fileChooser.showOpenDialog(null);

        try {
            if(file != null){

                InputStream fileToRead = new FileInputStream(file.toPath().toString());
                Workbook workbook = WorkbookFactory.create(fileToRead);

                String cellValue = "";
                int cellIndex = -1;
                String newQueryDelete = "";
                String newQueryInsert = "";
                Row row;
                Cell cell;
                
                List<String> lines; 
                int queryIndex;
                
				for (int i = 1; i < 4; i++) {
					Sheet sheet = workbook.getSheetAt(i);

	                Iterator rows = sheet.rowIterator();
	                rows.next();
	                lines = new ArrayList<String>();
	                queryIndex = -1;
	                
	                while (rows.hasNext()){
	                    
	                    row = (Row) rows.next();
	                    Iterator cells = row.cellIterator();
	                    
	                    newQueryDelete = QUERY_DELETE_BASE;
	                	newQueryInsert = QUERY_INSERT_BASE;
	                	cellIndex = -1;
	                	
	                	while (cells.hasNext()){

	                		cellIndex++;
	                		                			                    	
	                    	cell = (Cell) cells.next();
	                        
	                        if(cell.getCellTypeEnum().equals(CellType.STRING)) {
	                        	cellValue = cell.getStringCellValue();
	                        }else if(cell.getCellTypeEnum().equals(CellType.NUMERIC)) {
	                        	cellValue = Integer.toString((((int) cell.getNumericCellValue())));
	                        }else  {
	                        	cellValue = "null";
	                        }
	                    	                        
	                        switch (cellIndex) {
							case 0: //IDCARACTERISTICA
								newQueryDelete = newQueryDelete.replace("{0}", "'".concat(cellValue).concat("'"));
								newQueryInsert = newQueryInsert.replace("{0}", "'".concat(cellValue).concat("'"));
								break;
							case 1: //IDTIPONEGOCIO
								newQueryDelete = newQueryDelete.replace("{1}", cellValue);
								newQueryInsert = newQueryInsert.replace("{1}", cellValue);
								break;
							case 2: //IDSECCION
								newQueryDelete = newQueryDelete.replace("{2}", cellValue);
								newQueryInsert = newQueryInsert.replace("{2}", cellValue);
								break;
							case 3: //ORDEN
								newQueryInsert = newQueryInsert.replace("{3}", cellValue);
								break;
							case 4: //CLASEVALIDACION
								if(cellValue.equals("null")) {
									newQueryInsert = newQueryInsert.replace("{4}",cellValue);;
								}else {
									newQueryInsert = newQueryInsert.replace("{4}", "'".concat(cellValue).concat("'"));
								}							
								break;
							case 5: //DATAMIN
								newQueryInsert = newQueryInsert.replace("{5}", cellValue);
								break;
							case 6: //DATAMAX
								newQueryInsert = newQueryInsert.replace("{6}", cellValue);
								break;
							case 7: //PLACEHOLDER
								if(cellValue.equals("null")) {
									newQueryInsert = newQueryInsert.replace("{7}",cellValue);
								}else {
									newQueryInsert = newQueryInsert.replace("{7}", "'".concat(cellValue).concat("'"));
								}
								break;
							case 8: //OBLIGATORIA
								if(cellValue.equals("null")) {
									newQueryInsert = newQueryInsert.replace("{8}",cellValue);;
								}else {
									newQueryInsert = newQueryInsert.replace("{8}", "'".concat(cellValue).concat("'"));
								}
								break;
							case 9: //VALORXDEFECTO
								if(cellValue.equals("null")) {
									newQueryInsert = newQueryInsert.replace("{9}",cellValue);;
								}else {
									newQueryInsert = newQueryInsert.replace("{9}", "'".concat(cellValue).concat("'"));
								}
								break;
							case 10: //PROCESO
								if(cellValue.equals("null")) {
									newQueryInsert = newQueryInsert.replace("{10}",cellValue);;
								}else {
									newQueryInsert = newQueryInsert.replace("{10}", "'".concat(cellValue).concat("'"));
								}
								break;
							case 11: //CASAS
								if(cellValue != null && cellValue.toLowerCase().equals("x")) {
									//lines.add("\nPROMPT QUERY INSERT No " + ++queryIndex);
									//lines.add("PROMPT INSERT CHARACTERISTIC FOR TYPE RELEASTE <<CASA>>");
									//lines.add(newQueryDelete.replace("{X}", IDTIPOINMUEBLE.CASA.value));
									lines.add(newQueryInsert.replace("{X}", IDTIPOINMUEBLE.CASA.value));
								}							
								break;
							case 12: //APARTAMENTOS
								if(cellValue != null && cellValue.toLowerCase().equals("x")) {
									//lines.add("\nPROMPT QUERY INSERT No " + ++queryIndex);
									//lines.add("PROMPT INSERT CHARACTERISTIC FOR TYPE RELEASTE <<APARTAMENTO>>");
									//lines.add(newQueryDelete.replace("{X}", IDTIPOINMUEBLE.APARTAMENTO.value));
									lines.add(newQueryInsert.replace("{X}", IDTIPOINMUEBLE.APARTAMENTO.value));
								}	
								break;
							case 13: //OFICINAS
								if(cellValue != null && cellValue.toLowerCase().equals("x")) {
									//lines.add("\nPROMPT QUERY INSERT No " + ++queryIndex);
									//lines.add("PROMPT INSERT CHARACTERISTIC FOR TYPE RELEASTE <<OFICINA>>");
									//lines.add(newQueryDelete.replace("{X}", IDTIPOINMUEBLE.OFICINA.value));
									lines.add(newQueryInsert.replace("{X}", IDTIPOINMUEBLE.OFICINA.value));
								}	
								break;
							case 14: //BODEGA
								if(cellValue != null && cellValue.toLowerCase().equals("x")) {
									//lines.add("\nPROMPT QUERY INSERT No " + ++queryIndex);
									//lines.add("PROMPT INSERT CHARACTERISTIC FOR TYPE RELEASTE <<BODEGA>>");
									//lines.add(newQueryDelete.replace("{X}", IDTIPOINMUEBLE.BODEGA.value));
									lines.add(newQueryInsert.replace("{X}", IDTIPOINMUEBLE.BODEGA.value));
								}	
								break;
							case 15: //CONSULTORIO
								if(cellValue != null && cellValue.toLowerCase().equals("x")) {
									//lines.add("\nPROMPT QUERY INSERT No " + ++queryIndex);
									//lines.add("PROMPT INSERT CHARACTERISTIC FOR TYPE RELEASTE <<CONSULTORIO>>");
									//lines.add(newQueryDelete.replace("{X}", IDTIPOINMUEBLE.CONSULTORIO.value));
									lines.add(newQueryInsert.replace("{X}", IDTIPOINMUEBLE.CONSULTORIO.value));
								}	
								break;
							case 16: //LOCALCOMERCIAL
								if(cellValue != null && cellValue.toLowerCase().equals("x")) {
									//lines.add("\nPROMPT QUERY INSERT No " + ++queryIndex);
									//lines.add("PROMPT INSERT CHARACTERISTIC FOR TYPE RELEASTE <<LOCAL COMERCIAL>>");
									//lines.add(newQueryDelete.replace("{X}", IDTIPOINMUEBLE.LOCALCOMERCIAL.value));
									lines.add(newQueryInsert.replace("{X}", IDTIPOINMUEBLE.LOCALCOMERCIAL.value));
								}	
								break;
							case 17: //LOTE O CASALOTE
								if(cellValue != null && cellValue.toLowerCase().equals("x")) {
									//lines.add("\nPROMPT QUERY INSERT No " + ++queryIndex);
									//lines.add("PROMPT INSERT CHARACTERISTIC FOR TYPE RELEASTE <<LOTE O CASALOTE>>");
									//lines.add(newQueryDelete.replace("{X}", IDTIPOINMUEBLE.LOTEOCASALOTE.value));
									lines.add(newQueryInsert.replace("{X}", IDTIPOINMUEBLE.LOTEOCASALOTE.value));
								}	
								break;
							case 18: //CASA CAMPESTRE
								if(cellValue != null && cellValue.toLowerCase().equals("x")) {
									//lines.add("\nPROMPT QUERY INSERT No " + ++queryIndex);
									//lines.add("PROMPT INSERT CHARACTERISTIC FOR TYPE RELEASTE <<CASA CAMPESTE O FINA>>");
									//lines.add(newQueryDelete.replace("{X}", IDTIPOINMUEBLE.FINCA.value));
									lines.add(newQueryInsert.replace("{X}", IDTIPOINMUEBLE.FINCA.value));
								}	
								break;
	                        }
	                    }
	                }
	                lines.add("\nCOMMIT;");
	                this.createFile(FILENAMES[i-1],lines);
	                lines.removeAll(lines);
				}
            }   
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    private void createFile(String fileName, List<String> lines) {
    	try {
			Path file = Paths.get(PATH_LOCATION_FILES_RESULTS + fileName);
			Files.write(file, lines, Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
