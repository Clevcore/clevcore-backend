package ar.com.clevcore.backend.utils;

import org.eclipse.persistence.exceptions.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.clevcore.backend.exceptions.ClevcoreException;
import ar.com.clevcore.backend.exceptions.ClevcoreException.Severity;

public final class ExceptionUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionUtils.class);

    private ExceptionUtils() {
        throw new AssertionError();
    }

    public static void treateException(Exception exception, Object object) throws ClevcoreException {
        LOG.error("[E] Exception Received with object {} ", object.getClass().getName(), exception);

        if (exception.getCause() instanceof DatabaseException) {
            DatabaseException databaseException = (DatabaseException) exception.getCause();

            throw new ClevcoreException(Severity.ERROR, exception.getMessage(), databaseException.getErrorCode(),
                    exception.getCause(), object);
        } else {
            throw new ClevcoreException(Severity.FATAL, exception.getMessage(), -1, exception.getCause(), object);
        }
    }

}
