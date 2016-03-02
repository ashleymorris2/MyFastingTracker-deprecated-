package com.avaygo.myfastingtracker.classes;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;


import java.io.File;

/**
 * Created by Ash on 02/03/2016.
 *
 * Hopefully this class will retrieve the database that is stored on the device that keeps record
 * of a users previous fasts.
 *
 * Will extend the functionality to hopefully save all the app data into on file.
 */
public class FileProvider extends android.support.v4.content.FileProvider {

    //reference URL:
    //https://developer.android.com/reference/android/support/v4/content/FileProvider.html

    /**
     *
     * @param context The calling applications' context
     * @param databaseName The name of the database that is to be exported. Best to get this directly
     *                     from the database class by calling something like LogDatabaseHelper.TABLE_NAME.
     *
     * @return URI for the database that is to be shared.
     */
    public Uri getDatabaseURI(Context context, String databaseName){

        File data = Environment.getDataDirectory();
        String currentDBPath = "//data//com.avaygo.myfastingtracker//databases//" + databaseName;

        File exportFile = new File(data, currentDBPath);

        return getFileUri(context, exportFile);
    }

    public Uri getFileUri(Context context, File file){
        return getUriForFile(context, "com.avaygo.fileprovider", file);
    }
}
