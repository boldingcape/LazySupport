package generic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class VirtualConfigTest {

    VirtualConfig vc = null;

    @BeforeEach
    void testPrep(){
        vc = new VirtualConfig("HostInterfaceNetworking-vboxnet1", 600, 28800, 86400);
    }

    @Test
    void vcContentDeclaration(){
        Assertions.assertAll(
                () -> assertEquals(vc.getDhcpServerName(), "HostInterfaceNetworking-vboxnet1"),
                () -> assertEquals(vc.getDhcpMinLeaseTime(), 600),
                () -> assertEquals(vc.getDhcpDefaultLeaseTime(), 28800),
                () -> assertEquals(vc.getDhcpMaxLeaseTime(), 86400)
        );
    }

    @Test
    void invalidMinLeaseAboveMaxLeaseVcContentDeclaration(){
        assertThrows(IllegalArgumentException.class, () -> new VirtualConfig("HostInterfaceNetworking-vboxnet1", 86400, 28800, 86400));
    }

    @Test
    void invalidDefaultLeaseBelowMinLeaseVcContentDeclaration(){
        assertThrows(IllegalArgumentException.class, () -> new VirtualConfig("HostInterfaceNetworking-vboxnet1", 600, 599, 86400));
    }

    @Test
    void invalidDefaultLeaseAboveMaxLeaseVcContentDeclaration(){
        assertThrows(IllegalArgumentException.class, () -> new VirtualConfig("HostInterfaceNetworking-vboxnet1", 600, 86401, 86400));
    }

    @Test
    void invalidMaxLeaseBelowMinLeaseVcContentDeclaration(){
        assertThrows(IllegalArgumentException.class, () -> new VirtualConfig("HostInterfaceNetworking-vboxnet1", 600, 86401, 600));
    }

    @Test
    void vcMaxLeaseBelowMinLeaseModifyShouldFail(){
        vc.setDhcpMaxLeaseTime(200);
        Assertions.assertNotEquals(vc.getDhcpMaxLeaseTime(), 200);
    }

    @Test
    void vcMaxLeaseAboveMinLeaseModifyShouldPassed(){
        vc.setDhcpMaxLeaseTime(86000);
        Assertions.assertEquals(vc.getDhcpMaxLeaseTime(), 86000);
    }

    @Test
    void vcMinLeaseAboveMaxLeaseModifyShouldFail(){
        vc.setDhcpMinLeaseTime(80000);
        Assertions.assertNotEquals(vc.getDhcpMinLeaseTime(), 80000);
    }

    @Test
    void vcMinLeaseBelowMaxLeaseModifyShouldPassed(){
        vc.setDhcpMinLeaseTime(200);
        Assertions.assertEquals(vc.getDhcpMinLeaseTime(), 200);
    }

    @Test
    void vcDefaultLeastAboveMaxLeaseModifyShouldFail(){
        vc.setDhcpDefaultLeaseTime(1000000000);
        Assertions.assertNotEquals(vc.getDhcpDefaultLeaseTime(), 1000000000);
    }

    @Test
    void vcDefaultLeastBelowMinLeaseModifyShouldFail(){
        vc.setDhcpDefaultLeaseTime(0);
        Assertions.assertNotEquals(vc.getDhcpDefaultLeaseTime(), 0);
    }

    @Test
    void vcDefaultLeastBetweenMinLeaseAndMaxLeaseModifyShouldPassed(){
        vc.setDhcpDefaultLeaseTime(20000);
        Assertions.assertEquals(vc.getDhcpDefaultLeaseTime(), 20000);
    }

    @Test
    void setDHCPNameForCoverage(){
        vc.setDhcpServerName("test");
    }
}