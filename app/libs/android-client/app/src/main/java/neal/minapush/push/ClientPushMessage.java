package neal.minapush.push;

import android.widget.EditText;

/**
 *                ┏┓　　  ┏┓+ +
 * 　　　　　　　┏┛┻━━━┛┻┓ + +
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　━　　　┃ ++ + + +
 * 　　　　　 ████━████ ┃+
 * 　　　　　　　┃　　　　　　　┃ +
 * 　　　　　　　┃　　　┻　　　┃
 * BoosYWZ.   　┃　　　　　　　┃ + +
 * 　　　　　　  ┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃ + + + +
 * 　　　　　　　　　┃　　　┃　　　　Code is far away from bug with the animal protecting
 * 　　　　　　　　　┃　　　┃ + 　　　　神兽保佑,代码无bug
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃　　+
 * 　　　　　　　　　┃　 　 ┗━━━┓ + +
 * 　　　　　　　　　┃ 　　　　　　 ┣┓
 * 　　　　　　　　　┃ 　　　　　　 ┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛ + + + +
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛+ + + +
 * @author:     杨文蓁的小迷弟
 * @version:    2019.7.26 V.1.0
 * @param:      PushMessage
 * @deprecated:
 **/
public class ClientPushMessage {

    public String MSG;
    public ClientPushMessage(EditText et){
        MSG = et.getText().toString();
    }

    public String UseMsgValue(){
        return MSG;
    }
}
