package generic;

public class VirtualConfig {

    private String dhcpServerName;
    private long dhcpMinLeaseTime;
    private long dhcpDefaultLeaseTime;
    private long dhcpMaxLeaseTime;

    public String getDhcpServerName() {
        return dhcpServerName;
    }

    public void setDhcpServerName(String dhcpServerName) {
        this.dhcpServerName = dhcpServerName;
    }

    public long getDhcpMinLeaseTime() {
        return dhcpMinLeaseTime;
    }

    public void setDhcpMinLeaseTime(long dhcpMinLeaseTime) {
        this.dhcpMinLeaseTime = dhcpMinLeaseTime;
    }

    public long getDhcpDefaultLeaseTime() {
        return dhcpDefaultLeaseTime;
    }

    public void setDhcpDefaultLeaseTime(long dhcpDefaultLeaseTime) {
        this.dhcpDefaultLeaseTime = dhcpDefaultLeaseTime;
    }

    public long getDhcpMaxLeaseTime() {
        return dhcpMaxLeaseTime;
    }

    public void setDhcpMaxLeaseTime(long dhcpMaxLeaseTime) {
        this.dhcpMaxLeaseTime = dhcpMaxLeaseTime;
    }
}
