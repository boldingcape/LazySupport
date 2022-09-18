package virtualbox;

import generic.VirtualConfig;
import generic.VirtualInstance;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.virtualbox_6_1.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VirtualBoxControllerTest {
    IVirtualBox vbox = VirtualBoxManager.createInstance(null).getVBox();
    @BeforeEach
    void createVirtualInstance(){
    }

    @Test
    void matchingDhcpConfigShouldPassed(){

    }
}