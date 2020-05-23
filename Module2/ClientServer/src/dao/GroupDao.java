package dao;

import java.util.ArrayList;
import java.util.List;

public interface GroupDao {
    List<Group> getAllGroups();
    Group getGroup(int groupId);
    void addGroup(Group group);
    void updateGroup(Group group);
    void deleteGroup(int groupId);
    void saveGroupsToDB(ArrayList<Group> groups);
}
