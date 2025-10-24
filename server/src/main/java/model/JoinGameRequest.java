package model;

public class JoinGameRequest {
    private String authToken;
    private Integer gameID;
    private String playerColor; // "WHITE" or "BLACK"

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
