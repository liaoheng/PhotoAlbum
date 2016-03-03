package com.github.liaoheng.album.sample.utils;

import java.io.File;
import java.lang.reflect.Array;
import java.net.URI;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

/**
 * @author liaoheng
 * @author <a href="http://jodd.org" target="_blank">jodd</a>
 * @version 2015-11-25 23:33
 */
public class Utils {
    public static final String ANDROID_RESOURCE = "android.resource://";

    /**
     *  Resource to Uri
     * @param context
     * @param resourceId
     * @return {@link Uri}
     */
    public static String resourceIdToUri(Context context, int resourceId) {
        return ANDROID_RESOURCE + context.getPackageName() + File.separator + resourceId;
    }

    public static String getName(String filename) {
        if (filename == null) {
            return null;
        }
        int index = indexOfLastSeparator(filename);
        return filename.substring(index + 1);
    }
    /**
     * The Unix separator character.
     */
    private static final char UNIX_SEPARATOR = '/';

    /**
     * The Windows separator character.
     */
    private static final char WINDOWS_SEPARATOR = '\\';
    public static   int indexOfLastSeparator(String filename) {
        if (filename == null) {
            return -1;
        }
        int lastUnixPos = filename.lastIndexOf(UNIX_SEPARATOR);
        int lastWindowsPos = filename.lastIndexOf(WINDOWS_SEPARATOR);
        return Math.max(lastUnixPos, lastWindowsPos);
    }

    /**
     * 是否为网络地址
     * @param url
     * @return
     */
    public static boolean isHtmlUrl(String url) {
        URI uri = URI.create(url);
        return isHtmlUrl(uri);
    }

    /**
     * 是否为网络地址
     * @param uri
     * @return
     */
    public static boolean isHtmlUrl(URI uri) {
        if (TextUtils.isEmpty(uri.getScheme())) {
            return false;
        }
        return true;
    }

    /**
     * URL是否为指定网站
     * @param baseAuthority
     * @param url
     * @return
     */
    public static boolean isCurAuthority(String baseAuthority, String url) {
        URI uri = URI.create(url);
        if (!isHtmlUrl(uri)) {
            return false;
        }
        String authority = uri.getAuthority();
        if (TextUtils.isEmpty(authority)) {
            return false;
        }
        if (!baseAuthority.contentEquals(authority)) {
            return false;
        }
        return true;
    }

    /**
     * Safely compares two objects just like <code>equals()</code> would, except
     * it allows any of the 2 objects to be <code>null</code>.
     *
     * @return <code>true</code> if arguments are equal, otherwise <code>false</code>
     */
    public static boolean equals(Object obj1, Object obj2) {
        return (obj1 != null) ? (obj1.equals(obj2)) : (obj2 == null);
    }

    /**
     * Returns string representation of an object, while checking for <code>null</code>.
     */
    public static String toString(Object value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    // ---------------------------------------------------------------- misc

    /**
     * Returns length of the object. Returns <code>0</code> for <code>null</code>.
     * Returns <code>-1</code> for objects without a length.
     */
    public static int length(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length();
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).size();
        }
        if (obj instanceof Map) {
            return ((Map) obj).size();
        }

        int count;
        if (obj instanceof Iterator) {
            Iterator iter = (Iterator) obj;
            count = 0;
            while (iter.hasNext()) {
                count++;
                iter.next();
            }
            return count;
        }
        if (obj instanceof Enumeration) {
            Enumeration enumeration = (Enumeration) obj;
            count = 0;
            while (enumeration.hasMoreElements()) {
                count++;
                enumeration.nextElement();
            }
            return count;
        }
        if (obj.getClass().isArray() == true) {
            return Array.getLength(obj);
        }
        return -1;
    }

    /**
     * Returns <code>true</code> if first argument contains provided element.
     * It works for strings, collections, maps and arrays.
     s	 */
    public static boolean containsElement(Object obj, Object element) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof String) {
            if (element == null) {
                return false;
            }
            return ((String) obj).contains(element.toString());
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).contains(element);
        }
        if (obj instanceof Map) {
            return ((Map) obj).values().contains(element);
        }

        if (obj instanceof Iterator) {
            Iterator iter = (Iterator) obj;
            while (iter.hasNext()) {
                Object o = iter.next();
                if (equals(o, element)) {
                    return true;
                }
            }
            return false;
        }
        if (obj instanceof Enumeration) {
            Enumeration enumeration = (Enumeration) obj;
            while (enumeration.hasMoreElements()) {
                Object o = enumeration.nextElement();
                if (equals(o, element)) {
                    return true;
                }
            }
            return false;
        }
        if (obj.getClass().isArray() == true) {
            int len = Array.getLength(obj);
            for (int i = 0; i < len; i++) {
                Object o = Array.get(obj, i);
                if (equals(o, element)) {
                    return true;
                }
            }
        }
        return false;
    }
}
