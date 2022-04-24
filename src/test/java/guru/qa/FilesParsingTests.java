package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.opencsv.CSVReader;
import guru.qa.domain.User;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

public class FilesParsingTests {

    ClassLoader classLoader = getClass().getClassLoader();

    @Test
    public void parsePdfTest() throws Exception {
        open("https://junit.org/junit5/docs/current/user-guide/");

        File pdfDownload = $(byText("PDF download")).download();
        PDF pdf = new PDF(pdfDownload);
        assertThat(pdf.author).contains("Marc Philipp");
        assertThat(pdf.text).contains("The following example provides a glimpse at the minimum requirements for writing a test in JUnit");
        assertThat(pdf.numberOfPages).isEqualTo(166);
    }

    @Test
    public void parseXlsTest() throws Exception {
        open("http://romashka2008.ru/price");

        File xslDownload = $(".site-main__inner a[href*='prajs_ot']").download();
        XLS xls = new XLS(xslDownload);
        assertThat(xls.excel
                .getSheetAt(0)
                .getRow(30)
                .getCell(3)
                .getNumericCellValue()).isEqualTo(3404.7);
    }

    @Test
    public void parseCsvTest() throws Exception {
//        ClassLoader classLoader = getClass().getClassLoader(); // первый способ только для не статических методов
//        ClassLoader classLoader1 = FilesParsingTests.class.getClassLoader(); // можно использовать в статических методах
        try (InputStream is = classLoader.getResourceAsStream("files/Machine_readable_file_bdc_sf_2021_q4.csv")) {
            try (CSVReader reader = new CSVReader(new InputStreamReader(is))) { // можем использовать несколько выражений в блоке", " для автоматического закрытия", " когда станут не нужны
                List<String[]> content = reader.readAll();
                assertThat(content.get(0)).contains(
                        "Series_reference",
                        "Period",
                        "Data_value",
                        "Suppressed",
                        "STATUS",
                        "UNITS",
                        "Magnitude",
                        "Subject",
                        "Group",
                        "Series_title_1",
                        "Series_title_2",
                        "Series_title_3",
                        "Series_title_4",
                        "Series_title_5");
            }
        }
    }

    @Test
    public void parseZipTest() throws Exception {
        try(InputStream is = classLoader.getResourceAsStream("files/test.zip");
            ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry entry;
            while((entry = zis.getNextEntry()) != null) {
                assertThat(entry.getName()).isEqualTo("Machine_readable_file_bdc_sf_2021_q4.csv");
            }
        }
    }

    @Test
    public void jsonCommonTest() throws Exception {
        Gson gson = new Gson();
        try(InputStream is = classLoader.getResourceAsStream("files/jsonFile.json")) {
            String json = (new String(is.readAllBytes(), UTF_8));
            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            assertThat(jsonObject.get("name").getAsString()).isEqualTo("Rustam");
            assertThat(jsonObject.get("address").getAsJsonObject().get("street").getAsString()).isEqualTo("Komissarovskaya");

        }
    }

    @Test
    public void jsonTypeTest() throws Exception {
        Gson gson = new Gson();
        try(InputStream is = classLoader.getResourceAsStream("files/jsonFile.json")) {
            String json = (new String(is.readAllBytes(), UTF_8));
            User user = gson.fromJson(json, User.class);
            assertThat(user.name).isEqualTo("Rustam");
            assertThat(user.address.street).isEqualTo("Komissarovskaya");
        }
    }

    //Gson -> ObjectMapper, jsonObject -> jsonTree
}
