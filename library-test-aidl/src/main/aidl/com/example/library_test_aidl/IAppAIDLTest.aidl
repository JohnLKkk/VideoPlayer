// IAppAIDLTest.aidl
package com.example.library_test_aidl;
import com.example.library_test_aidl.AidlAiListener;

// Declare any non-default types here with import statements

interface IAppAIDLTest {
        void setListener(in AidlAiListener obj);
        void outPutLog(String msg);
}
