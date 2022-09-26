package virtualbox;

import generic.IHostNetworkInterfaceHolder;
import generic.NetworkConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.virtualbox_6_1.*;

import static org.mockito.Mockito.*;

class VirtualBoxNetworkConfigControllerTest {
    IVirtualBox vbox = null;
    VirtualBoxNetworkConfigController vboxnc = null;
    NetworkConfiguration nc = null;
    IHostNetworkInterfaceHolder iHostNetworkInterfaceHolder = null;

    @BeforeEach
    void mockCommonClass(){
        vbox = Mockito.mock(IVirtualBox.class);
        vboxnc = Mockito.mock(VirtualBoxNetworkConfigController.class);
        iHostNetworkInterfaceHolder = Mockito.mock(IHostNetworkInterfaceHolder.class);
    }



    @Nested
    class TestDelete{
        @Test
        void searchResultFailedShouldReturnFalse(){
            when(vboxnc.search(Mockito.any(),Mockito.any())).thenReturn(true);
            when(vboxnc.delete(Mockito.any(), Mockito.any(), Mockito.any())).thenCallRealMethod();
            Assertions.assertFalse(vboxnc.delete(nc, vbox, iHostNetworkInterfaceHolder));
        }

        @Test
        void searchResultTrueShouldReturnTrue(){
            IDHCPServer idhcpServer = Mockito.mock(IDHCPServer.class);
            IDHCPGlobalConfig idhcpGlobalConfig = Mockito.mock(IDHCPGlobalConfig.class);
            Holder<IHostNetworkInterface> hostif = Mockito.mock(Holder.class);
            IHostNetworkInterface ihostnetworkinterface = Mockito.mock(IHostNetworkInterface.class);
            IHost ihost = Mockito.mock(IHost.class);
            nc = Mockito.mock(NetworkConfiguration.class);

            when(vboxnc.search(Mockito.any(),Mockito.any())).thenReturn(false);
            doNothing().when(vbox).removeDHCPServer(Mockito.any());
            when(vbox.findDHCPServerByNetworkName(Mockito.anyString())).thenReturn(idhcpServer);
            when(vbox.getHost()).thenReturn(ihost);
            when(iHostNetworkInterfaceHolder.getHolder()).thenReturn(hostif);
            when(iHostNetworkInterfaceHolder.getHolderValue()).thenReturn(ihostnetworkinterface);
            when(ihost.removeHostOnlyNetworkInterface(Mockito.anyString())).thenReturn(Mockito.mock(IProgress.class));
            when(vboxnc.delete(Mockito.any(), Mockito.any(), Mockito.any())).thenCallRealMethod();
            Assertions.assertTrue(vboxnc.delete(nc, vbox, iHostNetworkInterfaceHolder));
        }
    }

    @Nested
    class TestModify{
        @Test
        void searchResultFailShouldReturnFalse(){
            when(vboxnc.search(Mockito.any(),Mockito.any())).thenReturn(true);
            when(vboxnc.modify(Mockito.any(), Mockito.any())).thenCallRealMethod();
            Assertions.assertFalse(vboxnc.modify(nc, vbox));
        }

        @Test
        void searchResultTrueShouldReturnTrue(){
            IDHCPServer idhcpServer = Mockito.mock(IDHCPServer.class);
            IDHCPGlobalConfig idhcpGlobalConfig = Mockito.mock(IDHCPGlobalConfig.class);
            nc = Mockito.mock(NetworkConfiguration.class);
            when(vboxnc.search(Mockito.any(),Mockito.any())).thenReturn(false);
            when(vbox.findDHCPServerByNetworkName(Mockito.any())).thenReturn(idhcpServer);
            when(idhcpServer.getGlobalConfig()).thenReturn(idhcpGlobalConfig);
            doNothing().when(idhcpGlobalConfig).setMinLeaseTime(Mockito.anyLong());
            doNothing().when(idhcpGlobalConfig).setDefaultLeaseTime(Mockito.anyLong());
            doNothing().when(idhcpGlobalConfig).setMaxLeaseTime(Mockito.anyLong());

            when(vboxnc.modify(Mockito.any(), Mockito.any())).thenCallRealMethod();
            Assertions.assertTrue(vboxnc.modify(nc, vbox));
        }
    }

    @Nested
    class TestCreate{
        @Test
        void searchResultTrueShouldReturnFalse(){
            when(vboxnc.search(Mockito.any(),Mockito.any())).thenReturn(true);
            when(vboxnc.create(Mockito.any(), Mockito.any(), Mockito.any())).thenCallRealMethod();
            Assertions.assertFalse(vboxnc.create(nc, vbox, iHostNetworkInterfaceHolder));
        }

