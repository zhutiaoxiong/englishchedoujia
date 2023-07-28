package view.view4me.nfcmoudle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.BlueInstructionCollection;

public class ManagerNfcBlue {
    private static ManagerNfcBlue _instance;
    private List<NfcData> listNfc;
    private List<NfcData> listNfcId;

    public static ManagerNfcBlue getInstance() {
        if (_instance == null)
            _instance = new ManagerNfcBlue();
        return _instance;
    }

    public void saveListNfc(Map<String, Integer> map) {
        List<NfcData> list = new ArrayList<>();
        list.add(new NfcData("Card One", map.get("cardOne") + ""));
        list.add(new NfcData("Card Two", map.get("cardTwo") + ""));
        list.add(new NfcData("Card Three", map.get("cardThree") + ""));
        list.add(new NfcData("Card Four", map.get("cardFour") + ""));
        list.add(new NfcData("Card Five", map.get("cardFive") + ""));
        listNfc = list;
        List<NfcData> listId = new ArrayList<>();
        listId.add(new NfcData("Card One", map.get("cardSix") + ""));
        listId.add(new NfcData("Card Two", map.get("cardSeven") + ""));
        listId.add(new NfcData("Card Three", map.get("cardEight") + ""));
        listNfcId= listId;
    }

    public List<NfcData> getListNfc() {
        return listNfc;
    }

    public List<NfcData> getListNfcId() {
        return listNfcId;
    }
}
