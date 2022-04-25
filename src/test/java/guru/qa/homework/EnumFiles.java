package guru.qa.homework;

public enum EnumFiles {

    TXT("txtSample.txt"),
    PDF("pdfSample.pdf"),
    XLSX("xlsxSample.xlsx"),
    CSV("csvSample.csv"),
    JSON("jsonSample.json"),
    JACKSON("jsonSample.json");

    public final String fileName;

    EnumFiles(String fileName) {
        this.fileName = fileName;
    }
}
