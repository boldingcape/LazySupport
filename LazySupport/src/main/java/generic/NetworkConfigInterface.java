package generic;

import org.virtualbox_6_1.IVirtualBox;

public interface NetworkConfigInterface {
    public Boolean[] checkConfig(NetworkConfiguration nc, IVirtualBox vbox);
    public boolean create(NetworkConfiguration nc, IVirtualBox vbox, IHostNetworkInterfaceHolder IHostNetworkInterfaceHolder);
    public boolean modify(NetworkConfiguration nc, IVirtualBox vbox);
    public boolean delete(NetworkConfiguration nc, IVirtualBox vbox, IHostNetworkInterfaceHolder IHostNetworkInterfaceHolder);
}
