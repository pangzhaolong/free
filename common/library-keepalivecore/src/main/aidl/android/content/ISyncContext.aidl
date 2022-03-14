// ISyncContext.aidl
package android.content;

// Declare any non-default types here with import statements

interface ISyncContext {
    void sendHeartbeat();
    void onFinished(in SyncResult result);
}