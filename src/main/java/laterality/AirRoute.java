package laterality;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AirRoute {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("usage: AirRoute <from> <to>\ne.g. java laterality.AirRoute DUB SYD\n  " + args.length);
            System.exit(1);
        }
        AirRouteConfiguration configuration = new AirRouteConfiguration();
        AirRouteFinder finder = new AirRouteFinder(configuration);

        List<Airport> route = finder.findRoute(args[0], args[1]);
        if (route.size() == 1) {
            route = Arrays.asList(route.get(0), route.get(0));
        }
        if (route.size() > 1) {
            int time = 0;
            Airport from = route.get(0);
            for (int i=1; i < route.size(); i++) {
                Airport to = route.get(i);
                Integer routeTime = configuration.getGraph().edgeValueOrDefault(from, to, 0);
                System.out.printf("%s -- %s ( %d )\n", from.getName(), to.getName(), routeTime);
                time += routeTime;
                from = to;
            }
            System.out.printf("time: %d\n", time);
        } else {
            throw new RuntimeException("Cannot route " + args[0] + " to " + args[1]);
        }

    }
}
