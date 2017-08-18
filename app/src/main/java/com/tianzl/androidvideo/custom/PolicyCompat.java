package com.tianzl.androidvideo.custom;

import android.content.Context;
import android.view.Window;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Created by tianzl on 2017/8/13.
 */

public class PolicyCompat {
    private static final String PHONE_WINDOW_CLASS_NAME   = "com.android.internal.policy.PhoneWindow";
    private static final String POLICY_MANAGER_CLASS_NAME = "com.android.internal.policy.PolicyManager";


    private PolicyCompat() {
    }


    /*
     * Private methods
     */
    private static Window createPhoneWindow(Context context) {
        try {
            /* Find class */
            Class<?> cls = Class.forName(PHONE_WINDOW_CLASS_NAME);

            /* Get constructor */
            Constructor c = cls.getConstructor(Context.class);

            /* Create instance */
            return (Window)c.newInstance(context);
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(PHONE_WINDOW_CLASS_NAME + " could not be loaded", e);
        }
        catch (Exception e) {
            throw new RuntimeException(PHONE_WINDOW_CLASS_NAME + " class could not be instantiated", e);
        }
    }

    private static Window makeNewWindow(Context context) {
        try {
            /* Find class */
            Class<?> cls = Class.forName(POLICY_MANAGER_CLASS_NAME);

            /* Find method */
            Method m = cls.getMethod("makeNewWindow", Context.class);

            /* Invoke method */
            return (Window)m.invoke(null, context);
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(POLICY_MANAGER_CLASS_NAME + " could not be loaded", e);
        }
        catch (Exception e) {
            throw new RuntimeException(POLICY_MANAGER_CLASS_NAME + ".makeNewWindow could not be invoked", e);
        }
    }

    /*
     * Public methods
     */
    public static Window createWindow(Context context) {
        if (false)
            return createPhoneWindow(context);
        else
            return makeNewWindow(context);
    }
}
