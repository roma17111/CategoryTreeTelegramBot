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

/**
 * Класс для обработки
 * выгруженного excel document
 * пользователем в тг бот для
 * изменений в БД
 */

@AllArgsConstructor
public class ExcelUploader {


    private final CategoryService categoryService;
    private final File file;
    private final Update update;




    /**
     * Метод с основной логикой по
     * обработке выгруженного документа
     * @param file Метод принимает преобразованный файл после выгрузки в бот
     * @return результат выгрузки
     * @throws InvalidExcelException Некорректный документ
     * @throws IOException Исключение при работе с потоками ввода вывода
     * @throws InvalidFormatException некорректный формат документа
     * @throws ElementIsAlreadyExistException элемент уже существует в базе
     * @throws ParentNotFoundException Корневой элемент не найден в базе
     */

    public boolean upload(File file) throws InvalidExcelException, IOException, InvalidFormatException, ElementIsAlreadyExistException, ParentNotFoundException {
        if (!isValidExcel(file)) {
            return false;
        }

        long chatId = update.getMessage().getChatId();

        //Очистка БД
        categoryService.deleteAllByChatId(chatId);

        int count = 0;

        //Подсчёт необходимых сдвигов вправо в документе
        try (Workbook workbook = new XSSFWorkbook(file)) {
            for (Row cell : workbook.getSheetAt(0)) {
                for (Cell cell1 : cell) {
                    if (cell1.getRowIndex() > 0) {
                        break;
                    }
                    count++;
                }
            }
            for (int i = 0; i < count; i++) {
                //Вызов метода для считывания данных сверху вниз
                initRow(workbook, i);
            }
        }
        return true;
    }

    /**
     * Метод для считывания данных сверху вниз
     * @param workbook Рабочая книга
     * @param position Позиция в колонке слева на права
     *                 для итерации сверху вниз
     * @throws ElementIsAlreadyExistException Элемент уже есть в базе
     * @throws ParentNotFoundException Родительский элемент не найден.
     */

    private void initRow(Workbook workbook, int position) throws ElementIsAlreadyExistException, ParentNotFoundException {
        List<String> elements = new ArrayList<>();
        for (Row cell : workbook.getSheetAt(0)) {
            // Проходим по элементам вниз. Если элементов нет, пропускаем шаг
            if (cell.getCell(position) == null) {
                continue;
            }
            String text = cell.getCell(position).getStringCellValue();
            elements.add(text);
        }
        // Вызов метода для добавления коллекции элементов, считанных сверху вниз
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
