package com.explara_core.database;


import android.content.Context;

import com.explara_core.common.BaseManager;
import com.explara_core.common_dto.CurrencyDetailsDto;
import com.explara_core.common_dto.CurrencyList;
import com.explara_core.common_dto.NotificationDetailsDto;
import com.explara_core.common_dto.SearchHistoryDto;
import com.explara_core.login.login_dto.LoginResponseDto;
import com.explara_core.utils.Log;
import com.explara_core.utils.Utility;
import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RawRowMapper;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by anudeep on 18/03/16.
 */

public class DataBaseManager extends BaseManager {

    private static DataBaseManager sDataBaseManager;
    private DatabaseHelper mDatabaseHelper;
    private ExecutorService mExecutorService;
    public List<CurrencyDetailsDto> mCurrencyDetailsList = new ArrayList<>();

    public interface FetchCurrencyDetailsListener {
        void onFetchingCurrencyList(List<String> currencyList);
    }

    public interface FetchHistoryDetailsListener {
        void onFetchingHistory(List<SearchHistoryDto> historySearchList);


    }


    public interface FetchCountryFromCurrencyListener {
        void onFetchingCountryFromCurrency(String currency);
    }

    public synchronized static DataBaseManager getInstance(Context context) {
        if (sDataBaseManager == null) {
            sDataBaseManager = new DataBaseManager(context);
        }
        return sDataBaseManager;
    }

    private DataBaseManager(Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
        mExecutorService = Executors.newSingleThreadExecutor();
    }

    /*public DatabaseHelper getHelper(){
        return mDatabaseHelper;
    }*/

