// IMtAidlInterface.aidl
package com.example.mtservice;

// Declare any non-default types here with import statements

interface IMtAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    int basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
}