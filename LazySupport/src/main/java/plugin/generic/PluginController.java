package plugin.generic;

public abstract class PluginController implements VirtualMachinePluginController{
    private boolean status = false;
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    @Override
    public abstract void create(VirtualInstance vi);
    @Override
    public abstract void up(VirtualInstance vi);
    @Override
    public abstract void stop(VirtualInstance vi);
    @Override
    public abstract void delete(VirtualInstance vi);
    @Override
    public abstract void validatePluginStatus();

}
