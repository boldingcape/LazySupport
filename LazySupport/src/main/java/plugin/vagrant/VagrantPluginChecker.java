package plugin.vagrant;

import plugin.generic.VirtualMachinePluginChecker;

import java.io.*;

public class VagrantPluginChecker implements VirtualMachinePluginChecker {
    private boolean status = false;
    @Override
    public void run() {
        try {
            Runtime rt = Runtime.getRuntime();
            String[] commands = {"vagrant", "--version"};
            Process proc = rt.exec(commands);
            proc.waitFor();
            if (proc.exitValue() == 0){
                this.status=true;
            }
            else {
                this.status=false;
            }
        }
        catch (IOException e){
            System.out.println("Command line query for vagrant status failed: " + e.getStackTrace());
        } catch (InterruptedException e) {
            throw new RuntimeException("Waiting for response of query failed: " + e.getStackTrace());
        }
    }

    @Override
    public boolean status() {
        if (status){
            System.out.println("Vagrant plugin detected -- Successfully");
            return true;
        }
        System.out.println("Vagrant plugin detected -- Failed");
        return false;
    }

}
