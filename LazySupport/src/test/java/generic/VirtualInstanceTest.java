package generic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VirtualInstanceTest {
    VirtualConfig vc = null;

    @BeforeEach
    void testPrep(){
        vc = new VirtualConfig("HostInterfaceNetworking-vboxnet1", 600, 28800, 86400);
    }

    @Test
    void setVcShouldReflectCorrectly(){
        VirtualInstance vi = new VirtualInstance();
        vi.setVc(vc);
        Assertions.assertNotNull(vi.getVc());
    }
}