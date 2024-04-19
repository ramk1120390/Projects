package com.Network.Network.DevicemetamodelApi;

import java.util.regex.*;

public class PortDescriptionPrinter {
    public static void main(String[] args) {
        String input = "{\"ChangedBy\":\"Not Available\",\"BackedupBy\":\"admin\",\"CapturedOn\":\"Oct 13, 2023 16:21 PM\",\"VersionNo\":1,\"LabelName\":\"BASELINE\",\"FileFormat\":\"Text\",\"Annotation\":\"Configuration stored as a result of manual backup operation.\",\"BASE_LINE_VERSION\":1421,\"versionId\":1421,\"baselineDiff\":{\"SAME_BASELINE\":true},\"FileType\":\"Running\",\"ChangeType\":0,\"Content\":\"\n" +
                "! 3916 Configuration File\n" +
                "! Chassis MAC: 2c:4a:11:8b:e7:60\n" +
                "! Host Name: va-wdst-att-cvs-ciena1\n" +
                "! Created: Fri Oct 13 20:21:17 2023\n" +
                "! Created by: CLI\n" +
                "! On terminal: \n" +
                "! SW Package: Slot 1 - saos-06-18-00-0183\n" +
                "! Build Number: 17502\n" +
                "! MIB Number: 04-18-00-0027\n" +
                "! Defaults: Suppressed\n" +
                "! Encoding: US-ASCII\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! SECLOG CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! ALARM MANAGER\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! RESOURCE CONFIG\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! Capture PROFILE CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! ACL PROFILE CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! ACL RULE CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! NETWORK CONFIG: vlans\n" +
                "!\n" +
                "vlan create vlan 280,300\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! CONGESTION AVOIDANCE PROFILE CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! RCOS QUEUE MAP CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! FCOS->RCOS MAP CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! RCOS->FCOS MAP CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! PROTOCOL->COS MAP CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! COMMAND LOG MANAGER\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! ROUTING DOMAIN\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! EVENT LOG MANAGER:\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! CHASSIS CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! SYSTEM CONFIG:\n" +
                "!\n" +
                "system set host-name va-wdst-att-cvs-ciena1\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! INTERFACE CONFIG:\n" +
                "!\n" +
                "interface remote set vlan 300\n" +
                "interface remote set ip 10.18.100.121/22 \n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! INTERFACE LOCAL CONFIG:\n" +
                "!\n" +
                "interface set gateway 10.18.100.1 \n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! IP INTERFACE CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! DNS CLIENT CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! NTP CONFIG:\n" +
                "!\n" +
                "ntp client add server 204.111.1.35\n" +
                "ntp client add server 204.111.1.36\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! SYSLOG CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! TWAMP CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! BFD GLOBAL\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! BFD PROFILES\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! BFD SESSIONS\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! AIS GLOBAL\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! AIS PROFILES\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! PORT CONFIG: ports\n" +
                "!\n" +
                "port set port 1 speed auto\n" +
                "port set port 1 max-frame-size 9216 description \"UNI BBEC/652210/ /STC 100M EPL cx AT&T\"\n" +
                "port set port 2 max-frame-size 9216 description \"MGMT va-wdst-att-cvs-ups1 (10.18.100.123)\"\n" +
                "port disable port 3\n" +
                "port set port 3 description OPEN\n" +
                "port disable port 4\n" +
                "port set port 4 description OPEN\n" +
                "port disable port 5\n" +
                "port set port 5 description OPEN\n" +
                "port set port 6 max-frame-size 9216 description \"NNI 1G P2P va-wdst-edge-8104-1 1/1/38\"\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! NETWORK CONFIG: vlan members and attributes\n" +
                "!\n" +
                "vlan add vlan 300 port 2\n" +
                "vlan add vlan 280,300 port 6\n" +
                "!\n" +
                "vlan rename vlan 280 name BBEC/652210//STC\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! NETWORK CONFIG: vlan cross-connect\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! NETWORK CONFIG: vlan translation\n" +
                "!\n" +
                "port set port 2 acceptable-frame-type untagged-only pvid 300 egress-untag-vlan 300 untagged-data-vid 300 vs-ingress-filter on\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! PORT STATE GROUP CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! SERVICE QUEUING CONFIG: \n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! SUB-PORT CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! VC TRANSFORM PROFILE CONFIG: l2-transform\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! MPLS CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! TUNNEL CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! VIRTUAL-CIRCUIT CONFIG: virtual circuits\n" +
                "!\n" +
                "virtual-circuit ethernet create vc vc280 vlan 280 statistics on\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! VIRTUAL-SWITCH CONFIG:\n" +
                "!\n" +
                "virtual-switch add reserved-vlan 4000\n" +
                "virtual-switch ethernet create vs BBEC/652210//STC reserved-vlan 4000 vc vc280 description BBEC/652210//STC\n" +
                "virtual-switch ethernet add vs BBEC/652210//STC port 1 encap-cos-policy port-inherit\n" +
                "virtual-circuit ethernet set port 1 vlan-ethertype-policy vlan-tpid\n" +
                "port set port 1 untagged-data-vs BBEC/652210//STC untagged-ctrl-vs BBEC/652210//STC\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! SUB-PORT VIRTUAL SWITCH ATTACHMENT CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! EGRESS PORT QUEUE GROUP CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! MISC QOS CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! FRAME INTEGRITY CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! AGING and LEARNING CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! SERVICE ACCESS CONTROL CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! STATIC MAC CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! BCAST FILTER CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! MAC MOTION CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! PFG CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! PORT-BASED PFG CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! IRB INTERFACE CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! MPLS GLOBAL FEATURE\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! MPLS-TE/GMPLS-TP LABEL RANGE\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! MPLS TE Attributes\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! MPLS FRR Global Attributes\n" +
                "!\n" +
                "\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! MPLS-TE TUNNEL AUTO-FACILITY-BYPASS PROFILE\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! MPLS TE Interface Attributes\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! RSVP-TE/GMPLS-RSVP-TE ENTITY\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! RSVP-TE/GMPLS-RSVP-TE INTERFACE\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! RSVP NEIGHBOR-AUTHENTICATION\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! MPLS/GMPLS RSVP-TE INTERFACES\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! RSVP PATH\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! MPLS-TE/GMPLS-TP TUNNEL QOS/BANDWIDTH PROFILE\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! MPLS-TE TUNNEL FAST-REROUTE PROFILE\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! MPLS/GMPLS TUNNEL-COS-PROFILE\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! MPLS-TE/GMPLS-TP INGRESS/EGRESS TUNNELS\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! MPLS STATIC TRANSIT TUNNEL\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! MPLS-TE AND GMPLS-TP BI-DIRECTIONAL ASSOCIATED TUNNEL\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! RSVP-TE/GMPLS-RSVP-TE GLOBAL\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! LDP GLOBAL SESSION CONFIGURATION\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! L2-VPN PSEUDOWIRE-COS-PROFILE\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! L2-VPN PSEUDOWIRE-QOS-PROFILE\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! L2-VPN PSEUDOWIRE-VCCV-PROFILE\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! L2-VPN PSEUDOWIRE-BFD-PROFILE\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! L2-VPN GLOBAL PSEUDOWIRE CONFIGURATION\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! L2-VPN STATIC and DYNAMIC PSEUDOWIREs\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! L2-VPN STATIC and DYNAMIC PROTECTION PSEUDOWIREs\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! RM\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! OSPF INSTANCES\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! ISIS INSTANCES\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! ISIS INTERFACES\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! ISIS SUMMARY ROUTES\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! ISIS ROUTE-LEAK\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! INTER-CHASSIS LINK CREATE CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! STATIC ARP\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! CPU IF CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! LACP CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! VIRTUAL-SWITCH MPLS AC CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! VIRTUAL-SWITCH MPLS-VC ATTACHMENT:\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! L2 CFT CONFIG:\n" +
                "!\n" +
                "l2-cft create profile BBEC/652210//STC\n" +
                "l2-cft protocol add profile BBEC/652210//STC ctrl-protocol cisco-cdp untagged-disposition forward\n" +
                "l2-cft protocol add profile BBEC/652210//STC ctrl-protocol cisco-dtp untagged-disposition forward\n" +
                "l2-cft protocol add profile BBEC/652210//STC ctrl-protocol cisco-pagp untagged-disposition forward\n" +
                "l2-cft protocol add profile BBEC/652210//STC ctrl-protocol cisco-udld untagged-disposition forward\n" +
                "l2-cft protocol add profile BBEC/652210//STC ctrl-protocol cisco-vtp untagged-disposition forward\n" +
                "l2-cft protocol add profile BBEC/652210//STC ctrl-protocol cisco-pvst untagged-disposition forward\n" +
                "l2-cft protocol add profile BBEC/652210//STC ctrl-protocol cisco-stp-uplink-fast untagged-disposition forward\n" +
                "l2-cft protocol add profile BBEC/652210//STC ctrl-protocol vlan-bridge untagged-disposition forward\n" +
                "l2-cft protocol add profile BBEC/652210//STC ctrl-protocol xstp untagged-disposition forward\n" +
                "l2-cft protocol add profile BBEC/652210//STC ctrl-protocol lacp untagged-disposition forward\n" +
                "l2-cft protocol add profile BBEC/652210//STC ctrl-protocol lacp-marker untagged-disposition forward\n" +
                "l2-cft protocol add profile BBEC/652210//STC ctrl-protocol oam untagged-disposition forward\n" +
                "l2-cft protocol add profile BBEC/652210//STC ctrl-protocol lldp untagged-disposition forward\n" +
                "l2-cft protocol add profile BBEC/652210//STC ctrl-protocol 802.1x untagged-disposition forward\n" +
                "l2-cft protocol add profile BBEC/652210//STC ctrl-protocol gmrp untagged-disposition forward\n" +
                "l2-cft protocol add profile BBEC/652210//STC ctrl-protocol gvrp untagged-disposition forward\n" +
                "l2-cft protocol add profile BBEC/652210//STC ctrl-protocol bridge-block untagged-disposition forward\n" +
                "l2-cft protocol add profile BBEC/652210//STC ctrl-protocol all-bridges-block untagged-disposition forward\n" +
                "l2-cft protocol add profile BBEC/652210//STC ctrl-protocol garp-block untagged-disposition forward\n" +
                "l2-cft set port 1 profile BBEC/652210//STC\n" +
                "l2-cft enable port 1\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! TRAFFIC PROFILING CONFIG:\n" +
                "!\n" +
                "traffic-profiling set meter-provisioning eir\n" +
                "!\n" +
                "traffic-profiling set port 1 mode advanced\n" +
                "!\n" +
                "traffic-profiling standard-profile create port 1 profile 1 name V280 cir 117056 eir 0 cbs 512 ebs 0\n" +
                "!\n" +
                "traffic-profiling enable port 1\n" +
                "!\n" +
                "traffic-profiling enable\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! LLDP CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! OAM CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! RSTP CONFIG:\n" +
                "!\n" +
                "!\n" +
                "rstp disable\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! MSTP CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! RING PROTECTION CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! RADSEC\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! SYSLOGTLS\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! DYING GASP CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! BENCHMARK CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! VIRTUAL-SWITCH MEMBER STATISTICS CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! USB-FLASH\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! MULTICAST SERVICES CONFIG: \n" +
                "!\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! VIRTUAL-SWITCH TDM MPLS AC CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! DEFAULT VLAN MEMBERSHIP REMOVAL CONFIG:\n" +
                "!\n" +
                "vlan remove vlan 1 port 2\n" +
                "vlan remove vlan 1 port 6\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! CFM CONFIG: global attributes\n" +
                "!\n" +
                "cfm enable\n" +
                "cfm set vs-automatic-meps off\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! CFM CONFIG: services\n" +
                "!\n" +
                "cfm service create vs BBEC/652210//STC name BBEC/652210//STC/280 md md2\n" +
                "cfm service set service BBEC/652210//STC/280 alarm-priority 2\n" +
                "cfm service set service BBEC/652210//STC/280 alarm-time 0\n" +
                "cfm service enable service BBEC/652210//STC/280\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! CFM CONFIG: meps\n" +
                "!\n" +
                "cfm mep create service BBEC/652210//STC/280 port 1 type up mepid 1\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! CFM CONFIG: y.1731 performance tests\n" +
                "!\n" +
                "cfm delay send service BBEC/652210//STC/280 local-mepid 1 mepid 2 iterate 0 repeat-delay 1 priority 0\n" +
                "cfm frame-loss send service BBEC/652210//STC/280 local-mepid 1 mepid 2 iterate 0 repeat-delay 1 priority 0\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! INTER-CHASSIS LINK SET CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! SHELL CONFIG:\n" +
                "!\n" +
                "system shell set global-more off\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! TELNET CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! RADIUS CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! TACACS CONFIG:\n" +
                "!\n" +
                "tacacs add server 204.111.76.25\n" +
                "tacacs add server 204.111.76.26\n" +
                "tacacs set timeout 24\n" +
                "tacacs set secret #A#cU3zqaSJSyab9rExYEZ5Fp1zRbO0s+FmjbeIbfyjOW6TO7Iv1n0OSaS3Ex0L4ITyxwTHxmWqjHz1ARTq7E+ijdaVo121ERNokw234LQtHM9te8RDElWOpOLliLkQPhe2T7RQfPJOG7lWfQps5yR4SWWuxJZxOGrE6/LqWkACMx864o7CN6M2vJApdGP6bTrTA1r+5IoBviwbgUti4soyMecBGjlOn+tPhlmm+lmOsKPLXEInaAczsnHbUIHFm5Sx\n" +
                "tacacs authorization enable\n" +
                "tacacs accounting enable\n" +
                "tacacs syslog enable\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! SECURITY CONFIG:\n" +
                "!\n" +
                "user create user netmgr access-level super secret $6$nhGTbIC.jG/0LVRe$zkeLQ9csEjcfjdzcPy.Y1TesZNGQDLwGKBt3SaNL6JNfuwySFKWLgd.gJVz29JekZe8372ti.2aODu5TUy4zm1\n" +
                "user delete user user\n" +
                "user delete user admin\n" +
                "user set user su access-level super max-session-limit 0 secret $6$5CikC9aXo9FbsEr.$SO.llGEt3y6bqHI1cl5NHCsOz2Zru3j5yJ5crh13IWrmreYk/sObilJZYux8gfutTd.Jd4sAHOBLqsxPA6o/x1\n" +
                "user auth set priority 1 method tacacs \n" +
                "user auth set priority 2 method local \n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! XFTP\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! NETWORK CONFIG: Vlan Attribute: Egress Tpid\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! RING PROTECTION CFM SERVICE CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! PERFORMANCE MONITOR TCA\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! PERFORMANCE MONITOR\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! PERFORMANCE MONITOR BULKSTATS\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! SSH CONFIG:\n" +
                "!\n" +
                "ssh server enable\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! VIRTUAL LINK LOSS INDICATION CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! DOT1X CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! LACP PORT SET CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! SNMP\n" +
                "!\n" +
                "snmp set contact NetOps\n" +
                "snmp notification standard IF-MIB disable notification-name linkDown\n" +
                "snmp notification standard IF-MIB disable notification-name linkUp\n" +
                "snmp reliable-trap enable\n" +
                "snmp create user Read-User auth-protocol noAuth engine-id 80:00:04:f7:05:00:23:8a:42:61:80:00\n" +
                "snmp create user Trap-User auth-protocol noAuth engine-id 80:00:04:f7:05:00:23:8a:42:61:80:00\n" +
                "snmp create user Write-User auth-protocol noAuth engine-id 80:00:04:f7:05:00:23:8a:42:61:80:00\n" +
                "snmp security-to-group attach user Trap-User sec-model v2c group Trap-Group\n" +
                "snmp security-to-group attach user Read-User sec-model v2c group Read-Group\n" +
                "snmp security-to-group attach user Write-User sec-model v2c group Write-Group\n" +
                "snmp create viewtree Trap-View sub-tree iso type exclude\n" +
                "snmp delete community-index t0000000\n" +
                "snmp delete community-index t0000001\n" +
                "snmp create community-index Read community d1XiE4puB sec-name Read-User transport-tag ReadTag\n" +
                "snmp create community-index Trap community Trap sec-name Trap-User transport-tag TrapTag\n" +
                "snmp create community-index Write community d1XiE4puB sec-name Write-User transport-tag WriteTag\n" +
                "snmp create access-entry Read-Group sec-model v2c sec-level noAuth read-view V12cView\n" +
                "snmp create access-entry Trap-Group sec-model v1 sec-level noAuth read-view Trap-View notify-view V12cView\n" +
                "snmp create access-entry Write-Group sec-model v2c sec-level noAuth read-view V12cView write-view V12cView\n" +
                "snmp create target Trap-Forwarder-1 addr 204.111.1.164/32 param-name Trap-Param\n" +
                "snmp create target cobia-READ addr 0.0.0.0/0 param-name none tag ReadTag\n" +
                "snmp create target cobia-WRITE addr 204.111.1.164/32 param-name none tag WriteTag\n" +
                "snmp create target Flatfish addr 204.111.2.153/32 param-name Trap-Param\n" +
                "snmp create target Scaldback addr 204.111.2.154/32 param-name Trap-Param\n" +
                "snmp create target-param Trap-Param sec-name Trap-User sec-model v1 sec-level noAuth\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! RMON\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! NETCONF CONFIG:\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! DHCP CLIENT\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! DHCP RELAY AGENT\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! DHCPV6 LDRA\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! DHCPV6 CLIENT\n" +
                "!\n" +
                "!\n" +
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                "! DHCP L3 RELAY\n" +
                "!\n" +
                "va-wdst-att-cvs-ciena1> \n" +
                "va-wdst-att-cvs-ciena1> \",\"Running\":1421,\"HostName\":\"va-wdst-att-cvs-ciena1\",\"fileId\":1250,\"BaseLine\":1}";

        // Define a regex pattern to match port number and description lines
        Pattern pattern = Pattern.compile("port set port (\\d+) .*description \"([^\"]*)\"");

        // Match the pattern against the input
        Matcher matcher = pattern.matcher(input);

        // Iterate over matches and print port number and description
        while (matcher.find()) {
            String portNumber = matcher.group(1);
            String description = matcher.group(2);
            System.out.println("Port " + portNumber + ": " + description);
        }
    }
}
