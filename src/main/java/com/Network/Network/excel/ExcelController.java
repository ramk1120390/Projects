package com.Network.Network.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
public class ExcelController {

    @Autowired
    private DnecPidMatchRepository dnecPidMatchRepository; // Assuming this repository is defined

  /*  @PostMapping("/upload")
    public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Please select a file.", HttpStatus.BAD_REQUEST);
        }

        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = getWorkbook(inputStream, file.getOriginalFilename());
            if (workbook == null) {
                return new ResponseEntity<>("Unsupported file format.", HttpStatus.BAD_REQUEST);
            }
            List<DnecPidMatch> dataList = parseExcel(workbook);
            saveToDatabase(dataList);
            return new ResponseEntity<>("File uploaded successfully.", HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to process file.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Workbook getWorkbook(InputStream inputStream, String fileName) throws IOException {
        if (fileName.toLowerCase().endsWith(".xlsx")) {
            return new XSSFWorkbook(inputStream);
        } else if (fileName.toLowerCase().endsWith(".xls")) {
            return new HSSFWorkbook(inputStream);
        } else {
            return null;
        }
    }

    private List<DnecPidMatch> parseExcel(Workbook workbook) {
        List<DnecPidMatch> dataList = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet
        Iterator<Row> iterator = sheet.iterator();
        if (iterator.hasNext()) {
            iterator.next(); // Skip the header row
        }
        while (iterator.hasNext()) {
            Row currentRow = iterator.next();
            Iterator<Cell> cellIterator = currentRow.iterator();
            DnecPidMatch data = new DnecPidMatch();
            while (cellIterator.hasNext()) {
                Cell currentCell = cellIterator.next();
                int columnIndex = currentCell.getColumnIndex();
                switch (columnIndex) {
                    case 0:
                        data.setPid(getCellValueAsString(currentCell));
                        break;
                    case 1:
                        data.setModel(getCellValueAsString(currentCell));
                        break;
                    case 2:
                        data.setProfile(getCellValueAsString(currentCell));
                        break;
                    case 3:
                        data.setFormfactor(getCellValueAsString(currentCell));
                        break;
                    case 4:
                        data.setSpeed(getCellValueAsString(currentCell));
                        break;
                    case 5:
                        data.setMedium(getCellValueAsString(currentCell));
                        break;
                    case 6:
                        data.setReach(getCellValueAsString(currentCell));
                        break;
                    case 7:
                        data.setBPIPluggable(getCellValueAsString(currentCell));
                        break;
                    case 8:
                        data.setDescription(getCellValueAsString(currentCell));
                        break;
                    case 9:
                        data.setPartnumber(getCellValueAsString(currentCell));
                        break;
                    case 10:
                        data.setRemark(getCellValueAsString(currentCell));
                        break;
                    case 11:
                        data.setWavelength(getCellValueAsString(currentCell));
                        break;
                    case 12:
                        data.setConnection(getCellValueAsString(currentCell));
                        break;
                    // Handle additional columns as needed
                }
            }
            dataList.add(data);
        }
        return dataList;
    }

    private String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    private void saveToDatabase(List<DnecPidMatch> dataList) {
        dnecPidMatchRepository.saveAll(dataList); // Assuming you have implemented this method in your repository
    }

   */

    @PostMapping("/upload")
    public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Please select a file.", HttpStatus.BAD_REQUEST);
        }

        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = getWorkbook(inputStream, file.getOriginalFilename());
            if (workbook == null) {
                return new ResponseEntity<>("Unsupported file format.", HttpStatus.BAD_REQUEST);
            }
            List<BpiData> dataList = parseExcel(workbook);
            saveToDatabase(dataList);
            return new ResponseEntity<>("File uploaded successfully.", HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to process file.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<BpiData> parseExcel(Workbook workbook) {
        List<BpiData> dataList = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet
        Iterator<Row> iterator = sheet.iterator();
        if (iterator.hasNext()) {
            iterator.next(); // Skip the header row
        }
        while (iterator.hasNext()) {
            Row currentRow = iterator.next();
            Iterator<Cell> cellIterator = currentRow.iterator();
            BpiData data = new BpiData();
            while (cellIterator.hasNext()) {
                Cell currentCell = cellIterator.next();
                int columnIndex = currentCell.getColumnIndex();
                switch (columnIndex) {
                    case 0:
                        data.setBpiCard(getCellValueAsString(currentCell));
                        break;
                    case 1:
                        data.setNetboxCard(getCellValueAsString(currentCell));
                        break;
                    case 2:
                        data.setBpiPartnumber(getCellValueAsString(currentCell));
                        break;
                    // Handle additional columns as needed
                }
            }
            dataList.add(data);
        }
        return dataList;
    }

    private String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }
    private Workbook getWorkbook(InputStream inputStream, String fileName) throws IOException {
        if (fileName.toLowerCase().endsWith(".xlsx")) {
            return new XSSFWorkbook(inputStream);
        } else if (fileName.toLowerCase().endsWith(".xls")) {
            return new HSSFWorkbook(inputStream);
        } else {
            return null;
        }
    }
    @Autowired
    private BpiDataRepo bpiDataRepo; // Assuming you have a repository interface for BpiData

    private void saveToDatabase(List<BpiData> dataList) {
        // Save each BpiData entity in the list to the database
        bpiDataRepo.saveAll(dataList);
    }

}