        @Test
        void searchResultFalseShouldReturnTrue(){
            IHost ihost = Mockito.mock(IHost.class);
            Holder<IHostNetworkInterface> hostif = Mockito.mock(Holder.class);
            IHostNetworkInterface ihostnetworkinterface = Mockito.mock(IHostNetworkInterface.class);
            IDHCPServer dhcpServer = Mockito.mock(IDHCPServer.class);

            when(vboxnc.search(Mockito.any(),Mockito.any())).thenReturn(false);
            when(vbox.getHost()).thenReturn(ihost);
            when(ihost.createHostOnlyNetworkInterface(Mockito.any())).thenReturn(Mockito.mock(IProgress.class));
            when(iHostNetworkInterfaceHolder.getHolder()).thenReturn(hostif);
            when(iHostNetworkInterfaceHolder.getHolderValue()).thenReturn(ihostnetworkinterface);
            when(vbox.createDHCPServer(Mockito.anyString())).thenReturn(dhcpServer);
            doNothing().when(dhcpServer).setConfiguration(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
            doNothing().when(dhcpServer).setEnabled(Mockito.anyBoolean());
            when(ihostnetworkinterface.getNetworkName()).thenReturn("test");
            when(ihostnetworkinterface.getIPAddress()).thenReturn("192.168.56.1");
            when(ihostnetworkinterface.getNetworkMask()).thenReturn("255.255.255.0");

            when(vboxnc.create(Mockito.any(), Mockito.any(), Mockito.any())).thenCallRealMethod();
            Assertions.assertTrue(vboxnc.create(nc, vbox, iHostNetworkInterfaceHolder));
        }
    }

    @Nested
    class TestCheck {
        @Test
        void searchResultFailShouldReturnArrayOfFail(){
            when(vboxnc.search(Mockito.any(),Mockito.any())).thenReturn(false);
            when(vboxnc.checkConfig(Mockito.any(), Mockito.any())).thenCallRealMethod();
            Assertions.assertArrayEquals(new Boolean[]{false, false}, vboxnc.checkConfig(nc, vbox));
        }

        @Test
        void searchResultTrueAndConfigMatchedShouldReturnArrayOfTrue(){
            nc = Mockito.mock(NetworkConfiguration.class);

            when(vboxnc.search(Mockito.any(),Mockito.any())).thenReturn(true);

            String name = "HostInterfaceNetworking-vboxnet0";
            long minLease = 600;
            long defaultLease = 28800;
            long maxLease = 86400;
            when(nc.getDhcpServerName()).thenReturn(name);
            when(nc.getDhcpMinLeaseTime()).thenReturn(minLease);
            when(nc.getDhcpDefaultLeaseTime()).thenReturn(defaultLease);
            when(nc.getDhcpMaxLeaseTime()).thenReturn(maxLease);

            IDHCPGlobalConfig idhcpConfig = Mockito.mock(IDHCPGlobalConfig.class);
            when(idhcpConfig.getMinLeaseTime()).thenReturn(minLease);
            when(idhcpConfig.getDefaultLeaseTime()).thenReturn(defaultLease);
            when(idhcpConfig.getMaxLeaseTime()).thenReturn(maxLease);

            IDHCPServer idhcpServer = Mockito.mock(IDHCPServer.class);
            when(vbox.findDHCPServerByNetworkName(Mockito.anyString())).thenReturn(idhcpServer);
            when(idhcpServer.getGlobalConfig()).thenReturn(idhcpConfig);

            when(vboxnc.checkConfig(Mockito.any(), Mockito.any())).thenCallRealMethod();

            Assertions.assertArrayEquals(new Boolean[]{true, true}, vboxnc.checkConfig(nc, vbox));
        }

        @Test
        void searchResultTrueAndMinLeaseMismatchedShouldReturnArrayOfTrueAndFalse(){
            nc = Mockito.mock(NetworkConfiguration.class);

            when(vboxnc.search(Mockito.any(),Mockito.any())).thenReturn(true);

            String name = "HostInterfaceNetworking-vboxnet0";
            long minLease = 600;
            long defaultLease = 28800;
            long maxLease = 86400;
            when(nc.getDhcpServerName()).thenReturn(name);
            when(nc.getDhcpMinLeaseTime()).thenReturn(minLease);
            when(nc.getDhcpDefaultLeaseTime()).thenReturn(defaultLease);
            when(nc.getDhcpMaxLeaseTime()).thenReturn(maxLease);

            IDHCPGlobalConfig idhcpConfig = Mockito.mock(IDHCPGlobalConfig.class);
            when(idhcpConfig.getMinLeaseTime()).thenReturn(500L);
            when(idhcpConfig.getDefaultLeaseTime()).thenReturn(defaultLease);
            when(idhcpConfig.getMaxLeaseTime()).thenReturn(maxLease);

            IDHCPServer idhcpServer = Mockito.mock(IDHCPServer.class);
            when(vbox.findDHCPServerByNetworkName(Mockito.anyString())).thenReturn(idhcpServer);
            when(idhcpServer.getGlobalConfig()).thenReturn(idhcpConfig);

            when(vboxnc.checkConfig(Mockito.any(), Mockito.any())).thenCallRealMethod();

            Assertions.assertArrayEquals(new Boolean[]{true, false}, vboxnc.checkConfig(nc, vbox));
        }

