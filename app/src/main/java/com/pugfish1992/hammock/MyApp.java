package com.pugfish1992.hammock;

import android.app.Application;

import com.pugfish1992.hammock.data.source.SQLiteDataSource;
import com.pugfish1992.javario.Javario;

/**
 * Created by daichi on 11/3/17.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SQLiteDataSource dataSource = new SQLiteDataSource(this);
        Javario.initialize(dataSource);
    }
}
