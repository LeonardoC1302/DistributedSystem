import java.util.List;

public class LoadBalancer {
    private List<Node> nodes;
    private ProcessManager processManager;
    private NodeCommunicator nodeCommunicator;

    public LoadBalancer(List<Node> nodes, ProcessManager processManager, NodeCommunicator nodeCommunicator) {
        this.nodes = nodes;
        this.processManager = processManager;
        this.nodeCommunicator = nodeCommunicator;
    }

    public void monitorAndBalanceLoad() {
        while (true) {
            checkNodeLoad();
            try {
                Thread.sleep(5000); // Revisamos la carga cada 5 segundos
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkNodeLoad() {
        for (Node node : nodes) {
            if (node.getCurrentProcesses() > node.getMaxProcesses() * 0.8) { // Umbral de 80% de capacidad
                redistributeProcesses(node);
            }
        }
    }

    private void redistributeProcesses(Node overloadedNode) {
        List<Process> processesToRedistribute = overloadedNode.getRunningProcesses();
        for (Process process : processesToRedistribute) {
            boolean reassigned = false;
            for (Node node : nodes) {
                if (node.getId() != overloadedNode.getId() && node.assignProcess(process)) {
                    overloadedNode.removeProcess(process);
                    reassigned = true;
                    break;
                }
            }
            if (!reassigned) {
                // No se pudo reasignar el proceso, quizás se deba notificar al usuario
            }
        }
    }
}