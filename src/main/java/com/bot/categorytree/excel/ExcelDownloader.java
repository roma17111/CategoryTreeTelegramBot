package com.bot.categorytree.excel;

import com.bot.categorytree.entity.Category;
import com.bot.categorytree.service.CategoryService;
import lombok.NonNull;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Класс для обработки
 * логики загрузки excel документа в бот
 * пользователю
 */

public class ExcelDownloader implements Callable<String> {

    private static final String CATEGORY_TREE = "documents/category tree ";
    private static final String XLSX_FORMAT = ".xlsx";
    private final Update update;
    private final CategoryService categoryService;

    public ExcelDownloader(@NonNull Update update,
                           @NonNull CategoryService categoryService) {
        this.update = update;
        this.categoryService = categoryService;
    }

    /**
     * Метод для конвертации данных из DB to excel
     * и предоставления пути к файлу
     * @param update Данные пользователя из тг, десеарлизованные
     *      *               из JSON in JAVA class
     * @return путь к файлу
     */
    private synchronized String getExelFromDB(Update update) {
        System.out.println("Начало загрузки");
        long chatId = update.getMessage().getChatId();
        // Получаем всё дерево элементов из базы по чату пользователя
        List<Category> categories = categoryService.getTree(chatId);
        if (categories.isEmpty()) {
            return "";
        }
        categories.stream().map(Category::getCategoryName).forEach(System.out::println);
        String name = update.getMessage().getChat().getUserName();
        String file = CATEGORY_TREE + name + XLSX_FORMAT;
        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream outputStream = new FileOutputStream(file)) {
            Sheet sheet = workbook.createSheet("tree");
            int position = 0;
            Row row = sheet.createRow(0);
            for (Category category : categories) {
                // Проходим в цикле, перебирая всё дерево. Если есть дочерние элементы,
                // печатаем их в таблице сверху вниз
                List<Category> parentCategories = categoryService.findAllByParentCategoryAndChatId(category, chatId);
                if (!parentCategories.isEmpty()) {
                    Cell cell = row.createCell(position);
                    cell.setCellValue(category.getCategoryName());
                    initRow(sheet,parentCategories,position);

                    // Если есть родительский элемент, передвигаем кусор в документе слева направо
                    position++;
                }

            }
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    /**
     * Заполнение каждого ряда таблицы excel
     * Сверху вниз, где колонка самого верхнего
     * ряда содержит корень или родительский элемент,
     * а элементы под ним - его потомки.
     * Если потомков нет, ряд сверху вниз не заполняется
     * @param sheet Таблица
     * @param parentCategories Коллеция потомков родителя
     *                         для заполнения сверху вниз
     *                         сразу после родителя
     * @param rowPosition Позиция слева направа
     *                    для заполнения сверху вниз
     */

    private void initRow(Sheet sheet,
                         List<Category> parentCategories,
                         int rowPosition) {
        for (int i = 0; i < parentCategories.size(); i++) {
            Category c = parentCategories.get(i);
            System.out.println(c.getCategoryName());
            Row row = sheet.getRow(i + 1);
            if (row == null) {
                row = sheet.createRow(i + 1);
            }
            row.createCell(rowPosition).setCellValue(c.getCategoryName());
        }

    }

    @Override
    public String call() throws Exception {
        return getExelFromDB(this.update);
    }
}
