import java.util.List;

public class ProcessManager {
    private List<Node> nodes;

    public ProcessManager(List<Node> nodes) {
        this.nodes = nodes;
    }

    public void assignProcess(Process process) {
        Node leastLoadedNode = findLeastLoadedNode();
        if (leastLoadedNode.assignProcess(process)) {
            process.setAssignedNode(leastLoadedNode);
            System.out.println("Proceso " + process.getId() + " asignado al nodo " + leastLoadedNode.getId());
        } else {
            System.out.println("No se pudo asignar el proceso " + process.getId() + " a ningún nodo");
        }
    }

    private Node findLeastLoadedNode() {
        Node leastLoadedNode = null;
        int minProcesses = Integer.MAX_VALUE;
        for (Node node : nodes) {
            if (node.getCurrentProcesses() < minProcesses) {
                leastLoadedNode = node;
                minProcesses = node.getCurrentProcesses();
            }
        }
        return leastLoadedNode;
    }
}