package generic;

import org.virtualbox_6_1.Holder;
import org.virtualbox_6_1.IHostNetworkInterface;

public class IHostNetworkInterfaceHolder{
    private Holder h;
    public IHostNetworkInterfaceHolder(Holder h) {
        this.h = h;
    }

    public Holder getHolder() {
        return h;
    }

    public IHostNetworkInterface getHolderValue(){
        return (IHostNetworkInterface) this.h.value;
    }

}
