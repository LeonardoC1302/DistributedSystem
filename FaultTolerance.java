import java.util.List;

public class FaultTolerance {
    private List<Node> nodes;
    private NodeCommunicator nodeCommunicator;

    public FaultTolerance(List<Node> nodes, NodeCommunicator nodeCommunicator) {
        this.nodes = nodes;
        this.nodeCommunicator = nodeCommunicator;
    }

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
        // Implementación de la lógica para comprobar si un nodo está activo
        // (p. ej., enviar un ping y esperar una respuesta)
        return true; // Simulando que todos los nodos están activos
    }

    private void handleNodeFailure(Node failedNode) {
        nodes.remove(failedNode);
        nodeCommunicator.handleNodeFailure(failedNode);
        System.out.println("Nodo " + failedNode.getId() + " ha fallado. Redistribuyendo procesos.");
    }
}