    public void saveAllCurrencWithCountriesInDb(final Context context) {

        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                // persist the account object to the database
                try {
                    //final Dao<CurrencyList, Integer> currencyListsDao = mDatabaseHelper.getCurrencyListsDao();
                    final Dao<CurrencyDetailsDto, Integer> currenctDetailsDao = mDatabaseHelper.getCurrencyDetailsDto();

                    // save currenct details only for the first time
                    if (currenctDetailsDao.countOf() == 0) {
                        String currencyString = Utility.getStringFromAssets(context, "api_response/currency.json");
                        Gson gson = new Gson();
                        CurrencyList currencyList = gson.fromJson(currencyString, CurrencyList.class);
                        List<CurrencyDetailsDto> currencyDetailsList = currencyList.currencyDetails;

                        // create an instance of CurrencyList
                        //CurrencyList currencyListObj = new CurrencyList();
                        // persist the currencyList object to the database
                        //currencyListsDao.create(currencyListObj);

                        //create records in currencyDetails table
                        for (CurrencyDetailsDto currencyDetailsDto : currencyDetailsList) {
                            CurrencyDetailsDto currencyDetailsDtoTemp = new CurrencyDetailsDto(currencyDetailsDto.currency, currencyDetailsDto.symbol, currencyDetailsDto.country);
                            currenctDetailsDao.create(currencyDetailsDtoTemp);
                        }
                        Log.d("Currency", "Currency details saved successfully.");
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.d("Currency", "Some issue occurred in persisting currency.");
                }
            }
        });
    }

    public List<CurrencyDetailsDto> getCurrencyDetailsDtoList() {
        // TODO Add executor
        if (mCurrencyDetailsList != null && mCurrencyDetailsList.size() > 0) {
            return mCurrencyDetailsList;
        } else {
            List<CurrencyDetailsDto> currencyDetailsDtos = new ArrayList<>();
            try {
                final Dao<CurrencyDetailsDto, Integer> currenctDetailsDao = mDatabaseHelper.getCurrencyDetailsDto();
                currencyDetailsDtos = currenctDetailsDao.queryForAll();
                mCurrencyDetailsList = currencyDetailsDtos;
            } catch (SQLException e) {
                e.printStackTrace();
                Log.d("Currency", "Some issue occurred fetching currency details.");
            }
            return currencyDetailsDtos;
        }
    }

    public void getCurrencyListForDb(final FetchCurrencyDetailsListener fetchCurrencyDetailsListener) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final Dao<CurrencyDetailsDto, Integer> currenctDetailsDao = mDatabaseHelper.getCurrencyDetailsDto();
                    List<String> currencyList = currenctDetailsDao.queryRaw("select currency from CurrencyDetailsDto", new RawRowMapper<String>() {
                        @Override
                        public String mapRow(String[] columnNames, String[] resultColumns) throws SQLException {
                            return resultColumns[0];
                        }
                    }).getResults();
                    fetchCurrencyDetailsListener.onFetchingCurrencyList(currencyList);
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.d("Currency", "Some issue occurred fetching currency details.");
                }
            }
        });

    }

    //public void getCurrencyFromCountry(FetchCountryFromCurrencyListener fetchCountryFromCurrencyListener,String country){

    public void getCurrencyFromCountry(final FetchCountryFromCurrencyListener fetchCountryFromCurrencyListener, final String country) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final Dao<CurrencyDetailsDto, Integer> currenctDetailsDao = mDatabaseHelper.getCurrencyDetailsDto();
                    // Method - 1
                    QueryBuilder<CurrencyDetailsDto, Integer> queryBuilder = currenctDetailsDao.queryBuilder();
                    queryBuilder.where().eq(CurrencyDetailsDto.COUNTRY_NAME, country);
                    // prepare our sql statement
                    PreparedQuery<CurrencyDetailsDto> preparedQuery = queryBuilder.prepare();
                    List<CurrencyDetailsDto> currencyDetails = currenctDetailsDao.query(preparedQuery);
                    //return currencyDetails.get(0).currency;
                    fetchCountryFromCurrencyListener.onFetchingCountryFromCurrency(currencyDetails.get(0).currency);

                    // Method - 2 (For reference)
                  /* // find out how many orders account-id #10 has
                    GenericRawResults<String[]> rawResults =
                            currenctDetailsDao.queryRaw(
                                    "select * from CurrencyDetailsDto where country ="+country);
                    // there should be 1 result
                    List<String[]> results = rawResults.getResults();
                    // the results array should have 1 value
                    String[] resultArray = results.get(0);
                    System.out.println("Currenct name:" + resultArray[0]); */

                    // Method - 3 (For reference)
                  /*  QueryBuilder<CurrencyDetailsDto, Integer> qb = currenctDetailsDao.queryBuilder();
                    qb.where().eq(CurrencyDetailsDto.COUNTRY_NAME, country);
                    GenericRawResults<String[]> results = currenctDetailsDao.queryRaw(qb.prepareStatementString());
                    List<String[]> results1 = results.getResults();
                    Log.d("Currency",results1.size()+"");*/


                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.d("Currency", "Some issue occurred fetching currency details.");
                }
            }
        });

    }

    public void saveNotificationDetails(final NotificationDetailsDto notificationDetailsDto) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Dao<NotificationDetailsDto, Integer> notificationDao = mDatabaseHelper.getNotificationDao();
                    notificationDao.create(notificationDetailsDto);
                    Log.d("Notification", "notification saved sucessfully.");
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.d("Notification", e.getMessage());
                }
            }
        });
    }

    public List<NotificationDetailsDto> getAllNotifictionsFromDb() {
       /* List<NotificationDetailsDto>  notificationList = new ArrayList<>();
        Dao<NotificationDetailsDto, Integer> notificationDao = null;
        try {
            notificationDao = mDatabaseHelper.getNotificationDao();
            notificationList = notificationDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notificationList; */

        // TODO Add executor
        List<NotificationDetailsDto> notificationList = new ArrayList<>();
        //fetch latest 10 records
        Dao<NotificationDetailsDto, Integer> notificationDao = null;
        try {
            notificationDao = mDatabaseHelper.getNotificationDao();
            if (notificationDao.countOf() > 0) {
                QueryBuilder<NotificationDetailsDto, Integer> builder = notificationDao.queryBuilder();
                builder.limit(10L);
                builder.orderBy("id", false);  // true for ascending, false for descending
                notificationList = notificationDao.query(builder.prepare());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notificationList;
    }

    public void saveBankDetailsInDb(final LoginResponseDto loginResponseDto, final Context context) {

        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // This is how, a reference of DAO object can be done
                    final Dao<LoginResponseDto.Bank, String> bankDao = mDatabaseHelper.getBankDao();
                    //This is the way to insert data into a database table
                    bankDao.create(loginResponseDto.bank);
                    Log.d("database", "Bank details saved successfully.");
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.d("database", e.getMessage());
                }
            }
        });
    }

    public void saveSearchHistoryDetails(final SearchHistoryDto searchHistoryDto) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Dao<SearchHistoryDto, Integer> searchHistoryDao = mDatabaseHelper.getSearchHistoryDao();
                    if (searchHistoryDao.countOf() >= 3) {
                        // Get latest 3 records.Delete the oldest record.Insert new record.
                        QueryBuilder<SearchHistoryDto, Integer> builder = searchHistoryDao.queryBuilder();
                        builder.limit(3L);
                        builder.orderBy("createdOn", false);  // true for ascending, false for descending
                        List<SearchHistoryDto> searchHistoryDetails = searchHistoryDao.query(builder.prepare());

                        if (!searchHistoryDetails.isEmpty() && searchHistoryDetails.size() == 3) {
                            // Delete the oldest record
                            searchHistoryDao.delete(searchHistoryDetails.get(2));
                        }
                    }
                    searchHistoryDao.create(searchHistoryDto);
                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }
        });


    }

    //    public List<SearchHistoryDto> getSearchHistoryDetails() {
