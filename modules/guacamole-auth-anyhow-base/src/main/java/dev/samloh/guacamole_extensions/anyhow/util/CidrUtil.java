package dev.samloh.guacamole_extensions.anyhow.util;

import org.apache.commons.net.util.SubnetUtils;

import java.util.Arrays;
import java.util.Collection;

public class CidrUtil {
    public static boolean checkIpAddress(String ipAddress, Collection<String> cidrRanges) {
        for (String cidrRange : cidrRanges) {
            SubnetUtils.SubnetInfo cidrRangeInfo = new SubnetUtils(cidrRange).getInfo();
            if (cidrRangeInfo.isInRange(ipAddress)) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkIpAddress(String ipAddress, String cidrRanges) {
        return checkIpAddress(ipAddress, Arrays.asList(cidrRanges.split("[\\s,]+")));
    }
}
