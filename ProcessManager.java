import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ProcessManager {
    private List<Node> nodes;
    private Queue<Process> waitingQueue; 

    public ProcessManager(List<Node> nodes) {
        this.nodes = nodes;
        this.waitingQueue = new LinkedList<>();
    }

    public void assignProcess(Process process) {
        Node leastLoadedNode = findLeastLoadedNode();
        if (leastLoadedNode.assignProcess(process)) {
            process.setAssignedNode(leastLoadedNode);
            System.out.println("Proceso " + process.getId() + " asignado al nodo " + leastLoadedNode.getId());
        } else {
            waitingQueue.add(process);
            System.out.println("Proceso " + process.getId() + " añadido a la cola de espera");
        }
    }

    public void reassignWaitingProcesses() {
        Queue<Process> tempQueue = new LinkedList<>();
        while (!waitingQueue.isEmpty()) {
            Process process = waitingQueue.poll();
            Node leastLoadedNode = findLeastLoadedNode();
            
            if (leastLoadedNode != null && leastLoadedNode.assignProcess(process)) {
                process.setAssignedNode(leastLoadedNode);
                System.out.println("Proceso " + process.getId() + " reasignado al nodo " + leastLoadedNode.getId());
            } else {
                tempQueue.add(process); // Si aún no hay nodos disponibles, el proceso permanece en la cola
            }
        }
        waitingQueue = tempQueue; // Actualiza la cola con los procesos que no se pudieron asignar
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

    public Node endProcess(Process process) {
        Node node = process.getAssignedNode();
        node.removeProcess(process);
        System.err.println("Proceso " + process.getId() + " finalizado en el nodo " + node.getId());
        return node;
    }
}