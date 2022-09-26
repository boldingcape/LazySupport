package generic;

import org.virtualbox_6_1.Holder;
import org.virtualbox_6_1.IVirtualBox;

public interface NetworkConfigInterface {
    public Boolean[] checkConfig(NetworkConfig nc, IVirtualBox vbox);
    public boolean create(NetworkConfig nc, IVirtualBox vbox,  IHostNetworkInterfaceHolder IHostNetworkInterfaceHolder);
    public boolean modify(NetworkConfig nc, IVirtualBox vbox);
    public boolean delete(NetworkConfig nc, IVirtualBox vbox, IHostNetworkInterfaceHolder IHostNetworkInterfaceHolder);
}
