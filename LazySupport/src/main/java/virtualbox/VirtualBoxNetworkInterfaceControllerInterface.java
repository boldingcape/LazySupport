package virtualbox;

import generic.NetworkInterfaceConfig;
import generic.VirtualConfig;
import org.virtualbox_6_1.*;

public class VirtualBoxNetworkInterfaceControllerInterface implements NetworkInterfaceConfig {
    @Override
    public Boolean[] check(VirtualConfig vc, IVirtualBox vbox) {
        Boolean[] result = new Boolean[]{false, false};
        if (vbox.findDHCPServerByNetworkName(vc.getDhcpServerName()) == null){
            return result;
        }
        else {
            result[0] = true;
            IDHCPConfig idhcpConfig = vbox.findDHCPServerByNetworkName(vc.getDhcpServerName()).getGlobalConfig();
            if (idhcpConfig.getMinLeaseTime() == vc.getDhcpMinLeaseTime() && idhcpConfig.getDefaultLeaseTime() == vc.getDhcpDefaultLeaseTime() && idhcpConfig.getMaxLeaseTime() == vc.getDhcpMaxLeaseTime()){
                result[1] = true;
            }
        }
        return result;
    }

    @Override
    public boolean create(VirtualConfig vc, IVirtualBox vbox) throws Exception {
        try {
            Holder<IHostNetworkInterface> hostif = new Holder<>();
            vbox.getHost().createHostOnlyNetworkInterface(hostif);
            IDHCPServer dhcpServer = vbox.createDHCPServer(hostif.value.getNetworkName());
            dhcpServer.setConfiguration(hostif.value.getIPAddress(),
                    hostif.value.getNetworkMask(),
                    (hostif.value.getIPAddress().substring(0, hostif.value.getIPAddress().length() - 1) + "2"),
                    (hostif.value.getIPAddress().substring(0, hostif.value.getIPAddress().length() - 1) + "254"));
            dhcpServer.setEnabled(true);
            return true;
        }
        catch (Exception e){
            throw new Exception(e);
        }
    }

    @Override
    public boolean modify(VirtualConfig vc, IVirtualBox vbox) throws Exception {
        try {
            vbox.findDHCPServerByNetworkName(vc.getDhcpServerName()).getGlobalConfig().setMinLeaseTime(vc.getDhcpMinLeaseTime());
            vbox.findDHCPServerByNetworkName(vc.getDhcpServerName()).getGlobalConfig().setDefaultLeaseTime(vc.getDhcpDefaultLeaseTime());
            vbox.findDHCPServerByNetworkName(vc.getDhcpServerName()).getGlobalConfig().setMaxLeaseTime(vc.getDhcpMaxLeaseTime());
            vbox.findDHCPServerByNetworkName(vc.getDhcpServerName()).restart();
            return true;
        }
        catch (Exception e){
            throw new Exception(e);
        }
    }

    @Override
    public boolean delete(VirtualConfig vc, IVirtualBox vbox) {
        return false;
    }
}
