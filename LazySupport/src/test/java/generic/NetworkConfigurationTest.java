package generic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NetworkConfigurationTest {

    NetworkConfiguration nc = null;

    @BeforeEach
    void testPrep(){
        nc = new NetworkConfiguration("HostInterfaceNetworking-vboxnet1", 600, 28800, 86400);
    }

    @Test
    void ncContentDeclaration(){
        Assertions.assertAll(
                () -> assertEquals(nc.getDhcpServerName(), "HostInterfaceNetworking-vboxnet1"),
                () -> assertEquals(nc.getDhcpMinLeaseTime(), 600),
                () -> assertEquals(nc.getDhcpDefaultLeaseTime(), 28800),
                () -> assertEquals(nc.getDhcpMaxLeaseTime(), 86400)
        );
    }

    @Test
    void invalidMinLeaseAboveMaxLeaseNcContentDeclaration(){
        assertThrows(IllegalArgumentException.class, () -> new NetworkConfiguration("HostInterfaceNetworking-vboxnet1", 86400, 28800, 86400));
    }

    @Test
    void invalidDefaultLeaseBelowMinLeaseNcContentDeclaration(){
        assertThrows(IllegalArgumentException.class, () -> new NetworkConfiguration("HostInterfaceNetworking-vboxnet1", 600, 599, 86400));
    }

    @Test
    void invalidDefaultLeaseAboveMaxLeaseNcContentDeclaration(){
        assertThrows(IllegalArgumentException.class, () -> new NetworkConfiguration("HostInterfaceNetworking-vboxnet1", 600, 86401, 86400));
    }

    @Test
    void invalidMaxLeaseBelowMinLeaseNcContentDeclaration(){
        assertThrows(IllegalArgumentException.class, () -> new NetworkConfiguration("HostInterfaceNetworking-vboxnet1", 600, 86401, 600));
    }

    @Test
    void ncMaxLeaseBelowMinLeaseModifyShouldFail(){
        nc.setDhcpMaxLeaseTime(200);
        Assertions.assertNotEquals(nc.getDhcpMaxLeaseTime(), 200);
    }

    @Test
    void ncMaxLeaseAboveMinLeaseModifyShouldPassed(){
        nc.setDhcpMaxLeaseTime(86000);
        Assertions.assertEquals(nc.getDhcpMaxLeaseTime(), 86000);
    }

    @Test
    void ncMinLeaseAboveMaxLeaseModifyShouldFail(){
        nc.setDhcpMinLeaseTime(80000);
        Assertions.assertNotEquals(nc.getDhcpMinLeaseTime(), 80000);
    }

    @Test
    void ncMinLeaseBelowMaxLeaseModifyShouldPassed(){
        nc.setDhcpMinLeaseTime(200);
        Assertions.assertEquals(nc.getDhcpMinLeaseTime(), 200);
    }

    @Test
    void ncDefaultLeastAboveMaxLeaseModifyShouldFail(){
        nc.setDhcpDefaultLeaseTime(1000000000);
        Assertions.assertNotEquals(nc.getDhcpDefaultLeaseTime(), 1000000000);
    }

    @Test
    void ncDefaultLeastBelowMinLeaseModifyShouldFail(){
        nc.setDhcpDefaultLeaseTime(0);
        Assertions.assertNotEquals(nc.getDhcpDefaultLeaseTime(), 0);
    }

    @Test
    void ncDefaultLeastBetweenMinLeaseAndMaxLeaseModifyShouldPassed(){
        nc.setDhcpDefaultLeaseTime(20000);
        Assertions.assertEquals(nc.getDhcpDefaultLeaseTime(), 20000);
    }

    @Test
    void setDHCPNameForCoverage(){
        nc.setDhcpServerName("test");
        Assertions.assertEquals("test", nc.getDhcpServerName());
    }
}