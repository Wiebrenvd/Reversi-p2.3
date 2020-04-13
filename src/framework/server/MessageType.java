package Framework.server;

public enum MessageType {
    OK("OK"),
    YOURTURN("SVR GAME YOURTURN"),
    MATCH("SVR GAME MATCH"),
    MOVE("SVR GAME MOVE"),
    WIN("SVR GAME WIN"),
    LOSS("SVR GAME LOSS"),
    DRAW("SVR GAME DRAW"),
    CHALLENGE("SVR GAME CHALLENGE"),
    PLAYERLIST("SVR PLAYERLIST"),
    GAMELIST("SVR GAMELIST"),
    NOMATCH("ERR Not in any match"),
    NOERROR("ERR java.lang.IllegalStateException:"),
    ERROR("ERR");

    private String title;

    MessageType(String title){
        this.title = title;
    }

    public String toString(){
        return this.title;
    }
}
