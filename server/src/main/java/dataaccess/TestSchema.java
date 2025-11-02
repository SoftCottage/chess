package dataaccess;

public class TestSchema {
    public static void main(String[] args) {
        try {
            DatabaseManager.initialize();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }
}
