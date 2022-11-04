# MiniTwins
An experimental service and front-end for Android, focusing on minimal APK size

## Purpose

This experiment is trying to set up a very minimal front-end which contains a basic renderer and all the 'business logic' of the app, with a back-end service that stores data, does fundamental work etc.

This is to allow 2 things:

1. Very small APK download when updating business logic
2. Harder for users to accidentally lose data when uninstalling apps

### Link spam

https://www.techotopia.com/index.php/Android_Remote_Bound_Services_%E2%80%93_A_Worked_Example
https://developer.android.com/guide/topics/manifest/service-element.html#proc
https://stackoverflow.com/questions/39728597/android-binding-to-a-remote-service

https://developer.android.com/guide/components/bound-services
https://android.googlesource.com/platform/development/+/master/samples/ApiDemos/src/com/example/android/apis/app/MessengerService.java
https://android.googlesource.com/platform/development/+/master/samples/ApiDemos/src/com/example/android/apis/app/MessengerServiceActivities.java
