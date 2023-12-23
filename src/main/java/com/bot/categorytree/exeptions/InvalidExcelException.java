package com.bot.categorytree.exeptions;

/**
 * Выгружен некорректный документ
 * Возможно неправильный формат
 */
public class InvalidExcelException extends Exception {

    public InvalidExcelException() {
    }

    public InvalidExcelException(String message) {
        super(message);
    }
}
