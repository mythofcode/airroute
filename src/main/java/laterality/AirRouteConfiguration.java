package laterality;

import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class AirRouteConfiguration {

    private final Map<String, Airport> nodeMap = new HashMap<>();
    MutableValueGraph<Airport, Integer> graph = ValueGraphBuilder.undirected().build();


    public AirRouteConfiguration() {
        readConfiguration();
    }

    public MutableValueGraph<Airport, Integer> getGraph() {
        return graph;
    }

    private void readConfiguration() {
        try {
            BufferedReader configReader = new BufferedReader(new InputStreamReader(AirRouteConfiguration.class.getResourceAsStream("/airroute.cfg")));

            while (configReader.ready()) {
                String configLine = configReader.readLine();
                String[] route = configLine.split("[ \t]*,[ \t]*");
                int cost = -1;
                if (route.length == 3) {
                    try {
                        cost = Integer.valueOf(route[2]);
                    } catch (NumberFormatException e) {
                        // fall through
                    }
                }
                if (cost < 0) {
                    System.err.println("Invalid line in configuration file airroute.cfg: " + configLine);
                    throw new RuntimeException("Configuration error");
                }
                addRoute(route[0], route[1], cost);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Cannot locate configuration file airroute.cfg");
            throw new RuntimeException(e);
        }
    }

    public Airport getNode(String name) {
        Airport airport = nodeMap.get(name);
        if (airport == null) {
            airport = new Airport(name);
            nodeMap.put(name, airport);
            graph.addNode(airport);
        }
        return airport;
    }

    private void addRoute(String from, String to, int cost) {
        Airport nFrom = getNode(from);
        Airport nTo = getNode(to);
        graph.putEdgeValue(nFrom, nTo, cost);
    }

}

