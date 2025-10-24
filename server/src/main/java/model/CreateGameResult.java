// CreateGameResult.java
package model;

public class CreateGameResult {
    private Integer gameID;
    private String message;

    // Success constructor
    public CreateGameResult(int gameID) {
        this.gameID = gameID;
        this.message = null;
    }

    // Error constructor
    public CreateGameResult(String message) {
        this.message = message;
        this.gameID = null;
    }

    public Integer getGameID() { return gameID; }
    public String getMessage() { return message; }
}
