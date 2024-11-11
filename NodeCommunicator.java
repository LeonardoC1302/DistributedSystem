import java.util.List;

public class NodeCommunicator {
    private List<Node> nodes;

    public NodeCommunicator(List<Node> nodes) {
        this.nodes = nodes;
    }

    public void broadcastResourceStatus() {
        for (Node node : nodes) {
            for (Resource resource : node.getResources().values()) {
                broadcastResourceStatus(node, resource);
            }
        }
    }

    private synchronized void broadcastResourceStatus(Node node, Resource resource) {
        for (Node otherNode : nodes) {
            if (otherNode != node) {
                otherNode.getResource(resource.getName()).acquireResource(node);
            }
        }
    }

    public void handleNodeFailure(Node failedNode) {
        for (Process process : failedNode.getRunningProcesses()) {
            reassignProcess(process);
        }
    }

    private void reassignProcess(Process process) {
        Node newNode = findLeastLoadedNode();
        if (newNode.assignProcess(process)) {
            process.setAssignedNode(newNode);
            System.out.println("Proceso " + process.getId() + " reasignado al nodo " + newNode.getId());
        } else {
            System.out.println("No se pudo reasignar el proceso " + process.getId());
        }
    }

    private Node findLeastLoadedNode() {
        // Implementación similar a la del ProcessManager
        return new Node(null, 0);
    }
}