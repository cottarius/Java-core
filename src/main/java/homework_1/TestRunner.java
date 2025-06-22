package homework_1;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TestRunner {
    public static void runTests(Class<?> testClass) {
        Method beforeSuit = null;
        Method afterSuit = null;
        List<Method> beforeTests = new ArrayList<>();
        List<Method> afterTests = new ArrayList<>();
        List<Method> testMethods = new ArrayList<>();

        for (Method method : testClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                if (beforeSuit != null) {
                    throw new RuntimeException("Before suite method has already been set");
                }
                if (!Modifier.isStatic(method.getModifiers())) {
                    throw new RuntimeException("Before suite method is not static");
                }
                beforeSuit = method;
            }
            if (method.isAnnotationPresent(AfterSuite.class)) {
                if (afterSuit != null) {
                    throw new RuntimeException("After suite method has already been set");
                }
                if (!Modifier.isStatic(method.getModifiers())) {
                    throw new RuntimeException("After suite method is not static");
                }
                afterSuit = method;
            }
            if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method);
            }
            if (method.isAnnotationPresent(BeforeTest.class)) {
                beforeTests.add(method);
            }
            if (method.isAnnotationPresent(AfterTest.class)) {
                afterTests.add(method);
            }
        }

        testMethods.sort(Comparator.comparingInt(m -> -m.getAnnotation(Test.class).priority()));

        try {
            Object instance = testClass.getDeclaredConstructor().newInstance();

            if (beforeSuit != null) {
                beforeSuit.invoke(null);
            }

            for (Method test : testMethods) {
                for (Method beforeTest : beforeTests) {
                    beforeTest.invoke(instance);
                }

                if (test.isAnnotationPresent(CsvSource.class)) {
                    String[] parts = test.getAnnotation(CsvSource.class).value().split(",\\s");
                    Object[] args = new Object[parts.length];
                    Class<?>[] types = test.getParameterTypes();

                    if (types.length != parts.length) {
                        throw new RuntimeException("Wrong number of parameters for test " + test.getName());
                    }
                    for (int i = 0; i < parts.length; i++) {
                        args[i] = parseArg(parts[i], types[i]);
                    }
                    test.invoke(instance, args);
                } else {
                    test.invoke(instance);
                }

                for (Method afterTest : afterTests) {
                    afterTest.invoke(instance);
                }
            }

            if (afterSuit != null) {
                afterSuit.invoke(null);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Object parseArg(String s, Class<?> type) {
        if (type == int.class) return Integer.parseInt(s);
        if (type == boolean.class) return Boolean.parseBoolean(s);
        if (type == String.class) return s;
        throw new RuntimeException("Unsupported parameter type: " + type);
    }
}
