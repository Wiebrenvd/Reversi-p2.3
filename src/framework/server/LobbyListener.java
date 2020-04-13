package Framework.server;

import java.util.Map;

import Framework.controllers.LobbyController;
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
            ServerMessage challengeResponse = sc.lastRespContains(MessageType.CHALLENGE);
            ServerMessage matchResponse = sc.lastRespContains(MessageType.MATCH);

            if (challengeResponse != null) {
                Map<String, String> cData = challengeResponse.getObj();

                if (!hasMatch && !hasChallenge && !challengeNumber.equals(cData.get("CHALLENGENUMBER"))) {
                    hasChallenge = true; // deny challenge -> hasChallenge = false
                    challengeNumber = cData.get("CHALLENGENUMBER");
                    Platform.runLater(() -> lc.showChallengePopup(this, cData.get("CHALLENGER"), cData.get("CHALLENGENUMBER")));
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

