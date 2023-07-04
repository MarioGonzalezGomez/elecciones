package mgg.code.util;

public class DB {
    private static DB db;

    private DB() {}
    public static DB getInstance() {
        if (db == null) {
            db = new DB();
        }
        return db;
    }


    //TODO:Implementar estos métodos
    public String getDb() {
        String dbActual = "";
        return dbActual;
    }

    public String aPrincipal() {
        return null;
    }

    public String aReserva() {
        return null;
    }

    public String aLocal() {
        return null;
    }

}
