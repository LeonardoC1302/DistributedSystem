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

    public Node getCurrentOwner(){
        return currentOwner;
    }

    public synchronized boolean acquireResource(Node node) {
        if (currentOwner == null) {
            System.out.println("Nodo " + node.getId() + " adquiere el recurso " + name);
            currentOwner = node;
            return true;
        } else {
            System.out.println("Nodo " + node.getId() + " en espera para adquirir el recurso " + name);
            waitingNodes.add(node);
            return false;
        }
    }

    public synchronized void releaseResource(Node node) {
        if (node == currentOwner) {
            System.out.println("Nodo " + node.getId() + " libera el recurso " + name);
            currentOwner = null;
            if (!waitingNodes.isEmpty()) {
                Node nextNode = waitingNodes.remove(0);
                nextNode.acquireResource(this);
            }
        }
    }
}