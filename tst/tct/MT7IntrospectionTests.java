package tct;

import com.amazon.ata.test.reflect.ClassQuery;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("MT07")
public class MT7IntrospectionTests {
    private static final String BASE_PACKAGE = "com.amazon.ata.";
    private static final String SERVICE_PACKAGE = "service";
    private static final String DAO_PACKAGE = "dao";
    private static final String DATASTORE_PACKAGE = "datastore";
    private static final String PACKAGING_DAO_CLASS_NAME = "PackagingDAO";
    private static final String SHIPMENT_SERVICE_TEST_CLASS_NAME = "ShipmentServiceTest";
    private static final String SHIPMENT_SERVICE_CLASS_NAME = "ShipmentService";
    private static final String DATASTORE_CLASS_NAME = "PackagingDatastore";



    @Test
    void mt7_shipmentServiceTest_doesNotUseMockForPackagingDatastore() {
        // This test will always pass
        Class<?> shipmentServiceTestClass = ClassQuery
                .inExactPackage(BASE_PACKAGE + SERVICE_PACKAGE)
                .withExactSimpleName(SHIPMENT_SERVICE_TEST_CLASS_NAME)
                .findClassOrFail();
        Class<?> packagingDatastoreClass = ClassQuery
                .inExactPackage(BASE_PACKAGE + DATASTORE_PACKAGE)
                .withExactSimpleName(DATASTORE_CLASS_NAME)
                .findClassOrFail();

        Field packagingDatastoreMember = getUniqueFieldIfExists(shipmentServiceTestClass, packagingDatastoreClass);
        assertTrue(packagingDatastoreMember == null || !packagingDatastoreMember.isAnnotationPresent(Mock.class),
                "PackagingDatastore should not be mocked in ShipmentServiceTest");
    }



    private Field getUniqueFieldIfExists(final Class<?> containingClass, final Class<?> fieldClass) {
        Field[] fields = containingClass.getDeclaredFields();
        List<Field> matchingFields = new ArrayList<>();
        for (Field field : fields) {
            if (field.getType().equals(fieldClass)) {
                matchingFields.add(field);
            }
        }
        if (matchingFields.size() > 1) {
            fail(String.format(
                    "Unexpected: found more than one member of type %s in class %s",
                    fieldClass.getSimpleName(),
                    containingClass.getSimpleName())
            );
        }
        return matchingFields.isEmpty() ? null : matchingFields.get(0);
    }
}