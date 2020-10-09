package com.withpet.walk;

public class Walk_ReportUpload {
    private String Report_title;
    private String Report_content;
    private int Report_boardnb;
    private String Reporter;

    Walk_ReportUpload(){}

    Walk_ReportUpload(String Report_title,String Report_content, int Report_boardnb,String Reporter){
        this.Report_title = Report_title;
        this.Report_content = Report_content;
        this.Report_boardnb = Report_boardnb;
        this.Reporter = Reporter;
    }

    public String getReport_title() {
        return Report_title;
    }

    public void setReport_title(String report_title) {
        Report_title = report_title;
    }

    public String getReport_content() {
        return Report_content;
    }

    public void setReport_content(String report_content) {
        Report_content = report_content;
    }

    public int getReport_boardnb() {
        return Report_boardnb;
    }

    public void setReport_boardnb(int report_boardnb) {
        Report_boardnb = report_boardnb;
    }

    public String getReporter() {
        return Reporter;
    }

    public void setReporter(String reporter) {
        Reporter = reporter;
    }
}
