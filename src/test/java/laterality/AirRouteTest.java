package laterality;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class AirRouteTest {

    @Test
    public void simpleRoute() {
        route("DUB", "LHR", "simple");
    }

    @Test
    public void sampleRoute() {
        route("DUB", "SYD", "sample");
    }

    @Test
    public void otherRoute() {
        route("DUB", "LAX", "other");
    }


    @Test
    public void selfRoute() {
        route("DUB", "DUB", "self");
    }

    @Test
    public void thereAndBackAgain() {
        route("SYD", "DUB", "thereAndBackAgain");
    }

    private void route(String from, String to, String name) {
        if (name != null && name.length() > 0)
            System.out.println(name);
        AirRoute.main(Arrays.asList(from, to).toArray(new String[2]));
        System.out.println();
    }
}
