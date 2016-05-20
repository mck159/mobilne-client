package com.example.maciek.myapplication;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    BluetoothManager bluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    TextView textViewConsole;
    private TextViewLogger tvLogger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        

        setContentView(R.layout.activity_main);
        textViewConsole = (TextView) findViewById(R.id.textViewConsole);
        this.tvLogger = new TextViewLogger(textViewConsole);
        tvLogger.info("Starting application...");

        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();


//        checkIfBLE();


        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
//        Intent enableLocationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//        startActivityForResult(enableLocationIntent, 2);

        enableBluetooth();
        findBLEDevices();
    }

    public void enableBluetooth() {
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }
    }

    public void checkIfBLE() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            tvLogger.error("NO BLE!!");
        }
    }

    public void findBLEDevices() {
        final BluetoothLeScanner bleScanner = mBluetoothAdapter.getBluetoothLeScanner();
        ScanSettings scanSettings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build();
        if (bleScanner == null) {
            tvLogger.error("NO BLE SCANNER");
            return;
        }

        tvLogger.info("Starting scanning for BLE Devices");

        bleScanner.startScan(new ArrayList<ScanFilter>(), scanSettings, new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                String resultString = "unknown";


                switch (callbackType) {
                    case ScanSettings.CALLBACK_TYPE_ALL_MATCHES:
                        resultString = "All matches";
                        connectGatt(result.getDevice());
                        break;
                    case ScanSettings.CALLBACK_TYPE_FIRST_MATCH:
                        Toast.makeText(getApplicationContext(), "First match", Toast.LENGTH_SHORT).show();
                        break;
                    case ScanSettings.CALLBACK_TYPE_MATCH_LOST:
                        Toast.makeText(getApplicationContext(), "Lost match", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "??", Toast.LENGTH_SHORT).show();
                }

                tvLogger.important("Scan result '%s', DEVICE: '%s'", resultString, result.getDevice().getName());

                bleScanner.stopScan(this);
                tvLogger.important("stoppedScan");
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                tvLogger.warn("Batch");
                super.onBatchScanResults(results);
            }

            @Override
            public void onScanFailed(int errorCode) {
                tvLogger.warn("Failed");
                super.onScanFailed(errorCode);
            }
        });
    }

    public void connectGatt(BluetoothDevice device) {
        tvLogger.important("Connecting to gatt device '%s'", device.getName());
        device.connectGatt(getApplicationContext(), false, gattCallback);
    }

    BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String state = "unknown";
            switch(status) {
                case BluetoothProfile.STATE_CONNECTED:
                    state = "connected";
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    state = "disconnected";
                    break;
            }
            tvLogger.important("Gatt conn state changed to '%s' with '%s'", state, status == BluetoothGatt.GATT_SUCCESS ? "SUCCESS" : "FAIL");
            super.onConnectionStateChange(gatt, status, newState);
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            tvLogger.warn("1");
            super.onServicesDiscovered(gatt, status);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            tvLogger.warn("2");
            super.onCharacteristicRead(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            tvLogger.warn("3");
            super.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            tvLogger.warn("4");
            super.onCharacteristicChanged(gatt, characteristic);
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            tvLogger.warn("5");
            super.onDescriptorRead(gatt, descriptor, status);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            tvLogger.warn("6");
            super.onDescriptorWrite(gatt, descriptor, status);
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            tvLogger.warn("7");
            super.onReliableWriteCompleted(gatt, status);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            tvLogger.warn("8");
            super.onReadRemoteRssi(gatt, rssi, status);
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            tvLogger.warn("?");
            super.onMtuChanged(gatt, mtu, status);
        }
    };
}