package guru.qa.homework.hwDomain;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import guru.qa.homework.EnumFiles;
import org.junit.jupiter.api.DisplayName;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

public class HomeWorkMethods {

    ClassLoader classLoader = getClass().getClassLoader();

    @DisplayName("Общий метод")
    public void commonMethod(EnumFiles testData) throws Exception {
        switch (testData) {
            case TXT -> readAndCheckTXTFile(testData.fileName);
            case PDF -> readAndCheckPdfFile(testData.fileName);
            case XLSX -> readAndCheckXlsxFile(testData.fileName);
            case CSV -> readAndCheckCsvFile(testData.fileName);
            case JSON -> readAndCheckJsonFileCommonWay(testData.fileName);
            case JACKSON -> readAndCheckJsonFileTypeWay(testData.fileName);
        }
    }

    @DisplayName("Чтение и проверка файла {name}")
    public void readAndCheckTXTFile(String name) throws Exception {
        ZipFile zf = new ZipFile(new File("src/test/resources/hwFiles/allFormat.zip"));
        ZipInputStream is = new ZipInputStream(classLoader.getResourceAsStream("hwFiles/allFormat.zip"));
        ZipEntry zipEntry = zf.getEntry(name);
        try(InputStream inputStream = zf.getInputStream(zipEntry)) {
            assertThat(new String(inputStream.readAllBytes(), UTF_8)).contains("Quis istum dolorem timet?");
            assertThat(zipEntry.getName()).contains("txtSample");
            assertThat(zipEntry.getTime()).isEqualTo(1650839070342L);
        }
    }

    @DisplayName("Чтение и проверка файла {name}")
    public void readAndCheckPdfFile(String name) throws Exception {
        ZipFile zf = new ZipFile(new File("src/test/resources/hwFiles/allFormat.zip"));
        ZipInputStream is = new ZipInputStream(classLoader.getResourceAsStream("hwFiles/allFormat.zip"));
        ZipEntry zipEntry = zf.getEntry(name);
        try(InputStream inputStream = zf.getInputStream(zipEntry)) {
            PDF pdf = new PDF(inputStream);
            assertThat(pdf.creator).isEqualTo("Microsoft® Office Word 2007");
            assertThat(pdf.text).contains("CO2, or perhaps a simpler 'starring' system: 5* = very green, 1* = not");
            assertThat(pdf.numberOfPages).isEqualTo(20);
        }
    }

    @DisplayName("Чтение и проверка файла {name}")
    public void readAndCheckXlsxFile(String name) throws Exception {
        ZipFile zf = new ZipFile(new File("src/test/resources/hwFiles/allFormat.zip"));
        ZipInputStream is = new ZipInputStream(classLoader.getResourceAsStream("hwFiles/allFormat.zip"));
        ZipEntry zipEntry = zf.getEntry(name);
        try(InputStream inputStream = zf.getInputStream(zipEntry)) {
            XLS xls = new XLS(inputStream);
            assertThat(xls.excel
                    .getSheetAt(0)
                    .getRow(96)
                    .getCell(5)
                    .getNumericCellValue()).isEqualTo(2386.5);
        }
    }

    @DisplayName("Чтение и проверка файла {name}")
    public void readAndCheckCsvFile(String name) throws Exception {
        ZipFile zf = new ZipFile(new File("src/test/resources/hwFiles/allFormat.zip"));
        ZipInputStream is = new ZipInputStream(classLoader.getResourceAsStream("hwFiles/allFormat.zip"));
        ZipEntry zipEntry = zf.getEntry(name);
        try(InputStream inputStream = zf.getInputStream(zipEntry);
            CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            List<String[]> values = reader.readAll();
            assertThat(values.get(22)).contains(
                    "ECTA.S19A1",
                    "2022.03",
                    "71690.6",
                    "",
                    "F",
                    "Dollars",
                    "6",
                    "Electronic Card Transactions (ANZSIC06) - ECT",
                    "Total values - Electronic card transactions A/S/T by division",
                    "Actual",
                    "RTS total industries",
                    "",
                    "",
                    ""
            );
            assertThat(values.size()).isEqualTo(18584);
        }
    }

    @DisplayName("Чтение и проверка файла {name}")
    public void readAndCheckJsonFileCommonWay(String name) throws Exception {
        ZipFile zf = new ZipFile(new File("src/test/resources/hwFiles/allFormat.zip"));
        ZipInputStream is = new ZipInputStream(classLoader.getResourceAsStream("hwFiles/allFormat.zip"));
        ZipEntry zipEntry = zf.getEntry(name);
        ObjectMapper objectMapper = new ObjectMapper();
        try(InputStream inputStream = zf.getInputStream(zipEntry)) {
            String json = (new String(inputStream.readAllBytes(), UTF_8));
            JsonNode jsonNode = objectMapper.readTree(json);
            assertThat(jsonNode.get("squadName").asText()).isEqualTo("Super hero squad");
            assertThat(jsonNode.get("members").get(0).get("age").asInt()).isEqualTo(29);
            assertThat(jsonNode.get("members").get(2).get("powers").get(2).asText()).isEqualTo("Inferno");
        }
    }

    @DisplayName("Чтение и проверка файла {name} с помощью POJO")
    public void readAndCheckJsonFileTypeWay(String name) throws Exception {
        ZipFile zf = new ZipFile(new File("src/test/resources/hwFiles/allFormat.zip"));
        ZipInputStream is = new ZipInputStream(classLoader.getResourceAsStream("hwFiles/allFormat.zip"));
        ZipEntry zipEntry = zf.getEntry(name);
        ObjectMapper objectMapper = new ObjectMapper();
        try(InputStream inputStream = zf.getInputStream(zipEntry)) {
            String json = (new String(inputStream.readAllBytes(), UTF_8));
            Team team = objectMapper.readValue(json, Team.class);
            assertThat(team.squadName).isEqualTo("Super hero squad");
            assertThat(team.members.get(0).age).isEqualTo(29);
            assertThat(team.members.get(2).powers.get(2)).isEqualTo("Inferno");
        }
    }
}
