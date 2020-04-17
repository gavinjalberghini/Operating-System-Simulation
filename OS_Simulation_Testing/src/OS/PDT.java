package OS;

import java.util.ArrayList;

public class PDT {

    //TRACKING
    ArrayList<Integer> childrenPids = new ArrayList<>();
    int parentPid = -1;

    //STATUS
    boolean parent = false;
    boolean child = false;

    public PDT(){

    }

    public boolean isChild() {
        return child;
    }

    public boolean isParent() {
        return parent;
    }

    public void setIsChild(boolean child) {
        this.child = child;
    }

    public void setIsParent(boolean parent) {
        this.parent = parent;
    }

    public int getParentPid() {
        return parentPid;
    }

    public void setParentPid(int parentPid) {
        this.parentPid = parentPid;
    }

    public void addChildToPDT(int childPid){
        this.childrenPids.add(childPid);
    }

    public ArrayList<Integer> getChildrenPids() {
        return childrenPids;
    }

    public void removeChildFromPDT(int childPid){
        this.childrenPids.remove(Integer.valueOf(childPid));
    }
}
