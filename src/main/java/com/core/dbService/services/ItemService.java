package com.core.dbService.services;

import com.core.dbService.entities.Item;
import com.core.dbService.enums.ItemCategory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Created by t.konst on 22.01.2017.
 */
public class ItemService extends DBService {

    SessionFactory sessionFactory = super.getSessionFactory();

    public Item getItemById(Integer id) {
        Session session = sessionFactory.openSession();
        Item item = session.get(Item.class, id);
        session.close();
        return item;
    }

    public Integer addItem(Integer loanId, String itemName, Double itemCost, String itemCategory) throws HibernateException {
        if (itemCategory == null) {
            itemCategory = ItemCategory.OTHER.name();
        }
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Integer id = insertItem(loanId, itemName, itemCost, itemCategory);
        transaction.commit();
        session.close();
        return id;
    }

    public Item getById(Integer id) {
        Session session = sessionFactory.openSession();
        return session.get(Item.class, id);
    }

    public Integer insertItem(Integer loanId, String itemName, Double itemCost, String itemCategory) {
        Session session = sessionFactory.openSession();
        return (Integer) session.save(new Item(loanId, itemName, itemCost, itemCategory));
    }

    public Integer insertItem(String itemName, Double itemCost, String itemCategory) {
        Session session = sessionFactory.openSession();
        return (Integer) session.save(new Item(itemName, itemCost, itemCategory));
    }
}
