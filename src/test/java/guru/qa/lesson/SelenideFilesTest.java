package guru.qa.lesson;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.nio.file.Files;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static java.nio.charset.StandardCharsets.UTF_8;

public class SelenideFilesTest {

    @Test
    public void selenideDownloadTest() throws Exception {
        open("https://github.com/junit-team/junit5/blob/main/README.md");

        File downloadedFile = $("#raw-url").download();
//        try (InputStream is = new FileInputStream(downloadedFile)) {
//            assertThat(new String(is.readAllBytes(), UTF_8)).contains(
//                    "This repository is the home of the next generation of JUnit");
//        }
        // Альтернативный способ чтения файла для использования в последующих проверках.
        String readString = Files.readString(downloadedFile.toPath(), UTF_8);
        Assertions.assertTrue(readString.contains("This repository is the home of the next generation of JUnit"));
    }

    @Test
    public void uploadFileTest() {
        open("https://the-internet.herokuapp.com/upload");

//        $("input[type='file']").uploadFile(new File("C:\\ProjectsJob\\QAGuruProjects\\Lesson_9_files\\src\\test\\resources\\files\\1.txt")); // bad practice
        $("input[type='file']").uploadFromClasspath("files/1.txt");
        $("#file-submit").click();
        $("div.example").shouldHave(text("File Uploaded!"));
        $("#uploaded-files").shouldHave(text("1.txt"));
    }

}
