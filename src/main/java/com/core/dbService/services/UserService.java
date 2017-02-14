package com.core.dbService.services;

import com.core.dbService.entities.*;
import org.hibernate.*;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by t.konst on 21.01.2017.
 */
public class UserService extends DBService {

    private SessionFactory sessionFactory = super.getSessionFactory();

    public Integer addUser(String login, String pass, Double debt) throws HibernateException {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        Integer id = -1;
        try {
            tx = session.beginTransaction();
            id = (Integer) session.save(new User(login, pass, debt));
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            id = -1;
        } finally {
            session.close();
            return id;
        }
    }

    public User getUserById(Integer id) throws HibernateException {
        Session session = sessionFactory.openSession();
        User user = session.get(User.class, id);
        session.close();
        return user;
    }

    public User getUserByLogin(String login) throws HibernateException {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(User.class);
        return (User) criteria.add(Restrictions.eq("user_login", login)).uniqueResult();
    }

    public Integer getIdByName(String login) throws HibernateException {
        return getUserByLogin(login).getUserId();
    }

    public boolean checkUserExists(String login) {
        return getUserByLogin(login) != null;
    }

    boolean checkUserExists(Integer id) {
        Session session = sessionFactory.openSession();
        return session.get(User.class, id) != null;
    }

    public String getPassword(String login) throws HibernateException {
        return getUserByLogin(login).getPassword();
    }

    public List<Group> getUserGroups(Integer userId) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(UsersGroups.class);
        List<UsersGroups> usersGroupsList = criteria.add(Restrictions.eq("user_id", userId)).list();
        List<Integer> groupsIDs = new ArrayList<Integer>();
        for (UsersGroups ug : usersGroupsList) {
            groupsIDs.add(ug.getGroupId());
        }
        Criteria usersCriteria = session.createCriteria(Group.class);
        List<Group> groups = (List<Group>) usersCriteria.add(Restrictions.in("group_id", groupsIDs)).list();
        session.close();
        return groups;
    }

    public List<Loan> getUserLoans(Integer userId) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(LoanUsers.class);
        List<LoanUsers> loanUsersList = criteria.add(Restrictions.eq("user_id", userId)).list();
        List<Integer> loansIDs = new ArrayList<Integer>() {
        };
        for (LoanUsers loanUsers : loanUsersList) {
            loansIDs.add(loanUsers.getLoanId());
        }
        Criteria usersCriteria = session.createCriteria(Loan.class);
        List<Loan> loans = (List<Loan>) usersCriteria.add(Restrictions.in("loan_id", loansIDs)).list();
        session.close();
        return loans;
    }

    public List<Loan> getUserPayments(Integer userId) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Loan.class);
        List<Loan> payments = criteria.add(Restrictions.eq("loan_payer", userId)).list();
        session.close();
        return payments;
    }

    public boolean deleteUser(User user) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(user);
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
