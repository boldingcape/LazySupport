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
    public void configDhcp(VirtualInstance vi) {
        DhcpServerResult dhcpResult = matchDhcp(vi.getVc(), vbox.getDHCPServers().listIterator());

        //if the return response is not null, it mean it found a matching dhcpServer
        if (dhcpResult.getIdhcpServer() != null){
            if (!dhcpResult.getMatchStatus()){
                dhcpResult.getIdhcpServer().getGlobalConfig().setMinLeaseTime(vi.getVc().getDhcpMinLeaseTime());
                dhcpResult.getIdhcpServer().getGlobalConfig().setDefaultLeaseTime(vi.getVc().getDhcpDefaultLeaseTime());
                dhcpResult.getIdhcpServer().getGlobalConfig().setMaxLeaseTime(vi.getVc().getDhcpMaxLeaseTime());
                dhcpResult.getIdhcpServer().restart();
            }
        }
        else{
            //ToDo: Create DHCPServer to be used.
        }
    }

    private DhcpServerResult matchDhcp(VirtualConfig vc, ListIterator<IDHCPServer> idhcpServerListIterator){
        //return first matched
        if (idhcpServerListIterator.hasNext()){
            IDHCPServer idhcpServer = idhcpServerListIterator.next();
            if (idhcpServer.getNetworkName() == vc.getDhcpServerName()){
                IDHCPConfig idhcpConfig = idhcpServer.getGlobalConfig();
                if (idhcpConfig.getMinLeaseTime() == vc.getDhcpMinLeaseTime() && idhcpConfig.getDefaultLeaseTime() == vc.getDhcpDefaultLeaseTime() && idhcpConfig.getMaxLeaseTime() == vc.getDhcpMaxLeaseTime()){
                    return new DhcpServerResult(idhcpServer, true);
                }
                else{
                    return new DhcpServerResult(idhcpServer, false);
                }
            }
        }
        return new DhcpServerResult(null, false);
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

    public class DhcpServerResult {
        private IDHCPServer idhcpServer = null;
        private Boolean matchStatus = false;
        public DhcpServerResult(IDHCPServer idhcpServer, Boolean matchStatus) {
            this.idhcpServer = idhcpServer;
            this.matchStatus = matchStatus;
        }

        public IDHCPServer getIdhcpServer() {
            return idhcpServer;
        }

        public void setIdhcpServer(IDHCPServer idhcpServer) {
            this.idhcpServer = idhcpServer;
        }

        public Boolean getMatchStatus() {
            return matchStatus;
        }

        public void setMatchStatus(Boolean matchStatus) {
            this.matchStatus = matchStatus;
        }
    }
}