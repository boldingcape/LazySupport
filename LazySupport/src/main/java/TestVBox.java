/* $Id: TestVBox.java 135976 2020-02-04 10:35:17Z bird $ */
/*! file
 * Small sample/testcase which demonstrates that the same source code can
 * be used to connect to the webservice and (XP)COM APIs.
 */

/*
 * Copyright (C) 2010-2020 Oracle Corporation
 *
 * This file is part of VirtualBox Open Source Edition (OSE), as
 * available from http://www.virtualbox.org. This file is free software;
 * you can redistribute it and/or modify it under the terms of the GNU
 * General Public License (GPL) as published by the Free Software
 * Foundation, in version 2 as it comes in the "COPYING" file of the
 * VirtualBox OSE distribution. VirtualBox OSE is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY of any kind.
 */

import org.virtualbox_6_1.*;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.math.BigInteger;

import static java.lang.Thread.sleep;

public class TestVBox
{
    static void processEvent(IEvent ev)
    {
        System.out.println("got event: " + ev);
        VBoxEventType type = ev.getType();
        System.out.println("type = " + type);
        switch (type)
        {
            case OnMachineStateChanged:
            {
                IMachineStateChangedEvent mcse = IMachineStateChangedEvent.queryInterface(ev);
                if (mcse == null)
                    System.out.println("Cannot query an interface");
                else
                    System.out.println("mid=" + mcse.getMachineId());
                break;
            }
        }
    }

