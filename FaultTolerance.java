import java.util.List;

public class FaultTolerance {
    private List<Node> nodes;
    private NodeCommunicator nodeCommunicator;

    public FaultTolerance(List<Node> nodes, NodeCommunicator nodeCommunicator) {
        this.nodes = nodes;
        this.nodeCommunicator = nodeCommunicator;
    }

    // 
    public void monitorNodes() {
        while (true) {
            checkNodeStatus();
            try {
                Thread.sleep(2000); // Revisamos el estado de los nodos cada 2 segundos
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkNodeStatus() {
        for (Node node : nodes) {
            if (!isNodeActive(node)) {
                handleNodeFailure(node);
            }
        }
    }

    private boolean isNodeActive(Node node) {
        return node.getActive();
    }

    public void handleNodeFailure(Node failedNode) {
        nodes.remove(failedNode);
        System.out.println("Nodo " + failedNode.getId() + " ha fallado. Redistribuyendo procesos.");
        nodeCommunicator.handleNodeFailure(failedNode);
    }
}