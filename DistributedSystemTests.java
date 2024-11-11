import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

class DistributedSystemTests {

    @Test
    void testProcessAssignment() {
        DistributedSystem system = new DistributedSystem(3, 2);
        Process process1 = new Process("Process-1", 10);
        Process process2 = new Process("Process-2", 15);
        Process process3 = new Process("Process-3", 8);

        system.processManager.assignProcess(process1);
        system.processManager.assignProcess(process2);
        system.processManager.assignProcess(process3);

        Assertions.assertNotNull(process1.getAssignedNode());
        Assertions.assertNotNull(process2.getAssignedNode());
        Assertions.assertNotNull(process3.getAssignedNode());
        Assertions.assertTrue(process1.getAssignedNode().getCurrentProcesses() <= process1.getAssignedNode().getMaxProcesses());
        Assertions.assertTrue(process2.getAssignedNode().getCurrentProcesses() <= process2.getAssignedNode().getMaxProcesses());
        Assertions.assertTrue(process3.getAssignedNode().getCurrentProcesses() <= process3.getAssignedNode().getMaxProcesses());
    }

    @Test
    void testResourceSynchronization() throws InterruptedException {
        DistributedSystem system = new DistributedSystem(3, 2);
        Node node1 = system.nodes.get(0);
        Node node2 = system.nodes.get(1);
        Resource resource = node1.getResource("Resource-A");

        CountDownLatch latch = new CountDownLatch(2);

        // Simulamos dos nodos intentando adquirir el mismo recurso
        new Thread(() -> {
            resource.acquireResource(node1);
            latch.countDown();
        }).start();

        new Thread(() -> {
            resource.acquireResource(node2);
            latch.countDown();
        }).start();

        latch.await(5, TimeUnit.SECONDS);

        Assertions.assertEquals(1, resource.getWaitingNodes().size());
    }

    @Test
    void testNodeFailover() {
        DistributedSystem system = new DistributedSystem(3, 2);
        Process process1 = new Process("Process-1", 10);
        Process process2 = new Process("Process-2", 15);
        Process process3 = new Process("Process-3", 8);

        system.processManager.assignProcess(process1);
        system.processManager.assignProcess(process2);
        system.processManager.assignProcess(process3);

        Node failedNode = process1.getAssignedNode();
        system.faultTolerance.handleNodeFailure(failedNode);

        Assertions.assertNull(process1.getAssignedNode());
        Assertions.assertNotNull(process2.getAssignedNode());
        Assertions.assertNotNull(process3.getAssignedNode());
    }

    @Test
    void testLoadBalancing() {
        DistributedSystem system = new DistributedSystem(3, 2);
        Process process1 = new Process("Process-1", 10);
        Process process2 = new Process("Process-2", 15);
        Process process3 = new Process("Process-3", 8);
        Process process4 = new Process("Process-4", 12);
        Process process5 = new Process("Process-5", 14);

        system.processManager.assignProcess(process1);
        system.processManager.assignProcess(process2);
        system.processManager.assignProcess(process3);
        system.processManager.assignProcess(process4);
        system.processManager.assignProcess(process5);

        // Esperamos a que el balanceador de carga redistribuya los procesos
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int maxProcessesPerNode = 0;
        for (Node node : system.nodes) {
            maxProcessesPerNode = Math.max(maxProcessesPerNode, node.getCurrentProcesses());
        }

        Assertions.assertTrue(maxProcessesPerNode <= system.nodes.get(0).getMaxProcesses() * 0.8);
    }
}