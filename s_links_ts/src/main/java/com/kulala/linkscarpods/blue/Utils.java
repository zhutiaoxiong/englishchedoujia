package com.kulala.linkscarpods.blue;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class includes a small subset of standard GATT attributes for demonstration purposes.
 */
public class Utils {

    private static HashMap<Integer, String> serviceTypes = new HashMap();
    static {
        // Sample Services.
        serviceTypes.put(BluetoothGattService.SERVICE_TYPE_PRIMARY, "PRIMARY");
        serviceTypes.put(BluetoothGattService.SERVICE_TYPE_SECONDARY, "SECONDARY");
    }

    public static String getServiceType(int type){
        return serviceTypes.get(type);
    }


    //-------------------------------------------
    private static HashMap<Integer, String> charPermissions = new HashMap();
    static {
        charPermissions.put(0, "UNKNOW");
        charPermissions.put(BluetoothGattCharacteristic.PERMISSION_READ, "READ");
        charPermissions.put(BluetoothGattCharacteristic.PERMISSION_READ_ENCRYPTED, "READ_ENCRYPTED");
        charPermissions.put(BluetoothGattCharacteristic.PERMISSION_READ_ENCRYPTED_MITM, "READ_ENCRYPTED_MITM");
        charPermissions.put(BluetoothGattCharacteristic.PERMISSION_WRITE, "WRITE");
        charPermissions.put(BluetoothGattCharacteristic.PERMISSION_WRITE_ENCRYPTED, "WRITE_ENCRYPTED");
        charPermissions.put(BluetoothGattCharacteristic.PERMISSION_WRITE_ENCRYPTED_MITM, "WRITE_ENCRYPTED_MITM");
        charPermissions.put(BluetoothGattCharacteristic.PERMISSION_WRITE_SIGNED, "WRITE_SIGNED");
        charPermissions.put(BluetoothGattCharacteristic.PERMISSION_WRITE_SIGNED_MITM, "WRITE_SIGNED_MITM");
    }

    public static String getCharPermission(int permission){
        return getHashMapValue(charPermissions,permission);
    }
    //-------------------------------------------
    private static HashMap<Integer, String> charProperties = new HashMap();
    static {

        charProperties.put(BluetoothGattCharacteristic.PROPERTY_BROADCAST, "BROADCAST");
        charProperties.put(BluetoothGattCharacteristic.PROPERTY_EXTENDED_PROPS, "EXTENDED_PROPS");
        charProperties.put(BluetoothGattCharacteristic.PROPERTY_INDICATE, "INDICATE");
        charProperties.put(BluetoothGattCharacteristic.PROPERTY_NOTIFY, "NOTIFY");
        charProperties.put(BluetoothGattCharacteristic.PROPERTY_READ, "READ");
        charProperties.put(BluetoothGattCharacteristic.PROPERTY_SIGNED_WRITE, "SIGNED_WRITE");
        charProperties.put(BluetoothGattCharacteristic.PROPERTY_WRITE, "WRITE");
        charProperties.put(BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE, "WRITE_NO_RESPONSE");
    }

    public static String getCharPropertie(int property){
        return getHashMapValue(charProperties,property);
    }
    public static boolean getCharPropertieMatch(int propertyUsed,int[] needs){
        List<Integer> list = getElement(propertyUsed);
        boolean[] matchs = new boolean[needs.length];
        for(int value : list){
            for(int i = 0;i<needs.length;i++){
                if(value == needs[i])matchs[i] = true;
            }
        }
        for(int i = 0;i<matchs.length;i++){
            if(matchs[i]==false)return false;
        }
        return true;
    }

    //--------------------------------------------------------------------------
    private static HashMap<Integer, String> descPermissions = new HashMap();
    static {
        descPermissions.put(0, "UNKNOW");
        descPermissions.put(BluetoothGattDescriptor.PERMISSION_READ, "READ");
        descPermissions.put(BluetoothGattDescriptor.PERMISSION_READ_ENCRYPTED, "READ_ENCRYPTED");
        descPermissions.put(BluetoothGattDescriptor.PERMISSION_READ_ENCRYPTED_MITM, "READ_ENCRYPTED_MITM");
        descPermissions.put(BluetoothGattDescriptor.PERMISSION_WRITE, "WRITE");
        descPermissions.put(BluetoothGattDescriptor.PERMISSION_WRITE_ENCRYPTED, "WRITE_ENCRYPTED");
        descPermissions.put(BluetoothGattDescriptor.PERMISSION_WRITE_ENCRYPTED_MITM, "WRITE_ENCRYPTED_MITM");
        descPermissions.put(BluetoothGattDescriptor.PERMISSION_WRITE_SIGNED, "WRITE_SIGNED");
        descPermissions.put(BluetoothGattDescriptor.PERMISSION_WRITE_SIGNED_MITM, "WRITE_SIGNED_MITM");
    }

    public static String getDescPermission(int property){
        return getHashMapValue(descPermissions,property);
    }


    private static String getHashMapValue(HashMap<Integer, String> hashMap,int number){
        String result =hashMap.get(number);
        if(TextUtils.isEmpty(result)){
            List<Integer> numbers = getElement(number);
            result="";
            for(int i=0;i<numbers.size();i++){
                result+=hashMap.get(numbers.get(i))+"|";
            }
        }
        return result;
    }

    /**
     * 位运算结果的反推函数10 -> 2 | 8;
     */
    static private List<Integer> getElement(int number){
        List<Integer> result = new ArrayList<Integer>();
        for (int i = 0; i < 32; i++){
            int b = 1 << i;
            if ((number & b) > 0)
                result.add(b);
        }

        return result;
    }

}
