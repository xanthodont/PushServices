package cloudservices.client.http.async.utility;

/**
 * author : 桥下一粒砂 (chenyoca@gmail.com)
 * date   : 2013-10-21
 * TODO
 */
public class TextUtils {

    public static String join(CharSequence delimiter, Iterable tokens) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (Object token: tokens) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(delimiter);
            }
            sb.append(token);
        }
        return sb.toString();
    }
}
