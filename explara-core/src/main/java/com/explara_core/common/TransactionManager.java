package com.explara_core.common;

/**
 * Created by debasishpanda on 22/09/15.
 */
public class TransactionManager extends BaseManager {

    private static final String TAG = TransactionManager.class.getSimpleName();
    //public TransactionDto mTransaction = new TransactionDto();
    public static TransactionManager mTransactionManager;

    private TransactionManager() {

    }

    public static synchronized TransactionManager getInstance() {
        if (mTransactionManager == null) {
            mTransactionManager = new TransactionManager();
        }
        return mTransactionManager;
    }

    @Override
    public void cleanUp() {
        // mTransaction = null;
    }

}
