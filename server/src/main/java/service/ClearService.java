package service;

import dataaccess.InMemoryDataAccess;
import dataaccess.DataAccessException;
import requestresult.ClearResult;

public class ClearService {

    private final InMemoryDataAccess inMemoryDataAccess;

    public ClearService(InMemoryDataAccess inMemoryDataAccess) {
        this.inMemoryDataAccess = inMemoryDataAccess;
    }

    public ClearResult clear() throws DataAccessException {
        inMemoryDataAccess.clearDatabase();
        return new ClearResult("Clear succeeded");
    }
}
