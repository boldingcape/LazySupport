# LazySupport
Streamline working environment setup <br/>
SDK API library reference:
https://download.virtualbox.org/virtualbox/4.1.20/SDKRef.pdf

The following VM parameter is required:
-Dvbox.home=</path/to/vb/binary> <br/>
-Dsun.boot.library.path=</path/to/vb/libvboxjxpcom.jnilib>

Example: <br/>
-Dsun.boot.library.path=/Applications/VirtualBox.app/Contents/MacOS <br/>
-Dvbox.home=/Applications/VirtualBox.app/Contents/MacOS

Base VM Image requisite:
1. Expose port 8080/tcp and 8443/tcp
2. Create a service which run bash script that call for dhclient -i <network-interface>

DHCP Server behaviour:
- it is lazy reactive, the status of expired ip will be update only when request to DHCP is done.
- if all ip within the range provide are leased, new lease request will not be entertained.
  - This bring the question why stick to 100-254?

General Flow of running a machine from creation:
- Create -> Clone -> Start -> Check Machine Status -> Ready -> Get IP <br/>
-> SSH into Machine -> Run Script

General Flow of running a machine on subsequent:
- Check if Machine exist -> Start -> Check Machine Status -> Ready -> Get IP 

To find all existing lease, refer to: https://superuser.com/questions/1502063/how-can-i-show-all-active-leases-of-a-virtualbox-dhcp-server

To clone vm, refer: https://forums.virtualbox.org/viewtopic.php?t=65880

https://superuser.com/questions/634195/how-to-get-ip-address-assigned-to-vm-running-in-background <br/>
If want to find the machine ip address, there are 2 ways:
1. Install virtualbox guest addition, and use IMachine.getGuestProperty
    - When insert virtualbox guest addition disk, can use "blkid" to find out the location of the disk then mount it.
2. Automate the release and assigning of ip address, then use IDHCPServer to find the ip address leased to the MAC address
    - If want to use this method, need to rebuild the base CENTOS 7 machine
    - Find the guest machine MAC info
    - Can use dhclient -r to release existing ip
    - "dhclient" for new ip from dhcpServer

To execute command in Guest OS, there are 2 ways:
1. Install virtualbox guest addition, and use vboxmanage equivalent JAVA SDK to guest control
2. execute command using ssh

