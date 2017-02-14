package com.core.dbService.services;

import com.core.dbService.entities.Item;
import com.core.dbService.entities.Loan;
import com.core.dbService.entities.LoanUsers;
import com.core.dbService.entities.User;
import com.core.dbService.enums.LoanState;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by t.konst on 23.01.2017.
 */
public class LoanService extends DBService {
    private SessionFactory sessionFactory = super.getSessionFactory();

    public Loan getLoanById(Integer id) {
        Session session = sessionFactory.openSession();
        Loan loan = session.get(Loan.class, id);
        session.close();
        return loan;
    }

    public Integer addLoanForOneUser(Integer userId, Double sum) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        Integer id = -1;
        try {
            tx = session.beginTransaction();
            id = (Integer) session.save(new Loan(userId, sum, 1));
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

    public Integer addLoanAndUsers(List<User> users, Integer payerId, Double sum) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        Integer id = -1;
        int usersCount = users.size() + 1;
        for (User user : users) {
            if (Objects.equals(user.getUserId(), payerId)) {
                usersCount--;
                break;
            }
        }
        Double share = sum / usersCount;
        try {
            tx = session.beginTransaction();
            id = (Integer) session.save(new Loan(payerId, sum, usersCount));
            session.save(new LoanUsers(payerId, id, LoanState.PAID, share, 1));
            User payer = session.load(User.class, payerId);
            payer.setDebt(payer.getDebt() + sum);
            for (User user : users) {
                if (Objects.equals(user.getUserId(), payerId))
                    continue;
                session.save(new LoanUsers(user.getUserId(), id, LoanState.NOT_PAID, share, 0));
                User loanUser = session.load(User.class, user.getUserId());
                loanUser.setDebt(loanUser.getDebt() - share);
                session.flush();
            }
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

    public boolean checkLoanExists(Integer groupId) {
        return getLoanById(groupId) != null;
    }

    public User getLoanPayer(Integer loanId) {
        UserService userService = new UserService();
        Session session = sessionFactory.openSession();
        Loan loan = getLoanById(loanId);
        if (loan == null)
            return null;
        User payer = userService.getUserById(loan.getPayer());
        session.close();
        return payer;
    }

    public List<User> getLoanUsers(Integer id) throws HibernateException {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(LoanUsers.class);
        List<LoanUsers> loanUsersList = (List<LoanUsers>) criteria.add(Restrictions.eq("loan_id", id)).list();
        List<Integer> usersIDs = new ArrayList<Integer>() {
        };
        for (LoanUsers user : loanUsersList) {
            usersIDs.add(user.getUserId());
        }
        List<User> users = null;
        if (usersIDs != null && !usersIDs.isEmpty()) {
            Criteria usersCriteria = session.createCriteria(User.class);
            users = (List<User>) usersCriteria.add(Restrictions.in("user_id", usersIDs)).list();
        }
        session.close();
        return users;
    }

    public boolean changeState(Integer state, Integer loanId) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        if (!checkLoanExists(loanId))
            return false;
        if (state != LoanState.NOT_PAID || state != LoanState.WAITING_CONFIRMATION || state != LoanState.PAID)
            return false;
        try {
            tx = session.beginTransaction();
            Loan loan = session.load(Loan.class, loanId);
            loan.setLoanState(state);
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

    public boolean deleteLoan(Loan loan) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(loan);
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

    //todo: сделать public, когда будет реализована функциональность item
    private List<Item> getLoanItems(Integer loanId) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Item.class);
        return (List<Item>) criteria.add(Restrictions.eq("item_loan", loanId)).list();
    }

    //todo: определить, нужен ли такой функционал, добавить изменение debt и состояние долга
    private boolean changeSum(Double sum, Integer loanId) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        if (!checkLoanExists(loanId))
            return false;
        try {
            tx = session.beginTransaction();
            Loan loan = session.load(Loan.class, loanId);
            loan.setLoanSum(sum);
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

    //todo: реализовать добавление юзера к чеку после добавления чека, добавить изменение debt и состояние долга
    private boolean addLoanUser(Integer userId, Integer loanId) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(new LoanUsers(userId, loanId, 0, 0));
            Loan loan = session.load(Loan.class, loanId);
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

    //todo: расширить функциональность для добавления информации о товарах при добавлении чека
    private Integer addLoanExtended(Integer userId) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        Integer id = -1;
        try {
            tx = session.beginTransaction();
            id = (Integer) session.save(new Loan(userId));
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
}
