import java.util.ArrayList;
import java.util.List;


public class DistributedSystem {
    private List<Node> nodes;
    private ProcessManager processManager;
    private NodeCommunicator nodeCommunicator;
    private LoadBalancer loadBalancer;
    private FaultTolerance faultTolerance;

    public DistributedSystem(int numNodes, int maxProcessesPerNode) {
        nodes = new ArrayList<>();
        // Inicialización de nodos y recursos
        for (int i = 0; i < numNodes; i++) {
            Node node = new Node("Node-" + i, maxProcessesPerNode);
            nodes.add(node);
            node.addResource(new Resource("Resource-A"));
            node.addResource(new Resource("Resource-B"));
        }

        processManager = new ProcessManager(nodes);
        nodeCommunicator = new NodeCommunicator(nodes);
        loadBalancer = new LoadBalancer(nodes, processManager, nodeCommunicator);
        faultTolerance = new FaultTolerance(nodes, nodeCommunicator);
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void assignProcess(Process process) {
        processManager.assignProcess(process);
    }

    public void handleNodeFailure(Node node) {
        faultTolerance.handleNodeFailure(node);
    }

    public void executeProcess(Process process) {
        process.execute(processManager);
    }

    public void start() {
        // Crear y asignar procesos
        Process process1 = new Process("Process-1", 10);
        Process process2 = new Process("Process-2", 15);
        Process process3 = new Process("Process-3", 8);
        Process process4 = new Process("Process-4", 5);
        Process process5 = new Process("Process-5", 3);
        Process process6 = new Process("Process-6", 2);
        Process process7 = new Process("Process-7", 1);

        processManager.assignProcess(process1);
        processManager.assignProcess(process2);
        processManager.assignProcess(process3);
        processManager.assignProcess(process4);
        processManager.assignProcess(process5);
        processManager.assignProcess(process6);
        processManager.assignProcess(process7);

        // Iniciar el balanceo de carga y detección de fallos
        Thread loadBalancerThread = new Thread(loadBalancer::monitorAndBalanceLoad);
        Thread faultToleranceThread = new Thread(faultTolerance::monitorNodes);
        loadBalancerThread.start();
        faultToleranceThread.start();

        // Esperar a que los procesos se ejecuten
        process1.execute(processManager);
        process2.execute(processManager);
        process3.execute(processManager);
        process4.execute(processManager);
        process5.execute(processManager);
        process6.execute(processManager);
        process7.execute(processManager);
    }

    public static void main(String[] args) {
        DistributedSystem system = new DistributedSystem(3, 2);
        system.start();
        System.out.println("Distributed System finished");
    }
}