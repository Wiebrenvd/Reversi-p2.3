package framework.server;

import java.util.Map;

import framework.controllers.LobbyController;
import framework.server.ServerConnection;
import javafx.application.Platform;

public class LobbyListener implements Runnable {


    private LobbyController lc;
    private ServerConnection sc;
    private boolean running = true;
    private boolean hasChallenge = false;
    private boolean hasMatch = false;
    private String challengeNumber;

    public LobbyListener(LobbyController lc, ServerConnection sc) {
        this.lc = lc;
        this.sc = sc;
    }

    @Override
    public void run() {
        challengeNumber = "";
        while (true) {
            String challengeResponse = sc.lastRespContains("SVR GAME CHALLENGE");
            String matchResponse = sc.lastRespContains("SVR GAME MATCH");


            if (challengeResponse != null) {

                Map<String, String> map = sc.getMap(challengeResponse);

                if (!hasMatch && !hasChallenge && !challengeNumber.equals(map.get("CHALLENGENUMBER"))) {
                    hasChallenge = true; // deny challenge -> hasChallenge = false
                    challengeNumber = map.get("CHALLENGENUMBER");
                    Platform.runLater(() -> lc.showChallengePopup(this, map.get("CHALLENGER"), map.get("CHALLENGENUMBER")));
                }

            }

            if (!hasMatch && matchResponse != null) {
                hasMatch = true;
                Platform.runLater(() -> lc.start());
                break;
            }


        }


    }

    public void setHasChallenge(boolean b) {
        hasChallenge = b;
    }


}

