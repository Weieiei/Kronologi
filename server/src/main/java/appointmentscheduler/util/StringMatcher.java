package appointmentscheduler.util;

import java.util.regex.Pattern;

public class StringMatcher {
    public static boolean verifyNaming(String fileName) {
        //alphanumeric character followed by dot and alpha character
        final Pattern pattern = Pattern.compile("\\w*.[A-Za-z]*");
        return pattern.matcher(fileName).matches();
    }
}
