package guru.qa.homework;

import guru.qa.homework.hwDomain.HomeWorkMethods;
import org.junit.jupiter.api.Test;

public class HomeWorkFilesTests extends HomeWorkMethods {

    @Test
    public void mainTest() throws Exception {
        commonMethod(EnumFiles.TXT);
        commonMethod(EnumFiles.PDF);
        commonMethod(EnumFiles.XLSX);
        commonMethod(EnumFiles.CSV);
        commonMethod(EnumFiles.JSON);
        commonMethod(EnumFiles.JACKSON);
    }
}
