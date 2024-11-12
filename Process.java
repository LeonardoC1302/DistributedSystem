public class Process {
    private String id;
    private int duration;
    private Node assignedNode;

    public Process(String id, int duration) {
        this.id = id;
        this.duration = duration;
        this.assignedNode = null;
    }

    public String getId() {
        return id;
    }

    public int getDuration() {
        return duration;
    }

    public Node getAssignedNode() {
        return assignedNode;
    }

    public void setAssignedNode(Node node) {
        this.assignedNode = node;
    }

    public void execute(ProcessManager processManager) {
        System.out.println("Ejecutando proceso " + id + " en el nodo " + assignedNode.getId());
        try {
            Thread.sleep(duration * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        processManager.endProcess(this);
        processManager.reassignWaitingProcesses();
    }
}