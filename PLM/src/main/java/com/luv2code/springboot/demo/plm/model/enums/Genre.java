package com.luv2code.springboot.demo.plm.model.enums;

public enum Genre {
    FICTION("Tiểu thuyết"),
    NON_FICTION("Phi tiểu thuyết"),
    SCIENCE_FICTION("Khoa học viễn tưởng"),
    FANTASY("Giả tưởng"),
    MYSTERY("Trinh thám"),
    ROMANCE("Lãng mạn"),
    THRILLER("Kinh dị"),
    BIOGRAPHY("Tiểu sử"),
    HISTORY("Lịch sử"),
    SCIENCE("Khoa học"),
    TECHNOLOGY("Công nghệ"),
    BUSINESS("Kinh doanh"),
    SELF_HELP("Tự lực"),
    EDUCATION("Giáo dục"),
    OTHER("Khác");

    private final String displayName;

    Genre(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
