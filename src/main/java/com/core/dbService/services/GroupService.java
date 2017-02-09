package com.core.dbService.services;

import com.core.dbService.entities.*;
import com.core.dbService.enums.GroupType;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by t.konst on 22.01.2017.
 */
public class GroupService extends DBService {

    private SessionFactory sessionFactory = super.getSessionFactory();

    public Integer addGroup(String name, String type) throws HibernateException {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        Integer id = -1;
        try {
            tx = session.beginTransaction();
            try {
                GroupType.valueOf(type);
            } catch (IllegalArgumentException e) {
                type = GroupType.OTHER.name();
            }
            id = (Integer) session.save(new Group(name, type));
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
        } finally {
            session.close();
            return id;
        }
    }

    public Group getGroupById(Integer id) throws HibernateException {
        Session session = sessionFactory.openSession();
        Group group = session.get(Group.class, id);
        session.close();
        return group;
    }

    public Group getGroupByName(String name) throws HibernateException {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Group.class);
        return (Group) criteria.add(Restrictions.eq("group_name", name)).uniqueResult();
    }

    public boolean checkGroupExists(String name) {
        return getGroupByName(name) != null;
    }

    public boolean checkGroupExists(Integer groupId) {
        return getGroupById(groupId) != null;
    }


    public boolean changeType(String type, String groupName) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        if (!checkGroupExists(groupName))
            return false;
        try {
            tx = session.beginTransaction();
            Group group = session.load(Group.class, getGroupByName(groupName).getGroupId());
            group.setGroupType(type);
            session.flush();
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            return false;
        } finally {
            session.close();
        }
    }

    public Integer addGroupUser(Integer userId, Integer groupId) throws HibernateException {
        Session session = sessionFactory.openSession();
        UserService userService = new UserService();
        Transaction tx = null;
        Integer id = -1;
        try {
            tx = session.beginTransaction();
            if (!userService.checkUserExists(userId))
                throw new HibernateException("No such user.");
            if (!checkGroupExists(groupId))
                throw new HibernateException("No such group.");
            id = (Integer) session.save(new UsersGroups(userId, groupId));
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            id = -1;
        } finally {
            session.close();
            return id;
        }
    }

    public Integer addGroupUser(String login, String groupName) throws HibernateException {
        Session session = sessionFactory.openSession();
        UserService userService = new UserService();
        Transaction tx = null;
        Integer id = -1;
        try {
            tx = session.beginTransaction();
            if (!userService.checkUserExists(login))
                throw new HibernateException("No such user.");
            if (!checkGroupExists(groupName))
                throw new HibernateException("No such group.");
            id = (Integer) session.save(new UsersGroups(userService.getIdByName(login), getGroupByName(groupName).getGroupId()));
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            id = -1;
        } finally {
            session.close();
            return id;
        }
    }

    public List<Integer> addGroupUsers(List<User> users, Integer groupId) {
        List<Integer> ids = new ArrayList<Integer>();
        for (User user : users) {
            ids.add(addGroupUser(user.getUserId(), groupId));
        }
        return ids;
    }

    public List<User> getGroupUsers(Integer id) throws HibernateException {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(UsersGroups.class);
        List<UsersGroups> usersGroupsList = (List<UsersGroups>) criteria.add(Restrictions.eq("group_id", id)).list();
        List<Integer> usersIDs = new ArrayList<Integer>() {
        };
        for (UsersGroups ug : usersGroupsList) {
            usersIDs.add(ug.getUserId());
        }
        List<User> users = null;
        if (usersIDs != null && !usersIDs.isEmpty()) {
            Criteria usersCriteria = session.createCriteria(User.class);
            users = (List<User>) usersCriteria.add(Restrictions.in("user_id", usersIDs)).list();
        }
        session.close();
        return users;
    }

    public boolean deleteGroup(Group group) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(group);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            return false;
        } finally {
            session.close();
        }
    }
}
