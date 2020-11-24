package com.exc.street.light.co.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author tanhonghang
 * @create 2020/10/12 15:31
 */
public class Util {

    /**
     * Format String : yyyy-MM-dd HH:mm:ss
     */
    public static final String DateFormat = "yyyy-MM-dd HH:mm:ss";

    public static final String openState = "开";

    public static Date getDateByString(String s) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);
        Date date = null;
        try {
            date = sdf.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 对json字符串格式化输出
     *
     * @param jsonStr
     * @return
     */
    public static String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr)){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            switch (current) {
                case '{':
                case '[':
                    sb.append(current);
                    sb.append('\n');
                    indent++;
                    addIndentBlank(sb, indent);
                    break;
                case '}':
                case ']':
                    sb.append('\n');
                    indent--;
                    addIndentBlank(sb, indent);
                    sb.append(current);
                    break;
                case ',':
                    sb.append(current);
                    if (last != '\\') {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }

        return sb.toString();
    }

    /**
     * 添加space
     *
     * @param sb
     * @param indent
     */
    private static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }

    /**
     * 井盖状态从String转int
     *
     * @return
     */
    public static int transferDipStatus(String DepStatus) {
        if (openState.equals(DepStatus)) {
            return 1;
        }
        return 0;
    }
}
