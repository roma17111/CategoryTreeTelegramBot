package com.bot.categorytree.excel;

import com.bot.categorytree.exeptions.ElementIsAlreadyExistException;
import com.bot.categorytree.exeptions.InvalidExcelException;
import com.bot.categorytree.exeptions.ParentNotFoundException;
import com.bot.categorytree.service.CategoryService;
import lombok.AllArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;

@AllArgsConstructor
public class ExcelUploader implements Callable<Boolean> {


    private final CategoryService categoryService;
    private final File file;
    private final Update update;


    @Override
    public Boolean call() throws Exception {
        if (!upload(file)) {
            return false;
        }
        return true;
    }

    private boolean upload(File file) throws InvalidExcelException, IOException, InvalidFormatException, ElementIsAlreadyExistException, ParentNotFoundException {
        if (!isValidExcel(file)) {
            return false;
        }

        int count = 0;

        try (Workbook workbook = new XSSFWorkbook(file)) {
            for (Row cell : workbook.getSheetAt(0)) {
                for (Cell cell1 : cell) {
                    if (cell1.getRowIndex() > 0) {
                        break;
                    }
                    count++;
                }
            }

            System.out.println(count);

            for (int i = 0; i < count; i++) {
                initRow(workbook, i);
                System.out.println("-----------");
            }
        }
        return true;
    }

    private void initRow(Workbook workbook, int count) throws ElementIsAlreadyExistException, ParentNotFoundException {
        List<String> elements = new ArrayList<>();
        for (Row cell : workbook.getSheetAt(0)) {
            if (cell.getCell(count) == null) {
                continue;
            }
            String text = cell.getCell(count).getStringCellValue();
            elements.add(text);
        }
        addElementsToDb(elements);
    }

    private void addElementsToDb(List<String> elements) throws ElementIsAlreadyExistException, ParentNotFoundException {
        long chatId = update.getMessage().getChatId();
        String parent = elements.get(0);
        long count = categoryService.countAllTrees(chatId);
        if (count == 0) {
            categoryService.addRoot(update, parent);
        }
        for (int i = 1; i < elements.size(); i++) {
            String element = elements.get(i);
            System.out.println(element);
            categoryService.addElement(parent,element,chatId);
        }
    }

    private boolean isValidExcel(File file) throws InvalidExcelException {
        try (Workbook workbook = new XSSFWorkbook(file);) {
            for (Row row : workbook.getSheetAt(0)) {
                for (Cell cell : row) {
                    cell.getStringCellValue();
                }
            }
        } catch (IOException | InvalidFormatException e) {
            throw new InvalidExcelException();
        }
        return true;
    }
}
