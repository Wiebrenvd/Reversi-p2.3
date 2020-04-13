package Framework.server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

public class ServerMessage {
    protected MessageType messageType;

    protected String message;
    protected String[] arrData;
    protected HashMap<String, String> objData;

    protected Long time;

    public ServerMessage(String message, Long time){
        this.messageType = getType(message);
        this.message = getMessage(message);

        this.objData = getObjData();
        this.arrData = getArrData();

        this.time = time;
    }

    //bron: https://stackoverflow.com/questions/23308193/break-or-return-from-java-8-stream-foreach (find first)
    public MessageType getType(String message){
        MessageType[] mTypes = MessageType.values();
        Optional<MessageType> result = Arrays.stream(mTypes).filter(obj -> message.startsWith(obj.toString())).findFirst();

        if(result.isPresent())
            return result.get();

        return null;
    }

    public String getMessage(String message){
        if(this.messageType != null) {
            int offset = this.messageType.toString().length();

            if(offset < message.length())
                return message.substring(this.messageType.toString().length() + 1);
        }

        return message;
    }

    public String[] getArrData(){
        String[] data = new String[]{};

        if(this.messageType == null)
            return data;

        String mess = this.message;

        int start = mess.indexOf("[");
        int end = mess.indexOf("]");

        if(start >= 0 && end >= 0 && end > start) {
            JSONArray jArray = null;
            try {
                jArray = new JSONArray(mess.toString().substring(start, end+1));
                data = new String[jArray.length()];

                for(int i = 0; i < jArray.length(); i++){
                    data[i] = jArray.getString(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    public HashMap<String, String> getObjData(){
        HashMap<String, String> data = new HashMap<String, String>();

        if(this.messageType == null)
            return data;

        String mess = this.message;

        int start = mess.indexOf("{");
        int end = mess.indexOf("}");

        if(start >= 0 && end >= 0 && end > start) {

            JSONObject jObj = null;
            try {
                jObj = new JSONObject(mess.toString().substring(start, end+1));
                JSONArray jKeys = jObj.names();

                for(int i = 0; i < jKeys.length(); i++){
                    String key = jKeys.getString(i);
                    String val = jObj.getString(key);

                    data.put(key, val);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            System.out.println(mess);
        }

        return data;
    }

    public MessageType getType() {
        return this.messageType;
    }

    public Long getTime(){
        return this.time;
    }

    public String[] getArr(){
        return this.arrData;
    }

    public HashMap<String, String> getObj(){
        return this.objData;
    }

    public String getMessage(){
        return this.message;
    }

    public String toString(){
        if(this.messageType != null)
            return (this.messageType.toString() + " " + this.message);

        return this.message;
    }
}