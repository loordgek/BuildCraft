package buildcraft.lib.misc.data;

import java.util.ArrayList;
import java.util.List;

import buildcraft.api.core.BCDebugging;
import buildcraft.api.core.BCLog;

public class IdAllocator {
    public static final boolean DEBUG = BCDebugging.shouldDebugLog("lib.id_alloc");

    private final IdAllocator parent;
    private final String name;
    private final List<String> idNameMap = new ArrayList<>();
    private boolean hasChildren;
    private int nextId = 0;

    private IdAllocator(IdAllocator parent, String name) {
        this.parent = parent;
        this.name = parent == null ? name : (parent.name + "." + name);
        if (parent != null) {
            idNameMap.addAll(parent.idNameMap);
        }
        nextId = parent == null ? 0 : parent.nextId;
    }

    public IdAllocator() {
        this(null, "unknown");
    }

    public IdAllocator(String name) {
        this(null, name);
    }

    public IdAllocator makeChild() {
        return makeChild("unknown");
    }

    public IdAllocator makeChild(String childName) {
        hasChildren = true;
        return new IdAllocator(this, childName);
    }

    public String getNameFor(int id) {
        if (id < 0) return "NEGATIVE ID " + id;
        if (id >= idNameMap.size()) return "UNKNOWN_CHILD " + id;
        return idNameMap.get(id);
    }

    public int allocId() {
        return allocId("ID");
    }

    public int allocId(String allocName) {
        if (hasChildren) {
            throw new IllegalStateException("A child of this object has already allocated ID's!"//
                + " You have probably set the calling class up wrong!");
        }
        if (DEBUG) {
            BCLog.logger.info("[lib.id_alloc] " + name + " allocated " + allocName + " as " + nextId);
        }
        idNameMap.add(allocName);
        return nextId++;
    }
}
