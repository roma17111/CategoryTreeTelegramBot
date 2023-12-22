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

    private synchronized String getExelFromDB(Update update) {
        System.out.println("Начало загрузки");
        long chatId = update.getMessage().getChatId();
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
            System.out.println("foreach\n\n");
            for (Category category : categories) {
                List<Category> parentCategories = categoryService.findAllByParentCategoryAndChatId(category, chatId);
                if (!parentCategories.isEmpty()) {
                    Cell cell = row.createCell(position);
                    cell.setCellValue(category.getCategoryName());
                    initRow(sheet,parentCategories,position);
                    position++;
                }

            }
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

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
