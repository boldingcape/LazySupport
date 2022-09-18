package generic;

import org.virtualbox_6_1.IVirtualBox;

public interface NetworkInterfaceConfig {
    public Boolean[] check(VirtualConfig vc, IVirtualBox vbox);
    public boolean create(VirtualConfig vc, IVirtualBox vbox) throws Exception;
    public boolean modify(VirtualConfig vc, IVirtualBox vbox) throws Exception;
    public boolean delete(VirtualConfig vc, IVirtualBox vbox);
}
