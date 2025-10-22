package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.ClearResult;

public class ClearService {

    private final DataAccess dataAccess;

    public ClearService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public ClearResult clear() throws DataAccessException {
        dataAccess.clearDatabase();  // <-- updated to correct method
        return new ClearResult("Clear succeeded");
    }
}
