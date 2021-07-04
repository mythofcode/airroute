package laterality;

import eu.happycoders.pathfinding.dijkstra.DijkstraWithPriorityQueue;

import java.util.List;

public class AirRouteFinder {

    private final AirRouteConfiguration c;

    public AirRouteFinder(AirRouteConfiguration airRouteConfiguration) {
       c = airRouteConfiguration;
    }

    public List<Airport> findRoute(String from, String to) {
        Airport nFrom = c.getNode(from);
        Airport nTo = c.getNode(to);

        List<Airport> shortestPath = DijkstraWithPriorityQueue.findShortestPath(c.getGraph(), nFrom, nTo);
        return shortestPath;
    }
}
