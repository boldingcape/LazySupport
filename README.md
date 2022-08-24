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


To clone vm, maybe can refer to IMachine.cloneTo

https://superuser.com/questions/634195/how-to-get-ip-address-assigned-to-vm-running-in-background <br/>
If want to find the machine ip address, there are 2 ways:
1. Install virtualbox guest addition, and use IMachine.getGuestProperty
    - When insert virtualbox guest addition disk, can use "blkid" to find out the location of the disk then mount it.
2. Automate the release and assigning of ip address, then use IDHCPServer to find the ip address leased to the MAC address
    - If want to use this method, need to rebuild the base CENTOS 7 machine
    - can use dhclient -r to release existing ip
    - dhclient for new ip

To execute command in Guest OS, there are 2 ways:
1. Install virtualbox guest addition, and use vboxmanage equivalent JAVA SDK to guest control
2. execute command using ssh

