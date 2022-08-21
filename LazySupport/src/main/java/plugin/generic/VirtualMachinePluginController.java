package plugin.generic;

public interface VirtualMachinePluginController {
    public void create(VirtualInstance vi);
    public void up(VirtualInstance vi);
    public void stop(VirtualInstance vi);
    public void delete(VirtualInstance vi);
    public void validatePluginStatus();
}
