package generic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.virtualbox_6_1.IHostNetworkInterface;
import org.virtualbox_6_1.Holder;

import static org.junit.jupiter.api.Assertions.*;

class IHostNetworkInterfaceHolderTest {
    @Test
    void getHolderIsHolderType(){
        Holder<IHostNetworkInterface> hostif = Mockito.mock(Holder.class);
        IHostNetworkInterfaceHolder iHostNetworkInterfaceHolder = new IHostNetworkInterfaceHolder(hostif);
        Assertions.assertTrue((iHostNetworkInterfaceHolder.getHolder() instanceof Holder<?>));
    }

    @Test
    void getHolderValueIsIHostNetworkInterfaceType(){
        Holder<IHostNetworkInterface> hostif = Mockito.mock(Holder.class);
        hostif.value = Mockito.mock(IHostNetworkInterface.class);
        IHostNetworkInterfaceHolder iHostNetworkInterfaceHolder = new IHostNetworkInterfaceHolder(hostif);
        Assertions.assertTrue((iHostNetworkInterfaceHolder.getHolderValue() instanceof IHostNetworkInterface));
    }
}