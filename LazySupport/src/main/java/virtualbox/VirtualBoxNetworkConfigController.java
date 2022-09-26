package virtualbox;

import generic.IHostNetworkInterfaceHolder;
import generic.NetworkConfigInterface;
import generic.NetworkConfiguration;
import org.virtualbox_6_1.*;

public class VirtualBoxNetworkConfigController implements NetworkConfigInterface {
    @Override
    public Boolean[] checkConfig(NetworkConfiguration nc, IVirtualBox vbox) {
        Boolean[] result = new Boolean[]{false, false};
        if (!search(nc, vbox)){
            return result;
        }
        else {
            result[0] = true;
            IDHCPGlobalConfig idhcpConfig = vbox.findDHCPServerByNetworkName(nc.getDhcpServerName()).getGlobalConfig();
            if (idhcpConfig.getMinLeaseTime() == nc.getDhcpMinLeaseTime() && idhcpConfig.getDefaultLeaseTime() == nc.getDhcpDefaultLeaseTime() && idhcpConfig.getMaxLeaseTime() == nc.getDhcpMaxLeaseTime()){
                result[1] = true;
            }
        }
        return result;
    }

    @Override
    public boolean create(NetworkConfiguration nc, IVirtualBox vbox, IHostNetworkInterfaceHolder iHostNetworkInterfaceHolder) {
        if (!search(nc, vbox)) {
            vbox.getHost().createHostOnlyNetworkInterface(iHostNetworkInterfaceHolder.getHolder());
            IHostNetworkInterface ihostnetworkinterface = iHostNetworkInterfaceHolder.getHolderValue();
            IDHCPServer dhcpServer = vbox.createDHCPServer(ihostnetworkinterface.getNetworkName());
            dhcpServer.setConfiguration(ihostnetworkinterface.getIPAddress(),
                    ihostnetworkinterface.getNetworkMask(),
                    (ihostnetworkinterface.getIPAddress().substring(0, ihostnetworkinterface.getIPAddress().length() - 1) + "2"),
                    (ihostnetworkinterface.getIPAddress().substring(0, ihostnetworkinterface.getIPAddress().length() - 1) + "254"));
            dhcpServer.setEnabled(true);
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean modify(NetworkConfiguration nc, IVirtualBox vbox) {
            if (!search(nc, vbox)) {
                IDHCPServer idhcpServer = vbox.findDHCPServerByNetworkName(nc.getDhcpServerName());
                idhcpServer.getGlobalConfig().setMinLeaseTime(nc.getDhcpMinLeaseTime());
                idhcpServer.getGlobalConfig().setDefaultLeaseTime(nc.getDhcpDefaultLeaseTime());
                idhcpServer.getGlobalConfig().setMaxLeaseTime(nc.getDhcpMaxLeaseTime());
                idhcpServer.restart();
                return true;
            }
            else{
                return false;
            }
    }

    @Override
    public boolean delete(NetworkConfiguration nc, IVirtualBox vbox, IHostNetworkInterfaceHolder IHostNetworkInterfaceHolder) {
        if (!search(nc, vbox)){
            vbox.removeDHCPServer(vbox.findDHCPServerByNetworkName(nc.getDhcpServerName()));
            vbox.getHost().removeHostOnlyNetworkInterface(IHostNetworkInterfaceHolder.getHolderValue().getId());
            return true;
        }
        else {
            return false;
        }
    }

    public boolean search(NetworkConfiguration nc, IVirtualBox vbox){
        try {
            return vbox.findDHCPServerByNetworkName(nc.getDhcpServerName()) instanceof IDHCPServer ? true:false;
        }
        catch (NullPointerException e){
            return false;
        }
    }
}
