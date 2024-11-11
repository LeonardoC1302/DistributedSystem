import java.util.ArrayList;
import java.util.List;

public class Resource {
    private String name;
    private List<Node> waitingNodes;
    private Node currentOwner;

    public Resource(String name) {
        this.name = name;
        this.waitingNodes = new ArrayList<>();
        this.currentOwner = null;
    }

    public String getName() {
        return name;
    }

    public synchronized boolean acquireResource(Node node) {
        if (currentOwner == null) {
            currentOwner = node;
            return true;
        } else {
            waitingNodes.add(node);
            return false;
        }
    }

    public synchronized void releaseResource(Node node) {
        if (node == currentOwner) {
            currentOwner = null;
            if (!waitingNodes.isEmpty()) {
                Node nextNode = waitingNodes.remove(0);
                nextNode.acquireResource(this);
            }
        }
    }
}