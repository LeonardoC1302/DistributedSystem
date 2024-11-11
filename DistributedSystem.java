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

    public void start() {
        // Crear y asignar procesos
        Process process1 = new Process("Process-1", 10);
        Process process2 = new Process("Process-2", 15);
        Process process3 = new Process("Process-3", 8);
        processManager.assignProcess(process1);
        processManager.assignProcess(process2);
        processManager.assignProcess(process3);

        // Iniciar el balanceo de carga y detección de fallos
        Thread loadBalancerThread = new Thread(loadBalancer::monitorAndBalanceLoad);
        Thread faultToleranceThread = new Thread(faultTolerance::monitorNodes);
        loadBalancerThread.start();
        faultToleranceThread.start();

        // Esperar a que los procesos finalicen
        process1.execute();
        process2.execute();
        process3.execute();
    }

    public static void main(String[] args) {
        DistributedSystem system = new DistributedSystem(3, 2);
        system.start();
    }
}