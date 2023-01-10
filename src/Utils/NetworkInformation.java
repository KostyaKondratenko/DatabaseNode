package Utils;

import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class NetworkInformation {

    public String firstIPv4Interface() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();

                // Filters inactive interfaces and loopback
                if (networkInterface.isLoopback() || !networkInterface.isUp())
                    continue;

                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();

                    // Filters IPv6 addresses
                    if (address instanceof Inet6Address)
                        continue;

                    return address.getHostAddress();
                }
            }
        } catch (SocketException e) {
        }

        return InetAddress.getLoopbackAddress().getHostAddress();
    }

    public String getNetworkInformation() {
        String result = "\nNetwork Interfaces\n";
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();

                // Filters inactive interfaces
                if (!networkInterface.isUp())
                    continue;

                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();

                    // Filters IPv6 addresses
                    if (address instanceof Inet6Address)
                        continue;

                    String ip = address.getHostAddress();
                    result += networkInterface.getDisplayName() + " " + ip + "\n";
                }
            }
        } catch (SocketException e) {
            result = "Error retrieving network interfaces.";
        }
        return result;
    }
}
