package virtualbox;

import generic.MachineController;
import generic.VirtualConfig;
import generic.VirtualInstance;
import org.virtualbox_6_1.*;

import java.util.ListIterator;

public class VirtualBoxController implements MachineController {

    private IVirtualBox vbox;

    public VirtualBoxController() {
        vbox = VirtualBoxManager.createInstance(null).getVBox();
    }

    @Override
    public void create(VirtualInstance vi) {

    }

    @Override
    public void up(VirtualInstance vi) {

    }

    @Override
    public void stop(VirtualInstance vi) {

    }

    @Override
    public void delete(VirtualInstance vi) {

    }
}
