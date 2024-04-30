package com.Network.Network.java8.Stream;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main1 {
    public static void main(String[] args) {
        // Updated input text
        String input = "router\n" +
                "mpls\n" +
                "label-range-ingress 16-1048575\n" +
                "label-range-bgp 228672-1048575\n" +
                "exit\n" +
                "!\n" +
                "ospf\n" +
                "dscp-mapping 48\n" +
                "exit\n" +
                "!\n" +
                "interface sw0\n" +
                "management\n" +
                "type vlan\n" +
                "vlan-id 1\n" +
                "exit\n" +
                "!\n" +
                "shutdown\n" +
                "exit\n" +
                "!\n" +
                "interface sw1\n" +
                "address 10.30.100.137/23\n" +
                "management\n" +
                "type dot1q\n" +
                "s-vlan 301\n" +
                "c-vlan 300\n" +
                "exit\n" +
                "!\n" +
                "no shutdown\n" +
                "exit\n" +
                "!\n" +
                "interface outBand0\n" +
                "description outBand0\n" +
                "address 10.11.5.201/24\n" +
                "no shutdown\n" +
                "exit\n" +
                "!\n" +
                "interface outBand1\n" +
                "description outBand0\n" +
                "address 10.11.5.201/24\n" +
                "no shutdown\n" +
                "exit\n" +
                "!\n" +
                "static-route 0.0.0.0/0 10.30.100.1 1\n" +
                "!\n" +
                "system\n" +
                "confirm-commit\n" +
                "shutdown\n" +
                "!\n" +
                "fp-mode --ERROR--\n" +
                "netconf-server\n" +
                "shutdown\n" +
                "!\n" +
                "banner \"BATM T-Metro-8104\"\n" +
                "snmp\n" +
                "engine-id 80:00:02:e2:03:00:c0:ab:02:ab:80\n" +
                "no shutdown\n" +
                "authentication-failure-trap\n" +
                "system-name wv-wlch-edge-8104-1\n" +
                "system-contact shentel-noc@shentel.net\n" +
                "system-description \"Switch software version 9.0.R2.2\"\n" +
                "view myview 1.3\n" +
                "view viewall 1.3\n" +
                "group noc noAuthNoPriv read viewall write non notify viewall\n" +
                "group mygroup authNoPriv read myview write myview notify myview\n" +
                "user asg1 mygroup v3 md5 37c38cfc8d89a31ee82e9da6755d1ef9\n" +
                "user \"RevealSuddenissueS!\" noc v2c\n" +
                "target-address flatfish\n" +
                "address 204.111.2.153\n" +
                "security-name \"RevealSuddenissueS!\"\n" +
                "type trap\n" +
                "!\n" +
                "target-address herring\n" +
                "address 204.111.1.183\n" +
                "security-name \"RevealSuddenissueS!\"\n" +
                "type trap\n" +
                "!\n" +
                "target-address scaldback\n" +
                "address 204.111.2.154\n" +
                "security-name \"RevealSuddenissueS!\"\n" +
                "type trap\n" +
                "!";

        String regex = "(?s)interface sw.*?\\d+\\n.*?(?=\\ninterface sw|\\Z)";
        String addressRegex = "address\\s+(\\S+)";
        String typeRegex = "type\\s+(\\S+)";
        String sVlanRegex = "s-vlan\\s+(\\d+)";
        String cVlanRegex = "c-vlan\\s+(\\d+)";
        String vlanIdRegex = "vlan-id\\s+(\\d+)";
        String outBandInterfaceRegex = "interface outBand\\d+\\s+((?:.|\\n)*?)exit";

        Pattern outBandPattern = Pattern.compile(outBandInterfaceRegex);
        Pattern addressPattern = Pattern.compile(addressRegex);
        Pattern typePattern = Pattern.compile(typeRegex);
        Pattern sVlanPattern = Pattern.compile(sVlanRegex);
        Pattern cVlanPattern = Pattern.compile(cVlanRegex);
        Pattern vlanIdPattern = Pattern.compile(vlanIdRegex);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        Matcher outBandMatcher = outBandPattern.matcher(input);
        HashMap<String, String> interfaceBlocks = new HashMap<>();
        StringBuilder outBandBlocks = new StringBuilder();
        // Find and store all matching blocks for each interface
        while (matcher.find()) {
            // Extract interface name
            String interfaceName = matcher.group(0).split("\\n")[0];
            // Store interface block with its name in the HashMap
            interfaceBlocks.put(interfaceName, matcher.group(0));
        }
        for (String interfaceName : interfaceBlocks.keySet()) {
            String interfaceBlock = interfaceBlocks.get(interfaceName);
            Matcher addressMatcher = addressPattern.matcher(interfaceBlock);
            Matcher typeMatcher = typePattern.matcher(interfaceBlock);
            Matcher sVlanMatcher = sVlanPattern.matcher(interfaceBlock);
            Matcher cVlanMatcher = cVlanPattern.matcher(interfaceBlock);
            Matcher vlanIdMatcher = vlanIdPattern.matcher(interfaceBlock);
            boolean isActive = interfaceBlock.contains("no shutdown");

            //System.out.println("Interface Block: \n" + interfaceBlock);
            String address = addressMatcher.find() ? addressMatcher.group(1) : "N/A";
            String type = typeMatcher.find() ? typeMatcher.group(1) : "N/A";
            String sVlan = sVlanMatcher.find() ? sVlanMatcher.group(1) : "N/A";
            String cVlan = cVlanMatcher.find() ? cVlanMatcher.group(1) : "N/A";
            String vlanId = vlanIdMatcher.find() ? vlanIdMatcher.group(1) : "N/A";

            System.out.println("name: " + interfaceName);
            System.out.println("Address: " + address);
            System.out.println("Type: " + type);
            System.out.println("S-VLAN: " + sVlan);
            System.out.println("C-VLAN: " + cVlan);
            System.out.println("Status: " + isActive);
            System.out.println("VLAN ID: " + vlanId);
            System.out.println("----------------------------------");


        }


        // Find and print all matching blocks for outband interfaces
        while (outBandMatcher.find()) {
            String interfaceBlock = outBandMatcher.group().trim(); // trim to remove leading/trailing whitespace
            String name = extractValue(interfaceBlock, "interface\\s+(\\S+)");
            String description = extractValue(interfaceBlock, "description\\s+(\\S+)");
            String address = extractValue(interfaceBlock, "address\\s+(\\S+)");
            Boolean Status = !interfaceBlock.contains("shutdown");
            // Print extracted values
            System.out.println("Name: " + name);
            System.out.println("Description: " + description);
            System.out.println("Address: " + address);
            System.out.println("Address: " + Status);

            System.out.println(); // Add a blank line for separation
        }
    }

    // Method to extract a specific value from a text block using a regex pattern
    private static String extractValue(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "N/A"; // If the value is not found, return "N/A"
        }
    }
}

