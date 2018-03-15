package co.buybuddy.networking.authentication.persistence;

/**
 * An abstract class for secure persistence implementations.
 */
public abstract class SecurePersistenceManager {
    /**
     * Persists given data in secure persistence layer.
     * @param data Data to be persisted.
     * @param type Type of the data.
     * @param key Key string to refer to the data later.
     */
    public abstract void persistData(byte[] data, SecureType type, String key);

    /**
     * Loads data from secure persistence layer.
     * @param type Type of the data.
     * @param key Key string to refer to the data.
     * @return A byte array containing data.
     */
    public abstract byte[] loadData(SecureType type, String key);

    /**
     * Removes data from secure persistence layer.
     * @param type Type of the data.
     * @param key Key string to refer to the data being removed.
     */
    public abstract void removeData(SecureType type, String key);
}
