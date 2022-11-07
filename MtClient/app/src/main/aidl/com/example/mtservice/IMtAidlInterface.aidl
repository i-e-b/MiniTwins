// IMtAidlInterface.aidl
package com.example.mtservice;

// Declare any non-default types here with import statements

/**
* This file should be kept up-to-date with its counter-part
* in MtService/app/src/main/aidl/com/example/mtservice/IMtAidlInterface.aidl
*/
interface IMtAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    int basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    /**
    * Get a string that represents the service version
    */
    String getServiceVersion();
}