    static class EventHandler
    {
        EventHandler() {}
        public void handleEvent(IEvent ev)
        {
            try {
                processEvent(ev);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    static void testEvents(VirtualBoxManager mgr, IEventSource es)
    {
        // active mode for Java doesn't fully work yet, and using passive
        // is more portable (the only mode for MSCOM and WS) and thus generally
        // recommended
        IEventListener listener = es.createListener();

        es.registerListener(listener, Arrays.asList(VBoxEventType.Any), false);

        try {
            for (int i = 0; i < 50; i++)
            {
                System.out.print(".");
                IEvent ev = es.getEvent(listener, 500);
                if (ev != null)
                {
                    processEvent(ev);
                    es.eventProcessed(listener, ev);
                }
                // process system event queue
                mgr.waitForEvents(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        es.unregisterListener(listener);
    }

    static void testEnumeration(VirtualBoxManager mgr, IVirtualBox vbox)
    {
        List<IMachine> machs = vbox.getMachines();
        for (IMachine m : machs)
        {
            String name;
            Long ram = 0L;
            boolean hwvirtEnabled = false, hwvirtNestedPaging = false;
            boolean paeEnabled = false;
            boolean inaccessible = false;
            try
            {
                name = m.getName();
                ram = m.getMemorySize();
                hwvirtEnabled = m.getHWVirtExProperty(HWVirtExPropertyType.Enabled);
                hwvirtNestedPaging = m.getHWVirtExProperty(HWVirtExPropertyType.NestedPaging);
                paeEnabled = m.getCPUProperty(CPUPropertyType.PAE);
                String osType = m.getOSTypeId();
                IGuestOSType foo = vbox.getGuestOSType(osType);
            }
            catch (VBoxException e)
            {
                name = "<inaccessible>";
                inaccessible = true;
            }
            System.out.println("VM name: " + name);
            if (!inaccessible)
            {
                System.out.println(" RAM size: " + ram + "MB"
                        + ", HWVirt: " + hwvirtEnabled
                        + ", Nested Paging: " + hwvirtNestedPaging
                        + ", PAE: " + paeEnabled);
            }
        }
        // process system event queue
        mgr.waitForEvents(0);
    }

    static boolean progressBar(VirtualBoxManager mgr, IProgress p, long waitMillis)
    {
        long end = System.currentTimeMillis() + waitMillis;
        while (!p.getCompleted())
        {
            // process system event queue
            mgr.waitForEvents(0);
            // wait for completion of the task, but at most 200 msecs
            p.waitForCompletion(200);
            if (System.currentTimeMillis() >= end)
                return false;
        }
        return true;
    }

    static void testStart(VirtualBoxManager mgr, IVirtualBox vbox)
    {
        IMachine m = vbox.getMachines().get(0);
        String name = m.getName();
        System.out.println("\nAttempting to start VM '" + name + "'");

        ISession session = mgr.getSessionObject();
        ArrayList<String> env = new ArrayList<String>();
        IProgress p = m.launchVMProcess(session, "gui", env);
        progressBar(mgr, p, 10000);
        System.out.println("Progress Bar wait completed");
        session.unlockMachine();
        // process system event queue
        mgr.waitForEvents(0);
    }

    static void testMultiServer()
    {
        VirtualBoxManager mgr1 = VirtualBoxManager.createInstance(null);
        VirtualBoxManager mgr2 = VirtualBoxManager.createInstance(null);

        try {
            mgr1.connect("http://i7:18083", "", "");
            mgr2.connect("http://main:18083", "", "");

            IMachine m1 = mgr1.getVBox().getMachines().get(0);
            IMachine m2 = mgr2.getVBox().getMachines().get(0);
            String name1 = m1.getName();
            String name2 = m2.getName();
            ISession session1 = mgr1.getSessionObject();
            ISession session2 = mgr2.getSessionObject();
            ArrayList<String> env = new ArrayList<String>();
            IProgress p1 = m1.launchVMProcess(session1, "gui", env);
            IProgress p2 = m2.launchVMProcess(session2, "gui", env);
            progressBar(mgr1, p1, 10000);
            progressBar(mgr2, p2, 10000);
            session1.unlockMachine();
            session2.unlockMachine();
            // process system event queue
            mgr1.waitForEvents(0);
            mgr2.waitForEvents(0);
        } finally {
            mgr1.cleanup();
            mgr2.cleanup();
        }
    }

    static void testReadLog(VirtualBoxManager mgr, IVirtualBox vbox)
    {
        IMachine m =  vbox.getMachines().get(0);
        long logNo = 0;
        long off = 0;
        long size = 16 * 1024;
        while (true)
        {
            byte[] buf = m.readLog(logNo, off, size);
            if (buf.length == 0)
                break;
            System.out.print(new String(buf));
            off += buf.length;
        }
        // process system event queue
        mgr.waitForEvents(0);
    }

    static void printErrorInfo(VBoxException e)
    {
        System.out.println("VBox error: " + e.getMessage());
        System.out.println("Error cause message: " + e.getCause());
        System.out.println("Overall result code: " + Integer.toHexString(e.getResultCode()));
        int i = 1;
        for (IVirtualBoxErrorInfo ei = e.getVirtualBoxErrorInfo(); ei != null; ei = ei.getNext(), i++)
        {
            System.out.println("Detail information #" + i);
            System.out.println("Error mesage: " + ei.getText());
            System.out.println("Result code:  " + Integer.toHexString(ei.getResultCode()));
            // optional, usually provides little additional information:
            System.out.println("Component:    " + ei.getComponent());
            System.out.println("Interface ID: " + ei.getInterfaceID());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        VirtualBoxManager mgr = VirtualBoxManager.createInstance(null);
        IVirtualBox vbox = mgr.getVBox();


        //IMachine im = vbox.createMachine(null, "TEST-codeCreation", null, vbox.getMachines().get(0).getOSTypeId(), null);
        //im.saveSettings();
        //vbox.registerMachine(im);



//        IDHCPServer dhcpServer1 = vbox.createDHCPServer("HostInterfaceNetworking-vboxnet1");
//        String ip = "192.168.57.1";
//        dhcpServer1.setConfiguration(ip, "255.255.255.0", ("192.168.57.1".substring(0, ip.length()-1)+"2") , ("192.168.57.1".substring(0, ip.length()-1)+"254"));
//        dhcpServer1.setEnabled(true);
//        System.out.println("dhcp network name: " + dhcpServer1.getNetworkName());
//        System.out.println("dhcp ip address: " + dhcpServer1.getIPAddress());
//        System.out.println("dhcp network mask: " + dhcpServer1.getNetworkMask());
//        System.out.println("dhcp lower: " + dhcpServer1.getLowerIP());
//        System.out.println("dhcp upper: " + dhcpServer1.getUpperIP());
//        vbox.removeDHCPServer(vbox.findDHCPServerByNetworkName("HostInterfaceNetworking-vboxnet1"));

//        IHost host = vbox.getHost();
//        Holder<IHostNetworkInterface> hostinterface = new Holder<>();
//        host.createHostOnlyNetworkInterface(hostinterface);
//        System.out.println("new interface name: " + hostinterface.value.getName());
//        System.out.println("new interface network name: " + hostinterface.value.getNetworkName());
//        System.out.println("new interface ip add: " + hostinterface.value.getIPAddress());
//        System.out.println("new interface network mask: " + hostinterface.value.getNetworkMask());

        for (IHostNetworkInterface ihost: vbox.getHost().getNetworkInterfaces()){
            if (ihost.getNetworkName().equals("HostInterfaceNetworking-vboxnet1") ){
                System.out.println("ihost name: " + ihost.getName());
                System.out.println("ihost network: " + ihost.getNetworkName());
                System.out.println("ihost ip:" + ihost.getIPAddress());
                System.out.println("ihost status: " + ihost.getStatus().name());
                System.out.println("ihost DHCP enabled? " + ihost.getDHCPEnabled());
                ihost.enableStaticIPConfig("192.168.58.1", "255.255.255.0");
            }
            System.out.println("");
        }

        //Wondering how is DHCPServer finding the network interface to work on
        //Is it because both the interface and dhcpserver have the same network name? don't think so, as it is just a string value
        //Does it mean they are within each other network subnet range?
        //Might be worth referencing the manual for slightly more information
        //Using vboxmanage, can specify the network or interface. However, SDK cannot.

        for (IDHCPServer dhcpServer: vbox.getDHCPServers()){
            System.out.println("dhcpServer network name: " + dhcpServer.getNetworkName());
            System.out.println("dhcpServer ip: " + dhcpServer.getIPAddress());
            System.out.println("is dhcpServer enabled? " + dhcpServer.getEnabled());

//            if (dhcpServer.getNetworkName().equals("HostInterfaceNetworking-vboxnet2")){
//                dhcpServer.setEnabled(true);
//            }

//            if (dhcpServer.getNetworkName().equals("HostInterfaceNetworking-vboxnet0")){
//
//                IDHCPConfig dhcpConfig = dhcpServer.getConfig(DHCPConfigScope.Global,null, (long) 0, true);
//                Holder<List<DHCPOption>> a = new Holder<>();
//                Holder<List<DHCPOptionEncoding>> b = new Holder<>();
//                dhcpConfig.getAllOptions(a, b);
//                System.out.println("dhcpOption: " + a.value.toString());
//                System.out.println("dhcpOptionEncoding: " + b.value.toString());
//                dhcpConfig.setMinLeaseTime((long) 600);
//                dhcpConfig.setDefaultLeaseTime((long) 28800);
//                dhcpConfig.setMaxLeaseTime((long) 86400);
//
//
//
//                //Don't really understand how does dhcpServer.start(), stop() work. As far as I can see, dhcpServer is start running from the start
//
//                Holder<String> address = new Holder<>();
//                Holder<String> state = new Holder<>();
//                Holder<Long> issued = new Holder<>();
//                Holder<Long> expired = new Holder<>();
//                // The integer refer to the index of ip address position, since you can assign more than 1 ip address to a device
//                dhcpServer.findLeaseByMAC("080027AEB98E", 0, address, state, issued, expired);
//                System.out.println("address: " + address.value);
//            }
            System.out.println("");
        }


//        for (IMachine imach: mgr.getVBox().getMachines()) {
//            if (imach.getName().contains("TEST")) {
//                System.out.println("TEST property");
//                System.out.println(imach.getHardwareUUID());
//                System.out.println(imach.getGuestPropertyValue("/VirtualBox/HostInfo/GUI/LanguageID"));
//                System.out.println(imach.getState());
//                System.out.println(imach.getSettingsFilePath());
//            }
//        }

    }

}