//        List<SearchHistoryDto> searchHistoryDetails = new ArrayList<>();
//        Dao<SearchHistoryDto, Integer> searchDao = null;
//        try {
//            searchDao = mDatabaseHelper.getSearchHistoryDao();
//            //searchHistoryDetails = searchDao.queryForAll();
//            if (searchDao.countOf() > 0) {
//                QueryBuilder<SearchHistoryDto, Integer> builder = searchDao.queryBuilder();
//                builder.limit(3L);
//                builder.orderBy("createdOn", false);  // true for ascending, false for descending
//                searchHistoryDetails = searchDao.query(builder.prepare());
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return searchHistoryDetails;
//
//
//    }
    public void getSearchHistoryDetails(final FetchHistoryDetailsListener fetchHistoryDetailsListener) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                List<SearchHistoryDto> searchHistoryDetails = new ArrayList<>();
                Dao<SearchHistoryDto, Integer> searchDao = null;
                try {
                    searchDao = mDatabaseHelper.getSearchHistoryDao();
                    //searchHistoryDetails = searchDao.queryForAll();
                    if (searchDao.countOf() > 0) {
                        QueryBuilder<SearchHistoryDto, Integer> builder = searchDao.queryBuilder();
                        builder.limit(3L);
                        builder.orderBy("createdOn", false);  // true for ascending, false for descending
                        searchHistoryDetails = searchDao.query(builder.prepare());
                        fetchHistoryDetailsListener.onFetchingHistory(searchHistoryDetails);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public List<LoginResponseDto.Bank> getBankDetails() {
        // TODO Add executor
        List<LoginResponseDto.Bank> bankDetails = new ArrayList<>();
        Dao<LoginResponseDto.Bank, String> bankDao = null;
        try {
            bankDao = mDatabaseHelper.getBankDao();
            bankDetails = bankDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bankDetails;
    }

    public void removeAllTablesData() {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                if (mDatabaseHelper != null) {
                    mDatabaseHelper.removeAllTablesData();
                }
            }
        });
    }

    @Override
    public void cleanUp() {
        if (mDatabaseHelper != null) {
            mDatabaseHelper.close();
            mDatabaseHelper = null;
        }
        sDataBaseManager = null;
    }
}
