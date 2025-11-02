package requestresult;

public class JoinGameModels {

    // Join Game Request
    public static class JoinGameRequest {
        private String authToken;
        private Integer gameID;
        private String playerColor;

        public JoinGameRequest() {}

        public JoinGameRequest(Integer gameID, String playerColor, String authToken) {
            this.gameID = gameID;
            this.playerColor = playerColor;
            this.authToken = authToken;
        }

        public Integer getGameID() { return gameID; }
        public void setGameID(Integer gameID) { this.gameID = gameID; }

        public String getPlayerColor() { return playerColor; }
        public void setPlayerColor(String playerColor) { this.playerColor = playerColor; }

        public String getAuthToken() { return authToken; }
        public void setAuthToken(String authToken) { this.authToken = authToken; }
    }

    // Join Game Result
    public static class JoinGameResult {
        private String message;

        public JoinGameResult() {}
        public JoinGameResult(String message) { this.message = message; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
