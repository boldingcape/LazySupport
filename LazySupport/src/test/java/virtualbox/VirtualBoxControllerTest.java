package virtualbox;

import generic.VirtualConfig;
import generic.VirtualInstance;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.virtualbox_6_1.IDHCPServer;
import org.virtualbox_6_1.IVirtualBox;
import org.virtualbox_6_1.VirtualBoxManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VirtualBoxControllerTest {
    IVirtualBox vbox = VirtualBoxManager.createInstance(null).getVBox();
    @BeforeEach
    void createVirtualInstance(){
        VirtualConfig vc = new VirtualConfig("HostInterfaceNetworking-vboxnet1", 600, 28800, 86400);
        VirtualInstance vi = new VirtualInstance();
        vi.setVc(vc);
    }

    @Test
    void matchingDhcpConfigShouldPassed(){
        IVirtualBox mockVBox = Mockito.mock(IVirtualBox.class);
        Mockito.when(mockVBox.getDHCPServers()).thenReturn(vbox.getDHCPServers());
        List<IDHCPServer> dhcpList = mockVBox.getDHCPServers();
        for (IDHCPServer dhcp: dhcpList){
            System.out.println("dhcp name: " + dhcp.getNetworkName());
        }
    }

}