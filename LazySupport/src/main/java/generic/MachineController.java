package generic;

public interface MachineController {
    public void configDhcp(VirtualInstance vi);
    public void create(VirtualInstance vi);
    public void up(VirtualInstance vi);
    public void stop(VirtualInstance vi);
    public void delete(VirtualInstance vi);
}
