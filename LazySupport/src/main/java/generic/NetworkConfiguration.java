package generic;

public class NetworkConfiguration {

    private String dhcpServerName;
    private long dhcpMinLeaseTime;
    private long dhcpDefaultLeaseTime;
    private long dhcpMaxLeaseTime;

    public NetworkConfiguration(String dhcpServerName, long dhcpMinLeaseTime, long dhcpDefaultLeaseTime, long dhcpMaxLeaseTime) throws IllegalArgumentException {
        if (dhcpMaxLeaseTime > dhcpMinLeaseTime && dhcpDefaultLeaseTime >= dhcpMinLeaseTime && dhcpDefaultLeaseTime <= dhcpMaxLeaseTime) {
            this.dhcpServerName = dhcpServerName;
            this.dhcpMinLeaseTime = dhcpMinLeaseTime;
            this.dhcpDefaultLeaseTime = dhcpDefaultLeaseTime;
            this.dhcpMaxLeaseTime = dhcpMaxLeaseTime;
        }
        else{
            throw new IllegalArgumentException("Invalid DHCP leasing value.");
        }
    }

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
        if (dhcpMinLeaseTime < dhcpMaxLeaseTime && dhcpMinLeaseTime <= dhcpDefaultLeaseTime) {
            this.dhcpMinLeaseTime = dhcpMinLeaseTime;
        }
    }

    public long getDhcpDefaultLeaseTime() {
        return dhcpDefaultLeaseTime;
    }

    public void setDhcpDefaultLeaseTime(long dhcpDefaultLeaseTime) {
        if (dhcpDefaultLeaseTime <= dhcpMaxLeaseTime && dhcpDefaultLeaseTime >= dhcpMinLeaseTime) {
            this.dhcpDefaultLeaseTime = dhcpDefaultLeaseTime;
        }
    }

    public long getDhcpMaxLeaseTime() {
        return dhcpMaxLeaseTime;
    }

    public void setDhcpMaxLeaseTime(long dhcpMaxLeaseTime) {
        if (dhcpMaxLeaseTime > dhcpMinLeaseTime && dhcpMaxLeaseTime >= dhcpDefaultLeaseTime) {
            this.dhcpMaxLeaseTime = dhcpMaxLeaseTime;
        }
    }
}
