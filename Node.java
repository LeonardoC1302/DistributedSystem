import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Node {
    private String id;
    private int maxProcesses;
    private int currentProcesses;
    private Map<String, Resource> resources;
    private List<Process> runningProcesses;
    private boolean active = true;

    public Node(String id, int maxProcesses) {
        this.id = id;
        this.maxProcesses = maxProcesses;
        this.currentProcesses = 0;
        this.resources = new HashMap<>();
        this.runningProcesses = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public int getMaxProcesses() {
        return maxProcesses;
    }

    public int getCurrentProcesses() {
        return currentProcesses;
    }

    public void addResource(Resource resource) {
        resources.put(resource.getName(), resource);
    }

    public Resource getResource(String name) {
        return resources.get(name);
    }

    public boolean assignProcess(Process process) {
        if (currentProcesses < maxProcesses) {
            runningProcesses.add(process);
            currentProcesses++;
            return true;
        }
        return false;
    }

    public boolean acquireResource(Resource resource) {
        if (resource.acquireResource(this)) {
            return true;
        } else {
            return false;
        }
    }

    public List<Process> getRunningProcesses() {
        return runningProcesses;
    }

    public HashMap<String, Resource> getResources() {
        return (HashMap<String, Resource>) resources;
    }

    public void removeProcess(Process process) {
        runningProcesses.remove(process);
        currentProcesses--;
    }

    public boolean getActive() {
        return active;
    }

    public boolean isProcessRunning(Process process) {
        if (runningProcesses == null || process == null) {
            return false;
        }
        return runningProcesses.contains(process);
    }
}