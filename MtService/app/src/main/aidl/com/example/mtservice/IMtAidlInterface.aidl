// IMtAidlInterface.aidl
package com.example.mtservice;

// Declare any non-default types here with import statements

/**
* AIDL methods should be used to do 'command' like things,
* that should take a small amount of time, *OR* that start
* a long running process on the service without needing to
* return data (like doing a sync to cloud).
*
* For things that transmit or received data, use a content
* provider, and use an insert or query method (which gives
* a cursor as a result)
*
* AIDL files are *NOT* Java, but uses its own syntax which
* is Java-like, but is only for building type definitions:

https://source.android.com/docs/core/architecture/aidl/aidl-language

* The definitions in this file have backing code generated
* by the compiler, under the type: `IMtAidlInterface.Stub`
* This is overridden in `RealMtAidlService.java` to bind a
* real definition. When you first create a new method, you
* should build the project after creating the interface so
* you avoid an error 'Method does not override method from
* its superclass'
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