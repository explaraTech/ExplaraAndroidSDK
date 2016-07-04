package com.explara_core.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.explara_core.common_dto.CurrencyDetailsDto;
import com.explara_core.common_dto.NotificationDetailsDto;
import com.explara_core.common_dto.SearchHistoryDto;
import com.explara_core.login.login_dto.Account;
import com.explara_core.login.login_dto.LoginResponseDto;
import com.explara_core.utils.Log;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by anudeep on 17/03/16.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Explara_Db";
    private static final String TAG = DatabaseHelper.class.getSimpleName();

    private Dao<Account, String> accountDao;
    private Dao<LoginResponseDto.Bank, String> bankDao;
    //private Dao<CurrencyList, Integer> currencyListsDao;
    private Dao<CurrencyDetailsDto, Integer> currencyDetailsDao;
    private Dao<NotificationDetailsDto, Integer> notificationDao;
    private Dao<SearchHistoryDto, Integer> searchDao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

   /* public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }*/

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        // Create tables. This onCreate() method will be invoked only once of the application life time i.e. the first time when the application starts.
        try {
            TableUtils.createTableIfNotExists(connectionSource, LoginResponseDto.Bank.class);
            //TableUtils.createTableIfNotExists(connectionSource, CurrencyList.class);
            TableUtils.createTableIfNotExists(connectionSource, CurrencyDetailsDto.class);
            TableUtils.createTableIfNotExists(connectionSource, NotificationDetailsDto.class);
            TableUtils.createTableIfNotExists(connectionSource, SearchHistoryDto.class);

        } catch (SQLException e) {
            e.printStackTrace();
            Log.e(TAG, "Unable to create datbases");
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
        Log.d(TAG, "OnUpgrade called");

       /* try {

            // In case of change in database of next version of application, please increase the value of DATABASE_VERSION variable, then this method will be invoked
            //automatically. Developer needs to handle the upgrade logic here, i.e. create a new table or a new column to an existing table, take the backups of the
            // existing database etc.

            TableUtils.dropTable(connectionSource, Account.class, true);
            TableUtils.dropTable(connectionSource, LoginResponseDto.Bank.class, true);
            onCreate(sqLiteDatabase, connectionSource);

        } catch (SQLException e) {
            e.printStackTrace();
            Log.e(DatabaseHelper.class.getName(), "Unable to upgrade database from version " + oldVer + " to new "
                    + newVer);
        }*/

    }


// Create the getDao methods of all database tables to access those from android code.
// Insert, delete, read, update everything will be happened through DAOs

    public Dao<Account, String> getAccountDao() throws SQLException {
        if (accountDao == null) {
            accountDao = getDao(Account.class);
        }
        return accountDao;
    }

    public Dao<LoginResponseDto.Bank, String> getBankDao() throws SQLException {
        if (bankDao == null) {
            bankDao = getDao(LoginResponseDto.Bank.class);
        }
        return bankDao;
    }

   /* public Dao<CurrencyList, Integer> getCurrencyListsDao() throws SQLException {
        if (currencyListsDao == null) {
            currencyListsDao = getDao(CurrencyList.class);
        }
        return currencyListsDao;
    }*/

    public Dao<CurrencyDetailsDto, Integer> getCurrencyDetailsDto() throws SQLException {
        if (currencyDetailsDao == null) {
            currencyDetailsDao = getDao(CurrencyDetailsDto.class);
        }
        return currencyDetailsDao;
    }

    public Dao<NotificationDetailsDto, Integer> getNotificationDao() throws SQLException {
        if (notificationDao == null) {
            notificationDao = getDao(NotificationDetailsDto.class);
        }
        return notificationDao;
    }

    public Dao<SearchHistoryDto, Integer> getSearchHistoryDao() throws SQLException {
        if (searchDao == null) {
            searchDao = getDao(SearchHistoryDto.class);

        }
        return searchDao;


    }


    public void removeAllTablesData() {
        try {
            //TableUtils.clearTable(getConnectionSource(),Account.class);
            TableUtils.clearTable(getConnectionSource(), LoginResponseDto.Bank.class);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e(TAG, "Unable to clear datbases");
        }
    }
}
