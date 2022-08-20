package console;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
class QueryTest {
    Query q = new Query();
    @Test
    void queryStatusByDefaultFailed() {
        Assertions.assertEquals(q.getRequestState(), 1);
    }

    @Test
    void querySuccess() {
        String[] commands = {"vagrant", "--version"};
        Assertions.assertAll(
                () -> assertDoesNotThrow(() -> q.request(commands)),
                () -> assertEquals(q.getRequestState(), 0)
        );
    }


    @Test
    void queryFailure() {
        String [] commands = {""};
        Assertions.assertAll(
                () -> assertThrows(IOException.class, () -> q.request(commands)),
                () -> assertEquals(q.getRequestState(), 1)
        );
    }

}