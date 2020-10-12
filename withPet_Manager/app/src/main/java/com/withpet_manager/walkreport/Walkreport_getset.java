package com.withpet_manager.walkreport;

public class Walkreport_getset {
    private int report_boardnb;
    private String report_content;
    private String report_title;
    private String reporter;

    Walkreport_getset(){
    }

    Walkreport_getset(int report_boardnb, String report_content, String report_title, String reporter){
        this.report_boardnb = report_boardnb;
        this.report_content = report_content;
        this.report_title = report_title;
        this.reporter = reporter;
    }

    public int getReport_boardnb() {
        return report_boardnb;
    }

    public void setReport_boardnb(int report_boardnb) {
        this.report_boardnb = report_boardnb;
    }

    public String getReport_content() {
        return report_content;
    }

    public void setReport_content(String report_content) {
        this.report_content = report_content;
    }

    public String getReport_title() {
        return report_title;
    }

    public void setReport_title(String report_title) {
        this.report_title = report_title;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }
}
