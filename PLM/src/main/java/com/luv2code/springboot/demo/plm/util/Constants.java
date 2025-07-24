package com.luv2code.springboot.demo.plm.util;

public final class Constants {

    // File Upload
    public static final String UPLOAD_DIR = "uploads";
    public static final String COVERS_DIR = "covers";
    public static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    // Pagination
    public static final int DEFAULT_PAGE_SIZE = 12;
    public static final int MAX_PAGE_SIZE = 100;

    // Budget
    public static final String MIN_BUDGET_AMOUNT = "1000";

    // Messages
    public static final String SUCCESS_BOOK_CREATED = "Thêm sách thành công!";
    public static final String SUCCESS_BOOK_UPDATED = "Cập nhật sách thành công!";
    public static final String SUCCESS_BOOK_DELETED = "Xóa sách thành công!";
    public static final String SUCCESS_BUDGET_ADDED = "Thêm ngân sách thành công!";

    public static final String ERROR_INSUFFICIENT_FUNDS = "Không đủ tiền để mua sách này!";
    public static final String ERROR_BOOK_NOT_FOUND = "Không tìm thấy sách!";
    public static final String ERROR_USER_NOT_FOUND = "Không tìm thấy người dùng!";
    public static final String ERROR_FILE_UPLOAD = "Lỗi khi upload file!";

    private Constants() {
        // Utility class
    }
}