        @Test
        void searchResultTrueAndDefaultLeaseMismatchedShouldReturnArrayOfTrueAndFalse(){
            nc = Mockito.mock(NetworkConfiguration.class);

            when(vboxnc.search(Mockito.any(),Mockito.any())).thenReturn(true);

            String name = "HostInterfaceNetworking-vboxnet0";
            long minLease = 600;
            long defaultLease = 28800;
            long maxLease = 86400;
            when(nc.getDhcpServerName()).thenReturn(name);
            when(nc.getDhcpMinLeaseTime()).thenReturn(minLease);
            when(nc.getDhcpDefaultLeaseTime()).thenReturn(defaultLease);
            when(nc.getDhcpMaxLeaseTime()).thenReturn(maxLease);

            IDHCPGlobalConfig idhcpConfig = Mockito.mock(IDHCPGlobalConfig.class);
            when(idhcpConfig.getMinLeaseTime()).thenReturn(minLease);
            when(idhcpConfig.getDefaultLeaseTime()).thenReturn(1000L);
            when(idhcpConfig.getMaxLeaseTime()).thenReturn(maxLease);

            IDHCPServer idhcpServer = Mockito.mock(IDHCPServer.class);
            when(vbox.findDHCPServerByNetworkName(Mockito.anyString())).thenReturn(idhcpServer);
            when(idhcpServer.getGlobalConfig()).thenReturn(idhcpConfig);

            when(vboxnc.checkConfig(Mockito.any(), Mockito.any())).thenCallRealMethod();

            Assertions.assertArrayEquals(new Boolean[]{true, false}, vboxnc.checkConfig(nc, vbox));
        }

        @Test
        void searchResultTrueAndMaxLeaseMismatchedShouldReturnArrayOfTrueAndFalse(){
            nc = Mockito.mock(NetworkConfiguration.class);

            when(vboxnc.search(Mockito.any(),Mockito.any())).thenReturn(true);

            String name = "HostInterfaceNetworking-vboxnet0";
            long minLease = 600;
            long defaultLease = 28800;
            long maxLease = 86400;
            when(nc.getDhcpServerName()).thenReturn(name);
            when(nc.getDhcpMinLeaseTime()).thenReturn(minLease);
            when(nc.getDhcpDefaultLeaseTime()).thenReturn(defaultLease);
            when(nc.getDhcpMaxLeaseTime()).thenReturn(maxLease);

            IDHCPGlobalConfig idhcpConfig = Mockito.mock(IDHCPGlobalConfig.class);
            when(idhcpConfig.getMinLeaseTime()).thenReturn(minLease);
            when(idhcpConfig.getDefaultLeaseTime()).thenReturn(defaultLease);
            when(idhcpConfig.getMaxLeaseTime()).thenReturn(1000000L);

            IDHCPServer idhcpServer = Mockito.mock(IDHCPServer.class);
            when(vbox.findDHCPServerByNetworkName(Mockito.anyString())).thenReturn(idhcpServer);
            when(idhcpServer.getGlobalConfig()).thenReturn(idhcpConfig);

            when(vboxnc.checkConfig(Mockito.any(), Mockito.any())).thenCallRealMethod();

            Assertions.assertArrayEquals(new Boolean[]{true, false}, vboxnc.checkConfig(nc, vbox));
        }
    }

    @Nested
    class TestSearch {
        @BeforeEach
        void ClassObjectDeclaration(){
            vboxnc = new VirtualBoxNetworkConfigController();
        }

        @Test
        void searchInvalidDHCPNameShouldFail() {
            nc = new NetworkConfiguration("HostInterfaceNetworking-vboxnet1", 600, 28800, 86400);
            when(vbox.findDHCPServerByNetworkName(Mockito.anyString())).thenThrow(new NullPointerException("..") {
            });
            Assertions.assertEquals(false, vboxnc.search(nc, vbox));
        }

        @Test
        void searchValidDHCPNameShouldPassed() {
            nc = new NetworkConfiguration("HostInterfaceNetworking-vboxnet0", 600, 28800, 86400);
            when(vbox.findDHCPServerByNetworkName(Mockito.anyString())).thenReturn(new IDHCPServer(null));
            Assertions.assertEquals(true, vboxnc.search(nc, vbox));
        }
    }
}