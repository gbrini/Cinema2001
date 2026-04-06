package test.com.cinema;

import com.cinema.model.dao.database.DatabaseConfig;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {
    @BeforeAll
    static void setUp() {
        DatabaseConfig.useTestDB();
    }
}
