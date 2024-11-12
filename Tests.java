import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Tests {
    private void test_01(){
        DistributedSystem system = new DistributedSystem(3, 2);
        Process process1 = new Process("Process-1", 10);
        Process process2 = new Process("Process-2", 15);
        Process process3 = new Process("Process-3", 8);

        system.assignProcess(process1);
        system.assignProcess(process2);
        system.assignProcess(process3);

        Process process4 = new Process("Process-4", 5);
        system.assignProcess(process4);
        List<Node> nodes = system.getNodes();
        if(nodes.get(0).isProcessRunning(process1) &&
           nodes.get(1).isProcessRunning(process2) &&
           nodes.get(2).isProcessRunning(process3) &&
           nodes.get(0).isProcessRunning(process4)){
            System.out.println(">>> Prueba 1: Asignación de Procesos y Balanceo de Carga: PASSED"); 
        }
        else{
            System.out.println(">>> Prueba 1: Asignación de Procesos y Balanceo de Carga: FAILED"); 
        }
    }

    private void test_02() throws InterruptedException {
        DistributedSystem system = new DistributedSystem(3, 2);
        Process process1 = new Process("Process-1", 10);
        Process process2 = new Process("Process-2", 15);

        system.assignProcess(process1);
        system.assignProcess(process2);

        List<Node> nodes = system.getNodes();
        Node node1 = nodes.get(0);
        Node node2 = nodes.get(1);
        Resource resource = nodes.get(0).getResource("Resource-A");

        AtomicBoolean isWorking = new AtomicBoolean(false);

        CountDownLatch latch = new CountDownLatch(2);
        new Thread(() -> {
            resource.acquireResource(node1);
            latch.countDown();
        }).start();

        new Thread(() -> {
            resource.acquireResource(node2);
            isWorking.set(resource.getCurrentOwner().getId().equals("Node-0"));
            resource.releaseResource(node1);
            resource.releaseResource(node2);
            latch.countDown();
        }).start();

        latch.await(5, TimeUnit.SECONDS);

        if(isWorking.get()){
            System.out.println(">>> Prueba 2: Sincronización de Recursos Compartidos: PASSED"); 
        }
        else{
            System.out.println(">>> Prueba 2: Sincronización de Recursos Compartidos: FAILED"); 
        }
    }

    private void test_03(){
        DistributedSystem system = new DistributedSystem(3, 2);
        Process process1 = new Process("Process-1", 10);
        Process process2 = new Process("Process-2", 15);
        Process process3 = new Process("Process-3", 8);

        system.assignProcess(process1);
        system.assignProcess(process2);
        system.assignProcess(process3);

        Node failedNode = process1.getAssignedNode();
        system.handleNodeFailure(failedNode);
         if(system.getNodes().get(0).isProcessRunning(process1) && system.getNodes().get(0).getId().equals("Node-1")){
            System.out.println(">>> Prueba 3: Manejo de Fallos: PASSED"); 
        }
        else{
            System.out.println(">>> Prueba 3: Manejo de Fallos: FAILED"); 
        }

    }

    private void test_04(){
        DistributedSystem system = new DistributedSystem(3, 2);
        Process process1 = new Process("Process-1", 1);
        Process process2 = new Process("Process-2", 2);
        Process process3 = new Process("Process-3", 3);

        system.assignProcess(process1);
        system.assignProcess(process2);

        system.executeProcess(process1);

        system.assignProcess(process3);
        
        List<Node> nodes = system.getNodes();
        boolean isWorking = nodes.get(1).isProcessRunning(process2) && nodes.get(0).isProcessRunning(process3);

        system.executeProcess(process2);
        system.executeProcess(process3);

        if(isWorking){
            System.out.println(">>> Prueba 4: Escalabilidad del Sistema: PASSED"); 
        }
        else{
            System.out.println(">>> Prueba 4: Escalabilidad del Sistema: FAILED"); 
        }
    }

    private void test_05(){
        DistributedSystem system = new DistributedSystem(2, 2);

        Process process1 = new Process("Process-1", 1);
        Process process2 = new Process("Process-2", 2);
        Process process3 = new Process("Process-3", 3);
        Process process4 = new Process("Process-4", 1);
        Process process5 = new Process("Process-5", 2);

        system.assignProcess(process1);
        system.assignProcess(process2);
        system.assignProcess(process3);
        system.assignProcess(process4);
        system.assignProcess(process5);
        
        system.executeProcess(process1);
        boolean isWorking = system.getNodes().get(0).isProcessRunning(process5);

        system.executeProcess(process2);
        system.executeProcess(process3);
        system.executeProcess(process4);
        system.executeProcess(process5);

        if (isWorking){
            System.out.println(">>> Prueba 5: Redistribución Automática de Procesos: PASSED");
        }
        else{
            System.out.println(">>> Prueba 5: Redistribución Automática de Procesos: FAILED"); 
        }
    }

    public static void main(String[] args) {
        Tests test = new Tests();
        System.err.println("\nPrueba 1: Asignación de Procesos y Balanceo de Carga\n"); 
        test.test_01();

        System.err.println("\nPrueba 2: Sincronización de Recursos Compartidos\n");
        try {
            test.test_02();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.err.println("\nPrueba 3: Manejo de Fallos\n");
        test.test_03();
        
        System.out.println("\nPrueba 4: Escalabilidad del Sistema\n");
        test.test_04();

        System.out.println("\nPrueba 5: Redistribución Automática de Procesos\n");
        test.test_05();
    }
    
}
