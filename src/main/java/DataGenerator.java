/**
 * Generates the test data for a MySQL NDB Cluster like in the master's thesis of Kevin Klugmann.
 * For an explanation of the test scenario have a look at page 11 of the thesis.
 *
 * This generator will create a MySQL script with INSERT statements for the MySQL NDB Cluster.
 * It will be saved in the resources folder of this project.
 *
 * It could take a long time to insert each value with one INSERT STATEMENT so you could use a batch statement.
 * For that use the switches {@code USE_ONE_INSERT_FOR_EACH_VALUE} and {@code USE_BATCH_INSERT}.
 * I had the experience that the batch statement not always works with a MYSQL NDB Cluster especially with a
 * big amount of values.
 *
 * @author Kevin Klugmann
 */
public class DataGenerator {
    private final long OVERALL_TIMESPAN_IN_DAYS = 365;
    private final long INTERVALL_TIMESPAN_IN_MINUTES = 15;

    // Values out of the table from the masterthesis (p. 17 table 3.1)
    private final long VALUES_PER_INTERVALL_TEST_CASE_1 =  30;
    private final long VALUES_PER_INTERVALL_TEST_CASE_2 =  60;
    private final long VALUES_PER_INTERVALL_TEST_CASE_3 =  90;
    private final long VALUES_PER_INTERVALL_TEST_CASE_4 =  120;

    private final String USE_ONE_INSERT_FOR_EACH_VALUE = "each_value";
    private final String USE_BATCH_INSERT = "batch_insert";

    public DataGenerator() {

    }

    public static void main(String... args) {

    }
}
