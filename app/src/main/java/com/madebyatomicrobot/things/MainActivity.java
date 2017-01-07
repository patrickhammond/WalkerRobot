package com.madebyatomicrobot.things;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

// There are lots of better ways of doing this...but it works...
public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView primeStatusView;
    private Button startView;
    private Button stopView;

    private Gpio gpio17;
    private Gpio gpio18;

    private AsyncTask<Void, Integer, Void> task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        primeStatusView = (TextView) findViewById(R.id.primes);
        startView = (Button) findViewById(R.id.start);
        stopView = (Button) findViewById(R.id.stop);

        startView.setEnabled(true);
        stopView.setEnabled(false);

        startView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showPrimes();
            }
        });

        stopView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });

        PeripheralManagerService manager = new PeripheralManagerService();
        try {
            gpio17 = manager.openGpio("BCM17");
            gpio17.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH);
            gpio17.setActiveType(Gpio.ACTIVE_LOW);

            gpio18 = manager.openGpio("BCM18");
            gpio18.setDirection(Gpio.DIRECTION_IN);
            gpio18.setActiveType(Gpio.ACTIVE_HIGH);

            gpio18.setEdgeTriggerType(Gpio.EDGE_FALLING);
            gpio18.registerGpioCallback(buttonCallback);
        } catch (IOException ex) {
            Log.w(TAG, "Unable to access GPIO", ex);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTask();

        if (gpio17 != null) {
            try {
                gpio17.close();
                gpio17 = null;
            } catch (IOException e) {
                Log.w(TAG, "Unable to close BCM17", e);
            }
        }

        if (gpio18 != null) {
            gpio18.unregisterGpioCallback(buttonCallback);

            try {
                gpio18.close();
                gpio18 = null;
            } catch (IOException e) {
                Log.w(TAG, "Unable to close BCM18", e);
            }
        }
    }

    private void showPrimes() {
        cancelTask();

        startView.setEnabled(false);
        stopView.setEnabled(true);

        task = new AsyncTask<Void, Integer, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                List<Integer> primes = Arrays.asList(2,3,5,7,11,13,17,19,23,29);
                try {
                    gpio17.setValue(false);
                    for (Integer prime : primes) {
                        publishProgress(prime);
                        for (int i = 0; i < prime; i++) {
                            Thread.sleep(250);
                            gpio17.setValue(true);
                            Thread.sleep(250);
                            gpio17.setValue(false);
                        }

                        gpio17.setValue(false);
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException ex) {
                    return null;
                }
                catch (Exception ex) {
                    Log.e(TAG, "Damn it!", ex);
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                primeStatusView.setText(primeStatusView.getText().toString() + " " + values[0].toString().trim());
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                startView.setEnabled(true);
                stopView.setEnabled(false);

                task = null;
            }
        }.execute();
    }

    private void reset() {
        cancelTask();

        primeStatusView.setText("");
        try {
            gpio17.setValue(false);
        } catch (IOException e) {
            Log.e(TAG, "Damn it!", e);
        }

        startView.setEnabled(true);
        stopView.setEnabled(false);
    }

    private GpioCallback buttonCallback = new GpioCallback() {
        @Override
        public boolean onGpioEdge(Gpio gpio) {
            try {
                if (gpio.getValue()) {
                    Log.v(TAG, "on");
                    showPrimes();
                } else {
                    Log.v(TAG, "off");
                    reset();
                }
            } catch (IOException e) {
                Log.e(TAG, "Damn it!", e);
            }

            return true;
        }

        @Override
        public void onGpioError(Gpio gpio, int error) {
            Log.w(TAG, gpio + ": Error event " + error);
        }
    };

    private void cancelTask() {
        if (task != null) {
            task.cancel(true);
        }
    }
}
