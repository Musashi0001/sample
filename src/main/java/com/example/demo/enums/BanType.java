package com.example.demo.enums;

public enum BanType {
    SOFT("ソフトBAN(一部機能制限)"),
    TEMPORARY("一時的BAN(一定期間アカウント停止,アクセス制限)"),
    PERMANENT("永久BAN(アカウント永久停止)");

    private final String label;

    BanType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
