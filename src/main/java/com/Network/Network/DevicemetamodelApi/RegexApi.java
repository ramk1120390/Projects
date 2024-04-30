package com.Network.Network.DevicemetamodelApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class RegexApi {

    @Autowired
    private ObjectMapper objectMapper; // Autowire ObjectMapper

    @PostMapping("/interfaces")
    public JsonNode parseCienaInterface(@RequestBody JsonNode config) {
        ObjectNode resultNode = objectMapper.createObjectNode();

        try {
            String jsonString = config.toString();
            jsonString = jsonString.replaceAll("\\\\n", "\n");
            jsonString = jsonString.trim();

            // Define regex pattern for interface remote configuration with VLAN ID and IP address
            Pattern interfaceRemotePatternWithVlan = Pattern.compile("interface remote set vlan (\\d+)\\s*interface remote set ip (\\S+)");
            // Define regex pattern for interface remote configuration with only IP address
            Pattern interfaceRemotePatternWithoutVlan = Pattern.compile("interface remote set ip (\\S+)");

            // Match regex pattern for interface remote configuration with VLAN ID and IP address
            Matcher interfaceRemoteMatcherWithVlan = interfaceRemotePatternWithVlan.matcher(jsonString);
            // Match regex pattern for interface remote configuration with only IP address
            Matcher interfaceRemoteMatcherWithoutVlan = interfaceRemotePatternWithoutVlan.matcher(jsonString);

            String vlanId = null;
            String ipAddress = null;

            // Find and set VLAN ID and IP address if VLAN ID exists
            if (interfaceRemoteMatcherWithVlan.find()) {
                vlanId = interfaceRemoteMatcherWithVlan.group(1);
                ipAddress = interfaceRemoteMatcherWithVlan.group(2);
            } else if (interfaceRemoteMatcherWithoutVlan.find()) { // If VLAN ID doesn't exist, find and set only IP address
                ipAddress = interfaceRemoteMatcherWithoutVlan.group(1);
            }

            // Create and set Interface object
            if (ipAddress != null) {
                ObjectNode cienaInterfaceNode = objectMapper.createObjectNode();
                cienaInterfaceNode.put("name", "remote");
                if (vlanId != null) {
                    cienaInterfaceNode.put("vlanid", vlanId);
                }
                ArrayNode ipAddressesArray = objectMapper.createArrayNode();
                ipAddressesArray.add(ipAddress); // Only IP address
                cienaInterfaceNode.set("ipaddrees", ipAddressesArray);
                resultNode.set("cienaInterface", cienaInterfaceNode);
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

            ArrayNode cienaPortsArray = objectMapper.createArrayNode();
            // Find and set port names
            HashSet<String> portNameSet = new HashSet<>(); // To store unique port names
            while (portMatcher.find()) {
                String portNumber = portMatcher.group(1);
                String portName = "port " + portNumber;

                // Check for duplicate port names
                if (!portNameSet.contains(portName)) {
                    portNameSet.add(portName);
                    // Create and add CienaPort object to the list
                    ObjectNode portNode = objectMapper.createObjectNode();
                    portNode.put("name", portName);
                    cienaPortsArray.add(portNode);
                }
            }

            // Set VLAN IDs for the appropriate port names
            while (vlanMatcher.find()) {
                String vlanIds = vlanMatcher.group(1);
                String[] vlanIdArray = vlanIds.split(",");
                String portNumber = vlanMatcher.group(2);
                String portName = "port " + portNumber;

                // Find the port object and set VLAN IDs
                for (JsonNode portNode : cienaPortsArray) {
                    if (portNode.get("name").asText().equals(portName)) {
                        ArrayNode vlanIdNode = objectMapper.createArrayNode();
                        for (String id : vlanIdArray) {
                            vlanIdNode.add(id);
                        }
                        ((ObjectNode) portNode).set("vlanid", vlanIdNode);
                        break;
                    }

                }
            }

            // Set port status based on whether it's disabled
            String portStatusString = jsonString;
            // Initialize port status to true
            for (JsonNode portNode : cienaPortsArray) {
                ((ObjectNode) portNode).put("status", true);
            }
            while (statusMatcher.find()) {
                String portNumber = statusMatcher.group(1);
                String portName = "port " + portNumber;
                boolean status = !portStatusString.contains("disable port " + portNumber);

                // Update port status
                for (JsonNode portNode : cienaPortsArray) {
                    if (portNode.get("name").asText().equals(portName)) {
                        ((ObjectNode) portNode).put("status", status);
                        break;
                    }
                }
            }
            for (JsonNode portNode : cienaPortsArray) {
                ((ObjectNode) portNode).put("mtu", "NA");
            }
            // Find and set MTU values
            while (mtuMatcher.find()) {
                String portNumber = mtuMatcher.group(1);
                String mtuValue = mtuMatcher.group(2);

                // Check if port number matches any previously found port name
                String portName = "port " + portNumber;
                for (JsonNode portNode : cienaPortsArray) {
                    if (portNode.get("name").asText().equals(portName)) {
                        ((ObjectNode) portNode).put("mtu", mtuValue);
                        break; // Exit loop once MTU value is set for the port
                    }
                }
            }

            resultNode.set("cienaPorts", cienaPortsArray);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultNode;
    }

    @PostMapping("/interfaces1")
    public JsonNode parseTelcoInterface(@RequestBody JsonNode config) {
        ObjectNode resultNode = objectMapper.createObjectNode();

        try {
            String jsonString = config.toString();
            jsonString = jsonString.replaceAll("\\\\n", "\n");
            jsonString = jsonString.trim();
            ObjectNode swInterfaceNode = objectMapper.createObjectNode();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return config;
    }
}
