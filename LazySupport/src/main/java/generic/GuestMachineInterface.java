package generic;

import org.virtualbox_6_1.IVirtualBox;

public interface GuestMachineInterface {
    public void create(IVirtualBox vbox);
    public void up(IVirtualBox vbox);
    public void stop(IVirtualBox vbox);
    public void delete(IVirtualBox vbox);
}
