package com.anroidbrowser.browser.support;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;
import com.anroidbrowser.browser.MainApplication;
import com.anroidbrowser.browser.R;
import com.anroidbrowser.browser.dataBase.DBHelper;
import com.anroidbrowser.browser.myTypes.HistoryElement;
import com.anroidbrowser.browser.myTypes.Person;

import java.util.Calendar;

/**
 * Created by root on 09.10.17.
 */
public class DownloadBroadcast extends BroadcastReceiver{

    private Context context;

    public DownloadBroadcast(Context context){
        this.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        //Сообщение о том, что загрузка закончена
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)){
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            Cursor cursor = dm.query(query);
            if (cursor.moveToFirst()){
                int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(columnIndex)) {
                    String uriString = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    Toast.makeText(context, R.string.download_successful, Toast.LENGTH_SHORT).show();

                    if(MainApplication.getInstance().isAuthorization()){
                        Person p = MainApplication.getInstance().getPerson();
                        DBHelper dbHelper = new DBHelper(context);
                        dbHelper.addToDownloads(Calendar.getInstance().getTime().toString(), getMimeType(Uri.parse(uriString)), uriString, p.getId());
                        p.addDownload(new HistoryElement(Calendar.getInstance().getTime().toString(), getMimeType(Uri.parse(uriString)), uriString, p.getId()));
                    }
                }
            }
        }
//                Сообщение о клике по нотификации
        else if (DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(action)){
            DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            //несколько параллельных загрузок могут быть объеденены в одну нотификацию,
            //по этому мы пытаемся получить список всех загрузок, связанных с
            //выбранной нотификацией
            long[] ids = intent.getLongArrayExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS);
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(ids);
            Cursor cursor = dm.query(query);
            if (cursor.moveToFirst()){
                do {
                    //здесь вы можете получить id загрузки и
                    //реализовать свое поведение
                    String uriString = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    Uri uri = Uri.parse(uriString);
                    intent.setDataAndType(uri, getMimeType(uri));
                    context.startActivity(intent);
                } while (cursor.moveToNext());
            }

        }
    }
    public String getMimeType(Uri uri) {
        String mimeType;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }
}
