// ============================================
// EJEMPLO 5: Test Doble con Excepción - Mock
// ============================================

package medicalconsultation.services.doubles;

import medicalconsultation.services.*;
import java.net.ConnectException;

public class HealthNationalServiceMock implements HealthNationalService {

    private boolean throwConnectException = false;
    private boolean throwHealthCardIDException = false;

    public void setThrowConnectException(boolean value) {
        this.throwConnectException = value;
    }

    public void setThrowHealthCardIDException(boolean value) {
        this.throwHealthCardIDException = value;
    }

    @Override
    public MedicalHistory getMedicalHistory(HealthCardID cip)
            throws ConnectException, HealthCardIDException {

        if (throwConnectException) {
            throw new ConnectException("Network error");
        }
        if (throwHealthCardIDException) {
            throw new HealthCardIDException("CIP not registered");
        }

        // Comportamiento controlado del mock
        return new MedicalHistory(cip, 100);
    }

    @Override
    public void updateMedicalHistory(MedicalHistory history)
            throws ConnectException {
        throw new UnsupportedOperationException("Not implemented in mock");
    }

    @Override
    public boolean existsHealthCardID(HealthCardID cip)
            throws ConnectException {
        throw new UnsupportedOperationException("Not implemented in mock");
    }

    @Override
    public void registerHealthCard(HealthCardID cip)
            throws ConnectException {
        throw new UnsupportedOperationException("Not implemented in mock");
    }
}
