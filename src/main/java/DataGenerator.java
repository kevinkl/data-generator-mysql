import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.io.FileWriter;
import java.util.Random;

/**
 * Generates the test data for a MySQL NDB Cluster like in the master's thesis of Kevin Klugmann.
 * For an explanation of the test scenario have a look at page 11 of the thesis.
 * The setup of the MySQL NDB Cluster starts at the page 35 of the thesis.
 * <p>
 * This generator will create a MySQL script with INSERT statements for the MySQL NDB Cluster.
 * It will be saved in the resources folder of this project.
 * <p>
 * It could take a long time to insert each value with one INSERT STATEMENT so you could use a batch statement.
 * I had the experience that the batch statement not always works with a MYSQL NDB Cluster especially with a
 * big amount of values. If you want to use a batch statement you have to rewrite the code.
 * <p>
 * The test scenario is with 4 timeseries (see page 15 in thesis) so 4 sql scripts will be generated.
 *
 * Have fun you guys!! Hope my thesis helps you! Keep me posted with your results, that would be really great :)
 *
 * @author Kevin Klugmann
 */
public class DataGenerator {
    // The files will be named "script1.sql", "script2.sql", "script3.sql" and "script4.sql"
    private final String PATH_FOR_SCRIPTS = "/home/kevin/Desktop/"; // should end with a "/" or for windows with "\\"
    private final String START_OF_SQL_FILENAME = "script";
    private final String ENDING_OF_SQL_FILENAME = ".sql";

    private final Duration OVERALL_TIMESPAN_IN_DAYS = Duration.standardDays(365);
    private final Duration INTERVALL_TIMESPAN_IN_MINUTES = Duration.standardMinutes(15L);
    private final DateTime START_DATE = DateTime.now().withTimeAtStartOfDay();

    // Values out of the table from the masterthesis (p. 17 table 3.1)
    private final long VALUES_PER_INTERVALL_TEST_CASE_1 = 30;
    private final long VALUES_PER_INTERVALL_TEST_CASE_2 = 60;
    private final long VALUES_PER_INTERVALL_TEST_CASE_3 = 90;
    private final long VALUES_PER_INTERVALL_TEST_CASE_4 = 120;

    // Choose out of the four test scenarios above
    private final long USED_TEST_SCENARIO_GRID = VALUES_PER_INTERVALL_TEST_CASE_1;


    private final double VALUE_FACTOR = 0.1;
    private final double START_VALUE = 0;
    // Just a random insert timestamp
    private final long INSERT_TS = 1497009245751l;


    public static void main(String... args) {
        new DataGenerator();
    }

    public DataGenerator() {
        startGeneratingData();
    }

    private void startGeneratingData() {
        try {
            for (int i = 1; i <= 4; i++) {
                String path = PATH_FOR_SCRIPTS + START_OF_SQL_FILENAME + Integer.toString(i) + ENDING_OF_SQL_FILENAME;
                FileWriter fw = new FileWriter(path,
                        false);
                // Just a random String with 128 characters. That's what I used in my thesis (look at page 20 UUID table 3.5)
                String token = generateRandomString(128);
                generateValuesFixTimeSpanFile(fw, token);
                System.out.println("Successfully created script: " + path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateValuesFixTimeSpanFile(FileWriter fw, String token) throws Exception {
        if (USED_TEST_SCENARIO_GRID > INTERVALL_TIMESPAN_IN_MINUTES.getMillis()) {
            throw new Exception("Values per grid are bigger than gird duration");
        }

        double startValue = START_VALUE;
        DateTime startDate = START_DATE;
        // Calculate the amount of the grids for the overallTime
        float amountOfGridsTmp = (float) OVERALL_TIMESPAN_IN_DAYS.getMillis() / (float) INTERVALL_TIMESPAN_IN_MINUTES.getMillis();
        int amountOfGrids = (int) Math.ceil(amountOfGridsTmp);

        DateTime createTS = startDate.plus(0);
        for (int i = 0; i < amountOfGrids; i++) {
            for (int valueIdx = 0; valueIdx < USED_TEST_SCENARIO_GRID; valueIdx++) {
                String insertString = createSqlInsert(token, INSERT_TS, createTS, startValue);
                fw.append(insertString);
                fw.append("\n");
                createTS = createTS.plusMillis(1);
                startValue += VALUE_FACTOR;
            }
            createTS = startDate.plus(INTERVALL_TIMESPAN_IN_MINUTES);
            startDate = createTS;
        }
        fw.close();
    }

    private static String createSqlInsert(String token, long insertTS, DateTime createTS, double value) {
        String insertTSStr = Long.toString(insertTS);
        String createTSString = Long.toString(createTS.getMillis());
        String valueString = Double.toString(value);
        return "INSERT INTO TSAAS_REL_DV_VALUES VALUES (\'" + token + "\', " + valueString + ", " + createTSString + ", " + insertTSStr + ");";
    }

    private String generateRandomString(int numberOfCharacters) {
        // Source: https://stackoverflow.com/questions/20536566/creating-a-random-string-with-a-z-and-0-9-in-java
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyyz1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < numberOfCharacters) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }
}
