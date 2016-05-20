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
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    BluetoothManager bluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            Toast.makeText(this, "NO BLE", Toast.LENGTH_SHORT).show();
//            finish();
        }
    }

    public void findBLEDevices() {
        final BluetoothLeScanner bleScanner = mBluetoothAdapter.getBluetoothLeScanner();
        ScanSettings scanSettings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build();
        if (bleScanner == null) {
            return;
        }
        bleScanner.startScan(new ArrayList<ScanFilter>(), scanSettings, new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                Toast.makeText(getApplicationContext(), "FOUND", Toast.LENGTH_SHORT).show();

                switch (callbackType) {
                    case ScanSettings.CALLBACK_TYPE_ALL_MATCHES:
                        Toast.makeText(getApplicationContext(), "All matches ", Toast.LENGTH_SHORT).show();
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

                bleScanner.stopScan(this);
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                Toast.makeText(getApplicationContext(), "BATCH", Toast.LENGTH_LONG).show();
                super.onBatchScanResults(results);
            }

            @Override
            public void onScanFailed(int errorCode) {
                Toast.makeText(getApplicationContext(), "FAILED", Toast.LENGTH_LONG).show();
                super.onScanFailed(errorCode);
            }
        });
    }

    public void connectGatt(BluetoothDevice device) {
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
//            Toast.makeText(this, status == BluetoothGatt.GATT_SUCCESS ? "SUCCESS" : "DUPA" + " ConnectionStateChanged to: " + state , Toast.LENGTH_SHORT).show();
            super.onConnectionStateChange(gatt, status, newState);
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
        }
    };
}