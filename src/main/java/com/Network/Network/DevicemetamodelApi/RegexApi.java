package com.Network.Network.DevicemetamodelApi;

import com.Network.Network.DevicemetamodelPojo.CienaInterface;
import com.Network.Network.DevicemetamodelPojo.CienaNcm;
import com.Network.Network.DevicemetamodelPojo.CienaPort;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class RegexApi {

    @PostMapping("/interfaces")
    public CienaNcm parseCienaInterface(@RequestBody JsonNode config) {
        CienaNcm cienaConfiguration = new CienaNcm();
        try {
            String jsonString = config.toString();
            jsonString = jsonString.replaceAll("\\\\n", "\n");
            jsonString = jsonString.trim();

            // Define regex pattern for interface remote configuration
            Pattern interfaceRemotePattern = Pattern.compile("interface remote set vlan (\\d+)\\s+interface remote set ip (\\S+)");

            // Match regex pattern
            Matcher interfaceRemoteMatcher = interfaceRemotePattern.matcher(jsonString);

            // Find and set VLAN and IP address for the interface
            if (interfaceRemoteMatcher.find()) {
                String vlanId = interfaceRemoteMatcher.group(1);
                String ipAddress = interfaceRemoteMatcher.group(2);

                // Create and set Interface object
                CienaInterface cienaInterface = new CienaInterface();
                cienaInterface.setName("remote");
                cienaInterface.setVlanid(vlanId);
                ArrayList<String> ipAddressesList = new ArrayList<>();
                ipAddressesList.add(ipAddress); // Assuming ipAddress is a single IP address, modify if it's multiple
                cienaInterface.setIpaddrees(ipAddressesList);
                cienaConfiguration.setCienaInterface(cienaInterface);
            }

            // Define regex pattern for port set
            Pattern portPattern = Pattern.compile("port (\\d+)(?: .*?)?");
            Pattern mtuPattern = Pattern.compile("port set port (\\d+) max-frame-size (\\d+)");
            Pattern statusPattern = Pattern.compile("port disable port (\\d+)");
            Pattern vlanPattern = Pattern.compile("vlan add vlan (\\d+(?:,\\d+)*) port (\\d+)");

            // Match the patterns against the input
            Matcher portMatcher = portPattern.matcher(jsonString);
            Matcher mtuMatcher = mtuPattern.matcher(jsonString);
            Matcher statusMatcher = statusPattern.matcher(jsonString);
            Matcher vlanMatcher = vlanPattern.matcher(jsonString);

            // Find and set port names
            HashSet<String> portNameSet = new HashSet<>(); // To store unique port names
            ArrayList<CienaPort> cienaPorts = new ArrayList<>();
            while (portMatcher.find()) {
                String portNumber = portMatcher.group(1);
                String portName = "port " + portNumber;

                // Check for duplicate port names
                if (!portNameSet.contains(portName)) {
                    portNameSet.add(portName);
                    // Create and add CienaPort object to the list
                    CienaPort port = new CienaPort();
                    port.setName(portName);
                    cienaPorts.add(port);
                }
            }

            // Set VLAN IDs for the appropriate port names
            while (vlanMatcher.find()) {
                String vlanIds = vlanMatcher.group(1);
                String[] vlanIdArray = vlanIds.split(",");
                String portNumber = vlanMatcher.group(2);
                String portName = "port " + portNumber;

                // Find the port object and set VLAN IDs
                for (CienaPort port : cienaPorts) {
                    if (port.getName().equals(portName)) {
                        port.setVlanid(new ArrayList<>(Arrays.asList(vlanIdArray)));
                        break;
                    }
                }
            }

            // Set port status based on whether it's disabled
            String portStatusString = jsonString;
            // Initialize port status to true
            for (CienaPort port : cienaPorts) {
                port.setStatus(true);
            }
            while (statusMatcher.find()) {
                String portNumber = statusMatcher.group(1);
                String portName = "port " + portNumber;
                boolean status = !portStatusString.contains("disable port " + portNumber);

                // Update port status
                for (CienaPort port : cienaPorts) {
                    if (port.getName().equals(portName)) {
                        port.setStatus(status);
                        break;
                    }
                }
            }

            // Find and set MTU values
            while (mtuMatcher.find()) {
                String portNumber = mtuMatcher.group(1);
                String mtuValue = mtuMatcher.group(2);

                // Check if port number matches any previously found port name
                String portName = "port " + portNumber;
                for (CienaPort port : cienaPorts) {
                    if (port.getName().equals(portName)) {
                        port.setMtu(mtuValue);
                        break; // Exit loop once MTU value is set for the port
                    }
                }
            }

            cienaConfiguration.setCienaPorts(cienaPorts);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cienaConfiguration;
    }
}
