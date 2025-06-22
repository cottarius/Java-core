package homework_1;

class SampleTest {

    @BeforeSuite
    public static void setup() {
        System.out.println("setup");
    }

    @AfterSuite
    public static void teardown() {
        System.out.println("teardown");
    }

    @BeforeTest
    public void beforeTest() {
        System.out.println("beforeTest");
    }

    @AfterTest
    public void afterTest() {
        System.out.println("afterTest");
    }

    @Test(priority = 10)
    public void testHighPriority() {
        System.out.println("testHighPriority");
    }

    @Test
    public void testDefaultPriority() {
        System.out.println("testDefaultPriority");
    }

    @Test(priority = 7)
    @CsvSource("10, Java, 20, true")
    public void testWithCsv(int a, String b, int c, boolean d) {
        System.out.printf("Csv Test: a=%d, b=%s, c=%d, d=%b%n", a, b, c, d);
    }

}
