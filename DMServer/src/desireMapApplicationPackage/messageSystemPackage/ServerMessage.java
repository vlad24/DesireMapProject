package desireMapApplicationPackage.messageSystemPackage;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ServerMessage implements Serializable {

    public List<String> registration_ids;
    public Map<String,String> data;

    public void addReceiver(String regId){
        if(registration_ids == null){
        	registration_ids = new LinkedList<String>();
        }
        registration_ids.add(regId);
    }

    public void fillServerMessage(String mID, String sender, String text){
        if(data == null){
            data = new HashMap<String,String>();
        }

        data.put("messageID", mID);
        data.put("sender", sender);
        data.put("message", text);
    }
}