package plugin.vagrant;

import console.Query;
import plugin.generic.VirtualMachinePluginChecker;

import java.io.*;

public class VagrantPluginChecker implements VirtualMachinePluginChecker {
    private boolean status = false;
    private String[] commands = {"vagrant", "--version"};
    public String[] getCommands() {
        return commands;
    }

    public void setCommands(String[] commands) {
        this.commands = commands;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public void run() {
        try {
            Query q = new Query();
            q.request(getCommands());
            if (q.getRequestState() == 0){
                setStatus(true);
            }
        }
        catch (Exception e){
            System.out.println("Invalid Command. Wrapped by: " + e);
        }
    }
}
