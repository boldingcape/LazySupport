package plugin.vagrant;

import console.Query;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class VagrantPluginCheckerTest {

    VagrantPluginChecker vpc = new VagrantPluginChecker();

    @Test
    //Assertion does not throw because the exception is thrown at a lower level. run() will catch the exception so it does not throw
    void vagrantPluginFailed()  {
        vpc.setCommands(new String[]{"lssss"});
        Assertions.assertAll(
                () -> assertDoesNotThrow(() -> vpc.run()),
                () -> assertEquals(vpc.isStatus(), false)
        );
    }


    @Test
    void vagrantPluginSuccess() {
        Assertions.assertAll(
                () -> assertDoesNotThrow(() -> vpc.run()),
                () -> assertEquals(vpc.isStatus(), true)
        );
    }
}