package com.Network.Network.DevicemetamodelApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/interfaces2")
    public AdranInterface parseInterfaces(@RequestBody JsonNode config) {
        AdranInterface adranInterface = new AdranInterface();
        try {
            String jsonString = config.toString();
            jsonString = jsonString.replaceAll("\\\\n", "\n").trim();
            ArrayList<Ethernet> ethernetList = new ArrayList<>();
            ArrayList<GigabitEthernet> gigabitEthernetList = new ArrayList<>();
            ArrayList<T1> t1List = new ArrayList<>();
            ArrayList<Fx> fxsList = new ArrayList<>();
            ArrayList<Pri> priList = new ArrayList<>();

            Pattern gigabitEthernetPattern = Pattern.compile("interface (gigabit-eth \\d+/\\d+)(.*?)!", Pattern.DOTALL);
            Pattern ethernetPattern = Pattern.compile("interface eth (\\d+/\\d+)(.*?)!", Pattern.DOTALL);
            Pattern T1Pattern = Pattern.compile("interface t1 (\\d+/\\d+)(.*?)!", Pattern.DOTALL);
            Pattern fxsPattern = Pattern.compile("interface fxs (\\d+/\\d+)(.*?)!", Pattern.DOTALL);
            Pattern priPattern = Pattern.compile("interface pri (\\d+)(.*?)!", Pattern.DOTALL);
            Pattern ipAddressPattern = Pattern.compile("ip address (\\S+) (\\S+)");
            Pattern mediaGatewayPattern = Pattern.compile("media-gateway ip (\\S+)");

            Matcher ethernetMatcher = ethernetPattern.matcher(jsonString);
            Matcher GigabitethernetMatcher = gigabitEthernetPattern.matcher(jsonString);
            Matcher T1Matcher = T1Pattern.matcher(jsonString);
            Matcher priMatcher = priPattern.matcher(jsonString);
            Matcher fxsMatcher=fxsPattern.matcher(jsonString);
            GigabitEthernet gigabitEthernet = new GigabitEthernet();

            while (ethernetMatcher.find()) {
                Ethernet ethernet = new Ethernet();
                String interfaceName = "interface eth " + ethernetMatcher.group(1);
                String interfaceConfig = ethernetMatcher.group(0);
                String description = "No Description";
                if (interfaceConfig.contains("description")) {
                    Pattern descPattern = Pattern.compile("description\\s+(.*?)\\s+", Pattern.DOTALL);
                    Matcher descMatcher = descPattern.matcher(interfaceConfig);
                    if (descMatcher.find()) {
                        description = descMatcher.group(1);
                    }
                }
                String ipaddress = "NA";
                if (!interfaceConfig.contains("no ip address")) {
                    Matcher ipMatcher = ipAddressPattern.matcher(interfaceConfig);
                    if (ipMatcher.find()) {
                        ipaddress = ipMatcher.group(1);
                    }
                }
                String mediaGateway = "NA";
                if (interfaceConfig.contains("media-gateway")) {
                    Matcher mediaGatewayMatcher = mediaGatewayPattern.matcher(interfaceConfig);
                    if (mediaGatewayMatcher.find()) {
                        mediaGateway = mediaGatewayMatcher.group(1);
                    }
                }
                Boolean Status = interfaceConfig.contains("no shutdown");
                ethernet.setName(interfaceName);
                ethernet.setIpaddress(ipaddress);
                ethernet.setMediagateway(mediaGateway);
                ethernet.setDesc(description);
                ethernet.setStatus(Status);
                ethernetList.add(ethernet);
            }


            while (GigabitethernetMatcher.find()) {
                String interfaceName = GigabitethernetMatcher.group(1); // Retrieves the complete interface name
                String interfaceConfig = GigabitethernetMatcher.group(0);
                String ipaddress = "NA";
                if (!interfaceConfig.contains("no ip address")) {
                    Matcher ipMatcher = ipAddressPattern.matcher(interfaceConfig);
                    if (ipMatcher.find()) {
                        ipaddress = ipMatcher.group(1);
                    }
                }
                String mediaGateway = "NA";
                if (interfaceConfig.contains("media-gateway")) {
                    Matcher mediaGatewayMatcher = mediaGatewayPattern.matcher(interfaceConfig);
                    if (mediaGatewayMatcher.find()) {
                        mediaGateway = mediaGatewayMatcher.group(1);
                    }
                }
                Boolean Status = interfaceConfig.contains("no shutdown");
                gigabitEthernet.setName(interfaceName);
                gigabitEthernet.setIpaddress(ipaddress);
                gigabitEthernet.setStatus(Status);
                gigabitEthernet.setMediagateway(mediaGateway);
                gigabitEthernetList.add(gigabitEthernet);
                //System.out.println(gigabitEthernet.toString());
                //System.out.println("--------");
            }

            while (T1Matcher.find()) {
                String interfaceName = "interface t1 " + T1Matcher.group(1); // Retrieves the complete interface name
                String interfaceConfig = T1Matcher.group(2).trim(); // Extract the interface configuration
                String description = "No Description";
                if (interfaceConfig.contains("description")) {
                    Pattern descPattern = Pattern.compile("description\\s+(.*?)(\\r?\\n|$)", Pattern.DOTALL);
                    Matcher descMatcher = descPattern.matcher(interfaceConfig);
                    if (descMatcher.find()) {
                        description = descMatcher.group(1).trim();
                    }
                }
                Boolean Status = interfaceConfig.contains("no shutdown");
                T1 t1 = new T1();
                t1.setName(interfaceName);
                t1.setDesc(description);
                t1.setStatus(Status);
                t1List.add(t1);
                //System.out.println(t1.toString());
            }

            while (priMatcher.find()) {
                String interfaceNumber = priMatcher.group(1); // Retrieves the interface number
                String interfaceConfig = priMatcher.group(2).trim(); // Extract the interface configuration
                String interfaceName = "interface pri " + interfaceNumber;
                String description = "No Description";
                if (interfaceConfig.contains("description")) {
                    Pattern descPattern = Pattern.compile("description\\s+(.*?)(\\r?\\n|$)", Pattern.DOTALL);
                    Matcher descMatcher = descPattern.matcher(interfaceConfig);
                    if (descMatcher.find()) {
                        description = descMatcher.group(1).trim();
                    }
                }
                Boolean status = !interfaceConfig.contains("shutdown");
                Pri pri = new Pri();
                pri.setName(interfaceName);
                pri.setDesc(description);
                pri.setStatus(status);
                priList.add(pri);
               // System.out.println(pri.toString());
            }

            while (fxsMatcher.find()){
                String interfaceName = "interface fxs " + fxsMatcher.group(1); // Retrieves the complete interface name
                String interfaceConfig = fxsMatcher.group(0);
                Boolean Status = interfaceConfig.contains("no shutdown");
                Fx fx=new Fx();
                fx.setName(interfaceName);
                fx.setStatus(Status);
               // System.out.println(interfaceName);
               // System.out.println(Status);
                fxsList.add(fx);
              //  System.out.println("--------------------");

            }
            adranInterface.setEthernetList(ethernetList);
            adranInterface.setGigabitEthernets(gigabitEthernetList);
            adranInterface.setT1ArrayList(t1List);
            adranInterface.setFxArrayList(fxsList);
            adranInterface.setPriArrayList(priList);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  adranInterface;
    }
}