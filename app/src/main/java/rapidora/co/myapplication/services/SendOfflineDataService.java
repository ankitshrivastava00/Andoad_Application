package rapidora.co.myapplication.services;

import android.app.IntentService;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import rapidora.co.myapplication.common.CustomLogger;
import rapidora.co.myapplication.common.Utils;
import rapidora.co.myapplication.db.DbUtils;
import rapidora.co.myapplication.model.OfflineDataModel;


/**
 * Created by azmat.ali.khan on 05/05/16.
 */
public class SendOfflineDataService extends IntentService {

    public SendOfflineDataService() {
        super(SendOfflineDataService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            List<OfflineDataModel> offlineDataModelList = DbUtils.getOfflineData(this);
            for (OfflineDataModel offlineModel : offlineDataModelList) {
                String response = null;
                try {
                    CustomLogger.getInsatance(this).putLog("::Offline Request::" + offlineModel.getData());
                    response = Utils.makeHttpURLConnection(offlineModel.getUrl(), offlineModel.getData(), this);
                    Log.e("send offflinde Data", response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (response != null) {
                    DbUtils.deleteOfflineRecord(offlineModel.getId(), this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
